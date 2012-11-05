package net.authorize;

import net.authorize.*;
import java.math.BigDecimal;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.data.creditcard.AVSCode;
import net.authorize.data.creditcard.CardType;
import net.authorize.data.echeck.BankAccountType;
import net.authorize.data.echeck.ECheckType;

public class UnitTestData {

	//protected static String apiLoginID = "2Mfk7E77";
	//protected static String transactionKey = "32TtuG628W938nYB";
//        protected static String apiLoginID = "2a8XX76p5q2";
//	protected static String transactionKey = "76bb7f69A8TnBW6X";
        protected static String apiLoginID = "3Q3KjuB3LvY6";
        protected static String transactionKey = "3Q3UTr5m3BSx9v56";
	protected static String cp_apiLoginID = System.getProperty("CP_API_LOGIN_ID");
	protected static String cp_transactionKey = System.getProperty("CP_TRANSACTION_KEY");
	protected static String merchantMD5Key = "9876543210123456789";
	protected static Merchant merchant = Merchant.createMerchant(Environment.PRODUCTION_TESTMODE, apiLoginID, transactionKey);

	// customer information
	protected  String firstName = "Shahid";
	protected  String lastName = "Tamboli";
	protected  String address = "";
	protected  String city = "Pune";
	protected  String state = "Maharashtra";
	protected  String zipPostalCode = "411016";
	protected  String magicSplitTenderZipCode = "46225";
	protected  String company = "Mobimedia Technologies";
	protected  String country = "US";
	protected  String customerId = "CUST000000";
	protected  String customerIP = "127.0.0.1";
	protected  String email = "shahid@mmobimedia.in";
	protected  String email2 = "customer2@merchant.com";
	protected  String phone = "9890218239";
	protected  String fax = "415-555-1313";
	protected  String customerDescription = "Customer A";
	protected  String customerDescription2 = "Customer B";

	// email receipt information
	protected  String headerEmailReceipt = "Thank you for purchasing " +
			"Widgets from The Antibes Company";
	protected  String footerEmailReceipt = "If you have any problems, " +
			"please contact us at +44 20 5555 1212";
	protected  String merchantEmail = "shahid1311@gmail.com";

	// order information
	protected  String orderDescription = "Widgets";
	protected  String invoiceNumber = Long.toString(System.currentTimeMillis());
	protected  String mdfKey = "notes";
	protected  String mdfValue = "Would like a blue widget.";

	// order item information
	protected  String itemDescription = "A widget for widgeting.";
	protected  String itemId = "widget_a_1000";
	protected  String itemName = "Widget A";
	protected  BigDecimal itemPrice = new BigDecimal(19.99);
	protected  BigDecimal itemQuantity = new BigDecimal(1.00);
	protected  String itemDescription2 = "A FREE micro widget.";
	protected  String itemId2 = "mwidget_z_0001";
	protected  String itemName2 = "Micro Widget Z";
	protected  BigDecimal itemPrice2 = new BigDecimal(0.01);
	protected  BigDecimal itemQuantity2 = new BigDecimal(1.00);

	// shipping address information is the same as the customer address

	// shipping charges information
	protected  BigDecimal dutyAmount = new BigDecimal(0.00);
	protected  String dutyItemDescription = "VAT";
	protected  String dutyItemName = "VAT Tax";
	protected  BigDecimal freightAmount = new BigDecimal(5.00);
	protected  String freightDescription = "Flat rate";
	protected  String freightItemName = "Shipping";
	protected  String purchaseOrderNumber = "PO-1001";
	protected  BigDecimal taxAmount = new BigDecimal(2.37);
	protected  String taxDescription = "9.5%";
	protected  boolean taxExempt = false;
	protected  String taxItemName = "California Tax";

	// credit card information
	protected  String creditCardNumber = "4346-7810-1034-1352";
	protected  String rawCreditCardNumber = "4346781010341352";
	protected  String maskedCreditCardNumber = "xxxx1111";
	protected  CardType cardType = CardType.VISA;
	protected  String creditCardExpMonth = "12";
	protected  String creditCardExpYear = "2020";
	protected  AVSCode avsCode = AVSCode.P;
	protected  String cardCodeVerification = "P";
	protected  String cardholderAuthenticationIndicator = "5";
	protected  String cardholderAuthenticationValue = "123";

