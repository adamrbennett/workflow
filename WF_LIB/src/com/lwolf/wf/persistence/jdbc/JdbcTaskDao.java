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
import com.lwolf.wf.persistence.TaskDao;
import com.lwolf.wf.persistence.entities.TaskEntity;

public class JdbcTaskDao extends JdbcDataAccessObject<TaskEntity> implements TaskDao {
	
	public JdbcTaskDao(DataSourceLocator dataSourceLocator, IdLocator idLocator) {
		super(dataSourceLocator, idLocator);
	}

	@Override
	public List<TaskEntity> findAll() throws DataException {
		return super.findAll(TaskEntity.class);
	}
	
	@Override
	public List<TaskEntity> findByProcess(TaskEntity entity) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement("select task_id, script_id, created_on, created_by, modified_on, modified_by, name, role, buttons, options, decision, due_on, completed_on from task where process_id = ?");
			ps.setInt(1, entity.processId);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				List<TaskEntity> list = new ArrayList<TaskEntity>();
				do {
					TaskEntity task = new TaskEntity();
					task.processId = entity.processId;
					task.taskId = rs.getInt("task_id");
					task.scriptId = rs.getString("script_id");
					task.createdOn = rs.getTimestamp("created_on");
					task.createdBy = rs.getString("created_by");
					task.modifiedOn = rs.getTimestamp("modified_on");
					task.modifiedBy = rs.getString("modified_by");
					task.name = rs.getString("name");
					task.role = rs.getString("role");
					task.buttons = rs.getString("buttons");
					task.options = rs.getString("options");
					task.decision = rs.getString("decision");
					task.dueOn = rs.getTimestamp("due_on");
					task.completedOn = rs.getTimestamp("completed_on");
					
					list.add(task);
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
	
	@Override
	public void findByProcessScript(TaskEntity entity) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement("select task_id, created_on, created_by, modified_on, modified_by, name, role, buttons, options, decision, due_on, completed_on from task where process_id = ? and script_id = ?");
			ps.setInt(1, entity.processId);
			ps.setString(2, entity.scriptId);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				entity.processId = entity.processId;
				entity.scriptId = entity.scriptId;
				entity.taskId = rs.getInt("task_id");
				entity.createdOn = rs.getTimestamp("created_on");
				entity.createdBy = rs.getString("created_by");
				entity.modifiedOn = rs.getTimestamp("modified_on");
				entity.modifiedBy = rs.getString("modified_by");
				entity.name = rs.getString("name");
				entity.role = rs.getString("role");
				entity.buttons = rs.getString("buttons");
				entity.options = rs.getString("options");
				entity.decision = rs.getString("decision");
				entity.dueOn = rs.getTimestamp("due_on");
				entity.completedOn = rs.getTimestamp("completed_on");
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
