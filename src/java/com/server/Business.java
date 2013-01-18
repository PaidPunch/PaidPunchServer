package com.server;

import org.json.JSONException;
import org.json.JSONObject;

public class Business 
{
	private String business_userid;
	private String name;
	private String desc;
	private String contactno;
	private String logo_path;
	private boolean busi_enabled;
	private String address_line1;
	private String city;
	private String state;
	private String zipcode;
	private String latitude;
	private String longitude;
	
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
	
	public JSONObject getMapOfBusiness()
	{
		JSONObject jsonOutput= new JSONObject();

		try
		{
	        // adding or set elements in Map by put method key and value pair
			jsonOutput.put("business_userid", business_userid);
			jsonOutput.put("name", name);
			jsonOutput.put("desc", desc);
			jsonOutput.put("contactno", contactno);
			jsonOutput.put("address_line1", address_line1);	
			jsonOutput.put("city", city);	
			jsonOutput.put("state", state);	
			jsonOutput.put("zipcode", zipcode);	
			jsonOutput.put("longitude", longitude);	
			jsonOutput.put("latitude", latitude);	
			jsonOutput.put("logo_path", logo_path);	
		}
		catch (JSONException ex)
		{
			Constants.logger.error("Error : " + ex.getMessage());
		}
		
		return jsonOutput;
	}
}
