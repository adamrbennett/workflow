package com.lwolf.wf.resources.bundles.notification;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class NotificationBundle extends ResourceBundle {

	private static final String BUNDLE_NAME = "com.lwolf.wf.resources.bundles.notification.Notifications";
	
	private static final String MODEL_DELETE_DEPENDENCY_PROCESS = "model.delete.dependency.process";
	
	private final ResourceBundle _bundle;
	
	public NotificationBundle(Locale locale) {
		_bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}
	
	public String modelDeleteDependencyProcess(Object... arguments) {
		return MessageFormat.format(_bundle.getString(MODEL_DELETE_DEPENDENCY_PROCESS), arguments);
	}

	@Override
	protected Object handleGetObject(String key) {
		return null;
	}

	@Override
	public Enumeration<String> getKeys() {
		return null;
	}

}
