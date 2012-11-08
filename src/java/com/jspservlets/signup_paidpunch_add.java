package com.jspservlets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Random;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.*;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Message.RecipientType;


/**
 * @author admin
 */
public class signup_paidpunch_add extends HttpServlet {

    ServletConfig config = null;
    ServletContext context;
    DBConnection db;
    Connection conn = null;
    PreparedStatement pstmt = null;
    Statement st = null;
    ResultSet rs = null;
    java.sql.Date sqlDate = null;
    HttpSession session = null;
    String path = "", businessname = "", businessdesc = "", email = "", password = "", securityCode = "",
            actualLogoPath = "";
    String businessaddress = "", city = "", state = "", pincode = "", countrycode = "", contactnumber = "",
            contactname = "", latitude = "", longitude = "";
    String punchCardName = "", noofpunches = "", valueofpunch = "", selling_price_punch = "", discount = "",
            expirydate = "", orangeCode = "", value_of_each_punch_disc = "", free_punch = "", punch_card_category = "";
    float disc_value_of_each_punch = 0, float_val_of_each_punch = 0, float_val_of_each_punchcard = 0;
    int restriction_time = 0, minpurval = 0, expirydays = 0;

    final String SMTP_HOST_NAME = "smtp.gmail.com";
    // final String SMTP_AUTH_USER = "mobimedia.mm@gmail.com";
    // final String SMTP_AUTH_PWD = "mobimedia";

    // final String SMTP_HOST_NAME = "mail.paidpunch.com";
    final String SMTP_AUTH_USER = "noreply@paidpunch.com";
    // final String SMTP_AUTH_PWD = "nor3ply";
    final String SMTP_AUTH_PWD = "P@idpunch";

