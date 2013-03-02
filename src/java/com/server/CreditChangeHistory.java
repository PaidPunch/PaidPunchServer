package com.server;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.db.DataAccess;

public class CreditChangeHistory 
{
	private static CreditChangeHistory singleton;
	
	// Change types
	public static final int USER_INVITE_SIGNUP = 1;
	public static final int BUSINESS_INVITE_SIGNUP = 2;
	public static final int USER_REFERRAL = 3;
	public static final int PURCHASE = 4;
	public static final int PUNCHCARD = 5;
	public static final int SPECIAL_INVITE_SIGNUP = 6;
	
	// Private constructor
	private CreditChangeHistory() 
	{
	}
	
	public boolean insertCreditChange(String user_id, float creditChange, int reason, String source_id)
	{
		boolean success = false;
		String queryString = "INSERT INTO creditchangehistory (user_id,credit_change,reason,source_id,date_modified) values(?,?,?,?,?)";
		
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(user_id);
		parameters.add(Float.toString(creditChange));
		parameters.add(Integer.toString(reason));
		parameters.add(source_id);
		
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDatetime = datetimeFormat.format(new java.util.Date().getTime());	
		parameters.add(strDatetime);
		
		try
    	{
        	int new_id = DataAccess.insertDatabase(queryString, parameters);
        	success = (new_id != 0);	
    	}
    	catch (SQLException ex)
        {
    		success = false;
        	Constants.logger.error(ex);
        }
		
		return success;
	}
	
	// Singleton 
	public static synchronized CreditChangeHistory getInstance() 
	{
		if (singleton == null) 
		{
			singleton = new CreditChangeHistory();
		}
		return singleton;
	}
}
