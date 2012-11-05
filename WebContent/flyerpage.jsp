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
	background : #ffffff url(images/flyer_bg.png) no-repeat;
	font: normal small Arial, Helvetica, sans-serif;
	color: #999999;
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

}

h2 {
	margin-bottom: .5em;
	font-size: 1.8em;
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
	width:180px;
        height :120px;
        color : #323232;

}


.boxed .title {
	height: 29px;
	padding: 7px 0 0 40px;
	background: url(images/arrows.png) no-repeat 10px 50%;
	text-transform: uppercase;
	font-size: 16px;
	color: #ffffff;
        text-align: center;
}

.boxed .content {
	padding: 2px;
}

/* Header */

#header {
	
	height: 30px;
	margin: 0 0;
	background: #F7F7F7 url(images/orange_gradient.png) repeat-x;
}

/* Header > Logo */

#top1{
    margin-top: 20px;
}

#logo {
        margin-left: 40px;
	text-align:left;
	
        color: #ffffff;
        font-weight: bold;
        font-size: 16px;
        float: left;
}

#savetext{
    float: left;
}

#logo arrow {
    margin-left: 20px;

}

#logo h1 {
	text-transform: uppercase;
	font-size: 2em;
}

#busstext h1 {
        margin-right: 40px;
        color: #F47B27;
        font-weight: bold;
	font-size: 38px;
        
        
        font-family: calibri;
        float: right;
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

/* Header > Top Menu */


/* Content */

#content {

	margin: 0 0;
	padding: 0 0;
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

#label {
	margin-bottom: .5em;
	font: normal medium Arial, Helvetica, sans-serif;
	float:left;
}

#login label1 {
	margin-bottom: .5em;

	padding: 2px 5px;
	
        font: normal large Arial, Helvetica, sans-serif;
	color: #000000;
}

#login #inputtext1{
	width: 200px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Arial, Helvetica, sans-serif;
	color: #999999;
	
}






#inputsubmit1, #inputcancel {
	margin-right: .9em;
	width: 80px;

	font: normal medium Arial, Helvetica, sans-serif;
}


#login p {
	margin: 0;
	padding-top: .2em;
	font-size: x-small;
	float:right;
}

/* Content > Sidebar > Updates*/

#updates {
}

#updates ul {
	margin: 0;
	list-style: none;
}

#updates li {
	margin-bottom: 1em;
}

#updates h3 {
	margin: 0;
	padding: 0 0 0 10px;
	background: url(images/img09.gif) no-repeat left center;
	font-size: x-small;
}

#updates p {
	margin: 0;
	padding: 0 0 0 10px;
	line-height: normal;
	font-size: .85em;
}

#updates a {
	text-decoration: none;
}


/* Footer */

#footer {

	clear: both;
	width: 750px;
	margin: 0 auto 0 auto;
	background: #F7F7F7 url(images/orange_gradient.png) repeat-x;
	border: 1px solid #CCCCCC;

}

/* Footer > Legal */

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
    int no_of_punches=0, value_of_punch=0, selling_price_punch=0 ;
    float discount=0; int disc=0;
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
                               String query = "SELECT business_name, punch_card_name, no_of_punches_per_card, value_of_each_punch, selling_price_of_punch_card, effective_discount, qrcode from business_users,punch_card where business_users.email_id='"+username+"' and punch_card.business_userid=business_users.business_userid";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                              // displaying records

                              if(rs.next()){
                                businessName = rs.getString(1);
                                punchCardName = rs.getString(2);
                                no_of_punches = rs.getInt(3);
                                value_of_punch = rs.getInt(4);
                                selling_price_punch = rs.getInt(5);
                                discount = rs.getFloat(6);
                                qrCodeValue = rs.getString(7);

                              }
                               disc = (int) discount;


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
<%--<div id="header">
            
	<div id="logo">
           <img id="arrow" src="images/arrows.png" alt="arrows"/> PAIDPUNCH
	</div>
</div>--%>
        <BR>
<div id="top1">
    <div id="logo">
    <img id="arrow" src="images/flyer_logo.png" alt="arrows" />
    </div><div id="busstext">
        <h1>Save <font color="#818285"><%=disc%>%</font> at <font color="#818285"><%=businessName%></font></h1><br>
        <h1>with <font color="#818285">Paid</font><font color="#F47B27">Punch</font></h1>
    </div>
</div><BR><BR>

<table style="font-family: Arial,Helvetica,sans-serif; padding-top: 10px; padding-bottom: 50px;" cellspacing="20px" >

	<tbody><tr>
<table style="font-family: calibri;" cellspacing="20px" >
<TR style="color: #58595B; font-size: 15px; font-weight: bold;">
    <!--<td style="font-size: 30px; text-align: center; width: 50px; font-weight: bold; color: white;"></td>-->
    <td valign="center">
        <img src="images/flyer_one.png" alt="One">
    </td>
    <td  valign="center">
              <label>Download the PaidPunch App and scan this QR code</label>
		
</td>
<td valign="center">
    <img src="images/flyer_two.png" alt="Two">
    </td>
<td  valign="center">
                        <div class="content">
                           <%
                            int costprice = no_of_punches * value_of_punch;
                            %>
                                 <label for="inputtext1">Pay the Cashier $<%=selling_price_punch%> and receive <%=no_of_punches%> $<%=value_of_punch%> vouchers of store credit ($<%=costprice%> value)</label>
                        </div>
</td>
<td valign="center">
    <img src="images/flyer_three.png" alt="Three">
    </td>
<td valign="center">
 	<div class="content">
                <label for="inputtext1">Everytime you visit us, open the PaidPunch App and use a $<%=value_of_punch%> PaidPunch towards your purchase.</label>
	</div>
</td>
</TR></table>
</tr>
<tr>
<table style="font-family: Arial,Helvetica,sans-serif; margin-left: 40px" cellspacing="20px" >
    <tr >
<!--<td style="font-size: 30px; text-align: center; width: 50px; font-weight: bold; color: white;"></td>-->
<td  align="center">
            <img src="http://api.qrserver.com/v1/create-qr-code/?data=<%=qrCodeValue%>&amp;size=180x180" alt="" title="" align="center"/>

					
</td>
<td valign="center" align="center">
    <img src="images/flyer_arrow.png" alt="" title="" v-align="center" align="center"/><br><br>
    
</td>

<td valign="center" align="center">
                                    
             <img src="images/flyer_card.png"  alt="" title="" v-align="center" align="center"/><br><br>
</td>
<td valign="center" align="center">
    <img src="images/flyer_arrow.png" alt="" title="" v-align="center" align="center"/><br><br>

</td>

<td style="background: url(images/mb_save.png) no-repeat; background-position: center;
    " width="200px">
    <font style="margin-left: 35%; color: #F47B27; font-size: 19px; font-weight: bold;">$<%=value_of_punch%></font><BR><font style="margin-left: 32%; color: #F47B27; font-size: 18px; font-weight: bold;">OFF</font>
		
            <%--<img src="images/flyer_shop.png" alt="" title="" v-align="center" align="center"/><br><br>--%>
					
</td>
</tr>
</table>
</tr>

</tbody></table>

</body>
</html>
<%
}
%>