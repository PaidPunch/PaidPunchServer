package com.app;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.db.SimpleDB;

import com.server.ProposedBusinessList;
import com.server.Constants;

public class ProposedBusinesses extends XmlHttpServlet
{
	private static final long serialVersionUID = -269101922421407179L;

	@Override
    public void init(ServletConfig config) throws ServletException
    {
	   super.init(config);

	   try
	   {
		   ServletContext context = config.getServletContext();
		   Constants.loadJDBCConstants(context);
	   }
	   catch(Exception e)
	   {
		   Constants.logger.error(e);
	   }
    }
	
	private JSONObject suggestBusiness(JSONObject requestInputs, HttpServletRequest request, HttpServletResponse response)
    {
		JSONObject results = null;
		
		try
		{
			String user_id = requestInputs.getString(Constants.USERID_PARAMNAME);
			ArrayList<HashMap<String,String>> userResultsArray = getUserInfo(user_id, null);
			if (userResultsArray.size() == 1)
	    	{
				HashMap<String,String> userInfo = userResultsArray.get(0);
				String validSessionId = userInfo.get(Constants.SESSIONID_PARAMNAME);	
				if (validateSessionId(validSessionId, request))
				{
					// Get UUID for naming new vote
					UUID itemName = UUID.randomUUID();
					
					// Get current datetime
					SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String currentDatetime = datetimeFormat.format(new java.util.Date().getTime());	
					
					List<ReplaceableAttribute> listAttributes = new ArrayList<ReplaceableAttribute>();
					listAttributes.add(new ReplaceableAttribute("user_id", user_id, true));
					listAttributes.add(new ReplaceableAttribute("business_name", requestInputs.getString(Constants.BIZNAME_PARAMNAME), true));
					listAttributes.add(new ReplaceableAttribute("business_info", requestInputs.getString(Constants.BIZINFO_PARAMNAME), true));
					listAttributes.add(new ReplaceableAttribute("modifiedDatetime", currentDatetime, true));
					
					SimpleDB sdb = SimpleDB.getInstance();
					sdb.updateItem("SuggestBusinessesTest", itemName.toString(), listAttributes);
					
					results = new JSONObject();
					results.put("statusCode", "00");
					results.put("statusMessage", "Thank you for your vote!.");
				}
				else
				{
					errorResponse(response, "403", "You are already logged into a different device");
				}
	    	}
			else
			{
				// Could not find user
	    		Constants.logger.error("Error: Unable to find user with id: " + user_id);
	    		errorResponse(response, "404", "Could not find user");
	    	}
		}
		catch (JSONException ex) 
		{
			errorResponse(response, "500", "Unable to retrieve businesses");
			Constants.logger.error("Error : " + ex.getMessage());
		}
		
		return results;
    }
	
	private JSONObject voteForBusiness(JSONObject requestInputs, String business_itemName, HttpServletRequest request, HttpServletResponse response)
    {
		JSONObject results = null;
		
		try
		{
			String user_id = requestInputs.getString(Constants.USERID_PARAMNAME);
			ArrayList<HashMap<String,String>> userResultsArray = getUserInfo(user_id, null);
			if (userResultsArray.size() == 1)
	    	{
				HashMap<String,String> userInfo = userResultsArray.get(0);
				String validSessionId = userInfo.get(Constants.SESSIONID_PARAMNAME);	
				if (validateSessionId(validSessionId, request))
				{
					// Get UUID for naming new vote
					UUID itemName = UUID.randomUUID();
					
					// Get current datetime
					SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String currentDatetime = datetimeFormat.format(new java.util.Date().getTime());	
					
					List<ReplaceableAttribute> listAttributes = new ArrayList<ReplaceableAttribute>();
					listAttributes.add(new ReplaceableAttribute("user_id", user_id, true));
					listAttributes.add(new ReplaceableAttribute("proposedbusiness_itemname", business_itemName, true));
					listAttributes.add(new ReplaceableAttribute("modifiedDatetime", currentDatetime, true));
					
					SimpleDB sdb = SimpleDB.getInstance();
					sdb.updateItem("VotesTest", itemName.toString(), listAttributes);
					
					results = new JSONObject();
					results.put("statusCode", "00");
					results.put("statusMessage", "Thank you for your vote!.");
				}
				else
				{
					errorResponse(response, "403", "You are already logged into a different device");
				}
	    	}
			else
			{
				// Could not find user
	    		Constants.logger.error("Error: Unable to find user with id: " + user_id);
	    		errorResponse(response, "404", "Could not find user");
	    	}
		}
		catch (JSONException ex) 
		{
			errorResponse(response, "500", "Unable to retrieve businesses");
			Constants.logger.error("Error : " + ex.getMessage());
		}
		
		return results;
    }
	
	/**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {    	
    	try
    	{    		
        	float expectedAPIVersion = getExpectedVersion(request);
        	if (validateVersion(response, expectedAPIVersion))
        	{
        		boolean enabledOnly = false;
        		String enabledOnlyString = request.getHeader("enabled-only");
        		if (enabledOnlyString != null)
        		{
        			enabledOnly = enabledOnlyString.equals("1");
        		}
        		
        		ProposedBusinessList businesses = ProposedBusinessList.getInstance();
            	JSONArray responseMap = null;
            	if (enabledOnly)
            	{
            		responseMap = businesses.getEnabledBusinesses();
            	}
            	else
            	{
            		responseMap = businesses.getBusinesses();
            	}
            	
    			if (responseMap != null)
    			{            				
    				// Send a response to caller
        			jsonResponse(response, responseMap);
    			}
    			else
    			{
    				errorResponse(response, "500", "Unable to retrieve businesses");
    			}
        	}
    	}
    	catch (Exception ex)
    	{
    		errorResponse(response, "500", "Unable to retrieve businesses");
			Constants.logger.error("Error : " + ex.getMessage());
		}
    }
    
    /**
     * Handles the HTTP <code>PUT</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
    	try
    	{
    		JSONObject responseMap = null;
        	float expectedAPIVersion = getExpectedVersion(request);
        	if (validateVersion(response, expectedAPIVersion))
        	{
        		String pathInfo = request.getPathInfo();
        		if (pathInfo != null)
        		{
        			String[] pathArray = pathInfo.substring(1).split("/");
        			if (pathArray.length >= 1)
        			{
        				JSONObject requestInputs = getRequestData(request);	
        				
        				if (pathArray[0].equalsIgnoreCase("suggest"))
        				{
        					responseMap = suggestBusiness(requestInputs, request, response);
        				}
        				else
        				{
        					// assume it's a proposed business id (not the same as a business_userid) vote
        					// e.g. /ProposedBusinesses/1034/vote
        					String business_itemname = pathArray[0];
        					String requestType = pathArray[1];
        					if (requestType.equalsIgnoreCase("vote"))
                			{
                				responseMap = voteForBusiness(requestInputs, business_itemname, request, response);
                			}
        					else
        					{
        						errorResponse(response, "403", "Incorrect API usage");
        					}
        				}
        			}
        			else
        			{
        				errorResponse(response, "403", "Incorrect API usage");
        			}
        		}
        		
        		if (responseMap != null)
    			{
    				// Send a response to caller
        			jsonResponse(response, responseMap);
    			}
        		else
        		{
        			errorResponse(response, "400", "Unable to process voting request");
        		}
        	}
    	}
    	catch (Exception ex)
    	{
			Constants.logger.error("Error : " + ex.getMessage());
			errorResponse(response, "500", "An unknown error occurred");
		}
    }
}
