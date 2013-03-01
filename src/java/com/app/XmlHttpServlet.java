package com.app;

import com.db.DataAccess;
import com.server.Constants;
import com.server.SimpleLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletInputStream;

import org.apache.commons.io.IOUtils;
import org.json.*;

public class XmlHttpServlet extends HttpServlet 
{
	private static final long serialVersionUID = -567922895839259243L;
	private static final String versionHeader = "api-version";
	protected float minValidVersion = (float)1.0;
	protected String currentClassName;
	
	protected float getExpectedVersion(HttpServletRequest request)
	{
		String versionText = request.getHeader(versionHeader);
		if (versionText != null)
		{
			return Float.parseFloat(versionText);	
		}
		else
		{
			return (float)-1;
		}
	}
	
	protected boolean validateVersion(HttpServletRequest request, HttpServletResponse response, float expectedVersion)
	{
		if (expectedVersion < minValidVersion)
		{
			errorResponse(request, response, "405", "There is a newer version of PaidPunch available. Please update to continue using this application.");
			return false;
		}
		return true;
	}
	
	protected boolean validateSessionId(String validSessionId, HttpServletRequest request)
	{
		String currentSessionId = request.getHeader(Constants.SESSIONID_PARAMNAME);
		return validSessionId.equals(currentSessionId);
	}
	
	protected void jsonResponse(HttpServletRequest request, HttpServletResponse response, JSONObject responseMap)
            throws IOException 
    {
        try 
        {
        	response.setContentType("application/json");
        	CorsUtils.addCorsHeaderInfo(request, response);
            PrintWriter out = response.getWriter();
            
            String res = responseMap.toString();
            
            out.print(res);
            SimpleLogger.getInstance().info(currentClassName, res);
        } 
        catch (Exception e) 
        {
            SimpleLogger.getInstance().error(currentClassName, e.getMessage());
        }

    }
	
	protected void jsonResponse(HttpServletRequest request, HttpServletResponse response, JSONArray responseArray)
            throws IOException 
    {
        try 
        {
        	response.setContentType("application/json");
        	CorsUtils.addCorsHeaderInfo(request, response);
            PrintWriter out = response.getWriter();
            
            String res = responseArray.toString();
            
            out.print(res);
            SimpleLogger.getInstance().info(currentClassName, res);
        } 
        catch (Exception e) 
        {
            SimpleLogger.getInstance().error(currentClassName, e.getMessage());
        }

    }
	
	protected void stringResponse(HttpServletResponse response, String responseString)
            throws IOException 
    {
        try 
        {
        	response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(responseString);
            SimpleLogger.getInstance().info(currentClassName, responseString);
        } 
        catch (Exception e) 
        {
            SimpleLogger.getInstance().error(currentClassName, e.getMessage());
        }

    }
	
	protected void errorResponse(HttpServletRequest request, HttpServletResponse response, String statusCode, String statusMessage) 
	{
        try 
        {
        	response.setContentType("application/json");
        	CorsUtils.addCorsHeaderInfo(request, response);
        	PrintWriter out = response.getWriter();
            
        	JSONObject responseMap = new JSONObject().put("statusMessage", statusMessage);
            String res = responseMap.toString();
            
            out.print(res);
            response.setStatus(Integer.parseInt(statusCode));
            SimpleLogger.getInstance().info(currentClassName, "Status Code:" + statusCode + "|Message:" + statusMessage);
        } 
        catch (Exception e) 
        {
            SimpleLogger.getInstance().error(currentClassName, e.getMessage());
        }
    }
	
	protected JSONObject getRequestData(HttpServletRequest request)
	{
		try
		{
			ServletInputStream inputStream = request.getInputStream();
	
			// Retrieve raw input from the input stream
	        byte[] body = IOUtils.toByteArray(inputStream);
	        String rawJSONString = new String(body, "ASCII");
	        
	        JSONObject current = new JSONObject(rawJSONString);
	        
	        // Return the parsed data as a HashMap
	        return current;
		}
    	catch (Exception e) 
        {
    	    SimpleLogger.getInstance().error(currentClassName, e.getMessage());
        }
		
		// Something blew up, return null
		return null;
	}
	
	protected String[] getPathInfoArray(HttpServletRequest request)
	{
		// Split the path info into component strings
		String pathInfo = request.getPathInfo();
		if (pathInfo != null)
		{
			return pathInfo.substring(1).split("/");
		}
		else
		{
			return null;
		}
	}
	
	// Get user info
	protected ArrayList<HashMap<String,String>> getUserInfo(String user_id, Connection conn)
	{
		ArrayList<HashMap<String,String>> resultsArray = null;
		if (user_id != null)
		{		
			String queryString = null;
			if (conn != null)
			{
				// Connection not being null implies that we're doing a full transaction 
				// across multiple database calls
				queryString = "SELECT * FROM app_user WHERE user_id = ? FOR UPDATE;";	
			}
			else
			{
				queryString = "SELECT * FROM app_user WHERE user_id = ?;";
			}
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(user_id);
			resultsArray = DataAccess.queryDatabase(conn, queryString, parameters);
		}
		return resultsArray;
	}
	
	// Get punchcard info
	protected ArrayList<HashMap<String,String>> getPunchCardInfo(String punchcardid)
	{
		ArrayList<HashMap<String,String>> resultsArray = null;
		if (punchcardid != null)
		{
			String queryString = "SELECT * FROM punch_card WHERE punch_card_id = ?;";
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(punchcardid);
			resultsArray = DataAccess.queryDatabase(queryString, parameters);
		}
		return resultsArray;
	}
	
	// Get product info
	protected ArrayList<HashMap<String,String>> getProductInfo(String product_id)
	{
		ArrayList<HashMap<String,String>> resultsArray = null;
		if (product_id != null)
		{		
			String queryString = "SELECT * FROM products WHERE product_id = ?;";
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(product_id);
			resultsArray = DataAccess.queryDatabase(queryString, parameters);
		}
		return resultsArray;
	}
	
	// Get business info
	protected ArrayList<HashMap<String,String>> getBusinessInfo(String businessid)
	{
		ArrayList<HashMap<String,String>> resultsArray = null;
		if (businessid != null)
		{
			String queryString = "SELECT * FROM business_users WHERE business_userid = ?;";
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(businessid);
			resultsArray = DataAccess.queryDatabase(queryString, parameters);
		}
		return resultsArray;
	}
}
