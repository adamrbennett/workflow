package com.lwolf.wf.repository.impl;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.dto.UserDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.persistence.UserDao;
import com.lwolf.wf.persistence.entities.UserEntity;
import com.lwolf.wf.repository.UserRepository;

public class UserRepositoryImpl implements UserRepository {
	
	private final UserDao _dao;
	
	public UserRepositoryImpl(UserDao dao) {
		_dao = dao;
	}

	@Override
	public void get(UserDto dto) throws RepositoryException {
		try {
			UserEntity entity = setEntity(dto, new UserEntity());
			_dao.read(entity);
			setDto(entity, dto);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	private UserEntity setEntity(UserDto dto, UserEntity entity) {
		entity.userName = dto.userName;
		entity.fullName = dto.fullName;
		return entity;
	}
	
	private UserDto setDto(UserEntity entity, UserDto dto) {
		dto.userName = entity.userName;
		dto.fullName = entity.fullName;
		return dto;
	}

}
