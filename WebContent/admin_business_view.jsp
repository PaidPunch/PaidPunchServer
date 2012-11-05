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
    if(username==null || username==""){
        response.sendRedirect("login.jsp");
    }else{

    %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link href='images/paidpunchlogo.png' rel='icon' type='image/png'/>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>PaidPunch</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link media="screen" rel="stylesheet" type="text/css">
<%--<script language="javaScript" type="text/javascript" src="scripts/calendar.js"></script>
<link href="cssfiles/calendar.css" rel="stylesheet" type="text/css">--%>
<link rel="stylesheet" href="cssfiles/jquery.ui.all.css">
<script src="scripts/jquery-1.7.1.js" type="text/javascript"></script>
<script src="scripts/jquery.ui.core.js" type="text/javascript"></script>
<script src="scripts/jquery.ui.widget.js" type="text/javascript"></script>
<script src="scripts/jquery.ui.datepicker.js" type="text/javascript"></script>
<link rel="stylesheet" href="cssfiles/demos.css">
<link rel="stylesheet" href="cssfiles/style.css" type="text/css" media="screen" />
<%--<script src="scripts/jquery-1.5.1.min.js" type="text/javascript"></script>--%>
<script>
	$(function() {
		var dates = $( "#from, #to" ).datepicker({
			defaultDate: "+1w",
			changeMonth: true,
			numberOfMonths: 1,
			onSelect: function( selectedDate ) {
                            
				var option = this.id == "from" ? "minDate" : "maxDate",
					instance = $( this ).data( "datepicker" ),
					date = $.datepicker.parseDate(
						instance.settings.dateFormat ||
						$.datepicker._defaults.dateFormat,
						selectedDate, instance.settings );
				dates.not( this ).datepicker( "option", option, date );
                                
			}
		});
	});
	</script>

<script type="text/javascript" language="javascript">
function viewTransaction(){
    var from_date = document.getElementById("from").value;
    var to_date = document.getElementById("to").value;

    if(from_date.length==null||from_date.length==""){
        alert("Enter From Date");
        return false;
    }
    if(to_date.length==null||to_date.length==""){
        alert("Enter To Date");
        return false;
    }

    if(validateDate(from_date)==false){
        alert("Enter valid From Date");
        return false;
    }

    if(validateDate(to_date)==false){
        alert("Enter valid To Date");
        return false;
    }

    var email_id = document.getElementById("business_email").value;
    document.getElementById("iframe_a").src = "paymentreport.jsp?id=01&email="+email_id+"&from="+from_date+"&to="+to_date;

    <%--alert("from"+from_date);
    alert("to"+to_date);
    document.getElementById("iframe_a").src="HOW.html";--%>
    <%--var the_char=my_car.charAt(0);--%>



}

function validateDate(passeddate){
    if(passeddate.charAt(2)!="/"||passeddate.charAt(5)!="/"){

        return false;
    }
    return true;
}

</script>
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
	
	margin-bottom: 10px;
        margin-right: 30px;
        margin-left: 80px;
	background: #F7F7F7 ;
	border: 1px solid #CCCCCC;
	width:420px;
        height:355px;
        padding-bottom: 5px;
        font-family: Calibri;

}

.boxed .title {
	height: 20px;
	padding: 7px 0 0 7px;
	background: url(images/orange_gradient.png) repeat-x 10px 50%;
	text-transform: uppercase;
	font-size: 1.2em;
	color: #FFFFFF;
	text-align: left;
}

.boxed2 {

	margin-bottom: 10px;
        margin-right: 30px;
       
	background: #F7F7F7 ;
	border: 1px solid #CCCCCC;
	width:400px;
        height:230px;
        font-family: Calibri;

}

.boxed2 .title {
	height: 20px;
	padding: 7px 0 0 7px;
	background: url(images/orange_gradient.png) repeat-x 10px 50%;
	text-transform: uppercase;
	font-size: 1.2em;
	color: #FFFFFF;
	text-align: left;
}


