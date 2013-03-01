package com.server;

import javax.servlet.ServletContext;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.*;

/**
 * @author user
 */
public class Constants 
{
    public static Logger logger = Logger.getLogger(Constants.class.getName());
    public final static String SQL_DATE_FORMAT = "yyyyMMdd";
    public final static String SQL_TIME_FORMAT = "HHmm";
    
    // Parameter names used for receiving input and providing output to client
    public static String TXTYPE_PARAMNAME = "txtype";
    public static String USERID_PARAMNAME = "user_id";
    public static String NAME_PARAMNAME = "username";
    public static String EMAIL_PARAMNAME = "email";
    public static String MOBILENO_PARAMNAME = "mobile_no";
    public static String PASSWORD_PARAMNAME = "password";
    public static String NEWPASSWORD_PARAMNAME = "new_password";
    public static String FBID_PARAMNAME = "fbid";
    public static String CREDIT_PARAMNAME = "credit";
    public static String DISABLED_PARAMNAME = "disabled";
    public static String REFERCODE_PARAMNAME = "refer_code";
    public static String USERCODE_PARAMNAME = "user_code";
    public static String SESSIONID_PARAMNAME = "sessionid";
    public static String PUNCHCARDID_PARAMNAME = "punchcardid";
    public static String BUSINESSID_PARAMNAME = "business_userid";
    public static String PRODUCTID_PARAMNAME = "product_id";
    public static String PROFILECREATED_PARAMNAME = "isprofile_created";
    public static String ZIPCODE_PARAMNAME = "zipcode";
    public static String BIZNAME_PARAMNAME = "business_name";
    public static String BIZINFO_PARAMNAME = "business_info";
    
    // SimpleDB domain names
    public static String BUSINESSES_DOMAIN = "Businesses";
    public static String RECORDS_DOMAIN = "Records";
    public static String REQUESTINVITES_DOMAIN = "RequestInvites";
    public static String SUGGESTBUSINESSES_DOMAIN = "SuggestBusinesses";
    public static String VOTES_DOMAIN = "Votes";

    // Used in database connection
    public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static String JDBC_URL = "";
    public static String USERID = "";
    public static String PASSWORD = "";
    
    // MailChimp list ID
    public static String MAILCHIMP_LIST_ID = "";

    public static String TEMP_IMAGE_FOLDER_URL = "";
    public static String TEMP_IMAGE_FOLDER_PATH = "";
    public static String IP_URL = "";
    public static String LOGO_TEMP_PATH = "";
    public static String imagePath = "";
    public static String logoActualPath = "";

    public static String APP_HOME_PATH = "";

    public static String QR_CODE_URL = "";
    public static String merchant_code_validate_time = "";
    public static String filereader = "";
    public static String applicationName = "";
    
    private static boolean initialized = false;
    
    // logger

    public static void loadJDBCConstants(ServletContext context) 
    {
    	if (!initialized)
    	{
    		init(context);

            LOGO_TEMP_PATH = context.getInitParameter("LOGO_TEMP_PATH");
            imagePath = context.getInitParameter("IMAGES_PATH");
            logoActualPath = context.getInitParameter("LOGO_IMAGE_PATH");
            merchant_code_validate_time = context.getInitParameter("merchant_code_validate_time");
            filereader = context.getInitParameter("filereader");
            applicationName = context.getInitParameter("application_name");		
            
            initialized = true;
    	}
    }
    
    private static boolean isProduction() 
    {
        Object o;
        try 
        {
           o = (new InitialContext()).lookup("java:comp/env/isProduction");
        }  
        catch (NamingException e) 
        {
           o = Boolean.FALSE; // assumes FALSE if the value isn't declared
        }
        return o == null ? Boolean.FALSE : (Boolean) o;
     }
    
    private static void initEndPoints()
    {
        if (isProduction())
        {
            SimpleLogger.getInstance().info(Constants.class.getSimpleName(), "Initializing production server endpoints");
            
            // Settings for production server
            JDBC_URL = "jdbc:mysql://paidpunchprod.csepczasc6nf.us-west-2.rds.amazonaws.com:3306/paidpunchprod";
            USERID = "paidpunchprod";
            PASSWORD = "Biscuit-1";
            
            BUSINESSES_DOMAIN = "Businesses";
            RECORDS_DOMAIN = "Records";
            REQUESTINVITES_DOMAIN = "RequestInvites";
            SUGGESTBUSINESSES_DOMAIN = "SuggestBusinesses";
            VOTES_DOMAIN = "Votes";
            
            MAILCHIMP_LIST_ID = "e7350c242f";
            
            IP_URL = "https://api.paidpunch.com";
        }
        else
        {
            SimpleLogger.getInstance().info(Constants.class.getSimpleName(), "Initializing test server endpoints");
            
            // Settings for test server
            JDBC_URL = "jdbc:mysql://paidpunchtest.csepczasc6nf.us-west-2.rds.amazonaws.com:3306/paidpunchtest";
            USERID = "paidpunch";
            PASSWORD = "Biscuit-1";
            
            BUSINESSES_DOMAIN = "BusinessesTest";
            RECORDS_DOMAIN = "RecordsTest";
            REQUESTINVITES_DOMAIN = "RequestInvitesTest";
            SUGGESTBUSINESSES_DOMAIN = "SuggestBusinessesTest";
            VOTES_DOMAIN = "VotesTest";
            
            MAILCHIMP_LIST_ID = "4ded3248b9";
            
            IP_URL = "https://test.paidpunch.com";
        }
    }

    private static void init(ServletContext context) 
    {        
        java.util.Properties prop = System.getProperties();
        java.util.Enumeration enum1 = prop.propertyNames();
        
        initEndPoints();
    }

}