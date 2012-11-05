<%-- 
    Document   : emailnotification
    Created on : Oct 12, 2011, 6:24:17 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
    <%
            String emailid = "";
            String secretcode="";
            try{
                     secretcode = session.getAttribute("secretcode").toString();
                     emailid =  session.getAttribute("email").toString();
                     
                     
               }
            catch(Exception e){
                response.sendRedirect("login.jsp");
            }

             if(secretcode==null||secretcode==""){
                        response.sendRedirect("login.jsp");
                    }
           else{
      %>
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
	margin-top:60px;
	margin-bottom: 110px;
	background: #F7F7F7 url(images/tables_long.PNG) repeat-x;
	border: 1px solid #CCCCCC;
	width:460px;

}

.boxed .title {
	height: 29px;
	padding: 7px 0 0 40px;
	background: url(images/arrows.png) no-repeat 10px 50%;
	text-transform: uppercase;
	font-size: 1.2em;
	color: #FFFFFF;
        text-align: left;
        font-family: Calibri;
}

.boxed .content {
	padding: 10px;
}

/* Header */

#header {
	height: 100px;
	margin: 0 auto;
	background: url(images/bg.png) repeat-x;
}

/* Header > Logo */

#logo {
	margin-left: 20px ;
	text-align:center;
	padding: 10px 0 0 0;
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

#login label {
	margin-bottom: .5em;
	font: normal medium Calibri;
	padding: 2px 5px;
	color : #323232;
        text-align:center;
        font-size: 16px;
}

#login label1 {
	display: block;
	font: normal medium Calibri;
        font-weight: bold;
        font-size: 20px;
        color : #323232;
        text-align:center;
}


#inputsubmit1, #inputcancel {
	width : 67px;

	font-size: 15px;
        color : #ffffff;
        margin-right: 20px;
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
    height: 25px;
	position:fixed;
   bottom:0;
   width:100%;
   background: #F47B27 url(images/orange_gradient.png) repeat-x 0 0;

}



    </style>

</head>
<body>

<%

            session.setAttribute("email","");
            session.setAttribute("password","") ;
            session.setAttribute("businessname","") ;
            session.setAttribute("businessdesc","") ;
            session.setAttribute("filepath","") ;
            session.setAttribute("businessaddress","") ;
            session.setAttribute("city","") ;
            session.setAttribute("state","") ;
            session.setAttribute("pincode","") ;
            session.setAttribute("countrycode","") ;
            session.setAttribute("contactnumber","") ;
            session.setAttribute("secretcode","");
            session.setAttribute("contactname", "");


            session.removeAttribute("email") ;
            session.removeAttribute("password") ;
            session.removeAttribute("businessname") ;
            session.removeAttribute("businessdesc") ;
            session.removeAttribute("filepath") ;
            session.removeAttribute("businessaddress") ;
            session.removeAttribute("city") ;
            session.removeAttribute("state") ;
            session.removeAttribute("pincode") ;
            session.removeAttribute("countrycode") ;
            session.removeAttribute("contactnumber") ;
            session.removeAttribute("secretcode") ;
            session.removeAttribute("contactname");
            session.invalidate();

%>
<div id="header">
	<div id="topmenu">

	</div>
	<div id="logo">
		<img src="images/paidpunch.png">
	</div>
</div>


<div id="content">

	</div>


	<table style="font-family: Arial,Helvetica,sans-serif; padding-top: 10px; padding-bottom: 50px;" width="100%">
	<tbody><tr>


<td valign="top" align="center">

		<div id="login" class="boxed">
			<h1 class="title">Congratulations!!</h1>
			<div class="content">
				<form id="form1" name="user" method="post" action="login.jsp">
					<fieldset>
        <%-- Changed on Tony's Request ******** --%>
					<%--<legend>Email Verification</legend>
                                        <label1>You’re almost done!</label1><BR>
					<label >We have sent an email to <B><%=emailid%></B><br>Please click the link within the email to verify your email address.</label><br><br><br>
					<input id="inputsubmit1" type="submit" name="inputsubmit1" value="OK" /><BR><BR>--%>
					<legend>Email Verification</legend>
                                        <%--<label1>Congratulations!!</label1><BR>--%>
					<label>Your PaidPunch Business has been set up successfully.</label><br><br><br>
					<input id="inputsubmit1" type="submit" name="inputsubmit1" value="OK" /><BR><BR>
					</fieldset>
				</form>
			</div>
		</div>
</td>
</tr>

</tbody></table>



<div id="footer">
	<p id="legal"></p>
</div>
</body>
</html>
<%
}
%>