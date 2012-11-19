package com.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlRequestParser extends DefaultHandler 
{
	private ArrayList<String> validElements;
    private HashMap<String, String> xmlData;
    private String tempVal = "";
    private String parameter_val = "";

    protected XmlRequestParser() 
    {
    }
    
    public XmlRequestParser(ArrayList<String> validElements)
    {
    	this.validElements = validElements;
    }

    public void parseDocument(InputSource in) 
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try 
        {
            // get a new instance of parser
            SAXParser sp = spf.newSAXParser();
            // parse the file and also register this class for call backs
            sp.parse(in, this);

        } 
        catch (SAXException se) 
        {
            Constants.logger.error(se.toString());
        } 
        catch (ParserConfigurationException pce) 
        {
            Constants.logger.error(pce.toString());
        } 
        catch (IOException ie) 
        {
            Constants.logger.error(ie.toString());
        }
    }

    // Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
    {
        if (qName.equalsIgnoreCase("paidpunch-req")) 
        {
            xmlData = new HashMap<String, String>();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException 
    {
        tempVal = new String(ch, start, length);
        parameter_val = parameter_val + tempVal;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException 
    {
    	boolean expectedElementFound = false;
    	if (!(qName.equalsIgnoreCase("paidpunch-req")))
    	{
        	for (String current : validElements)
    		{
        		if (qName.equalsIgnoreCase(current)) 
        		{
                    xmlData.put(current, parameter_val);
                    parameter_val = "";
                    expectedElementFound = true;
                    break;
                }
    		}
    	}
    	else
    	{
    		// Element was paidpunch-req, which is a well known enclosing element that can be ignored.
    		expectedElementFound = true;
    	}
    	if (!expectedElementFound)
    	{
    		// This element was an unexpected one. Go ahead and log it, and then clear the parameter_val. 
    		Constants.logger.info("Unexpected element in XML request: " + qName + " with value " + parameter_val);
    		parameter_val = "";
    	}
    }

    public HashMap<String, String> getData() 
    {
        return xmlData;
    }

}