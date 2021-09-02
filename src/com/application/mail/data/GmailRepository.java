package com.application.mail.data;

import com.application.mail.data.model.Account;
import com.application.mail.data.model.MailId;
import com.application.mail.exceptions.InvalidMailIdException;

public class GmailRepository extends MailRepository{
	private static GmailRepository instance;
	private GmailRepository(RepositoryDispatcher dispatcher) {
		super("gmail.com", dispatcher);
	}
	public static GmailRepository getInstance(RepositoryDispatcher dispatcher)
	{
		if(instance==null)
		{
			synchronized(GmailRepository.class)
			{
				if(instance==null) 
				{
					instance=new GmailRepository(dispatcher);
					instance.fillMockData();
				}
			}
		}

		return instance;

	}
	private void fillMockData()  {
		try {
			addAccount(new Account("Kannan",new MailId("ram@gmail.com"), "1234", null, 512542561));
			addAccount(new Account("Ram",new MailId("rex@gmail.com"), "1234", null, 512542561));
		} catch (InvalidMailIdException e) {
			e.printStackTrace();
		}
	}
}
