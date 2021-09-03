package com.application.mail.exceptions;

public class AccountNotFoundException extends AuthenticationFailedException {
	private final String id;

	public AccountNotFoundException(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	
}
