package com.lwolf.wf.admin.auth;

import com.lwolf.wf.admin.exceptions.SigningException;

public interface RequestSigner {
	
	public String sign(Payload payload) throws SigningException;

}
