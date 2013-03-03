package com.server;

import org.json.JSONException;
import org.json.JSONObject;

public class BusinessOffer 
{
    private String currentClassName;
    
    private String offerId;
    private float discount;
    private int offerType;
    private String couponCode;
    private String desc;
    private String condition1;
    private String condition2;
    private String condition3;
    private boolean disabled;
    private String expiryDate;
    private String modifiedDate;
    
    public BusinessOffer() 
    {
        currentClassName = BusinessOffer.class.getSimpleName();
    }
    
    public String getOfferId()
    {
        return offerId;
    }
    
    public float getDiscount()
    {
        return discount;
    }
    
    public int getType()
    {
        return offerType;
    }
    
    public String getCouponCode()
    {
        return couponCode;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public String getCondition1()
    {
        return condition1;
    }
    
    public String getCondition2()
    {
        return condition2;
    }
    
    public String getCondition3()
    {
        return condition3;
    }
    
    public boolean getDisabled()
    {
        return disabled;
    }
    
    public String getExpiryDate()
    {
        return expiryDate;
    }
    
    public String getModifiedDate()
    {
        return modifiedDate;
    }
    
    public void setOfferId(String offerId)
    {
        this.offerId = offerId;
    }
    
    public void setDiscount(float discount)
    {
        this.discount = discount;
    }
    
    public void setType(int offerType)
    {
        this.offerType = offerType;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public void setCouponCode(String couponCode)
    {
        this.couponCode = couponCode;
    }
    
    public void setCondition1(String condition1)
    {
        this.condition1 = condition1;
    }
    
    public void setCondition2(String condition2)
    {
        this.condition2 = condition2;
    }
    
    public void setCondition3(String condition3)
    {
        this.condition3 = condition3;
    }
    
    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }
    
    public void setExpiryDate(String expiryDate)
    {
        this.expiryDate = expiryDate;
    }
    
    public void setModifiedDate(String modifiedDate)
    {
        this.modifiedDate = modifiedDate;
    }
    
    public JSONObject getJSONOfOffer()
    {
        JSONObject jsonOutput= new JSONObject();

        try
        {
            jsonOutput.put("offerid", offerId);
            jsonOutput.put("discount", discount); 
            jsonOutput.put("type", offerType);   
            jsonOutput.put("code", couponCode); 
            jsonOutput.put("condition1", condition1); 
            jsonOutput.put("condition2", condition2); 
            jsonOutput.put("condition3", condition3);   
            jsonOutput.put("disabled", disabled); 
            jsonOutput.put("expirydate", expiryDate); 
            jsonOutput.put("modifieddate", modifiedDate);  
        }
        catch (JSONException ex)
        {
            SimpleLogger.getInstance().error(currentClassName, ex.getMessage());
        }
        
        return jsonOutput;
    }
}
