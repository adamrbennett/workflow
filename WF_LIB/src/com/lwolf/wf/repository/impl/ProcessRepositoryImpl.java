package com.lwolf.wf.repository.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.dto.ProcessDto;
import com.lwolf.wf.dto.ProcessListDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.persistence.ProcessDao;
import com.lwolf.wf.persistence.entities.ProcessEntity;
import com.lwolf.wf.repository.ProcessRepository;

public class ProcessRepositoryImpl implements ProcessRepository {
	
	private final ProcessDao _dao;
	
	public ProcessRepositoryImpl(ProcessDao dao) {
		_dao = dao;
	}

	@Override
	public void get(ProcessDto dto) throws RepositoryException {
		try {
			ProcessEntity entity = setEntity(dto, new ProcessEntity());
			_dao.read(entity);
			setDto(entity, dto);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	@Override
	public void get(ProcessListDto dto) throws RepositoryException {
		try {
			List<ProcessEntity> entities = _dao.findByModel(setEntity(dto, new ProcessEntity()));
			dto.clear();
			if (entities != null && !entities.isEmpty()) {
				for (ProcessEntity entity : entities) {
					dto.add(setDto(entity, new ProcessDto()));
				}
			}
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void add(ProcessDto dto) throws RepositoryException {
		try {
			ProcessEntity entity = setEntity(dto, new ProcessEntity());
			
			if (entity.data != null) {
				if (entity.data.available() <= 0) {
					entity.data.reset();
				} else {
					entity.data.mark(5242880); // 5mb
				}
			}
			
			entity.processId = _dao.create(entity);
			
			setDto(entity, dto);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		} catch (IOException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void update(ProcessDto dto) throws RepositoryException {
		try {
			ProcessEntity entity = setEntity(dto, new ProcessEntity());
			
			if (entity.data != null) {
				if (entity.data.available() <= 0) {
					entity.data.reset();
				} else {
					entity.data.mark(5242880); // 5mb
				}
			}
			
			_dao.update(entity);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		} catch (IOException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void remove(ProcessDto dto) throws RepositoryException {
		try {
			_dao.delete(setEntity(dto, new ProcessEntity()));
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	@Override
	public List<ProcessDto> list() throws RepositoryException {
		try {
			List<ProcessEntity> entities = _dao.findAll();
			List<ProcessDto> list = null;
			if (entities != null && !entities.isEmpty()) {
				list = new ArrayList<ProcessDto>();
				for (ProcessEntity entity : entities) {
					list.add(setDto(entity, new ProcessDto()));
				}
			}
			return list;
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	private ProcessEntity setEntity(ProcessDto dto, ProcessEntity entity) {
		entity.processId = dto.processId;
		entity.modelId = dto.modelId;
		entity.createdOn = dto.createdOn;
		entity.createdBy = dto.createdBy;
		entity.modifiedOn = dto.modifiedOn;
		entity.modifiedBy = dto.modifiedBy;
		entity.name = dto.name;
		entity.data = dto.data;
		return entity;
	}
	
	private ProcessEntity setEntity(ProcessListDto dto, ProcessEntity entity) {
		entity.modelId = dto.modelId;
		return entity;
	}
	
	private ProcessDto setDto(ProcessEntity entity, ProcessDto dto) {
		dto.processId = entity.processId;
		dto.modelId = entity.modelId;
		dto.createdOn = entity.createdOn;
		dto.createdBy = entity.createdBy;
		dto.modifiedOn = entity.modifiedOn;
		dto.modifiedBy = entity.modifiedBy;
		dto.name = entity.name;
		dto.data = entity.data;
		return dto;
	}

}
