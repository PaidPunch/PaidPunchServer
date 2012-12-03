package com.app;

import com.db.DataAccess;
import com.db.DataAccessController;
import com.server.Constants;
import com.server.CreditChangeHistory;
import com.server.ProductsList;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.authorize.CustomerProfileCommunication;
import net.authorize.TransactionCommunication;

public class Products extends XmlHttpServlet  {

	private static final long serialVersionUID = 4628293433991773440L;
	
	private ArrayList<String> putElements;
	
	@Override
    public void init(ServletConfig config) throws ServletException
    {
	   super.init(config);

	   try
	   {
		   ServletContext context = config.getServletContext();
		   Constants.loadJDBCConstants(context);
		   
		   initializePutElements();
	   }
	   catch(Exception e)
	   {
		   Constants.logger.error(e);
	   }
    }

	private void initializePutElements()
	{
		putElements = new ArrayList<String>();
		putElements.add(Constants.USERID_PARAMNAME);
		putElements.add(Constants.SESSIONID_PARAMNAME);
		putElements.add(Constants.PRODUCTID_PARAMNAME);
	}
	
	// Get user info
	private ArrayList<HashMap<String,String>> getUserInfo(String user_id, Connection conn)
	{
		ArrayList<HashMap<String,String>> resultsArray = null;
		if (user_id != null)
		{		
			String queryString = null;
			if (conn != null)
			{
				// Connection not being null implies that we're doing a full transaction 
				// across multiple database calls
				queryString = "SELECT * FROM app_user WHERE user_id = ? FOR UPDATE;";	
			}
			else
			{
				queryString = "SELECT * FROM app_user WHERE user_id = ?;";
			}
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(user_id);
			resultsArray = DataAccess.queryDatabase(conn, queryString, parameters);
		}
		return resultsArray;
	}
	
	// Get product info
	private ArrayList<HashMap<String,String>> getProductInfo(String product_id)
	{
		ArrayList<HashMap<String,String>> resultsArray = null;
		if (product_id != null)
		{		
			String queryString = "SELECT * FROM products WHERE product_id = ?;";
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(product_id);
			resultsArray = DataAccess.queryDatabase(queryString, parameters);
		}
		return resultsArray;
	}
	
    private HashMap<String,String> getPaymentInfo(String user_id, String profile_id)
    {
    	HashMap<String,String> results = null;
    	CustomerProfileCommunication profile = new CustomerProfileCommunication();
        
    	try
    	{
    		// getCustomerProfileAuth actually returns a Vector, but all items
        	// within that vector are Strings.
        	@SuppressWarnings("unchecked")
        	Vector<String> profile_data = profile.getCustomerProfileAuth(profile_id);
        	
        	// code represents success or failure. On failure, just return null.
            String code = profile_data.elementAt(0);
            if (code.equalsIgnoreCase("00"))
            {
            	results = new HashMap<String,String>();
            	results.put("code", code);
            	results.put("message", profile_data.elementAt(1));
            	results.put("payment_id", profile_data.elementAt(2));
            	results.put("maskno", profile_data.elementAt(3));
            }	
            else
            {
            	Constants.logger.error("Error: Payment info retrieval failed for user_id " + user_id +
            			"with error code " + code + " and message: " + profile_data.elementAt(1));
            }
    	}
    	catch (Exception ex)
		{
			Constants.logger.error("Error : " + ex.getMessage());
		}
    	
        return results;
    }
    
    private boolean updateCreditsForUser(Connection conn, String user_id, String new_credit_amount)
	{
		boolean success = false;
		String queryString = "UPDATE app_user SET credit = ? WHERE user_id = ?";
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(new_credit_amount);
		parameters.add(user_id);
		try
		{
			success = DataAccess.updateDatabaseWithExistingConnection(conn, queryString, parameters);	
		}
		catch (SQLException ex)
        {
    		success = false;
        	Constants.logger.error(ex);
        }
		
		return success;
	}
    
    // This function call is for record keeping purposes. Do not fail overall transaction if this fails
    private void insertPaymentDetails(String user_id, String product_id, String transaction_id, String gatewaymsg)
    {
		String queryString = "INSERT INTO payment_details (punchcard_id,appuser_id,token,response,product_id) values(?,?,?,?,?)";
		
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add("0");
		parameters.add(user_id);
		parameters.add(transaction_id);
		parameters.add(gatewaymsg);
		parameters.add(product_id);
		
		try
    	{
        	int new_id = DataAccess.insertDatabase(queryString, parameters);
        	if (new_id == 0)
        	{
        		Constants.logger.error("Error: Could not insert new row into payment_details for user_id: " +
        				user_id +
        				"and product_id: " +
        				product_id);
        	}
    	}
    	catch (SQLException ex)
        {
        	Constants.logger.error(ex);
        }
    }
    
