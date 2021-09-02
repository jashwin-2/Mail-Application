package com.application.mail.data.model;

public class MailId {
	private String id;
	private String domain;
	private String name;
	
	public MailId(String id) 
	{
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}
}
