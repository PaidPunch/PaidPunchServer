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
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
/**
 *
 * @author admin
 */
public class edit_mystery_punch extends HttpServlet {

    ServletConfig config = null;
    ServletContext context;
    
    DBConnection db;
    Connection conn = null;
    PreparedStatement pstmt=null;
    Statement stmt =  null;
    ResultSet rs = null;

    HttpSession session=null;

    String username = "";
    int busid=0, punch_card_id=0;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            session = request.getSession(false);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
           config = getServletConfig();
           context = config.getServletContext();
       }catch(Exception e){
        e.printStackTrace();
       }
        com.server.Constants.loadJDBCConstants(context);
        PrintWriter out = response.getWriter();
        try{

                try{
                 username = session.getAttribute("username").toString();
                }catch(Exception e){
                    e.printStackTrace();
                }
                db = new DBConnection();
                conn = db.con;
                stmt =  db.stmt;
                rs = null;
                try{
                  String query = "Select punch_card.punch_card_id,punch_card.business_userid from punch_card,business_users where punch_card.business_userid=business_users.business_userid and business_users.email_id='"+username+"'";
                  rs = stmt.executeQuery(query);
                  com.server.Constants.logger.info("The select query is " + query);


                      if(rs.next()){
                        punch_card_id = rs.getInt(1);
                        busid = rs.getInt(2);
                     }
                }catch(SQLException e){
                                com.server.Constants.logger.error("Error in Sql in mystery edit.jsp "+e.getMessage());
                                throw new ServletException("SQL Exception.", e);
                }

           int no_of_mystery_punches = Integer.parseInt(request.getParameter("counterval"));

            no_of_mystery_punches = no_of_mystery_punches-1;


            long valid_mystery_punches = 0;

            try{
                  String query = "Select count(*) from mystery_punch where punch_card_id = "+punch_card_id+" and mysterypunchvalid='Y'";
                  rs = stmt.executeQuery(query);
                  com.server.Constants.logger.info("The select query is " + query);


                      if(rs.next()){
                        valid_mystery_punches = rs.getLong(1);
                      }

                }catch(SQLException e){
                                com.server.Constants.logger.error("Error in Sql in mystery edit.jsp "+e.getMessage());
                                throw new ServletException("SQL Exception.", e);
                }

            String mystery_punch_values[] = new String[no_of_mystery_punches];
            int mystery_punch_probability[] = new int[no_of_mystery_punches];
            String isPaidPunchMystery[] = new String[no_of_mystery_punches];
            int mysteryid[] = new int[no_of_mystery_punches];
            String isvalidmystery[] = new String[no_of_mystery_punches];
            
            for(int i=0; i<no_of_mystery_punches;i++){
                mysteryid[i] = Integer.parseInt(request.getParameter("mysteryid"+(i+1)));
                mystery_punch_values[i] = request.getParameter("mysteryvalue"+(i+1));
                mystery_punch_probability[i] = Integer.parseInt(request.getParameter("probability"+(i+1)));
                isPaidPunchMystery[i] = request.getParameter("ispaidpunchmystery"+(i+1));
                isvalidmystery[i] = request.getParameter("isvalidmystery"+(i+1));
                System.out.println(""+request.getParameter("mysteryid"+(i+1)));
                System.out.println("MysteryValue"+i+" : "+mystery_punch_values[i]);
                System.out.println("Probability Value"+i+" : "+mystery_punch_probability[i]);
                System.out.println("Is PaidPunch Mystery "+i+" : "+isPaidPunchMystery[i]);
                System.out.println("mysteryid"+i+" : "+mysteryid[i]);
                System.out.println("Is Valid Mystery "+i+" : "+isvalidmystery[i]);
            }

            updateMysteryPunches(punch_card_id,mysteryid,mystery_punch_values,mystery_punch_probability,isvalidmystery,isPaidPunchMystery,no_of_mystery_punches,(int)valid_mystery_punches);

       //     deletemysteryPunches(punch_card_id);

            //insertmysteryPunches(punch_card_id,mystery_punch_values,mystery_punch_probability,no_of_mystery_punches,isPaidPunchMystery);


            response.sendRedirect("business_user_settings.jsp");
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet edit_mystery_punch</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet edit_mystery_punch at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            out.close();
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


    public void updateMysteryPunches(int punchcardid,int mysteryId[],String mysteryValues[], int mysteryProb[],String isValidMystery[] ,String isMysteryPunch[],int no_of_punches_passed,int no_of_valid_punchesdb) throws ServletException, SQLException{

        if(no_of_punches_passed>=no_of_valid_punchesdb){
            //update and insert
            try{
                db = new DBConnection();
                conn = db.con;
                stmt =  db.stmt;
                for(int i=0; i<no_of_valid_punchesdb; i++){
                    String updateMysteryQuery = "Update mystery_punch set value_of_myst_punch='"+mysteryValues[i]+"', probability="+mysteryProb[i]+", paidpunchmystery='"+isMysteryPunch[i]+"', mysterypunchvalid='"+isValidMystery[i]+"' where mystery_id="+mysteryId[i]+";";
                    com.server.Constants.logger.info("The query is "+updateMysteryQuery);
                     System.out.println("The query is "+updateMysteryQuery);
                     stmt.executeUpdate(updateMysteryQuery);
                }
                for(int j=no_of_valid_punchesdb; j<no_of_punches_passed;j++){
                    String INSERT_RECORD = "Insert into mystery_punch(punch_card_id, value_of_myst_punch,probability, paidpunchmystery,mysterypunchvalid) values( ?, ?, ?, ?, ?)";
                    //Constants.logger.info("Insert Query is "+INSERT_RECORD);
                    com.server.Constants.logger.info("The query is "+INSERT_RECORD);
                    pstmt = (PreparedStatement) conn.prepareStatement(INSERT_RECORD);
                    pstmt.setInt(1, punchcardid);
                    pstmt.setString(2, mysteryValues[j]);
                    pstmt.setInt(3,  mysteryProb[j]);
                    pstmt.setString(4, isMysteryPunch[j]);
                     pstmt.setString(5, isValidMystery[j]);
                    pstmt.executeUpdate();
                }
            }catch(SQLException sqle){
                    //Constants.logger.error("Error in Sql in AppDistributor Hits"+sqle.getMessage());
                throw new ServletException("SQL Exception.", sqle);
            } finally{
                    try{
                        if(pstmt != null) {
                              pstmt.close();
  //                            Constants.logger.info("Closing Prepared Statement ");
                              pstmt = null;
                          }

                        if(rs != null) {
                              rs.close();
    //                          Constants.logger.info("Closing rs Statement ");
                              rs = null;
                          }
                          db.closeConnection();
                      } catch (SQLException e) {
                            //Constants.logger.error("Error in Sql in App Distributor Hits"+e.getMessage());
                      }
            }
        }else{
            //update and delete
            try{
                db = new DBConnection();
                conn = db.con;
                stmt =  db.stmt;

                String diableMysteryQuery = "Update mystery_punch set mysterypunchvalid='N' where punch_card_id="+punchcardid+";";
                com.server.Constants.logger.info("The query is "+diableMysteryQuery);
                System.out.println("The query is "+diableMysteryQuery);
                stmt.executeUpdate(diableMysteryQuery);

                for(int i=0; i<no_of_punches_passed; i++){
                    String updateMysteryQuery = "Update mystery_punch set value_of_myst_punch='"+mysteryValues[i]+"', probability="+mysteryProb[i]+", paidpunchmystery='"+isMysteryPunch[i]+"', mysterypunchvalid='"+isValidMystery[i]+"' where mystery_id="+mysteryId[i]+";";
                    com.server.Constants.logger.info("The query is "+updateMysteryQuery);
                     System.out.println("The query is "+updateMysteryQuery);
                     stmt.executeUpdate(updateMysteryQuery);
                }
                }catch(SQLException sqle){
                    //Constants.logger.error("Error in Sql in AppDistributor Hits"+sqle.getMessage());
                throw new ServletException("SQL Exception.", sqle);
            } finally{
                    try{
                        if(pstmt != null) {
                              pstmt.close();
  //                            Constants.logger.info("Closing Prepared Statement ");
                              pstmt = null;
                          }

                        if(rs != null) {
                              rs.close();
    //                          Constants.logger.info("Closing rs Statement ");
                              rs = null;
                          }
                          db.closeConnection();
                      } catch (SQLException e) {
                            //Constants.logger.error("Error in Sql in App Distributor Hits"+e.getMessage());
                      }
            }

        }


    }



    public void insertmysteryPunches (int punchcardid, int mysteryId[], String mysteryValues[], int mysteryProb[],String isValidMystery[] , int no_of_mysteries, String isMysteryPunch[]) throws ServletException, SQLException{


            try{
            db = new DBConnection();
            conn = db.con;
            stmt =  db.stmt;
                for(int i=0; i<no_of_mysteries; i++){
                        String INSERT_RECORD = "Insert into mystery_punch(punch_card_id, value_of_myst_punch,probability, paidpunchmystery) values( ?, ?, ?, ?)";
                        //Constants.logger.info("Insert Query is "+INSERT_RECORD);
                        com.server.Constants.logger.info("The query is "+INSERT_RECORD);
                        pstmt = (PreparedStatement) conn.prepareStatement(INSERT_RECORD);
                        pstmt.setInt(1, punchcardid);
                        pstmt.setString(2, mysteryValues[i]);
                        pstmt.setInt(3,  mysteryProb[i]);
                        pstmt.setString(4, isMysteryPunch[i]);
                        pstmt.executeUpdate();

                }


            }catch(SQLException sqle){
                    //Constants.logger.error("Error in Sql in AppDistributor Hits"+sqle.getMessage());
                throw new ServletException("SQL Exception.", sqle);
            } finally{
                    try{
                        if(pstmt != null) {
                              pstmt.close();
  //                            Constants.logger.info("Closing Prepared Statement ");
                              pstmt = null;
                          }

                        if(rs != null) {
                              rs.close();
    //                          Constants.logger.info("Closing rs Statement ");
                              rs = null;
                          }
                          db.closeConnection();
                      } catch (SQLException e) {
                            //Constants.logger.error("Error in Sql in App Distributor Hits"+e.getMessage());
                      }
            }

       }

    public void deletemysteryPunches(int punch_card_id) throws ServletException{
         try {
                     db = new DBConnection();
                     stmt = db.stmt;

                     String deleteMysteryQuery = "delete from mystery_punch where punch_card_id = "+punch_card_id;
                     com.server.Constants.logger.info("The query is "+deleteMysteryQuery);
                     System.out.println("The query is "+deleteMysteryQuery);
                     stmt.executeUpdate(deleteMysteryQuery);


            }catch (SQLException e) {
                    System.out.println("Error"+e);
                  com.server.Constants.logger.error("Error in Sql in savedeletebusiness.java in deletBusiness "+e.getMessage());
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
                            com.server.Constants.logger.error("Error in Sql in savedeletebusiness.java in deletBusiness"+e.getMessage());
                      }
                   }
    }


}
