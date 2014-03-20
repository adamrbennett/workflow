package com.lwolf.wf.test.locators;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import com.lwolf.scud.exceptions.ResourceException;
import com.lwolf.scud.locators.ResourceLocator;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class TestResourceLocator implements ResourceLocator {

	private DataSource dataSource;
	private UserTransaction userTransaction;
	private TransactionManager transactionManager;
	private Context ctx;
	
	public TestResourceLocator() {
		
	}
	
	public TestResourceLocator(DataSource dataSource, UserTransaction userTransaction, TransactionManager transactionManager) {
		this.dataSource = dataSource;
		this.userTransaction = userTransaction;
		this.transactionManager = transactionManager;
	}

	@Override
	public DataSource getDataSource() throws ResourceException {
		if (dataSource == null) {
			MysqlDataSource ds = new MysqlDataSource();
			ds.setUrl("jdbc:mysql://localhost:3306/workflowtest");
			ds.setUser("tomcat");
			ds.setPassword("tomcat");
			
			dataSource = ds;
		}
		return dataSource;
	}

	@Override
	public UserTransaction getUserTransaction() throws ResourceException {
		try {
			if (userTransaction == null) {
				userTransaction = (UserTransaction) getContext().lookup("java:comp/UserTransaction");
			}
			return userTransaction;
		} catch (NamingException ex) {
			throw new ResourceException(ex);
		}
	}

	@Override
	public TransactionManager getTransactionManager() throws ResourceException {
		try {
			if (transactionManager == null) {
				transactionManager = (TransactionManager) getContext().lookup("java:comp/TransactionManager");
			}
			return transactionManager;
		} catch (NamingException ex) {
			throw new ResourceException(ex);
		}
	}
	
	private Context getContext() throws NamingException {
		if (ctx == null) {
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
			ctx = new InitialContext(props);
		}
		return ctx;
	}

}
