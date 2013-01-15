package com.server;

import java.io.InputStream;
import org.json.*;

public class Template 
{
	private String templateId;
	private String name;
	private String desc;
	private String groupId;
	private String groupName;
	private String template;
	private boolean usesFile;
	private boolean disabled;
	private String modifiedDate;
	
	public String getTemplateId()
	{
		return templateId;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public String getGroupId()
	{
		return groupId;
	}
	
	public String getGroupName()
	{
		return groupName;
	}
	
	public String getTemplate()
	{
		return template;
	}
	
	public boolean getUsesFile()
	{
		return usesFile;
	}
	
	public boolean getDisabled()
	{
		return disabled;
	}
	
	public String getModifiedDate()
	{
		return modifiedDate;
	}
	
	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}
	
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	
	public void setTemplate(String template, String usesFile)
	{
		this.usesFile = Boolean.parseBoolean(usesFile);
		if (this.usesFile)
		{
			String templatePath = "com/server/" + template;
			InputStream inStreamTemplate = this.getClass().getClassLoader().getResourceAsStream(templatePath);
    		if (inStreamTemplate != null)
    		{
    			String templateString = Utility.convertInputStreamToString(inStreamTemplate);
    			this.template = templateString;
    		}
		}
		else
		{
			this.template = template;
		}
	}
	
	public void setDisabled(String disabled)
	{
		this.disabled = Boolean.parseBoolean(disabled);
	}
	
	public void setModifiedDate(String modifiedDate)
	{
		this.modifiedDate = modifiedDate;
	}
	
	public JSONObject getMapOfTemplate()
	{
		JSONObject jsonOutput= new JSONObject();

		try
		{
	        // adding or set elements in Map by put method key and value pair
			jsonOutput.put("template_id", templateId);
			jsonOutput.put("name", name);
			jsonOutput.put("desc", desc);
			jsonOutput.put("templateValue", template);
			jsonOutput.put("group_id", groupId);	
		}
		catch (JSONException ex)
		{
			Constants.logger.error("Error : " + ex.getMessage());
		}
		
		return jsonOutput;
	}
}
