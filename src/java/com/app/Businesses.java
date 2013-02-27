package com.app;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.server.BusinessesList;
import com.server.Constants;

public class Businesses extends XmlHttpServlet
{
	private static final long serialVersionUID = -5166275496778467853L;
	
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
        		boolean enabledOnly = false;
        		String enabledOnlyString = request.getHeader("enabled-only");
        		if (enabledOnlyString != null)
        		{
        			enabledOnly = enabledOnlyString.equals("1");
        		}
        		
        		BusinessesList businesses = BusinessesList.getInstance();
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
        			jsonResponse(request, response, responseMap);
    			}
    			else
    			{
    				errorResponse(response, "500", "Unable to retrieve businesses");
    			}
        	}
    	}
    	catch (Exception ex)
    	{
			Constants.logger.error("Error : " + ex.getMessage());
		}
    }
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        CorsUtils.addOptionsCorsHeaderInfo(req, resp);
        super.doOptions(req, resp);
    }

}
