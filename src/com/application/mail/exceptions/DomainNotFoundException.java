package com.application.mail.exceptions;

public class DomainNotFoundException extends Exception {
private final String domainName;

public DomainNotFoundException(String domainName) {
	this.domainName = domainName;
}

public String getDomainName() {
	return domainName;
}

}
