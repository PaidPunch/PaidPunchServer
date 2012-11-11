package com.jspservlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.*;

import java.util.*;

import javax.servlet.*;

import javax.servlet.http.*;

/**
 * @author Shahid
 */
public class ChangePassword extends HttpServlet {
    ServletConfig config = null;
    ServletContext context;
    HttpSession session = null;

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
             * out.println("<title>Servlet change_password</title>"); out.println("</head>"); out.println("<body>");
             * out.println("<h1>Servlet change_password at " + request.getContextPath () + "</h1>");
             * out.println("</body>"); out.println("</html>");
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
        System.out.println("doPost ");
        try {
            session = request.getSession(false);

            config = getServletConfig();
            context = config.getServletContext();
        } catch (Exception e) {

        }
        com.server.Constants.loadJDBCConstants(context);
        String username = "", oldpassword = "", newpassword = "", code = "00";
        try {
            username = session.getAttribute("username").toString();

            if (username == null || username == "") {
                response.sendRedirect("login.jsp");
            }

        } catch (Exception e) {

        }

        oldpassword = request.getParameter("oldpassword");
        newpassword = request.getParameter("newpassword");
        System.out.println("Old Password : " + oldpassword + "New Password : " + newpassword);
        PrintWriter out = response.getWriter();
        if (oldpassword != null && newpassword != null) {

            code = changePassword(username, oldpassword, newpassword);

        }
        else {

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

    public String changePassword(String username, String oldpass, String newpass) throws ServletException {
        DBConnection db = null;
        Statement stmt = null;
        ResultSet rs = null;
        String codeFlag = "00";

        try {
            db = new DBConnection();
            stmt = db.stmt;
            String query = "SELECT password from business_users where email_id='" + username + "'";
            rs = stmt.executeQuery(query);
            com.server.Constants.logger.info("The select query is " + query);
            // displaying records
            String userpassword = "";
            if (rs.next()) {
                userpassword = rs.getObject(1).toString();
                if (userpassword.equals(oldpass)) {
                    String updatePasswordQuery = "Update business_users set password = '" + newpass
                            + "' where email_id ='" + username + "'";
                    com.server.Constants.logger.info("The query is " + updatePasswordQuery);
                    System.out.println("The query is " + updatePasswordQuery);
                    stmt.executeUpdate(updatePasswordQuery);

                    codeFlag = "02";
                }
                else {
                    codeFlag = "01";
                }
            }
            else {
                codeFlag = "00";
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
        return codeFlag;
    }

}