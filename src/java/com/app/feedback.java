package com.app;

import com.db.DataAccessController;
import com.server.SAXParserExample;
import com.server.AccessRequest;
import com.server.Constants;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
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
import org.xml.sax.InputSource;

/**
 * @author qube26
 */
public class Feedback extends HttpServlet {

    private static final long serialVersionUID = 4900798195594466416L;

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
            if (reqtype.equalsIgnoreCase("SENDFEEDBACK-REQ")) {
                feebback_insertion(list, response);
                return;
            }

        } catch (Exception e) {
            
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
    }

    private void feebback_insertion(List list, HttpServletResponse response) {
        AccessRequest arz = (AccessRequest) list.get(0);
        String userid = arz.getUserId();
        String feedbackdata = arz.getFeedbackText();
        Vector rowdata = new Vector();
        rowdata.add(userid);
        rowdata.add(feedbackdata);
        Date time1 = new Date();
        java.sql.Date sqlDate = new java.sql.Date(time1.getTime());
        java.sql.Time time = new java.sql.Time(time1.getTime());
        rowdata.add(sqlDate);
        rowdata.add(time);

        boolean expirestatus = false;
        String sessionid = arz.getSessionId();
        SessionHandler session = new SessionHandler();
        boolean sessionverify = session.sessionidverify(userid, sessionid);
        if (!sessionverify) {
            feedBack_Xml(response, "400", "You have logged in from another device");
            return;
        }
        int res = DataAccessController.insert_feedback_data("feedback", rowdata);
        if (res == 1) {
            feedBack_Xml(response, "00", "Thank you for feedback.");
            return;
        } else {
            feedBack_Xml(response, "01", "Failed to submit feedback.Please try again...");
            return;
        }
    }

    private void feedBack_Xml(HttpServletResponse p_response, String statusCode, String statusMessage) {
        try {
            PrintWriter out = p_response.getWriter();
            Constants.logger.info("feedBack Response->");
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