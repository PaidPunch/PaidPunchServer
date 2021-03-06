package com.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.InputSource;

import com.db.DataAccess;
import com.db.DataAccessController;
import com.jspservlets.SignupAddPunch;
import com.server.AccessRequest;
import com.server.Constants;
import com.server.SAXParserExample;
import com.server.Utility;

/**
 * @author qube26
 */
public class Register extends HttpServlet {

    private static final long serialVersionUID = 8523351195259620022L;
    ServletConfig config = null;
    private Vector userdata, userinfo;

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
    ServletContext context;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            response.setContentType("text/html;charset=UTF-8");
            List list;
            try {
                config = getServletConfig();
                context = config.getServletContext();
                Constants.loadJDBCConstants(context);
            } catch (Exception e) {
                Constants.logger.error(e);
            }
            try {
                ServletInputStream in = request.getInputStream();
                // in.setEncoding("UTF-8");

                SAXParserExample example = new SAXParserExample();

                int info;
                StringBuffer sb = new StringBuffer();
                while ((info = in.read()) != -1) {
                    char temp;
                    temp = (char) info;
                    sb.append(temp);
                }
                Constants.logger.info("-------------paid_punch---------------");

                Constants.logger.info("XML File rev in app_registation");
                String xmldata = new String(sb);
                xmldata = xmldata.trim();
                // org.apache.commons.lang.StringEscapeUtils u=new org.apache.commons.lang.StringEscapeUtils();
                // xmldata=StringEscapeUtils.unescapeXml(xmldata);
                // xmldata = new String(xmldata.getBytes("UTF-8"), "UTF-8");
                InputSource iSource = new InputSource(new StringReader(xmldata));
                iSource.setEncoding("UTF-8");
                example.parseDocument(iSource);
                list = example.getData();
                AccessRequest arz = (AccessRequest) list.get(0);
                String reqtype = arz.getTxType();

                if (reqtype.equalsIgnoreCase("REGISTER-REQ")) {
                    Constants.logger.info("XML File" + sb);
                    userregister(list, response);
                }
                if (reqtype.equalsIgnoreCase("LOGIN-REQ")) {
                    String username = "";
                    String password = "";
                    String sessionId = arz.getSessionId();
                    // if sesstion id .
                    if (sessionId.equalsIgnoreCase("")) {
                        SessionHandler session = new SessionHandler();
                        sessionId = session.generateSessionId();
                    }
                    username = arz.getName();
                    password = arz.getPassword();
                    DataAccess dataAccess = new DataAccess();
                    String res = "";
                    res = dataAccess.login(username, password, sessionId);
                    if (res.equalsIgnoreCase("02")) {
                        xmlloginORlogout(response, "02", "User not registered ", res);
                        return;
                    } else if (res.equalsIgnoreCase("01")) {

                        xmlloginORlogout(response, "01", "Incorrect password", res);
                        return;
                    } else if (res.equalsIgnoreCase("03")) {

                        xmlloginORlogout(response, "01",
                                "This account is logged in on another device. Sign out on that device and try again.",
                                res);
                        return;
                    } else if (res.equalsIgnoreCase("04")) {

                        xmlloginORlogout(
                                response,
                                "04",
                                "You still need to verify your email address. Click the link within the email that we sent you.",
                                res);
                        return;
                    } else {
                        userdata = DataAccessController.getDataFromTable("app_user", "user_id", res);
                        userinfo = (Vector) userdata.elementAt(0);
                        userinfo.add(sessionId);
                    }
                    xmllogin(response, " Login Successful", userinfo);

                    return;
                }// login type request
                if (reqtype.equalsIgnoreCase("BUSSINESSOFFER-REQ")) {
                    Constants.logger.info("XML File" + sb);
                    BUSSINESSOFFER(list, response);
                }
                // if (reqtype.equalsIgnoreCase("BUYBUSSINESSOFFER-REQ")) {
                // buy_Bussiness_Offer(list, response);
                // }
                if (reqtype.equalsIgnoreCase("LOGOUT-REQ")) {
                    Constants.logger.info("XML File" + sb);
                    logOut(list, response);
                }
            } catch (Exception ex) {
                xmlloginORlogout(response, "01", "Failed to process request", "0");
                Constants.logger.error(ex);
            }
        } catch (Exception e) {
            Constants.logger.error(e);
            xmlloginORlogout(response, "01", "Failed to process request", "0");
        }

    }

    private void BUSSINESSOFFER(List list, HttpServletResponse response) {
        String userId = "";
        String verificationCode = "";
        java.util.Date punch_expire_Date;
        try {
            AccessRequest arz = (AccessRequest) list.get(0);
            userId = arz.getUserId();
            verificationCode = arz.getVerificationCode();
            String sessionId = arz.getSessionId();
            SessionHandler session = new SessionHandler();
            boolean b = session.sessionidverify(userId, sessionId);
            boolean isFreePunch = false;
            // boolean b = DataAccessControler.getUserValidation("app_user", "user_id", userid);
            String msg;
            if (b) {
                Vector rowdata = (Vector) DataAccessController.getDataFromTable("punch_card", "qrcode", verificationCode)
                        .elementAt(0);
                DataAccess da = new DataAccess();

                // user already purch free card
                isFreePunch = da.isFreePunch("" + rowdata.elementAt(0), userId);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                String strDate = dateFormat.format(new Date());

                java.util.Date l_scan_Date = dateFormat.parse(strDate);

                int validdays = Integer.parseInt(rowdata.elementAt(13).toString());
                Date expire_time_in_long = Utility.addDays(l_scan_Date, validdays);

                strDate = dateFormat.format(expire_time_in_long);
                punch_expire_Date = dateFormat.parse(strDate);

                Constants.logger.debug("l_scan_Date={}" + l_scan_Date);
                // punch_expire_Date = new java.util.Date(((java.sql.Date) rowdata.elementAt(5)).getTime());
                if (Utility.isAfterDateTime(l_scan_Date, punch_expire_Date)) {
                    msg = "This card is no longer available";

                    Constants.logger.info(msg);
                    getBusinesXml(response, rowdata, "02", msg, isFreePunch);
                    return;
                } else {

                    String bid = "" + rowdata.elementAt(7);
                    Vector businessdata = (Vector) DataAccessController.getDataFromTable("business_users",
                            "business_userid", bid).elementAt(0);
                    String busn_provide_free_punch = "" + businessdata.elementAt(12);

                    // secoond time free punch not allow
                    if (busn_provide_free_punch.endsWith("true") && isFreePunch == false)
                    {
                        isFreePunch = true;
                    }
                    else
                    {
                        isFreePunch = false;
                    }
                    String buss_name = "";
                    byte buss_logo[] = null;
                    buss_name = "" + businessdata.elementAt(1);
                    buss_logo = (byte[]) businessdata.elementAt(7);
                    rowdata.add(buss_name);
                    rowdata.add(buss_logo);
                    rowdata.add(businessdata.elementAt(5));
                    String imageurl = businessdata.elementAt(11).toString();

                    dateFormat = new SimpleDateFormat("MM-dd-yyyy");

                    String punchexpireDate = dateFormat.format(punch_expire_Date);
                    rowdata.add(punchexpireDate);
                    rowdata.add(imageurl);
                    getBusinesXml(response, rowdata, "00", "Succesful", isFreePunch);
                    return;
                }

            } else {
                Vector d = null;
                getBusinesXml(response, d, "400", "You have logged in from another device", isFreePunch);
                return;

            }

        } catch (Exception e) {
            Vector d = null;
            try {
                getBusinesXml(response, d, "01", "No Match found.Please scan correct QR code.", false);
                return;
            } catch (EncoderException ex) {
                Constants.logger.error(ex);
            }
            Constants.logger.error(e);
        }
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
            throws ServletException, IOException {
        processRequest(request, response);
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

    private void userregister(List list, HttpServletResponse response) {
        String name = "";
        String password = "";
        try {
            AccessRequest arz = (AccessRequest) list.get(0);
            if (arz.getName() == null || arz.getName().equalsIgnoreCase("(null)")
                    || arz.getName().equalsIgnoreCase("null"))
            {
                arz.setName("");
            }
            if (arz.getEmail() == null || arz.getEmail().equalsIgnoreCase("(null)")
                    || arz.getEmail().equalsIgnoreCase("null"))
            {
                arz.setEmail("");
            }
            if (arz.getPin() == null || arz.getPin().equalsIgnoreCase("(null)")
                    || arz.getPin().equalsIgnoreCase("null"))
            {
                arz.setPin("");
            }
            if (arz.getMobileNumber() == null || arz.getMobileNumber().equalsIgnoreCase("(null)")
                    || arz.getMobileNumber().equalsIgnoreCase("null"))
            {
                arz.setMobileNumber("");
            }

            name = arz.getName();

            password = arz.getPassword();

            Vector rowdata = new Vector();
            rowdata.add(name);

            rowdata.add(arz.getEmail());

            rowdata.add(arz.getMobileNumber());
            rowdata.add(password);
            String pin = "";
            rowdata.add(arz.getPin());
            // by defult staus no boz not login yet
            rowdata.add("N");
            // email not verify at reg time.

            rowdata.add("N");
            java.sql.Time time = new java.sql.Time(new Date().getTime());
            java.sql.Date date = new java.sql.Date(new Date().getTime());
            rowdata.add(time);
            rowdata.add(date);
            rowdata.add("N");
            Vector colmid = new Vector();
            colmid.add("email_id");
            colmid.add("password");
            colmid.add("isfbaccount");
            Vector data = new Vector();
            data.add(arz.getEmail());
            data.add(password);
            data.add("N");
            DataAccess dataaccess = new DataAccess();
            String res = dataaccess.UserRegistration(rowdata);
            if (res.equalsIgnoreCase("00")) {
                Vector userdata = (Vector) DataAccessController.getDataFromTable("app_user", colmid, data);
                Vector userinfo = (Vector) userdata.elementAt(0);
                regXml(response, res, userinfo);
                // email send for app user conformation
                SignupAddPunch mail = new SignupAddPunch();
                mail.sendEmail_For_app_user("" + userinfo.elementAt(0), arz.getEmail());

                return;
            }
            Vector userinfo = null;
            regXml(response, res, userinfo);

            return;

        } catch (Exception e) {

            Constants.logger.error(e);

        }
        try {
            Vector userinfo = null;
            regXml(response, "02", userinfo);
        } catch (IOException ex) {
            Constants.logger.error(ex);
        }
    }

    private void regXml(HttpServletResponse response, String res, Vector userinfo) throws IOException {
        PrintWriter out = response.getWriter();
        Constants.logger.info("Respones ");
        Constants.logger.info("statuscode" + res);
        String statusMessage = "";

        response.setHeader("Content-Disposition", "attachement; filename= response.xml");
        out.print("<?xml version='1.0' ?>"
                + "<paidpunch-resp>"
                + "<statusCode>" + res + "</statusCode>");
        if (res.equalsIgnoreCase("00")) {
            statusMessage = "You’re almost done!"
                    + " We’ve sent an email to " + userinfo.elementAt(2) + "."
                    + " Click the link within the email to confirm your account and begin saving money with PaidPunch!";
            String respons = "<userid>" + userinfo.elementAt(0) + "</userid>"
                    + "<name>" + userinfo.elementAt(1) + "</name><pin>" + userinfo.elementAt(5) + "</pin>"
                    + "<email>" + userinfo.elementAt(2) + "</email>"
                    + "<mobilenumber>" + userinfo.elementAt(3) + "</mobilenumber>";
            out.print(respons);
            Constants.logger.info("respons" + respons);
        }
        if (res.equalsIgnoreCase("01")) {
            statusMessage = " Emailid Already Registered.";
        }
        if (res.equalsIgnoreCase("02")) {
            statusMessage = "Failed to Process Registration Request.Please try again.";

        }
        out.print("<statusMessage>" + statusMessage + "</statusMessage>");

        out.print("</paidpunch-resp>");

    }

    // this function call when login failed

    private void xmlloginORlogout(HttpServletResponse p_response, String statusCode, String statusMessage, String userid)
            throws IOException {
        try {
            PrintWriter out = p_response.getWriter();
            Constants.logger.info("Respones userid   " + userid);
            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);

            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            String res = "<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + "<statusCode>" + statusCode + "</statusCode>"
                    + "<statusMessage>" + statusMessage + "</statusMessage>"
                    + "</paidpunch-resp>";
            out.print(res);
            Constants.logger.info(res);
        } catch (Exception e) {
            Constants.logger.error(e);
        }

    }

    // this function call when login succesful

    private void xmllogin(HttpServletResponse p_response, String mesage, Vector userdata) {
        String userid = "" + userdata.elementAt(0);
        String statusCode = "00";
        String statusMessage = mesage;

        try {
            PrintWriter out = p_response.getWriter();

            Constants.logger.info("Respones userid   " + userid);
            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);

            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            out.print("<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + "<statusCode>" + statusCode + "</statusCode>");
            String respons = "<userid>" + userid + "</userid>"
                    + "<email>" + userdata.elementAt(2) + "</email>"
                    + "<mobilenumber>" + userdata.elementAt(3) + "</mobilenumber>"
                    + "<name>" + userdata.elementAt(1) + "</name>"
                    + "<sessionid>" + userdata.elementAt(10) + "</sessionid>"
                    + "<is_profileid_created>" + userdata.elementAt(13) + "</is_profileid_created>";
            out.print(respons);

            out.print("<statusMessage>" + statusMessage + "</statusMessage>");

            out.print("</paidpunch-resp>");
            Constants.logger.info("user login with userid " + userid);

        } catch (Exception e) {
            Constants.logger.error(e);
        }

    }

    private void getBusinesXml(HttpServletResponse p_response, Vector businessdata, String statusCode,
            String statusMessage, boolean isfreepunch) throws EncoderException {
        try {
            PrintWriter out = p_response.getWriter();

            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);

            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            out.print("<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + "<statusCode>" + statusCode + "</statusCode>");
            out.print("<statusMessage>" + statusMessage + "</statusMessage>");
            if (statusCode.equalsIgnoreCase("00")) {
                byte[] l_imageData = null;
                l_imageData = (byte[]) businessdata.elementAt(17);// 14
                Base64 baseimage = new Base64();
                baseimage.encode(l_imageData);

                int val1 = Integer.parseInt("" + businessdata.elementAt(2));
                float val2 = Float.parseFloat("" + businessdata.elementAt(3));

                String respons = "<bussinessid>"
                        + businessdata.elementAt(7)
                        + "</bussinessid>"// 7
                        + "<bussinessname>"
                        + StringEscapeUtils.escapeXml(businessdata.elementAt(8).toString())
                        + "</bussinessname>"// 8
                        + "<punchcardid>" + businessdata.elementAt(0) + "</punchcardid>"
                        + "<punchcardname>" + StringEscapeUtils.escapeXml(businessdata.elementAt(1).toString())
                        + "</punchcardname>"
                        + "<pucnchcarddesc>" + StringEscapeUtils.escapeXml(businessdata.elementAt(18).toString())
                        + "</pucnchcarddesc>"
                        + "<totalnoofpunches>" + businessdata.elementAt(2) + "</totalnoofpunches>"
                        + "<eachpunchvalue>" + businessdata.elementAt(3) + "</eachpunchvalue>"
                        + "<actualprice>" + val1 * val2 + "</actualprice>"
                        + "<sellingprice>" + businessdata.elementAt(4) + "</sellingprice>"
                        + "<discount>" + businessdata.elementAt(6) + "</discount>"
                        + "<expiredate>" + new String(businessdata.elementAt(19).toString()) + "</expiredate>"
                        + "<isfreepunch>" + isfreepunch + "</isfreepunch>"
                        + "<is_mystery_punch>" + businessdata.elementAt(11) + "</is_mystery_punch>"
                        + "<minimum_value>" + businessdata.elementAt(14) + "</minimum_value>"
                        + "<expire_days>" + businessdata.elementAt(13) + "</expire_days>"
                        + "<discount_value_of_each_punch>" + businessdata.elementAt(9)
                        + "</discount_value_of_each_punch>"
                        + "<redeem_time_diff>" + businessdata.elementAt(10) + "</redeem_time_diff>"
                        + "<bussinesslogo_url>" + businessdata.elementAt(20).toString() + "</bussinesslogo_url>"
                        + "<punchcard_code>" + businessdata.elementAt(15).toString() + "</punchcard_code>";// 10

                out.print(respons);
                // out.print("<bussinesslogo>" + new String(baseimage.encode(l_imageData)) + "</bussinesslogo>");
                Constants.logger.info("respons" + respons);
            }

            out.print("</paidpunch-resp>");

        } catch (IOException ex) {
            Constants.logger.error(ex);
        }
    }
    
    private void logOut(List list, HttpServletResponse response) {
        AccessRequest arz = (AccessRequest) list.get(0);
        String userId = arz.getUserId();
        DataAccess da = new DataAccess();
        String res = da.logout(userId);
        if (res.equalsIgnoreCase("01")) {
            try {
                xmlloginORlogout(response, res, "Failed to logout.Please try after sometime", userId);
            } catch (IOException ex) {
                Constants.logger.error(ex);
            }
            return;
        } else {
            try {
                xmlloginORlogout(response, "00", "You have been Logged out Successfully", userId);
            } catch (IOException ex) {
                Constants.logger.error(ex);
            }
        }

    }
}
