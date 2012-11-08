package merchant;

import org.apache.commons.codec.binary.Base64;
import com.app.punch;
import com.db.DataAccess;
import com.db.DataAccessControler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.server.Constants;
import com.server.SAXParserExample;
import com.server.aczreqElements;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;
import javax.servlet.ServletInputStream;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.InputSource;

/**
 * @author qube26
 */
public class merchant_app extends HttpServlet {

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
    ServletConfig config = null;
    ServletContext context;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

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

            Constants.logger.info("XML File" + sb);
            String xmldata = new String(sb);
            xmldata = xmldata.trim();
            InputSource iSource = new InputSource(new StringReader(xmldata));

            example.parseDocument(iSource);
            list = example.getData();
            aczreqElements arz = (aczreqElements) list.get(0);
            String reqtype = arz.getTxtype();
            if (reqtype.equalsIgnoreCase("Redeem-REQ")) {
                redeem(list, response);

            } else if (reqtype.equalsIgnoreCase("Issue-REQ")) {
                issue(list, response);
            } else if (reqtype.equalsIgnoreCase("Login-REQ")) {
                login(list, response);
            }

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

    private void redeem(List list, HttpServletResponse response) {
        String barcodevalue = "";
        String bussinessid = "";
        try {
            aczreqElements arz = (aczreqElements) list.get(0);
            barcodevalue = arz.getBarcodevalue();
            bussinessid = arz.getBusinessid();
            Vector rowdata = new Vector();
            rowdata.add(barcodevalue);
            rowdata.add(bussinessid);
            Vector userinfo = new Vector();
            userinfo.add("");
            userinfo.add("");
            // config = getServletConfig();
            // ServletContext context = config.getServletContext();
            String path = context.getRealPath("appMessage.properties").toString();
            Properties props = new Properties();
            props.load(new FileInputStream(path));
            PropertyConfigurator.configure(props);
            PrintWriter out = response.getWriter();

            try {
                String invalidbarcode, validbarcode;
                invalidbarcode = props.getProperty("redeem.invaildbarcode");
                validbarcode = props.getProperty("redeem.vaildbarcode");
                Constants.logger.info(" " + barcodevalue + "  " + bussinessid);
                boolean b = DataAccessControler.barcodechecker(bussinessid, barcodevalue);
                if (b) {
                    xml_write(response, "01", invalidbarcode);
                } else {
                    Vector filed = new Vector();
                    filed.add("barcode_scan_time");
                    filed.add("barcode_scan_date");
                    filed.add("barcode_status");
                    Vector filedvalue = new Vector();
                    Date d = new Date();
                    java.sql.Date date = new java.sql.Date(d.getTime());
                    java.sql.Time time = new java.sql.Time(d.getTime());
                    filedvalue.add(time);
                    filedvalue.add(date);
                    filedvalue.add("scan");
                    int i = DataAccessControler.updateDataToTable("punch_card_tracker", "barcode_value", barcodevalue,
                            filed, filedvalue);

                    if (i == -1) {
                        xml_write(response, "01", invalidbarcode);

                    } else {
                        xml_write(response, "00", validbarcode);
                    }
                }
            } catch (Exception ex) {
                Constants.logger.error(ex);
            }

        } catch (Exception e) {

            Constants.logger.error(e);

        }
        try {
            Vector userinfo = null;

        } catch (Exception ex) {
            Constants.logger.error(ex);
        }

    }

    private void xml_write(HttpServletResponse p_response, String statusCode, String statusMessage) throws IOException {
        try {
            PrintWriter out = p_response.getWriter();

            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);
            // String res = "<?xml version='1.0' ?>"
            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            String res = "<? xml version='1.0' ?>"
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

    private void issue(List list, HttpServletResponse response) {

        String bussinessid = "";

        try {
            aczreqElements arz = (aczreqElements) list.get(0);
            bussinessid = arz.getBusinessid();

            String path = context.getRealPath("appMessage.properties").toString();
            Properties props = new Properties();
            props.load(new FileInputStream(path));
            PropertyConfigurator.configure(props);
            String failuremsg = props.getProperty("issue.failure");
            String successmsg = props.getProperty("issue.success");
            PrintWriter out = response.getWriter();
            String issueno = RandomInteger();
            boolean check_table = DataAccessControler.getchecker("marchant_code", "code", issueno);
            while (check_table) {
                issueno = RandomInteger();
                check_table = DataAccessControler.getchecker("marchant_code", "code", issueno);
            }
            Vector rowdata = new Vector();
            rowdata.add(bussinessid);
            rowdata.add(issueno);
            rowdata.add(new java.sql.Date(new Date().getTime()));
            rowdata.add(new java.sql.Time(new Date().getTime()));
            rowdata.add("genrate");
            int res = DataAccessControler.insert_issue_code("marchant_code", rowdata);
            if (res != -1) {
                issue_xml_write(response, "00", successmsg, issueno);
            } else {
                issue_xml_write(response, "01", failuremsg, issueno);
            }
        } catch (Exception e) {
        }
    }

    private void issue_xml_write(HttpServletResponse p_response, String statusCode, String statusMessage, String code) {
        try {
            PrintWriter out = p_response.getWriter();

            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);

            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            String res = "<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + "<statusCode>" + statusCode + "</statusCode>"
                    + "<statusMessage>" + statusMessage + "</statusMessage>";
            if (statusCode.equalsIgnoreCase("00")) {
                res = res + "<issueCode>" + code + "</issueCode>";
            }
            res = res + "</paidpunch-resp>";
            out.print(res);
            Constants.logger.info(res);
        } catch (Exception e) {
            Constants.logger.error(e);
        }
    }

