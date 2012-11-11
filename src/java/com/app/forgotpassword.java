package com.app;

import com.db.DataAccessController;
import com.jspservlets.SignupAddPunch;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.PropertyConfigurator;
import com.server.Constants;
import com.server.SAXParserExample;
import com.server.AccessRequestElements;
import java.io.StringReader;
import javax.servlet.ServletInputStream;
import org.xml.sax.InputSource;

/**
 * @author qube26
 */
public class ForgotPassword extends HttpServlet {

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
    ServletConfig config = null;
    private List list;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            config = getServletConfig();
            context = config.getServletContext();
            Properties props = new Properties();
            String path = context.getRealPath("appMessage.properties").toString();
            props.load(new FileInputStream(path));
            PropertyConfigurator.configure(props);
            String emailmessage = "";

            Constants.loadJDBCConstants(context);

            ServletInputStream in = request.getInputStream();

            SAXParserExample xmlparser = new SAXParserExample();
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

            xmlparser.parseDocument(iSource);
            list = xmlparser.getData();
            AccessRequestElements arz = (AccessRequestElements) list.get(0);
            String reqtype = arz.getTxtype();
            String email = arz.getEmail();
            // String email="sgelda7@gmail.com";
            try {
                String password = DataAccessController.getPassword(email);

                if (password.equals("")) {

                    emailmessage = props.getProperty("forgotpassword.invaildemailid");
                    forgotpasswordxml(response, "01", emailmessage);
                    return;

                } else {
                    emailmessage = props.getProperty("forgotpassword.vaildemailid");

                }
                SignupAddPunch mail = new SignupAddPunch();
                mail.sendEmail_For_forgotPassword(password, email);
                forgotpasswordxml(response, "00", emailmessage + " " + email + " .");
            } catch (SQLException ex) {
                Logger.getLogger(ForgotPassword.class.getName()).log(Level.SEVERE, null, ex);
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

    private void forgotpasswordxml(HttpServletResponse p_response, String statusCode, String statusMessage)
            throws IOException {
        try {
            PrintWriter out = p_response.getWriter();

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