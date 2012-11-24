package com.app;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.DataAccess;
import com.server.Constants;
import com.server.Utility;

public class Punches extends XmlHttpServlet 
{
	private static final long serialVersionUID = -5700319928859466433L;
	
	private ArrayList<String> postElements;

	@Override
    public void init(ServletConfig config) throws ServletException
    {
	   super.init(config);

	   try
	   {
		   ServletContext context = config.getServletContext();
		   Constants.loadJDBCConstants(context);
		   
		   initializePostElements();
	   }
	   catch(Exception e)
	   {
		   Constants.logger.error(e);
	   }
    }
	
	private void initializePostElements()
	{
		postElements = new ArrayList<String>();
		postElements.add(Constants.USERID_PARAMNAME);
		postElements.add(Constants.PUNCHCARDID_PARAMNAME);
	}
	
	// Get user info
	private ArrayList<HashMap<String,String>> getUserInfo(String user_id)
	{
		ArrayList<HashMap<String,String>> resultsArray = null;
		if (user_id != null)
		{
			String queryString = "SELECT credit FROM app_user WHERE user_id = ?;";
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(user_id);
			resultsArray = DataAccess.queryDatabase(queryString, parameters);
		}
		return resultsArray;
	}
	
	// Get punchcard info
	private ArrayList<HashMap<String,String>> getPunchCardInfo(String punchcardid)
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
	
	// Get business info
	private ArrayList<HashMap<String,String>> getBusinessInfo(String businessid)
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
	
	private boolean punchcardNotExpired(String expireDateString)
	{
		boolean valid = false;
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date expireDate = dateFormat.parse(expireDateString);
            java.util.Date currentDate = new java.util.Date();

            // punch expired?
            valid = !(Utility.isAfterDateTime(currentDate, expireDate));
        }
        catch (ParseException ex)
        {
        	Constants.logger.error(ex);
        }
        return valid;
	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
    	float expectedAPIVersion = getExpectedVersion(request);
    	if (validateVersion(response, expectedAPIVersion))
    	{
        	HashMap<String, String> requestInputs = getRequestData(request, postElements);	
        	
        	String user_id = requestInputs.get(Constants.USERID_PARAMNAME);
        	ArrayList<HashMap<String,String>> userResultsArray = getUserInfo(user_id);
        	if (userResultsArray.size() == 1)
        	{
        		String punchcardid = requestInputs.get(Constants.PUNCHCARDID_PARAMNAME);
            	ArrayList<HashMap<String,String>> punchcardResultsArray = getPunchCardInfo(punchcardid);
            	if (punchcardResultsArray.size() == 1)
            	{
            		HashMap<String,String> punchcardInfo = punchcardResultsArray.get(0);
            		// Check if punchcard is still valid
            		if (punchcardNotExpired(punchcardInfo.get("expiry_date")))
            		{
            			String business_id = requestInputs.get(Constants.BUSINESSID_PARAMNAME);
            			ArrayList<HashMap<String,String>> businessResultsArray = getBusinessInfo(business_id);
            			if (businessResultsArray.size() == 1)
            			{
            				HashMap<String,String> businessInfo = businessResultsArray.get(0);
            				String busi_enabled = businessInfo.get("busi_enabled");
            	            if (busi_enabled.equalsIgnoreCase("Y")) 
            	            {
            	            	// TODO: Complete purchase of punches using credits
            	            }
            	            else
            	            {
                				// Business is not enabled
                        		Constants.logger.error("Error: Tried to purchase punchcard from expired business: " + business_id);
                        		errorResponse(response, "404", "Unknown business.");
            	            }
            			}
            			else
            			{
            				// Could not find business
                    		Constants.logger.error("Error: Unable to find business with id: " + business_id);
                    		errorResponse(response, "404", "Unknown business.");
            			}
            		}
            		else
            		{
            			// Punchcard is expired
                		Constants.logger.error("Error: Tried to purchase expired punchcard: " + punchcardid);
                		errorResponse(response, "403", "Punchcard is expired.");
            		}
            	}
            	else
            	{
        			// Could not find punchcard
            		Constants.logger.error("Error: Unable to find punchcard with id: " + punchcardid);
            		errorResponse(response, "404", "Unknown punchcard");
            	}
        	}
        	else
        	{
    			// Could not find user
        		Constants.logger.error("Error: Unable to find user with id: " + user_id);
        		errorResponse(response, "404", "Unknown user");
        	}
    	}
    	
    	//xmlResponse(response, currentProducts);
    }
}
