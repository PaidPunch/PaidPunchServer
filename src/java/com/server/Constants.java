package com.server;

import java.io.FileInputStream;
import javax.servlet.ServletContext;
import org.apache.log4j.*;
import org.apache.log4j.PropertyConfigurator;
import java.util.Properties;


/**
 * @author user
 */
public class Constants {

    public static Logger logger = Logger.getLogger(Constants.class.getName());
    public final static String SQL_DATE_FORMAT = "yyyyMMdd";
    public final static String SQL_TIME_FORMAT = "HHmm";

    // Used in database connection
    public static String JDBC_DRIVER = "";
    public static String JDBC_URL = "";
    public static String USERID = "";
    public static String PASSWORD = "";

    public static String TEMP_IMAGE_FOLDER_URL = "";
    public static String TEMP_IMAGE_FOLDER_PATH = "";
    public static String IP_URL = "";
    public static String LOGO_TEMP_PATH = "";
    public static String PunchTime = "";
    public static String Pdf_Read_Path = "";
    public static String Pdf_Write_Path = "";
    public static String imagePath = "";
    public static String logoActualPath = "";
    // public static String LOG_FOLDER_PATH ="";
    // public static String DATA_FOLDER_PATH ="";
    // public static String DATA_FOLDER_URL ="";

    public static String APP_HOME_PATH = "";

    public static String QR_CODE_URL = "";
    public static String merchant_code_validate_time = "";
    public static String filereader = "";
    public static String applicationName = "";

    // logger

    public static void loadJDBCConstants(ServletContext context) {
        init(context);

        JDBC_DRIVER = context.getInitParameter("JDBC_DRIVER");
        JDBC_URL = context.getInitParameter("JDBC_URL");
        USERID = context.getInitParameter("USERID");
        PASSWORD = context.getInitParameter("PASSWORD");
        IP_URL = context.getInitParameter("URL");
        LOGO_TEMP_PATH = context.getInitParameter("LOGO_TEMP_PATH");
        PunchTime = context.getInitParameter("punchTime");
        Pdf_Read_Path = context.getInitParameter("PDF_READ_PATH");
        Pdf_Write_Path = context.getInitParameter("PDF_WRITE_PATH");
        imagePath = context.getInitParameter("IMAGES_PATH");
        logoActualPath = context.getInitParameter("LOGO_IMAGE_PATH");
        merchant_code_validate_time = context.getInitParameter("merchant_code_validate_time");
        filereader = context.getInitParameter("filereader");
        applicationName = context.getInitParameter("application_name");
        // HOST_IP = context.getInitParameter("HOST_IP");
    }

    public static void init(ServletContext context) {
        try {
            Properties props = new Properties();
            // context=getServletContext();
            String path = context.getRealPath("paidpunch.properties").toString();

            props.load(new FileInputStream(path));

            PropertyConfigurator.configure(props);
        } catch (java.io.IOException ioe) {
            System.out.println("Error in loading log file" + ioe.getMessage());
        }
        java.util.Properties prop = System.getProperties();
        java.util.Enumeration enum1 = prop.propertyNames();
    }

}