package com.jspservlets;

import java.sql.Time;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Shahid
 */
public class save_delete_business_admin extends HttpServlet {

    ServletConfig config = null;
    ServletContext context;
    HttpSession session = null;
    java.sql.Date sqlDate = null;

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
        try {
            /*
             * TODO output your page here out.println("<html>"); out.println("<head>");
             * out.println("<title>Servlet save_delete_business_admin</title>"); out.println("</head>");
             * out.println("<body>"); out.println("<h1>Servlet save_delete_business_admin at " + request.getContextPath
             * () + "</h1>"); out.println("</body>"); out.println("</html>");
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

        try {
            config = getServletConfig();

            context = config.getServletContext();
        } catch (Exception e) {

        }
        com.server.Constants.loadJDBCConstants(context);
        String code = "00";
        String businessId = "", expiryDate = "";
        try {
            businessId = request.getParameter("busid");
            expiryDate = request.getParameter("expirydt");
        } catch (Exception e) {

        }
        PrintWriter out = response.getWriter();
        if (businessId != null && expiryDate != null) {
            code = updateExpiryDate(businessId, expiryDate);
        }

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
        try {
            config = getServletConfig();

            context = config.getServletContext();
        } catch (Exception e) {

        }
        com.server.Constants.loadJDBCConstants(context);
        String code = "03";
        String businessId = "";
        String transType = "";
        try {
            businessId = request.getParameter("busid");
            transType = request.getParameter("transtype");

        } catch (Exception e) {

        }
        PrintWriter out = response.getWriter();
        if (businessId != null) {
            if (transType.equalsIgnoreCase("00"))
                code = deleteBusiness(businessId);
            else if (transType.equalsIgnoreCase("01"))
                code = enableBusiness(businessId);
        }
        out.println(code);
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

    public String updateExpiryDate(String businessid, String expdate) throws ServletException {
        DBConnection db = null;
        Statement stmt = null;
        ResultSet rs = null;
        String codeFlag = "00";

        expdate = expdate.replace("-", "/");
        System.out.println("Expiry Date" + expdate);
        String str1[] = expdate.split("/");
        expdate = str1[2] + "-" + str1[1] + "-" + str1[0];
        System.out.println("expiryDate" + expdate);

        int busid = Integer.parseInt(businessid);
        try {
            db = new DBConnection();
            stmt = db.stmt;

            String updatePasswordQuery = "Update punch_card set expiry_date = '" + java.sql.Date.valueOf(expdate)
                    + "' where business_userid =" + busid;
            com.server.Constants.logger.info("The query is " + updatePasswordQuery);
            System.out.println("The query is " + updatePasswordQuery);
            stmt.executeUpdate(updatePasswordQuery);
            codeFlag = "01";
        } catch (SQLException e) {
            com.server.Constants.logger.error("Error in Sql in savedeletebusiness.java in updateExpiryDate "
                    + e.getMessage());
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
                com.server.Constants.logger.error("Error in Sql in savedeletebusiness.java in updateExpiryDate"
                        + e.getMessage());
            }
        }
        return codeFlag;
    }

    public String deleteBusiness(String businessid) throws ServletException {
        DBConnection db = null;
        Statement stmt = null;
        ResultSet rs = null;
        String codeFlag = "03";

        Date time1 = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(time1);
        // int hour= cal1.HOUR;
        // cal1.add(Calendar.YEAR,1);
        cal1.add(Calendar.DATE, -1);
        Date date = cal1.getTime();
        System.out.println("" + time1);
        sqlDate = new java.sql.Date(date.getTime());
        System.out.println("<br>" + sqlDate);

        int busid = Integer.parseInt(businessid);
        try {
            db = new DBConnection();
            stmt = db.stmt;

            String updatePasswordQuery = "Update business_users set busi_enabled = 'N' where business_userid =" + busid;
            // String updatePasswordQuery = "delete from business_users where business_userid = "+busid;
            // String updatePasswordQuery = "Update punch_card set expiry_date = '" + sqlDate +
            // "' where business_userid ="+busid;
            com.server.Constants.logger.info("The query is " + updatePasswordQuery);
            System.out.println("The query is " + updatePasswordQuery);
            stmt.executeUpdate(updatePasswordQuery);
            codeFlag = "04";

        } catch (SQLException e) {
            System.out.println("Error" + e);
            com.server.Constants.logger.error("Error in Sql in savedeletebusiness.java in deletBusiness "
                    + e.getMessage());
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
                com.server.Constants.logger.error("Error in Sql in savedeletebusiness.java in deletBusiness"
                        + e.getMessage());
            }
        }
        return codeFlag;
    }

    public String enableBusiness(String businessid) throws ServletException {
        DBConnection db = null;
        Statement stmt = null;
        ResultSet rs = null;
        String codeFlag = "03";

        Date time1 = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(time1);
        // int hour= cal1.HOUR;
        // cal1.add(Calendar.YEAR,1);
        cal1.add(Calendar.DATE, -1);
        Date date = cal1.getTime();
        System.out.println("" + time1);
        sqlDate = new java.sql.Date(date.getTime());
        System.out.println("<br>" + sqlDate);

        int busid = Integer.parseInt(businessid);
        try {
            db = new DBConnection();
            stmt = db.stmt;

            String updatePasswordQuery = "Update business_users set busi_enabled = 'Y' where business_userid =" + busid;
            // String updatePasswordQuery = "delete from business_users where business_userid = "+busid;
            // String updatePasswordQuery = "Update punch_card set expiry_date = '" + sqlDate +
            // "' where business_userid ="+busid;
            com.server.Constants.logger.info("The query is " + updatePasswordQuery);
            System.out.println("The query is " + updatePasswordQuery);
            stmt.executeUpdate(updatePasswordQuery);
            codeFlag = "04";

        } catch (SQLException e) {
            System.out.println("Error" + e);
            com.server.Constants.logger.error("Error in Sql in savedeletebusiness.java in deletBusiness "
                    + e.getMessage());
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
                com.server.Constants.logger.error("Error in Sql in savedeletebusiness.java in deletBusiness"
                        + e.getMessage());
            }
        }
        return codeFlag;
    }

}