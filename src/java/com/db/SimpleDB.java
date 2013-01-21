package com.db;

import java.io.IOException;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.server.Constants;

import org.json.*;

public class SimpleDB 
{
	private static SimpleDB singleton;
	private AmazonSimpleDB sdb;
	
	// Private constructor
	private SimpleDB() 
	{
		try
		{
			sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
					this.getClass().getClassLoader().getResourceAsStream("com/db/AwsCredentials.properties")));
			sdb.setEndpoint("sdb.us-west-2.amazonaws.com");
		}
		catch (IOException ex)
		{
			Constants.logger.error("Error : " + ex.getMessage());
		}
	}
	
	// This is used for both inserts and updates
	public void updateItem(String domainName, String itemName, List<ReplaceableAttribute> attributes)
	{			
		try
		{
			PutAttributesRequest request = new PutAttributesRequest(domainName, itemName, attributes);
			sdb.putAttributes(request);
		} 
		catch (AmazonServiceException ase) 
		{
			Constants.logger.error("Caught an AmazonServiceException, which means your request made it "
	                + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
			Constants.logger.error("Error Message:    " + ase.getMessage());
			Constants.logger.error("HTTP Status Code: " + ase.getStatusCode());
			Constants.logger.error("AWS Error Code:   " + ase.getErrorCode());
			Constants.logger.error("Error Type:       " + ase.getErrorType());
			Constants.logger.error("Request ID:       " + ase.getRequestId());
	    } 
		catch (AmazonClientException ace) 
	    {
			Constants.logger.error("Caught an AmazonClientException, which means the client encountered "
	                + "a serious internal problem while trying to communicate with SimpleDB, "
	                + "such as not being able to access the network.");
			Constants.logger.error("Error Message: " + ace.getMessage());
	    }
	}
	
	public List<Item> selectQuery(String queryString)
	{
		try
		{
			SelectRequest selectRequest = new SelectRequest(queryString);
			
			return sdb.select(selectRequest).getItems();
		} 
		catch (AmazonServiceException ase) 
		{
			Constants.logger.error("Caught an AmazonServiceException, which means your request made it "
	                + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
			Constants.logger.error("Error Message:    " + ase.getMessage());
			Constants.logger.error("HTTP Status Code: " + ase.getStatusCode());
			Constants.logger.error("AWS Error Code:   " + ase.getErrorCode());
			Constants.logger.error("Error Type:       " + ase.getErrorType());
			Constants.logger.error("Request ID:       " + ase.getRequestId());
	    } 
		catch (AmazonClientException ace) 
	    {
			Constants.logger.error("Caught an AmazonClientException, which means the client encountered "
	                + "a serious internal problem while trying to communicate with SimpleDB, "
	                + "such as not being able to access the network.");
			Constants.logger.error("Error Message: " + ace.getMessage());
	    }

		return null;
	}
	
	public JSONArray selectQueryAsJSON(String queryString)
	{
		// Results consist of an array of JSONObjects. Each JSONObject
		// contains the attributes for a single item. 
		JSONArray results = new JSONArray();
		
		try
		{
			SelectRequest selectRequest = new SelectRequest(queryString);
			
	        for (Item item : sdb.select(selectRequest).getItems()) 
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
	                results.put(currentItem);
	        	}
	        	catch (JSONException ex)
	        	{
	        		Constants.logger.error("Error : " + ex.getMessage());
	        	}
	        }
		} 
		catch (AmazonServiceException ase) 
		{
			Constants.logger.error("Caught an AmazonServiceException, which means your request made it "
	                + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
			Constants.logger.error("Error Message:    " + ase.getMessage());
			Constants.logger.error("HTTP Status Code: " + ase.getStatusCode());
			Constants.logger.error("AWS Error Code:   " + ase.getErrorCode());
			Constants.logger.error("Error Type:       " + ase.getErrorType());
			Constants.logger.error("Request ID:       " + ase.getRequestId());
	    } 
		catch (AmazonClientException ace) 
	    {
			Constants.logger.error("Caught an AmazonClientException, which means the client encountered "
	                + "a serious internal problem while trying to communicate with SimpleDB, "
	                + "such as not being able to access the network.");
			Constants.logger.error("Error Message: " + ace.getMessage());
	    }

		return results;
	}
	
	public void deleteItem(String domainName, String itemName)
	{
		try
		{
			sdb.deleteAttributes(new DeleteAttributesRequest(domainName, itemName));
		} 
		catch (AmazonServiceException ase) 
		{
			Constants.logger.error("Caught an AmazonServiceException, which means your request made it "
	                + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
			Constants.logger.error("Error Message:    " + ase.getMessage());
			Constants.logger.error("HTTP Status Code: " + ase.getStatusCode());
			Constants.logger.error("AWS Error Code:   " + ase.getErrorCode());
			Constants.logger.error("Error Type:       " + ase.getErrorType());
			Constants.logger.error("Request ID:       " + ase.getRequestId());
	    } 
		catch (AmazonClientException ace) 
	    {
			Constants.logger.error("Caught an AmazonClientException, which means the client encountered "
	                + "a serious internal problem while trying to communicate with SimpleDB, "
	                + "such as not being able to access the network.");
			Constants.logger.error("Error Message: " + ace.getMessage());
	    }
	}
	
	// Disable cloning for singletons
	public Object clone() throws CloneNotSupportedException 
	{
		throw new CloneNotSupportedException();
	}
	
	// Singleton 
	public static synchronized SimpleDB getInstance() 
	{
		if (singleton == null) 
		{
			singleton = new SimpleDB();
		}
		return singleton;
	}
}
