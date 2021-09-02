package com.application.mail.view;
import static java.lang.System.out;

import java.util.Scanner;
import com.application.mail.data.MailRepository;
import com.application.mail.data.RepositoryDispatcher;
import com.application.mail.data.model.Account;
import com.application.mail.data.model.Mail;
import com.application.mail.data.model.MailId;
import com.application.mail.exceptions.AccouuntNotFoundException;
import com.application.mail.exceptions.DomainNotFoundException;



public class LoginView {
	private RepositoryDispatcher dispatcher;
	private MailRepository repository;
	private Account account;
	private Scanner sc;
	private MailId mailid;
	public LoginView(RepositoryDispatcher dispatcher, MailRepository repository) {
		this.dispatcher = dispatcher;
		this.repository = repository;
		sc=new Scanner(System.in);
	}


	public void start()
	{

		System.out.println("Enter your Mail Id");
		try {
			mailid = new MailId(sc.nextLine());
			repository=dispatcher.getRepository(mailid.getDomain());
			if(repository.conatins(mailid.getId()))
				System.out.println("Enter your password");
			String password=sc.nextLine();
			account=repository.authenticate(mailid.getId(), password);
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			onExceptionCaught(exp);
			start();
		}
		showMenu();
	}


	private void onExceptionCaught(Exception exp) {


	}


	private void showMenu() {
		out.println("******* Menu *******");
		MenuItems[] options = MenuItems.values();
		for (MenuItems option : options) {
			out.println(option.number + " . " + option.text);
		}
		int option = Integer.parseInt(sc.nextLine());
		onMenuItemSelected(MenuItems.getValue(option));
	}


	private void onMenuItemSelected(MenuItems value) {
		switch(value)
		{
		case COMPOSE_MAIL:
			composeMail();
			break;
		case ALLMAILS:
			showMails();
			break;
		case RECEIVED:
			break;
		case SENT:
			break;
		case LOGOUT:
			start();
			break;
		}
		showMenu();

	}


	private void showMails() {
		for(Mail str : account.getAllMails())
			System.out.println(str.getReceiver()+str.getSubject()+str.getBody()+str.getAttachment()+" type"+str.getType());
	}


	private void composeMail() {
		String subject,body,attachement;
		MailId receiverMailid = null;
		System.out.println("Enter receivers Mail Id ");
		try {
			receiverMailid=new MailId(sc.nextLine());
			System.out.println("************* COMPOSE *************\nFrom : \t"+account.getUserId()+"\t To: "+receiverMailid);
		}
		catch(Exception exp)
		{
			onExceptionCaught(exp);
			composeMail();
		}
		System.out.print("Subject : \t");
		subject=sc.nextLine();
		System.out.println();
		System.out.print("Body :\t");
		body=sc.nextLine();
		System.out.println();
		System.out.print("Add attachement  : \t");
		attachement=sc.nextLine();

		Mail mail=new Mail(account.getUserId(), receiverMailid, subject, body, attachement, Mail.Type.SENT);
		sendMail(mail);
	}


	private void sendMail(Mail mail) {
		try {
			repository.sendMail(mail);
		} catch (AccouuntNotFoundException | DomainNotFoundException | CloneNotSupportedException e) {
			onExceptionCaught(e);
			showMenu();
		}
	}

}
