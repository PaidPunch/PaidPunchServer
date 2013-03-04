package com.app;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.server.BusinessesList;
import com.server.BusinessesList2;
import com.server.Constants;
import com.server.SimpleLogger;

public class Businesses extends XmlHttpServlet
{
	private static final long serialVersionUID = -5166275496778467853L;
	
	@Override
    public void init(ServletConfig config) throws ServletException
    {
	   super.init(config);
	   currentClassName = Businesses.class.getSimpleName();

	   try
	   {
		   ServletContext context = config.getServletContext();
		   Constants.loadJDBCConstants(context);
	   }
	   catch(Exception e)
	   {
		   SimpleLogger.getInstance().error(currentClassName, e.getMessage());
	   }
    }
	
	// Process /Businesses requests
	private void getAllBusinesses(HttpServletRequest request, HttpServletResponse response, BusinessesList2 businesses, boolean enabledOnly)
	{
	    JSONArray responseMap = null;
        if (enabledOnly)
        {
            responseMap = businesses.getAllEnabledBusinesses();
        }
        else
        {
            responseMap = businesses.getAllBusinesses();
        }
        
        if (responseMap != null)
        {                           
            try
            {
                // Send a response to caller
                jsonResponse(request, response, responseMap);    
            }
            catch (IOException e)
            {
                SimpleLogger.getInstance().error(currentClassName, e);
                errorResponse(request, response, "500", "Unable to retrieve businesses");
            }
        }
        else
        {
            errorResponse(request, response, "500", "Unable to retrieve businesses");
        } 
	}
	
	// Process /Businesses requests
    private void getSingleBusiness(HttpServletRequest request, HttpServletResponse response, BusinessesList2 businesses, boolean enabledOnly, String business_id)
    {
        JSONObject responseMap = businesses.getSingleBusiness(business_id, enabledOnly);
        if (responseMap != null)
        {                           
            try
            {
                // Send a response to caller
                jsonResponse(request, response, responseMap);    
            }
            catch (IOException e)
            {
                SimpleLogger.getInstance().error(currentClassName, e);
                errorResponse(request, response, "500", "Unable to retrieve business");
            }
        }
        else
        {
            errorResponse(request, response, "404", "Unable to retrieve business");
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
        	if (validateVersion(request, response, expectedAPIVersion))
        	{
        	    boolean enabledOnly = false;
                String enabledOnlyString = request.getHeader("enabled-only");
                if (enabledOnlyString != null)
                {
                    enabledOnly = enabledOnlyString.equals("1");
                }
                
                String APIVersionText = Float.toString(expectedAPIVersion);
                
        	    if (APIVersionText.equals("1.0"))
        	    {                    
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
                        errorResponse(request, response, "500", "Unable to retrieve businesses");
                    }    
        	    }
        	    else if (APIVersionText.equals("1.1"))
        	    {
        	        BusinessesList2 businesses = BusinessesList2.getInstance();
                    String[] pathArray = getPathInfoArray(request);
                    
                    // /Businesses
                    if (pathArray == null)
                    {
                        getAllBusinesses(request, response, businesses, enabledOnly);
                    }
                    else
                    {
                        String business_id = pathArray[0];
                        getSingleBusiness(request, response, businesses, enabledOnly, business_id);
                    } 
        	    }
        	    else
        	    {
        	        SimpleLogger.getInstance().error(currentClassName, "UnknownAPIVersion");
        	        errorResponse(request, response, "405", "Unknown API version");
        	    }
        	}
    	}
    	catch (Exception ex)
    	{
    	    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
		}
    }
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        CorsUtils.addOptionsCorsHeaderInfo(req, resp);
        super.doOptions(req, resp);
    }

}
