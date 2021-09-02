package com.application.mail;

import java.util.Scanner;
import com.application.mail.data.GmailRepository;
import com.application.mail.data.RepositoryDispatcher;
import com.application.mail.data.ZohoRepository;
import com.application.mail.view.MailView;

public class Main {

	public static void main(String[] args) {
		RepositoryDispatcher dispatcher=new RepositoryDispatcher(null);
		GmailRepository gmail=GmailRepository.getInstance(dispatcher);
		ZohoRepository zoho=ZohoRepository.getInstance(dispatcher);
		
		dispatcher.addInRepositorys(gmail);
		dispatcher.addInRepositorys(zoho);
		Scanner sc=new Scanner(System.in);
		while(true)
		{
			
			System.out.println("Which application do you want to open \n1-->  Zoho Mail\n2-->  Gmail");
			if(Integer.parseInt(sc.nextLine())==1)
				new MailView(zoho, "Zoho").loginView();
			else
				new MailView(gmail,"Gmail").loginView();
		}
		
	}

}
