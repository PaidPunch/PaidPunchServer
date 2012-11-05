<%--
    Document   : business_admin_user
    Created on : Dec 13, 2011, 5:47:02 PM
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
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link href='images/paidpunchlogo.png' rel='icon' type='image/png'/>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>PaidPunch</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link media="screen" rel="stylesheet" type="text/css">



        
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}


form {
}

fieldset {
	border: none;
}

legend {
	display: none;
}

h1, h2, h3 {
	margin: 0;
	color: #828285;
}

h2 {
	margin-bottom: .5em;
	font-size: 25px;
}

h3 {
	margin-bottom: 1em;
	font-size: 1em;
}


a {

	color: #EA672E;

}

a:hover {
	text-decoration: none;
	color: #11A6D4;
}

img {
	border: none;
}

/* Boxed Style */

.boxed {

	margin-top:10px;
	margin-bottom: 20px;
	background: #F7F7F7 ;
	border: 1px solid #CCCCCC;
	width:800px;
        font-family: Calibri;
       

}

.boxed .title {
	height: 20px;
	padding: 7px 0 0 7px;
	background: url(images/orange_gradient.png) repeat-x 10px 50%;
	text-transform: uppercase;
	font-size: 18px;
	color: #FFFFFF;
	text-align: left;
}



/* Header */



/* Header > Logo */

#logo {

	text-align:center;
	padding: 10px 20px 0 0;
}

#logo h1 {
	text-transform: uppercase;
	font-size: 2em;
}

#logo h2 {
	margin: 0;
	text-transform: uppercase;
	font-size: 1.2em;
}

#logo a {
	text-decoration: none;
	color: #FFFFFF;

}

/* Content */

#content {
	width: 750px;
	margin: 0 auto;
	padding: 15px 0;

}


/* Content > Sidebar > Login */

#login {
}

#login a {
	font-size: 1.5em;
	color: #EA672E;

}

#login a:hover {
	text-decoration: none;
	color: #11A6D4;
}


#label {
        margin-left: 5px;
	font: normal medium Calibri;
        font-size:15px;
        color : #323232;
        font-weight: bold;

}

#label2 {

	margin-bottom: .5em;

	font: normal medium Calibri;
        font-size:15px;
        color : #323232;
        font-weight: bold;


}


#label1 {

        margin-right: 5px;
	font: normal medium Calibri;
        font-size:15px;
        color : #F47B27;
        font-weight: bold;

}

#label3 {

	margin-bottom: .5em;
        margin-right: 5px;
        margin-left: 10px;
	font: normal medium Calibri;
        font-size:15px;
        color : #F47B27;
        font-weight: bold;

}

#login #inputtext1{
	width: 180px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
        font-weight: bold;

}






#inputsubmit1, #inputsubmit2, #inputsubmit3 {
	width : 67px;
	font-size: 15px;
        color : #ffffff;
        margin-right: 25px;
	font-family: calibri;
        font-weight: bold;
        background: url(images/button_base.png) repeat;
}


#login p {
	margin: 0;
	padding-top: .2em;
	font-size: x-small;
	float:right;
}

/* Footer */

#footer {
        height: 25px;
	position:fixed;
       bottom:0;
   width:100%;
   background: #F47B27 url(images/orange_gradient.png) repeat-x 0 0;

}

div.tableContainer {
        margin: 10px;
	clear: both;
	border: 0px solid #963;
	height: 360px;
	overflow: auto;
	width:780px;
}



/* set table header to a fixed position. WinIE 6.x only                                       */
/* In WinIE 6.x, any element with a position property set to relative and is a child of       */
/* an element that has an overflow property set, the relative value translates into fixed.    */
/* Ex: parent element DIV with a class of tableContainer has an overflow property set to auto */
thead.fixedHeader tr {
	position: relative
}


/* make the TH elements pretty */
thead.fixedHeader th {

	border-left: 0px solid #EB8;
	border-right: 0px solid #B74;
	border-top: 0px solid #EB8;
	font-weight: bold;
	padding: 2px 3px 3px 4px;
	
        color:#F47B27;
        font-size: 16px;
        font-family: Calibri;
}

/* make the A elements pretty. makes for nice clickable headers                */
thead.fixedHeader a, thead.fixedHeader a:link, thead.fixedHeader a:visited {
	color: #FFF;
	display: block;
	text-decoration: none;
	width: 100%
}

/* make the A elements pretty. makes for nice clickable headers                */
/* WARNING: swapping the background on hover may cause problems in WinIE 6.x   */
thead.fixedHeader a:hover {
	color: #FFF;
	display: block;
	text-decoration: underline;
	width: 100%
}



/* make TD elements pretty. Provide alternating classes for striping the table */
/* http://www.alistapart.com/articles/zebratables/                             */
tbody.scrollContent td, tbody.scrollContent tr.normalRow td {
	background: #FFF;
	font-size: 14px;
        color: #323232;
	padding: 2px 3px 3px 4px;
        font-family: Calibri;
        height: 30px;
}

tbody.scrollContent tr.alternateRow td {
	background: #EEE;
	font-size: 14px;
        color: #323232;
	padding: 2px 3px 3px 4px;
        font-family: Calibri;
        height: 30px;
}

tbody.scrollContent td {
	width: 200px
}

tbody.scrollContent td + td {
	width: 100px
}

tbody.scrollContent td + td + td {
	width: 100px
}

tbody.scrollContent td + td + td +td {
	width: 100px
}

