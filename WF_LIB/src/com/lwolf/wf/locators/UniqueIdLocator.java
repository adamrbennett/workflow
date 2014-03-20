package com.lwolf.wf.locators;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.transaction.InvalidTransactionException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import com.lwolf.scud.exceptions.ResourceException;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.scud.locators.ResourceLocator;

public class UniqueIdLocator implements IdLocator {
	
	private static final Hashtable<String, Iterator<Integer>> uniqueIds = new Hashtable<String, Iterator<Integer>>();
	
	private final ResourceLocator resourceLocator;
	
	private static IdLocator _instance;
	
	public UniqueIdLocator(ResourceLocator resourceLocator) {
		this.resourceLocator = resourceLocator;
	}
	
	public static IdLocator load(IdLocator locator) {
		_instance = locator;
		return _instance;
	}
	
	public static IdLocator locator() {
		return _instance;
	}
	
	public Integer id(String table) throws ResourceException {
		
		Integer nextId = null;
		
		synchronized (uniqueIds) {
			Iterator<Integer> tableIds = uniqueIds.get(table);
			if (tableIds == null || !tableIds.hasNext()) {
				tableIds = idList(table).iterator();
				uniqueIds.put(table, tableIds);
			}
			nextId = tableIds.next();
		}
		
		return nextId;
		
		
	}
	
	private synchronized List<Integer> idList(String table) throws ResourceException {

		Transaction tx = null;
		
		// if a transaction is in place
		try {
			if (resourceLocator.getUserTransaction().getStatus() != Status.STATUS_NO_TRANSACTION) {
				// suspend the transaction
				tx = resourceLocator.getTransactionManager().suspend();
			}
		} catch (ResourceException ex) {
			throw new ResourceException(ex);
		} catch (SystemException ex) {
			throw new ResourceException(ex);
		}
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int tries = 0;
		
		do {
			try {
				conn = resourceLocator.getDataSource().getConnection();
				//conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				ps = conn.prepareStatement("select next_id from unique_id where table_name = ?");
				ps.setString(1, table);
				rs = ps.executeQuery();
				
				List<Integer> lst = Collections.synchronizedList(new ArrayList<Integer>());
				int nextId = 0;
				short prefetch = 10;
				
				if (rs.next()) {
					nextId = rs.getInt(1);
					
					for (int i=0; i<prefetch; i++) {
						lst.add(new Integer(nextId));
						nextId++;
					}
				} else {
					throw new ResourceException("Table not found in unique_id: " + table);
				}
				
				rs.close();
				ps.close();
				
				ps = conn.prepareStatement("update unique_id set next_id = ? where table_name = ?");
				ps.setLong(1, nextId);
				ps.setString(2, table);
				ps.execute();
				
				// resume any open transaction
				try {
					if (tx != null) {
						resourceLocator.getTransactionManager().resume(tx);
					}
				} catch (ResourceException ex) {
					throw new ResourceException(ex);
				} catch (SystemException ex) {
					throw new ResourceException(ex);
				} catch (InvalidTransactionException ex) {
					throw new ResourceException(ex);
				}
				
				return lst;
			} catch (ResourceException ex) {
				throw new ResourceException(ex);
			} catch (SQLException sqlEx) {
				// deadlock
				if (sqlEx != null && "40001".equals(sqlEx.getSQLState())) {
					tries++;
				// stale connection
				} else if (sqlEx != null && "11211".equals(sqlEx.getSQLState())) {
					tries++;
				} else {
					throw new ResourceException(sqlEx);
				}
			} finally {
				closeConnection(conn, ps, rs);
			}
		} while (tries < 3);
		
		// resume any open transaction
		try {
			if (tx != null) {
				resourceLocator.getTransactionManager().resume(tx);
			}
		} catch (ResourceException ex) {
			throw new ResourceException(ex);
		} catch (SystemException ex) {
			throw new ResourceException(ex);
		} catch (InvalidTransactionException ex) {
			throw new ResourceException(ex);
		}
		
		throw new ResourceException("Connection (deadlock,staleconnection) exception could not be resolved");
	}
	
	private void closeConnection(Connection conn, PreparedStatement ps, ResultSet rs) {
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

}
