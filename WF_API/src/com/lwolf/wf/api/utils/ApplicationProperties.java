package com.lwolf.wf.api.utils;

import java.io.IOException;
import java.util.Properties;

public class ApplicationProperties {
	
	private static final Properties props = new Properties();
	
	private static String getProperty(String key) throws IOException {
		if (props.isEmpty()) {
			props.load(ApplicationProperties.class.getResourceAsStream("/com/lwolf/wf/api/resources/application.properties"));
		}
		return props.getProperty(key);
	}
	
	public static String uploadTempDir() throws IOException {
		return getProperty("upload.temp.dir");
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