tbody.scrollContent td + td + td + td + td{
	width: 150px
}






    </style>

        <link rel="stylesheet" href="cssfiles/style.css" type="text/css" media="screen" />
        <script src="scripts/jquery-1.5.1.min.js" type="text/javascript"></script>

        <%

                    String userAgent = request.getHeader("user-agent");
                    if (userAgent.indexOf("MSIE") > -1) {
        %>
       <link href="cssfiles/tab_css_ie.css" rel="stylesheet" type="text/css" />

        <%                    } else {
        %>
        <link href="cssfiles/tab_css_other.css" rel="stylesheet" type="text/css" />
        <%                    }
        %>
        <script src="scripts/common_tab.js" type="text/javascript"></script>
        

</head>
<body>
<%

        int userid=0;

        try{
            String userId = request.getParameter("radioval");
            
           userid  = Integer.parseInt(userId);
           System.out.println("userId"+userId);
        }catch(Exception e){
        }
    %>
<div id="pagewrap">


                <img id="logo" src="images/paidpunch.png" alt="paidpunch" />
                <ul id="jsddm">
                    <li  ><a href="business_admin_user.jsp">Home</a>

                    </li>
                    <li ><a href="manage_business.jsp">Businesses</a>

                    </li>
                    <li class="navtext-sel"><a href="overview_business.jsp">Users</a>

                    </li>

                </ul>
                <a href="signout.jsp" style="color: #323232; float: right; height: 32px; margin-top: 10px; margin-left: 0;">Signout</a>

        </div>
        <br>

        <table style="font-family: Arial,Helvetica,sans-serif; padding-top: 10px; padding-bottom: 50px;" width="100%" >
            <tbody>

            <tr><td valign="top" align="center">
        
<%

            String userName = "";

            String punch_card_name = "",download_date="",download_time="";
            int total_no_of_punches=0,remaining_punches=0,redeemed_punches=0;

            DBConnection db = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                               db = new DBConnection();
                               stmt = db.stmt;

                               try{

                               String query = "SELECT username FROM app_user where user_id="+userid;
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                userName = rs.getString(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }
                               com.server.Constants.logger.info("\nUser Name"+userName);


                             try{

                               //String query = "SELECT COUNT(*) FROM punchcard_download";
                                   String query = "SELECT punch_card.punch_card_name,punch_card.no_of_punches_per_card,punchcard_download.punch_used,punchcard_download.download_date,punchcard_download.download_time FROM punch_card,punchcard_download where punchcard_download.app_user_id="+userid+" and punch_card.punch_card_id=punchcard_download.punch_card_id and punchcard_download.isfreepunch='false';";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                               %>

        <div id="busilist" class="boxed">
                        <h2 class="title"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/><%=userName%> Transaction Details</h2>
                                <div class="content">

               

<%
        if(!rs.next()){
         %>
    

           <div style="font-size: 24px; font-family: Calibri; height: 30px;margin-top: 10px;" align="center"><%=userName%> has no Punch Cards Downloaded</div>
       
         <%
        }else{
                rs = stmt.executeQuery(query);
                com.server.Constants.logger.info("The select query is " + query);
%>
    <div id="tableContainer" class="tableContainer">
        <table class="scrollTable" border="0" cellpadding="0" cellspacing="0" width="100%">
        <thead class="fixedHeader">

            <tr class="alternateRow" align="left">

		<th >Business Name</th>
                <th align="center">Total Punches</th>
                <th align="center">Redeemed Punches</th>
		<th align="center">Remaining Punches</th>
                <th align="center">Downloaded on</th>



	</tr>
</thead>
<tbody class="scrollContent">

<%

                               int i=1;
                              while(rs.next()){

                                punch_card_name = rs.getString(1);
                                total_no_of_punches = rs.getInt(2);
                                remaining_punches = rs.getInt(3);
                                download_date = rs.getDate(4).toString();
                                download_time = rs.getTime(5).toString();

                                redeemed_punches = total_no_of_punches - remaining_punches;

                                if(i % 2 != 0){
                                    %>
    <tr class="normalRow" valign="middle">
        
		<td align="left"><%=punch_card_name%></td>
                <td align="center"><%=total_no_of_punches%></td>
                <td align="center"><%=redeemed_punches%></td>
                <td align="center"><%=remaining_punches%></td>
                <td align="center"><%=download_date%> &nbsp;&nbsp;   <%=download_time%></td>

	</tr>
        <%
                }else{
%>
                    <tr class="alternateRow" valign="middle">
                                    <td align="left"><%=punch_card_name%></td>
                                    <td align="center"><%=total_no_of_punches%></td>
                                    <td align="center"><%=redeemed_punches%></td>
                                    <td align="center"><%=remaining_punches%></td>
                                    <td align="center"><%=download_date%> &nbsp;&nbsp; <%=download_time%></td>

                            </tr>
	<%
                                }
                                i++;
                              }
                               %>
                               </tbody>
                                    </table>
                               </div>
        <%
                          }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }
                              

                              

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






			</div>
		</div>
     </td></tr>

<tr >


<td valign="top" align="center">
    <input id="inputsubmit1" type="button" name="back_btn" value="Back" onClick="location.href='overview_business.jsp'" />
    
</td>

</tr>



<tr><td><br><br><br></td></tr>

</tbody></table>




<div id="footer">
	<p id="legal"></p>
</div>
</body>
</html>
<%
}
%>