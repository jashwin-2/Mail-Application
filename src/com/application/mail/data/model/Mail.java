package com.application.mail.data.model;

public class Mail {
	private final MailId sender;
	private final MailId receiver;
	private Type type;
	private String subject,body,attachment;


	public Mail(MailId sender, MailId receiver, String subject, String body, String attachment , Type type) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.body = body;
		this.attachment = attachment;
		this.type=type;
	}

	public enum Type
	{
		SENT,RECEIVED;
	}


}

