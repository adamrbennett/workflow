package com.lwolf.wf.admin.utils;

import java.io.IOException;
import java.util.Properties;

public class ApplicationProperties {
	
	private static final Properties props = new Properties();

	private static String getProperty(String key) throws IOException {
		if (props.isEmpty()) {
			props.load(ApplicationProperties.class.getResourceAsStream("/com/lwolf/wf/admin/resources/application.properties"));
		}
		
		return props.getProperty(key);
	}
	
	public static String workflowApiScheme() throws IOException {
		return getProperty("workflow.api.scheme");
	}
	
	public static String workflowApiHost() throws IOException {
		return getProperty("workflow.api.host");
	}
	
	public static int workflowApiPort() throws IOException {
		int port = 80;
		String property = getProperty("workflow.api.port");
		try {
			port = Integer.valueOf(property);
		} catch (Throwable th) {}
		return port;
	}
	
	public static String workflowApiContextRoot() throws IOException {
		return getProperty("workflow.api.contextRoot");
	}
	
	public static String workflowApiUpload() throws IOException {
		return getProperty("workflow.api.upload");
	}
	
	public static String workflowApiKey() throws IOException {
		return getProperty("workflow.api.key");
	}
	
	public static String workflowSecretKey() throws IOException {
		return getProperty("workflow.secret.key");
	}
	
	public static String transactionManagerUrl() throws IOException {
		return getProperty("transactionManager.url");
	}
	
	public static String userTransactionUrl() throws IOException {
		return getProperty("userTransaction.url");
	}
	
	public static String dataSourceUrl() throws IOException {
		return getProperty("dataSource.url");
	}

}
