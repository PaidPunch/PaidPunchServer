package com.app;

import com.server.Constants;

import java.io.IOException;
import java.io.PrintWriter;

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
}
