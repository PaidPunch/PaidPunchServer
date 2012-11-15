package com.jspservlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
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
public class CheckSecretCode extends HttpServlet {

    private static final long serialVersionUID = 3898559334493263827L;
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

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {

    }

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
             * out.println("<title>Servlet getname</title>"); out.println("</head>"); out.println("<body>");
             * out.println("<h1>Servlet getname at " + request.getContextPath () + "</h1>"); out.println("</body>");
             * out.println("</html>");
             */
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
        session = request.getSession(true);
        session.setMaxInactiveInterval(3600);
        try {
            config = getServletConfig();
            context = config.getServletContext();
        } catch (Exception e) {

        }
        com.server.Constants.loadJDBCConstants(context);
        String secretcode = "";
        PrintWriter out = response.getWriter();
        secretcode = generateSecretCode();
        out.println(secretcode);
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
        session = request.getSession(true);
        try {
            config = getServletConfig();
            context = config.getServletContext();
        } catch (Exception e) {

        }
        com.server.Constants.loadJDBCConstants(context);
        String secretcode = "", code = "";
        PrintWriter out = response.getWriter();
        if (request.getParameter("secretcode") != null) {
            secretcode = request.getParameter("secretcode");

            boolean secretcodeflag = getSecretCode(secretcode);
            if (secretcodeflag) {
                code = "00";
                session.setAttribute("secretcode", secretcode);
            }
            else {
                code = "01";
            }
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
    }
    
    public boolean getSecretCode(String secretcode) throws ServletException {
        DBConnection db = null;
        Statement stmt = null;
        ResultSet rs = null;
        boolean codeFlag = false;

        try {
            db = new DBConnection();
            stmt = db.stmt;
            String query = "SELECT * from secretcode where code_value='" + secretcode + "'";
            rs = stmt.executeQuery(query);
            com.server.Constants.logger.info("The select query is " + query);
            // displaying records
            String codeusedflag = "";
            if (rs.next()) {
                codeusedflag = rs.getObject(3).toString();
                if (codeusedflag.equalsIgnoreCase("N"))
                    codeFlag = true;
                else
                    codeFlag = false;
            }
            else {
                codeFlag = false;
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

    public String generateSecretCode() throws ServletException {
        DBConnection db = null;
        Statement st = null;
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        boolean codeFlag = false;
        String secretcode = "";
        boolean fl = true;
        try {
            db = new DBConnection();
            conn = db.con;
            st = db.stmt;
            String INSERT_RECORD = "Insert into secretcode(code_value, code_used) values(?, ?)";
            long aStart = 10000;
            long aEnd = 99999;
            while (true) {

                Random generator = new Random();
                generator.setSeed(System.currentTimeMillis());
                if (aStart > aEnd) {
                    throw new IllegalArgumentException("Start cannot exceed End.");
                }
                // get the range, casting to long to avoid overflow problems
                long range = (long) aEnd - (long) aStart + 1;
                // compute a fraction of the range, 0 <= frac < range
                long fraction = (long) (range * generator.nextDouble());
                long randomNumber = (long) (fraction + aStart);
                System.out.println("Generated : " + randomNumber);
                secretcode = "" + randomNumber;

                // Constants.logger.info("Insert Query is "+INSERT_RECORD);
                pstmt = (PreparedStatement) conn.prepareStatement(INSERT_RECORD);
                pstmt.setString(1, secretcode);
                pstmt.setString(2, "N");
                if (pstmt.executeUpdate() == 0)
                    continue;
                else
                    break;
            }

        } catch (SQLException sqle) {
            // Constants.logger.error("Error in Sql in AppDistributor Hits"+sqle.getMessage());
            throw new ServletException("SQL Exception.", sqle);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                    // Constants.logger.info("Closing Prepared Statement ");
                    pstmt = null;
                }

                if (rs != null) {
                    rs.close();
                    // Constants.logger.info("Closing rs Statement ");
                    rs = null;
                }
                db.closeConnection();
            } catch (SQLException e) {
                // Constants.logger.error("Error in Sql in App Distributor Hits"+e.getMessage());
            }
        }
        return secretcode;
    }

}