    private boolean updateUserCredits(String user_id, String product_id, float increaseAmount)
    {
    	boolean success = false;
		Connection conn = DataAccessController.createConnection();
		try
		{
			conn.setAutoCommit(false);
			
			ArrayList<HashMap<String,String>> userResultsArray = getUserInfo(user_id, conn);
			if (userResultsArray.size() == 1)
        	{
				HashMap<String,String> userInfo = userResultsArray.get(0);
				
				float credit = Float.parseFloat(userInfo.get("credit"));
				float newAmount = credit + increaseAmount;
				
				// update number of credits remaining for current user
				success = updateCreditsForUser(conn, user_id, Float.toString(newAmount));
				if (success)
				{
					// Insert tracking row into change history table
					// If this fails, don't return an error
			        CreditChangeHistory changeHistory = CreditChangeHistory.getInstance();
					changeHistory.insertCreditChange(user_id, increaseAmount, CreditChangeHistory.PURCHASE, product_id);
				}
				else
				{
					// credit update failed
	        		Constants.logger.error("Credit update failed for user_id " + user_id);
				}
        	}
			else
			{
				// Could not find user
        		Constants.logger.error("Error: Unable to find user with id: " + user_id);
			}
			
			if (success)
			{
				conn.commit();
			}
			else
			{
				conn.rollback();
			}	
		}
		catch (SQLException ex)
		{
			Constants.logger.error("Error : " + ex.getMessage());
		}
		finally
		{
			try
			{
				conn.close();
			}
			catch (SQLException ex)
			{
				Constants.logger.error("Error : " + ex.getMessage());
			}
		}
		return success;
    }
    
    private boolean purchaseProduct(String user_id, String product_id, String profile_id, String payment_id, float cost, float credit)
    {
    	TransactionCommunication tc = new TransactionCommunication();
    	boolean success = false;
    	
    	try
    	{
        	// getCustomerProfileAuth actually returns a Vector, but all items
        	// within that vector are Strings.
        	@SuppressWarnings("unchecked")
            Vector<String> paymentdata = tc.makePaymentAuthCapture(profile_id, payment_id, Float.toString(cost));
            String code = paymentdata.elementAt(0);
            String servermsg = paymentdata.elementAt(1);
            String gatewaymsg = paymentdata.elementAt(2);
            String invoiceno = paymentdata.elementAt(3);
            String authcode = paymentdata.elementAt(4);
            String transaction_id = paymentdata.elementAt(5);
            Constants.logger.info("Payment details:" +
            	" userid: " + user_id +
            	", code: " + code +
            	", servermsg: " + servermsg +
            	", gatewaymsg: " + gatewaymsg +
            	", invoiceno: " + invoiceno +
            	", authcode: " + authcode +
            	", transactionid: " + transaction_id);
            
            // Insert row into payment_details table for tracking purposes
            insertPaymentDetails(user_id, product_id, transaction_id, gatewaymsg);

            // Update credits for user
            if (code.equalsIgnoreCase("00")) 
            {
            	success = updateUserCredits(user_id, product_id, credit);	
            }
            else
            {
            	Constants.logger.error("Error: Unable to purchase credits for user: " + user_id);
            }
    	}
    	catch (Exception ex)
		{
			Constants.logger.error("Error : " + ex.getMessage());
		}
    	return success;
    }
	
	/**
     * Handles the HTTP <code>GET</code> method.
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {    	
    	ProductsList products = ProductsList.getInstance();
    	HashMap<String, Object> currentProducts = products.getMapOfProducts();
    	xmlResponse(response, currentProducts);
    }
    
	/**
     * Handles the HTTP <code>PUT</code> method.
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
        	
        	String user_id = requestInputs.get(Constants.USERID_PARAMNAME);
        	ArrayList<HashMap<String,String>> userResultsArray = getUserInfo(user_id, null);
        	if (userResultsArray.size() == 1)
        	{
        		String product_id = requestInputs.get(Constants.PRODUCTID_PARAMNAME);
            	ArrayList<HashMap<String,String>> productResultsArray = getProductInfo(product_id);
            	if (productResultsArray.size() == 1)
            	{
            		HashMap<String,String> productInfo = productResultsArray.get(0);
            		// Check if punchcard is still valid
            		if (!Boolean.getBoolean(productInfo.get("disabled")))
            		{
            			boolean success = false;
            			HashMap<String,String> userInfo = userResultsArray.get(0);
            			String profile_id = userInfo.get("profile_id");
            			HashMap<String,String> paymentInfo = getPaymentInfo(user_id, profile_id);
            			if (paymentInfo != null)
            			{
            				success = purchaseProduct(
            								user_id, 
            								product_id, 
            								profile_id, 
            								paymentInfo.get("payment_id"), 
            								Float.parseFloat(productInfo.get("cost")),
            								Float.parseFloat(productInfo.get("credits")));
            			}
            			
            			if (success)
            			{
            				// Provide successful response to caller
    	            		HashMap<String, Object> responseMap = new HashMap<String,Object>();
    	            		responseMap.put("statusCode", "00");
    	            		responseMap.put("statusMessage", "Credit product purchased successfully");
    	            		
    	            		// Send a response to caller
    	        			xmlResponse(response, responseMap);
            			}
            			else
                		{
                			// Could not retrieve payment information
                    		Constants.logger.error("Error: Unable to retrieve  " + product_id);
                    		errorResponse(response, "400", "Unable to purchase credits");
                		}
            		}
            		else
            		{
            			// Product is disabled
                		Constants.logger.error("Error: Attempt to purchase disabled product " + product_id);
                		errorResponse(response, "403", "This product is no longer available for purchase");
            		}
            	}
            	else
            	{
            		// Could not find product
            		Constants.logger.error("Error: Unable to find product with id: " + product_id);
            		errorResponse(response, "404", "Unknown credit product");
            	}
        	}
        	else
        	{
        		// Could not find user
        		Constants.logger.error("Error: Unable to find user with id: " + user_id);
        		errorResponse(response, "404", "Unknown user");
        	}
    	}
    	
    	//xmlResponse(response, currentProducts);
    }
}
