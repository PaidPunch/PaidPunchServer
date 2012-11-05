<%--
    Document   : business_admin_user
    Created on : Dec 13, 2011, 5:47:02 PM
    Author     : Shahid
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
<script language="javaScript" type="text/javascript" src="scripts/calendar.js"></script>
<link href="cssfiles/calendar.css" rel="stylesheet" type="text/css">
<script language="javascript">

        function getXMLObject()  //XML OBJECT
        {
           var xmlHttp = false;
           try {
             xmlHttp = new ActiveXObject("Msxml2.XMLHTTP")  // For Old Microsoft Browsers
           }
           catch (e) {
             try {
               xmlHttp = new ActiveXObject("Microsoft.XMLHTTP")  // For Microsoft IE 6.0+
             }
             catch (e2) {
               xmlHttp = false   // No Browser accepts the XMLHTTP Object then false
             }
           }
           if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
             xmlHttp = new XMLHttpRequest();        //For Mozilla, Opera Browsers
           }
           return xmlHttp;  // Mandatory Statement returning the ajax object created
        }

	var xmlhttp = new getXMLObject();//xmlhttp holds the ajax object

        function ajaxFunction(btn_pressed) {
           if(checkIfRadioSelected()==true)
               {
                   if(btn_pressed==1){
                       if(document.busilist.changedDate.value==""){
                           alert("Date has not been Changed");
                       }
                       else{
                           if(xmlhttp) {

                                var business_id = document.getElementById("hidden1");
                                var expiryDate = document.getElementById("hidden2");

                                xmlhttp.open("GET","save_delete_admin?busid="+business_id.value+"&expirydt="+expiryDate.value,true); //getname will be the servlet name
                                xmlhttp.onreadystatechange  = handleServerResponse;
                                xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                                xmlhttp.send(null); //Posting txtname to Servlet
                          }
                       }

                       
                   }
                   else{
                       if(btn_pressed==2){
                           var r=confirm("Are you sure you want to disable?");
                            if (r==true)
                              {
                                  if(xmlhttp) {

                                        var business_id = document.getElementById("hidden1");
                                        
                                        xmlhttp.open("POST","save_delete_admin",true); //getname will be the servlet name
                                        xmlhttp.onreadystatechange  = handleServerResponse;
                                        xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                                        xmlhttp.send("busid="+business_id.value+"&transtype=00"); //Posting txtname to Servlet
                                  }
                              }
                            else
                              {
                             
                              }

                       }else if(btn_pressed==3){
                           var r=confirm("Are you sure you want to enable?");
                            if (r==true)
                              {
                                  if(xmlhttp) {

                                        var business_id = document.getElementById("hidden1");

                                        xmlhttp.open("POST","save_delete_admin",true); //getname will be the servlet name
                                        xmlhttp.onreadystatechange  = handleServerResponse;
                                        xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                                        xmlhttp.send("busid="+business_id.value+"&transtype=01"); //Posting txtname to Servlet
                                  }
                              }
                            else
                              {

                              }

                       }
                   }
               }
                
           }

                    

        function handleServerResponse() {
   if (xmlhttp.readyState == 4) {
     if(xmlhttp.status == 200) {
       var resptext = xmlhttp.responseText;
       //Update the HTML Form element
       resptext = parseInt(resptext);

       switch (resptext)
        {

            case 0:
                 alert("Could not Update");
                 break;
            case 1:
                 alert("Updated Successfully");
                 document.busilist.changedDate.value="";
                 break;
            
            case 3:
                 alert("Business could not be deleted. Try later");
                 break;
            case 4:
                 <%--alert("Business Deleted Successfully");--%>
                 location.href="manage_business.jsp";
                 break;
            case 5:
                 alert("Invalid Password");
                 document.userform.password.value="";
                 document.userform.password.focus();
                 break;
            case 6:

                 document.userform.status.value=resptext;
                 document.forms["userform"].submit();
                 break;
             case 7:

                 document.userform.status.value=resptext;
                 document.forms["userform"].submit();
                 break;
        }
     }
     else {
         if(xmlhttp.status == 500) {
	        alert("Session TimeOut. Unable To Process Your Request");
                //Process Further
          }
          else {
         alert("Error during AJAX call. Please try again");
        }
       }
    }
}

        function setRadioSelected(busid){
            
           var radios = document.getElementsByName('bussid');
            var val;
            for (var i = 0; i < radios.length; i++) {
               if (radios[i].type == 'radio' && radios[i].value==busid) {
                // get value, set checked flag or do whatever you need to
                     radios[i].checked = true;
                     setHiddenValue(busid);
                     break;

                }

            }

}

            function checkIfRadioSelected(){
                var radios = document.getElementsByName('bussid');
                var val;
                for (var i = 0; i < radios.length; i++) {

                     if (radios[i].type == 'radio' && radios[i].checked) {
                    // get value, set checked flag or do whatever you need to
                         val = radios[i].value;

                    }

                }
                if(val==null){
                    alert("No Business Selected");
                    return false;
                }

                return true;
            }

            function setHiddenValue(busidpassed){
                document.busilist.radioval.value= busidpassed;
            }

            function viewBusinessClicked(){
                if(checkIfRadioSelected()==true){
                    document.forms["busilist"].submit();
                }
                

            }

            function setChangedDate(val){
                alert(val);
                alert(document.getElementsByName(val).value);
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
        
	margin-top:10px;
	margin-bottom: 20px;
	background: #F7F7F7 ;
	border: 1px solid #CCCCCC;
	width:760px;
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






#inputsubmit1, #inputsubmit2, #inputsubmit3, #inputsubmit4 {
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

	position:fixed;
       bottom:0;
   width:100%;
   background: #F47B27 url(images/orange_gradient.png) repeat-x 0 0;
   height : 25px;

}


