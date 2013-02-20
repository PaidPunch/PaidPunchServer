package com.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.db.SimpleDB;

public class RecordsList 
{
	private static RecordsList singleton;
	public static String PURCHASE_PUNCH = "PurchasedPunch";
	public static String PURCHASE_CREDIT = "PurchasedCredit";
	public static String USED_PUNCH = "UsedPunch";
	public static String SIGNED_UP = "SignedUp";
	
	private static String PURCHASE_PUNCH_DESC = "<USERNAME> purchased a $<AMOUNT> punchcard for <BUSINESSNAME>";
	private static String PURCHASE_CREDIT_DESC = "<USERNAME> purchased <AMOUNT> credits";
	private static String USED_PUNCH_DESC = "<USERNAME> used a punchcard at <BUSINESSNAME>";
	private static String SIGNED_UP_DESC = "<USERNAME> just signed up for PaidPunch. Not just good looking, smart too.";
	
	private HashMap<String,String> descMap = null;
	
	// Private constructor
	private RecordsList() 
	{
		descMap = new HashMap<String,String>();
		descMap.put(PURCHASE_PUNCH, PURCHASE_PUNCH_DESC);
		descMap.put(PURCHASE_CREDIT, PURCHASE_CREDIT_DESC);
		descMap.put(USED_PUNCH, USED_PUNCH_DESC);
		descMap.put(SIGNED_UP, SIGNED_UP_DESC);
	}
	
	private String replacePlaceholders(String recordType, String userName, String businessName, String amount)
	{
		String template = descMap.get(recordType);
		
		if (userName != null)
		{
			template = template.replace("<USERNAME>", userName);
		}
		
		if (businessName != null)
		{
			template = template.replace("<BUSINESSNAME>", businessName);
		}
		
		if (amount != null)
		{
			template = template.replace("<AMOUNT>", amount);
		}
		
		return template;
	}
	
	private boolean recordUserAction(String user_id, String recordType, String desc)
	{
		boolean success = false;
		
		// Get UUID for naming new vote
		UUID itemName = UUID.randomUUID();
		
		// Get current datetime
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDatetime = datetimeFormat.format(new java.util.Date().getTime());	
		
		try
		{
			List<ReplaceableAttribute> listAttributes = new ArrayList<ReplaceableAttribute>();
			listAttributes.add(new ReplaceableAttribute("user_id", user_id, true));
			listAttributes.add(new ReplaceableAttribute("record_type", recordType, true));
			listAttributes.add(new ReplaceableAttribute("desc", desc, true));
			listAttributes.add(new ReplaceableAttribute("modifiedDatetime", currentDatetime, true));
			
			SimpleDB sdb = SimpleDB.getInstance();
			sdb.updateItem(Constants.RECORDS_DOMAIN, itemName.toString(), listAttributes);
			
			success = true;
		}
		catch (Exception ex) 
		{
			Constants.logger.error("Error : " + ex.getMessage());
		}
		
		return success;
	}
	
	public boolean recordPunchPurchase(String user_id, String userName, String businessName, String amount)
	{
		boolean success = false;
		String desc = replacePlaceholders(PURCHASE_PUNCH, userName, businessName, amount);
		success = recordUserAction(user_id, PURCHASE_PUNCH, desc);
		return success;
	}
	
	public boolean recordCreditPurchase(String user_id, String userName, String amount)
	{
		boolean success = false;
		String desc = replacePlaceholders(PURCHASE_CREDIT, userName, null, amount);
		success = recordUserAction(user_id, PURCHASE_CREDIT, desc);
		return success;
	}
	
	public boolean recordPunchUse(String user_id, String userName, String businessName)
	{
		boolean success = false;
		String desc = replacePlaceholders(PURCHASE_PUNCH, userName, businessName, null);
		success = recordUserAction(user_id, PURCHASE_PUNCH, desc);
		return success;
	}
	
	public boolean recordSignup(String user_id, String userName)
	{
		boolean success = false;
		String desc = replacePlaceholders(SIGNED_UP, userName, null, null);
		success = recordUserAction(user_id, SIGNED_UP, desc);
		return success;
	}
	
	// Singleton 
	public static synchronized RecordsList getInstance() 
	{
		if (singleton == null) 
		{
			singleton = new RecordsList();
		}
		return singleton;
	}
}
