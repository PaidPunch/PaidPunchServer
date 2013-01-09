package com.app;

import com.server.ProductsList;
import com.server.TemplatesList;
import com.server.Utility;

import java.io.InputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.server.Constants;

public class Templates extends XmlHttpServlet {

	private static final long serialVersionUID = -8055302843390368457L;

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
        		TemplatesList templates = TemplatesList.getInstance();
            	JSONObject responseMap = templates.getTemplates();
    			if (responseMap != null)
    			{            				
    				// Send a response to caller
        			jsonResponse(response, responseMap);
    			}
    			else
    			{
    				errorResponse(response, "500", "Unable to retrieve templates");
    			}
        	}
    	}
    	catch (Exception ex)
    	{
			Constants.logger.error("Error : " + ex.getMessage());
		}
    }
}
