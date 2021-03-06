package com.app;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

import com.db.DataAccess;
import com.db.DataAccessController;
import com.db.SimpleDB;
import com.db.DataAccess.ResultSetHandler;
import com.jspservlets.SignupAddPunch;
import com.server.Constants;
import com.server.CreditChangeHistory;
import com.server.MailingListSubscribers;
import com.server.RecordsList;
import com.server.SimpleLogger;

import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

public class Users extends XmlHttpServlet  
{

	private static final long serialVersionUID = -9044506610414211667L;
	private static final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String emailRegistration = "EMAIL-REGISTER";
	private static final String facebookRegistration = "FACEBOOK-REGISTER";
	private static final String emailLogin = "EMAIL-LOGIN";
	private static final String facebookLogin = "FACEBOOK-LOGIN";
	private static final float rewardCreditValue = (float)5.00;
	
	@Override
    public void init(ServletConfig config) throws ServletException
    {
	   super.init(config);
	   currentClassName = Users.class.getSimpleName();

	   try
	   {
		   ServletContext context = config.getServletContext();
		   Constants.loadJDBCConstants(context);
	   }
	   catch(Exception e)
	   {
	       SimpleLogger.getInstance().error(currentClassName, e);
	   }
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
	
	/********* Registration  *********/
	
	private HashMap<String,String> getReferralEntity(String refer_code, String queryString)
	{
		HashMap<String,String> results = null;
		if (refer_code != null)
		{
			ArrayList<HashMap<String,String>> resultsArray = null;
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(refer_code);
			resultsArray = DataAccess.queryDatabase(queryString, parameters);
			if (resultsArray.size() >= 1)
			{
				results = resultsArray.get(0);
				
				// Warn if multiple entries were returned
				if (resultsArray.size() > 1)
	            {
	                // The referral code returned more than a single entity. Return an error after logging.
	                SimpleLogger.getInstance().warn(currentClassName, "MultipleEntriesReferralCode|Refer_code:" + refer_code);
	            }
			} 
        	else
        	{
        		// The refer code does not match an existing entity
        	    SimpleLogger.getInstance().warn(currentClassName, "NoEntityReferralCode|Refer_code:" + refer_code);
        	}
		}
		return results;
	}
	
	// Get information about user associated with the referral code used by new registering user
	private HashMap<String,String> getReferringUser(String refer_code)
	{
		String queryString = "SELECT user_id, credit FROM app_user WHERE user_code = ?;";
		return getReferralEntity(refer_code, queryString);
	}
	
	// Get information about business associated with the referral code used by new registering user
	private HashMap<String,String> getReferringBusiness(String refer_code)
	{
		String queryString = "SELECT business_userid FROM business_users WHERE business_code = ?;";
		return getReferralEntity(refer_code, queryString);
	}
	
	// Get information about special invite code associated with the referral code used by new registering user
	private HashMap<String,String> getReferringSpecialCode(String refer_code)
    {
	    HashMap<String,String> results = null;
        String queryString = "select * from `" + Constants.CODES_DOMAIN + "` where InviteCode = \"" + refer_code + "\"";
        SimpleDB sdb = SimpleDB.getInstance();
        List<Item> items = sdb.selectQuery(queryString);
        
        if (items.size() > 1)
        {
            // Warn if more than one item found
            SimpleLogger.getInstance().warn(currentClassName, "MultipleSpecialCodeMatches|Refer_code:" + refer_code);
        }
        
        if (items.size() >= 1)
        {
            // get the first item
            Item currentItem = items.get(0);
            
            results = new HashMap<String,String>();
            
            for (Attribute attribute : currentItem.getAttributes()) 
            {
                results.put(attribute.getName(), attribute.getValue());
            }
        }
        
        return results;
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
        SimpleLogger.getInstance().info(currentClassName, "CreateNewUser|name:" + name + "|email:" + email);
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
			    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
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
	
	private int registerEmailUser(HttpServletRequest request, HttpServletResponse response, JSONObject requestInputs, String referringId, boolean userReferred, boolean specialCodeReferred, String userCode, float amount)
            throws ServletException, IOException 
	{
		int user_id = 0;
		String email;
		try 
		{
			email = requestInputs.getString(Constants.EMAIL_PARAMNAME);
		
			if (!doesEmailAccountExist(email))
			{
				try
				{
					// Insert new user into database
					java.sql.Time time = new java.sql.Time(new Date().getTime());
		            java.sql.Date date = new java.sql.Date(new Date().getTime());
		            String name = requestInputs.getString(Constants.NAME_PARAMNAME);
		            String queryString = "insert into app_user " +
		                    " (username,email_id,mobile_no,password,pincode,user_status,isemailverified,time,date,isfbaccount,credit,refer_code,user_code,user_referred) " +
		            		"values(?,?,?,?,?,'N','N','" + 
		                    time + "','" + 
		            		date + 
		            		"','N'," + 
		    	            amount + 
		    	            ",?,?,?);";
	
		            ArrayList<String> parameters = new ArrayList<String>();
		            parameters.add(name);
		            parameters.add(email);
		            parameters.add(requestInputs.getString(Constants.MOBILENO_PARAMNAME));
					parameters.add(requestInputs.getString(Constants.PASSWORD_PARAMNAME));
					parameters.add(requestInputs.getString(Constants.ZIPCODE_PARAMNAME));
		            parameters.add(requestInputs.getString(Constants.REFERCODE_PARAMNAME));
		            parameters.add(userCode);
		            if (userReferred)
		            {
		            	parameters.add("1");
		            }
		            else
		            {
		            	parameters.add("0");
		            }
		            user_id = DataAccess.insertDatabase(queryString, parameters);	
					if (user_id != 0)
					{
						// Put a tracking row into the credit change history table
						CreditChangeHistory changeHistory = CreditChangeHistory.getInstance();
						int reason = 0;
						if (specialCodeReferred)
						{
						    reason = CreditChangeHistory.SPECIAL_INVITE_SIGNUP;
						}
						else if (userReferred)
						{
							reason = CreditChangeHistory.USER_INVITE_SIGNUP;
						}
						else
						{
							reason = CreditChangeHistory.BUSINESS_INVITE_SIGNUP;
						}
						changeHistory.insertCreditChange(Integer.toString(user_id), amount, reason, referringId);
						
						// Send confirmation email
						SignupAddPunch mail = new SignupAddPunch();
		                mail.sendEmail_For_app_user(Integer.toString(user_id), email);
		                
		             // Adding email to subscribers list on mailchimp
						MailingListSubscribers.getInstance().subscribeToMailingList(email);
					}
					else
					{
						// The referral code returned more than a single user. Return an error after logging.
					    SimpleLogger.getInstance().error(currentClassName, "UnableToRegisterNewUser");
			    		errorResponse(request, response, "500", "Server is currently unavailable. Please try again later.");
					}
				}
				catch (Exception ex)
				{
				    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
				}
			}
			else
			{
				// The referral code returned more than a single user. Return an error after logging.
			    SimpleLogger.getInstance().error(currentClassName, "EmailInUse|Email:" + email);
	    		errorResponse(request, response, "403", "Cannot register. Email is already in use.");
			}
		} 
		catch (JSONException ex) 
		{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
		}
		return user_id;
	}
	
	private int registerFBUser(HttpServletRequest request, HttpServletResponse response, JSONObject requestInputs, String referringId, boolean userReferred, boolean specialCodeReferred, String userCode, float amount)
            throws ServletException, IOException 
	{
		int user_id = 0;
		try
		{
			String fbid = requestInputs.getString(Constants.FBID_PARAMNAME);
			if (!doesFBIDExist(fbid))
			{
				try
				{
					// Insert new user into database
					java.sql.Time time = new java.sql.Time(new Date().getTime());
		            java.sql.Date date = new java.sql.Date(new Date().getTime());
		            String name = requestInputs.getString(Constants.NAME_PARAMNAME);
		            String email = requestInputs.getString(Constants.EMAIL_PARAMNAME);
		            String queryString = "insert into app_user " +
		            		"(username,email_id,user_status,isemailverified,Date,Time,sessionid,isfbaccount,fbid,credit,refer_code,user_code,user_referred,pincode)" + 
		            		"values(?,?,'Y','Y','" + 
		            		date + "','" + 
		            		time + "',?,'Y',?," + 
		            		amount + 
		            		",?,?,?,?);";
		            ArrayList<String> parameters = new ArrayList<String>();
		            parameters.add(name);
		            parameters.add(email);
		            parameters.add(request.getHeader(Constants.SESSIONID_PARAMNAME));
					parameters.add(fbid);
		            parameters.add(requestInputs.getString(Constants.REFERCODE_PARAMNAME));
		            parameters.add(userCode);
		            if (userReferred)
		            {
		            	parameters.add("1");
		            }
		            else
		            {
		            	parameters.add("0");
		            }
		            parameters.add(requestInputs.getString(Constants.ZIPCODE_PARAMNAME));
		            user_id = DataAccess.insertDatabase(queryString, parameters);	
					if (user_id != 0)
					{
						// Put a tracking row into the credit change history table
						CreditChangeHistory changeHistory = CreditChangeHistory.getInstance();
						int reason = 0;
						if (specialCodeReferred)
						{
						    reason = CreditChangeHistory.SPECIAL_INVITE_SIGNUP;
						}
						else if (userReferred)
						{
							reason = CreditChangeHistory.USER_INVITE_SIGNUP;
						}
						else
						{
							reason = CreditChangeHistory.BUSINESS_INVITE_SIGNUP;
						}
						changeHistory.insertCreditChange(Integer.toString(user_id), amount, reason, referringId);
						
						sendWelcomeEmail(name, email);
						
						// Adding email to subscribers list on mailchimp
						MailingListSubscribers.getInstance().subscribeToMailingList(email);
					}
					else
					{
						// The referral code returned more than a single user. Return an error after logging.
					    SimpleLogger.getInstance().error(currentClassName, "UnableToRegisterNewUser");
			    		errorResponse(request, response, "500", "Server is currently unavailable. Please try again later.");
					}
				}
				catch (Exception ex)
				{
				    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
				}
			}
			else
			{
				// The referral code returned more than a single user. Return an error after logging.
			    SimpleLogger.getInstance().error(currentClassName, "FacebookIdInUser|fbid:" + fbid);
	    		errorResponse(request, response, "403", "Cannot register. Facebook account is already in use.");
			}
		} 
		catch (JSONException ex) 
		{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
		}
		return user_id;
	}
	
	private JSONObject createEmailRegistrationResponse(int new_user_id, String user_code, float amount, JSONObject requestInputs)
	{
		JSONObject responseMap = new JSONObject();
		try
		{
			responseMap.put("statusCode", "00");
			responseMap.put("user_id", new_user_id);
			responseMap.put("username", requestInputs.get(Constants.NAME_PARAMNAME));
			String email = requestInputs.getString(Constants.EMAIL_PARAMNAME);
			responseMap.put("email", email);
			responseMap.put("mobilenumber", requestInputs.get(Constants.MOBILENO_PARAMNAME));
			responseMap.put("user_code", user_code);
			responseMap.put("credit", amount);
			String statusMessage =  "You are almost done!"
	                + " We have sent an email to " + email + "."
	                + " Click the link within the email to confirm your account and begin saving money with PaidPunch!";
			responseMap.put("statusMessage", statusMessage);
		} 
		catch (JSONException ex) 
		{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
		}
		return responseMap;
	}
	
	private JSONObject createFacebookRegistrationResponse(int new_user_id, String user_code, float amount, HttpServletRequest request)
	{
		JSONObject responseMap = new JSONObject();
		try
		{
			responseMap.put("statusCode", "00");
			responseMap.put("user_id", new_user_id);
			responseMap.put("sessionid", request.getHeader(Constants.SESSIONID_PARAMNAME));
			responseMap.put("is_profileid_created", "false");
			responseMap.put("user_code", user_code);
			responseMap.put("credit", amount);
			responseMap.put("statusMessage", "Registration successful!");
		} 
		catch (JSONException ex) 
		{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
		}
		return responseMap;
	}
	
	private void addRewardCreditToReferrer(String userid, String credit, String referredUserId)
	{
		// Update referrer with reward credit
		float updatedAmount = Float.parseFloat(credit) + rewardCreditValue;
		int res = DataAccessController.updatetDataToTable("app_user", "user_id", userid, "credit", Float.toString(updatedAmount));
        if (res == 1)
        {
            SimpleLogger.getInstance().info(currentClassName, "Updated credit with referral reward for user_id:" + userid);
        }
        else
        {
            SimpleLogger.getInstance().error(currentClassName, "CreditFromReferralRewardFailed|User_id:" + userid);
        }
        
        // Insert tracking row into change history table
        CreditChangeHistory changeHistory = CreditChangeHistory.getInstance();
		changeHistory.insertCreditChange(userid, rewardCreditValue, CreditChangeHistory.USER_REFERRAL, referredUserId);
	}
	
	/********* Login  *********/
	
	private JSONObject handleLogin(HttpServletRequest request, HttpServletResponse response, JSONObject requestInputs)
	{
		JSONObject responseMap = null;
		HashMap<String,String> userData = null;
		
		try
		{
			String loginType = requestInputs.getString(Constants.TXTYPE_PARAMNAME);
			if (loginType != null)
			{
				if (loginType.equalsIgnoreCase(emailLogin))
				{
					userData = validateEmailLogin(requestInputs);
					if (userData != null)
					{
						if (userData.get("isemailverified").equals("Y"))
						{
							responseMap = createLoginResponse(userData);
						}
						else
						{
							errorResponse(request, response, "403", "Please verify your email first being trying to log in.");
						}
					}
					else
					{
						errorResponse(request, response, "404", "User not found. Either email or password is invalid. Please sign up first.");
					}
				}
				else if (loginType.equalsIgnoreCase(facebookLogin))
				{
					userData = validateFacebookLogin(requestInputs);
					if (userData != null)
					{
						responseMap = createLoginResponse(userData);
					}
					else
					{
						errorResponse(request, response, "404", "User not found. Either facebook id is invalid. Please sign up first.");
					}
				}
				else
				{
					// Unknown login type specified by caller
				    SimpleLogger.getInstance().error(currentClassName, "UnknownLoginType|Login_type:" + loginType);
	        		errorResponse(request, response, "404", "User update error");
				}
				
				if (responseMap != null)
				{
					// Update sessionid for user
					updateSessionForUser(userData, request);	
				}
			}
			else
			{
				// Unknown login type specified by caller
			    SimpleLogger.getInstance().error(currentClassName, "UnknownLoginType|Login_type:" + loginType);
	    		errorResponse(request, response, "404", "User update error");
			}
		}
		catch (JSONException ex)
		{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
			errorResponse(request, response, "400", "Bad request");
		}
		
		return responseMap;
	}
	
	private HashMap<String,String> validateEmailLogin(JSONObject requestInputs)
	{
		HashMap<String,String> results = null;
		ArrayList<HashMap<String,String>> resultsArray = null;
		
		try
		{
			String queryString = "SELECT * FROM app_user WHERE email_id=? and password=? and isfbaccount='N';";
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(requestInputs.getString(Constants.EMAIL_PARAMNAME));
			parameters.add(requestInputs.getString(Constants.PASSWORD_PARAMNAME));
			resultsArray = DataAccess.queryDatabase(queryString, parameters);
			if (resultsArray.size() == 1)
			{
				results = resultsArray.get(0);
			}
			else if (resultsArray.size() > 1)
	    	{
	    		// The referral code returned more than a single entity. Return an error after logging.
			    SimpleLogger.getInstance().error(currentClassName, "MultipleEmailAccts|Email:" + requestInputs.getString(Constants.EMAIL_PARAMNAME));
	    	}
	    	else
	    	{
	    		// The refer code does not match an existing entity
	    	    SimpleLogger.getInstance().warn(currentClassName, "NoSuchEmailAcct|Email:" + requestInputs.getString(Constants.EMAIL_PARAMNAME));
	    	}	
		}
		catch (JSONException ex) 
		{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
		}
		return results;
	}
	
	private JSONObject createLoginResponse(HashMap<String,String> userData)
	{
		JSONObject responseMap = new JSONObject();
		try
		{
			responseMap.put("statusCode", "00");
			responseMap.put("user_id", userData.get(Constants.USERID_PARAMNAME));
			responseMap.put("username", userData.get(Constants.NAME_PARAMNAME));
			responseMap.put("email", userData.get(Constants.EMAIL_PARAMNAME));
			responseMap.put("mobile_no", userData.get(Constants.MOBILENO_PARAMNAME));
			responseMap.put("zipcode", userData.get("pincode"));
			responseMap.put("user_code", userData.get(Constants.USERCODE_PARAMNAME));
			responseMap.put("credit", userData.get(Constants.CREDIT_PARAMNAME));
			responseMap.put("isprofile_created", userData.get(Constants.PROFILECREATED_PARAMNAME));
			responseMap.put("statusMessage", "Login Successful");
		} 
		catch (JSONException ex) 
		{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
		}
		return responseMap;
	}
	
	private HashMap<String,String> validateFacebookLogin(JSONObject requestInputs)
	{
		HashMap<String,String> results = null;
		ArrayList<HashMap<String,String>> resultsArray = null;
		
		try
		{
			String queryString = "SELECT * FROM app_user WHERE fbid=? and email_id=?;";
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(requestInputs.getString(Constants.FBID_PARAMNAME));
			parameters.add(requestInputs.getString(Constants.EMAIL_PARAMNAME));
			resultsArray = DataAccess.queryDatabase(queryString, parameters);
			if (resultsArray.size() == 1)
			{
				results = resultsArray.get(0);
			}
			else if (resultsArray.size() > 1)
	    	{
	    		// The referral code returned more than a single entity. Return an error after logging.
			    SimpleLogger.getInstance().error(currentClassName, "MultipleFBAccts|FBID:" + requestInputs.getString(Constants.FBID_PARAMNAME));
	    	}
	    	else
	    	{
	    		// The refer code does not match an existing entity
	    	    SimpleLogger.getInstance().warn(currentClassName, "NoFBAcct|FBID:" + requestInputs.getString(Constants.FBID_PARAMNAME));
	    	}	
		}
		catch (JSONException ex) 
		{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
		}
		return results;
	}
	
	private boolean updateSessionForUser(HashMap<String,String> userData, HttpServletRequest request)
	{
		boolean success = false;
		try
		{
			String queryString = "UPDATE app_user SET sessionid = ?, user_status = 'Y' WHERE user_id = ?";
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(request.getHeader(Constants.SESSIONID_PARAMNAME));
			parameters.add(userData.get(Constants.USERID_PARAMNAME));
			success = DataAccess.updateDatabase(queryString, parameters);
		}
		catch (SQLException ex)
		{
		    SimpleLogger.getInstance().error(currentClassName, ex);
        }
		
		return success;
	}
	
	/********* Update Password  *********/
	
	private boolean updatePasswordForUser(Connection conn, String user_id, String new_password)
	{
		boolean success = false;
		String queryString = "UPDATE app_user SET password = ? WHERE user_id = ?";
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(new_password);
		parameters.add(user_id);
		try
		{
			success = DataAccess.updateDatabaseWithExistingConnection(conn, queryString, parameters);	
		}
		catch (SQLException ex)
        {
    		success = false;
    		SimpleLogger.getInstance().error(currentClassName, ex);
        }
		
		return success;
	}
	
	private JSONObject changePassword(HttpServletRequest request, JSONObject requestInputs, HttpServletResponse response, String user_id)
    {
		JSONObject results = null;
    	boolean success = false;
		Connection conn = DataAccessController.createConnection();
		try
		{
			conn.setAutoCommit(false);
			
			ArrayList<HashMap<String,String>> userResultsArray = getUserInfo(user_id, conn);
			if (userResultsArray.size() == 1)
        	{
				HashMap<String,String> userInfo = userResultsArray.get(0);
				String validSessionId = userInfo.get(Constants.SESSIONID_PARAMNAME);	
				if (validateSessionId(validSessionId, request))
				{
					String old_password = requestInputs.getString(Constants.PASSWORD_PARAMNAME);
					String current_password = userInfo.get(Constants.PASSWORD_PARAMNAME);
					if (old_password.equals(current_password))
					{
						String new_password = requestInputs.getString(Constants.NEWPASSWORD_PARAMNAME);
						if (updatePasswordForUser(conn, user_id, new_password))
						{
							try
							{
								results = new JSONObject();
								results.put("statusCode", "00");
								results.put("statusMessage", "Password Change Successful");
							} 
							catch (JSONException ex) 
							{
							    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
							}
							success = true;
						}
						else
						{
							errorResponse(request, response, "500", "Password update failed");
						}
					}
					else
					{
						errorResponse(request, response, "403", "Existing password does not match");
					}
				}
				else
				{
					errorResponse(request, response, "403", "You are already logged into a different device");
				}
        	}
			else
			{
				// Could not find user
        		SimpleLogger.getInstance().unknownUser(currentClassName, user_id);
        		errorResponse(request, response, "404", "Could not find user");
			}
			
			if (success)
			{
				conn.commit();
			}
			else
			{
				conn.rollback();
			}	
		}
		catch (SQLException ex)
		{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
			errorResponse(request, response, "500", "Unable to update password");
		}
		catch (JSONException ex)
    	{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
			errorResponse(request, response, "500", "Unable to update password");
		}
		finally
		{
			try
			{
				conn.close();
			}
			catch (SQLException ex)
			{
			    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
			}
		}
		return results;
    }
	
	/********* Update User Info  *********/
	
	private JSONObject changeUserInfo(HttpServletRequest request, JSONObject requestInputs, HttpServletResponse response, String user_id)
    {
		JSONObject results = null;
    	boolean success = false;
		try
		{			
			ArrayList<HashMap<String,String>> userResultsArray = getUserInfo(user_id, null);
			if (userResultsArray.size() == 1)
        	{
				HashMap<String,String> userInfo = userResultsArray.get(0);
				String validSessionId = userInfo.get(Constants.SESSIONID_PARAMNAME);	
				if (validateSessionId(validSessionId, request))
				{
					String queryString = "UPDATE app_user SET ";
					ArrayList<String> parameters = new ArrayList<String>();
					StringBuilder infoList = new StringBuilder();
					try
					{
						parameters.add(requestInputs.getString(Constants.MOBILENO_PARAMNAME));
						infoList.append("mobile_no = ?");
					}
					catch (JSONException ex)
			    	{
						// No action necessary, just means mobileno is empty
					}
					
					try
					{
						String zipcode = requestInputs.getString(Constants.ZIPCODE_PARAMNAME);
						if (infoList.length() > 0)
						{
							infoList.append(",");
						}
						infoList.append("pincode = ?");
						parameters.add(zipcode);
					}
					catch (JSONException ex)
			    	{
						// No action necessary, just means zipcode is empty
					}
					
					try
					{
						String username = requestInputs.getString(Constants.NAME_PARAMNAME);
						if (infoList.length() > 0)
						{
							infoList.append(",");
						}
						infoList.append("username = ?");
						parameters.add(username);
					}
					catch (JSONException ex)
			    	{
						// No action necessary, just means name is empty
					}
					
					queryString = queryString + infoList.toString() + " WHERE user_id = ?";
					parameters.add(user_id);
					
					success = DataAccess.updateDatabase(queryString, parameters);
					if (success)
					{
						results = new JSONObject();
						results.put("statusCode", "00");
						results.put("statusMessage", "Information updated.");	
					}
				}
				else
				{
					errorResponse(request, response, "403", "You are already logged into a different device");
				}
        	}
			else
			{
				// Could not find user
        		SimpleLogger.getInstance().unknownUser(currentClassName, user_id);
        		errorResponse(request, response, "404", "Could not find user");
			}
		}
		catch (Exception ex)
    	{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
			errorResponse(request, response, "500", "Unable to update user info");
		}
		return results;
    }
	
	/********* Request invite to PaidPunch  *********/
	
	private JSONObject requestInvite(JSONObject requestInputs, HttpServletRequest request, HttpServletResponse response)
    {
		JSONObject results = null;
		
		try
		{
			String email = requestInputs.getString(Constants.EMAIL_PARAMNAME);
			
			// Get UUID for naming new vote
			UUID itemName = UUID.randomUUID();
			
			// Get current datetime
			SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDatetime = datetimeFormat.format(new java.util.Date().getTime());	
			
			List<ReplaceableAttribute> listAttributes = new ArrayList<ReplaceableAttribute>();
			listAttributes.add(new ReplaceableAttribute("email", email, true));
			listAttributes.add(new ReplaceableAttribute("modifiedDatetime", currentDatetime, true));
			
			SimpleDB sdb = SimpleDB.getInstance();
			sdb.updateItem(Constants.REQUESTINVITES_DOMAIN, itemName.toString(), listAttributes);
			
			results = new JSONObject();
			results.put("statusCode", "00");
			results.put("statusMessage", "Thank you for submitting your request!");
		}
		catch (JSONException ex) 
		{
			errorResponse(request, response, "500", "An unknown failure happened");
			SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
		}
		
		return results;
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
    	try
    	{
    		JSONObject responseMap = null;
        	float expectedAPIVersion = getExpectedVersion(request);
        	if (validateVersion(request, response, expectedAPIVersion))
        	{
        		JSONObject requestInputs = getRequestData(request);	
        		
        		String pathInfo = request.getPathInfo();
        		if (pathInfo != null)
        		{
        			String optionalCommand = pathInfo.substring(1);	
        			if (optionalCommand.equalsIgnoreCase("requestInvite"))
        			{
        				responseMap = requestInvite(requestInputs, request, response);
        				if (responseMap != null)
        				{
        					// Send a response to caller
                			jsonResponse(request, response, responseMap);
        				}
        			}
        			else
        			{
        				errorResponse(request, response, "404", "Unknown command");
        			}
        		}
        		else
        		{
        			// Creating a new user
        			String refer_code = requestInputs.getString(Constants.REFERCODE_PARAMNAME);
                	boolean userReferral = false;
                	boolean specialCodeReferral = false;
                	
                	HashMap<String,String> results;
                	// Start by checking for special code usage
                	results = getReferringSpecialCode(refer_code);
                	if (results != null)
                	{
                	    specialCodeReferral = true;
                	}
                	else
                	{
                	    results = getReferringUser(refer_code);
                        if (results != null)
                        {
                            userReferral = true;
                        }
                        else
                        {
                            // Failed to find a referring user. Check businesses.
                            results = getReferringBusiness(refer_code);
                        }    
                	}
                	
                	if (results != null)
                	{
            			String referringId = null;
            			float amount = rewardCreditValue;
            			if (specialCodeReferral)
            			{
            			    // No referring id for special code referrals
            			    referringId = "";
            			    
            			    // Amount attached to special code
            			    String amtString = results.get(Constants.AMOUNT_PARAMNAME);
            			    amount = Float.parseFloat(amtString);
            			}
            			else if (userReferral)
            			{
            				// get the user id for the referring user
            				referringId = results.get(Constants.USERID_PARAMNAME);	
            			}
            			else
            			{
            				// get the business id for the referring business
            				referringId = results.get(Constants.BUSINESSID_PARAMNAME);
            			}
                		
                		int new_user_id = 0;
                		String registrationType = requestInputs.getString(Constants.TXTYPE_PARAMNAME);
                		if (registrationType != null)
                		{
                			// Generate a length 5 random alphanumeric code for the new user
                			String userCode = getRandomAlphaNumericCode(5);
                			if (registrationType.equalsIgnoreCase(emailRegistration))
                			{
                				// Perform email registration
                				new_user_id = registerEmailUser(request, response, requestInputs, referringId, userReferral, specialCodeReferral, userCode, amount);
                				if (new_user_id != 0)
                        		{
                					responseMap = createEmailRegistrationResponse(new_user_id, userCode, amount, requestInputs);
                        		}
                			}
                			else if (registrationType.equalsIgnoreCase(facebookRegistration))
                			{
                				// Perform Facebook registration
                				new_user_id = registerFBUser(request, response, requestInputs, referringId, userReferral, specialCodeReferral, userCode, amount);
                				if (new_user_id != 0)
                        		{
                					responseMap = createFacebookRegistrationResponse(new_user_id, userCode, amount, request);
                        		}
                			}
                			else
                			{
                				// Unknown registration type specified by caller
                			    SimpleLogger.getInstance().error(currentClassName, "UnknownRegistrationType|RegistrationType:" + registrationType);
                        		errorResponse(request, response, "404", "Registration error");
                			}
                		}
                		else
                		{
                			// No registration type specified by caller
                		    SimpleLogger.getInstance().error(currentClassName, "NullRegistrationType");
                    		errorResponse(request, response, "404", "Registration error");
                		}
                		
                		if (new_user_id != 0)
                		{
                    		// Successfully created user, now reward the referring user if there is one
                			if (userReferral)
                			{
                				// Get the referring user's info and then call function to reward them
                    			addRewardCreditToReferrer(referringId, results.get(Constants.CREDIT_PARAMNAME), Integer.toString(new_user_id));
                			}
                			
                			//
                			RecordsList.getInstance().recordSignup(Integer.toString(new_user_id), requestInputs.getString(Constants.NAME_PARAMNAME));
                			
                			// Send a response to caller
                			jsonResponse(request, response, responseMap);
                		}
                	}
                	else
                	{
                		errorResponse(request, response, "404", "The referral code is invalid");
                	}
        		}
        	}
    	}
    	catch (JSONException ex)
    	{
    	    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
			errorResponse(request, response, "500", "An unknown error occurred");
		}
    }
    
    /**
     * Handles the HTTP <code>PUT</code> method.
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
    	try
    	{
    		JSONObject responseMap = null;
        	float expectedAPIVersion = getExpectedVersion(request);
        	if (validateVersion(request, response, expectedAPIVersion))
        	{
        		String[] pathArray = getPathInfoArray(request);
    			if (pathArray != null && pathArray.length >= 1)
    			{
    				JSONObject requestInputs = getRequestData(request);	
    				
    				// /user/login
    				if (pathArray[0].equalsIgnoreCase("login"))
    				{
    					responseMap = handleLogin(request, response, requestInputs);
    				}
    				else
    				{
    					// else assume it's a user_id
    					// e.g. /user/1034
    					String user_id = pathArray[0];
        				if (pathArray.length > 1)
        				{
        					// /user/l034/password
        					if (pathArray[1].equalsIgnoreCase("password"))
                			{
                				responseMap = changePassword(request, requestInputs, response, user_id);
                			}
        					else
                			{
                				// No login type specified by caller
        					    SimpleLogger.getInstance().error(currentClassName, "UnknownRequestType");
                        		errorResponse(request, response, "403", "unknown request request");
                			}
        				}
        				else
        				{
        					// /user/1034
        					// updating user info
        					responseMap = changeUserInfo(request, requestInputs, response, user_id);
        				}
    					
    				}
    			}
    			else
    			{
    				// Could not find user
    			    SimpleLogger.getInstance().error(currentClassName, "MissingUserId");
            		errorResponse(request, response, "403", "Improper request");
    			}
        		
        		if (responseMap != null)
    			{
    				// Send a response to caller
        			jsonResponse(request, response, responseMap);
    			}
        	}
    	}
    	catch (Exception ex)
    	{
    	    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
			errorResponse(request, response, "500", "An unknown error occurred");
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
    		JSONObject responseMap = null;
        	float expectedAPIVersion = getExpectedVersion(request);
        	if (validateVersion(request, response, expectedAPIVersion))
        	{
        		String pathInfo = request.getPathInfo();
        		if (pathInfo != null)
        		{
        			String user_id = pathInfo.substring(1);
        			ArrayList<HashMap<String,String>> userResultsArray = getUserInfo(user_id, null);
        			if (userResultsArray.size() == 1)
                	{
        				HashMap<String,String> userInfo = userResultsArray.get(0);
        				String validSessionId = userInfo.get(Constants.SESSIONID_PARAMNAME);	
        				if (validateSessionId(validSessionId, request))
        				{
        					responseMap = createLoginResponse(userInfo);
        					
        					if (responseMap != null)
                			{            				
                				// Send a response to caller
                    			jsonResponse(request, response, responseMap);
                			}
        				}
        				else
        				{
        					errorResponse(request, response, "403", "You are already logged into a different device");
        				}
                	}
        			else
        			{
        				// Could not find user
                		Constants.logger.error("Error: Unable to find user with id: " + user_id);
                		SimpleLogger.getInstance().unknownUser(currentClassName, user_id);
                		errorResponse(request, response, "404", "Could not find user");
        			}
        		}
        		else
        		{
        			// Could not find user
        		    SimpleLogger.getInstance().error(currentClassName, "MissingUserId");
            		errorResponse(request, response, "404", "Could not find user");
        		}
        	}
    	}
    	catch (Exception ex)
    	{
    	    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
			errorResponse(request, response, "500", "An unknown error occurred");
		}
    }
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CorsUtils.addOptionsCorsHeaderInfo(req, resp);
        super.doOptions(req, resp);
    }
}
