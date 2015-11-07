package com.web.support.exception;

public class NotLoginedException extends RuntimeException {

	private static final long serialVersionUID = 1055751083898691551L;

	public NotLoginedException() {
	}

	public NotLoginedException(String message) {
		super(message);
	}

}