    String emailMsgTxt = "";
    final String emailSubjectTxt = "PaidPunch Verification Email";
    // final String emailFromAddress = "mobimedia.mm@gmail.com";
    final String emailFromAddress = "noreply@paidpunch.com";
    // final String SMTP_PORT = "25";
    final String SMTP_PORT = "465";
    final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    String recipient_email_id = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            session = request.getSession(false);
        } catch (Exception e) {

        }

        try {
            config = getServletConfig();

            context = config.getServletContext();
        } catch (Exception e) {

        }
        com.server.Constants.loadJDBCConstants(context);
        PrintWriter out = response.getWriter();
        try {
            securityCode = session.getAttribute("secretcode").toString();
            email = session.getAttribute("email").toString();
            password = session.getAttribute("password").toString();
            businessname = session.getAttribute("businessname").toString();
            businessdesc = session.getAttribute("businessdesc").toString();
            path = session.getAttribute("filepath").toString();
            businessaddress = session.getAttribute("businessaddress").toString();
            city = session.getAttribute("city").toString();
            state = session.getAttribute("state").toString();
            pincode = session.getAttribute("pincode").toString();
            countrycode = session.getAttribute("countrycode").toString();
            contactnumber = session.getAttribute("contactnumber").toString();
            contactname = session.getAttribute("contactname").toString();
            actualLogoPath = session.getAttribute("actuallogopath").toString();
            latitude = session.getAttribute("latitude").toString();
            longitude = session.getAttribute("longitude").toString();
            punchCardName = session.getAttribute("punchcardname").toString();
            noofpunches = session.getAttribute("noofpunches").toString();
            // valueofpunch = request.getParameter("punchvalue");
            // selling_price_punch = request.getParameter("sppunchcard");
            valueofpunch = session.getAttribute("float_val_of_punch").toString();
            selling_price_punch = session.getAttribute("float_val_of_punchcard").toString();
            discount = session.getAttribute("discount").toString();
            expirydate = session.getAttribute("expirydate").toString();
            value_of_each_punch_disc = session.getAttribute("disc_value_each_punch").toString();
            free_punch = session.getAttribute("freepunch").toString();
            String restrict = session.getAttribute("restrictiontime").toString();
            punch_card_category = session.getAttribute("card_category").toString();
            String minval = session.getAttribute("minpurchaseval").toString();
            String expdays = session.getAttribute("expireddays").toString();

            restriction_time = Integer.parseInt(restrict);
            minpurval = Integer.parseInt(minval);
            expirydays = Integer.parseInt(expdays);
            System.out.println(businessname + "\n" + businessdesc + "\n" + email + "\n" + password + "\nPath :" + path
                    + "\n" + businessaddress + "\n" + city + "\n" + countrycode + "\n" + latitude + "\n" + longitude);
            com.server.Constants.logger.info(businessname + "\n" + businessdesc + "\n" + email + "\n" + password
                    + "\nPath :" + path + "\n" + businessaddress + "\n" + city + "\n" + countrycode + "\n" + latitude
                    + "\n" + longitude);
            disc_value_of_each_punch = (Float.valueOf(value_of_each_punch_disc)).floatValue();
            // disc_value_of_each_punch = Float.parseFloat(value_of_each_punch_disc);
            float_val_of_each_punch = (Float.valueOf(valueofpunch)).floatValue();
            // float_val_of_each_punch = Float.parseFloat(valueofpunch);
            float_val_of_each_punchcard = (Float.valueOf(selling_price_punch)).floatValue();
            // float_val_of_each_punchcard = Float.parseFloat(selling_price_punch);

            System.out.println("Value of Punches :\n");
            System.out.println(punchCardName + "\n" + noofpunches + "\n" + float_val_of_each_punch + "\n"
                    + float_val_of_each_punchcard + "\n" + discount + "\n" + expirydate);
            com.server.Constants.logger.info(punchCardName + "\n" + noofpunches + "\n" + float_val_of_each_punch + "\n"
                    + float_val_of_each_punchcard + "\n" + discount + "\n" + expirydate);
        } catch (Exception e) {
            out.println("Error occured while adding the Card. Please try again");
        }
        try {
            int no_of_mystery_punches = Integer.parseInt(request.getParameter("counterval"));

            no_of_mystery_punches = no_of_mystery_punches - 1;

            String mystery_punch_values[] = new String[no_of_mystery_punches];
            int mystery_punch_probability[] = new int[no_of_mystery_punches];
            String isPaidPunchMystery[] = new String[no_of_mystery_punches];

            for (int i = 0; i < no_of_mystery_punches; i++) {
                mystery_punch_values[i] = request.getParameter("mysteryvalue" + (i + 1));
                mystery_punch_probability[i] = Integer.parseInt(request.getParameter("probability" + (i + 1)));
                isPaidPunchMystery[i] = request.getParameter("ispaidpunchmystery" + (i + 1));
                System.out.println("MysteryValue" + i + " : " + mystery_punch_values[i]);
                System.out.println("Probability Value" + i + " : " + mystery_punch_probability[i]);
                System.out.println("Is PaidPunch Mystery " + i + " : " + isPaidPunchMystery[i]);
            }

            Date time1 = new Date();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(time1);
            // int hour= cal1.HOUR;
            cal1.add(Calendar.YEAR, 1);
            cal1.add(Calendar.DATE, -1);
            Date date = cal1.getTime();
            System.out.println("" + time1);
            sqlDate = new java.sql.Date(date.getTime());
            System.out.println("<br>" + sqlDate);

            // sqlDate = new java.sql.Date(new java.util.Date().getTime());

            /*
             * long aStart=000000000; long aEnd = 999999999; Random generator = new Random();
             * generator.setSeed(System.currentTimeMillis()); if ( aStart > aEnd ) { throw new
             * IllegalArgumentException("Start cannot exceed End."); } //get the range, casting to long to avoid
             * overflow problems long range = (long)aEnd - (long)aStart + 1; // compute a fraction of the range, 0 <=
             * frac < range long fraction = (long)(range * generator.nextDouble()); long randomNumber = (long)(fraction
             * + aStart); System.out.println("Generated : " + randomNumber);
             */

            Date time2 = new Date();
            DateFormat formatter;
            formatter = new SimpleDateFormat("yyMMddHHmmssSSSSS");
            String strtimeDate = formatter.format(time2);
            orangeCode = "" + strtimeDate;

            // disc_value_of_each_punch = (selling_price_punch/noofpunches);

            int businessuserid = insertIntoBusiness_Users();
            insertBusinessAddress(businessuserid);
            int punchcardid = insertPaidPunchDetails(businessuserid);
            insertmysteryPunches(punchcardid, mystery_punch_values, mystery_punch_probability, no_of_mystery_punches,
                    isPaidPunchMystery);
            // ************************** Changed on Tony's Request ****************************************/
            // sendEmail(businessuserid);
            response.sendRedirect("emailnotification.jsp");
        } catch (Exception e) {
            out.println("Error occured while adding the Card. Please try again");
        }

        try {
            /*
             * TODO output your page here out.println("<html>"); out.println("<head>");
             * out.println("<title>Servlet signup_paidpunch_add</title>"); out.println("</head>");
             * out.println("<body>"); out.println("<h1>Servlet signup_paidpunch_add at " + request.getContextPath () +
             * "</h1>"); out.println("</body>"); out.println("</html>");
             */
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed"
    // desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
            throws ServletException, IOException {
        // processRequest(request, response);

        try {
            config = getServletConfig();
            context = config.getServletContext();
        } catch (Exception e) {

        }
        com.server.Constants.loadJDBCConstants(context);
        String code = "00";
        String punch_card_name = "";
        try {
            punch_card_name = request.getParameter("punchcardname");
            // business_name = request.getParameter("businessname");
        } catch (Exception e) {

        }

        PrintWriter out = response.getWriter();
        if (request.getParameter(punch_card_name) != null) {// &&request.getParameter("businessname") != null) {

            code = getPunchCardName(punch_card_name);// ,business_name);
        }

        /*
         * The value to be returned for code is between the following 3: "00" : Email id and Business not registered
         * before "01" : Email id has already been registered "03" : Business Name has already been registered by that
         * name
         */
        out.println(code);
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
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Returns a short description of the servlet.
     * 
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public int insertIntoBusiness_Users() throws ServletException, SQLException, IOException {
        FileInputStream fs = null;
        try {
            File file = new File(path);// "C:\\Documents and Settings\\admin\\My Documents\\Paid Punch\\uploaded files\\"
                                       // + "images\\"+finalimage);

            fs = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(signup_paidpunch_add.class.getName()).log(Level.SEVERE, null, ex);
        }

        int insertedUserId = 0;
        try {
            db = new DBConnection();
            conn = db.con;
            st = db.stmt;
            String INSERT_RECORD = "Insert into business_users(business_name, email_id,password,secretcode,buss_desc,isemailverified,bussiness_logo,role,contactno,contactname,logo_path, is_free_punch) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // Constants.logger.info("Insert Query is "+INSERT_RECORD);
            com.server.Constants.logger.info("Insert Query is " + INSERT_RECORD);
            pstmt = (PreparedStatement) conn.prepareStatement(INSERT_RECORD);
            pstmt.setString(1, businessname);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, securityCode);
            pstmt.setString(5, businessdesc);
            // ************************** Changed on Tony's Request ****************************************/
            // pstmt.setString(6, "N");
            pstmt.setString(6, "Y");
            pstmt.setBinaryStream(7, fs, fs.available());
            pstmt.setString(8, "user");
            pstmt.setString(9, contactnumber);
            pstmt.setString(10, contactname);
            pstmt.setString(11, actualLogoPath);
            pstmt.setString(12, free_punch);
            pstmt.executeUpdate();

            rs = st.executeQuery("Select LAST_INSERT_ID() from business_users");

            if (rs.next()) {
                insertedUserId = Integer.parseInt(rs.getObject(1).toString());
                // Constants.logger.info("The inserted value id was "+cmv.getAppDistHitId());
            }

            String securityCodequery = "Update secretcode set code_used = 'Y' where code_value ='" + securityCode + "'";
            com.server.Constants.logger.info("The query is " + securityCodequery);
            System.out.println("The query is " + securityCodequery);
            st.executeUpdate(securityCodequery);

        } catch (SQLException sqle) {
            // Constants.logger.error("Error in Sql in AppDistributor Hits"+sqle.getMessage());
            throw new ServletException("SQL Exception.", sqle);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                    // Constants.logger.info("Closing Prepared Statement ");
                    pstmt = null;
                }

                if (rs != null) {
                    rs.close();
                    // Constants.logger.info("Closing rs Statement ");
                    rs = null;
                }
                db.closeConnection();
            } catch (SQLException e) {
                // Constants.logger.error("Error in Sql in App Distributor Hits"+e.getMessage());
            }
        }
        return insertedUserId;
    }

    public void insertBusinessAddress(int businessuserid) throws ServletException, SQLException {

        try {
            db = new DBConnection();
            conn = db.con;
            st = db.stmt;
            String INSERT_RECORD = "Insert into bussiness_address(business_id, address_line1, city, state, country, zipcode, latitude, longitude) values(?, ?, ?, ?, ?, ?, ?, ?)";
            // Constants.logger.info("Insert Query is "+INSERT_RECORD);
            com.server.Constants.logger.info("The query is " + INSERT_RECORD);
            pstmt = (PreparedStatement) conn.prepareStatement(INSERT_RECORD);
            pstmt.setInt(1, businessuserid);
            pstmt.setString(2, businessaddress);
            pstmt.setString(3, city);
            pstmt.setString(4, state);
            pstmt.setString(5, countrycode);
            pstmt.setString(6, pincode);
            pstmt.setString(7, latitude);
            pstmt.setString(8, longitude);
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            // Constants.logger.error("Error in Sql in AppDistributor Hits"+sqle.getMessage());
            throw new ServletException("SQL Exception.", sqle);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                    // Constants.logger.info("Closing Prepared Statement ");
                    pstmt = null;
                }

                if (rs != null) {
                    rs.close();
                    // Constants.logger.info("Closing rs Statement ");
                    rs = null;
                }
                db.closeConnection();
            } catch (SQLException e) {
                // Constants.logger.error("Error in Sql in App Distributor Hits"+e.getMessage());
            }
        }

    }

    public int insertPaidPunchDetails(int businessuserid) throws ServletException, SQLException {

        int insertedUserId = 0;
        try {
            db = new DBConnection();
            conn = db.con;
            st = db.stmt;
            String INSERT_RECORD = "Insert into punch_card(punch_card_name, no_of_punches_per_card, value_of_each_punch,"
                    +
                    " selling_price_of_punch_card, expiry_date, effective_discount, business_userid, qrcode, disc_value_of_each_punch, restriction_time, is_mystery_punch, punchcard_category, expirydays, minimumvalue) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // Constants.logger.info("Insert Query is "+INSERT_RECORD);
            com.server.Constants.logger.info("The query is " + INSERT_RECORD);
            pstmt = (PreparedStatement) conn.prepareStatement(INSERT_RECORD);
            pstmt.setString(1, punchCardName);
            pstmt.setString(2, noofpunches);
            pstmt.setFloat(3, float_val_of_each_punch);
            pstmt.setFloat(4, float_val_of_each_punchcard);
            pstmt.setDate(5, sqlDate);
            pstmt.setString(6, discount);
            pstmt.setInt(7, businessuserid);
            pstmt.setString(8, punchCardName);
            pstmt.setFloat(9, disc_value_of_each_punch);
            pstmt.setInt(10, restriction_time);
            pstmt.setString(11, "true");
            pstmt.setString(12, punch_card_category);
            pstmt.setInt(13, expirydays);
            pstmt.setInt(14, minpurval);
            pstmt.executeUpdate();

            rs = st.executeQuery("Select LAST_INSERT_ID() from punch_card");

            if (rs.next()) {
                insertedUserId = Integer.parseInt(rs.getObject(1).toString());
                // Constants.logger.info("The inserted value id was "+cmv.getAppDistHitId());
            }

        } catch (SQLException sqle) {
            // Constants.logger.error("Error in Sql in AppDistributor Hits"+sqle.getMessage());
            throw new ServletException("SQL Exception.", sqle);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                    // Constants.logger.info("Closing Prepared Statement ");
                    pstmt = null;
                }

                if (rs != null) {
                    rs.close();
                    // Constants.logger.info("Closing rs Statement ");
                    rs = null;
                }
                db.closeConnection();
            } catch (SQLException e) {
                // Constants.logger.error("Error in Sql in App Distributor Hits"+e.getMessage());
            }
        }
        return insertedUserId;

    }

    public void insertmysteryPunches(int punchcardid, String mysteryValues[], int mysteryProb[], int no_of_mysteries,
            String isMysteryPunch[]) throws ServletException, SQLException {

        try {
            db = new DBConnection();
            conn = db.con;
            st = db.stmt;
            for (int i = 0; i < no_of_mysteries; i++) {
                String INSERT_RECORD = "Insert into mystery_punch(punch_card_id, value_of_myst_punch,probability, paidpunchmystery, mysterypunchvalid) values( ?, ?, ?, ?, ?)";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                com.server.Constants.logger.info("The query is " + INSERT_RECORD);
                pstmt = (PreparedStatement) conn.prepareStatement(INSERT_RECORD);
                pstmt.setInt(1, punchcardid);
                pstmt.setString(2, mysteryValues[i]);
                pstmt.setInt(3, mysteryProb[i]);
                pstmt.setString(4, isMysteryPunch[i]);
                pstmt.setString(5, "Y");
                pstmt.executeUpdate();

            }

        } catch (SQLException sqle) {
            // Constants.logger.error("Error in Sql in AppDistributor Hits"+sqle.getMessage());
            throw new ServletException("SQL Exception.", sqle);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                    // Constants.logger.info("Closing Prepared Statement ");
                    pstmt = null;
                }

                if (rs != null) {
                    rs.close();
                    // Constants.logger.info("Closing rs Statement ");
                    rs = null;
                }
                db.closeConnection();
            } catch (SQLException e) {
                // Constants.logger.error("Error in Sql in App Distributor Hits"+e.getMessage());
            }
        }

    }

    public void sendEmail(int businessuserid) {
        try {

            try {
                recipient_email_id = email;
            } catch (Exception e) {

            }
            com.server.Constants.logger.info("Recipient Email : " + recipient_email_id);
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            boolean debug = false;

            // String password = getPassword(recipient_email_id);

            // Set the host smtp address
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.port", SMTP_PORT);
            // props.setProperty("mail.user", SMTP_AUTH_USER);
            // props.setProperty("mail.password", SMTP_AUTH_PWD);
            props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.put("mail.smtp.socketFactory.fallback", "false");

            // url = new URLName(protocol,SMTP_HOST_NAME,-1, mbox,SMTP_AUTH_USER,SMTP_AUTH_USER);
            // Authenticator auth = getPasswordAuthentication();SMTP_AUTH_USER,SMTP_AUTH_PWD);
            Authenticator auth = new SMTPAuthenticator();
            Session session1 = Session.getDefaultInstance(props, auth);
            // Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
            session1.setDebug(debug);

            // create a message
            Message msg = new MimeMessage(session1);

            // set the from and to address
            InternetAddress addressFrom = new InternetAddress(emailFromAddress);
            msg.setFrom(addressFrom);

            InternetAddress addressTo = new InternetAddress(recipient_email_id);

            msg.setRecipient(Message.RecipientType.TO, addressTo);

            // Setting the Subject and Content Type
            com.server.Constants.logger.info("Setting Subject and Receipient");
            msg.setSubject(emailSubjectTxt);
            // emailMsgTxt =
            // "Congratulations for Signing Up with Paid Punch!!! \nPlease click on the Following link to complete your registration : \n\n";
            emailMsgTxt = "You\'re almost done! Please click the following link to verify your email and complete your PaidPunch registration:  \n\n";
            emailMsgTxt += com.server.Constants.IP_URL + "/" + com.server.Constants.applicationName
                    + "/verifybususer?uid=" + businessuserid + "&email=" + email + "\n\n";
            msg.setContent(emailMsgTxt, "text/plain");
            Transport.send(msg);

            // emailMsgTxt =
            // "You\'re almost done! Please click the following link to verify your email and complete your PaidPunch registration:  \n\n";
            // emailMsgTxt +=
            // com.server.Constants.IP_URL+"/paid_punch/verifybususer?uid="+businessuserid+"&email="+email+"\n\n";
            // emailMsgTxt +=
            // com.server.Constants.IP_URL+"/"+com.server.Constants.applicationName+"/verifybususer?uid="+businessuserid+"&email="+email+"\n\n";

            // messageSending(emailSubjectTxt, recipient_email_id , emailMsgTxt);

            /*
             * TODO output your page here out.println("<html>"); out.println("<head>");
             * out.println("<title>Servlet forgot_pass_mail_send</title>"); out.println("</head>");
             * out.println("<body>"); out.println("<h1>Servlet forgot_pass_mail_send at " + request.getContextPath () +
             * "</h1>"); out.println("</body>"); out.println("</html>");
             */
        } catch (Exception e) {
            com.server.Constants.logger.info("Error in Sending Email : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendEmail_For_app_user(String appid, String useremail) {
        try {
            String emailContentforapp = "";
            try {
                recipient_email_id = useremail;
            } catch (Exception e) {
            }

            com.server.Constants.logger.info("Recipient Email : " + recipient_email_id);
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            boolean debug = false;

            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.put("mail.smtp.socketFactory.fallback", "false");
            // props.put("mail.smtp.starttls.enable", "true");

            Authenticator auth = new SMTPAuthenticator();
            Session session1 = Session.getDefaultInstance(props, auth);
            session1.setDebug(debug);
            Message msg = new MimeMessage(session1);
            InternetAddress addressFrom = new InternetAddress(emailFromAddress);
            msg.setFrom(addressFrom);
            InternetAddress addressTo = new InternetAddress(recipient_email_id);
            msg.setRecipient(Message.RecipientType.TO, addressTo);
            msg.setSubject(emailSubjectTxt);
            emailContentforapp = "Thank you for signing up with PaidPunch!<br> You\'re just a couple steps away from saving lots of money at great businesses throughout your city!<br>Click the following link to confirm your email address:<br> ";
            // emailContentforapp +=
            // com.server.Constants.IP_URL+"/paid_punch/verifyappuser?userid="+appid+"&email="+useremail+"<br>";
            emailContentforapp += com.server.Constants.IP_URL + "/" + com.server.Constants.applicationName
                    + "/verifyappuser?userid=" + appid + "&email=" + useremail + "<br>";
            emailContentforapp += "<br>Enjoy!<br>"
                    + "The <a href=http://twitter.com/#!/paidpunch>@PaidPunch</a> Team";
            msg.setContent(emailContentforapp, "text/html");
            Transport.send(msg);

            // appUsermessageSending(emailSubjectTxt,recipient_email_id , emailContentforapp);

        } catch (Exception e) {
            e.printStackTrace();
            com.server.Constants.logger.info(e.getStackTrace());
        }
    }

    public void sendEmail_For_forgotPassword(String Password, String useremail) {
        try {
            String emailContentforForgotPassword = "";
            try {
                recipient_email_id = useremail;
            } catch (Exception e) {
            }
            com.server.Constants.logger.info("Recipient Email : " + recipient_email_id);
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            boolean debug = false;

            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.port", SMTP_PORT);
            // props.setProperty("mail.user", SMTP_AUTH_USER);
            // props.setProperty("mail.password", SMTP_AUTH_PWD);
            props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.put("mail.smtp.socketFactory.fallback", "false");
            Authenticator auth = new SMTPAuthenticator();
            Session session1 = Session.getDefaultInstance(props, auth);
            session1.setDebug(debug);
            Message msg = new MimeMessage(session1);
            InternetAddress addressFrom = new InternetAddress(emailFromAddress);
            msg.setFrom(addressFrom);
            InternetAddress addressTo = new InternetAddress(recipient_email_id);
            msg.setRecipient(Message.RecipientType.TO, addressTo);
            msg.setSubject(emailSubjectTxt);
            emailContentforForgotPassword = "Dear  "
                    + useremail
                    + ",<br><br>We have processed your request for password retrieval. Your account details are mentioned below:"
                    + "<br><br>UserName:  " + useremail + "<br>Password:  " + Password
                    + "<br><br>Please keep them safe.<br><br>Regards,<br>PaidPunch Team ";
            // emailMsgTxt +=
            // com.server.Constants.IP_URL+"/paid_punch/verifyappuser?userid="+appid+"&email="+useremail+"<br>";
            msg.setContent(emailContentforForgotPassword, "text/html");
            Transport.send(msg);

            // appUsermessageSending("PaidPunch Credentials",recipient_email_id , emailContentforForgotPassword);
        } catch (Exception e) {
            com.server.Constants.logger.info("" + e.getStackTrace());
        }
    }

    public void sendConfirmationEmail(String passed_email, String userName) {
        try {

            try {
                recipient_email_id = passed_email;
            } catch (Exception e) {

            }
            com.server.Constants.logger.info("Recipient Email : " + recipient_email_id);
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            boolean debug = false;

            // String password = getPassword(recipient_email_id);

            // Set the host smtp address
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.port", SMTP_PORT);
            // props.setProperty("mail.user", SMTP_AUTH_USER);
            // props.setProperty("mail.password", SMTP_AUTH_PWD);
            props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.put("mail.smtp.socketFactory.fallback", "false");

            // url = new URLName(protocol,SMTP_HOST_NAME,-1, mbox,SMTP_AUTH_USER,SMTP_AUTH_USER);
            // Authenticator auth = getPasswordAuthentication();SMTP_AUTH_USER,SMTP_AUTH_PWD);
            Authenticator auth = new SMTPAuthenticator();
            Session session1 = Session.getDefaultInstance(props, auth);
            // Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
            session1.setDebug(debug);

            // create a message
            Message msg = new MimeMessage(session1);

            // set the from and to address
            InternetAddress addressFrom = new InternetAddress(emailFromAddress);
            msg.setFrom(addressFrom);

            InternetAddress addressTo = new InternetAddress(recipient_email_id);

            msg.setRecipient(Message.RecipientType.TO, addressTo);

            // Setting the Subject and Content Type
            com.server.Constants.logger.info("Setting Subject and Receipient");
            String email_sub_txt = "Welcome to PaidPunch";
            msg.setSubject(email_sub_txt);
            // emailMsgTxt =
            // "Congratulations for Signing Up with Paid Punch!!! \nPlease click on the Following link to complete your registration : \n\n";
            emailMsgTxt = "<html xmlns='http://www.w3.org/1999/xhtml'>"
                    +
                    "<head>"
                    +
                    ""
                    +
                    "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />"
                    +
                    "<title>Welcome to PaidPunch</title>"
                    +
                    "</head>"
                    +
                    ""
                    +
                    "<body>"
                    +
                    "<table width='98%' border='0' cellspacing='0' cellpadding='0'>"
                    +
                    "  <tr>"
                    +
                    "    <td bgcolor='#FFFFFF'><table border='0' cellpadding='0' cellspacing='0' height='100%' width='100%' id='backgroundTable' style='margin: 0;padding: 0;background-color: #FAFAFA;height: 100% !important;width: 100% !important;'>"
                    +
                    ""
                    +
                    "<tr>"
                    +
                    " 	<td align='center' valign='top' style='border-collapse: collapse;'>"
                    +
                    "                    	<table border='0' cellpadding='0' cellspacing='0' width='600'>"
                    +
                    "            	<tr>"
                    +
                    "                          	<td align='center' valign='top' width='50' id='colorBar' style='padding-top: 15px;padding-right: 5px;padding-bottom: 40px;padding-left: 5px;border-collapse: collapse;background-color: #F47B2B;'>"
                    +
                    "                              	<img src='http://www.paidpunch.com/images/smile.png' height='40' width='40' style='border: 0;height: auto;line-height: 100%;outline: none;text-decoration: none;'>"
                    +
                    "                                </td>"
                    +
                    "                                <td valign='top' width='535' id='templateContainer' style='padding-left: 20px;border-collapse: collapse;'>"
                    +
                    "                                    <!-- // Begin Template Preheader \\ -->"
                    +
                    "                                    <table border='0' cellpadding='0' cellspacing='0' width='100%' id='templatePreheader'>"
                    +
                    "                                        <tr>"
                    +
                    "                                            <td valign='top' class='preheaderContent' style='border-collapse: collapse;'>"
                    +
                    "                                                <!-- // Begin Preheader Content \\ -->"
                    +
                    "                                                <table border='0' cellpadding='0' cellspacing='0' width='100%'>"
                    +
                    "                                                    <tr>"
                    +
                    "                      </tr>"
                    +
                    "                                              </table>"
                    +
                    "                                                <!-- // Begin Preheader Content \\ -->"
                    +
                    "                                            </td>"
                    +
                    "                                        </tr>"
                    +
                    "                                    </table>"
                    +
                    "                                    <!-- // End Template Preheader \\ -->"
                    +
                    "                                    <!-- // Begin Template Header \\ -->"
                    +
                    "                                    <table border='0' cellpadding='0' cellspacing='0' width='100%' id='templateHeader'>"
                    +
                    "                                    	<tr>"
                    +
                    "                                        	<td valign='top' style='padding-top: 40px;padding-bottom: 40px;border-collapse: collapse;'>"
                    +
                    "                                                <!-- // Begin Header Content \\ -->"
                    +
                    "                                                <table border='0' cellpadding='0' cellspacing='0' width='100%'>"
                    +
                    "                                                    <tr>"
                    +
                    "                                                       <td colspan='2' valign='top' class='headerContent' style='border-collapse: collapse;color: #202020;font-family: Helvetica;font-size: 34px;font-weight: bold;line-height: 100%;padding: 0;text-align: left;vertical-align: bottom;'>"
                    +
                    "                                                            <img src='https://www.paidpunch.com/images/transhoriz_small.png' alt='' border='0' style='margin: 0;padding: 0;max-width: 500px;border: 0;height: auto;line-height: 100%;outline: none;text-decoration: none;' id='headerImage campaign-icon'></td>"
                    +
                    "                                                            <td width='35%' colspan='2' style='color:#F47B2B; text-align:right; font-size:30px; font-family: Helvetica, Geneva, Verdana; padding-top:6px;'>Welcome!</td>"
                    +
                    "                                                            </tr>"
                    +
                    "                                               </table>"
                    +
                    "                                                <!-- // End Header Content \\ -->"
                    +
                    "                                            </td>"
                    +
                    "                                        </tr>"
                    +
                    "                                    </table>"
                    +
                    "                                    <!-- // End Template Header \\ -->"
                    +
                    "                                    <!-- // Begin Template Body \\ -->"
                    +
                    "                                    <table border='0' cellpadding='0' cellspacing='0' width='100%'>"
                    +
                    "                                       <tr>"
                    +
                    "                                            <td valign='top' width='100%' id='templateBody' style='border-collapse: collapse;'>"
                    +
                    "                                               <table border='0' cellpadding='0' cellspacing='0' width='100%'>"
                    +
                    "                                                    <tr>"
                    +
                    "                                                        <td colspan='2' valign='top' class='bodyContent' style='padding-top: 20px;border-collapse: collapse;'>"
                    +
                    "                                                          <div style='color: #FFF; font-family: Helvetica; font-size: 13px; line-height: 150%; text-align: left;'><h1 style='color: #363636;display: block;font-family: Helvetica;font-size: 32px;font-weight: normal;letter-spacing: -1px;line-height: 100%;margin-top: 0;margin-right: 0;margin-bottom: 0px;margin-left: 0;text-align: left;'>"
                    +
                    "	<b id='internal-source-marker_0.7230599608737975' style='color: rgb(0, 0, 0); font-family: Arial; line-height: normal; text-align: -webkit-auto; -webkit-text-size-adjust: auto; font-size: medium; '><span style='font-size: 15px; font-family: Arial; background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '>"
                    + userName
                    + ",</span></b></h1>"
                    +
                    "                                                          <p><span id='internal-source-marker_0.7230599608737975' style='color: rgb(0, 0, 0); font-family: Times; line-height: normal; text-align: -webkit-auto; -webkit-text-size-adjust: auto; font-size: medium; '><span style='font-size: 15px; font-family: Arial; background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '>Congratulations on successfully signing up for PaidPunch!</span><br>"
                    +
                    "  <br>"
                    +
                    "  <span style='font-size: 15px; font-family: Arial; background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '>On behalf of </span><a href='https://www.paidpunch.com/about-us.html' style='font-weight: bold;color: #26ABE2;text-decoration: none;'><span style='font-size: 15px; font-family: Arial; color: rgb(17, 85, 204); background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '>our entire team</span></a><span style='font-size: 15px; font-family: Arial; background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '> I would like to personally welcome you to PaidPunch. We are so excited that you have decided to take part in the movement that is sweeping across San Diego! </span><br>"
                    +
                    "  <br>"
                    +
                    "  <span style='font-size: 15px; font-family: Arial; background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '>Everyday, more and more tech savvy individuals like yourself are discovering the amazing benefits that PaidPunch has to offer. Using PaidPunch you will immediately elevate yourself to VIP status at the businesses you visit. </span><br>"
                    +
                    "  <br>"
                    +
                    " <span style='font-size: 15px; font-family: Arial; background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '>PaidPunch is guaranteed to save you money every visit (no collecting points or stamps in hopes of getting a discount sometime in the future). Plus, on top of saving money every visit you will also get a free prize which can be anything from a free meal to a round of drinks depending on the business you are at.</span><br>"
                    +
                    "  <br>"
                    +
                    "  <span style='font-size: 15px; font-family: Arial; background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '>Your experience matters and I want to hear from you. Let&rsquo;s stay in touch (<a href='http://www.facebook.com/pages/PaidPunch/223162497771376' style='font-weight: bold;color: #26ABE2;text-decoration: none;'><span style='font-size: 15px; font-family: Arial; color: rgb(17, 85, 204); background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '>Facebook</span></a>, <a href='http://www.twitter.com/#!/paidpunch' style='font-weight: bold;color: #26ABE2;text-decoration: none;'><span style='font-size: 15px; font-family: Arial; color: rgb(17, 85, 204); background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '>Twitter</span></a>, <a href='http://blog.paidpunch.com' style='font-weight: bold;color: #26ABE2;text-decoration: none;'><span style='font-size: 15px; font-family: Arial; color: rgb(17, 85, 204); background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '> blog</span></a>, <a href='mailto:tony@paidpunch.com' style='font-weight: bold;color: #26ABE2;text-decoration: none;'><span style='font-size: 15px; font-family: Arial; color: rgb(17, 85, 204); background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; '>email</span></a>).</span><br>"
                    +
                    "  </span></p>"
                    +
                    "                                                            <p><span style='color: rgb(0, 0, 0); font-family: Times; line-height: normal; text-align: -webkit-auto; -webkit-text-size-adjust: auto; font-size: medium; '><br>"
                    +
                    "                                                            <span style='font-size: 15px; font-family: Arial; background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap;padding-left:31px;'>Tony Mandarano</span><br>"
                    +
                    "                                                            <img src='https://www.paidpunch.com/images/tonysig.png' width='250'/><br>"
                    +
                    "                                                            <span style='font-size: 15px; font-family: Arial; background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; padding-left:31px; '>Co-Founder</span><br>"
                    +
                    "                                                            <span style='font-size: 15px; font-family: Arial; background-color: transparent; font-weight: normal; vertical-align: baseline; white-space: pre-wrap; padding-left:31px; '>PaidPunch</span></span> </p>"
                    +
                    "<p>&nbsp;</p>"
                    +
                    "                                                          </div>"
                    +
                    "                                                        </td>"
                    +
                    "                                                    </tr>"
                    +
                    "                                                </table>"
                    +
                    "                                            </td>"
                    +
                    "                                        </tr>"
                    +
                    "                                    </table>"
                    +
                    "                                    <!-- // End Template Body \\ -->"
                    +
                    "                                    <!-- // Begin Template Footer \\ -->"
                    +
                    "                                    <table border='0' cellpadding='0' cellspacing='0' width='100%'>"
                    +
                    "                                   	<tr>"
                    +
                    "                                        	<td valign='top' style='border-collapse: collapse;'>"
                    +
                    "               <!-- // Begin Footer Content \\ -->"
                    +
                    "                                                <table border='0' cellpadding='0' cellspacing='0' width='100%' id='templateFooter' style='border-top: 1px dotted #202020;'>"
                    +
                    "                                                    <tr>"
                    +
                    "                                                        <td valign='top' width='320' class='footerContent' style='padding-top: 40px;padding-right: 20px;padding-bottom: 20px;padding-left: 0;border-collapse: collapse;'>"
                    +
                    "                                                            <div style='color: #202020;font-family: Helvetica;font-size: 11px;line-height: 125%;text-align: left;'>"
                    +
                    "                                                                <em>Copyright &copy; 2012 PaidPunch, LLC, All rights reserved.</em>"
                    +
                    "                                                                <br>"
                    +
                    "</td>                                                        <td valign='top' width='200' id='monkeyRewards' class='footerContent' style='padding-top: 40px;padding-right: 0;padding-bottom: 20px;padding-left: 0;border-collapse: collapse;'>"
                    +
                    "                                                          <div style='color: #202020;font-family: Helvetica;font-size: 11px;line-height: 125%;text-align: left;'>Stay happy. Save money. Win prizes.</div>"
                    +
                    "                                                       </td>" +
                    "                                                    </tr>" +
                    "                                                    <tr>" +
                    "                       </tr>" +
                    "</table>" +
                    "                   <!-- // End Footer Content \\ -->" +
                    "                   </td>" +
                    "                </tr>" +
                    "               </table>" +
                    "          <!-- // End Template Footer \\ -->" +
                    "	<br>" +
                    "      </td>" +
                    " </tr>" +
                    "    </table>" +
                    "  </td>" +
                    "  </tr>" +
                    " </table>" +
                    "  </center>" +
                    "</td>" +
                    "  </tr>" +
                    "</table>" +
                    "</body>" +
                    "</html>";

            msg.setContent(emailMsgTxt, "text/html");
            Transport.send(msg);

            // emailMsgTxt =
            // "You\'re almost done! Please click the following link to verify your email and complete your PaidPunch registration:  \n\n";
            // emailMsgTxt +=
            // com.server.Constants.IP_URL+"/paid_punch/verifybususer?uid="+businessuserid+"&email="+email+"\n\n";
            // emailMsgTxt +=
            // com.server.Constants.IP_URL+"/"+com.server.Constants.applicationName+"/verifybususer?uid="+businessuserid+"&email="+email+"\n\n";

            // messageSending(emailSubjectTxt, recipient_email_id , emailMsgTxt);

            /*
             * TODO output your page here out.println("<html>"); out.println("<head>");
             * out.println("<title>Servlet forgot_pass_mail_send</title>"); out.println("</head>");
             * out.println("<body>"); out.println("<h1>Servlet forgot_pass_mail_send at " + request.getContextPath () +
             * "</h1>"); out.println("</body>"); out.println("</html>");
             */
        } catch (Exception e) {
            com.server.Constants.logger.info("Error in Sending Email : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * private void messageSending(String mail_subject, String mail_to, String mail_body) throws MessagingException {
     * Message message = new MimeMessage(getSession());
     * 
     * message.addRecipient(RecipientType.TO, new InternetAddress(mail_to)); message.addFrom(new InternetAddress[] { new
     * InternetAddress(SMTP_AUTH_USER) });
     * 
     * message.setSubject(mail_subject); message.setContent(mail_body, "text/plain");
     * 
     * Transport.send(message); }
     */
    /*
     * private void appUsermessageSending(String mail_subject, String mail_to, String mail_body) throws
     * MessagingException { Message message = new MimeMessage(getSession());
     * 
     * message.addRecipient(RecipientType.TO, new InternetAddress(mail_to)); message.addFrom(new InternetAddress[] { new
     * InternetAddress(SMTP_AUTH_USER) });
     * 
     * message.setSubject(mail_subject); message.setContent(mail_body, "text/html");
     * 
     * Transport.send(message); }
     */

    public class SMTPAuthenticator extends javax.mail.Authenticator {
        private PasswordAuthentication authentication;

        public SMTPAuthenticator() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            authentication = new PasswordAuthentication(username, password);
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }

    /*
     * public Session getSession() { SMTPAuthenticator authenticator = new SMTPAuthenticator();
     * 
     * Properties properties = new Properties(); properties.setProperty("mail.smtp.submitter",
     * authenticator.getPasswordAuthentication().getUserName()); properties.setProperty("mail.smtp.auth", "true");
     * 
     * properties.setProperty("mail.smtp.host", SMTP_HOST_NAME); properties.setProperty("mail.smtp.port", SMTP_PORT);
     * 
     * return Session.getInstance(properties, authenticator); }
     */

    public String getPunchCardName(String punchCardName) throws ServletException {// ,String businessname) throws
                                                                                  // ServletException{
        DBConnection db = null;
        Statement stmt = null;
        ResultSet rs = null;
        String status = "00";

        try {
            db = new DBConnection();
            stmt = db.stmt;
            String query1 = "SELECT * from punch_card where punch_card_name ='" + punchCardName + "'";
            rs = stmt.executeQuery(query1);
            com.server.Constants.logger.info("The select query is " + query1);
            // displaying records

            if (rs.next()) {
                status = "01";
            }

        } catch (SQLException e) {
            com.server.Constants.logger
                    .error("Error in Sql in checksecretcode.java in getsecretcode " + e.getMessage());
            throw new ServletException("SQL Exception.", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    // Constants.logger.info("Closing rs Statement ");
                    rs = null;
                }
                db.closeConnection();

            } catch (SQLException e) {
                com.server.Constants.logger.error("Error in closing SQL in checksecretcode.java" + e.getMessage());
            }
        }
        return status;
    }

}
