package net.authorize;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.BigDecimal;
import java.util.Vector;
import net.authorize.cim.Result;
import net.authorize.cim.TransactionType;
import net.authorize.data.Order;
import net.authorize.data.cim.PaymentTransaction;
import org.junit.Assert;


/**
 *
 * @author admin
 */
public class TransactionCommunication {
    String refId;
    Order order;
    PaymentTransaction paymentTransaction;
    private UnitTestData utd = null;

    public Vector makePaymentAuthCapture(String profileIdfromApp, String paymentProfileIdfromApp, String amount) throws Exception{
                Vector transactionDetailsVector = new Vector();
                UnitTestData utd = new UnitTestData();

                com.server.Constants.logger.info("******************Request for making Transaction from Server************");
//	private static String customerShippingAddressId = null;
                com.server.Constants.logger.info("CustomerProfile ID : "+profileIdfromApp + "Payment Profile Id : "+paymentProfileIdfromApp+" Amount : "+amount);
                Order order;
	//	Payment payment;
                PaymentTransaction paymentTransaction;

                BigDecimal amount_bd = new BigDecimal(amount);
                //refId = "REFID:" + System.currentTimeMillis();
                // Create a payment profile

                order = Order.createOrder();
                //order.setDescription(utd.getOrderDescription());
                order.setInvoiceNumber(Long.toString(System.currentTimeMillis()));
                order.setTotalAmount(amount_bd);

                /*
                    // create order item
                    OrderItem orderItem = OrderItem.createOrderItem();
                    orderItem.setItemDescription(utd.getItemDescription());
                    orderItem.setItemId(utd.getItemId());
                    orderItem.setItemName(utd.getItemName());
                    orderItem.setItemPrice(utd.getItemPrice());
                    orderItem.setItemQuantity(utd.getItemQuantity());
                    orderItem.setItemTaxable(true);
                    order.addOrderItem(orderItem);


                    for(int i = 0; i <= 2; i++) {
                            order.addOrderItem(orderItem);
                    }
    */

                paymentTransaction = PaymentTransaction.createPaymentTransaction();
                paymentTransaction.setOrder(order);
               // paymentTransaction.setCardCode(utd.getCardholderAuthenticationValue());

                String refId = "REFID:" + System.currentTimeMillis();

                net.authorize.cim.Transaction transaction =  utd.getMerchant().createCIMTransaction(TransactionType.CREATE_CUSTOMER_PROFILE_TRANSACTION);
		transaction.setRefId(refId);
		transaction.setCustomerProfileId(profileIdfromApp);
		transaction.setCustomerPaymentProfileId(paymentProfileIdfromApp);
		//transaction.setCustomerShippingAddressId(customerShippingAddressId);
		paymentTransaction.setTransactionType(net.authorize.TransactionType.AUTH_CAPTURE);
		transaction.setPaymentTransaction(paymentTransaction);
		//transaction.addExtraOption("ip_address","127.0.0.1");
                try{
		Result<Transaction> result = (Result<Transaction>)utd.getMerchant().postTransaction(transaction);

                transactionDetailsVector = result.printTransactionProfileMessages();

                if(transactionDetailsVector.get(0).toString().equalsIgnoreCase("00")){
                   try{
                        Assert.assertNotNull(result);
                        Assert.assertTrue(result.isOk());
                        Assert.assertNotNull(result.getRefId());
                        Assert.assertTrue(result.getDirectResponseList().size() > 0);
                    }catch(Exception e){
                         com.server.Constants.logger.error(e.getMessage());
                        }
                   }
                }catch(Exception e){
                    com.server.Constants.logger.error(e.getMessage());

                }


            //v = testCreateCustomerProfileTransactionRequest_AuthCapture(profileIdfromApp,paymentProfileIdfromApp);

//                System.out.println("Vector contains...");
                 com.server.Constants.logger.info("*****Response in Vector Format sent for Transaction Resquest********");
                //display elements of Vector
                for(int index=0; index < transactionDetailsVector.size(); index++)
                    com.server.Constants.logger.info(""+transactionDetailsVector.get(index));
//                    System.out.println(transactionDetailsVector.get(index));
               return transactionDetailsVector;
        }


    /*
    public Vector makePaymentAuthCapture(String profileIdfromApp, String paymentProfileIdfromApp, String amount) throws Exception{
            Vector v = new Vector();
            BigDecimal amount_bd = new BigDecimal(amount);
            //refId = "REFID:" + System.currentTimeMillis();
	    // Create a payment profile

            order = Order.createOrder();
	    //order.setDescription(utd.getOrderDescription());
	    order.setInvoiceNumber(Long.toString(System.currentTimeMillis()));
	    order.setTotalAmount(amount_bd);

            /*
		// create order item
		OrderItem orderItem = OrderItem.createOrderItem();
		orderItem.setItemDescription(utd.getItemDescription());
		orderItem.setItemId(utd.getItemId());
		orderItem.setItemName(utd.getItemName());
		orderItem.setItemPrice(utd.getItemPrice());
		orderItem.setItemQuantity(utd.getItemQuantity());
		orderItem.setItemTaxable(true);
		order.addOrderItem(orderItem);


		for(int i = 0; i <= 2; i++) {
			order.addOrderItem(orderItem);
		}


            paymentTransaction = PaymentTransaction.createPaymentTransaction();
	    paymentTransaction.setOrder(order);
	   // paymentTransaction.setCardCode(utd.getCardholderAuthenticationValue());

            v = testCreateCustomerProfileTransactionRequest_AuthCapture(profileIdfromApp,paymentProfileIdfromApp);

            System.out.println("Vector contains...");
            //display elements of Vector
            for(int index=0; index < v.size(); index++)
                System.out.println(v.get(index));
            return v;
        }

    public Vector testCreateCustomerProfileTransactionRequest_AuthCapture(String profid,String paymentprofId) {
		// Create an auth capture txn request

                Vector transactionDetailsVector = new Vector();

                net.authorize.cim.Transaction transaction =  utd.getMerchant().createCIMTransaction(TransactionType.CREATE_CUSTOMER_PROFILE_TRANSACTION);
		transaction.setRefId(refId);
		transaction.setCustomerProfileId(profid);
		transaction.setCustomerPaymentProfileId(paymentprofId);
		//transaction.setCustomerShippingAddressId(customerShippingAddressId);
		paymentTransaction.setTransactionType(net.authorize.TransactionType.AUTH_CAPTURE);
		transaction.setPaymentTransaction(paymentTransaction);
		//transaction.addExtraOption("ip_address","127.0.0.1");

		Result<Transaction> result = (Result<Transaction>)utd.getMerchant().postTransaction(transaction);

                transactionDetailsVector = result.printTransactionProfileMessages();

                if(transactionDetailsVector.get(0).toString().equalsIgnoreCase("00")){
                   try{
                        Assert.assertNotNull(result);
                        //result.printMessages();
                        Assert.assertTrue(result.isOk());
 //                       Assert.assertNotNull(result.getRefId());
                        //transactionId = result.getDirectResponseList().get(0).getDirectResponseMap().get(ResponseField.TRANSACTION_ID);
                        //transactionId = result.getDirectResponseList().get(0).getDirectResponseMap().get(ResponseField.AUTHORIZATION_CODE);
                        Assert.assertTrue(result.getDirectResponseList().size() > 0);
                    }catch(Exception e){
                         e.printStackTrace();
                        }
                   }
                return transactionDetailsVector;
	}
*/
}
