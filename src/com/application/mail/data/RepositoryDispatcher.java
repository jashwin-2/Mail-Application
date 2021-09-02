package com.application.mail.data;
import java.util.HashMap;
import java.util.Map;

import com.application.mail.exceptions.DomainNotFoundException;

public class RepositoryDispatcher {
	private  Map<String,MailRepository> repositorys;

	public RepositoryDispatcher(Map<String, MailRepository> repositorys) {
		this.repositorys = repositorys;
	}
	
	public MailRepository getRepository(String domainName) throws DomainNotFoundException
	{
		if(repositorys.get(domainName)==null)
			throw new DomainNotFoundException(domainName);
		else
			return repositorys.get(domainName);
	}
	
	public void addInRepositorys(MailRepository repository)
	{
		if(this.repositorys==null)
			this.repositorys=new HashMap<String,MailRepository>();
		repositorys.put(repository.getDomainName(), repository);
	}
}
