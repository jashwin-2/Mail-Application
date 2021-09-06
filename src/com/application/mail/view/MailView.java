package com.application.mail.view;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.application.mail.exceptions.*;
import com.application.mail.data.MailRepository;
import com.application.mail.data.model.Account;
import com.application.mail.data.model.Mail;
import com.application.mail.data.model.Mail.Type;
import com.application.mail.data.model.MailId;

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
		String name,password,mailId;
		Long mobileNo;
		System.out.println("Enter your name : ");
		name = sc.nextLine();
		try {
			System.out.println("Enter your Mobile No : ");
			mobileNo= Long.parseLong(sc.nextLine());

			while(true)
			{
				System.out.println("Enter the mail id you want wihtout domain name : ");
				mailId=sc.nextLine();

				if(mailId.contains("@") || !MailId.isValidMail(mailId+="@"+repository.getDomainName())) {
					System.out.println("Entered mail is id invalid only use Alphabets , numbers , . and _ ");
					continue;
				}
				if(repository.contains(mailId)) {
					System.out.println("Mail id "+mailId+" is already taken enter a different mail id");
					continue;
				}
				break;
			}
			System.out.println("Enter your password : ");
			password=sc.nextLine();
			repository.addAccount(new Account(name, new MailId(mailId), password, null, mobileNo));
			System.out.println("Account created succesfully..\nyour mailId : "+mailId);
			loginView();
		}
		catch(Exception exp)
		{
			onExceptionCaught(exp);
			createAccount();
		}
	}


	public void login()
	{

		System.out.println("Enter your Mail Id");
		try {
			mailid = new MailId(sc.nextLine());
			//repository=dispatcher.getRepository(mailid.getDomain());
			if(repository.contains(mailid.getId()))
				System.out.println("Enter your password");
			else 
				throw new AccountNotFoundException(mailid.getId());
			String password=sc.nextLine();
			account=repository.authenticate(mailid.getId(), password);
		}
		catch(Exception exp)
		{
			onExceptionCaught(exp);
			login();
		}
		if(account!=null)
			showMenu();
	}


	private void onExceptionCaught(Exception exp) {
		if(exp instanceof AccountNotFoundException)
			System.out.println("Entered Account "+((AccountNotFoundException)exp).getId()+" Not found");
		else if(exp instanceof InvalidPasswordException)
			System.out.println("The password you entered is in correct");
		else if(exp instanceof DomainNotFoundException)
			System.out.println("Entered Domain "+ ((DomainNotFoundException)exp).getDomainName()+" Not Found");
		else if(exp instanceof InvalidMailIdException)
			System.out.println("Entered mail ID is in Correct ");
		else 
			System.out.println("Invalid Input ");

	}

	private void showMenu() {
		System.out.println("******* Menu *******");
		MenuItems[] options = MenuItems.values();
		for (MenuItems option : options) {
			System.out.println(option.number + " . " + option.text);
		}
		try {
			int option = Integer.parseInt(sc.nextLine());
			onMenuItemSelected(MenuItems.getValue(option));
		}catch(NumberFormatException e)
		{
			onExceptionCaught(e);
			showMenu();
		}
	}


	private void onMenuItemSelected(MenuItems value) {
		switch(value)
		{
		case COMPOSE_MAIL:
			composeMail(null);
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
		case DRAFT:
			showMails(Mail.Type.DRAFT);
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
		List<Mail> allMails=account.getAllMails();

		if(allMails.size()==0) {
			System.out.println("You don't have any mail to show");
			return;
		}
		System.out.println("************ All Mails ***********");
		System.out.println("Sno\tName\tSubject\t\tType");
		for(int i=allMails.size()-1;i>=0;i--)
		{
			Mail mail=allMails.get(i);
			mails.add(mail);
			if(mail.getType().equals(Mail.Type.RECEIVED))
				System.out.println(count+++"\t"+mail.getSender().getName()+"\t"+mail.getSubject()+"\t\t"+mail.getType());
			else
				System.out.println(count+++"\t"+mail.getReceiver().getName()+"\t"+mail.getSubject()+"\t\t"+mail.getType());
		}
		getChoiceToOpen(mails,true);
	}


	private void showMails(Type type) {
		int count=1;
		List<Mail> mails=new ArrayList<>();
		List<Mail> allMails=account.getAllMails();
		if(allMails.size()==0) {
			System.out.println("You don't have any "+type.text+" to show");
			return;
		}
		System.out.println("************ "+ type.text+ "***********");
		System.out.println("Sno\tName\tSubject");
		for(int i=allMails.size()-1;i>=0;i--) {
			Mail mail=allMails.get(i);

			if(mail.getType().equals(type)) {
				mails.add(mail);
				if(mail.getType().equals(Mail.Type.RECEIVED))
					System.out.println(count+++"\t"+mail.getSender().getName()+"\t"+mail.getSubject());
				else
					System.out.println(count+++"\t"+mail.getReceiver().getName()+"\t"+mail.getSubject());
			}
		}
		if(count==1) {
			System.out.println("You don't have any "+type.text+" to show");
			return;
		}
		getChoiceToOpen(mails,false);

	}

	public void getChoiceToOpen(List<Mail> mails,boolean isCalledFromAllMails)
	{
		int choice=0;
		System.out.println("\nWhich one(Sno) do you want to open \t\t Press 0 to for previous menu");

		try {
			choice=Integer.parseInt(sc.nextLine());
			if(choice == 0)
				return;
			onOpen(mails.get(choice-1));

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
		System.out.println("From : "+mail.getSender()+"\tTo : "+mail.getReceiver());
		System.out.println("\nSubject : "+mail.getSubject()+"\n\n\t"+mail.getBody()+"\n\nAttachment : "+mail.getAttachment());
		System.out.print("Enter your choice \n 0--> Back \t1-->Reply\t2-->Delete Mail\t");
		if(mail.getType().equals(Mail.Type.DRAFT))
			System.out.println("3--> Send Mail");
		try {
			int choice=Integer.parseInt(sc.nextLine());

			if(choice==1)
				composeMail(mail.getType().equals(Mail.Type.RECEIVED) ? mail.getSender() : mail.getReceiver());
			else if(choice==2) {
				account.deleteMail(mail);
				System.out.println("Mail deleted Succesfully...!");
			}
			else if(choice == 3) {
				mail.setType(Mail.Type.SENT);
				account.deleteMail(mail);
				sendMail(mail);
			}
			else if(choice==0)
				return;
		}catch(NumberFormatException e)
		{
			onExceptionCaught(e);
			onOpen(mail);
		}
	}


	private void composeMail(MailId mailId) {
		String subject,body,attachement;
		MailId receiverMailid = mailId;
		if(receiverMailid==null)
		{
			System.out.println("Enter receivers Mail Id ");
			try {
				receiverMailid=new MailId(sc.nextLine());
				if(!repository.isValid(receiverMailid))
					throw new AccountNotFoundException(receiverMailid.getId());
			}
			catch(AccountNotFoundException | InvalidMailIdException | DomainNotFoundException exp)
			{
				onExceptionCaught(exp);
				receiverMailid=null;
				composeMail(null);
			}
		}
		if(receiverMailid==null)
			return;
		System.out.println("************* COMPOSE *************\nFrom : \t"+account.getUserId()+"\t To: "+receiverMailid);
		System.out.print("Subject : \t");
		subject=sc.nextLine();
		System.out.println();
		System.out.print("Body :\t");
		body=sc.nextLine();
		System.out.println();
		System.out.print("Add attachement  : \t");
		attachement=sc.nextLine();
		Mail mail=new Mail(account.getUserId(), receiverMailid, subject, body, attachement, Mail.Type.SENT);
		while(true)
		{
			System.out.println("Enter your chouce \n 1--> Send Mail \t 2--> Save as Draft");
			try {
				int choice=Integer.parseInt(sc.nextLine());
				if(choice==1){
					sendMail(mail);
					break;
				}
				else if(choice==2){
					mail.setType(Mail.Type.DRAFT);
					account.addInMail(mail);
					System.out.println("Mail saved as Draft Succesfully...!");
					break;
				}
			}catch(NumberFormatException e){
				onExceptionCaught(e);
				continue;
			}
		}
	}


	private void sendMail(Mail mail) {
		try {
			repository.sendMail(mail);
			System.out.println("\nMail sent succesfully..!\n");
		} catch (AuthenticationFailedException | DomainNotFoundException | CloneNotSupportedException e) {
			onExceptionCaught(e);
			showMenu();
		}
	}

}
