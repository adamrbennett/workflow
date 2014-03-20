package com.lwolf.wf.persistence;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.persistence.entities.UserEntity;

public interface UserDao {
	
	public void read(UserEntity entity) throws DataException;

}
