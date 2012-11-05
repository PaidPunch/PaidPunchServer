/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jspservlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.*;
import java.io.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Shahid
 */
public class DisplayImage extends HttpServlet {

    ServletConfig config = null;
          ServletContext context;
          HttpSession session=null;


    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        try{
           session = request.getSession(false);

           config = getServletConfig();
           context = config.getServletContext();
       }catch(Exception e){

       }
        com.server.Constants.loadJDBCConstants(context);
        int business_userID=0;

        try{
            business_userID = Integer.parseInt(request.getParameter("bussid"));
        }catch(Exception e){

        }

        DBConnection db = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                        db = new DBConnection();
                        stmt = db.stmt;
                        String query="select bussiness_logo from business_users where business_userid="+business_userID;
                        rs = stmt.executeQuery(query);
                          String imgLen="";
                          if(rs.next()){
                              imgLen = rs.getString(1);
                              System.out.println(imgLen.length());
                          }

                          rs = stmt.executeQuery(query);
                          if(rs.next()){
                            int len = imgLen.length();
                            byte [] rb = new byte[len];
                            InputStream readImg = rs.getBinaryStream(1);
                            int index=readImg.read(rb, 0, len);
                            System.out.println("index"+index);
                            stmt.close();
                              response.reset();
                              response.setContentType("image/jpg");
                              response.getOutputStream().write(rb,0,len);
                              response.getOutputStream().flush();
                            }
                        }catch (SQLException e) {
                            com.server.Constants.logger.error("Error in Sql in checksecretcode.java in getsecretcode "+e.getMessage());
                          throw new ServletException("SQL Exception.", e);
                      } finally {
                      try {
                          if(rs != null) {
                              rs.close();
                              //Constants.logger.info("Closing rs Statement ");
                              rs = null;
                          }
                          db.closeConnection();

                      } catch (SQLException e) {
                            com.server.Constants.logger.error("Error in closing SQL in checksecretcode.java"+e.getMessage());
                      }
                   }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}



