package com.application.mail.view;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.application.mail.data.MailRepository;
import com.application.mail.data.model.Account;
import com.application.mail.data.model.Mail;
import com.application.mail.data.model.Mail.Type;
import com.application.mail.data.model.MailId;
import com.application.mail.exceptions.AccouuntNotFoundException;
import com.application.mail.exceptions.DomainNotFoundException;



public class MailView {

	private MailRepository repository;
	private String name;
	private Account account;
	private Scanner sc;
	private MailId mailid;
	public MailView(MailRepository repository,String name) {
		this.repository = repository;
		this.name=name;
		sc=new Scanner(System.in);
	}


	public void loginView() 
	{
		System.out.println("*********** Welcom to "+name+" Mails ************* \nEnter your choice\n1--> Login\n2--> Create Account\n3 --> Exit");
		String choice=sc.nextLine();
		if(choice.charAt(0)=='1')
			login();
		else if(choice.charAt(0)=='2')
			createAccount();
		else if(choice.charAt(0)=='3')
			return;
		else {
			System.out.println("Invalid input ");
			loginView();
		}
	}
	private void createAccount() {

	}


	public void login()
	{

		System.out.println("Enter your Mail Id");
		try {
			mailid = new MailId(sc.nextLine());
			//repository=dispatcher.getRepository(mailid.getDomain());
			if(repository.conatins(mailid.getId()))
				System.out.println("Enter your password");
			String password=sc.nextLine();
			account=repository.authenticate(mailid.getId(), password);
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			onExceptionCaught(exp);
			login();
		}
		showMenu();
	}


	private void onExceptionCaught(Exception exp) {


	}


	private void showMenu() {
		System.out.println("******* Menu *******");
		MenuItems[] options = MenuItems.values();
		for (MenuItems option : options) {
			System.out.println(option.number + " . " + option.text);
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
			showMails(Mail.Type.RECEIVED);
			break;
		case SENT:
			showMails(Mail.Type.SENT);
			break;
		case LOGOUT:
			loginView();
			return;
		}
		showMenu();

	}


	private void showMails() {
		int count=1;
		List<Mail> mails=new ArrayList<>();
		System.out.println("************ All Mails ***********");
		System.out.println("Sno\tName\tSubject\t\tType");
		for(Mail mail : account.getAllMails())
		{
			mails.add(count, mail);
			if(mail.getType().equals(Mail.Type.RECEIVED))
				System.out.println(count+++"\t"+mail.getSender().getName()+"\t"+mail.getSubject()+"\t\tReceived");
			else
				System.out.println(count+++"\t"+mail.getReceiver().getName()+"\t"+mail.getSubject()+"\tSent");
		}
		if(count==1) {
			System.out.println("You did not have any mails ");
			return;
		}
		getChoiceToOpen(mails,true);
	}


	private void showMails(Type type) {
		int count=1;
		List<Mail> mails=new ArrayList<>();
		System.out.println("************ "+ type.text+ "***********");
		System.out.println("Sno\tName\tSubject");
		for(Mail mail : account.getAllMails())
			if(mail.getType().equals(type)) {
				mails.add(mail);
				if(mail.getType().equals(Mail.Type.RECEIVED))
					System.out.println(count+++"\t"+mail.getSender().getName()+"\t"+mail.getSubject());
				else
					System.out.println(count+++"\t"+mail.getReceiver().getName()+"\t"+mail.getSubject());
			}
		if(count==1) {
			System.out.println("You don't have any "+type.text+" to show");
			return;
		}
		getChoiceToOpen(mails,false);

	}

	public void getChoiceToOpen(List<Mail> mails,boolean isCalledFromAllMails)
	{
		System.out.println("Which one do you want to open ");

		try {
			int choice=Integer.parseInt(sc.nextLine());
			mails.get(choice-1);
		}catch(NumberFormatException | IndexOutOfBoundsException e)
		{
			System.out.println("Invalid input ");
			if(isCalledFromAllMails)
				showMails();
			else
				showMails(mails.get(0).getType());
		}
	}
	private void onOpen(Mail mail) {
		// TODO Auto-generated method stub

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
