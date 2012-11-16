package com.app;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.server.Constants;

public class XmlHttpServlet extends HttpServlet 
{
	private static final long serialVersionUID = -567922895839259243L;
	
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
	
	protected void xmlResponse(HttpServletResponse p_response, HashMap<String, Object>responseMap)
            throws IOException 
    {
        try 
        {
        	p_response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = p_response.getWriter();
            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            
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
}
