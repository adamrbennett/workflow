package com.lwolf.wf.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.scud.exceptions.ResourceException;
import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.scud.persistence.jdbc.JdbcDataAccessObject;
import com.lwolf.wf.persistence.InputDao;
import com.lwolf.wf.persistence.entities.InputEntity;
import com.lwolf.wf.persistence.entities.TaskEntity;

public class JdbcInputDao extends JdbcDataAccessObject<InputEntity> implements InputDao {
	
	public JdbcInputDao(DataSourceLocator dataSourceLocator, IdLocator idLocator) {
		super(dataSourceLocator, idLocator);
	}
	
	public Map<String, InputEntity> findByTask(TaskEntity task) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement("select input_id, name, value from input where process_id = ? and task_id = ?");
			ps.setInt(1, task.processId);
			ps.setInt(2, task.taskId);
			rs = ps.executeQuery();
			
			Map<String, InputEntity> inputs = null;
			if (rs.next()) {
				inputs = new HashMap<String, InputEntity>();
				do {
					InputEntity input = new InputEntity();
					input.processId = task.processId;
					input.taskId = task.taskId;
					input.inputId = rs.getInt("input_id");
					input.name = rs.getString("name");
					input.value = rs.getString("value");
					
					inputs.put(input.name, input);
				} while (rs.next());
			}
			return inputs;
		} catch (ResourceException ex) {
			throw new DataException(ex);
		} catch (SQLException ex) {
			throw new DataException(ex);
		} finally {
			closeConnection(conn, ps, rs);
		}
	}

}
