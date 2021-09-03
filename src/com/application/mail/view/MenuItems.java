package com.application.mail.view;

public enum MenuItems {
	COMPOSE_MAIL(1,"Compose mail"),
	ALLMAILS(2,"Show All Mails"),
	SENT(3,"Show Sent Mails"),			
	RECEIVED(4,"Show Received Mails"),
	DRAFT(5,"Show draft mails"),
	LOGOUT(6,"Log out");

	public final int number;
	public final String text;
	MenuItems(int optNum, String text) {
		this.number=optNum;
		this.text=text;
	}
	
	public static MenuItems getValue(int option) {
        for (MenuItems item : values()) {
            if (item.number == option) {
                return item;
            }
        }
        return null;
    }
	
}
