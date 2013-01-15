package com.server;

import com.db.DataAccess;
import com.db.DataAccess.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.json.*;

public class TemplatesList 
{
	private static TemplatesList singleton;
	private Date lastRefreshTime;
	private JSONArray currentTemplates;
	
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
	
	private void refreshTemplatesFromDatabaseIfNecessary()
	{
		if (timeForRefresh())
		{
			try
			{
				currentTemplates = new JSONArray();
				
				String queryString = "SELECT * FROM pptemplates;";
				try
				{
					DataAccess.queryDatabaseCustom(queryString, null, currentTemplates, new ResultSetHandler()
					{
						 public void handle(ResultSet results, Object returnObj) throws SQLException
		                 { 
							// The cast here is a well-known one, so the suppression is OK
							 //@SuppressWarnings("unchecked")
							 JSONArray arrayTemplates = (JSONArray)returnObj;
		                     while(results.next())
		                     {
		                    	 Template currentTemplate = new Template();
		                    	 
		                    	 // Populate product information
		                    	 currentTemplate.setTemplateId(results.getString("pptemplate_id"));
		                    	 currentTemplate.setName(results.getString("name"));
		                    	 currentTemplate.setDesc(results.getString("desc"));
		                    	 currentTemplate.setGroupId(results.getString("group_id"));
		                    	 currentTemplate.setGroupName(results.getString("group_name"));
		                    	 currentTemplate.setTemplate(results.getString("template"), results.getString("uses_file"));
		                    	 currentTemplate.setDisabled(results.getString("disabled"));
		                    	 currentTemplate.setModifiedDate(results.getString("date_modified"));
		                    	 
		                    	 // Add current template to the template list
		                    	 
		                    	 arrayTemplates.put(currentTemplate.getMapOfTemplate());
		                     } 
		                 }
					});
					lastRefreshTime = new Date();
				}
				catch (SQLException ex)
				{
					Constants.logger.error("Error : " + ex.getMessage());
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
	
	public JSONArray getTemplates()
	{
		// Refresh the data if necessary
		refreshTemplatesFromDatabaseIfNecessary();
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
