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

        function ajaxFunction() {
            
               if(xmlhttp) {
                  
                    xmlhttp.open("GET","checksecretcode?ignoreMe=" + new Date().getTime(),true); //getname will be the servlet name
                    xmlhttp.onreadystatechange  = handleServerResponse;
                    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xmlhttp.send(); //Posting txtname to Servlet
                  }
       

        }

        function handleServerResponse() {
           if (xmlhttp.readyState == 4) {
             if(xmlhttp.status == 200) {
               var resptext = xmlhttp.responseText;
               //Update the HTML Form element
                     document.user.securitycode.value=resptext;

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
	background: #F7F7F7 url(images/tables_long.PNG) repeat-x;
	border: 1px solid #CCCCCC;
	width:620px;

}

.boxed .title {
	height: 29px;
	padding: 7px 0 0 40px;
	background: url(images/arrows.png) no-repeat 10px 50%;
	text-transform: uppercase;
	font-size: 1.2em;
	color: #FFFFFF;
        text-align: left;
}

.boxed1 {


	margin-bottom: 10px;
	background: #efefef url(images/tables_long.PNG) repeat-x;
	border: 1px solid #CCCCCC;
	width:620px;

}

.boxed1 .title {
	height: 29px;
	padding: 7px 0 0 40px;
	background: url(images/arrows.png) no-repeat 10px 50%;
	text-transform: uppercase;
	font-size: 1.2em;
	color: #FFFFFF;
        text-align: left;
}

.boxed .content {
	padding: 10px;
}
.boxed1 .content {
	padding: 10px;
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



#login label {
        padding: 2px 5px;
	
	font: normal medium Calibri;
        font-size:16px;
        color : #323232;
        
}

#login #inputtext1{
	width: 180px;
	
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	
}






#inputsubmit1, #inputcancel {
	width : 67px;
	font-size: 15px;
        color : #ffffff;
	font-family: calibri;
        font-weight: bold;
        background: url(images/button_base.png) repeat;
        margin-left: 20px;
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
   height: 25px;
   background: #F47B27 url(images/orange_gradient.png) repeat-x 0 0;

}

#commentstd {
    padding: 2px 5px;

	font: normal medium Calibri;
        font-size:16px;
        color : #323232;
}

div.tableContainer {
	clear: both;
	border: 0px solid #963;
	height: 250px;
	overflow: auto;
	
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
                    <li  class="navtext-sel"><a href="business_admin_user.jsp">Home</a>

                    </li>
                    <li><a href="manage_business.jsp">Businesses</a>

                    </li>
                    <li><a href="overview_business.jsp">Users</a>

                    </li>

                   
                </ul>
                <a href="signout.jsp" style="color: #323232; float: right; height: 32px; margin-top: 10px; margin-left: 0;">Signout</a>

        </div>
        <br>


	<table style="font-family: Calibri;  padding-bottom: 50px;" width="100%">
	<tbody><tr>


<td valign="top" align="center">

		<div id="login" class="boxed">
                    <h1 class="title" style="font-family: Calibri; font-size: 17px;">Generate Secret Code</h1>
			<div class="content">
				<form id="form1" name="user" method="post" action="forgot_pass">
					<fieldset>
					
					<label for="inputtext1">This will generate a key for a Business to Signup</label><br><br><input id="inputtext1" type="text" name="securitycode" value="" readonly="readonly"/>
					<input id="inputsubmit1" type="button" name="generate" value="Generate" onClick = "ajaxFunction();"/><br>
					
					</fieldset>
				</form>
			</div>
		</div>
</td>
</tr>
<tr><td><br></td></tr>
<tr>


<td valign="top" align="center">

		<div id="login" class="boxed1">
                    <h1 class="title" style="font-family: Calibri; font-size: 17px;">Feedback</h1>
			<div class="content">
                            <div id="tableContainer" class="tableContainer">
                            <table width="100%" class="scrollTable">
					<fieldset><%

           String feedback_text="",email_id="",feedback_date="",feedback_time="";

            DBConnection db = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                               db = new DBConnection();
                               stmt = db.stmt;

                               try{

                               //String query = "SELECT feedback.feedback_data,feedback_date,feedback_time,app_user.email_id FROM feedback,app_user where app_user.user_id=feedback.app_user_id";
                                   String query = "SELECT feedback.feedback_data,feedback_date,feedback_time,app_user.email_id FROM feedback,app_user where app_user.user_id=feedback.app_user_id Order By feedback.feedback_date desc,feedback.feedback_time desc LIMIT 30;";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                               if(!rs.next()){
                                    %>
                                    <TR><TD style="padding: 5px 7px; font: normal medium Calibri; font-size:17px; color : #323232;" align="center">There is no Feedback!</TD></TR>
<%
                               }
                               else{
                                rs = stmt.executeQuery(query);
                              while(rs.next()){
                               feedback_text = rs.getString(1);
                               feedback_date = rs.getDate(2).toString();
                               feedback_time = rs.getTime(3).toString();
                               email_id = rs.getString(4);
                               %>
                                   <TR><TD >
                                          <table width="100%" >
                                              <TR align="left" style="background: #efefef url(images/callout.png) no-repeat; " height="65px" valign="middle">
                                                  <TD style="padding: 5px 20px; font: normal medium Calibri; font-size:17px; color : #323232; " colspan="3" >
                                                        <%=feedback_text%>
                                                  </TD>
                                              </TR>
                                              <TR>
                                                  <TD align="left" id="commentstd" width="60%" valign="top">
                                                        <%=email_id%>
                                                  </TD>
                                                  <TD style="padding: 2px 5px; font: normal medium Calibri; font-size:16px; color : #323232;" width="20%" valign="top">
                                                        <%=feedback_date%>
                                                  </TD>
                                                  <TD style="padding: 2px 5px; font: normal medium Calibri; font-size:16px; color : #323232;" width="20%" valign="top">
                                                        <%=feedback_time%>
                                                  </TD>
                                              </TR>
                                          </table></TD>
                                   </TR>
                                   <TR><TD><BR></TD></TR>
         <%

                              }
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

					</fieldset>
			    </table>
                            </div>
			</div>
		</div>
</td>
</tr>

<tr align="center">

<!--<td style="font-size: 30px; text-align: center; width: 50px; font-weight: bold; color: white;"></td>-->
<td valign="top" >
</td>

</tr>

<tr><td><br></td></tr>
</tbody></table>



<div id="footer">
	<p id="legal"></p>
</div>
</body>
</html>
<%
  }
 %>