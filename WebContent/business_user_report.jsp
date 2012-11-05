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
<%@ page import="java.text.DecimalFormat"%>

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
<script language="javaScript" type="text/javascript" src="scripts/calendar.js"></script>
<link href="cssfiles/calendar.css" rel="stylesheet" type="text/css">

<script language="JavaScript" type="text/javascript">

function setTableHeader(){

mycel1 = document.getElementById('theadertab');
var c=0;
while( mycel1.childNodes.length ){
    mycel1.removeChild( mycel1.childNodes[c] );
    c++;
}

mycel1.appendChild(
document.createTextNode( 'App-User' )
);

}

function setTableHeader1(){

mycel1 = document.getElementById('theadertab');
var c=0;
while( mycel1.childNodes.length ){
    mycel1.removeChild( mycel1.childNodes[c] );
    c++;
}

mycel1.appendChild(
document.createTextNode( 'User Name' )
);

}

function changeImage(abc)
{

if(window.ActiveXObject)
{
    document.getElementById(this.id).style.background='transparent url(/images/another.jpg) no-repeat top left;';
}
else
{
    document.getElementById(this.id).style.backgroundColor = 'transparent';
    document.getElementById(this.id).style.backgroundImage = 'url(/images/another.jpg)';
    document.getElementById(this.id).style.backgroundRepeat = 'no-repeat';
    document.getElementById(this.id).style.backgroundPosition = 'top left';
}

}
</script>

<style type="text/css">
* {
	margin: 0;
	padding: 0;
}


form {
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

.white{color:#FFF;
	     font-family:Calibri,SansSerif;
	       font-size:18px }

.grey {color:#323232;
	        font-family:Calibri,SansSerif;
	            font-size:16px ; }


.red {color:#D50000;
	font-family:Calibri,SansSerif;
	font-size:16px ; }

a:active{color:#D50000;
             font-family:Calibri,SansSerif;}

a:hover{color:#D50000;
            font-family:Calibri,SansSerif;}

.linkcolor { color: #D50000; }

.selected { font: 16px Calibri; color:#D50000; }


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
        padding-bottom: 5px;
        height:360px;
        font-family: Calibri;
        font-weight: bold;

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
        margin-left: 80px;
	background: #F7F7F7 ;
	border: 1px solid #CCCCCC;
	width:420px;
        height:240px;
        font-family: Calibri;
        font-weight: bold;

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
	width:580px;
        height:620px;

}

.boxed1 .title {
	height: 20px;
	padding: 7px 0 0 7px;
	background: url(images/orange_gradient.png) repeat-x 10px 50%;
	text-transform: uppercase;
	font-size: 16px;
	color: #FFFFFF;
	text-align: left;
}

#iFrametable {
    margin-left: 10px;
    margin-right: 10px;
    
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




#login a122:hover {
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
        
        margin-left: 5px;
	font: normal medium Calibri;
        font-size:18px;
        color : #323232;
        font-weight: bold;
        
}

#label2 {

	

	font: normal medium Calibri;
        font-size:18px;
        color : #323232;
        font-weight: bold;
        
        text-align: left;

}


#label1 {
        padding-right: 5px;
        margin-right: 5px;
	font: normal medium Calibri;
        font-size:18px;
        color : #F47B27;
        font-weight: bold;
        
}

#label4 {
        padding: 2px 5px;
	margin-bottom: .5em;
        margin-right: 5px;
	font: normal medium Calibri;
        font-size:18px;
        color : #F47B27;
        font-weight: bold;
        float: left;
}


#label3 {
        padding: 2px 5px;
	margin-bottom: .5em;

	font: normal medium Calibri;
        font-size:18px;
        color : #323232;
        font-weight: bold;

        text-align: center;

}
#login #inputtext1{
	width: 180px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;

}

#inputsubmit1 {
	width : 67px;
	font-size: 15px;
        color : #ffffff;
        margin-left: 170px;
        margin-right: 25px;
	font-family: Calibri;
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

	position:fixed;
       bottom:0;
   width:100%;
   background: #F47B27 url(images/orange_gradient.png) repeat-x 0 0;

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


.tHeaderc1 td a{
    background: url(button_base.png);
    font-family : Calibri;
    color : #000000;
    font-size: 16px;
}

tr.tHeaderc1 td a:hover{
    color:red;
    background: #efefef;
    font-family : Calibri;
}


#redeemlink{
    padding-top: 0.5em;
    height:25px;
    width:80px;
    background: #F47B27 url(images/orange_gradient.png) repeat-x 0 0;
    text-align: center;
    font-family : Calibri;
}

