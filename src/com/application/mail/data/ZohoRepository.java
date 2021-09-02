package com.application.mail.data;

import com.application.mail.data.model.Account;
import com.application.mail.data.model.MailId;
import com.application.mail.exceptions.InvalidMailIdException;

public class ZohoRepository extends MailRepository{

	private static ZohoRepository instance;
	private ZohoRepository(RepositoryDispatcher dispatcher) {
		super("zoho.com", dispatcher);
	}

	public static ZohoRepository getInstance(RepositoryDispatcher dispatcher)
	{
		if(instance==null)
		{
			synchronized(GmailRepository.class)
			{
				if(instance==null) 
				{
					instance=new ZohoRepository(dispatcher);
					instance.fillMockData();
				}
			}
		}

		return instance;

	}
	private void fillMockData()  {
		try {
			addAccount(new Account("jashwin",new MailId("roy@zoho.com"), "1234", null, 512542561));
			addAccount(new Account("kishore",new MailId("rocky@zoho.com"), "1234", null, 512542561));
		} catch (InvalidMailIdException e) {
			e.printStackTrace();
		}
	}
}


