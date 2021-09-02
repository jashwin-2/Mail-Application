package com.application.mail.data.model;

import java.util.regex.Pattern;

import com.application.mail.exceptions.InvalidMailIdException;

public class MailId {
	private String id;
	private String domain;
	private String name;

	public MailId(String id) throws InvalidMailIdException 
	{
		if(isValidMail(id))
			this.id = id;
		else
			throw new InvalidMailIdException();
	}

	public String getId() {
		return id;
	}

	public String getDomain() {
		if(domain==null)
			setFields();
		return domain;
	}

	private void setFields() {
		String [] str=id.split("@", 2);
		this. name= str[0].split("[0-9]")[0];
		domain=str[1];
	}

	public boolean isValidMail(String str)
	{
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";  
		Pattern pattern = Pattern.compile(regex); 
		if(pattern.matcher(str).matches())
			return true;
		return false;
	}


	@Override
	public String toString() {
		return this.id;
	}

	public String getName() {
		if(name==null)
			setFields();
		return name;
	}


}
