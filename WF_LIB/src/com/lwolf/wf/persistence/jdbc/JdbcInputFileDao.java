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
import com.lwolf.wf.persistence.InputFileDao;
import com.lwolf.wf.persistence.entities.InputEntity;
import com.lwolf.wf.persistence.entities.InputFileEntity;

public class JdbcInputFileDao extends JdbcDataAccessObject<InputFileEntity> implements InputFileDao {
	
	public JdbcInputFileDao(DataSourceLocator dataSourceLocator, IdLocator idLocator) {
		super(dataSourceLocator, idLocator);
	}
	
	@Override
	public Integer create(InputFileEntity entity) throws DataException {
		return super.create(entity);
	}

	@Override
	public void delete(InputFileEntity entity) throws DataException {
		super.delete(entity);
	}

	@Override
	public void read(InputFileEntity entity) throws DataException {
		super.read(entity);
	}

	@Override
	public void update(InputFileEntity entity) throws DataException {
		super.update(entity);
	}

	public List<InputFileEntity> findByInput(InputEntity input) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement("select file_id, file_name, file_type from input_file where process_id = ? and task_id = ? and input_id = ?");
			ps.setInt(1, input.processId);
			ps.setInt(2, input.taskId);
			ps.setInt(3, input.inputId);
			rs = ps.executeQuery();
			
			List<InputFileEntity> files = null;
			if (rs.next()) {
				files = new ArrayList<InputFileEntity>();
				do {
					InputFileEntity file = new InputFileEntity();
					file.processId = input.processId;
					file.taskId = input.taskId;
					file.inputId = input.inputId;
					file.fileId = rs.getInt("file_id");
					file.fileName = rs.getString("file_name");
					file.fileType = rs.getString("file_type");
					
					files.add(file);
				} while (rs.next());
			}
			return files;
		} catch (ResourceException ex) {
			throw new DataException(ex);
		} catch (SQLException ex) {
			throw new DataException(ex);
		} finally {
			closeConnection(conn, ps, rs);
		}
	}

}