.boxed1 {
	
	margin-bottom: 10px;
        margin-right: 30px;
	background: #F7F7F7 ;
	border: 1px solid #CCCCCC;
	width:400px;
        height:360px;

}

.boxed1 .title {
	height: 20px;
	padding: 7px 0 0 7px;
	background: url(images/orange_gradient.png) repeat-x 10px 50%;
	text-transform: uppercase;
	font-size: 1.2em;
	color: #FFFFFF;
	text-align: left;
        font-family: Calibri;
}

.boxed3 {

	margin-bottom: 10px;
        margin-right: 30px;

	background: #F7F7F7 ;
	border: 1px solid #CCCCCC;
	width:420px;
        height:230px;
        font-family: Calibri;

}

.boxed3 .title {
	height: 20px;
	padding: 7px 0 0 7px;
	background: url(images/orange_gradient.png) repeat-x 10px 50%;
	text-transform: uppercase;
	font-size: 1.2em;
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
	padding: 10px 0;
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

#boxlogo {
        float : left;
	text-align:center;
	padding-left: 5px ;
        padding-right: 5px;
        margin-top: 30px;
}


#label {
        padding: 2px 5px;
	margin-bottom: .5em;
        margin-left: 5px;
	font: normal medium Calibri;
        font-size:16px;
        color : #323232;
        font-weight: bold;
        float: left;
}

#label2 {
        
	margin-bottom: .5em;

	font: normal medium Calibri;
        font-size:16px;
        color : #323232;
        font-weight: bold;
        float:left;
        text-align: left;

}


#label1 {
        padding: 2px 5px;
	margin-bottom: .5em;
        margin-right: 5px;
	font: normal medium Calibri;
        font-size:16px;
        color : #F47B27;
        font-weight: bold;
        float: right;
}

#label4 {
        padding: 2px 5px;
	margin-bottom: .5em;
        margin-right: 5px;
	font: normal medium Calibri;
        font-size:16px;
        color : #F47B27;
        font-weight: bold;
        float: left;
}


#label3 {
        padding: 2px 5px;
	margin-bottom: .5em;

	font: normal medium Calibri;
        font-size:16px;
        color : #323232;
        font-weight: bold;

        text-align: center;

}

#label5 {

        margin-left: 5px;
	font: normal medium Calibri;
        font-size:18px;
        color : #323232;
        font-weight: bold;

}

#label7 {



	font: normal medium Calibri;
        font-size:18px;
        color : #323232;
        font-weight: bold;

        text-align: left;

}


#label6 {
        padding-right: 5px;
        margin-right: 5px;
	font: normal medium Calibri;
        font-size:18px;
        color : #F47B27;
        font-weight: bold;

}

#label8 {
        padding: 2px 5px;
	margin-bottom: .5em;
        margin-right: 5px;
	font: normal medium Calibri;
        font-size:18px;
        color : #F47B27;
        font-weight: bold;
        float: left;
}


#login #inputtext1{
	width: 180px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;

}

#inputsubmit3 {
	width : 67px;
	font-size: 15px;
        color : #ffffff;
	font-family: calibri;
        font-weight: bold;
        margin-left: 10px;
        background: url(images/button_base.png) repeat;
}




#inputsubmit1 {
	width : 67px;
	font-size: 15px;
        color : #ffffff;
        margin-left: 170px;
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


/* define height and width of scrollable area. Add 16px to width for scrollbar          */
div.tableContainer {
	clear: both;
        padding-left: 5px;
        padding-right: 5px;
	border: 0px solid #963;
        margin-left: 70px;
	height: 130px;
	overflow: auto;
	width: 230px;
       background: #fff url("images/card.PNG") no-repeat 0 0;
       float:left;
}



    </style>

    <title></title>


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
        
            

        int busid=0;
        try{
            String businessId = request.getParameter("radioval");
           busid  = Integer.parseInt(businessId);
           System.out.println("BusinessId"+businessId);
        }catch(Exception e){
        }
    %>
