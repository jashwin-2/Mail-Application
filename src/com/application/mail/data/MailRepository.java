package com.application.mail.data;
import java.util.Map;

import com.application.mail.data.model.Account;
import com.application.mail.data.model.Mail;
import com.application.mail.data.model.MailId;
import com.application.mail.exceptions.AccouuntNotFoundException;
import com.application.mail.exceptions.AuthenticationFailedException;
import com.application.mail.exceptions.InvalidPasswordException;
public class MailRepository {
	private final String domainName;
	public Map<String,Account> accounts;
	
	public MailRepository(String domainName) {
		this.domainName = domainName;
	}
	
	public Account getAccount(String id) throws AccouuntNotFoundException
	{
		if(accounts.get(id)==null)
			throw new AccouuntNotFoundException(id);
		else
			return accounts.get(id);
	}
	
	public Account authenticate(String id,String password) throws AuthenticationFailedException
	{
		Account account=getAccount(id);
		if(account.getPassword().equals(password))
			return getAccount(id);
		else
			throw new InvalidPasswordException();
	}
	
	public void sendMail(Mail mail,MailId receiver) throws AccouuntNotFoundException
	{
		if(this.domainName.equals(receiver.getDomain()))
		{
			getAccount(receiver.getId()).addInMail(mail);
		}
		else
		{
			
		}
			
	}

	public String getDomainName() {
		return domainName;
	}
}
