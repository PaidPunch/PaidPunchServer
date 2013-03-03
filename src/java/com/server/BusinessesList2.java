package com.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;

import com.db.DataAccess;
import com.db.DataAccess.ResultSetHandler;

public class BusinessesList2 extends DataObjectBase 
{
    private static BusinessesList2 singleton;
    private HashMap<String,Business> currentBusinesses;
    
    // Private constructor
    private BusinessesList2() 
    {
        lastRefreshTime = null;
        currentBusinesses = null;
        currentClassName = Business.class.getSimpleName();
        // Refresh interval in milliseconds
        refreshInterval = 15 * 60 * 1000L;
    }
    
    private void getListOfBusinesses()
    {
        String queryString = "SELECT b.business_userid,b.business_name,b.buss_desc,a.contactno,b.logo_path,b.busi_enabled," + 
                "a.address_id,a.address_line1,a.city,a.state,a.zipcode,a.longitude,a.latitude," +
                "o.offer_id,o.discount,o.price,o.offertype,o.code,o.name,o.description,o.condition1,o.condition2,o.condition3,o.disabled,o.expiry_date " +
                "FROM business_users b, bussiness_address a, offers o " +
                "WHERE b.business_userid = a.business_id AND b.business_userid = o.business_id;";
        try
        {
            DataAccess.queryDatabaseCustom(queryString, null, currentBusinesses, new ResultSetHandler()
            {
                 public void handle(ResultSet results, Object returnObj) throws SQLException
                 { 
                     // The cast here is a well-known one, so the suppression is OK
                     @SuppressWarnings("unchecked")
                     HashMap<String,Business> hashBusinesses = (HashMap<String,Business>)returnObj;
                     while(results.next())
                     {
                         // First check to see if a business already exists for this one
                         String bizId = results.getString("business_userid");
                         Business currentBusiness = hashBusinesses.get(bizId);
                         if (currentBusiness == null)
                         {
                             // No business exists, create a new one
                             currentBusiness = new Business();
                             
                             // Populate product information
                             currentBusiness.setVersion(2);
                             currentBusiness.setBusinessUserId(bizId);
                             currentBusiness.setName(results.getString("business_name"));
                             currentBusiness.setDesc(results.getString("buss_desc"));
                             currentBusiness.setLogoPath(results.getString("logo_path"));
                             currentBusiness.setBusiEnabled(results.getString("busi_enabled"));    
                             
                             // Add current Business to the Business list
                             hashBusinesses.put(bizId, currentBusiness);
                         }
                         
                         String address_id = results.getString("address_id");
                         BusinessBranch currentBranch = null;
                         if (currentBusiness.getBranches() != null)
                         {
                             currentBusiness.getBranches().get(address_id);    
                         }
                         if (currentBranch == null)
                         {
                             // Create a new branch
                             currentBranch = new BusinessBranch();
                             
                             // Populate branch information
                             currentBranch.setAddressLine(results.getString("address_line1"));
                             currentBranch.setCity(results.getString("city"));
                             currentBranch.setState(results.getString("state"));
                             currentBranch.setZipcode(results.getString("zipcode"));
                             currentBranch.setLongitude(results.getString("longitude"));
                             currentBranch.setLatitude(results.getString("latitude"));
                             currentBranch.setContactNo(results.getString("contactno"));
                             
                             // Add current branch to the Business 
                             currentBusiness.insertBranch(address_id, currentBranch);    
                         }
                         
                         String offer_id = results.getString("offer_id");
                         BusinessOffer currentOffer = null;
                         if (currentBusiness.getOffers() != null)
                         {
                             currentBusiness.getOffers().get(offer_id);
                         }
                         if (currentOffer == null)
                         {
                             // Create a new offer
                             currentOffer = new BusinessOffer();
                             
                             // Populate offer information
                             currentOffer.setOfferId(results.getString("offer_id"));
                             currentOffer.setDiscount(Float.parseFloat(results.getString("discount")));
                             currentOffer.setType(Integer.parseInt(results.getString("offertype")));
                             currentOffer.setCouponCode(results.getString("code"));
                             currentOffer.setDesc(results.getString("description"));
                             currentOffer.setCondition1(results.getString("condition1"));
                             currentOffer.setCondition2(results.getString("condition2"));
                             currentOffer.setCondition3(results.getString("condition3"));
                             currentOffer.setDisabled(Boolean.parseBoolean(results.getString("disabled")));
                             currentOffer.setExpiryDate(results.getString("expiry_date"));
                             
                             // Add current branch to the Business 
                             currentBusiness.insertOffer(offer_id, currentOffer);
                         }
                     } 
                 }
            });
        }
        catch (SQLException ex)
        {
            SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
        }
    }
    
    private void refreshBusinessesFromDatabaseIfNecessary()
    {
        if (timeForRefresh())
        {
            try
            {
                currentBusinesses = new HashMap<String,Business>();
                
                getListOfBusinesses();
                
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
    
    public JSONArray getAllBusinesses()
    {
        // Refresh the data if necessary
        refreshBusinessesFromDatabaseIfNecessary();
        JSONArray jsonBusinesses = new JSONArray();
        for (Map.Entry<String, Business> entry : currentBusinesses.entrySet())
        {
            jsonBusinesses.put(entry.getValue().getJSONOfBusiness());
        }
        return jsonBusinesses;
    }
    
    public JSONArray getAllEnabledBusinesses()
    {
        // Refresh the data if necessary
        refreshBusinessesFromDatabaseIfNecessary();
        JSONArray jsonBusinesses = new JSONArray();
        for (Map.Entry<String, Business> entry : currentBusinesses.entrySet())
        {
            Business current = entry.getValue();
            if (current.getBusiEnabled())
            {
                jsonBusinesses.put(current.getJSONOfBusiness());    
            }
        }
        return jsonBusinesses;
    }
    
    // Disable cloning for singletons
    public Object clone() throws CloneNotSupportedException 
    {
        throw new CloneNotSupportedException();
    }
    
    // Singleton 
    public static synchronized BusinessesList2 getInstance() 
    {
        if (singleton == null) 
        {
            singleton = new BusinessesList2();
        }
        return singleton;
    }
}
