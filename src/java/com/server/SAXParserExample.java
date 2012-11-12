package com.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserExample extends DefaultHandler {

    List aczList = new ArrayList();
    String reqesttype = "";
    // to maintain context
    private AccessRequest acz;

    private String tempVal = "";
    String parameter_val = "";

    public SAXParserExample() {
        // myEmpls = new ArrayList();
        // reqesttype="camp";
    }

    public SAXParserExample(String temp) {
        // myEmpls = new ArrayList();
        reqesttype = temp;
    }

    public void runExample() {
        // parseDocument();
        printData();
    }

    public void parseDocument(InputSource in) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            // get a new instance of parser
            SAXParser sp = spf.newSAXParser();
            // parse the file and also register this class for call backs
            sp.parse(in, this);

        } catch (SAXException se) {
            Constants.logger.error(se.toString());
        } catch (ParserConfigurationException pce) {
            Constants.logger.error(pce.toString());
        } catch (IOException ie) {
            Constants.logger.error(ie.toString());
        }
    }

    /**
     * Iterate through the list and print the contents
     */
    private void printData() {

        // System.out.println("No of Employees '" + myEmpls.size() + "'.");
        //
        // Iterator it = myEmpls.iterator();
        // while(it.hasNext()) {
        // System.out.println(it.next().toString());
        // }
    }

    // Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // reset
        // tempVal = "";
        if (qName.equalsIgnoreCase("paidpunch-req")) {
            // create a new instance of employee
            acz = new AccessRequest();
            // tempEmp.setType(attributes.getValue("type"));
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
        parameter_val = parameter_val + tempVal;

    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("paidpunch-req")) {
            aczList.add(acz);
        } else if (qName.equalsIgnoreCase("txtype")) {
            acz.setTxType(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("username")) {
            acz.setUserName(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("name")) {
            acz.setName(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("password")) {
            acz.setPassword(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("email")) {
            acz.setEmail(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("mobilenumber")) {
            acz.setMobileNumber(parameter_val);

            parameter_val = "";
        }
        else if (qName.equalsIgnoreCase("city")) {
            acz.setCity(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("scannedcode")) {
            acz.setVerificationCode(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("userId")) {
            acz.setUserId(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("punchcardid")) {
            acz.setPunchCardId(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("orangeqrscannedvalue")) {
            acz.setOrangQrScannedValue(parameter_val);
            parameter_val = "";

        } else if (qName.equalsIgnoreCase("punch_card_downloadid")) {
            acz.setPunchCardDownloadId(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("oldpassword")) {
            acz.setOldPassword(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("feedbackText")) {
            acz.setFeedbackText(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("pincode")) {
            acz.setPin(parameter_val);

            parameter_val = "";
        }
        else if (qName.equalsIgnoreCase("business_name")) {
            acz.setBusinessName(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("sessionid")) {
            acz.setSessionId(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("barcodevalue")) {
            acz.setBarcodeValue(parameter_val);
            parameter_val = "";
        } else if (qName.equalsIgnoreCase("bussinessid")) {
            acz.setBusinessId(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("FBid")) {
            acz.setFbid(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("isfreepunch")) {
            acz.isFreePunch(parameter_val);
            parameter_val = "";

        } else if (qName.equalsIgnoreCase("transactionid")) {
            acz.setTid(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("is_mystery_punch")) {
            acz.setIsMysteryPunch(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("cardno")) {
            acz.setCardNumber(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("cvv")) {
            acz.setCvv(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("exp_date")) {
            acz.setExpirationDate(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("amount")) {
            acz.setAmount(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("paymentid")) {
            acz.setPaymentId(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("cardtype")) {
            acz.setCardType(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("expirestatus")) {
            acz.setExpireStatus(parameter_val);

            parameter_val = "";
        }
        else if (qName.equalsIgnoreCase("offerQRscannedcode")) {

            parameter_val = "";
        }

    }

    // public static void main(String[] args){
    // SAXParserExample spe = new SAXParserExample();
    // spe.runExample();
    // }

    public List getData() {
        return aczList;
    }

}