	// eCheck information
	protected  String bankAccountName = "Test Checking";
	protected  String bankAccountNumber = "1234567890";
	protected  BankAccountType bankAccountType = BankAccountType.CHECKING;
	protected  String bankCheckNumber = "1001";
	protected  String bankName = "Bank of America";
	protected  ECheckType eCheckType = ECheckType.WEB;
	protected  String routingNumber = "111000025";

	// transaction information
	protected  BigDecimal totalAmount = new BigDecimal(27.36);

	protected  String reportingBatchId = "814302";
	protected  String reportingTransId = "2156009012";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static String getApiLoginID() {
        return apiLoginID;
    }

    public static void setApiLoginID(String apiLoginID) {
        UnitTestData.apiLoginID = apiLoginID;
    }

    public AVSCode getAvsCode() {
        return avsCode;
    }

    public void setAvsCode(AVSCode avsCode) {
        this.avsCode = avsCode;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public BankAccountType getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(BankAccountType bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public String getBankCheckNumber() {
        return bankCheckNumber;
    }

    public void setBankCheckNumber(String bankCheckNumber) {
        this.bankCheckNumber = bankCheckNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardCodeVerification() {
        return cardCodeVerification;
    }

    public void setCardCodeVerification(String cardCodeVerification) {
        this.cardCodeVerification = cardCodeVerification;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardholderAuthenticationIndicator() {
        return cardholderAuthenticationIndicator;
    }

    public void setCardholderAuthenticationIndicator(String cardholderAuthenticationIndicator) {
        this.cardholderAuthenticationIndicator = cardholderAuthenticationIndicator;
    }

    public String getCardholderAuthenticationValue() {
        return cardholderAuthenticationValue;
    }

    public void setCardholderAuthenticationValue(String cardholderAuthenticationValue) {
        this.cardholderAuthenticationValue = cardholderAuthenticationValue;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static String getCp_apiLoginID() {
        return cp_apiLoginID;
    }

    public static void setCp_apiLoginID(String cp_apiLoginID) {
        UnitTestData.cp_apiLoginID = cp_apiLoginID;
    }

    public static String getCp_transactionKey() {
        return cp_transactionKey;
    }

    public static void setCp_transactionKey(String cp_transactionKey) {
        UnitTestData.cp_transactionKey = cp_transactionKey;
    }

    public String getCreditCardExpMonth() {
        return creditCardExpMonth;
    }

    public void setCreditCardExpMonth(String creditCardExpMonth) {
        this.creditCardExpMonth = creditCardExpMonth;
    }

    public String getCreditCardExpYear() {
        return creditCardExpYear;
    }

    public void setCreditCardExpYear(String creditCardExpYear) {
        this.creditCardExpYear = creditCardExpYear;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCustomerDescription() {
        return customerDescription;
    }

    public void setCustomerDescription(String customerDescription) {
        this.customerDescription = customerDescription;
    }

    public String getCustomerDescription2() {
        return customerDescription2;
    }

    public void setCustomerDescription2(String customerDescription2) {
        this.customerDescription2 = customerDescription2;
    }

    public String getCustomerIP() {
        return customerIP;
    }

    public void setCustomerIP(String customerIP) {
        this.customerIP = customerIP;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getDutyAmount() {
        return dutyAmount;
    }

    public void setDutyAmount(BigDecimal dutyAmount) {
        this.dutyAmount = dutyAmount;
    }

    public String getDutyItemDescription() {
        return dutyItemDescription;
    }

    public void setDutyItemDescription(String dutyItemDescription) {
        this.dutyItemDescription = dutyItemDescription;
    }

    public String getDutyItemName() {
        return dutyItemName;
    }

    public void setDutyItemName(String dutyItemName) {
        this.dutyItemName = dutyItemName;
    }

    public ECheckType geteCheckType() {
        return eCheckType;
    }

    public void seteCheckType(ECheckType eCheckType) {
        this.eCheckType = eCheckType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFooterEmailReceipt() {
        return footerEmailReceipt;
    }

    public void setFooterEmailReceipt(String footerEmailReceipt) {
        this.footerEmailReceipt = footerEmailReceipt;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public String getFreightDescription() {
        return freightDescription;
    }

    public void setFreightDescription(String freightDescription) {
        this.freightDescription = freightDescription;
    }

    public String getFreightItemName() {
        return freightItemName;
    }

    public void setFreightItemName(String freightItemName) {
        this.freightItemName = freightItemName;
    }

    public String getHeaderEmailReceipt() {
        return headerEmailReceipt;
    }

    public void setHeaderEmailReceipt(String headerEmailReceipt) {
        this.headerEmailReceipt = headerEmailReceipt;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDescription2() {
        return itemDescription2;
    }

    public void setItemDescription2(String itemDescription2) {
        this.itemDescription2 = itemDescription2;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId2() {
        return itemId2;
    }

    public void setItemId2(String itemId2) {
        this.itemId2 = itemId2;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName2() {
        return itemName2;
    }

    public void setItemName2(String itemName2) {
        this.itemName2 = itemName2;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getItemPrice2() {
        return itemPrice2;
    }

    public void setItemPrice2(BigDecimal itemPrice2) {
        this.itemPrice2 = itemPrice2;
    }

    public BigDecimal getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(BigDecimal itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getItemQuantity2() {
        return itemQuantity2;
    }

    public void setItemQuantity2(BigDecimal itemQuantity2) {
        this.itemQuantity2 = itemQuantity2;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMagicSplitTenderZipCode() {
        return magicSplitTenderZipCode;
    }

    public void setMagicSplitTenderZipCode(String magicSplitTenderZipCode) {
        this.magicSplitTenderZipCode = magicSplitTenderZipCode;
    }

    public String getMaskedCreditCardNumber() {
        return maskedCreditCardNumber;
    }

    public void setMaskedCreditCardNumber(String maskedCreditCardNumber) {
        this.maskedCreditCardNumber = maskedCreditCardNumber;
    }

    public String getMdfKey() {
        return mdfKey;
    }

    public void setMdfKey(String mdfKey) {
        this.mdfKey = mdfKey;
    }

    public String getMdfValue() {
        return mdfValue;
    }

    public void setMdfValue(String mdfValue) {
        this.mdfValue = mdfValue;
    }

    public static Merchant getMerchant() {
        return merchant;
    }

    public static void setMerchant(Merchant merchant) {
        UnitTestData.merchant = merchant;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public static String getMerchantMD5Key() {
        return merchantMD5Key;
    }

    public static void setMerchantMD5Key(String merchantMD5Key) {
        UnitTestData.merchantMD5Key = merchantMD5Key;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getRawCreditCardNumber() {
        return rawCreditCardNumber;
    }

    public void setRawCreditCardNumber(String rawCreditCardNumber) {
        this.rawCreditCardNumber = rawCreditCardNumber;
    }

    public String getReportingBatchId() {
        return reportingBatchId;
    }

    public void setReportingBatchId(String reportingBatchId) {
        this.reportingBatchId = reportingBatchId;
    }

    public String getReportingTransId() {
        return reportingTransId;
    }

    public void setReportingTransId(String reportingTransId) {
        this.reportingTransId = reportingTransId;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxDescription() {
        return taxDescription;
    }

    public void setTaxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    public String getTaxItemName() {
        return taxItemName;
    }

    public void setTaxItemName(String taxItemName) {
        this.taxItemName = taxItemName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static String getTransactionKey() {
        return transactionKey;
    }

    public static void setTransactionKey(String transactionKey) {
        UnitTestData.transactionKey = transactionKey;
    }

    public String getZipPostalCode() {
        return zipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode) {
        this.zipPostalCode = zipPostalCode;
    }

        
}
