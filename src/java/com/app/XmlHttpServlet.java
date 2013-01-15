package com.app;

import com.db.DataAccess;
import com.server.Constants;

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
	
	protected boolean validateVersion(HttpServletResponse response, float expectedVersion)
	{
		if (expectedVersion < minValidVersion)
		{
			errorResponse(response, "405", "There is a newer version of PaidPunch available. Please update to continue using this application.");
			return false;
		}
		return true;
	}
	
	protected boolean validateSessionId(String validSessionId, JSONObject requestInputs)
	{
		try 
		{
			String currentSessionId = requestInputs.getString(Constants.SESSIONID_PARAMNAME);
			return validSessionId.equals(currentSessionId);
		}
		catch (JSONException ex) 
		{
			Constants.logger.error("Error : " + ex.getMessage());
		}
		return false;
	}
	
	protected void jsonResponse(HttpServletResponse response, JSONObject responseMap)
            throws IOException 
    {
        try 
        {
        	response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            
            String res = responseMap.toString();
            
            out.print(res);
            Constants.logger.info(res);
        } 
        catch (Exception e) 
        {
            Constants.logger.error(e);
        }

    }
	
	protected void jsonResponse(HttpServletResponse response, JSONArray responseArray)
            throws IOException 
    {
        try 
        {
        	response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            
            String res = responseArray.toString();
            
            out.print(res);
            Constants.logger.info(res);
        } 
        catch (Exception e) 
        {
            Constants.logger.error(e);
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
            Constants.logger.info(responseString);
        } 
        catch (Exception e) 
        {
            Constants.logger.error(e);
        }

    }
	
	protected void errorResponse(HttpServletResponse response, String statusCode, String statusMessage) 
	{
        try 
        {
        	response.setContentType("application/json");
        	PrintWriter out = response.getWriter();
            
        	JSONObject responseMap = new JSONObject().put("statusMessage", statusMessage);
            String res = responseMap.toString();
            
            out.print(res);
            response.setStatus(Integer.parseInt(statusCode));
            Constants.logger.info("Error response - Status Code: " + statusCode + ", Message: " + statusMessage);
        } 
        catch (Exception e) 
        {
            Constants.logger.error(e);
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
            Constants.logger.error(e);
        }
		
		// Something blew up, return null
		return null;
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
}
