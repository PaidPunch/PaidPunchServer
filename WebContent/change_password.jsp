<%--
    Document   : business_admin_user
    Created on : Dec 13, 2011, 5:47:02 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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

        function validateallFields()
	{
              
			if(document.user.oldpassword.value==""||document.user.oldpassword.value==null)
			{
				alert("Enter Old Password ");
				document.user.oldpassword.focus();
				return false;
			}

			if(document.user.newpassword.value==""||document.user.newpassword.value==null)
			{
                                alert("Enter New Password ");
				document.user.newpassword.focus();
				return false;
			}

			if(document.user.confirmpassword.value==""||document.user.confirmpassword.value==null)
			{
				alert("Enter Confirm Password ");
				document.user.confirmpassword.focus();
				return false;
			}

                        if(document.user.newpassword.value!=document.user.confirmpassword.value)
                         {
				alert("New Password and Confirm Password do not match");
				document.user.confirmpassword.focus();
				return false;
			}

                        if(document.user.oldpassword.value==document.user.newpassword.value)
                         {
				alert("Old Password and new Password should not be same");
				document.user.oldpassword.focus();
				return false;
			}
			return true;
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
               
                if(validateallFields()==true){
                    
                    var old_password = document.getElementById("inputtext1");
                    var new_password = document.getElementById("inputtext2");

                    if(xmlhttp) {
                        
                       
                        xmlhttp.open("POST","change_pass",true); //getname will be the servlet name
                        xmlhttp.onreadystatechange  = handleServerResponse;
                        xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                        xmlhttp.send("oldpassword=" + old_password.value+"&newpassword="+new_password.value);
                       
                        
                       
                        
                    }

                }
        }

        function handleServerResponse() {
           if (xmlhttp.readyState == 4) {
             if(xmlhttp.status == 200) {
               var resptext = xmlhttp.responseText;
                 
               //Update the HTML Form element
                    if(resptext==02){
                            alert("Password Updated Successfully");
                            document.user.oldpassword.value="";
                            document.user.newpassword.value="";
                            document.user.confirmpassword.value="";
                            location.href="business_user_settings.jsp";
                         
                
                    }
                     else {
                         if(resptext==01){
                            alert("Old Password is Invalid. Enter correct Password");
                             document.user.oldpassword.value="";
                             document.user.oldpassword.focus();
                         }
                     
                         if(resptext==00){
                             alert("Password could not be updated");
                            document.user.oldpassword.value="";
                             document.user.newpassword.value="";
                             document.user.confirmpassword.value="";
                             document.user.oldpassword.focus();
                     }
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
	margin-top:60px;
	margin-bottom: 110px;
	background: #F7F7F7 url(images/tables_long.PNG) repeat-x;
	border: 1px solid #CCCCCC;
	width:420px;
        font-family: Calibri;

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



#login label {
	margin-bottom: .5em;
	font: normal medium Calibri;
        font-weight: bold;
	padding: 2px 5px;
	float:left;
}

#login #inputtext1, #login #inputtext2, #login #inputtext3{
	width: 170px;
	margin-bottom: .5em;
        margin-right: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal medium Calibri;
        
	color: #999999;
        
        float:right;
}






#inputsubmit1, #inputcancel ,#inputsubmit2, #inputsubmit3 {
	width: 70px;
        height : 25px;
	font-size: 16px;
        color : #ffffff;
        margin-right: 20px;
	font-family: calibri;
        font-weight: bold;
        background: #F47B27 url(images/orange_gradient.png) repeat-x 0 0;
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
                    <li  ><a href="business_user_report.jsp">Report</a>

                    </li>
                    <li class="navtext-sel"><a href="business_user_settings.jsp">Settings</a>

                    </li>

                </ul>
                <a href="signout.jsp" style="color: #323232; float: right; height: 32px; margin-top: 10px; margin-left: 0;">Signout</a>

        </div>
        <br>


	<table style="font-family: Arial,Helvetica,sans-serif; padding-top: 10px; padding-bottom: 50px;" width="100%">
	<tbody><tr>

<!--<td style="font-size: 30px; text-align: center; width: 50px; font-weight: bold; color: white;"></td>-->
<td valign="top" align="center">

		<div id="login" class="boxed">
                    <h1 class="title" style="font-family: Calibri; font-size: 18px;">Change Password</h1>
			<div class="content">
				<form id="form1" name="user" >
					<fieldset>
					<legend>Change Password</legend>
                                        
                                        <label for="inputtext1">Old Password<font color="#FF6699">*</font>:</label><input id="inputtext1" type="password" name="oldpassword" value="" maxlength="20"/><br><br>
                                        <label for="inputtext2">New Password<font color="#FF6699">*</font>:</label><input id="inputtext2" type="password" name="newpassword" value="" maxlength="20"/><br><br>
					<label for="inputtext3">Confirm Password<font color="#FF6699">*</font>:</label><input id="inputtext3" type="password" name="confirmpassword" value="" maxlength="20"/><br><br><br>


					<input id="inputsubmit1" type="button" name="changepwdbtn" value="Save" onClick="ajaxFunction();"/>
                                        <input id="inputsubmit2" type="button" name="clear_btn" value="Cancel" onClick="location.href='business_user_settings.jsp'"/><br>
                                        


					</fieldset>
				</form>
			</div>
		</div>
</td>


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