package com.server;

import java.io.InputStream;
import java.util.Date;
import org.json.JSONObject;

public class TemplatesList 
{
	private static TemplatesList singleton;
	private Date lastRefreshTime;
	private JSONObject currentTemplates;
	
	// Private constructor
	private TemplatesList() 
	{
		lastRefreshTime = null;
		currentTemplates = null;
	}
	
	private boolean timeForRefresh()
	{
		if (lastRefreshTime != null)
		{
			Date currentTime = new Date();
			// Refresh interval in milliseconds
			final long refreshInterval = 15 * 60 * 1000L;
			Date refreshTime = new Date(lastRefreshTime.getTime() + refreshInterval);
			return (currentTime.compareTo(refreshTime) > 0);	
		}
		else
		{
			return true;
		}
	}
	
	private void refreshTemplatesFromFilesIfNecessary()
	{
		if (timeForRefresh())
		{
			try
			{
				currentTemplates = new JSONObject();
				InputStream inFacebook = this.getClass().getClassLoader().getResourceAsStream("com/server/facebookTemplate");
	    		if (inFacebook != null)
	    		{
	    			String facebookTemplate = Utility.convertInputStreamToString(inFacebook);
	    			currentTemplates.put("facebook", facebookTemplate);
	    		}
	    		
	    		InputStream inEmail = this.getClass().getClassLoader().getResourceAsStream("com/server/emailTemplate");
	    		if (inEmail != null)
	    		{
	    			String emailTemplate = Utility.convertInputStreamToString(inEmail);
	    			currentTemplates.put("email", emailTemplate);
	    		}	
	    		
	    		lastRefreshTime = new Date();
			}
			catch (Exception ex)
	    	{
				Constants.logger.error("Error : " + ex.getMessage());
				lastRefreshTime = null;
				currentTemplates = null;
			}
		}
	}
	
	// Disable cloning for singletons
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public JSONObject getTemplates()
	{
		// Refresh the data if necessary
		refreshTemplatesFromFilesIfNecessary();
		return currentTemplates;
	}
	
	// Singleton 
	public static synchronized TemplatesList getInstance() 
	{
		if (singleton == null) 
		{
			singleton = new TemplatesList();
		}
		return singleton;
	}
}
