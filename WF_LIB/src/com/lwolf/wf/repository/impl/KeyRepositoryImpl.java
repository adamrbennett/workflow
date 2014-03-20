package com.lwolf.wf.repository.impl;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.persistence.KeyDao;
import com.lwolf.wf.repository.KeyRepository;

public class KeyRepositoryImpl implements KeyRepository {
	
	private final KeyDao _dao;
	
	public KeyRepositoryImpl(KeyDao dao) {
		_dao = dao;
	}

	@Override
	public String get(String apiKey) throws RepositoryException {
		try {
			return _dao.getSecretKey(apiKey);
		} catch (DataException ex) {
			throw new RepositoryException(ex);
		}
	}

}
