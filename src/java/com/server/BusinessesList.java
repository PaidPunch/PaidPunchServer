package com.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;

import com.db.DataAccess;
import com.db.DataAccess.ResultSetHandler;

public class BusinessesList extends DataObjectBase
{
	private static BusinessesList singleton;
	private ArrayList<Business> currentBusinesses;
	
	// Private constructor
	private BusinessesList() 
	{
		lastRefreshTime = null;
		currentBusinesses = null;
		currentClassName = Business.class.getSimpleName();
		// Refresh interval in milliseconds
        refreshInterval = 15 * 60 * 1000L;
	}
	
	private void refreshBusinessesFromDatabaseIfNecessary()
	{
		if (timeForRefresh())
		{
			try
			{
				currentBusinesses = new ArrayList<Business>();
				
				String queryString = "SELECT b.business_userid,b.business_name,b.buss_desc,a.contactno,b.logo_path,b.busi_enabled," + 
				                             "a.address_line1,a.city,a.state,a.zipcode,a.longitude,a.latitude " +
				                             "FROM business_users b, bussiness_address a WHERE b.business_userid = a.business_id;";
				try
				{
					DataAccess.queryDatabaseCustom(queryString, null, currentBusinesses, new ResultSetHandler()
					{
						 public void handle(ResultSet results, Object returnObj) throws SQLException
		                 { 
							 // The cast here is a well-known one, so the suppression is OK
							 @SuppressWarnings("unchecked")
							 ArrayList<Business> arrayBusinesses = (ArrayList<Business>)returnObj;
		                     while(results.next())
		                     {
		                    	 Business currentBusiness = new Business();
		                    	 
		                    	 // Populate product information
		                    	 currentBusiness.setVersion(1);
		                    	 currentBusiness.setBusinessUserId(results.getString("business_userid"));
		                    	 currentBusiness.setName(results.getString("business_name"));
		                    	 currentBusiness.setDesc(results.getString("buss_desc"));
		                    	 currentBusiness.setContactNo(results.getString("contactno"));
		                    	 currentBusiness.setLogoPath(results.getString("logo_path"));
		                    	 currentBusiness.setAddressLine(results.getString("address_line1"));
		                    	 currentBusiness.setCity(results.getString("city"));
		                    	 currentBusiness.setState(results.getString("state"));
		                    	 currentBusiness.setZipcode(results.getString("zipcode"));
		                    	 currentBusiness.setLongitude(results.getString("longitude"));
		                    	 currentBusiness.setLatitude(results.getString("latitude"));
		                    	 currentBusiness.setBusiEnabled(results.getString("busi_enabled"));
		                    	 
		                    	 // Add current Business to the Business list
		                    	 arrayBusinesses.add(currentBusiness);
		                     } 
		                 }
					});
					lastRefreshTime = new Date();
				}
				catch (SQLException ex)
				{
				    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
				}
	    		
	    		lastRefreshTime = new Date();
			}
			catch (Exception ex)
	    	{
			    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
				lastRefreshTime = null;
				currentBusinesses = null;
			}
		}
	}
	
	// Disable cloning for singletons
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public JSONArray getBusinesses()
	{
		// Refresh the data if necessary
		refreshBusinessesFromDatabaseIfNecessary();
		JSONArray jsonBusinesses = new JSONArray();
		for (Business current : currentBusinesses)
		{
			jsonBusinesses.put(current.getJSONOfBusiness());
		}
		return jsonBusinesses;
	}
	
	public JSONArray getEnabledBusinesses()
	{
		// Refresh the data if necessary
		refreshBusinessesFromDatabaseIfNecessary();
		JSONArray jsonBusinesses = new JSONArray();
		for (Business current : currentBusinesses)
		{
			if (current.getBusiEnabled())
			{
				jsonBusinesses.put(current.getJSONOfBusiness());	
			}
		}
		return jsonBusinesses;
	}
	
	// Singleton 
	public static synchronized BusinessesList getInstance() 
	{
		if (singleton == null) 
		{
			singleton = new BusinessesList();
		}
		return singleton;
	}
}
