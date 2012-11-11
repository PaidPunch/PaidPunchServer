package com.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.DataAccessController;
import com.jspservlets.SignupAddPunch;
import com.server.Constants;

/**
 * @author qube26
 */
public class VerifyAppUser extends HttpServlet {

    private static final long serialVersionUID = 4252480741460418814L;

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
            int res = DataAccessController.updatetDataToTable("app_user", "user_id", userid, "isemailverified", "Y");
            Vector userdata = (Vector) DataAccessController.getDataFromTable("app_user", "user_id", userid).elementAt(0);
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
            SignupAddPunch emailsender = new SignupAddPunch();
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