#issuedlink{
    margin-left: 30px;
    padding-top: .5em;
    height:25px;
    width:80px;
    background: #efefef;
    text-align: center;
    font-family : Calibri;
}
    </style>

<script language="JavaScript" type="text/javascript">
    /*<![CDATA[*/
var Lst ;
var temp;

function CngClass(obj){

 if (Lst)
 {
       // alert(Lst);
	 Lst.className='grey';

 }
 else
 {
        //alert(Lst);
	 temp.className='grey';
 }
 obj.className='selected';

 Lst=obj;

}
function onload_fun()
{

    	document.getElementById("vision").className='red';
        
     temp=document.getElementById("vision");
     
}

/*]]>*/
</script>

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
<body onload="onload_fun();">
<%

            DBConnection db1 = null;
            Statement stmt1 = null;
            ResultSet rs1 = null;
            int busid=0;

            try {
                               db1 = new DBConnection();
                               stmt1 = db1.stmt;

                               try{
                                    String query = "SELECT business_userid FROM business_users where email_id='"+username+"'";
                                    rs1 = stmt1.executeQuery(query);
                                    com.server.Constants.logger.info("The select query is " + query);

                                   if(rs1.next()){
                                    busid = rs1.getInt(1);
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
                          if(rs1 != null) {
                              rs1.close();
                              //Constants.logger.info("Closing rs Statement ");
                              rs1 = null;
                          }
                          db1.closeConnection();

                      } catch (SQLException e) {
                            com.server.Constants.logger.error("Error in closing SQL in checksecretcode.java"+e.getMessage());
                      }
                   }

    %>
<div id="pagewrap">


                <img id="logo" src="images/paidpunch.png" alt="paidpunch" />
                <ul id="jsddm">
                    <li class="navtext-sel"><a href="business_user_report.jsp">Report</a>

                    </li>
                    <li ><a href="business_user_settings.jsp">Settings</a>

                    </li>
                    
                </ul>
                <a href="signout.jsp" style="color: #323232; float: right; height: 32px; margin-top: 10px; margin-left: 0;">Signout</a>

        </div>
        <br>
<%


    String business_name="",email_id="",busi_desc="",discount="";
    String contactno="",contactname="",punch_card_name="",expiry_date="";
    float disc=0,actual_value_of_punch=0;
    int noofpunches=0,expirydays = 0;
    float valueofpunch=0,sppunch=0;
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
                            }catch(SQLException e){
                                com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                throw new ServletException("SQL Exception.", e);
                            }

