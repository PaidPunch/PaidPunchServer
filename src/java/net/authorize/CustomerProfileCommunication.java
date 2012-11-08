package net.authorize;

import java.util.Vector;
import net.authorize.cim.Result;
import net.authorize.cim.TransactionType;
import net.authorize.cim.ValidationModeType;
import net.authorize.data.cim.CustomerProfile;
import net.authorize.data.cim.PaymentProfile;
import net.authorize.data.creditcard.CreditCard;
import net.authorize.data.xml.Customer;
import net.authorize.data.xml.CustomerType;
import net.authorize.data.xml.Payment;
import org.junit.Assert;

/**
 * @author admin
 */
public class CustomerProfileCommunication {

    /*
     * private static String customerProfileId = null; private static String customerPaymentProfileId = null; // private
     * static String customerShippingAddressId = null; private static String authCode = null; private static String
     * transactionId = null; private static String splitTenderId = null;
     * 
     * String refId; private CreditCard creditCard;
     * 
     * private Customer customer; private CustomerProfile customerProfile; private PaymentProfile paymentProfile; //
     * Payment payment; private UnitTestData utd = null;
     */

    public Vector createCustomerProfileAuth(String customerName, String customerEmail, String creditCardno,
            String expiryDate, String cvvNo) throws Exception {
        String refId;

        com.server.Constants.logger.info("******************Request for Creating Profile from Server************");
        com.server.Constants.logger.info("Customer Name : " + customerName + " Customer Email : " + customerEmail
                + "\n ExpiryDate : " + expiryDate + "CVV No : " + cvvNo);
        UnitTestData utd = new UnitTestData();

        Vector createCustVector = new Vector();
        refId = "REFID:" + System.currentTimeMillis();

        // Create a new credit card
        CreditCard creditCard = CreditCard.createCreditCard();
        creditCard.setCreditCardNumber(creditCardno);
        creditCard.setExpirationDate(expiryDate);
        creditCard.setCardCode(cvvNo);
        // creditCard.setCardType(CardType.VISA);

        Customer customer;
        customer = Customer.createCustomer();
        customer.setEmail(customerEmail);

        // Create a payment profile
        PaymentProfile paymentProfile = PaymentProfile.createPaymentProfile();
        paymentProfile.addPayment(Payment.createPayment(creditCard));
        paymentProfile.setCustomerType(CustomerType.INDIVIDUAL);

        CustomerProfile customerProfile = CustomerProfile.createCustomerProfile();
        customerProfile.setDescription(customerName);
        customerProfile.setMerchantCustomerId("" + System.currentTimeMillis());
        customerProfile.setEmail(customerEmail);

        net.authorize.cim.Transaction transaction = utd.getMerchant().createCIMTransaction(
                TransactionType.CREATE_CUSTOMER_PROFILE);
        transaction.setRefId(refId);
        transaction.setCustomerProfile(customerProfile);
        transaction.addPaymentProfile(paymentProfile);
        transaction.setValidationMode(ValidationModeType.TEST_MODE);

        Result<Transaction> result = (Result<Transaction>) utd.getMerchant().postTransaction(transaction);

        Assert.assertNotNull(result);

        createCustVector = result.returnCreateProfile();
        if (createCustVector.get(0).toString().equalsIgnoreCase("00")) {
            try {
                Assert.assertTrue(result.isOk());
                Assert.assertNotNull(result.getRefId());
                Assert.assertNotNull(result.getCustomerProfileId());
                Assert.assertNotNull(result.getCustomerPaymentProfileIdList());
                Assert.assertTrue(result.getCustomerPaymentProfileIdList().size() == 1);
                Assert.assertTrue(result.getDirectResponseList().size() == 1);
            } catch (Exception e) {
                com.server.Constants.logger.error(e.getMessage());
            }
        }

        // customerProfileId = result.getCustomerProfileId();

        // v = testCreateCustomerProfile();

        // System.out.println("Vector contains...");
        // display elements of Vector
        com.server.Constants.logger.info("*****Response in Vector Format sent for Create Customer pofile********");
        for (int index = 0; index < createCustVector.size(); index++)
            com.server.Constants.logger.info("" + createCustVector.get(index));
        // System.out.println(createCustVector.get(index));
        return createCustVector;
    }

