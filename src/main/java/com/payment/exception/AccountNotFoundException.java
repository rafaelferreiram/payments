package com.payment.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException  extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;

	private HttpStatus code;

	public AccountNotFoundException(String message, HttpStatus code) {
		super();
		this.message = message;
		this.code = code;
	}

	public AccountNotFoundException(String message) {
		super();
		this.message = message;
	}

}
