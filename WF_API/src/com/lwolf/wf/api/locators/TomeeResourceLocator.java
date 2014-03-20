package com.lwolf.wf.api.locators;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import com.lwolf.scud.exceptions.ResourceException;
import com.lwolf.scud.locators.ResourceLocator;
import com.lwolf.wf.api.utils.ApplicationProperties;

public class TomeeResourceLocator implements ResourceLocator {
	
	private static ResourceLocator _instance;
	
	private Context ctx;
	private UserTransaction userTransaction;
	private TransactionManager transactionManager;
	private DataSource dataSource;
	
	private TomeeResourceLocator() {
	}
	
	public static ResourceLocator locator() {
		if (_instance == null) {
			_instance = new TomeeResourceLocator();
		}
		return _instance;
	}
	
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("TomeeResourceLocator is a singleton");
	}

	@Override
	public UserTransaction getUserTransaction() throws ResourceException {
		try {
			if (userTransaction == null) {
				userTransaction = (UserTransaction) getContext().lookup(ApplicationProperties.userTransactionUrl());
			}
			return userTransaction;
		} catch (NamingException ex) {
			throw new ResourceException(ex);
		} catch (IOException ex) {
			throw new ResourceException(ex);
		}
	}

	@Override
	public TransactionManager getTransactionManager() throws ResourceException {
		try {
			if (transactionManager == null) {
				transactionManager = (TransactionManager) getContext().lookup(ApplicationProperties.transactionManagerUrl());
			}
			return transactionManager;
		} catch (NamingException ex) {
			throw new ResourceException(ex);
		} catch (IOException ex) {
			throw new ResourceException(ex);
		}
	}

	@Override
	public DataSource getDataSource() throws ResourceException {
		try {
			if (dataSource == null) {
				dataSource = (DataSource) getContext().lookup(ApplicationProperties.dataSourceUrl());
			}
			return dataSource;
		} catch (NamingException ex) {
			throw new ResourceException(ex);
		} catch (IOException ex) {
			throw new ResourceException(ex);
		}
	}
	
	private Context getContext() throws NamingException {
		if (ctx == null) {
			ctx = new InitialContext();
		}
		return ctx;
	}

}
