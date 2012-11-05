<%-- 
    Document   : paymentreport
    Created on : Apr 18, 2012, 1:22:23 PM
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

    String username="";

    try{
         username = session.getAttribute("username").toString();
        }catch(Exception e){
        }
        if(username==null||username==""){
             response.sendRedirect("login.jsp");
          }
    else{
  %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
        <style>


        tr.normalRow td {
	background: #FFF;


}

    tr.alternateRow td {
	background: #EEE;


}

tr.alternateRow th {
	background: #EEE;
}


          table {
            border: 0px solid black;
            width: 380px;
            height: 50%;
            font-family: Calibri;
          }

    </style>
    <body>
        <%

            String display = request.getParameter("id");
            if(display.equalsIgnoreCase("00")){
                 %>
                 <h2></h2>
        <%
            }else{
                String email_id = request.getParameter("email");
                String from_date = request.getParameter("from");
                String to_date = request.getParameter("to");

                System.out.println("Old Dates : "+from_date+" "+to_date);
                String from_dt[]=from_date.split("/");
                from_date = from_dt[2]+"/"+from_dt[0]+"/"+from_dt[1];
                String to_dt[]=to_date.split("/");
                to_date = to_dt[2]+"/"+to_dt[0]+"/"+to_dt[1];

                System.out.println("New Dates : "+from_date+" "+to_date);

                float selling_price=0;
                int pid=0;
                long punch_card_downloads = 0, mystery_punch_redeemed = 0;
                float amount_punch_card = 0, amount_mystery = 0, total_amount=0;

                
                DBConnection db = null;
                Statement stmt = null;
                ResultSet rs = null;
                
                try {
                               db = new DBConnection();
                               stmt = db.stmt;

                               try{
                               
                               
                               String query1 = "Select punch_card_id,selling_price_of_punch_card from punch_card where business_userid=(Select business_userid from business_users where email_id='"+email_id+"');";
                               rs = stmt.executeQuery(query1);
                               if(rs.next()){
                                     pid = rs.getInt(1);
                                     selling_price = rs.getFloat(2);
                               }
                               }catch(SQLException e){
                                com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                throw new ServletException("SQL Exception.", e);
                            }

                            try{


                               String query1 = "SELECT COUNT(*) FROM punchcard_download where punch_card_id="+pid+" and isfreepunch='false' and download_date between '"+from_date+"' and '"+to_date+"';";
                               System.out.println(query1);
                               rs = stmt.executeQuery(query1);
                               if(rs.next()){
                                    punch_card_downloads = rs.getLong(1);
                               }
                               }catch(SQLException e){
                                com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                throw new ServletException("SQL Exception.", e);
                            }

                              /* try{


                               String query1 = "SELECT COUNT(*) FROM punchcard_download where mystery_punchid=(SELECT mystery_id FROM mystery_punch where punch_card_id="+pid+" and paidpunchmystery='Y') and download_date between '"+from_date+"' and '"+to_date+"';";
                               System.out.println(query1);
                               rs = stmt.executeQuery(query1);
                               if(rs.next()){
                                    mystery_punch_redeemed = rs.getLong(1);
                               }
                               }catch(SQLException e){
                                com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                throw new ServletException("SQL Exception.", e);
                            }*/

                             amount_punch_card =  punch_card_downloads*selling_price;
                            // amount_mystery = mystery_punch_redeemed * 20;
                           //  total_amount = amount_punch_card+amount_mystery;
                             total_amount = amount_punch_card;
                            
                        }catch (Exception e) {
                            com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
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
%>

        <table id="menuTable" border="0" cellspacing="0" cellpadding="0.5">
            <tr class="alternateRow">
                <th>Transaction<br>Type</th>
                <th>No of <br>Downloads</th>
                <th>Value of each<br>card/Mystery</th>
                <th>Amount</th>
            </tr>
            
            <tr align="center" class="normalRow" >
                <td>Punch Card</td>
                <td><%=punch_card_downloads%></td>
                <td><%=selling_price%></td>
                <td><b>$</b><%=amount_punch_card%></td>
            </tr>
            <%--<tr align="center" class="normalRow">
                <td>Jackpot Mystery</td>
                <td><%=mystery_punch_redeemed%></td>
                <td>20</td>
                <td><b>$</b><%=amount_mystery%></td>
            </tr>--%>
            <tr class="alternateRow"><td colspan="4"><br></td></tr>
            <tr align="center" class="alternateRow">
                <td></td>
                <td></td>
                <td style="font-weight: bold">Total</td>
                <td><div style="border:1px solid #963;"><b>$<%=total_amount%></b></div></td>
            </tr>

        </table>
<%
            }
%>
    </body>
</html>
<%
    }
%>