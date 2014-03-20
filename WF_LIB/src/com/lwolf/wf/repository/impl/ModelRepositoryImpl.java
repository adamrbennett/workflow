package com.lwolf.wf.repository.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.dto.ModelDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.persistence.ModelDao;
import com.lwolf.wf.persistence.entities.ModelEntity;
import com.lwolf.wf.repository.ModelRepository;

public class ModelRepositoryImpl implements ModelRepository {
	
	private final ModelDao _dao;
	
	public ModelRepositoryImpl(ModelDao dao) {
		_dao = dao;
	}

	@Override
	public void get(ModelDto dto) throws RepositoryException {
		try {
			ModelEntity entity = setEntity(dto, new ModelEntity());
			_dao.read(entity);
			setDto(entity, dto);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void add(ModelDto dto) throws RepositoryException {
		try {
			ModelEntity entity = setEntity(dto, new ModelEntity());
			
			if (entity.fileData != null) {
				if (entity.fileData.available() <= 0) {
					entity.fileData.reset();
				} else {
					entity.fileData.mark(5242880); // 5mb
				}
			}
			
			entity.modelId = _dao.create(entity);
			
			setDto(entity, dto);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		} catch (IOException ex) {
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void update(ModelDto dto) throws RepositoryException {
		try {
			ModelEntity entity = setEntity(dto, new ModelEntity());
			
			if (entity.fileData != null) {
				if (entity.fileData.available() <= 0) {
					entity.fileData.reset();
				} else {
					entity.fileData.mark(5242880); // 5mb
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
	public void remove(ModelDto dto) throws RepositoryException {
		try {
			_dao.delete(setEntity(dto, new ModelEntity()));
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	@Override
	public List<ModelDto> list() throws RepositoryException {
		try {
			List<ModelEntity> entities = _dao.findAll();
			List<ModelDto> list = null;
			if (entities != null && !entities.isEmpty()) {
				list = new ArrayList<ModelDto>();
				for (ModelEntity entity : entities) {
					list.add(setDto(entity, new ModelDto()));
				}
			}
			return list;
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	private ModelEntity setEntity(ModelDto dto, ModelEntity entity) {
		entity.modelId = dto.modelId;
		entity.createdOn = dto.createdOn;
		entity.createdBy = dto.createdBy;
		entity.modifiedOn = dto.modifiedOn;
		entity.modifiedBy = dto.modifiedBy;
		entity.name = dto.name;
		entity.versionNumber = dto.versionNumber;
		entity.fileName = dto.fileName;
		entity.fileType = dto.fileType;
		entity.fileData = dto.fileData;
		return entity;
	}
	
	private ModelDto setDto(ModelEntity entity, ModelDto dto) {
		dto.modelId = entity.modelId;
		dto.createdOn = entity.createdOn;
		dto.createdBy = entity.createdBy;
		dto.modifiedOn = entity.modifiedOn;
		dto.modifiedBy = entity.modifiedBy;
		dto.name = entity.name;
		dto.versionNumber = entity.versionNumber;
		dto.fileName = entity.fileName;
		dto.fileType = entity.fileType;
		dto.fileData = entity.fileData;
		return dto;
	}

}
