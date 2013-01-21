package com.server;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.db.SimpleDB;

public class ProposedBusinessList 
{
	private static ProposedBusinessList singleton;
	private Date lastRefreshTime;
	private List<Item> currentBusinesses;
	private JSONArray arrayBusinesses;
	private JSONArray arrayEnabledBusinesses;
	
	private static String businessDomain = "BusinessesTest";
	
	// Private constructor
	private ProposedBusinessList() 
	{
		lastRefreshTime = null;
		currentBusinesses = null;
		arrayBusinesses = null;
		arrayEnabledBusinesses = null;
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
	
	private void refreshBusinessesFromSimpleDBIfNecessary()
	{
		if (timeForRefresh())
		{
			try
			{
				SimpleDB sdb = SimpleDB.getInstance();
				String allQuery = "select * from `" + businessDomain + "`";
				currentBusinesses = sdb.selectQuery(allQuery);
				
	    		lastRefreshTime = new Date();
	    		arrayBusinesses = null;
	    		arrayEnabledBusinesses = null;
			}
			catch (Exception ex)
	    	{
				Constants.logger.error("Error : " + ex.getMessage());
				lastRefreshTime = null;
				currentBusinesses = null;
			}
		}
	}
	
	// Disable cloning for singletons
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	private boolean isEnabled(Item current)
	{
		boolean enabled = false;
		for (Attribute attribute : current.getAttributes()) 
        {
        	if (attribute.getName().equalsIgnoreCase("status") &&
        		attribute.getValue().equalsIgnoreCase("enabled"))
        	{
        		enabled = true;
        		break;
        	}
        }
		return enabled;
	}
	
	public JSONArray getBusinesses()
	{
		// Refresh the data if necessary
		refreshBusinessesFromSimpleDBIfNecessary();
		
		if (arrayBusinesses == null)
		{
			arrayBusinesses = new JSONArray();
			for (Item item : currentBusinesses) 
	        {
	        	try
	        	{        		
	        		JSONObject currentItem = new JSONObject();
	        		
	        		// Store item name first
	        		currentItem.put("itemName", item.getName());
	        		
	        		// Store remaining attributes
	                for (Attribute attribute : item.getAttributes()) 
	                {
	                	currentItem.put(attribute.getName(), attribute.getValue());
	                }
	                
	                // Put current JSONObject (item) into larger results array
	                arrayBusinesses.put(currentItem);
	        	}
	        	catch (JSONException ex)
	        	{
	        		Constants.logger.error("Error : " + ex.getMessage());
	        	}
	        }	
		}
		
		return arrayBusinesses;
	}
	
	public JSONArray getEnabledBusinesses()
	{
		// Refresh the data if necessary
		refreshBusinessesFromSimpleDBIfNecessary();
		
		if (arrayEnabledBusinesses == null)
		{
			arrayEnabledBusinesses = new JSONArray();
			for (Item item : currentBusinesses) 
	        {
	        	try
	        	{    
	        		if (isEnabled(item))
	        		{
	        			JSONObject currentItem = new JSONObject();
	            		
	            		// Store item name first
	            		currentItem.put("itemName", item.getName());
	            		
	            		// Store remaining attributes
	                    for (Attribute attribute : item.getAttributes()) 
	                    {
	                    	currentItem.put(attribute.getName(), attribute.getValue());
	                    }
	                    
	                    // Put current JSONObject (item) into larger results array
	                    arrayEnabledBusinesses.put(currentItem);	
	        		}
	        	}
	        	catch (JSONException ex)
	        	{
	        		Constants.logger.error("Error : " + ex.getMessage());
	        	}
	        }	
		}
		
		return arrayEnabledBusinesses;
	}
	
	// Singleton 
	public static synchronized ProposedBusinessList getInstance() 
	{
		if (singleton == null) 
		{
			singleton = new ProposedBusinessList();
		}
		return singleton;
	}
}
