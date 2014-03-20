package com.lwolf.scud.persistence.jdbc;

import java.io.BufferedInputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.scud.exceptions.ResourceException;
import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.scud.persistence.Entity;

public abstract class JdbcDataAccessObject<T extends Entity> {
	
	protected DataSourceLocator dataSourceLocator;
	protected IdLocator idLocator;
	
	public JdbcDataAccessObject(DataSourceLocator dataSourceLocator, IdLocator idLocator) {
		this.dataSourceLocator = dataSourceLocator;
		this.idLocator = idLocator;
	}
	
	protected Connection getConnection() throws ResourceException, SQLException {
		return dataSourceLocator.getDataSource().getConnection();
	}
	
	protected void closeConnection(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException ex) {}
		try {
			if (ps != null)
				ps.close();
		} catch (SQLException ex) {}
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException ex) {}
	}

	public Integer create(T entity) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			
			Class<? extends Entity> clazz = entity.getClass();
			
			String tableName = getTableName(clazz);
			Map<String, Field> columns = getColumns(clazz);
			Map<String, Field> idColumns = getIdColumns(clazz);
			
			if (columns == null || columns.isEmpty()) {
				throw new DataException("No @Column annotations found for class: " + clazz.toString());
			}
			if (idColumns == null || idColumns.isEmpty()) {
				throw new DataException("No @Id annotations found for class: " + clazz.toString());
			}
			
			// set the id
			Integer id = null;
			for (Map.Entry<String, Field> entry : idColumns.entrySet()) {
				if (entry.getValue().isAnnotationPresent(GeneratedValue.class)) {
					id = idLocator.id(tableName);
					entry.getValue().set(entity, id);
				}
			}
			
			// get column names and values
			StringBuilder columnNames = new StringBuilder();
			StringBuilder values = new StringBuilder();
			
			for (Map.Entry<String, Field> entry : columns.entrySet()) {
				if (columnNames.length() > 0)
					columnNames.append(",");
				
				if (values.length() > 0)
					values.append(",");
				
				columnNames.append(entry.getKey());
				values.append("?");
			}
			
			// build sql statement
			StringBuilder sql = new StringBuilder("insert into ");
			sql.append(tableName);
			sql.append("(");
			sql.append(columnNames);
			sql.append(") values (");
			sql.append(values);
			sql.append(")");
			
			// execute statement
			conn = getConnection();
			ps = conn.prepareStatement(sql.toString());
			
			int i = 1;
			for (Map.Entry<String, Field> entry : columns.entrySet()) {
				ps.setObject(i, entry.getValue().get(entity));
				i++;
			}
			
			ps.execute();
			
			return id;
			
		} catch (IllegalAccessException ex) {
			throw new DataException(ex);
		} catch (ResourceException ex) {
			throw new DataException(ex);
		} catch (SQLException ex) {
			throw new DataException(ex);
		} finally {
			closeConnection(conn, ps, null);
		}
	}
	
	public void read(T entity) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			Class<? extends Entity> clazz = entity.getClass();
			
			String tableName = getTableName(clazz);
			Map<String, Field> columns = getColumns(clazz);
			Map<String, Field> idColumns = getIdColumns(clazz);
			
			if (columns == null || columns.isEmpty()) {
				throw new DataException("No @Column annotations found for class: " + clazz.toString());
			}
			if (idColumns == null || idColumns.isEmpty()) {
				throw new DataException("No @Id annotations found for class: " + clazz.toString());
			}
			
			// get column names
			StringBuilder columnNames = new StringBuilder();
			
			for (Map.Entry<String, Field> entry : columns.entrySet()) {
				if (!isLazy(entry.getValue())) {
					if (columnNames.length() > 0)
						columnNames.append(",");
					
					columnNames.append(entry.getKey());
				}
			}
			
			// build where clause
			StringBuilder where = new StringBuilder();
			
			for (Map.Entry<String, Field> entry : idColumns.entrySet()) {
				if (where.length() > 0)
					where.append(" and ");
				
				where.append(entry.getKey());
				where.append("=?");
			}
			
			// build sql statement
			StringBuilder sql = new StringBuilder("select ");
			sql.append(columnNames);
			sql.append(" from ");
			sql.append(tableName);
			sql.append(" where ");
			sql.append(where);
			
			// execute
			conn = getConnection();
			ps = conn.prepareStatement(sql.toString());
			
			int i = 1;
			for (Map.Entry<String, Field> entry : idColumns.entrySet()) {
				ps.setObject(i, entry.getValue().get(entity));
				i++;
			}
			
			rs = ps.executeQuery();
			
			// populate entity
			if (rs.next()) {
				for (Map.Entry<String, Field> entry : columns.entrySet()) {
					if (!isLazy(entry.getValue()) && !isLob(entry.getValue())) {
						entry.getValue().set(entity, rs.getObject(entry.getKey()));
					} else if (!isLazy(entry.getValue()) && isLob(entry.getValue())) {
						entry.getValue().set(entity, new BufferedInputStream(rs.getBinaryStream(entry.getKey())));
					}
				}
			}
			
		} catch (SQLException ex) {
			throw new DataException(ex);
		} catch (ResourceException ex) {
			throw new DataException(ex);
		} catch (IllegalAccessException ex) {
			throw new DataException(ex);
		} finally {
			closeConnection(conn, ps, rs);
		}
	}
	
	public void update(T entity) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			
			Class<? extends Entity> clazz = entity.getClass();
			
			String tableName = getTableName(clazz);
			Map<String, Field> columns = getColumns(clazz);
			Map<String, Field> idColumns = getIdColumns(clazz);
			
			if (columns == null || columns.isEmpty()) {
				throw new DataException("No @Column annotations found for class: " + clazz.toString());
			}
			if (idColumns == null || idColumns.isEmpty()) {
				throw new DataException("No @Id annotations found for class: " + clazz.toString());
			}
			
			// build set clause
			StringBuilder set = new StringBuilder();
			
			for (Map.Entry<String, Field> entry : columns.entrySet()) {
				if (!isLob(entry.getValue()) || entry.getValue().get(entity) != null) {
					if (set.length() > 0)
						set.append(",");
					
					set.append(entry.getKey());
					set.append("=?");
				}
			}
			
			// build where clause
			StringBuilder where = new StringBuilder();
			
			for (Map.Entry<String, Field> entry : idColumns.entrySet()) {
				if (where.length() > 0)
					where.append(" and ");
				
				where.append(entry.getKey());
				where.append("=?");
			}
			
			// build sql statement
			StringBuilder sql = new StringBuilder("update ");
			sql.append(tableName);
			sql.append(" set ");
			sql.append(set);
			sql.append(" where ");
			sql.append(where);
			
			// execute statement
			conn = getConnection();
			ps = conn.prepareStatement(sql.toString());
			
			int i = 1;
			for (Map.Entry<String, Field> entry : columns.entrySet()) {
				if (!isLob(entry.getValue()) || entry.getValue().get(entity) != null) {
					ps.setObject(i, entry.getValue().get(entity));
					i++;
				}
			}
			for (Map.Entry<String, Field> entry : idColumns.entrySet()) {
				ps.setObject(i, entry.getValue().get(entity));
				i++;
			}
			
			ps.execute();
			
		} catch (IllegalAccessException ex) {
			throw new DataException(ex);
		} catch (ResourceException ex) {
			throw new DataException(ex);
		} catch (SQLException ex) {
			throw new DataException(ex);
		} finally {
			closeConnection(conn, ps, null);
		}
	}
	
	public void delete(T entity) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			
			Class<? extends Entity> clazz = entity.getClass();
			
			String tableName = getTableName(clazz);
			Map<String, Field> idColumns = getIdColumns(clazz);
			
			if (idColumns == null || idColumns.isEmpty()) {
				throw new DataException("No @Id annotations found for class: " + clazz.toString());
			}
			
			// build where clause
			StringBuilder where = new StringBuilder();
			
			for (Map.Entry<String, Field> entry : idColumns.entrySet()) {
				if (where.length() > 0)
					where.append(" and ");
				
				where.append(entry.getKey());
				where.append("=?");
			}
			
			// build sql statement
			StringBuilder sql = new StringBuilder("delete from ");
			sql.append(tableName);
			sql.append(" where ");
			sql.append(where);
			
			// execute statement
			conn = getConnection();
			ps = conn.prepareStatement(sql.toString());
			
			int i = 1;
			for (Map.Entry<String, Field> entry : idColumns.entrySet()) {
				ps.setObject(i, entry.getValue().get(entity));
				i++;
			}
			
			ps.execute();
			
		} catch (IllegalAccessException ex) {
			throw new DataException(ex);
		} catch (ResourceException ex) {
			throw new DataException(ex);
		} catch (SQLException ex) {
			throw new DataException(ex);
		} finally {
			closeConnection(conn, ps, null);
		}
	}
	
	public List<T> findAll(Class<T> clazz) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String tableName = getTableName(clazz);
			Map<String, Field> columns = getColumns(clazz);
			
			if (columns == null || columns.isEmpty()) {
				throw new DataException("No @Column annotations found for class: " + clazz.toString());
			}
			
			// get column names
			StringBuilder columnNames = new StringBuilder();
			
			for (Map.Entry<String, Field> entry : columns.entrySet()) {
				if (!isLazy(entry.getValue()) && !isLob(entry.getValue())) {
					if (columnNames.length() > 0)
						columnNames.append(",");
					
					columnNames.append(entry.getKey());
				}
			}
			
			// get order clause
			String order = getOrder(clazz);
			
			// build sql statement
			StringBuilder sql = new StringBuilder("select ");
			sql.append(columnNames);
			sql.append(" from ");
			sql.append(tableName);
			if (order != null)
				sql.append(" order by ").append(order);
			
			// execute statement
			conn = getConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			
			if (rs.next()) {
				List<T> list = new ArrayList<T>();
				do {
					T t = clazz.newInstance();
					
					for (Map.Entry<String, Field> entry : columns.entrySet()) {
						if (!isLazy(entry.getValue()) && !isLob(entry.getValue())) {
							entry.getValue().set(t, rs.getObject(entry.getKey()));
						}
					}
					
					list.add(t);
				} while (rs.next());
				
				return list;
			} else {
				return null;
			}
			
		} catch (SQLException ex) {
			throw new DataException(ex);
		} catch (ResourceException ex) {
			throw new DataException(ex);
		} catch (IllegalAccessException ex) {
			throw new DataException(ex);
		} catch (InstantiationException ex) {
			throw new DataException(ex);
		} finally {
			closeConnection(conn, ps, rs);
		}
	}
	
	private String getTableName(Class<? extends Entity> clazz) throws DataException {
		// check for table annotation
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new DataException("@Table annotation is missing from Entity");
		}
		
		// check for table name
		Table table = clazz.getAnnotation(Table.class);
		if (table.name() == null || table.name().equals("")) {
			throw new DataException("No table name defined on @Table annotation for Entity");
		}
		
		return table.name();
	}
	
	private Map<String, Field> getColumns(Class<? extends Entity> clazz) throws IllegalAccessException {
		HashMap<String, Field> columns = null;
		
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			columns = new HashMap<String, Field>();
			
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					columns.put(field.getAnnotation(Column.class).name(), field);
				}
			}
			
			if (columns.isEmpty())
				columns = null;
		}
		
		return columns;
	}
	
	private Map<String, Field> getIdColumns(Class<? extends Entity> clazz) throws IllegalAccessException {
		HashMap<String, Field> columns = null;
		
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			columns = new HashMap<String, Field>();
			
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class) && field.isAnnotationPresent(Id.class)) {
					columns.put(field.getAnnotation(Column.class).name(), field);
				}
			}
			
			if (columns.isEmpty())
				columns = null;
		}
		
		return columns;
	}
	
	private String getOrder(Class<? extends Entity> clazz) throws IllegalAccessException {
		StringBuilder order = new StringBuilder();
		
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(OrderBy.class) && field.isAnnotationPresent(Column.class)) {
					String scend = field.getAnnotation(OrderBy.class).value();
					if (scend == null || scend.equals(""))
						scend = "asc";
					
					if (order.length() > 0)
						order.append(",");
					
					order.append(field.getAnnotation(Column.class).name());
					order.append(" ");
					order.append(scend);
				}
			}
		}
		
		if (order.length() > 0)
			return order.toString();
		else
			return null;
	}
	
	private boolean isLazy(Field field) {
		return
			field.isAnnotationPresent(Basic.class) &&
			field.getAnnotation(Basic.class).fetch() == FetchType.LAZY;
	}
	
	private boolean isLob(Field field) {
		return
			field.isAnnotationPresent(Lob.class);
	}
}
