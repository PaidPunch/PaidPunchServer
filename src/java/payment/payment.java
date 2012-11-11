package payment;

import com.app.SessionHandler;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import com.db.DataAccess;
import com.db.DataAccessController;
import com.jspservlets.SignupAddPunch;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.server.Constants;
import com.server.SAXParserExample;
import com.server.Utility;
import com.server.AccessRequestElements;
import java.io.FileInputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletInputStream;
import net.authorize.TransactionCommunication;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.InputSource;

/**
 * @author qube26
 */
public class Payment extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        String msg;
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

            SAXParserExample example = new SAXParserExample();

            int info;
            StringBuffer sb = new StringBuffer();
            while ((info = in.read()) != -1) {
                char temp;
                temp = (char) info;
                sb.append(temp);
            }
            Constants.logger.info("-------------paid_punch---------------");

            Constants.logger.info("XML File rev");
            String xmldata = new String(sb);
            xmldata = xmldata.trim();
            InputSource iSource = new InputSource(new StringReader(xmldata));

            example.parseDocument(iSource);
            list = example.getData();
            AccessRequestElements arz = (AccessRequestElements) list.get(0);
            String reqtype = arz.getTxtype();

            if (reqtype.equalsIgnoreCase("BUYBUSSINESSOFFER-REQ")) {
                try {
                    Constants.logger.info("requset for BUYBUSSINESSOFFER-REQ");
                    buy_Bussiness_Offer(list, response);
                } catch (ParseException ex) {
                    Constants.logger.info(ex.toString());
                }
            }
        } finally {
            out.close();
        }
    }

    private void buy_Bussiness_Offer(List list, HttpServletResponse response) throws ParseException {
        try {
            String path = context.getRealPath("appMessage.properties").toString();
            Properties props = new Properties();
            props.load(new FileInputStream(path));
            PropertyConfigurator.configure(props);
            String wrongnoenter = props.getProperty("buy.punch.wrong.entered");
            String codeexpiremsg = props.getProperty("buy.punch.merchant.code.expire");

            // String successmsg = props.getProperty("issue.success");
            AccessRequestElements arz = (AccessRequestElements) list.get(0);
            String userId = arz.getUserId();
            String sccancode = arz.getVerificationCode();
            String oreangecode = arz.getOrangeqrscannedvalue();
            byte[] decoded = Base64.decodeBase64(oreangecode.getBytes());
            String reangecode = new String(decoded);
            String punchcardid = arz.getPunchCardID();
            String sessionid = arz.getSessionid();
            String isfreepunch = arz.getIsfreepunch();
            String tid = null;
            String mystrypunchid = null, value_mystery_punch = "";
            String amt = arz.getAmount();
            String paymentid = arz.getPaymentid();
            Constants.logger.info("userid:" + userId);
            Constants.logger.info("punchcardid:" + punchcardid);
            Constants.logger.info("paymentid:" + paymentid);
            SessionHandler session = new SessionHandler();
            boolean sessionverify = session.sessionidverify(userId, sessionid);
            if (!(sessionverify)) {
                buy_Punch(response, userdata, "400", "You have logged in from another device");
                return;
            }
            // punch card exipre check and deactive bussn
            Vector puch_card_data = (Vector) DataAccessController.getDataFromTable("punch_card", "punch_card_id",
                    punchcardid).elementAt(0);
            Vector buss_data = (Vector) DataAccessController.getDataFromTable("business_users", "business_userid",
                    puch_card_data.elementAt(7).toString()).elementAt(0);
            String busi_enabled = buss_data.elementAt(14).toString();
            if (busi_enabled.equalsIgnoreCase("N")) {
                buy_Punch(response, userdata, "401", " Sorry,This business is expired ");
                return;
            }

            String expiredate = puch_card_data.elementAt(5).toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date tempexipredate = dateFormat.parse(expiredate);

            String strDate = dateFormat.format(new java.util.Date());
            java.util.Date l_scan_Date = dateFormat.parse(strDate);

            // punch exipre
            if (Utility.isAfterDateTime(l_scan_Date, tempexipredate)) {
                buy_Punch(response, userdata, "401", " Sorry,This business is expired ");
                return;
            }

            if (isfreepunch.equalsIgnoreCase("false")) {
                DataAccess da = new DataAccess();
                Vector profile_info = da.getprofileid(userId);
                if (profile_info == null) {
                    buy_Punch(response, userdata, "02", "Failed to process request.Please try again.");
                    return;
                }
                String profileid = profile_info.elementAt(0).toString();
                TransactionCommunication tc = new TransactionCommunication();
                Vector paymentdata = tc.makePaymentAuthCapture(profileid, paymentid, amt);
                String code = "", servermes = "", getwaymessage = "", invoiceno = "", authcode = "", transactionId = "";
                code = paymentdata.elementAt(0).toString();
                servermes = paymentdata.elementAt(1).toString();
                getwaymessage = paymentdata.elementAt(2).toString();

                invoiceno = paymentdata.elementAt(3).toString();
                authcode = paymentdata.elementAt(4).toString();
                transactionId = paymentdata.elementAt(5).toString();
                com.server.Constants.logger.info("Invoice No : " + invoiceno + " Authentication Code : " + authcode
                        + " Transaction ID : " + transactionId);
                Vector payment_data = new Vector();
                payment_data.add(punchcardid);
                payment_data.add(userId);
                payment_data.add(transactionId);
                payment_data.add(getwaymessage);
                tid = DataAccessController.insert_payment(payment_data);
                if (!(code.equalsIgnoreCase("00"))) {

                    buy_Punch(response, userdata, code, getwaymessage);
                    return;
                }

                // tid=da.getpaymentID();
                // tid="123456";
            }

            // only one time purch free punch
            if (isfreepunch.equalsIgnoreCase("true")) {
                DataAccess da = new DataAccess();
                boolean freepunch_buy = da.check_free_punch(punchcardid, userId);
                if (freepunch_buy) {
                    buy_Punch(response, userdata, "01", "You have already unlocked a free punch");
                    return;
                }
            }
            if (isfreepunch.equalsIgnoreCase("true")) {
                tid = "" + 0;
            }

            // punch card valid or not
            boolean b = DataAccessController.getUserValidation("punch_card", "punch_card_id", punchcardid);
            if (b) {
                userdata = new Vector();
                userdata.add(userId);
                userdata.add(punchcardid);
                Vector punchdata = (Vector) DataAccessController.getDataFromTable("punch_card", "punch_card_id",
                        punchcardid).elementAt(0);

                // no verifyer code modify on 19 jan 2012

                // String vrifyno = "" + punchdata.elementAt(7);

                // 13 mar 2012
                // boolean no_check = DataAccessControler.buy_punch_verifyer("marchant_code", oreangecode,
                // arz.getPunchCardID());
                // if (no_check) {
                // buy_Punch(response, userdata, "02", wrongnoenter);
                // return;
                //
                // }
                // boolean timecheck = timelimitcheck(oreangecode, punchcardid, Constants.merchant_code_validate_time);
                // if (timecheck) {
                // buy_Punch(response, userdata, "02",codeexpiremsg);
                // return;
                // }

                // if((""+punchdata.elementAt(11)).equalsIgnoreCase("true"))
                // {
                // DataAccess da=new DataAccess();
                // Vector mystery_data=DataAccessControler.getDataFromTable("mystery_punch", "punch_card_id",
                // punchcardid);
                // Vector mystery_info=(Vector) mystery_data.elementAt(0);
                // mystrypunchid =""+mystery_info.elementAt(0);
                // value_mystery_punch=""+mystery_info.elementAt(2);
                // dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormat_exipre = new SimpleDateFormat("MM-dd-yyyy");
                strDate = dateFormat_exipre.format(new Date());
                l_scan_Date = dateFormat_exipre.parse(strDate);
                int validdays = Integer.parseInt(punchdata.elementAt(13).toString());
                Date expire_time_in_long = Utility.addDays(l_scan_Date, validdays);
                String punch_expire_Date = dateFormat_exipre.format(expire_time_in_long);
                // Date punch_expire_Date = dateFormat.parse(strDate);

                java.util.Date match_Start_Date = new java.util.Date(((java.sql.Date) punchdata.elementAt(5)).getTime());
                strDate = dateFormat.format(new Date());
                l_scan_Date = dateFormat.parse(strDate);
                // if (Utility.isAfterDateTime(l_scan_Date, match_Start_Date)) {
                // buy_Punch(response, userdata, "01", "Expired");
                // return;
                // }
                SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
                String strTime = timeFormat.format(new Date());

                java.util.Date sqlTime = timeFormat.parse(strTime);
                java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());

                // no of punchs
                if (isfreepunch.equalsIgnoreCase("true")) {
                    userdata.add("1");

                } else {
                    userdata.add(punchdata.elementAt(2));

                }
                userdata.add(sqlDate);
                userdata.add(sqlTime);
                if (isfreepunch.equalsIgnoreCase("false")) {
                    userdata.add(tid);
                }
                userdata.add(isfreepunch);
                userdata.add(punch_expire_Date);
                // userdata.add(mystrypunchid);
                int i = DataAccessController.insert_punchcard_download("punchcard_download", userdata, isfreepunch);
                if (i != -1) {
                    DataAccess da = new DataAccess();
                    if (isfreepunch.equalsIgnoreCase("true"))
                    {
                        da.insert_user_feeds(punchcardid, userId, "free", "F", null);
                    }
                    else
                    {
                        da.insert_user_feeds(punchcardid, userId, "bought", "F", null);
                    }
                    buy_Punch(response, userdata, "00", "Succesful");
                    return;
                } else {
                    buy_Punch(response, userdata, "02", "Failed to process request.Please try again.");
                    return;
                }

            } else {
                buy_Punch(response, userdata, "400", "You have logged in from another device");

            }
        } catch (SSLPeerUnverifiedException ssl)
        {
            Constants.logger.error(ssl);
            buy_Punch(response, userdata, "02", "Failed to process request.Please try again.");
            return;
        } catch (Exception ex) {
            Constants.logger.error(ex);
            buy_Punch(response, userdata, "02", "Failed to process request.Please try again.");
            return;
        }
    }

    private void buy_Punch(HttpServletResponse p_response, Vector userdata, String statusCode, String statusMessage) {
        try {
            PrintWriter out = p_response.getWriter();
            // Constants.logger.info("Respones userid   " + userid);
            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);

            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            String res = "<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + "<statusCode>" + statusCode + "</statusCode>"
                    + "<statusMessage>" + statusMessage + "</statusMessage>"
                    + "</paidpunch-resp>";
            out.print(res);
            Constants.logger.info("Buy request response");
            Constants.logger.info(res);
        } catch (Exception e) {
            Constants.logger.error(e);
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
}
