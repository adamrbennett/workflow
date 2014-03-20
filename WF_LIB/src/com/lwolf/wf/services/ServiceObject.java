package com.lwolf.wf.services;

import java.util.Date;
import java.util.Locale;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.lwolf.scud.exceptions.ResourceException;
import com.lwolf.scud.locators.TransactionLocator;
import com.lwolf.wf.dto.DataTransferObject;
import com.lwolf.wf.exceptions.ServiceException;
import com.lwolf.wf.resources.bundles.notification.NotificationBundle;

public abstract class ServiceObject<T extends DataTransferObject> {
	
	private final TransactionLocator _transactionLocator;
	private final Locale _locale;

	protected final NotificationBundle notificationBundle;
	
	private UserTransaction tx;
	
	protected ServiceObject(Locale locale, TransactionLocator transactionLocator) {
		_locale = locale;
		_transactionLocator = transactionLocator;
		
		notificationBundle = new NotificationBundle(_locale);
	}
	
	public final void add(T dto) throws ServiceException {
		if (dto.createdOn == null)
			dto.createdOn = new Date();
		
		doAdd(dto);
	}
	
	public final void update(T dto) throws ServiceException {
		dto.modifiedOn = new Date();
		doUpdate(dto);
	}
	
	protected void begin() throws ResourceException, SystemException, NotSupportedException {
		tx = _transactionLocator.getUserTransaction();
		
		// if a transaction is not already in place
		if (tx.getStatus() == Status.STATUS_NO_TRANSACTION)
			tx.begin();
	}
	
	protected void commit() throws SystemException, NotSupportedException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
		if (tx.getStatus() != Status.STATUS_NO_TRANSACTION)
			tx.commit();
	}
	
	protected void rollback() {
		try {
			if (tx.getStatus() != Status.STATUS_NO_TRANSACTION)
				tx.rollback();
		} catch (SystemException ex) {}
	}
	
	protected abstract void doAdd(T dto) throws ServiceException;
	protected abstract void doUpdate(T dto) throws ServiceException;

}