    public Vector getCustomerProfileAuth(String getcustomerProfileId) throws Exception {
        Vector getCustVector = new Vector();
        com.server.Constants.logger
                .info("******************Request for Getting Profile Details from Server************");
        com.server.Constants.logger.info("CustomerProfile ID : " + getcustomerProfileId);
        UnitTestData utd = new UnitTestData();
        net.authorize.cim.Transaction transaction = utd.getMerchant().createCIMTransaction(
                TransactionType.GET_CUSTOMER_PROFILE);

        transaction.setCustomerProfileId(getcustomerProfileId);
        Result<Transaction> result = (Result<Transaction>) utd.getMerchant().postTransaction(transaction);

        getCustVector = result.printCustomerProfileMessages();

        if (getCustVector.get(0).toString().equalsIgnoreCase("00")) {
            try {
                Assert.assertNotNull(result);
                Assert.assertTrue(result.isOk());
                Assert.assertNotNull(result.getCustomerProfileId());
                Assert.assertNotNull(result.getCustomerProfile());
                // Assert.assertEquals(utd.getCustomerDescription(), result.getCustomerProfile().getDescription());
                // Assert.assertEquals(utd.getEmail(), result.getCustomerProfile().getEmail());
                Assert.assertNotNull(result.getCustomerProfile().getCustomerProfileId());
                // Assert.assertNotNull(result.getCustomerProfile().getShipToAddressList());
                // Assert.assertTrue(result.getCustomerProfile().getShipToAddressList().size() > 0);
                Assert.assertNotNull(result.getCustomerPaymentProfileIdList());
                Assert.assertTrue(result.getCustomerPaymentProfileIdList().size() > 0);
            } catch (Exception e) {
                com.server.Constants.logger.error(e.getMessage());
            }
        }

        // v = testGetCustomerProfileRequest(profileId);

        // System.out.println("Vector contains...");
        // display elements of Vector
        com.server.Constants.logger.info("*****Response in Vector Format sent for Get Customer profile********");
        for (int index = 0; index < getCustVector.size(); index++)
            com.server.Constants.logger.info("" + getCustVector.get(index));
        return getCustVector;
    }

    public Vector testDeleteCustomerProfileRequest(String deletecustomerProfileId) {
        // delete a customer profile
        com.server.Constants.logger
                .info("******************Request for Deleting Profile Details from Server************");
        com.server.Constants.logger.info("CustomerProfile ID to delete : " + deletecustomerProfileId);
        Vector deleteCustVector = new Vector();

        String refId = "REFID:" + System.currentTimeMillis();
        UnitTestData utd = new UnitTestData();
        net.authorize.cim.Transaction transaction = utd.getMerchant().createCIMTransaction(
                TransactionType.DELETE_CUSTOMER_PROFILE);
        transaction.setRefId(refId);
        transaction.setCustomerProfileId(deletecustomerProfileId);

        Result<Transaction> result = (Result<Transaction>) utd.getMerchant().postTransaction(transaction);
        deleteCustVector = result.returnDeleteProfile();

        if (deleteCustVector.get(0).toString().equalsIgnoreCase("00")) {
            try {
                Assert.assertNotNull(result);
                // result.printMessages();
                Assert.assertTrue(result.isOk());
                Assert.assertNotNull(result.getRefId());

            } catch (Exception e) {
                com.server.Constants.logger.error(e.getMessage());
            }
        }

        // System.out.println("Vector contains...");
        com.server.Constants.logger.info("*****Response in Vector Format sent for Delete Customer pofile********");
        // display elements of Vector
        for (int index = 0; index < deleteCustVector.size(); index++)
            com.server.Constants.logger.info("" + deleteCustVector.get(index));
        // System.out.println(deleteCustVector.get(index));
        return deleteCustVector;
        // recreate for next unit test
        // testCreateCustomerProfile();
    }

/*
    public Vector createCustomerProfileAuth(String customerName, String customerEmail, String creditCardno, String expiryDate, String cvvNo) throws Exception{
            Vector v = new Vector();
            refId = "REFID:" + System.currentTimeMillis();
	    // Create a payment profile

            paymentProfile = PaymentProfile.createPaymentProfile();

            // Create a new credit card
            //
	    creditCard = CreditCard.createCreditCard();
	    creditCard.setCreditCardNumber(creditCardno);
	    creditCard.setExpirationDate(expiryDate);
            creditCard.setCardCode(cvvNo);

            customer = Customer.createCustomer();
            customer.setEmail(customerEmail);

            paymentProfile.addPayment(Payment.createPayment(creditCard));
	    paymentProfile.setCustomerType(CustomerType.INDIVIDUAL);

	    customerProfile = CustomerProfile.createCustomerProfile();
            customerProfile.setDescription(customerName);
	    customerProfile.setMerchantCustomerId(""+System.currentTimeMillis());
	    customerProfile.setEmail(customerEmail);

            v = testCreateCustomerProfile();

            System.out.println("Vector contains...");
            //display elements of Vector
            for(int index=0; index < v.size(); index++)
                System.out.println(v.get(index));
            return v;
        }

*/

