package com.app;

import com.db.DataAccess;
import com.db.DataAccessController;
import com.db.DataAccess.ResultSetHandler;
import com.jspservlets.SignupAddPunch;
import com.server.Constants;
import com.server.CreditChangeHistory;

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
	private static final String emailRegistration = "EMAIL-REGISTER";
	private static final String facebookRegistration = "FACEBOOK-REGISTER";
	private static final float rewardCreditValue = (float)5.00;
	
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
		postElements.add(Constants.TXTYPE_PARAMNAME);
		postElements.add(Constants.NAME_PARAMNAME);
		postElements.add(Constants.MOBILENO_PARAMNAME);
		postElements.add(Constants.PASSWORD_PARAMNAME);
		postElements.add(Constants.EMAIL_PARAMNAME);
		postElements.add(Constants.FBID_PARAMNAME);
		postElements.add(Constants.REFERCODE_PARAMNAME);
		postElements.add(Constants.SESSIONID_PARAMNAME);
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
		ArrayList<HashMap<String,String>> resultsArray = null;
		if (refer_code != null)
		{
			String queryString = "SELECT user_id, credit FROM app_user WHERE user_code = ?;";
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(refer_code);
			resultsArray = DataAccess.queryDatabase(queryString, parameters);
		}
		return resultsArray;
	}
	
	private void sendWelcomeEmail(String name, String email)
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
	
	private boolean doesAccountExistInternal(String queryString, String value)
	{
		boolean exists = true;
		if (value != null)
		{
			try
			{
				ArrayList<String> parameters = new ArrayList<String>();
				parameters.add(value);
				ArrayList<Boolean> result = new ArrayList<Boolean>();
				DataAccess.queryDatabaseCustom(queryString, parameters, result, new ResultSetHandler()
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
	
	// Check if an email account also exists already for this user
	private boolean doesEmailAccountExist(String email)
	{
		String queryString = "SELECT user_id FROM app_user WHERE email_id=? and isfbaccount='N';";
		return doesAccountExistInternal(queryString, email);
	}
	
	// Check if an FBID exists already for this account
	private boolean doesFBIDExist(String fbid)
	{
		String queryString = "SELECT user_id FROM app_user WHERE fbid=?;";
		return doesAccountExistInternal(queryString, fbid);
	}
	
	private int registerEmailUser(HttpServletRequest request, HttpServletResponse response, HashMap<String, String> requestInputs, String referringUserId)
            throws ServletException, IOException 
	{
		int user_id = 0;
		String email = requestInputs.get(Constants.EMAIL_PARAMNAME);
		if (!doesEmailAccountExist(email))
		{
			try
			{
				// Insert new user into database
				java.sql.Time time = new java.sql.Time(new Date().getTime());
	            java.sql.Date date = new java.sql.Date(new Date().getTime());
	            String name = requestInputs.get(Constants.NAME_PARAMNAME);
	            String queryString = "insert into app_user " +
	                    " (username,email_id,mobile_no,password,pincode,user_status,isemailverified,time,date,isfbaccount,credit,refer_code,user_code) " +
	            		"values(?,?,?,?,0,'N','N','" + 
	                    time + "','" + 
	            		date + 
	            		"','N'," + 
	    	            rewardCreditValue + 
	    	            ",?,?);";

	            ArrayList<String> parameters = new ArrayList<String>();
	            parameters.add(name);
	            parameters.add(email);
	            parameters.add(requestInputs.get(Constants.MOBILENO_PARAMNAME));
				parameters.add(requestInputs.get(Constants.PASSWORD_PARAMNAME));
	            parameters.add(requestInputs.get(Constants.REFERCODE_PARAMNAME));
	            // Generate a length 5 random alphanumeric code for the new user
	            parameters.add(getRandomAlphaNumericCode(5));
	            user_id = DataAccess.insertDatabase(queryString, parameters);	
				if (user_id != 0)
				{
					// Put a tracking row into the credit change history table
					CreditChangeHistory changeHistory = CreditChangeHistory.getInstance();
					changeHistory.insertCreditChange(Integer.toString(user_id), rewardCreditValue, CreditChangeHistory.SIGNUP, referringUserId);
					
					// Send confirmation email
					SignupAddPunch mail = new SignupAddPunch();
	                mail.sendEmail_For_app_user(Integer.toString(user_id), email);
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
    		Constants.logger.error("Error: Email " + email + " is already being used");
    		errorResponse(response, "403", "Cannot register. Email is already in use.");
		}
		return user_id;
	}
	
	private int registerFBUser(HttpServletRequest request, HttpServletResponse response, HashMap<String, String> requestInputs, String referringUserId)
            throws ServletException, IOException 
	{
		int user_id = 0;
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
	            String queryString = "insert into app_user " +
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
	            user_id = DataAccess.insertDatabase(queryString, parameters);	
				if (user_id != 0)
				{
					// Put a tracking row into the credit change history table
					CreditChangeHistory changeHistory = CreditChangeHistory.getInstance();
					changeHistory.insertCreditChange(Integer.toString(user_id), rewardCreditValue, CreditChangeHistory.SIGNUP, referringUserId);
					
					sendWelcomeEmail(name, email);
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
		return user_id;
	}
	
	private HashMap<String,Object> createEmailRegistrationResponse(int new_user_id, HashMap<String, String> requestInputs)
	{
		HashMap<String, Object> responseMap = new HashMap<String,Object>();
		responseMap.put("statusCode", "00");
		responseMap.put("userid", new_user_id);
		responseMap.put("name", requestInputs.get(Constants.NAME_PARAMNAME));
		String email = requestInputs.get(Constants.EMAIL_PARAMNAME);
		responseMap.put("email", email);
		responseMap.put("mobilenumber", requestInputs.get(Constants.MOBILENO_PARAMNAME));
		String statusMessage =  "You’re almost done!"
                + " We’ve sent an email to " + email + "."
                + " Click the link within the email to confirm your account and begin saving money with PaidPunch!";
		responseMap.put("statusMessage", statusMessage);
		return responseMap;
	}
	
	private HashMap<String,Object> createFacebookRegistrationResponse(int new_user_id, HashMap<String, String> requestInputs)
	{
		HashMap<String, Object> responseMap = new HashMap<String,Object>();
		responseMap.put("statusCode", "00");
		responseMap.put("userid", new_user_id);
		responseMap.put("sessionid", requestInputs.get(Constants.SESSIONID_PARAMNAME));
		responseMap.put("is_profileid_created", "false");
		responseMap.put("statusMessage", "Registration successful!");
		return responseMap;
	}
	
	private void addRewardCreditToReferrer(String userid, String credit, String referredUserId)
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
        
        // Insert tracking row into change history table
        CreditChangeHistory changeHistory = CreditChangeHistory.getInstance();
		changeHistory.insertCreditChange(userid, rewardCreditValue, CreditChangeHistory.USER_INVITE, referredUserId);
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
    	HashMap<String, Object> responseMap = null;
    	float expectedAPIVersion = getExpectedVersion(request);
    	if (validateVersion(response, expectedAPIVersion))
    	{
        	HashMap<String, String> requestInputs = getRequestData(request, postElements);	
        	
        	String refer_code = requestInputs.get(Constants.REFERCODE_PARAMNAME);
        	ArrayList<HashMap<String,String>> resultsArray = getReferringUser(refer_code);
        	if (resultsArray.size() == 1)
        	{
        		HashMap<String, String> userParams = resultsArray.get(0);
        		String referringUserId = userParams.get(Constants.USERID_PARAMNAME);
        		
        		int new_user_id = 0;
        		String registrationType = requestInputs.get(Constants.TXTYPE_PARAMNAME);
        		if (registrationType != null)
        		{
        			if (registrationType.equalsIgnoreCase(emailRegistration))
        			{
        				// Perform email registration
        				new_user_id = registerEmailUser(request, response, requestInputs, referringUserId);
        				if (new_user_id != 0)
                		{
        					responseMap = createEmailRegistrationResponse(new_user_id, requestInputs);
                		}
        			}
        			else if (registrationType.equalsIgnoreCase(facebookRegistration))
        			{
        				// Perform Facebook registration
        				new_user_id = registerFBUser(request, response, requestInputs, referringUserId);
        				if (new_user_id != 0)
                		{
        					responseMap = createFacebookRegistrationResponse(new_user_id, requestInputs);
                		}
        			}
        			else
        			{
        				// Unknown registration type specified by caller
                		Constants.logger.error("Error: unknown registration type");
                		errorResponse(response, "404", "Registration error");
        			}
        		}
        		else
        		{
        			// No registration type specified by caller
            		Constants.logger.error("Error: null registration type");
            		errorResponse(response, "404", "Registration error");
        		}
        		
        		// Successfully created user, now reward the referring user
        		if (new_user_id != 0)
        		{
        			// Get the referring user's info and then call function to reward them
        			addRewardCreditToReferrer(referringUserId, userParams.get(Constants.CREDIT_PARAMNAME), Integer.toString(new_user_id));
        	    	
        			// Send a response to caller
        			xmlResponse(response, responseMap);
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
    }
}
