package com.app;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import com.server.Constants;

public class XmlHttpServlet extends HttpServlet 
{
	private static final long serialVersionUID = -567922895839259243L;
	
	protected void xmlResponse(HttpServletResponse p_response, HashMap<String, String>responseMap)
            throws IOException 
    {
        try 
        {
        	p_response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = p_response.getWriter();
            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            
            String xmlResponse = "";
            for (String name: responseMap.keySet())
            {
                String value = responseMap.get(name);  
                xmlResponse = xmlResponse + "<" + name + ">" + value + "</" + name + ">";
            } 
            
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