   /*
    public Vector getCustomerProfileAuth(String profileId) throws Exception{
            Vector v = new Vector();
            //refId = "REFID:" + System.currentTimeMillis();
	    // Create a payment profile

            v = testGetCustomerProfileRequest(profileId);

            System.out.println("Vector contains...");
            //display elements of Vector
            for(int index=0; index < v.size(); index++)
                System.out.println(v.get(index));
            return v;
        }

/*
    public Vector testCreateCustomerProfile() {
                Vector createCustVector = new Vector();
		// Create a new customer profile
		net.authorize.cim.Transaction transaction =  utd.getMerchant().createCIMTransaction(TransactionType.CREATE_CUSTOMER_PROFILE);
		transaction.setRefId(refId);
		transaction.setCustomerProfile(customerProfile);
		transaction.addPaymentProfile(paymentProfile);
		transaction.setValidationMode(ValidationModeType.TEST_MODE);

		Result<Transaction> result = (Result<Transaction>)utd.getMerchant().postTransaction(transaction);

		Assert.assertNotNull(result);

                createCustVector = result.returnCreateProfile();
		if(createCustVector.get(0).toString().equalsIgnoreCase("00")){
                    try{
                    Assert.assertTrue(result.isOk());
                    Assert.assertNotNull(result.getRefId());
                    Assert.assertNotNull(result.getCustomerProfileId());
                    Assert.assertNotNull(result.getCustomerPaymentProfileIdList());
                    Assert.assertTrue(result.getCustomerPaymentProfileIdList().size() == 1);
                    Assert.assertTrue(result.getDirectResponseList().size() == 1);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

            //customerProfileId = result.getCustomerProfileId();
               return createCustVector;
	}


                public Vector testGetCustomerProfileRequest(String getcustomerProfileId) {
		// get a customer profile
                 Vector getCustVector = new Vector();

		net.authorize.cim.Transaction transaction =  utd.getMerchant().createCIMTransaction(TransactionType.GET_CUSTOMER_PROFILE);

		//transaction.setCustomerProfileId(customerProfileId);
                transaction.setCustomerProfileId(getcustomerProfileId);
		Result<Transaction> result = (Result<Transaction>)utd.getMerchant().postTransaction(transaction);


                getCustVector = result.printCustomerProfileMessages();

                if(getCustVector.get(0).toString().equalsIgnoreCase("00")){
                    try{
                        Assert.assertNotNull(result);
                        Assert.assertTrue(result.isOk());
                        Assert.assertNotNull(result.getCustomerProfileId());
                        Assert.assertNotNull(result.getCustomerProfile());
//                        Assert.assertEquals(utd.getCustomerDescription(), result.getCustomerProfile().getDescription());
//                        Assert.assertEquals(utd.getEmail(), result.getCustomerProfile().getEmail());
                        Assert.assertNotNull(result.getCustomerProfile().getCustomerProfileId());
//                        Assert.assertNotNull(result.getCustomerProfile().getShipToAddressList());
  //                      Assert.assertTrue(result.getCustomerProfile().getShipToAddressList().size() > 0);
                        Assert.assertNotNull(result.getCustomerPaymentProfileIdList());
                        Assert.assertTrue(result.getCustomerPaymentProfileIdList().size() > 0);
                        }catch(Exception e){
                         e.printStackTrace();
                        }
                    }

                return getCustVector;
	}
*/


}
