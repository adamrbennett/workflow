package com.lwolf.scud.locators;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import com.lwolf.scud.exceptions.ResourceException;

public interface TransactionLocator {
	public UserTransaction getUserTransaction() throws ResourceException;
	public TransactionManager getTransactionManager() throws ResourceException;
}
