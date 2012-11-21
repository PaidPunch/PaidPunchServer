package com.app;

import com.db.DataAccess;
import com.db.DataAccessController;
import com.db.DataAccess.ResultSetHandler;
import com.jspservlets.SignupAddPunch;
import com.server.Constants;

import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Users extends XmlHttpServlet  {

	private static final long serialVersionUID = -9044506610414211667L;
	private static final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final float rewardCreditValue = (float)5.00;
	
	private ArrayList<String> postElements;
	private ArrayList<String> putElements;
	
	@Override
    public void init(ServletConfig config) throws ServletException
    {
	   super.init(config);

	   try
	   {
		   ServletContext context = config.getServletContext();
		   Constants.loadJDBCConstants(context);
		   
		   initializePostElements();
		   initializePutElements();
	   }
	   catch(Exception e)
	   {
		   Constants.logger.error(e);
	   }
    }
	
	private void initializePostElements()
	{
		postElements = new ArrayList<String>();
		postElements.add(Constants.TXTYPE_PARAMNAME);
		postElements.add(Constants.NAME_PARAMNAME);
		postElements.add(Constants.EMAIL_PARAMNAME);
		postElements.add(Constants.FBID_PARAMNAME);
		postElements.add(Constants.REFERCODE_PARAMNAME);
		postElements.add(Constants.SESSIONID_PARAMNAME);
	}
	
	private void initializePutElements()
	{
		putElements = new ArrayList<String>();
		putElements.add(Constants.USERID_PARAMNAME);
	}
	
	private String getRandomAlphaNumericCode(int len) 
	{
		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) 
		{
			int ndx = (int) (Math.random() * ALPHA_NUM.length());
			sb.append(ALPHA_NUM.charAt(ndx));
		}
		return sb.toString();
	}
	
	// Get information about user associated with the referral code used by new registering user
	private ArrayList<HashMap<String,String>> getReferringUser(String refer_code)
	{
		ArrayList<HashMap<String,String>> resultsArray = new ArrayList<HashMap<String,String>>();
		String queryString = "SELECT user_id, credit FROM app_user WHERE user_code = ?;";
		if (refer_code != null)
		{
			try
			{
				ArrayList<String> parameters = new ArrayList<String>();
				parameters.add(refer_code);
				DataAccess.queryDatabase(queryString, parameters, resultsArray, new ResultSetHandler()
				{
					 public void handle(ResultSet results, Object returnObj) throws SQLException
	                 { 						 
						 // The cast here is a well-known one, so the suppression is OK
						 @SuppressWarnings("unchecked")
						 ArrayList<HashMap<String,String>> resultsArray = (ArrayList<HashMap<String,String>>)returnObj;
	                     while(results.next())
	                     {                    	 
	                    	 // Populate user information
	                    	 HashMap<String,String> current = new HashMap<String,String>();
	                    	 current.put(Constants.USERID_PARAMNAME, results.getString(Constants.USERID_PARAMNAME));
	                    	 current.put(Constants.CREDIT_PARAMNAME, results.getString(Constants.CREDIT_PARAMNAME));
	                    	 resultsArray.add(current);
	                     }
	                 }
				});
			}
			catch (SQLException ex)
			{
				Constants.logger.error("Error : " + ex.getMessage());
			}
		}
		return resultsArray;
	}
	
	// Check if an FBID exists already for this account
	private boolean doesFBIDExist(String fbid)
	{
		boolean exists = true;
		String queryString = "SELECT user_id FROM app_user a where fbid=?;";
		if (fbid != null)
		{
			try
			{
				ArrayList<String> parameters = new ArrayList<String>();
				parameters.add(fbid);
				ArrayList<Boolean> result = new ArrayList<Boolean>();
				DataAccess.queryDatabase(queryString, parameters, result, new ResultSetHandler()
				{
					 public void handle(ResultSet results, Object returnObj) throws SQLException
	                 { 						 
						 // The cast here is a well-known one, so the suppression is OK
						 @SuppressWarnings("unchecked")
						 ArrayList<Boolean> exists = (ArrayList<Boolean>)returnObj;
	                     if (results.next())
	                     {                    	 
	                    	 // Indicate a result was found
	                    	 exists.add(true);
	                     }
	                     else
	                     {
	                    	// Indicate there was no matching fbid
	                    	 exists.add(false);
	                     }
	                 }
				});
				exists = result.get(0);
			}
			catch (SQLException ex)
			{
				Constants.logger.error("Error : " + ex.getMessage());
			}
		}
		return exists;
	}
	
	private boolean registerFBUser(HttpServletRequest request, HttpServletResponse response, HashMap<String, String> requestInputs)
            throws ServletException, IOException 
	{
		boolean success = false;
		String fbid = requestInputs.get(Constants.FBID_PARAMNAME);
		if (!doesFBIDExist(fbid))
		{
			try
			{
				// Insert new user into database
				java.sql.Time time = new java.sql.Time(new Date().getTime());
	            java.sql.Date date = new java.sql.Date(new Date().getTime());
	            String name = requestInputs.get(Constants.NAME_PARAMNAME);
	            String email = requestInputs.get(Constants.EMAIL_PARAMNAME);
	            String queryString = "insert into app_user" + "" +
	            		"(username,email_id,user_status,isemailverified,Date,Time,sessionid,isfbaccount,fbid,credit,refer_code,user_code)" + 
	            		"values(?,?,'Y','Y','" + 
	            		date + "','" + 
	            		time + "',?,'Y',?," + 
	            		rewardCreditValue + 
	            		",?,?);";
	            ArrayList<String> parameters = new ArrayList<String>();
	            parameters.add(name);
	            parameters.add(email);
	            parameters.add(requestInputs.get(Constants.SESSIONID_PARAMNAME));
				parameters.add(fbid);
	            parameters.add(requestInputs.get(Constants.REFERCODE_PARAMNAME));
	            // Generate a length 5 random alphanumeric code for the new user
	            parameters.add(getRandomAlphaNumericCode(5));
			    success = DataAccess.updateDatabase(queryString, parameters);	
				if (success)
				{
					// Get the first name from the name field
					int spaceIndex = name.indexOf(' ');
					String firstName;
					if (spaceIndex != -1)
					{
						firstName = name.substring(0, spaceIndex);
					}
					else
					{
						firstName = name;
					}
					
					// Send a welcome/confirmation mail to the person who signed up
                    SignupAddPunch emailsender = new SignupAddPunch();
                    emailsender.sendConfirmationEmail(email, firstName);
                    Constants.logger.info("Created new user with name " + name + " and email " + email);
				}
				else
				{
					// The referral code returned more than a single user. Return an error after logging.
		    		Constants.logger.error("Error: Unable to register new user");
		    		errorResponse(response, "500", "Server is currently unavailable. Please try again later.");
				}
			}
			catch (Exception ex)
			{
				Constants.logger.error("Error : " + ex.getMessage());
			}
		}
		else
		{
			// The referral code returned more than a single user. Return an error after logging.
    		Constants.logger.error("Error: Facebook id " + fbid + " is already being used");
    		errorResponse(response, "403", "Cannot register. Facebook account is already in use.");
		}
		return success;
	}
	
	private void addRewardCreditToReferrer(String userid, String credit)
	{
		// Update referrer with reward credit
		float updatedAmount = Float.parseFloat(credit) + rewardCreditValue;
		int res = DataAccessController.updatetDataToTable("app_user", "user_id", userid, "credit", Float.toString(updatedAmount));
        if (res == 1)
        {
        	Constants.logger.info("Updated credit with referral reward for userid: " + userid);
        }
        else
        {
        	Constants.logger.error("Unable to update credit with referral reward for userid: " + userid);
        }
	}

	/**
     * Handles the HTTP <code>POST</code> method.
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {    	
    	float expectedAPIVersion = getExpectedVersion(request);
    	if (validateVersion(response, expectedAPIVersion))
    	{
        	HashMap<String, String> requestInputs = getRequestData(request, postElements);	
        	
        	String refer_code = requestInputs.get(Constants.REFERCODE_PARAMNAME);
        	ArrayList<HashMap<String,String>> resultsArray = getReferringUser(refer_code);
        	if (resultsArray.size() == 1)
        	{
        		boolean success = false;
        		success = registerFBUser(request, response, requestInputs);
        		if (success)
        		{
        			// Get the referring user's info and then call function to reward them
        			HashMap<String, String> userParams = resultsArray.get(0);
        			addRewardCreditToReferrer(userParams.get(Constants.USERID_PARAMNAME), userParams.get(Constants.CREDIT_PARAMNAME));
        		}
        	}
        	else if (resultsArray.size() > 1)
        	{
        		// The referral code returned more than a single user. Return an error after logging.
        		Constants.logger.error("Error: Refer code " + refer_code + " returned more than one user");
        		errorResponse(response, "404", "The referral code is invalid");
        	}
        	else
        	{
        		// The refer code does not match an existing user
        		Constants.logger.info("Warning: Refer code " + refer_code + " does not match any known users");
        		errorResponse(response, "404", "The referral code is invalid");
        	}
    	}

    	//xmlResponse(response, currentProducts);
    }
    
	/**
     * Handles the HTTP <code>POST</code> method.
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
    	float expectedAPIVersion = getExpectedVersion(request);
    	if (validateVersion(response, expectedAPIVersion))
    	{
        	HashMap<String, String> requestInputs = getRequestData(request, putElements);	
        	
        	// TODO: Remove later. Temporary to get rid of warning.
        	Constants.logger.info(requestInputs.toString());
    	}
    	
    	//xmlResponse(response, currentProducts);
    }
}
