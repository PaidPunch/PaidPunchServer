<%-- 
    Document   : login
    Created on : Oct 4, 2011, 3:38:16 PM
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
<link href="cssfiles/default.css" rel="stylesheet" type="text/css" />

<script language="javascript">
		function validateemail()
		{

			if(document.userform.email.value=="")
			{
				alert("Enter Email ");
				document.userform.email.focus();
				return false;
			}

			if(!isemail(document.userform.email.value))
			{

				document.userform.email.focus();
				return false;
			}

			if(document.userform.password.value==""||document.userform.password.value==null)
			{
				alert("Enter Password ");
				document.userform.password.focus();
				return false;
			}

			return true;
		}


	function isemail()
	{

		if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(document.userform.email.value))
		{
			return true;
		}
		alert("Email address invalid");
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

function ajaxFunction(valuepassed) {
    
    if(valuepassed==0){
        if(validateemail()==true){
            if(xmlhttp) {

                var email_id = document.getElementById("inputtext1");
                var password = document.getElementById("inputtext2");

                xmlhttp.open("POST","login_validate",true); //getname will be the servlet name
                xmlhttp.onreadystatechange  = handleServerResponse;
                xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                xmlhttp.send("email="+email_id.value+"&password="+password.value); //Posting txtname to Servlet
              }
        }
    }else if(valuepassed==1){
       
        if(document.signupform.secretcode.value==""||document.signupform.secretcode.value==null)
		{
			alert("Enter Secret Code");
			document.signupform.secretcode.focus();

		}
        else{

              if(xmlhttp) {

                var secretcode = document.getElementById("inputtext3");

                xmlhttp.open("POST","checksecretcode",true); //getname will be the servlet name
                xmlhttp.onreadystatechange  = handleServerResponse;
                xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                xmlhttp.send("secretcode=" + secretcode.value); //Posting txtname to Servlet
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
                 
                 document.forms["signupform"].submit();
                 break;
            case 1:
                 alert("Invalid Secret Code");
                 document.signupform.secretcode.value="";
                 document.signupform.secretcode.focus();
                 break;
            case 2:
                 alert("Email has not been verified. Please click the link that has been sent to your mail and try again");
                 document.userform.email.value="";
                 document.userform.password.value="";
                 document.userform.email.focus();
                 break;     
            case 3:
                 alert("Invalid Email or Pasword");
                 document.userform.email.value="";
                 document.userform.password.value="";
                 document.userform.email.focus();
                 break;
            case 4:
                 alert("Invalid Email");
                 document.userform.email.value="";
                 document.userform.password.value="";
                 document.userform.email.focus();
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

</script>

</head>
<body>
<%
System.out.println("A");
%>
<div id="header">
	<div id="topmenu">

	</div>
	<div id="logo">
            <img src="images/paidpunch.png" alt="PaidPunch">
	</div>
</div>





<table style="font-family: Calibri; padding-top: 30px; padding-bottom: 50px;" width="100%" align="center">
    <tbody><tr align="center">

<td style="font-size: 30px; text-align: center; width: 50px; font-weight: bold; color: white;"></td>
<td valign="top">

		<div id="login" class="boxed">
			<h2 class="title">Sign In</h2>
			<div class="content">
				<form id="form1" name="userform" method="get" action="login_validate">
					<fieldset>
					<legend>Sign-In</legend>
                                        <input type="hidden" id="statustext" name="status" value=""/>
					<label for="inputtext1">Email</label>
                                        <input id="inputtext1" type="text" name="email" value="" maxlength="35"/><br><br>
					<label for="inputtext2">Password</label>
					<input id="inputtext2" type="password" name="password" value="" maxlength="30"/><br><br>
					<input id="inputsubmit1" type="button" name="inputsubmit1" value="Sign In" onClick = "ajaxFunction('0');"/>
					<p><a href="forgot_password.jsp">Forgot Password?</a></p>
					</fieldset>
				</form>
			</div>
		</div>
</td>
        </tr><tr><td><BR></td></tr><tr align="center">
<td style="font-size: 30px; text-align: center; width: 50px; font-weight: bold; color: white;"></td>
<td valign="center">

		<div id="partners" class="boxed">
			<h2 class="title">Sign Up</h2>
			<div class="content">
				<form id="form2" name="signupform" method="post" action="signup_business.jsp">
					<fieldset>
					<legend>Sign-Up</legend>
					<label for="inputtext3">Secret Code</label>
					<input id="inputtext3" type="text" name="secretcode" value="" maxlength="20"/><br><br>
					<input id="inputsubmit2" type="button" name="signupsubmit" value="Sign Up" onClick="ajaxFunction(1);"/>

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
