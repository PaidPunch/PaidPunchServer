package com.server;

import java.util.HashMap;

public class Product {

	private String productId;
	private String name;
	private String desc;
	private boolean disabled;
	private float credits;
	private float cost;
	private String modifiedDate;
	
	public String getProductId()
	{
		return productId;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public String getModifiedDate()
	{
		return modifiedDate;
	}
	
	public float getCredits()
	{
		return credits;
	}
	
	public float getCost()
	{
		return cost;
	}
	
	public boolean getDisabled()
	{
		return disabled;
	}
	
	public void setProductId(String productId)
	{
		this.productId = productId;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
	public void setModifiedDate(String modifiedDate)
	{
		this.modifiedDate = modifiedDate;
	}
	
	public void setCredits(String credits)
	{
		this.credits = Float.parseFloat(credits);
	}
	
	public void setCost(String cost)
	{
		this.cost = Float.parseFloat(cost);
	}
	
	public void setDisabled(String disabled)
	{
		this.disabled = Boolean.parseBoolean(disabled);
	}
	
	public HashMap<String, Object> getMapOfProduct()
	{
		HashMap<String,Object> internalMap= new HashMap<String, Object>();

        // adding or set elements in Map by put method key and value pair
		internalMap.put("product_id", productId);
		internalMap.put("name", name);
		internalMap.put("desc", desc);
		internalMap.put("credits", credits);
		internalMap.put("cost", cost);
		
		return internalMap;
	}
}
