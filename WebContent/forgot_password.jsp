<%-- 
    Document   : forgotpassword
    Created on : Oct 4, 2011, 12:57:33 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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

<script language="javascript">
		function validateemail()
		{
			if(document.user.email.value=="")
			{
				alert("Please Enter Email ");
				document.user.email.focus();
				return false;
			}

			if(!isemail(document.user.email.value))
			{

				document.user.email.focus();
				return false;
			}

			return true;
		}


	function isemail()
	{

		if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(document.user.email.value))
		{
			return true;
		}
		alert("Please Enter Valid Email");
		return false;
	}

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
            if(validateemail()==true){
               if(xmlhttp) {
                   var email_id = document.getElementById("inputtext1");

                    xmlhttp.open("GET","forgot_pass?email=" + email_id.value,true); //getname will be the servlet name
                    xmlhttp.onreadystatechange  = handleServerResponse;
                    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xmlhttp.send(); //Posting txtname to Servlet
                  }
                }
            
        }

        function handleServerResponse() {
           if (xmlhttp.readyState == 4) {
             if(xmlhttp.status == 200) {
               var resptext = xmlhttp.responseText;
               //Update the HTML Form element
               if(resptext==00){
                   
                   document.forms["user"].submit();
               }
               else{

                   alert("Email Address does not exist");
                   document.user.email.value="";
                   document.user.email.focus();
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


</script>

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
	width:400px;

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
	float:left;
        color : #323232;
        text-align:center;
        font-size: 16px;
        
}

#login #inputtext1{
	width: 300px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	
}






#inputsubmit1, #inputsubmit2 {
	width : 67px;

	font-size: 15px;
        color : #ffffff;
        margin-right: 20px;
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
        height:25px;
	position:fixed;
   bottom:0;
   width:100%;
   background: #F47B27 url(images/orange_gradient.png) repeat-x 0 0;

}



    </style>

</head>
<body>

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
			<h2 class="title">Forgot Password</h2>
			<div class="content">
				<form id="form1" name="user" method="post" action="forgot_pass">
					<fieldset>
					<legend>Forgot Password</legend>
					<label for="inputtext1">Enter your email below and we will send you the password!</label><input id="inputtext1" type="text" name="email" value="" maxlength="35"/><br><br>
					<input id="inputsubmit1" type="button" name="inputsubmit1" value="OK" onClick = "ajaxFunction();"/><input id="inputsubmit2" type="button" name="cancel" value="Cancel" onClick = "location.href='login.jsp'"/>
					
					</fieldset>
				</form>
			</div>
		</div>
</td>
</tr>

<tr align="center">


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
