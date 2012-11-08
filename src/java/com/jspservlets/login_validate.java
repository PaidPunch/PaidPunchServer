package com.jspservlets;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author admin
 */
public class login_validate extends HttpServlet {

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
             * out.println("<title>Servlet login_validate</title>"); out.println("</head>"); out.println("<body>");
             * out.println("<h1>Servlet login_validate at " + request.getContextPath () + "</h1>");
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

        session = request.getSession(true);
        String code = "";
        String email_id = "";
        try {
            code = request.getParameter("status");
            System.out.println("Status Code : " + code);
            email_id = request.getParameter("email");
            session.setAttribute("username", email_id);
            // password = request.getParameter("password");
        } catch (Exception e) {

        }
        PrintWriter out = response.getWriter();
        if (email_id != null) {
            if (code.equalsIgnoreCase("6")) {
                response.sendRedirect("business_admin_user.jsp");
            }
            else if (code.equalsIgnoreCase("7")) {
                response.sendRedirect("business_user_report.jsp");
            }
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
        // processRequest(request, response);
        try {
            config = getServletConfig();

            context = config.getServletContext();
        } catch (Exception e) {

        }
        com.server.Constants.loadJDBCConstants(context);
        String code = "03";
        String email_id = "", password = "";
        try {
            email_id = request.getParameter("email");
            password = request.getParameter("password");
        } catch (Exception e) {

        }
        PrintWriter out = response.getWriter();
        if (email_id != null && password != null) {
            code = getUserNamePassword(email_id, password);
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

    public String getUserNamePassword(String email_id, String password) throws ServletException {
        DBConnection db = null;
        Statement stmt = null;
        ResultSet rs = null;
        String code = "03";
        String passwd = "", role = "";
        String isUserValidated = "N";
        try {
            db = new DBConnection();
            stmt = db.stmt;
            String query = "SELECT email_id,password,role,isemailverified from business_users where email_id='"
                    + email_id + "'";
            rs = stmt.executeQuery(query);
            com.server.Constants.logger.info("The select query is " + query);
            // displaying records

            if (rs.next()) {
                // username = rs.getObject(1).toString();
                passwd = rs.getObject(2).toString();
                role = rs.getObject(3).toString();
                isUserValidated = rs.getObject(4).toString();
                if (password.equalsIgnoreCase(passwd)) {
                    if (isUserValidated.equalsIgnoreCase("Y")) {
                        if (role.equalsIgnoreCase("user")) {
                            code = "07";
                        }
                        else if (role.equalsIgnoreCase("admin")) {
                            code = "06";
                        }
                    } else {
                        code = "02";
                    }
                }
                else {
                    code = "05";
                }
            }
            else {
                code = "04";
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
        return code;
    }

}