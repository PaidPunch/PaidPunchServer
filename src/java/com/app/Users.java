package com.app;

import com.db.DataAccess;
import com.db.DataAccess.ResultSetHandler;
import com.server.Constants;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Users extends XmlHttpServlet  {

	private static final long serialVersionUID = -9044506610414211667L;
	
	private ArrayList<String> postElements;
	private ArrayList<String> putElements;
	
	private void initializePostElements()
	{
		postElements = new ArrayList<String>();
		postElements.add(Constants.NAME_PARAMNAME);
		postElements.add(Constants.EMAIL_PARAMNAME);
		postElements.add(Constants.FBID_PARAMNAME);
		postElements.add(Constants.REFERCODE_PARAMNAME);
		postElements.add(Constants.SESSIONID_PARAMNAME);
	}
	
	private void initializePutElements()
	{
		putElements = new ArrayList<String>();
		putElements.add(Constants.USERID_PARAMNAME);
	}
	
	private void getReferringUser(String refer_code, ArrayList<HashMap<String,String>> resultsArray)
	{
		String queryString = "SELECT user_id, credit FROM app_user WHERE user_code = ?;";
		if (refer_code != null)
		{
			try
			{
				ArrayList<String> parameters = new ArrayList<String>();
				parameters.add(refer_code);
				DataAccess.queryDatabase(queryString, parameters, resultsArray, new ResultSetHandler()
				{
					 public void handle(ResultSet results, Object returnObj) throws SQLException
	                 { 						 
						 // The cast here is a well-known one, so the suppression is OK
						 @SuppressWarnings("unchecked")
						 ArrayList<HashMap<String,String>> resultsArray = (ArrayList<HashMap<String,String>>)returnObj;
	                     while(results.next())
	                     {                    	 
	                    	 // Populate user information
	                    	 HashMap<String,String> current = new HashMap<String,String>();
	                    	 current.put(Constants.USERID_PARAMNAME, results.getString(Constants.USERID_PARAMNAME));
	                    	 current.put(Constants.CREDIT_PARAMNAME, results.getString(Constants.CREDIT_PARAMNAME));
	                    	 resultsArray.add(current);
	                     }
	                 }
				});
			}
			catch (SQLException ex)
			{
				Constants.logger.error("Error : " + ex.getMessage());
			}
		}
	}
	
	@Override
    public void init(ServletConfig config) throws ServletException
    {
	   super.init(config);

	   try
	   {
		   ServletContext context = config.getServletContext();
		   Constants.loadJDBCConstants(context);
		   
		   initializePostElements();
		   initializePutElements();
	   }
	   catch(Exception e)
	   {
		   Constants.logger.error(e);
	   }
    }

	/**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {    	
    	float expectedAPIVersion = getExpectedVersion(request);
    	if (validateVersion(response, expectedAPIVersion))
    	{
        	HashMap<String, String> requestInputs = getRequestData(request, postElements);	
        	
        	ArrayList<HashMap<String,String>> resultsArray = new ArrayList<HashMap<String,String>>();
        	String refer_code = requestInputs.get(Constants.REFERCODE_PARAMNAME);
        	getReferringUser(refer_code, resultsArray);
        	if (resultsArray.size() == 1)
        	{
        		// TODO: Temporary reponse 
        		PrintWriter out = response.getWriter();                
                out.print("Hello World");
        	}
        	else if (resultsArray.size() > 1)
        	{
        		// The referral code returned more than a single user. Return an error after logging.
        		Constants.logger.error("Error: Refer code " + refer_code + " returned more than one user");
        		errorResponse(response, "404", "The referral code is invalid");
        	}
        	else
        	{
        		// The refer code does not match an existing user
        		Constants.logger.info("Warning: Refer code " + refer_code + " does not match any known users");
        		errorResponse(response, "404", "The referral code is invalid");
        	}
    	}

    	//xmlResponse(response, currentProducts);
    }
    
	/**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
    	float expectedAPIVersion = getExpectedVersion(request);
    	if (validateVersion(response, expectedAPIVersion))
    	{
        	HashMap<String, String> requestInputs = getRequestData(request, putElements);	
        	
        	// TODO: Remove later. Temporary to get rid of warning.
        	Constants.logger.info(requestInputs.toString());
    	}
    	
    	//xmlResponse(response, currentProducts);
    }
}
