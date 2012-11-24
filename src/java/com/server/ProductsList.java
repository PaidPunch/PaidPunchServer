package com.server;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.db.DataAccess;
import com.db.DataAccess.ResultSetHandler;

public class ProductsList 
{
	private static ProductsList singleton;
	private Date lastRefreshTime;
	private ArrayList<Product> currentProducts;
	
	// Private constructor
	private ProductsList() 
	{
		lastRefreshTime = null;
	}
	
	private boolean timeForRefresh()
	{
		if (lastRefreshTime != null)
		{
			Date currentTime = new Date();
			// Refresh interval in milliseconds
			final long refreshInterval = 15 * 60 * 1000L;
			Date refreshTime = new Date(lastRefreshTime.getTime() + refreshInterval);
			return (currentTime.compareTo(refreshTime) > 0);	
		}
		else
		{
			return true;
		}
	}
	
	private void refreshProductsFromDatabaseIfNecessary()
	{
		if (timeForRefresh())
		{
			currentProducts = new ArrayList<Product>();
			
			String queryString = "SELECT * FROM products;";
			try
			{
				DataAccess.queryDatabase(queryString, null, currentProducts, new ResultSetHandler()
				{
					 public void handle(ResultSet results, Object returnObj) throws SQLException
	                 { 
						 // The cast here is a well-known one, so the suppression is OK
						 @SuppressWarnings("unchecked")
	                     ArrayList<Product> arrayProducts = (ArrayList<Product>)returnObj;
	                     while(results.next())
	                     {
	                    	 Product currentProduct = new Product();
	                    	 
	                    	 // Populate product information
	                    	 currentProduct.setProductId(results.getString("product_id"));
	                    	 currentProduct.setName(results.getString("name"));
	                    	 currentProduct.setDesc(results.getString("desc"));
	                    	 currentProduct.setCredits(results.getString("credits"));
	                    	 currentProduct.setCost(results.getString("cost"));
	                    	 currentProduct.setDisabled(results.getString("disabled"));
	                    	 currentProduct.setModifiedDate(results.getString("date_modified"));
	                    	 
	                    	 // Add current product to the product list
	                    	 arrayProducts.add(currentProduct);
	                     }
	                 }
				});
				lastRefreshTime = new Date();
			}
			catch (SQLException ex)
			{
				Constants.logger.error("Error : " + ex.getMessage());
			}
		}
	}
	
	// Disable cloning for singletons
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public Product getProductByID(String product_id, boolean enabledOnly)
	{
		// Refresh the data if necessary
		refreshProductsFromDatabaseIfNecessary();
		
		for (Product current : currentProducts)
		{
			// Found product that matches requested ID
			if (current.getProductId().equalsIgnoreCase(product_id))
			{
				// Either the caller doesn't care whether product is enabled,
				// or product is enabled
				if (!enabledOnly || !current.getDisabled())
				{
					return current;
				}
				else
				{
					return null;
				}
			}
		}
		
		return null;
	}
	
	public ArrayList<Product> getProducts()
	{
		// Refresh the data if necessary
		refreshProductsFromDatabaseIfNecessary();
		return currentProducts;
	}
	
	public ArrayList<Product> getEnabledProducts()
	{
		// Refresh the data if necessary
		refreshProductsFromDatabaseIfNecessary();
		
		ArrayList<Product> enabledProducts = new ArrayList<Product>();
		for (Product current : currentProducts)
		{
			if (!current.getDisabled())
			{
				enabledProducts.add(current);
			}
		}
		
		return enabledProducts;
	}
	
	public HashMap<String, Object> getMapOfProducts()
	{
		// Refresh the data if necessary
		refreshProductsFromDatabaseIfNecessary();
				
		ArrayList<HashMap<String, Object>> mapArray = new ArrayList<HashMap<String, Object>>();
		for (Product current : currentProducts)
		{
			if (!current.getDisabled())
			{
				mapArray.add(current.getMapOfProduct());
			}
		}
		
		HashMap<String,Object> mp= new HashMap<String, Object>();
		mp.put("products", mapArray);
		
		return mp;
	}
	
	// Singleton 
	public static synchronized ProductsList getInstance() {
		if (singleton == null) {
			singleton = new ProductsList();
		}
		return singleton;
	}
}
