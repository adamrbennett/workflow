package com.lwolf.wf.api.exceptions;

public class PayloadException extends Exception {

	private static final long serialVersionUID = 1L;

	public PayloadException() {
	}

	public PayloadException(String message) {
		super(message);
	}

	public PayloadException(Throwable cause) {
		super(cause);
	}

	public PayloadException(String message, Throwable cause) {
		super(message, cause);
	}

	public PayloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
