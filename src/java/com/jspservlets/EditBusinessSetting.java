package com.jspservlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * @author admin
 */
public class EditBusinessSetting extends HttpServlet {

    private static final long serialVersionUID = -2572245981349029141L;
    DBConnection db;
    Connection conn = null;
    PreparedStatement pstmt = null;
    Statement st = null;
    ResultSet rs = null;

    HttpSession session = null;

    String businessdesc = "", businessaddress = "", city = "", state = "", pincode = "", countrycode = "",
            contactnumber = "", contactname = "";
    String user_name = "";
    String latitude = "", longitude = "";

    // int timeChanged = 0;

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
    protected void processtimeRequestChange(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        String buttonVal = "";

        try {
            session = request.getSession(false);
        } catch (Exception e) {

        }
        PrintWriter out = response.getWriter();
        try {
            String time_request_change = "", expiredays = "", minvalue = "";
            int timeChanged = 0, expireChanged = 0, minvalChanged = 0;
            try {
                user_name = session.getAttribute("username").toString();
            } catch (Exception e) {

            }
            buttonVal = request.getParameter("buttonval");
            if (buttonVal.equalsIgnoreCase("time")) {
                time_request_change = request.getParameter("tc");
                timeChanged = Integer.parseInt(time_request_change);
                saveTimeRequestChanges(timeChanged);
            } else if (buttonVal.equalsIgnoreCase("expire")) {
                expiredays = request.getParameter("ed");
                expireChanged = Integer.parseInt(expiredays);
                saveExpiredDateChanges(expireChanged);
            } else if (buttonVal.equalsIgnoreCase("minval")) {
                minvalue = request.getParameter("mv");
                minvalChanged = Integer.parseInt(minvalue);
                saveMinimumValueChanges(minvalChanged);
            }
            // time_request_change = request.getParameter("tc");
            response.sendRedirect("business_user_settings.jsp");

        } finally {
            out.close();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            session = request.getSession(false);
        } catch (Exception e) {

        }
        PrintWriter out = response.getWriter();
        try {
            try {
                user_name = session.getAttribute("username").toString();
            } catch (Exception e) {

            }
            businessdesc = request.getParameter("businessdesc");
            businessaddress = request.getParameter("businessaddress");
            city = request.getParameter("city");
            state = request.getParameter("state");
            pincode = request.getParameter("pincode");
            countrycode = request.getParameter("countrycode");
            contactnumber = request.getParameter("contactnumber");
            contactname = request.getParameter("contactname");
            latitude = request.getParameter("latitude");
            longitude = request.getParameter("longitude");

            saveChanges();
            response.sendRedirect("business_user_settings.jsp");

        } finally {
            out.close();
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
        try {
            processtimeRequestChange(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EditBusinessSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EditBusinessSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void saveChanges() throws ServletException, SQLException {
        try {
            int buss_id = 0;
            db = new DBConnection();
            conn = db.con;
            st = db.stmt;
            rs = null;
            try {

                String query = "SELECT business_userid FROM business_users where email_id='" + user_name + "'";
                rs = st.executeQuery(query);
                com.server.Constants.logger.info("The select query is " + query);
                if (rs.next()) {
                    buss_id = rs.getInt(1);
                }

                String UPDATE_RECORD = "Update business_users set buss_desc='" + businessdesc + "', contactno='"
                        + contactnumber + "', contactname='" + contactname + "' where email_id='" + user_name + "';";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                st.executeUpdate(UPDATE_RECORD);

                String UPDATE_RECORD1 = "Update bussiness_address set address_line1='" + businessaddress + "', city='"
                        + city + "', state='" + state + "', country='" + countrycode + "', zipcode='" + pincode
                        + "', latitude='" + latitude + "', longitude='" + longitude + "' where business_id=" + buss_id
                        + ";";
                st.executeUpdate(UPDATE_RECORD1);
            } catch (SQLException e) {
                com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "
                        + e.getMessage());
                throw new ServletException("SQL Exception.", e);
            }
        } catch (Exception e) {
            com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "
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
                com.server.Constants.logger.error("Error in closing SQL in checksecretcode.java" + e.getMessage());
            }
        }
    }

    public void saveTimeRequestChanges(int timeChanged) throws ServletException, SQLException {

        try {
            db = new DBConnection();

            conn = db.con;
            st = db.stmt;
            try {
                String UPDATE_RECORD = "Update punch_card set restriction_time ="
                        + timeChanged
                        + " where business_userid= (Select business_users.business_userid from business_users where business_users.email_id='"
                        + user_name + "');";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                st.executeUpdate(UPDATE_RECORD);
                String UPDATE_RECORD1 = "Update business_users set isemailverified ='N' where business_users.email_id='"
                        + user_name + "';";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                st.executeUpdate(UPDATE_RECORD1);
                String UPDATE_RECORD2 = "Update business_users set isemailverified ='Y' where business_users.email_id='"
                        + user_name + "';";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                st.executeUpdate(UPDATE_RECORD2);
            } catch (SQLException e) {
                com.server.Constants.logger.error("Error in Sql in  in getting QR CODE Value " + e.getMessage());
                throw new ServletException("SQL Exception.", e);
            }
        } catch (Exception e) {
            com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "
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
                com.server.Constants.logger.error("Error in closing SQL in checksecretcode.java" + e.getMessage());
            }
        }

    }

    public void saveExpiredDateChanges(int expiredDateChanged) throws ServletException, SQLException {

        try {
            db = new DBConnection();
            conn = db.con;
            st = db.stmt;
            try {
                String UPDATE_RECORD = "Update punch_card set expirydays ="
                        + expiredDateChanged
                        + " where business_userid= (Select business_users.business_userid from business_users where business_users.email_id='"
                        + user_name + "');";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                st.executeUpdate(UPDATE_RECORD);
                String UPDATE_RECORD1 = "Update business_users set isemailverified ='N' where business_users.email_id='"
                        + user_name + "';";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                st.executeUpdate(UPDATE_RECORD1);
                String UPDATE_RECORD2 = "Update business_users set isemailverified ='Y' where business_users.email_id='"
                        + user_name + "';";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                st.executeUpdate(UPDATE_RECORD2);
            } catch (SQLException e) {
                com.server.Constants.logger.error("Error in Sql in  in getting QR CODE Value " + e.getMessage());
                throw new ServletException("SQL Exception.", e);
            }
        } catch (Exception e) {
            com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "
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
                com.server.Constants.logger.error("Error in closing SQL in checksecretcode.java" + e.getMessage());
            }
        }
    }

    public void saveMinimumValueChanges(int minValueChanged) throws ServletException, SQLException {

        try {
            db = new DBConnection();
            conn = db.con;
            st = db.stmt;
            try {
                String UPDATE_RECORD = "Update punch_card set minimumvalue ="
                        + minValueChanged
                        + " where business_userid= (Select business_users.business_userid from business_users where business_users.email_id='"
                        + user_name + "');";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                st.executeUpdate(UPDATE_RECORD);
                String UPDATE_RECORD1 = "Update business_users set isemailverified ='N' where business_users.email_id='"
                        + user_name + "';";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                st.executeUpdate(UPDATE_RECORD1);
                String UPDATE_RECORD2 = "Update business_users set isemailverified ='Y' where business_users.email_id='"
                        + user_name + "';";
                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                st.executeUpdate(UPDATE_RECORD2);
            } catch (SQLException e) {
                com.server.Constants.logger.error("Error in Sql in  in getting QR CODE Value " + e.getMessage());
                throw new ServletException("SQL Exception.", e);
            }
        } catch (Exception e) {
            com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "
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
                com.server.Constants.logger.error("Error in closing SQL in checksecretcode.java" + e.getMessage());
            }
        }
    }

}