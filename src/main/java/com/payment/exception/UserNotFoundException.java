package com.payment.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;

	private HttpStatus code;

	public UserNotFoundException(String message, HttpStatus code) {
		super();
		this.message = message;
		this.code = code;
	}

	public UserNotFoundException(String message) {
		super();
		this.message = message;
	}

}
