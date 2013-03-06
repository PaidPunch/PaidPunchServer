package com.server;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.db.DataAccess;
import com.db.SimpleDB;

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
		// Get UUID for naming new suggestion
        UUID itemName = UUID.randomUUID();
        
        // Get current datetime
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDatetime = datetimeFormat.format(new java.util.Date().getTime()); 
        
        List<ReplaceableAttribute> listAttributes = new ArrayList<ReplaceableAttribute>();
        listAttributes.add(new ReplaceableAttribute("user_id", user_id, true));
        listAttributes.add(new ReplaceableAttribute("credit_change", Float.toString(creditChange), true));
        listAttributes.add(new ReplaceableAttribute("reason", Integer.toString(reason), true));
        listAttributes.add(new ReplaceableAttribute("source_id", source_id, true));
        listAttributes.add(new ReplaceableAttribute("date_modified", currentDatetime, true));
        
        SimpleDB sdb = SimpleDB.getInstance();
        sdb.updateItem(Constants.CREDITCHANGE_DOMAIN, itemName.toString(), listAttributes);
		
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
