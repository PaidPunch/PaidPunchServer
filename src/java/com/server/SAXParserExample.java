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
    private AccessRequestElements acz;

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

        // get a factory
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
            acz = new AccessRequestElements();
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
            acz.setTxtype(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("username")) {
            acz.setUsername(parameter_val);

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
            acz.setMobilenumber(parameter_val);

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
            acz.setPunchCardID(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("orangeqrscannedvalue")) {
            acz.setOrangeqrscannedvalue(parameter_val);
            parameter_val = "";

        } else if (qName.equalsIgnoreCase("punch_card_downloadid")) {
            acz.setPunch_card_downloadid(parameter_val);

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
            acz.setBusiness_name(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("sessionid")) {
            acz.setSessionid(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("barcodevalue")) {
            acz.setBarcodevalue(parameter_val);
            parameter_val = "";
        } else if (qName.equalsIgnoreCase("bussinessid")) {
            acz.setBusinessid(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("FBid")) {
            acz.setFbid(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("isfreepunch")) {
            acz.setIsfreepunch(parameter_val);
            parameter_val = "";

        } else if (qName.equalsIgnoreCase("transactionid")) {
            acz.setTid(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("is_mystery_punch")) {
            acz.setIs_mystery_punch(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("cardno")) {
            acz.setCardno(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("cvv")) {
            acz.setCvv(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("exp_date")) {
            acz.setExp_date(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("amount")) {
            acz.setAmount(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("paymentid")) {
            acz.setPaymentid(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("cardtype")) {
            acz.setCardtype(parameter_val);

            parameter_val = "";
        } else if (qName.equalsIgnoreCase("expirestatus")) {
            acz.setExpirestatus(parameter_val);

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