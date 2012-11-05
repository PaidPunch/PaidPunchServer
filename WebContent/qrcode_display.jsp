<%-- 
    Document   : qrcode_display
    Created on : Oct 13, 2011, 11:52:45 AM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.server.Constants"%>
<%@page import="com.jspservlets.DBConnection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.mysql.jdbc.Statement"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


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

body {
	height: 100%;
	font: normal small Calibri;
	color: #999999;
        background: #efefef;
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
	color: #F47B27;
}

h2 {
	margin-bottom: .5em;
	font-size: 1.4em;
}
h3 {
	margin-bottom: 1em;
	font-size: 1em;
}

p, blockquote, ul, ol {
	margin-bottom: 1.5em;
	line-height: 1.8em;
}

p {
}

blockquote {
}

ul {
	margin-left: 2em;
	list-style: square;
}

ul li {
}

ol {
	margin-left: 2em;
	list-style: lower-roman;
}

ol li {
}

a {
	font-size: 1.5em;
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
	
	background: #F7F7F7 url(images/tables_long.PNG) repeat-x;
	border: 1px solid #CCCCCC;
	width: 100%;
        height:100%;
}

.boxedqrcode {
        margin-top:15px;
	background: #FF8C00;
	border: 1px solid #CCCCCC;
	width:240px;
        height:240px;
        margin-bottom: 5px;
        

}
.boxed .title {
	height: 27px;
	padding: 7px 0 0 35px;
	background: url(images/arrows.png) no-repeat 10px 50%;
	font-size: 1.2em;
	color: #FFFFFF;
        text-align: left;
}

.boxed .content {
	padding: 10px;
}

/* Header */

#header {
	width: 700px;
	height: 130px;
	margin: 0 auto;
	background: url(images/img02.jpg) no-repeat;
}

/* Header > Logo */

#logo {
	
	text-align:center;
	padding: 25px 0 0 0;
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
	margin: 0 0;
	padding: 10px 0;
}

/* Content > Main */

#main {
	float: left;
	width: 750px;
}

/* Content > Main > Example */

#example {
}

/* Content > Main > Welcome */

#welcome {
	width: 750px;
	margin: 0 0 0 0;
	padding: 0 0 0 0;

}

/* Content > Sidebar */

#sidebar {
	float: right;
	width: 220px;
}

/* Content > Sidebar > Login */

#login {
}

#login label {
	margin-bottom: .5em;
	font: normal medium Calibri;
	padding: 2px 5px;
	float:left;
}

#login label1 {
	
        font-weight: bold;
	padding: 2px 5px;
	font-size: 20px;
        font: normal medium Calibri;
	color: #323232;
}

#label2 {
	
        font-size: 18px;
	padding: 2px 5px;
	font: normal small Calibri;
	color: #323232;
}

#legal {

	margin: 0;
	padding: 5px;
	text-align: center;
	color: #FFFFFF;
	height: 15px;

	bottom: 0;

}

#legal a {
	font-weight: bold;
	color: #FFFFFF;

}

/* Footer > Links */

#links {
	margin: 0;
	padding: 10px;
	text-align: center;
}

    </style>

</head>
<body>
<%
    String username="";
    String qrCodeValue="";
    String punchCardName = "";
    String businessName="";
    try{
         username = session.getAttribute("username").toString();
        }catch(Exception e){
        }

        if(username==null||username==""){
             response.sendRedirect("login.jsp");
          }
          else{

            DBConnection db = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                               db = new DBConnection();
                               stmt = db.stmt;
                               //String query = "SELECT punch_card_name,qrcode from punch_card where business_userid=(Select business_userid from business_users where email_id='"+username+"')";
                               String query = "SELECT business_name,punch_card_name,orange_qrcode_value from business_users,punch_card where business_users.email_id='"+username+"' and punch_card.business_userid=business_users.business_userid";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                              // displaying records

                              if(rs.next()){
                                businessName = rs.getObject(1).toString();
                                  punchCardName = rs.getObject(2).toString();
                                qrCodeValue = rs.getObject(3).toString();
                                businessName = businessName.toUpperCase();
                              }
                              
                              

                      }catch (SQLException e) {
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


	<table style="font-family: Arial,Helvetica,sans-serif;" width="100%">
	<tbody><tr>


<td valign="top" align="center">

		<div id="login" class="boxed">
			<h2 class="title">Issuing QR Code for <%=businessName%></h2>
			<div class="content">
				<form id="form1" name="user" method="post" action="forgot_pass">
					<fieldset>
					<legend>QR Code</legend>
                                        <%--<label1><%=punchCardName%></label1>--%>
                                        <%--<label>Following is the QR Code related to the Business you have created</label>--%><div id="qrcode" class="boxedqrcode"><br><img src="http://api.qrserver.com/v1/create-qr-code/?data=<%=qrCodeValue%>&amp;size=200x200" alt="" title="" v-align="center" align="center"/></div><br><br>
                                        <div id="label2"><B>ATTENTION</B> : Keep this QR code hidden and allow the customer to scan it <B>only after receiving payment.</B> Scanning this code will issue a PaidPunch Card to the customer.</div>
					
					</fieldset>
				</form>
			</div>
		</div>
</td>
</tr>

</tbody></table>

</body>
</html>

<%
}
%>
