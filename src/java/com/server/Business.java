package com.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Business 
{
    private String currentClassName;
    
    private int version;
	private String business_userid;
	private String name;
	private String desc;
	private String logo_path;
	private boolean busi_enabled;
	private String url_path;
	
	// V2-only arrays
	private HashMap<String,BusinessBranch> businessBranches;
	private HashMap<String,Punchcard> punchcards;
	
	// Moved to BusinessBranches in V2.
	// This is here for V1 backward compatibility only
	private String address_line1;
	private String city;
	private String state;
	private String zipcode;
	private String latitude;
	private String longitude;
	private String contactno;
	
	public Business() 
    {
        currentClassName = Business.class.getSimpleName();
    }
	
	public int getVersion()
    {
        return version;
    }
	
	public String getBusinessUserId()
	{
		return business_userid;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public String getContactNo()
	{
		return contactno;
	}
	
	public String getLogoPath()
	{
		return logo_path;
	}
	
	public String getAddressLine()
	{
		return address_line1;
	}
	
	public boolean getEnabled()
	{
		return busi_enabled;
	}
	
	public String getCity()
	{
		return city;
	}
	
	public String getState()
	{
		return state;
	}
	
	public String getZipcode()
	{
		return zipcode;
	}
	
	public String getLatitude()
	{
		return latitude;
	}
	
	public String getLongitude()
	{
		return longitude;
	}
	
	public boolean getBusiEnabled()
	{
		return busi_enabled;
	}
	
	public HashMap<String,BusinessBranch> getBranches()
	{
	    return businessBranches;
	}
	
	public HashMap<String,Punchcard> getPunchcards()
    {
        return punchcards;
    }
	
	public String getUrlPath()
	{
	    return url_path;
	}
	
	public void setVersion(int version)
    {
        this.version = version;
    }
	
	public void setBusinessUserId(String business_userid)
	{
		this.business_userid = business_userid;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
	public void setContactNo(String contactno)
	{
		this.contactno = contactno;
	}
	
	public void setLogoPath(String logo_path)
	{
		this.logo_path = Utility.getTrueLogoPath(logo_path);
	}
	
	public void setAddressLine(String address_line1)
	{
		this.address_line1 = address_line1;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}
	
	public void setState(String state)
	{
		this.state = state;
	}
	
	public void setZipcode(String zipcode)
	{
		this.zipcode = zipcode;
	}
	
	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}
	
	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}
	
	public void setBusiEnabled(String busi_enabled)
	{
		this.busi_enabled = busi_enabled.equalsIgnoreCase("Y");
	}
	
	public void setUrlPath(String url_path)
	{
	    this.url_path = url_path;
	}
	
	public void insertBranch(String address_id, BusinessBranch branch)
	{
	    if (businessBranches == null)
	    {
	        businessBranches = new HashMap<String,BusinessBranch>();
	    }
	    
	    businessBranches.put(address_id, branch);
	}
	
	public void insertPunchcard(String punchcardid, Punchcard punchcard)
    {
        if (punchcards == null)
        {
            punchcards = new HashMap<String,Punchcard>();
        }
        
        punchcards.put(punchcardid, punchcard);
    }
	
	public JSONObject getJSONOfBusiness()
	{
		JSONObject jsonOutput= new JSONObject();

		try
		{
	        // adding or set elements in Map by put method key and value pair
			jsonOutput.put("business_userid", business_userid);
			jsonOutput.put("name", name);
			jsonOutput.put("desc", desc);
			jsonOutput.put("logo_path", logo_path);	
			
			if (version == 1)
			{
		        jsonOutput.put("contactno", contactno);
	            jsonOutput.put("address_line1", address_line1); 
	            jsonOutput.put("city", city);   
	            jsonOutput.put("state", state); 
	            jsonOutput.put("zipcode", zipcode); 
	            jsonOutput.put("longitude", longitude); 
	            jsonOutput.put("latitude", latitude);   
			}
			else if (version == 2)
			{
			    jsonOutput.put("url_path", url_path);
			    
			    JSONArray jsonBranches = new JSONArray();
			    for (Map.Entry<String, BusinessBranch> entry : businessBranches.entrySet())
			    {
			        jsonBranches.put(entry.getValue().getJSONOfBranch());  
			    }
		        jsonOutput.put("branches", jsonBranches);
		        
		        JSONArray jsonPunchcards = new JSONArray();
		        for (Map.Entry<String, Punchcard> entry : punchcards.entrySet())
                {
		            jsonPunchcards.put(entry.getValue().getJSONOfOffer());  
                }
                jsonOutput.put("offers", jsonPunchcards);
			}
		}
		catch (JSONException ex)
		{
		    SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
		}
		
		return jsonOutput;
	}
	
	public JSONObject getJSONOfBusiness(ArrayList<Integer> regions)
    {
        JSONObject jsonOutput= null;

        try
        {            
            if (version == 1)
            {
                SimpleLogger.getInstance().warn(Business.class.getSimpleName(), "This function is not available for version 1 calls!"); 
            }
            else if (version == 2)
            {
                boolean atLeastOneBranch = false;
                JSONArray jsonBranches = new JSONArray();
                for (Map.Entry<String, BusinessBranch> entry : businessBranches.entrySet())
                {
                    if (entry.getValue().isInRegionList(regions))
                    {
                        jsonBranches.put(entry.getValue().getJSONOfBranch());  
                        atLeastOneBranch = true;
                    }
                }
                
                // At least a single branch in this business is in the region we care about
                if (atLeastOneBranch)
                {
                    jsonOutput= new JSONObject();
                    
                    // adding or set elements in Map by put method key and value pair
                    jsonOutput.put("business_userid", business_userid);
                    jsonOutput.put("name", name);
                    jsonOutput.put("desc", desc);
                    jsonOutput.put("logo_path", logo_path); 
                    jsonOutput.put("url_path", url_path);
                    
                    jsonOutput.put("branches", jsonBranches);
                    
                    JSONArray jsonPunchcards = new JSONArray();
                    for (Map.Entry<String, Punchcard> entry : punchcards.entrySet())
                    {
                        jsonPunchcards.put(entry.getValue().getJSONOfOffer());  
                    }
                    jsonOutput.put("offers", jsonPunchcards);    
                }
            }
        }
        catch (JSONException ex)
        {
            SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
        }
        
        return jsonOutput;
    }
}
