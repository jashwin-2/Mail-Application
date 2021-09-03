package com.application.mail.data;
import java.util.HashMap;
import java.util.Map;
import com.application.mail.data.model.Account;
import com.application.mail.data.model.Mail;
import com.application.mail.data.model.MailId;
import com.application.mail.exceptions.AccountNotFoundException;
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

	public boolean contains(String id)
	{
		return accounts.containsKey(id);
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

	public void sendMail(Mail mail) throws DomainNotFoundException, AccountNotFoundException, CloneNotSupportedException
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
			if(receiverrep.contains(receiverMailId))
				receiverrep.receiveMail(receiverMail,receiverMailId);
			else
				throw new AccountNotFoundException(receiverMailId);
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

	public boolean isValid(MailId mailId) throws DomainNotFoundException{
		String domainName=mailId.getDomain();
		boolean found;
		if(domainName.equals(this.domainName))
			found =contains(mailId.getId());
		else
			found =dispatcher.getRepository(domainName).contains(mailId.getId());
		return found;
	}
}
