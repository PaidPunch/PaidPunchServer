package com.app;

import com.db.DataAccessControler;
import com.jspservlets.signup_paidpunch_add;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.server.Constants;
import java.util.Vector;

/**
 * @author qube26
 */
public class verifyappuser extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ServletConfig config = null;
        try {
            config = getServletConfig();
            ServletContext context = config.getServletContext();
            Constants.loadJDBCConstants(context);
            String userid = request.getParameter("userid");
            int res = DataAccessControler.updatetDataToTable("app_user", "user_id", userid, "isemailverified", "Y");
            Vector userdata = (Vector) DataAccessControler.getDataFromTable("app_user", "user_id", userid).elementAt(0);
            String username = userdata.elementAt(1).toString();
            String email = userdata.elementAt(2).toString();
            String temp = username;
            String namearray[] = temp.split(" ");
            String lastName = "";
            if (namearray.length > 1)
            {
                lastName = namearray[namearray.length - 1];
                char firstChar = lastName.charAt(0);
                // lastName=""+firstChar;
                // namearray[namearray.length-1]=lastName;
            }
            temp = "";
            for (int index = 0; index < namearray.length - 1; index++)
            {
                temp = temp + namearray[index];
                temp = temp + " ";
            }
            temp = temp.trim();
            signup_paidpunch_add emailsender = new signup_paidpunch_add();
            emailsender.sendConfirmationEmail(email, temp);

            System.out.print(res);

            response.sendRedirect("appuserverify.jsp");
        } catch (Exception e)
        {

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

}
