package com.lwolf.wf.admin.auth.impl;

import javax.ws.rs.core.HttpHeaders;

import com.lwolf.wf.admin.auth.Payload;

public class WorkflowPayload implements Payload {
	
	private static final String CONTENT_MD5 = "Content-MD5";
	private static final String CONTENT_TYPE = HttpHeaders.CONTENT_TYPE;
	private static final String DATE = HttpHeaders.DATE;
	
	private String _payload;
	
	private String _method;
	private String _uri;
	private String _protocol;
	private String _contentMD5;
	private String _contentType;
	private String _date;
	
	public WorkflowPayload() {
	}
	
	public void setMethod(String method) {
		_method = method;
	}
	
	public void setUri(String uri) {
		_uri = uri;
	}
	
	public void setProtocol(String protocol) {
		_protocol = protocol;
	}
	
	public void setContentMD5(String contentMD5) {
		_contentMD5 = contentMD5;
	}
	
	public void setContentType(String contentType) {
		_contentType = contentType;
	}
	
	public void setDate(String date) {
		_date = date;
	}
	
	@Override
	public String payload() {
		// build payload
		StringBuilder payload = new StringBuilder();
		payload.append(_method).append(" ");
		payload.append(_uri).append(" ");
		payload.append(_protocol).append("\n");
		payload.append(CONTENT_MD5).append(": ").append(_contentMD5).append("\n");
		payload.append(CONTENT_TYPE).append(": ").append(_contentType).append("\n");
		payload.append(DATE).append(": ").append(_date);
		
		_payload = payload.toString();
		
		return _payload;
	}
	
	@Override
	public String toString() {
		return payload();
	}

}
