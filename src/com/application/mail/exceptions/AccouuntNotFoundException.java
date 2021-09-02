package com.application.mail.exceptions;

public class AccouuntNotFoundException extends AuthenticationFailedException {
	private String id;

	public AccouuntNotFoundException(String id) {
		super();
		this.setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
