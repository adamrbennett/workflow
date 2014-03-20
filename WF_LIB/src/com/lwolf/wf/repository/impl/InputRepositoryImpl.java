package com.lwolf.wf.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.dto.InputDto;
import com.lwolf.wf.dto.InputFileDto;
import com.lwolf.wf.dto.InputMapDto;
import com.lwolf.wf.dto.TaskDto;
import com.lwolf.wf.dto.TaskListDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.persistence.InputDao;
import com.lwolf.wf.persistence.InputFileDao;
import com.lwolf.wf.persistence.entities.InputEntity;
import com.lwolf.wf.persistence.entities.InputFileEntity;
import com.lwolf.wf.persistence.entities.TaskEntity;
import com.lwolf.wf.repository.InputRepository;

public class InputRepositoryImpl implements InputRepository {
	
	private final InputDao _inputDao;
	private final InputFileDao _inputFileDao;
	
	public InputRepositoryImpl(InputDao inputDao, InputFileDao inputFileDao) {
		_inputDao = inputDao;
		_inputFileDao = inputFileDao;
	}

	@Override
	public void get(InputDto dto) throws RepositoryException {
		try {
			InputEntity entity = setEntity(dto, new InputEntity());
			_inputDao.read(entity);
			setDto(entity, dto);
			setFiles(entity, dto);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void add(InputDto dto) throws RepositoryException {
		try {
			InputEntity entity = setEntity(dto, new InputEntity());
			_inputDao.create(entity);
			setDto(entity, dto);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void update(InputDto dto) throws RepositoryException {
		try {
			// populate entity
			InputEntity entity = setEntity(dto, new InputEntity());
			
			// update the input
			_inputDao.update(setEntity(dto, new InputEntity()));
			
			// get the existing files
			List<InputFileEntity> existingFiles = _inputFileDao.findByInput(entity);
			
			// create or update files
			if (dto.files != null) {
				for (InputFileDto file : dto.files) {
					InputFileEntity fileEntity = setEntity(file, new InputFileEntity());
					fileEntity.processId = entity.processId;
					fileEntity.taskId = entity.taskId;
					fileEntity.inputId = entity.inputId;
					
					if (!exists(fileEntity, existingFiles)) {
						file.fileId = _inputFileDao.create(fileEntity);
					}
				}
			}
			
			// delete files
			if (existingFiles != null) {
				for (InputFileEntity file : existingFiles) {
					InputFileDto fileDto = setDto(file, new InputFileDto());
					fileDto.processId = dto.processId;
					fileDto.taskId = dto.taskId;
					fileDto.inputId = dto.inputId;
					
					if (!exists(fileDto, dto.files)) {
						_inputFileDao.delete(file);
					}
				}
			}
			
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void remove(InputDto dto) throws RepositoryException {
		try {
			_inputDao.delete(setEntity(dto, new InputEntity()));
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void get(InputMapDto map) throws RepositoryException {
		try {
			TaskEntity entity = new TaskEntity();
			entity.processId = map.processId;
			entity.taskId = map.taskId;
			
			Map<String, InputEntity> entities = _inputDao.findByTask(entity);
			if (entities != null && !entities.isEmpty()) {
				map.clear();
				for (Entry<String, InputEntity> entry : entities.entrySet()) {
					InputEntity inputEntity = entry.getValue();
					InputDto inputDto = setDto(inputEntity, new InputDto());
					setFiles(inputEntity, inputDto);
					map.put(entry.getKey(), inputDto);
				}
			}
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void add(InputMapDto map) throws RepositoryException {
		if (map != null && !map.isEmpty()) {
			for (Entry<String, InputDto> entry : map.entrySet()) {
				entry.getValue().taskId = map.taskId;
				add(entry.getValue());
			}
		}
	}

	@Override
	public void update(InputMapDto map) throws RepositoryException {
		if (map != null && !map.isEmpty()) {
			for (Entry<String, InputDto> entry : map.entrySet()) {
				update(entry.getValue());
			}
		}
	}

	@Override
	public void get(TaskListDto list) throws RepositoryException {
		try {
			if (list != null && !list.isEmpty()) {
				for (TaskDto dto : list) {
					TaskEntity entity = new TaskEntity();
					entity.processId = dto.processId;
					entity.taskId = dto.taskId;
					
					Map<String, InputEntity> entities = _inputDao.findByTask(entity);
					dto.inputs = new InputMapDto(dto.processId, dto.taskId);
					if (entities != null && !entities.isEmpty()) {
						for (Entry<String, InputEntity> entry : entities.entrySet()) {
							InputEntity inputEntity = entry.getValue();
							InputDto inputDto = setDto(inputEntity, new InputDto());
							setFiles(inputEntity, inputDto);
							dto.inputs.put(entry.getKey(), inputDto);
						}
					}
				}
			}
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	@Override
	public void get(InputFileDto file) throws RepositoryException {
		try {
			InputFileEntity entity = new InputFileEntity();
			_inputFileDao.read(setEntity(file, entity));
			setDto(entity, file);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	private void setFiles(InputEntity entity, InputDto dto) throws DataException {
		List<InputFileEntity> files = _inputFileDao.findByInput(entity);
		if (files != null) {
			dto.files = new ArrayList<InputFileDto>();
			for (InputFileEntity file : files) {
				dto.files.add(setDto(file, new InputFileDto()));
			}
		}
	}
	
	private InputFileEntity setEntity(InputFileDto dto, InputFileEntity entity) {
		entity.processId = dto.processId;
		entity.taskId = dto.taskId;
		entity.inputId = dto.inputId;
		entity.fileId = dto.fileId;
		entity.fileName = dto.fileName;
		entity.fileType = dto.fileType;
		entity.fileData = dto.fileData;
		return entity;
	}
	
	private InputEntity setEntity(InputDto dto, InputEntity entity) {
		entity.inputId = dto.inputId;
		entity.processId = dto.processId;
		entity.taskId = dto.taskId;
		entity.name = dto.name;
		entity.value = dto.value;
		return entity;
	}
	
	private InputFileDto setDto(InputFileEntity entity, InputFileDto dto) {
		dto.processId = entity.processId;
		dto.taskId = entity.taskId;
		dto.inputId = entity.inputId;
		dto.fileId = entity.fileId;
		dto.fileName = entity.fileName;
		dto.fileType = entity.fileType;
		dto.fileData = entity.fileData;
		return dto;
	}
	
	private InputDto setDto(InputEntity entity, InputDto dto) {
		dto.inputId = entity.inputId;
		dto.processId = entity.processId;
		dto.taskId = entity.taskId;
		dto.name = entity.name;
		dto.value = entity.value;
		return dto;
	}
	
	private boolean exists(InputFileEntity file, List<InputFileEntity> files) {
		if (file == null || files == null || files.isEmpty())
			return false;
		
		return files.contains(file);
	}
	
	private boolean exists(InputFileDto file, List<InputFileDto> files) {
		if (file == null || files == null || files.isEmpty())
			return false;
		
		return files.contains(file);
	}

}
