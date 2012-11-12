package com.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.InputSource;

import com.db.DataAccessController;
import com.server.AccessRequest;
import com.server.Constants;
import com.server.SAXParserExample;

/**
 * @author qube26
 */
public class Update extends HttpServlet {

    private static final long serialVersionUID = 1137930851698507264L;

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
        PrintWriter out = response.getWriter();
        ServletConfig config = null;
        Vector card_list = null;

        try {
            response.setContentType("text/html;charset=UTF-8");
            ServletContext context;
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
            AccessRequest arz = (AccessRequest) list.get(0);

            String reqtype = arz.getTxType();
            if (reqtype.equalsIgnoreCase("EMAILUPDATE-REQ")) {
                email_Updaton(list, response);
            }
            if (reqtype.equalsIgnoreCase("VERIFICATION-REQ")) {
                Password_updation(list, response);
            }
        } catch (Exception e) {
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

    private void email_Updaton(List list, HttpServletResponse response) {
        AccessRequest arz = (AccessRequest) list.get(0);
        String userid = arz.getUserId();
        String email = arz.getEmail();
        String mobileno = arz.getMobileNumber();
        Vector fields = new Vector();
        fields.add("email_id");
        fields.add("mobile_no");
        Vector fields_value = new Vector();
        fields_value.add(email);
        fields_value.add(mobileno);

        String sessionid = arz.getSessionId();
        SessionHandler session = new SessionHandler();
        boolean sessionverify = session.sessionidverify(userid, sessionid);
        if (!sessionverify)
        {
            email_Updation_Xml(response, email, mobileno, "400", "You have logged in from another device");
            return;
        }

        int res = DataAccessController.updateDataToTable("app_user", "user_id", userid, fields, fields_value);
        if (res == 1) {
            email_Updation_Xml(response, email, mobileno, "00", "Update Successful");
            return;
        } else {
            email_Updation_Xml(response, email, mobileno, "01", "Failed to process request.");
            return;
        }
    }

    private void email_Updation_Xml(HttpServletResponse response, String email, String mobileno, String statusCode,
            String statusMessage) {

        PrintWriter out = null;
        try {
            out = response.getWriter();
            String res = "";
            res = res + "<?xml version='1.0' ?>"
                    + "<paidpunch-resp>";
            if (statusCode.equalsIgnoreCase("00")) {
                res = res + "<statusCode>" + statusCode + "</statusCode>"
                        + "<statusMessage>" + statusMessage + "</statusMessage>"
                        + " <email>" + email + "</email>"
                        + "<mobileNumber>" + mobileno + "</mobileNumber>"
                        + "</paidpunch-resp>";
            } else {
                res = res + "<statusCode>" + statusCode + "</statusCode>"
                        + "<statusMessage>" + statusMessage + "</statusMessage>"
                        + "</paidpunch-resp>";

            }
            out.print(res);
            Constants.logger.info("Email Updataion Response->" + res);
        } catch (Exception ex) {
            Constants.logger.error(ex);
        }

    }

    private void Password_updation(List list, HttpServletResponse response) {
        AccessRequest arz = (AccessRequest) list.get(0);
        String userid = arz.getUserId();
        String oldpassword = arz.getOldPassword();
        String password = arz.getPassword();

        Vector pid = new Vector();
        pid.add("user_id");
        pid.add("password");
        Vector pid_value = new Vector();
        pid_value.add(userid);
        pid_value.add(oldpassword);
        Vector fields = new Vector();
        fields.add("password");
        Vector fields_value = new Vector();
        fields_value.add(password);

        String sessionid = arz.getSessionId();
        SessionHandler session = new SessionHandler();
        boolean sessionverify = session.sessionidverify(userid, sessionid);
        if (!sessionverify)
        {
            Password_updation_Xml(response, "400", "You have logged in from another device");
            return;
        }

        int res = DataAccessController.updateDataToTable("app_user", pid, pid_value, fields, fields_value);
        if (res == 1)
        {
            Password_updation_Xml(response, "00", "Your password is updated successfully.");
            return;
        }
        else
        {
            Password_updation_Xml(response, "01", "Failed to process request.");
            return;
        }

    }

    private void Password_updation_Xml(HttpServletResponse p_response, String statusCode, String statusMessage) {
        try {
            PrintWriter out = p_response.getWriter();
            Constants.logger.info("Password Updataion Response->");
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
}