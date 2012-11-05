<%-- 
    Document   : city
    Created on : Mar 28, 2012, 4:31:07 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.server.Constants"%>
<%@page import="com.jspservlets.DBConnection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.mysql.jdbc.Statement"%>

<%
  try{

    DBConnection db = null;
    Statement stmt = null;
    ResultSet rs = null;

    String state=request.getParameter("count");
    String buffer="<select name='city' id='city'><option value='-1'>Select</option>";
    com.server.Constants.logger.info("State Passed : "+state);
    try {
             db = new DBConnection();
             stmt = db.stmt;
                  try{

                      String query1 = "Select Name from city where District='"+state+"' order by Name";
                      rs = stmt.executeQuery(query1);
                      while(rs.next()){
                            buffer=buffer+"<option value='"+rs.getString(1)+"'>"+rs.getString(1)+"</option>";
                      }
                      }catch(SQLException e){
                                com.server.Constants.logger.error("Error in Sql in state.jsp in  "+e.getMessage());
                                throw new ServletException("SQL Exception.", e);
                     }

                }catch (Exception e) {
                            com.server.Constants.logger.error("Error in Sql in state.jsp "+e.getMessage());
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
                     com.server.Constants.logger.error("Error in closing SQL in state.jsp"+e.getMessage());
                }
            }

            buffer=buffer+"</select>";
            response.getWriter().println(buffer);
            }
     catch(Exception e){
     System.out.println(e);
 }

 /*
 Class.forName("com.mysql.jdbc.Driver").newInstance();
 Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/paidpunch","root","root");
 Statement stmt = con.createStatement();
 ResultSet rs = stmt.executeQuery("Select Name from city where District='"+state+"' order by Name");
   while(rs.next()){
    buffer=buffer+"<option value='"+rs.getString(1)+"'>"+rs.getString(1)+"</option>";
   }
 buffer=buffer+"</select>";
 response.getWriter().println(buffer);
 */
 %>