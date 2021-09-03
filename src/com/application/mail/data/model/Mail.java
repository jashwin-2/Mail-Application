package com.application.mail.data.model;

public class Mail implements Cloneable {
	private final MailId sender;
	private final MailId receiver;
	private Type type;
	private String subject,body,attachment;


	public Mail(MailId sender, MailId receiver, String subject, String body, String attachment , Type type) {

		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.body = body;
		this.attachment = attachment;
		this.type=type;
	}

	public enum Type
	{
		SENT("Sent Mails"),RECEIVED("Received Mails"),DRAFT("Draft Mails");
		public final String text;
		Type(String string) {
			text=string;
		}
		public String toString()
		{
			switch(this)
			{
			case SENT:
				return "Sent";
			case RECEIVED:
				return "Received";
			case DRAFT:
				return "Draft";
			}
			return null;
		}
	}

	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public MailId getReceiver() {
		return receiver;
	}

	public MailId getSender() {
		return sender;
	}

}