div.tableContainer {
	clear: both;
	border: 0px solid #963;
	height: 330px;
	overflow: auto;
	width:750px;
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
	text-align: left;
        color:#F47B27;
        font-size: 15px;
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
        height:30px;
}

tbody.scrollContent tr.alternateRow td {
	background: #EEE;
	font-size: 14px;
        color: #323232;
	padding: 2px 3px 3px 4px;
        font-family: Calibri;
        height:30px;
}

tbody.scrollContent td {
	width: 30px
}

tbody.scrollContent td + td {
	width: 200px
}

tbody.scrollContent td + td + td {
	width: 250px
}

tbody.scrollContent td + td + td +td {
	width: 200px
}

tbody.scrollContent td + td + td + td + td{
	width: 200px
}

#busilist #expdate{
	width: 70px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 0px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
        font-size: 15px;

}

#busilist #expdate1{
	width: 70px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 0px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
        background: #EEE;
        font-size: 15px;
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
    long totalpunchessold=0,totalredeemedpunches=0,totalbusinesscreated=0,totalRevenue1=0;
    long totalfreepunchesdownloaded=0, totalfreepunchesredeemed=0, totalmysteryredeemed=0,jackpotmysteryredeemed=0;

    float totalRevenue=0,avg_value_of_punches=0, totalpunchvalue=0;
    int pid = 0;

    String maxsoldBusinessName="",maxRedeemedBusinessName="";


            DBConnection db = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                               db = new DBConnection();
                               stmt = db.stmt;

                               try{

                               String query = "SELECT COUNT(*) FROM punch_card";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalbusinesscreated = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }


                               try{

                               //String query = "SELECT COUNT(*) FROM punchcard_download";
                                   //String query = "SELECT punch_card.no_of_punches_per_card FROM punchcard_download,punch_card where punch_card.punch_card_id=punchcard_download.punch_card_id;";
                               String query = "SELECT punch_card.no_of_punches_per_card FROM punchcard_download,punch_card where punchcard_download.isfreepunch='false' and punch_card.punch_card_id=punchcard_download.punch_card_id;";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              while(rs.next()){
                                totalpunchessold = totalpunchessold + rs.getInt(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }

                                try{

                               //String query = "SELECT COUNT(*) FROM punchcard_download";
                                   //String query = "SELECT punch_card.no_of_punches_per_card FROM punchcard_download,punch_card where punch_card.punch_card_id=punchcard_download.punch_card_id;";
                               String query = "SELECT count(*) FROM punchcard_download where punchcard_download.isfreepunch='true'";
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
                               String query = "Select COUNT(*) from punch_card_tracker, punchcard_download where punchcard_download.isfreepunch='false' and punchcard_download.punch_card_downloadid=punch_card_tracker.punch_card_downloadid;";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalredeemedpunches = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }


                              try{

                               //String query = "SELECT COUNT(*) FROM punch_card_tracker";
                               String query = "Select COUNT(*) from punch_card_tracker, punchcard_download where punchcard_download.isfreepunch='true' and punchcard_download.punch_card_downloadid=punch_card_tracker.punch_card_downloadid;";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalfreepunchesredeemed = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }

                              com.server.Constants.logger.info("\nTotal Punch Cards Sold"+totalpunchessold+"\nNo of Punches Redeemed : "+totalredeemedpunches);

                              try{

                              // String query = "SELECT punch_card.disc_value_of_each_punch FROM punch_card_tracker,punch_card where punch_card.punch_card_id=punch_card_tracker.punch_card_id";
                               String query = "SELECT punch_card.disc_value_of_each_punch FROM punch_card_tracker,punch_card,punchcard_download where punchcard_download.isfreepunch='false' and punchcard_download.punch_card_downloadid=punch_card_tracker.punch_card_downloadid and punch_card.punch_card_id=punch_card_tracker.punch_card_id;";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              while(rs.next()){
                                totalRevenue = totalRevenue+rs.getFloat(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }
                              //totalRevenue1 = (long)totalRevenue;
                              System.out.println("Total Revenue : "+totalRevenue);
                              
                              totalRevenue = Round(totalRevenue,2);

                              try{

                               //String query = "Select COUNT(*) FROM punch_card_tracker where is_mystery_punch='true';";
                               String query = "Select COUNT(*) FROM punchcard_download where mystery_punchid IS NOT NULL;";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalmysteryredeemed = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }


                              try{

                               //String query = "Select COUNT(*) FROM punch_card_tracker where is_mystery_punch='true';";
                               String query = "SELECT COUNT(*) FROM punchcard_download,mystery_punch where punchcard_download.mystery_punchid=mystery_punch.mystery_id and mystery_punch.paidpunchmystery='Y';";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                jackpotmysteryredeemed = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }


                               try{
                               long countofgroupby=0;
                               String query = "Select punch_card_id,COUNT(punch_card_id) from punchcard_download GROUP BY punch_card_id";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                                    int temppid = 0;
                                    long maxCount = 0;

                                  while(rs.next()){
                                    pid = rs.getInt(1);
                                    countofgroupby = rs.getLong(2);
                                    System.out.println("pid : "+pid+" count of groupby for punchcarddownload : "+countofgroupby);
                                    if(countofgroupby>maxCount){
                                        temppid = pid;
                                        maxCount = countofgroupby ;
                                    }
                                    System.out.println("Temporary ID punchcard_download: "+temppid);
                                  }

                               String query1 = "SELECT business_users.business_name FROM business_users,punch_card where punch_card.punch_card_id="+temppid+" and business_users.business_userid=punch_card.business_userid;";
                               rs = stmt.executeQuery(query1);
                               com.server.Constants.logger.info("The select query is " + query1);
                               if(rs.next()){
                                    maxsoldBusinessName = rs.getString(1);
                               }

                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }

                              try{
                                long countofgroupby=0;
                               String query = "Select punch_card_id,COUNT(punch_card_id) from punch_card_tracker GROUP BY punch_card_id";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                                    int temppid = 0;
                                    long maxCount = 0;
                                  while(rs.next()){
                                    pid = rs.getInt(1);
                                    countofgroupby = rs.getLong(2);
                                    System.out.println("pid : "+pid+" count of groupby for punch_card_tracker : "+countofgroupby);

                                    if(countofgroupby>maxCount){
                                        temppid = pid;
                                        maxCount = countofgroupby ;
                                    }
                                    System.out.println("Temporary ID punch_card_tracker : "+temppid);
                                  }

                               String query1 = "SELECT business_users.business_name FROM business_users,punch_card where punch_card.punch_card_id="+temppid+" and business_users.business_userid=punch_card.business_userid;";
                               rs = stmt.executeQuery(query1);
                               com.server.Constants.logger.info("The select query is " + query1);
                               if(rs.next()){
                                    maxRedeemedBusinessName = rs.getString(1);
                               }

                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }

                               try{

                               String query = "SELECT value_of_each_punch FROM punch_card";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              while(rs.next()){
                                totalpunchvalue = totalpunchvalue+rs.getFloat(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }
                              if(totalbusinesscreated==0){
                                  avg_value_of_punches = 0;
                              }else{
                                avg_value_of_punches = totalpunchvalue/totalbusinesscreated;
                                avg_value_of_punches = Round(avg_value_of_punches, 2);
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


                   long totaloutstandingpunches = totalpunchessold-totalredeemedpunches;

%>


	<table style="font-family: Arial,Helvetica,sans-serif; padding-top: 10px; padding-bottom: 50px;" width="100%">
            <tbody>
            <tr>


<td valign="top" align="center">

		<div id="login" class="boxed">
                    <h2 class="title"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/>Business Overview for PaidPunch</h2>
			<div class="content">
				
                               
                                <Table width="98%">
                                    <TR ><TD id="label" width="30%" align="left">Total Business Created </TD><TD width="3%" id="label2">:</TD><TD width="15%" id="label1" align="left"><%=totalbusinesscreated%></TD><TD width="30%" id="label" align="left">Total Revenue Generated</TD><TD width="3%" id="label2">:</TD><TD width="25%" id="label1" align="right">$<%=totalRevenue%></TD></TR>
                                    <TR><TD width="30%" id="label" align="left">Total Punches Sold </TD><TD width="3%" id="label2">:</TD><TD width="15%" id="label1" align="left"><%=totalpunchessold%></TD><TD id="label" width="30%" align="left">Maximum Punch Cards sold by</TD><TD width="3%" id="label2">:</TD><TD width="25%" id="label1" align="right"><%=maxsoldBusinessName%></TD></TR>
                                     <TR><TD id="label" width="30%" align="left">Total Punches Redeemed</TD><TD width="3%" id="label2">:</TD><TD width="15%" id="label1" align="left"><%=totalredeemedpunches%></TD><TD width="30%" id="label" align="left">Maximum Punches redeemed of</TD><TD width="3%" id="label2">:</TD><TD id="label1" width="25%" align="right"><%=maxRedeemedBusinessName%></TD></TR>
                                     <TR><TD id="label" width="30%" align="left">Total Punches Outstanding</TD><TD width="3%" id="label2">:</TD><TD width="15%" id="label1" align="left"><%=totaloutstandingpunches%></TD><TD id="label" width="30%" align="left">Average Value of Punches</TD><TD width="3%" id="label2">:</TD><TD width="25%" id="label1" align="right"><%=avg_value_of_punches%></TD></TR>
                                     <TR><TD id="label" width="30%" align="left">Total Free Punches Unlocked</TD><TD width="3%" id="label2">:</TD><TD width="15%" id="label1" align="left"><%=totalfreepunchesdownloaded%></TD><TD id="label" width="30%" align="left">Total Mystery Punches Redeemed</TD><TD width="3%" id="label2">:</TD><TD width="25%" id="label1" align="right"><%=totalmysteryredeemed%></TD></TR>
                                     <TR><TD id="label" width="30%" align="left">Total Free Punches Redeemed</TD><TD width="3%" id="label2">:</TD><TD width="15%" id="label1" align="left"><%=totalfreepunchesredeemed%></TD><%--<TD id="label" width="33%" align="left">Jackpot Mystery Punches Redeemed</TD><TD width="3%" id="label2">:</TD><TD width="22%" id="label1" align="right"><%=jackpotmysteryredeemed%></TD>--%></TR>
                                    
                                </Table>
                                     
			</div>
		</div>
</td>
</tr>

            <form id="form1" name="busilist" action="admin_business_view.jsp" method="post">
            <tr><td valign="top" align="center">
                    <input id="hidden1" type="hidden" value="" name="radioval" readonly="readonly"/>
                    <input id="hidden2" type="hidden" value="" name="changedDate" readonly="readonly"/>
            <div id="busilist" class="boxed">
                <h2 class="title"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/>Business List</h2>
			<div class="content">
				

                                    <div id="tableContainer" class="tableContainer">
<table class="scrollTable" border="0" cellpadding="0" cellspacing="0" width="100%">
<thead class="fixedHeader">

    <%

    
    String expiryDate[];
    String punchCardName[];
    String businessName[];
    String business_emailid[];
    String business_number[];
    String business_enabled[];
    int businessId[];
    
   
           // DBConnection db = null;
           // Statement stmt = null;
           // ResultSet rs = null;

            try {
                               db = new DBConnection();
                               stmt = db.stmt;

                               String query = "SELECT business_users.business_userid,business_users.business_name,business_users.email_id,business_users.contactno,business_users.busi_enabled, punch_card.punch_card_name,punch_card.expiry_date FROM business_users,punch_card where business_users.role='user' and punch_card.business_userid=business_users.business_userid";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                              rs.last();
                              int row = rs.getRow();

                              businessId = new int[row];
                              businessName = new String[row];
                              expiryDate = new String[row];
                              punchCardName = new String[row];
                              business_emailid = new String[row];
                              business_number = new String[row];
                              business_enabled = new String[row];
                               //String query = "SELECT punch_card_name,qrcode from punch_card where business_userid=(Select business_userid from business_users where email_id='"+username+"')";
                               //query = "SELECT business_users.business_userid,business_users.business_name,punch_card.punch_card_name,punch_card.expiry_date FROM business_users,punch_card where business_users.role='user' and punch_card.business_userid=business_users.business_userid";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                              // displaying records
                                int i=0;
                               
                              while(rs.next()){
                               // int busid = rs.getInt(1);
                                businessId[i] = rs.getInt(1);
                                businessName[i] = rs.getString(2);
                                business_emailid[i] = rs.getString(3);
                                business_number[i] = rs.getString(4);
                                business_enabled[i] = rs.getString(5);
                                punchCardName[i] = rs.getString(6);
                                expiryDate[i] = rs.getDate(7).toString();
                                System.out.println(businessId[i]+" "+businessName[i]+" "+business_emailid[i]+" "+punchCardName[i]+" "+expiryDate[i]);
                               
                                expiryDate[i] = expiryDate[i].replace("-", "/");
                                System.out.println("Expiry Date"+expiryDate[i]);
                                String str1[]=expiryDate[i].split("/");
                                expiryDate[i]=str1[2]+"-"+str1[1]+"-"+str1[0];
                                 i++;
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


<tr class="alternateRow" align="left">
                <th></th>
		<th>Business Name</th>
		<th>Email</th>
                <th>Business Number</th>
                <th>Enabled</th>
		<%--<th>Expiry Date</th>--%>
	</tr>
</thead>
<tbody class="scrollContent">
    <%
            for(int i=1;i<=businessId.length;i++){
                if(i % 2 != 0){
    %>
    <tr class="normalRow" valign="middle">
                <td align="center"><input type="radio" class="radiodata" name="bussid" value="<%=businessId[i-1]%>" onClick="setHiddenValue(<%=businessId[i-1]%>);"></td> <%--javascript:busilist.radioval.value= '<%=businessId[i-1]%>' " ></td>--%>
		<td align="left"><%=businessName[i-1]%></td>
                <td align="left"><%=business_emailid[i-1]%></td>
                <td align="left"><%=business_number[i-1]%></td>
                <td align="left"><%=business_enabled[i-1]%></td>
                <%--<td align="left"><input type="text" name="datum<%=i%>" id="expdate" value="<%=expiryDate[i-1]%>" readonly="readonly"><a href="#" onClick="setRadioSelected(<%=businessId[i-1]%>); setYears(2010, 2020); showCalender(this, 'datum<%=i%>', 'changedDate');">
      <img src="images/calendar.png" alt="calendar"></a></td>--%>
	</tr>
        <%
                }else{
%>
<tr class="alternateRow" valign="middle">
                <td  align="center" ><input type="radio" class="radiodata" name="bussid" value="<%=businessId[i-1]%>" onClick="setHiddenValue(<%=businessId[i-1]%>);"></td> <%-- javascript:busilist.radioval.value= '<%=businessId[i-1]%>'"></td>--%>
		<td align="left"><%=businessName[i-1]%></td>
                <td align="left"><%=business_emailid[i-1]%></td>
                <td align="left"><%=business_number[i-1]%></td>
                <td align="left"><%=business_enabled[i-1]%></td>
		<%--<td align="left"><input type="text" name="datum<%=i%>" id="expdate1" value="<%=expiryDate[i-1]%>" readonly="readonly"><a href="#" onClick="setRadioSelected(<%=businessId[i-1]%>); setYears(2010, 2020); showCalender(this, 'datum<%=i%>', 'changedDate');">
      <img src="images/calendar.png" alt="calendar"></a></td>--%>
	</tr>
	<%
                }
            }
        %>


</tbody>
</table>
</div>

                                
			</div>
		</div>


 <!-- Calender Script  -->

<%-- <table id="calenderTable"  >
        <tbody id="calenderTableHead" >
          <tr >
            <td colspan="4" align="center">
	          <select onChange="showCalenderBody(createCalender(document.getElementById('selectYear').value,
	           this.selectedIndex, false));" id="selectMonth">
	              <option value="0">Jan</option>
	              <option value="1">Feb</option>
	              <option value="2">Mar</option>
	              <option value="3">Apr</option>
	              <option value="4">May</option>
	              <option value="5">Jun</option>
	              <option value="6">Jul</option>
	              <option value="7">Aug</option>
	              <option value="8">Sep</option>
	              <option value="9">Oct</option>
	              <option value="10">Nov</option>
	              <option value="11">Dec</option>
	          </select>
            </td>
            <td colspan="2" align="center">
			    <select onChange="showCalenderBody(createCalender(this.value,
				document.getElementById('selectMonth').selectedIndex, false));" id="selectYear">
				</select>
			</td>
            <td align="center">
			    <a href="#" onClick="closeCalender();"><font color="#ffffff" size="+1">X</font></a>
			</td>
		  </tr>
       </tbody>
       <tbody id="calenderTableDays">
         <tr>
           <td >Sun</td><td>Mon</td><td>Tue</td><td>Wed</td><td>Thu</td><td>Fri</td><td>Sat</td>
         </tr>
       </tbody>
       <tbody id="calender"></tbody>
    </table>--%>

<!-- End Calender Script  -->


                        </td></tr>


<tr align="center">


<td valign="top" align="center" >
    <input id="inputsubmit1" type="button" name="view_btn" value="View" onClick="viewBusinessClicked();"/>
    <%--<input id="inputsubmit2" type="button" name="saveexpirydate" value="Save" onClick="ajaxFunction(1);"/>--%>
    <input id="inputsubmit3" type="button" name="delete_btn" value="Disable" onClick="ajaxFunction(2);" />
    <input id="inputsubmit4" type="button" name="enable_btn" value="Enable" onClick="ajaxFunction(3);" /><br><br>

</td>

</tr>



<tr><td><br></td></tr>
</form>
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
public static float Round(float Rval, int Rpl) {
  float p = (float)Math.pow(10,Rpl);
  Rval = Rval * p;
  float tmp = Math.round(Rval);
  return (float)tmp/p;
  }
%>
