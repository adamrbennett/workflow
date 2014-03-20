package com.lwolf.wf.admin.auth;

public interface Payload {

	public void setMethod(String method);
	public void setUri(String uri);
	public void setProtocol(String protocol);
	public void setContentMD5(String contentMD5);
	public void setContentType(String contentType);
	public void setDate(String date);
	public String payload();

}
