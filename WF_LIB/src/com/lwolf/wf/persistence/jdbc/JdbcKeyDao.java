package com.lwolf.wf.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.scud.exceptions.ResourceException;
import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.wf.persistence.KeyDao;

public class JdbcKeyDao implements KeyDao {
	
	private final DataSourceLocator _dataSourceLocator;

	public JdbcKeyDao(DataSourceLocator dataSourceLocator) {
		_dataSourceLocator = dataSourceLocator;
	}

	@Override
	public String getSecretKey(String apiKey) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			conn = _dataSourceLocator.getDataSource().getConnection();
			ps = conn.prepareStatement("select secret_key from api_keys where api_key = ?");
			ps.setString(1, apiKey);
			rs = ps.executeQuery();
			
			String secretKey = null;
			if (rs.next()) {
				secretKey = rs.getString("secret_key");
			}
			
			return secretKey;
			
		} catch (ResourceException ex) {
			throw new DataException(ex);
		} catch (SQLException ex) {
			throw new DataException(ex);
		} finally {
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

}
