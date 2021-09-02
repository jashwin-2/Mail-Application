package com.application.mail.data;
import java.util.HashMap;
import java.util.Map;
import com.application.mail.data.model.Account;
import com.application.mail.data.model.Mail;
import com.application.mail.exceptions.AccouuntNotFoundException;
import com.application.mail.exceptions.AuthenticationFailedException;
import com.application.mail.exceptions.DomainNotFoundException;
import com.application.mail.exceptions.InvalidPasswordException;
public class MailRepository {
	private final String domainName;
	public Map<String,Account> accounts;
	private RepositoryDispatcher dispatcher;

	public MailRepository(String domainName,RepositoryDispatcher dispatcher) {
		this.domainName = domainName;
		this.dispatcher=dispatcher;
	}

	public boolean conatins(String id) throws AccouuntNotFoundException
	{
		if(accounts.containsKey(id)==false)
			throw new AccouuntNotFoundException(id);
		return true;
	}

	public Account getAccount(String id) 
	{
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

	public void addAccount(Account account)
	{
		if(accounts==null)
			accounts= new HashMap<String,Account>();
		accounts.put(account.getUserId().getId(), account);
	}

	public void sendMail(Mail mail) throws DomainNotFoundException, AccouuntNotFoundException, CloneNotSupportedException
	{
		String receiverMailId=mail.getReceiver().getId();
		Account sender=getAccount(mail.getSender().getId());
		Mail receiverMail=(Mail) mail.clone();
		receiverMail.setType(Mail.Type.RECEIVED);
		if(this.domainName.equals(mail.getReceiver().getDomain()))
		{
			sender.addInMail(mail);
			getAccount(receiverMailId).addInMail(receiverMail);
		}
		else
		{
			MailRepository receiverrep=dispatcher.getRepository(mail.getReceiver().getDomain());
			if(receiverrep.conatins(receiverMailId))
				receiverrep.receiveMail(receiverMail,receiverMailId);
			sender.addInMail(mail);
		}

	}

	public void receiveMail(Mail mail, String receiverMailId) 
	{
		getAccount(receiverMailId).addInMail(mail);
	}

	public String getDomainName() {
		return domainName;
	}
}
