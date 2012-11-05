<%-- 
    Document   : state
    Created on : Mar 28, 2012, 5:43:01 PM
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

    String country=request.getParameter("count");

    com.server.Constants.logger.info("Country Passed : "+country);
    //    System.out.println("Country Passed : "+country);
 String buffer="<select name=\"state\" id=\"state\" onchange=\"showCity(this.value);\" ><option value=\"-1\">Select</option>";

    try {
             db = new DBConnection();
             stmt = db.stmt;
                  try{
                      String query1 = "Select distinct District from city where CountryCode='"+country+"' order by District";
                      rs = stmt.executeQuery(query1);
                      while(rs.next()){
                        buffer=buffer+"<option value=\""+rs.getString(1)+"\">"+rs.getString(1)+"</option>";
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
 System.out.println("buffer Passed : "+buffer);
  response.getWriter().println(buffer);
 }catch(Exception e){
     System.out.println(e);
 }
/*

 try{
 Class.forName("com.mysql.jdbc.Driver").newInstance();
 Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/paidpunch","root","root");
 Statement stmt = con.createStatement();
 ResultSet rs = stmt.executeQuery("Select distinct District from city where CountryCode='"+country+"' order by District");



 catch(Exception e){
     System.out.println(e);
 }
*/
 %>