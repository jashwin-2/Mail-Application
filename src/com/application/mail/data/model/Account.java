package com.application.mail.data.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
	private String name;
	private final MailId userId;
	private String password;
	List<Mail> allMails;
	long mobileNo;
	
	public Account(String name, MailId id, String password, List<Mail> allMails, long mobileNo) {
		this.name = name;
		this.userId = id;
		this.password = password;
		this.allMails = allMails;
		this.mobileNo = mobileNo;
	}
	
	public void addInMail(Mail mail)
	{
		if(allMails==null)
			allMails =new ArrayList<Mail>();
		allMails.add(mail);
	}
	
	public void deleteMail(Mail mail)
	{
		allMails.remove(mail);
	}
	public String getName() {
		return name;
	}
	public MailId getUserId() {
		return userId;
	}
	public String getPassword() {
		return password;
	}
	public List<Mail> getAllMails() {
		return allMails;
	}
	public long getMobileNo() {
		return mobileNo;
	}
}
