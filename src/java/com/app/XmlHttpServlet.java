package com.app;

import com.server.Constants;
import com.server.XmlRequestParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletInputStream;

import org.xml.sax.InputSource;

public class XmlHttpServlet extends HttpServlet 
{
	private static final long serialVersionUID = -567922895839259243L;
	private static final String versionHeader = "api-version";
	protected float minValidVersion = (float)1.0;
	
	private String processHashMap(HashMap<String, Object>responseMap)
	{
		String xmlResponse = "";
        for (String name: responseMap.keySet())
        {
            Object value = responseMap.get(name);
            if (value instanceof HashMap)
            {
            	xmlResponse = xmlResponse + "<" + name + ">";
            	// The cast here is a well-known one, so the suppression is OK
				 @SuppressWarnings("unchecked")
            	HashMap<String, Object> newMap = (HashMap<String, Object>)value;
            	xmlResponse = xmlResponse + processHashMap(newMap);
            	xmlResponse = xmlResponse + "</" + name + ">";
            }
            else if (value instanceof ArrayList)
            {
            	xmlResponse = xmlResponse + "<" + name + ">";
            	// The cast here is a well-known one, so the suppression is OK
				 @SuppressWarnings("unchecked")
            	ArrayList<HashMap<String, Object>> mapArray = (ArrayList<HashMap<String, Object>>)value;
				for (HashMap<String, Object> current : mapArray)
				{
					xmlResponse = xmlResponse + processHashMap(current);
				}
				xmlResponse = xmlResponse + "</" + name + ">";
            }
            else
            {
            	xmlResponse = xmlResponse + "<" + name + ">" + value + "</" + name + ">";	
            }
        } 
        return xmlResponse;
	}
	
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
	
	// Generate XML response from hashmap. There're 3 types of hashmaps:
	// 1. Key - String value: <key>value</key>
	// 2. Key - Hashmap: <key>[hashmap->xml]</key>, e.g. <places><address><street>....</street></address></places>
	// 3. Key - Array: <keys><key>...</key><key>...</key>......</keys>
	protected void xmlResponse(HttpServletResponse response, HashMap<String, Object>responseMap)
            throws IOException 
    {
        try 
        {
        	response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            
            String xmlResponse = processHashMap(responseMap);
            
            String res = "<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + xmlResponse
                    + "</paidpunch-resp>";
            
            out.print(res);
            Constants.logger.info(res);
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
            response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            String res = "<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + "<statusCode>" + statusCode + "</statusCode>"
                    + "<statusMessage>" + statusMessage + "</statusMessage>"
                    + "</paidpunch-resp>";
            PrintWriter out = response.getWriter();
            out.print(res);
            response.setStatus(Integer.parseInt(statusCode));
            Constants.logger.info("Error response - Status Code: " + statusCode + ", Message: " + statusMessage);
        } 
        catch (Exception e) 
        {
            Constants.logger.error(e);
        }
    }
	
	protected HashMap<String, String> getRequestData(HttpServletRequest request, ArrayList<String> validElements)
	{
		try
		{
			ServletInputStream inputStream = request.getInputStream();
			XmlRequestParser parser = new XmlRequestParser(validElements);
	
			// Retrieve raw input from the input stream
	        int info;
	        StringBuffer rawXMLInput = new StringBuffer();
	        while ((info = inputStream.read()) != -1) 
	        {
	            char temp;
	            temp = (char) info;
	            rawXMLInput.append(temp);
	        }
	
	        // Trim the XML request data and then parse it as a UTF-8 string
	        String xmldata = new String(rawXMLInput);
	        xmldata = xmldata.trim();        
	        InputSource iSource = new InputSource(new StringReader(xmldata));
	        iSource.setEncoding("UTF-8");
	        parser.parseDocument(iSource);
	        
	        // Return the parsed data as a HashMap
	        return parser.getData();
		}
    	catch (Exception e) 
        {
            Constants.logger.error(e);
        }
		
		// Something blew up, return null
		return null;
	}
}