<div id="pagewrap">


                <img id="logo" src="images/paidpunch.png" alt="paidpunch" />
                <ul id="jsddm">
                    <li  ><a href="business_admin_user.jsp">Home</a>

                    </li>
                    <li class="navtext-sel"><a href="manage_business.jsp">Businesses</a>

                    </li>
                    <li><a href="overview_business.jsp">Users</a>

                    </li>


                </ul>
                <a href="signout.jsp" style="color: #323232; float: right; height: 32px; margin-top: 10px; margin-left: 0;">Signout</a>

        </div>
        <br>
<%


    String business_name="",email_id="",busi_desc="",address="",discount="";
    String city="",state="",country="",pincode="",contactno="",contactname="",punch_card_name="",expiry_date="", latitude="",longitude="";
    float disc=0,actual_value_of_punch=0,valueofpunch=0,sppunch=0;
    int noofpunches=0, expirydays = 0;
    int punch_card_id=0;
    long totalpunchcardsold=0,redeemedpunches=0, totalmysteryRedeemed=0,totalfreepunchesdownloaded=0,totalfreepunchesredeemed=0;
    long jackpotmysteryRedeemed=0;
            DBConnection db = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                               db = new DBConnection();
                               stmt = db.stmt;

                               try{

                               String query = "SELECT * FROM business_users where business_userid="+busid;
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                               business_name = rs.getString("business_name");
                               email_id = rs.getString("email_id");
                               busi_desc = rs.getString("buss_desc");
                               contactno = rs.getString("contactno");
                               contactname = rs.getString("contactname");

                              }

                               query = "SELECT * FROM bussiness_address where business_id="+busid;
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                               address = rs.getString("address_line1");
                               city = rs.getString("city");
                               state = rs.getString("state");
                               pincode = rs.getString("zipcode");
                               country = rs.getString("country");
                               latitude = rs.getString("latitude");
                               longitude = rs.getString("longitude");
                              }
                            }catch(SQLException e){
                                com.server.Constants.logger.error("Error in Sql in admin_business_view.jsp in getting business details"+e);
                                throw new ServletException("SQL Exception.", e);
                            }



                            try{
                                   String query = "SELECT * FROM  punch_card where punch_card.business_userid="+busid;
                                   rs = stmt.executeQuery(query);
                                   com.server.Constants.logger.info("The select query is " + query);

                                  if(rs.next()){
                                        punch_card_id = rs.getInt("punch_card_id");
                                        punch_card_name = rs.getString("punch_card_name");
                                        noofpunches = rs.getInt("no_of_punches_per_card");
                                        valueofpunch = rs.getFloat("value_of_each_punch");
                                        sppunch = rs.getFloat("selling_price_of_punch_card");
                                        expirydays = rs.getInt("expirydays");
                                        expiry_date = rs.getDate("expiry_date").toString();
                                        disc = rs.getFloat("effective_discount");
                                         actual_value_of_punch = rs.getFloat("disc_value_of_each_punch");
                                        discount = ""+disc;

                                        valueofpunch = floatRound(valueofpunch,2);
                                        sppunch = floatRound(sppunch,2);

                                  }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in admin_business_view.jsp in getting punch card values"+e);
                                 throw new ServletException("SQL Exception.", e);
                            }

                            com.server.Constants.logger.info("\npunch_card_id : "+punch_card_id+"\nPunchCardName : "+punch_card_name+"\nNoofPunches : "+noofpunches+"\nValue of Each Punch : "+valueofpunch+"\nDiscount : "+discount+"\nSelling Price : "+sppunch+"\nExpiryDate : "+expiry_date);

                            try{

                               String query = "SELECT COUNT(*) FROM punchcard_download where punch_card_id="+punch_card_id+" and isfreepunch='false'";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalpunchcardsold = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in admin_business_view.jsp in getting total downloaded punch cards"+e);
                                 throw new ServletException("SQL Exception.", e);
                              }

                              try{

                               //String query = "SELECT COUNT(*) FROM punch_card_tracker where punch_card_id="+punch_card_id;
                               String query = "SELECT COUNT(*) FROM punch_card_tracker,punchcard_download where punch_card_tracker.punch_card_id="+punch_card_id+" and punchcard_download.isfreepunch='false' and punchcard_download.punch_card_downloadid=punch_card_tracker.punch_card_downloadid and punch_card_tracker.is_mystery_punch='false';";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                redeemedpunches = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in admin_business_view.jsp in getting total redeemed punches"+e);
                                 throw new ServletException("SQL Exception.", e);
                              }

                            try{

                               //String query = "SELECT COUNT(*) FROM punch_card_tracker where punch_card_id="+punch_card_id;
                               //String query = "Select COUNT(*) FROM punch_card_tracker where is_mystery_punch='true' and punch_card_id="+punch_card_id;
                                String query = "Select COUNT(*) FROM punchcard_download where punch_card_id="+punch_card_id+" and mystery_punchid IS NOT NULL;";
                                 rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalmysteryRedeemed = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in admin_business_view.jsp in getting total redeemed punches"+e);
                                 throw new ServletException("SQL Exception.", e);
                              }
                            /*
                            try{

                               //String query = "SELECT COUNT(*) FROM punch_card_tracker where punch_card_id="+punch_card_id;
                               String query = "SELECT COUNT(*) FROM punchcard_download where mystery_punchid=(SELECT mystery_id FROM mystery_punch where punch_card_id="+punch_card_id+" and paidpunchmystery='Y');";
                                rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                jackpotmysteryRedeemed = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in admin_business_view.jsp in getting total redeemed punches"+e);
                                 throw new ServletException("SQL Exception.", e);
                              }
                              */

                            try{

                               //String query = "SELECT COUNT(*) FROM punchcard_download";
                                   //String query = "SELECT punch_card.no_of_punches_per_card FROM punchcard_download,punch_card where punch_card.punch_card_id=punchcard_download.punch_card_id;";
                               String query = "SELECT count(*) FROM punchcard_download where punchcard_download.punch_card_id="+punch_card_id+" and punchcard_download.isfreepunch='true'";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalfreepunchesdownloaded =  rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }

                              try{

                               //String query = "SELECT COUNT(*) FROM punch_card_tracker";
                               String query = "Select COUNT(*) from punch_card_tracker, punchcard_download where punch_card_tracker.punch_card_id="+punch_card_id+" and punchcard_download.isfreepunch='true' and punchcard_download.punch_card_downloadid=punch_card_tracker.punch_card_downloadid;";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalfreepunchesredeemed = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }


                              com.server.Constants.logger.info("\nTotal Punch Cards Sold"+totalpunchcardsold+"\nNo of Punches Redeemed : "+redeemedpunches);


                      }catch (Exception e) {
                            com.server.Constants.logger.error("Error in Sql in admin_business_view.jsp "+e);
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
                            com.server.Constants.logger.error("Error in closing SQL in admin_business_view.jsp "+e);
                      }
                   }


                float cppunch = noofpunches*valueofpunch;
                cppunch = floatRound(cppunch,2);
                long totalpunchessold = totalpunchcardsold*noofpunches;
                float totalvaluecollected = totalpunchessold * actual_value_of_punch;
                
                totalvaluecollected = floatRound(totalvaluecollected,2);
                
                float revenue = redeemedpunches * actual_value_of_punch;
                
                revenue = floatRound(revenue,2);
                
                long outstandingPunches = totalpunchessold - redeemedpunches;
                float outstandingRevenue = totalvaluecollected - revenue;
                outstandingRevenue = floatRound(outstandingRevenue,2);
               
           //     long totalvaluecollected1 = (long)totalvaluecollected;
           //     long revenue1 = (long)revenue;
           //     long outstandingRevenue = totalvaluecollected1 - revenue1;
               


