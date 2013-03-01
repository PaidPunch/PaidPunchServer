package com.server;

import java.io.IOException;

import com.ecwid.mailchimp.*;
import com.ecwid.mailchimp.method.list.*;;

public class MailingListSubscribers 
{
	// Holds a subscriber's merge_vars info (see http://apidocs.mailchimp.com/api/1.3/listsubscribe.func.php )
	public static class MergeVars extends MailChimpObject 
	{
		private static final long serialVersionUID = -4193987762026109668L;
		
		@Field
	    public String EMAIL, FNAME, LNAME;

	    public MergeVars() { }

	    public MergeVars(String email) 
	    {
	        this.EMAIL = email;
	    }
	}
	
	private static MailingListSubscribers singleton;
	private MailChimpClient mailChimpClient;
	
	// Private constructor
	private MailingListSubscribers() 
	{
		mailChimpClient = new MailChimpClient(); 
	}
	
	// Singleton 
	public static synchronized MailingListSubscribers getInstance() 
	{
		if (singleton == null) 
		{
			singleton = new MailingListSubscribers();
		}
		return singleton;
	}
	
	public void subscribeToMailingList(String currentMail)
	{
		try
		{
			// Subscribe a person
			ListSubscribeMethod listSubscribeMethod = new ListSubscribeMethod();
			listSubscribeMethod.apikey = "3b36b9bee071e226ca34df164816f24b-us5";
			listSubscribeMethod.id = Constants.MAILCHIMP_LIST_ID;
			listSubscribeMethod.email_address = currentMail;
			listSubscribeMethod.double_optin = false;
			listSubscribeMethod.update_existing = true;
			listSubscribeMethod.merge_vars = new MailingListSubscribers.MergeVars(currentMail);
			mailChimpClient.execute(listSubscribeMethod);	
		}
		catch (IOException ex)
		{
			Constants.logger.error("Error : " + ex.getMessage());
		}
		catch (MailChimpException ex)
		{
			Constants.logger.error("Error : " + ex.getMessage());
		}
	}
}
