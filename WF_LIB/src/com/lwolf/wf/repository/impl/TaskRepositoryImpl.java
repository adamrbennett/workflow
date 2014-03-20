package com.lwolf.wf.repository.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.dto.TaskDto;
import com.lwolf.wf.dto.TaskListDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.persistence.TaskDao;
import com.lwolf.wf.persistence.entities.TaskEntity;
import com.lwolf.wf.repository.TaskRepository;

public class TaskRepositoryImpl implements TaskRepository {
	
	private final TaskDao _taskDao;
	
	public TaskRepositoryImpl(TaskDao taskDao) {
		_taskDao = taskDao;
	}

	@Override
	public void get(TaskDto dto) throws RepositoryException {
		try {
			// get the task
			TaskEntity entity = setEntity(dto, new TaskEntity());
			
			if (entity.taskId != null) {
				_taskDao.read(entity);
			} else {
				_taskDao.findByProcessScript(entity);
			}
			
			setDto(entity, dto);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	@Override
	public void get(TaskListDto dto) throws RepositoryException {
		try {
			List<TaskEntity> entities = _taskDao.findByProcess(setEntity(dto, new TaskEntity()));
			dto.clear();
			if (entities != null && !entities.isEmpty()) {
				for (TaskEntity entity : entities) {
					dto.add(setDto(entity, new TaskDto()));
				}
			}
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void add(TaskDto dto) throws RepositoryException {
		try {
			TaskEntity entity = setEntity(dto, new TaskEntity());
			
			// default due date to tomorrow, if none specified
			if (entity.dueOn == null) {
				Calendar cal = new GregorianCalendar();
				cal.add(Calendar.DATE, 1);
				
				entity.dueOn = cal.getTime();
			}
			
			dto.taskId = _taskDao.create(entity);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void update(TaskDto dto) throws RepositoryException {
		try {
			// update the task
			TaskEntity entity = setEntity(dto, new TaskEntity());
			_taskDao.update(entity);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void remove(TaskDto dto) throws RepositoryException {
		try {
			_taskDao.delete(setEntity(dto, new TaskEntity()));
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	private TaskEntity setEntity(TaskDto dto, TaskEntity entity) {
		entity.taskId = dto.taskId;
		entity.processId = dto.processId;
		entity.scriptId = dto.scriptId;
		entity.createdOn = dto.createdOn;
		entity.createdBy = dto.createdBy;
		entity.modifiedOn = dto.modifiedOn;
		entity.modifiedBy = dto.modifiedBy;
		entity.name = dto.name;
		entity.role = dto.role;
		entity.buttons = dto.buttons;
		entity.options = dto.options;
		entity.decision = dto.decision;
		entity.dueOn = dto.dueOn;
		entity.completedOn = dto.completedOn;
		return entity;
	}
	
	private TaskEntity setEntity(TaskListDto dto, TaskEntity entity) {
		entity.processId = dto.processId;
		return entity;
	}
	
	private TaskDto setDto(TaskEntity entity, TaskDto dto) {
		dto.taskId = entity.taskId;
		dto.processId = entity.processId;
		dto.scriptId = entity.scriptId;
		dto.createdOn = entity.createdOn;
		dto.createdBy = entity.createdBy;
		dto.modifiedOn = entity.modifiedOn;
		dto.modifiedBy = entity.modifiedBy;
		dto.name = entity.name;
		dto.role = entity.role;
		dto.buttons = entity.buttons;
		dto.options = entity.options;
		dto.decision = entity.decision;
		dto.dueOn = entity.dueOn;
		dto.completedOn = entity.completedOn;
		return dto;
	}

}