%>

        <table style="font-family: Arial,Helvetica,sans-serif; padding-top: 10px; padding-bottom: 50px;" width="100%" >
            <tbody>

            
                <tr><td valign="top" align="right" >
                  
                <div id="busilist" class="boxed">
                <h1 style="font-size: 16px; font-weight: bold;" class="title"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/><%=business_name%> Overview</h1>
			<div class="content">
                           <table width="95%" cellspacing="0" cellpadding="0">
                               <tr><td><br></td></tr>
                             <TR><TD rowspan="14" valign="middle"><img src="DisplayImage?bussid=<%=busid%>" alt="pplogo" height="120px" width="120px" style=" margin-right: 10px;"></TD>
                            <td id="label5" align="left" width="75%">Punches Sold</td><td id="label7" width="5%">:</td><TD width="20%" id="label6" align="right"><%=totalpunchessold%></TD></TR>
                             <TR><td id="label5" align="left" width="75%">Total Collected</td><td id="label7" width="5%">:</td><TD width="20%" id="label6" align="right" style="color: #323232">$<%=totalvaluecollected%></TD></TR>
                            <TR><td><BR></td></TR>
                            <TR ><td id="label5" align="left" width="75%">Punches Redeemed</td><td id="label7" width="5%">:</td><TD width="20%" id="label6" align="right"><%=redeemedpunches%></TD></TR>
                            <TR><td id="label5" align="left" width="75%">Total Revenue</td><td id="label7" width="5%">:</td><TD width="20%" id="label6" align="right" style="color: #38761D">$<%=revenue%></TD></TR>
                            <TR><td><BR></td></TR>
                            <TR><td id="label5" align="left" width="75%">Punches Outstanding</td><td id="label7" width="5%">:</td><TD width="20%" id="label6" align="right"><%=outstandingPunches%></TD></TR>
                            <TR><td id="label5" align="left" width="75%">Total Outstanding</td><td id="label7" width="5%">:</td><TD width="20%" id="label6" align="right" style="color: #323232">$<%=outstandingRevenue%></TD></TR>
                            <TR><td><BR></td></TR>
                            <TR><td id="label5" align="left" width="75%">Free Punches Sold</td><td id="label7" width="5%">:</td><TD width="20%" id="label6" align="right"><%=totalfreepunchesdownloaded%></TD></TR>
                            <TR><td id="label5" align="left" width="75%">Free Punches Redeemed</td><td id="label7" width="5%">:</td><TD width="20%" id="label6" align="right" style="color: #323232"><%=totalfreepunchesredeemed%></TD></TR>
                            <TR><td><BR></td></TR>
                            <TR><td id="label5" align="left" width="75%">Total Mystery Redeemed</td><td id="label7" width="5%">:</td><TD width="20%" id="label6" align="right" style="color: #323232"><%=totalmysteryRedeemed%></TD></TR>
                            <%--<TR><td id="label5" align="left" width="75%">Jackpot Mystery Redeemed</td><td id="label7" width="5%">:</td><TD width="20%" id="label6" align="right" style="color: #323232"><%=jackpotmysteryRedeemed%></TD></TR>--%>
                            </table>
                </div>
		</div>
                      </td>

                       <td valign="top" align="left">

            <div id="busilist" class="boxed1">
                <h2 class="title" style="font-family: Calibri; font-size:17px;"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/>Business Details</h2>
			<div class="content">
                           
                            <%--<div id="label">Punch Card Name</div><div id="label2">:</div><div id="label1"><%=punch_card_name%></div><BR><BR>--%>
                            <div id="label">Email </div><div id="label2">:</div><div id="label1"><%=email_id%></div><BR><BR>
                            <%--<div id="label">Business Description</div><div id="label2">:</div><br><div id="label4">123456789012345678901234567891234567890123456789<br>0123456789123456789012345678901234567891234567890123456789012345678912345678901234567890123456789</div><BR><BR>--%>
                            <div id="label">Address</div><div id="label2">:</div><div id="label1"><%=address%></div><BR><BR>
                            <div id="label">City</div><div id="label2">:</div><div id="label1"><%=city%></div><BR><BR>
                            <div id="label">State</div><div id="label2">:</div><div id="label1"><%=state%></div><BR><BR>
                            <div id="label">Zipcode</div><div id="label2">:</div><div id="label1"><%=pincode%></div><BR><BR>
                            <div id="label">Country</div><div id="label2">:</div><div id="label1"><%=country%></div><BR><BR>
                            <div id="label">Contact Name</div><div id="label2">:</div><div id="label1"><%=contactname%></div><BR><BR>
                            <div id="label">Contact Number</div><div id="label2">:</div><div id="label1"><%=contactno%></div><BR><BR>
                            <div id="label">Latitude</div><div id="label2">:</div><div id="label1"><%=latitude%></div><BR><BR>
                            <div id="label">Longitude</div><div id="label2">:</div><div id="label1"><%=longitude%></div><BR><BR><BR>
                            

			</div>
		</div>
                      </td>

                </tr>
               <tr> <td valign="top" align="right">
                         <div id="busilist" class="boxed3">
                            <h2 class="title" style="font-family: Calibri; font-size:17px;"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/><%=business_name%> Payment Details</h2>
                            <div class="content" >
                                    
                                        <div class="demo" style="float: left; margin-left: 20px;">

                                        <label for="from" style="font-weight: bold; font-size: 12px;">From</label>
                                        <input type="text" id="from" name="from" size="14" maxlength="10"/>
                                        <label for="to" style="font-weight: bold; font-size: 12px;">to</label>
                                        <input type="text" id="to" name="to" size="14" maxlength="10"/>
                                        <input type="hidden" id="business_email" name="business_email" value="<%=email_id%>">
                                        <input id="inputsubmit3" align="center" type="button" name="view_btn" value="View" onClick="viewTransaction();"/>
                                        </div>
                            <div style="float: left; margin-left: 10px; margin-right: 10px;">
                                <iframe src="paymentreport.jsp?id=00" id="iframe_a" name="iframe_a" width="400px" height="140px" frameborder="0" style="border:0px solid #963; "></iframe>
                            </div>
                                   

                            </div>

                         </div>
                     </td>

                    <td valign="top" align="left">

            <div id="busilist" class="boxed2">
                <h2 class="title" style="font-family: Calibri; font-size:17px;"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/><%=business_name%> PaidPunch Card</h2>
			<div class="content">
                            <div id="label3"><%=noofpunches%> $<%=valueofpunch%> Punches ($<%=cppunch%> Value) for $<%=sppunch%> <br>Punches' promotional value expires after <%=expirydays%> days</div>
                            <div id="tableContainer" class="tableContainer"><br><br>