System.out.println("BUsiness Name:"+business_name+"\nEmail_id: "+email_id+"\nBusi Desc: "+busi_desc+"\nCOntact Name:"+contactname+"\nContatct Num : "+contactno);

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
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
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


                float cppunch = noofpunches*valueofpunch;
                cppunch =  floatRound(cppunch,2);
                long totalpunchessold = totalpunchcardsold*noofpunches;
                float totalvaluecollected = totalpunchessold * actual_value_of_punch;
                totalvaluecollected = floatRound(totalvaluecollected,2);
                float revenue = redeemedpunches * actual_value_of_punch;
                revenue = floatRound(revenue,2);
                
                long outstandingPunches = totalpunchessold - redeemedpunches;
                float outstandingRevenue = totalvaluecollected - revenue;
                outstandingRevenue = floatRound(outstandingRevenue,2);
               //  DecimalFormat df = new DecimalFormat("###.##");
                //outstandingRevenue = df.format(outstandingRevenue);
                //long totalvaluecollected1 = (long)totalvaluecollected;
                //long revenue1 = (long)revenue;
                //long outstandingRevenue = totalvaluecollected1 - revenue1;



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
                            <td id="label" align="left" width="75%">Punches Sold</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right"><%=totalpunchessold%></TD></TR>
                             <TR><td id="label" align="left" width="75%">Total Collected</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right" style="color: #323232">$<%=totalvaluecollected%></TD></TR>
                            <TR><td><BR></td></TR>
                            <TR ><td id="label" align="left" width="75%">Punches Redeemed</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right"><%=redeemedpunches%></TD></TR>
                            <TR><td id="label" align="left" width="75%">Total Revenue</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right" style="color: #38761D">$<%=revenue%></TD></TR>
                            <TR><td><BR></td></TR>
                            <TR><td id="label" align="left" width="75%">Punches Outstanding</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right"><%=outstandingPunches%></TD></TR>
                            <TR><td id="label" align="left" width="75%">Total Outstanding</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right" style="color: #323232">$<%=outstandingRevenue%></TD></TR>
                            <TR><td><BR></td></TR>
                            <TR><td id="label" align="left" width="75%">Free Punches Sold</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right"><%=totalfreepunchesdownloaded%></TD></TR>
                            <TR><td id="label" align="left" width="75%">Free Punches Redeemed</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right" style="color: #323232"><%=totalfreepunchesredeemed%></TD></TR>
                            <TR><td><BR></td></TR>
                            <TR><td id="label" align="left" width="75%">Total Mystery Redeemed</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right" style="color: #323232"><%=totalmysteryRedeemed%></TD></TR>
                            <%--<TR><td id="label" align="left" width="80%">Jackpot Mystery Redeemed</td><td id="label2" width="5%">:</td><TD width="15%" id="label1" align="right" style="color: #323232"><%=jackpotmysteryRedeemed%></TD></TR>--%>
                            </table>

                            <%--<table width="95%" cellspacing="0" cellpadding="0">
                                <TR><td rowspan="8" valign="middle"><img src="DisplayImage?bussid=<%=busid%>" alt="pplogo" height="120px" width="120px" style="margin-top: 25px; margin-right: 10px;"></td>
                            <td id="label" align="left" width="75%">Punches Sold</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right"><%=totalpunchessold%></TD></TR>
                            <TR><td id="label" align="left" width="75%">Total Collected</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right" style="color: #323232">$<%=totalvaluecollected%></TD></TR>
                            <TR><td><BR></td></TR>
                            <TR ><td id="label" align="left" width="75%">Punches Redeemed</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right"><%=redeemedpunches%></TD></TR>
                            <TR><td id="label" align="left" width="75%">Total Revenue</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right" style="color: #38761D">$<%=revenue%></TD></TR>
                            <TR><td><BR></td></TR>
                            <TR><td id="label" align="left" width="75%">Punches Outstanding</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right"><%=outstandingPunches%></TD></TR>
                            <TR><td id="label" align="left" width="75%">Total Outstanding</td><td id="label2" width="5%">:</td><TD width="20%" id="label1" align="right" style="color: #323232">$<%=outstandingRevenue%></TD></TR>
                            </table>--%>
                </div>
		</div>
                      </td>
                <td valign="top" rowspan="2" align="left">

            <div id="busilist" class="boxed1">
                <h1 style="font-size: 16px; font-weight: bold; font-family: Calibri;" class="title"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/>Transaction History</h1>
			<div class="content">
                            
                            <div id="iFrametable">
                                <table width="200px" height="15px" cellspacing="10px" style="margin-left: 20px;">
                                    <TR><TD><a href="redeemed_report.jsp" target="iframe_a" style="font-family:Calibri; font-size:16px;" onclick="CngClass(this);" class="grey" id="vision" > <%--onclick="setTableHeader();"--%>Redeemed</a></TD>
                                    <TD> <a href="issued_report.jsp" style="font-family:Calibri; font-size:16px;" target="iframe_a"  onclick="CngClass(this);" class="grey"><%--onclick="setTableHeader1();">--%>Issued</a> </TD>
               </TR>
            </table>
                   <BR>
                            <table width="560px" border="0" id="" style="background:#efefef;">
                                <TR align="center" style="font-size:16px; color : #F47B27; font-weight: bold;">
                                    <TH width="30%" valign="middle">Date</TH>
                                <%--<TH width="15%">Time</TH>--%>
                                <TH width="30%" valign="middle" id="theadertab">Name</TH>
                                <TH width="30%" valign="middle" id="theadertab">Email</TH>
                                <TH width="10%" valign="middle" id="theadertab">Free Punch</TH>
                            </TR>
                            </table>
                   <iframe src="redeemed_report.jsp" name="iframe_a" width="570px" height="530px" frameborder="0" style="border: 0px solid #963;"></iframe>


                            </div>

			</div>
		</div>
                      </td></tr>
                <tr><td valign="top" align="right">

            <div id="busilist" class="boxed2">
                <h1 style="font-size: 16px; font-weight: bold;" class="title"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/><%=business_name%> PaidPunch Card</h1>
			<div class="content">
                            <div id="label3"><%=noofpunches%> $<%=valueofpunch%> Punches ($<%=cppunch%> Value) for $<%=sppunch%><br>Punches' promotional value expires after <%=expirydays%> days </div>
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
                 %><TR style="color: #B"><%
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

           </td></tr>



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


<%!
      public static float floatRound(float Rval, int Rpl) {
            float p = (float)Math.pow(10,Rpl);
            Rval = Rval * p;
            float tmp = Math.round(Rval);
            return (float)tmp/p;
  }
%>