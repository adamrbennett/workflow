package com.lwolf.wf.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.scud.exceptions.ResourceException;
import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.scud.persistence.jdbc.JdbcDataAccessObject;
import com.lwolf.wf.persistence.ProcessDao;
import com.lwolf.wf.persistence.entities.ProcessEntity;

public class JdbcProcessDao extends JdbcDataAccessObject<ProcessEntity> implements ProcessDao {
	
	public JdbcProcessDao(DataSourceLocator dataSourceLocator, IdLocator idLocator) {
		super(dataSourceLocator, idLocator);
	}

	@Override
	public Integer create(ProcessEntity entity) throws DataException {
		return super.create(entity);
	}

	@Override
	public void delete(ProcessEntity entity) throws DataException {
		super.delete(entity);
	}

	@Override
	public void read(ProcessEntity entity) throws DataException {
		super.read(entity);
	}

	@Override
	public void update(ProcessEntity entity) throws DataException {
		super.update(entity);
	}

	@Override
	public List<ProcessEntity> findAll() throws DataException {
		return super.findAll(ProcessEntity.class);
	}
	
	@Override
	public List<ProcessEntity> findByModel(ProcessEntity entity) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement("select process_id, name from process where model_id = ?");
			ps.setInt(1, entity.modelId);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				List<ProcessEntity> list = new ArrayList<ProcessEntity>();
				do {
					ProcessEntity process = new ProcessEntity();
					process.modelId = entity.modelId;
					process.processId = rs.getInt("process_id");
					process.name = rs.getString("name");
					
					list.add(process);
				} while (rs.next());
				
				return list;
			} else {
				return null;
			}

		} catch (ResourceException ex) {
			throw new DataException(ex);
		} catch (SQLException ex) {
			throw new DataException(ex);
		} finally {
			closeConnection(conn, ps, rs);
		}
	}
}
