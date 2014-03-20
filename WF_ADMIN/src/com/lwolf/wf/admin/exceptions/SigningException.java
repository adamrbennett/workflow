package com.lwolf.wf.admin.exceptions;

public class SigningException extends Exception {

	private static final long serialVersionUID = 1L;

	public SigningException() {
	}

	public SigningException(String message) {
		super(message);
	}

	public SigningException(Throwable cause) {
		super(cause);
	}

	public SigningException(String message, Throwable cause) {
		super(message, cause);
	}

	public SigningException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