<table class="scrollTable" border="0" cellpadding="0" cellspacing="0" width="100%">

<tbody class="scrollContent">
    <%
            int a=noofpunches;
            int noofRows=a/5;
                    System.out.println("NoofRows: "+noofRows+" noofPuncehs:"+a);
            for(int i=0;i<noofRows;i++){
                %>
                <TR>
    <%
                    for(int j=0;j<5;j++){
    %>
                        <td><img src="images/unusedpunch.png" alt="unusedpunch"></td>
     <%

                }
                    %>
                 </TR><tr><td><br></td></tr>
                 <%

            }

            int noofColumns = noofpunches%5;
            if(noofColumns!=0){
                %><TR><%
                for(int k=0;k<noofColumns;k++){
   %>
                    <td><img src="images/unusedpunch.png" alt="unusedpunch"></td>
                    <%

                }
                %></TR><%
            }
            System.out.println("NoofColumns: "+noofColumns);


%>


	</tbody>
</table>
                        </div>

                        </div>
		</div>

           </td>







         <%%>


                    </tr>
                    <tr><td colspan="2" align="center"><input id="inputsubmit1" align="center" type="button" name="back_btn" value="Back" onClick="location.href='manage_business.jsp'"/></td></tr>



<tr><td><br><br><br><br><br></td></tr>

</tbody></table>




<div id="footer">
	<p id="legal"></p>
</div>

<%
    }
%>

</body>
</html>

<%!
      public static float floatRound(float Rval, int Rpl) {
            float p = (float)Math.pow(10,Rpl);
            Rval = Rval * p;
            float tmp = Math.round(Rval);
            return (float)tmp/p;
  }
%>