    private void login(List list, HttpServletResponse response) {
        try {
            aczreqElements arz = (aczreqElements) list.get(0);
            String path = context.getRealPath("appMessage.properties").toString();
            Properties props = new Properties();
            props.load(new FileInputStream(path));
            PropertyConfigurator.configure(props);
            String failuremsg = props.getProperty("issue.failure");
            String successmsg = props.getProperty("issue.success");
            PrintWriter out = response.getWriter();
            String username = arz.getName();
            String password = arz.getPassword();
            DataAccess da = new DataAccess();
            Vector userinfo = da.merchant_login(username, password);
            String res = "" + userinfo.get(0);
            if (res.equalsIgnoreCase("02")) {
                xmlloginORlogout(response, "02", "User not registered ", res);
                return;
            } else if (res.equalsIgnoreCase("01")) {

                xmlloginORlogout(response, "01", "Incorrect password", res);
                return;
            } else {
                xmllogin(response, " Login Successful", userinfo);
            }

        } catch (IOException ex) {
            Constants.logger.equals(ex);
        }

    }

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

    private void xmllogin(HttpServletResponse p_response, String mesage, Vector userdata) {
        String userid = "" + userdata.elementAt(1);
        String statusCode = "00";
        String statusMessage = mesage;

        try {
            PrintWriter out = p_response.getWriter();

            Constants.logger.info("Respones userid   " + userid);
            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);
            byte[] l_imageData = null;
            l_imageData = (byte[]) userdata.elementAt(3);
            Base64 baseimage = new Base64();
            baseimage.encode(l_imageData);
            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            out.print("<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + "<statusCode>" + statusCode + "</statusCode>");
            String respons = "<userid>" + userid + "</userid>"
                    + "<name>" + userdata.elementAt(2) + "</name>"
                    + "<totalPunch>" + userdata.elementAt(4) + "</totalPunch>"
                    + "<value_of_each_Punch>" + userdata.elementAt(5) + "</value_of_each_Punch>"
                    + "<selling_Price>" + userdata.elementAt(6) + "</selling_Price>";
            Constants.logger.info("respons" + respons);
            respons = respons + "<logo>" + new String(baseimage.encode(l_imageData)) + "</logo>";

            out.print(respons);

            out.print("<statusMessage>" + statusMessage + "</statusMessage>");

            out.print("</paidpunch-resp>");

        } catch (Exception e) {
            Constants.logger.error(e);
        }

    }

    public static String RandomInteger() {

        // int aStart = 100;
        // int aEnd = 999;
        // Random aRandom = new Random();
        // if (aStart > aEnd) {
        // throw new IllegalArgumentException("Start cannot exceed End.");
        // }
        // //get the range, casting to long to avoid overflow problems
        // long range = aEnd - (long) aStart + 1;
        // // System.out.println("range>>>>>>>>>>>" + range);
        // // compute a fraction of the range, 0 <= frac < range
        // long fraction = (long) (range * aRandom.nextDouble());
        // // System.out.println("fraction>>>>>>>>>>>>>>>>>>>>" + fraction);
        // long randomNumber = fraction + (long) aStart;
        // System.out.println("Generated : " + randomNumber + " length " + ("" + randomNumber).length());
        int E4 = 0;
        String temp = "";
        boolean b = true;
        while (b)
        {
            E4 = (int) Math.round(Math.random() * 1000); // 3 digit random
            System.out.print("\nnumber  " + E4);
            temp = "" + E4;
            if (temp.length() == 3)
            {
                b = false;
            }
            temp = "";
        }

        System.out.println("number genrate times " + E4);
        return "" + E4;
    }

}