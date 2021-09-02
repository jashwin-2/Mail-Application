package com.application.mail;

import com.application.mail.data.MailRepository;
import com.application.mail.data.RepositoryDispatcher;
import com.application.mail.data.model.Account;
import com.application.mail.data.model.MailId;
import com.application.mail.exceptions.InvalidMailIdException;
import com.application.mail.view.LoginView;

public class Main {

	public static void main(String[] args) {
		
		RepositoryDispatcher dispatcher= new RepositoryDispatcher(null);
		MailRepository re=new MailRepository("gmail.com",dispatcher);
		dispatcher.addInRepositorys(re);
		try {
			re.addAccount(new Account("jashwin",new MailId("abc@gmail.com"), "1234", null, 512542561));
			re.addAccount(new Account("jashwin",new MailId("cde@gmail.com"), "1234", null, 512542561));
		} catch (InvalidMailIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new LoginView(dispatcher, null).start();

	}

}
