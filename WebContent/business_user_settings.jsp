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
      function PopupCenter(pageURL, title,w,h) {

            var left = (screen.width/2)-(w/2);
            var top = (screen.height/2)-(h/2);
            window.open (pageURL, title, 'toolbar=no, location=no, directories=no, status=no, menubar=yes, scrollbars=no,  copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
        }

</script>

<style type="text/css">
			div.wrapper{ position: relative; margin: 0  auto 30px auto; width: 500px; text-align: left; border: solid 1px #aaaaaa; }
			#users{  }
			#users .user{ border: solid 1px #bbbbbb; background-color: #dddddd; padding: 10px; margin: 5px; }
			#users .user .controls{ float: right; }

			/*-------------impromptu---------- */
			.jqifade{ position: absolute; background-color: #aaaaaa; }
			div.jqi{ width: 400px; font-family: Verdana, Geneva, Arial, Helvetica, sans-serif; position: absolute; background-color: #ffffff; font-size: 11px; text-align: left; border: solid 1px #eeeeee; -moz-border-radius: 10px; -webkit-border-radius: 10px; padding: 7px; }
			div.jqi .jqicontainer{ font-weight: bold; }
			div.jqi .jqiclose{ position: absolute; top: 4px; right: -2px; width: 18px; cursor: default; color: #bbbbbb; font-weight: bold; }
			div.jqi .jqimessage{ padding: 10px; line-height: 20px; color: #444444; }
			div.jqi .jqibuttons{ text-align: right; padding: 5px 0 5px 0; border: solid 1px #eeeeee; background-color: #f4f4f4; }
			div.jqi button{ padding: 3px 10px; margin: 0 10px; background-color: #BF5E26; border: solid 1px #f4f4f4; color: #ffffff; font-weight: bold; font-size: 12px; }
			div.jqi button:hover{ background-color: #728A8C; }
			div.jqi button.jqidefaultbutton{ background-color: #BF5E26; }
			.jqiwarning .jqi .jqibuttons{ background-color: #BF5E26; }

            div.jqi .jqimessage .field{ padding: 5px 0; }
			div.jqi .jqimessage .field label{ display: block; clear: left; float: left; width: 100px; }
			div.jqi .jqimessage .field input{ width: 50px; border: solid 1px #777777; }
			div.jqi .jqimessage .field input.error{ width: 150px; border: solid 1px #ff0000; }
			/*-------------------------------- */
		</style>


<script type="text/javascript" src="scripts/jquery-1.js"></script>
<script type="text/javascript" src="scripts/jquery-impromptu.js"></script>


<script type="text/javascript">

               function setButtonClick(buttonClick){
                   document.changetime.buttonval.value=buttonClick;
               }

			function editUser(id){
                            
				var user = $('#userid'+id)

                                var buttonName = document.changetime.buttonval.value;
                                
                                <%--var fname = user.find('.fname').text();--%>
				var lname = '';

                                if(buttonName == 'time'){
                                  lname = document.changetime.tc.value;
                                  var txt = 'Edit below to change the Time Restriction(in min)'+
                                    '<div class="field"><input type="text" id="editlname" name="editlname" value="'+ lname +'" maxlength="4" /></div>';
                                }else if(buttonName == 'expire'){
                                  lname = document.changetime.ed.value;
                                  var txt = 'Edit below to change the no of days card Expires'+
                                    '<div class="field"><input type="text" id="editlname" name="editlname" value="'+ lname +'" maxlength="3" /></div>';
                                }else if(buttonName == 'minval'){
                                  lname = document.changetime.mv.value;
                                  var txt = 'Edit below to change the Minimum Value of Purchase'+
                                    '<div class="field"><input type="text" id="editlname" name="editlname" value="'+ lname +'" maxlength="3" /></div>';
                                }


                               <%-- var fname = user.find('.fname').text();
				var lname = document.changetime.tc.value;--%>
                                

				<%--var txt = 'Edit below to change the Time Restriction(in min)'+

                                        '<div class="field"><input type="text" id="editlname" name="editlname" value="'+ lname +'" maxlength="4" /></div>';
--%>

				$.prompt(txt,{
                                    
					buttons:{Change:true, Cancel:false},
					submit: function(v,m,f){
						//this is simple pre submit validation, the submit function
						//return true to proceed to the callback, or false to take
						//no further action, the prompt will stay open.
						var flag = true;
						if (v) {

                                                        if ($.trim(f.editlname) == '') {
								m.find('#editlname').addClass('error');
								flag = false;
							}
							else m.find('#editlname').removeClass('error');
						}
						return flag;
					},
					callback: function(v,m,f){

						if(v){
							//Here is where you would do an ajax post to edit the user
							//also you might want to print out true/false from your .php
							//file and verify it has been removed before removing from the
							//html.  if false dont remove, $promt() the error.

							//$.post('edituser.php',{userfname:f.editfname,userlname:f.editlname}, callback:function(data){
							//	if(data == 'true'){


									<%--user.find('.lname').text(f.editlname);--%>
                                                       if(buttonName == 'time'){
                                                                        var timer =  f.editlname;

                                                                        if(popUpAlertTimer(timer)==true){
                                                                            <%--alert(f.editlname);--%>
                                                                            document.changetime.tc.value = f.editlname;
                                                                            document.forms["changetime"].submit();
                                                                        }
                                                       }else if(buttonName == 'expire'){
                                                                        var expires =  f.editlname;

                                                                        if(popUpAlertExpiry(expires)==true){
                                                                            <%--alert(f.editlname);--%>
                                                                            document.changetime.ed.value = f.editlname;
                                                                            document.forms["changetime"].submit();
                                                                        }
                                                       } else if(buttonName == 'minval'){
                                                                        var minvalue =  f.editlname;

                                                                        if(popUpAlertminval(minvalue)==true){
                                                                            <%--alert(f.editlname);--%>
                                                                            document.changetime.mv.value = f.editlname;
                                                                            document.forms["changetime"].submit();
                                                                        }
                                                       }

							//	}else{ $.prompt('An Error Occured while editing this user'); }
							//});
						}
						else{}

					}
				});
			}

                        function popUpAlertTimer(timer){
                            var timerestrict = timer;
                            if(checkAlphaNumerics(timerestrict)==false)
                            {  alert("Time Restriction should be numeric");
                                return false;
                            }

                            if(blankspac(timerestrict)==false)
                            {
                              alert("Blank Spaces Not Allowed in Time Restriction");
                              return false;
                            }

                            if(checkminus(timerestrict)==false)
                            {
                              alert("Negative Numbers Not Allowed in Time Restriction");
                                return false;
                            }
                            if(timerestrict.length>4)
                            {
                              alert("Number Cannot be more than 4 digits");
                                return false;
                            }
                            timerestrict = parseInt(timerestrict);
                            if(timerestrict==0)
                            {
                              alert("Time Restriction cannot equal 0");
                                return false;
                            }
                            return true;
                        }

                        function popUpAlertExpiry(expires){
                            var expiredays = expires;
                            if(checkAlphaNumerics(expiredays)==false)
                            {  alert("No of days for expiry date should be numeric");
                                return false;
                            }

                            if(blankspac(expiredays)==false)
                            {
                              alert("Blank Spaces Not Allowed in Expiry Date");
                              return false;
                            }

                            if(checkminus(expiredays)==false)
                            {
                              alert("Negative Numbers Not Allowed in Expiry Date");
                                return false;
                            }
                            if(expiredays.length>3)
                            {
                              alert("Number Cannot be more than 3 digits");
                                return false;
                            }
                            expiredays = parseInt(expiredays);
                            if(expiredays<30){
                                alert("Expire Days cannot be less than 30");
                                return false;
                            }
                            return true;
                        }

                        function popUpAlertminval(minval){
                            var minvalue = minval;
                            if(checkAlphaNumerics(minvalue)==false)
                            {  alert("Minimum Value should be numeric");
                                return false;
                            }

                            if(blankspac(minvalue)==false)
                            {
                              alert("Blank Spaces Not Allowed in Minimum Value");
                              return false;
                            }

                            if(checkminus(minvalue)==false)
                            {
                              alert("Negative Numbers Not Allowed in Minimum Value");
                                return false;
                            }
                            if(minvalue.length>4)
                            {
                              alert("Number Cannot be more than 3 digits");
                                return false;
                            }
                            minvalue = parseInt(minvalue);
                            var punchcardvalue = parseInt(document.changetime.punchcardval.value);
                            if(minvalue<punchcardvalue){
                                alert("Minimum Value cannot be greater than the Punch value");
                                return false;
                            }
                            return true;
                        }

            function checkAlphaNumerics(checkString) {
                var regExp = /^[A-Za-z]$/;

                  for(var i = 0; i < checkString.length; i++)
                  {
                    if ( checkString.charAt(i).match(regExp))
                    {
                      return false;
                    }
                  }


                return true;
        }
        function blankspac(checkString)
        {
            //var regExp = /^".*"$/

                  for(var i = 0; i < checkString.length; i++)
                  {

                    if (checkString.charAt(i)==" ")
                    {
                      return false;
                    }

                }
                return true;
        }

        function checkminus(checkString)
        {
            //var regExp = /^".*"$/

                  for(var i = 0; i < checkString.length; i++)
                  {

                    if (checkString.charAt(i)=="-")
                    {
                      return false;
                    }

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
	color: #11A6D4;
        font-family: Calibri;
        
}

h2 {
	margin-bottom: .5em;
	font-size: 18px;
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
	margin-bottom: 1em;
	background: #F7F7F7 url(images/tables_long.PNG) repeat-x;
	border: 1px solid #CCCCCC;
	width:380px;

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
	padding: 2px 5px;
	float:left;
}

#login #inputtext1{
	width: 150px;
	margin-bottom: .5em;
        margin-right: 1.5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #999999;
        text-align: center;

}






#inputsubmit1, #inputcancel ,#inputsubmit2, #inputsubmit3, #inputsubmit4, #inputsubmit5, #inputsubmit6, #inputsubmit7 {
	width : 150px;
        height : 25px;
	font-size: 16px;
        color : #ffffff;
        margin-right: 20px;
	font-family: Calibri;
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
    height:25px;
    position:fixed;
   bottom:0;
   width:100%;
   background: #F47B27 url(images/orange_gradient.png) repeat-x 0 0;

}



/* define height and width of scrollable area. Add 16px to width for scrollbar          */
div.tableContainer {
	clear: both;
	border: 0px solid #963;
	height: 162px;
	overflow: auto;
	width: 298px;
       background: #fff url("images/card.PNG") no-repeat 0 0;
}


    </style>


        <link rel="stylesheet" href="cssfiles/style.css" type="text/css" media="screen" />
      

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
    
    String business_name="";
    String pdf_Path="";
    int restriction_time=0,expirydays=0,minimumvalue=0;
    float valueofeachpunch = 0;
    
        DBConnection db = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                               db = new DBConnection();
                               stmt = db.stmt;

                               try{

                               String query = "SELECT business_name FROM business_users where email_id='"+username+"'";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                               business_name = rs.getString("business_name");

                              }

                               String query1 = "SELECT value_of_each_punch,restriction_time,expirydays,minimumvalue FROM punch_card,business_users where business_users.email_id='"+username+"' and punch_card.business_userid=business_users.business_userid";
                               rs = stmt.executeQuery(query1);
                               com.server.Constants.logger.info("The select query is " + query1);

                              if(rs.next()){
                                  valueofeachpunch = rs.getFloat("value_of_each_punch");
                                 restriction_time = rs.getInt("restriction_time");
                                 expirydays = rs.getInt("expirydays");
                                 minimumvalue = rs.getInt("minimumvalue");

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


<td valign="top" align="center">

		<div id="login" class="boxed">
			<h2 class="title">Punch Card Settings</h2>
			<div class="content">
				<form id="form1" name="user" method="post" action="forgot_pass">
					<fieldset>
					<legend>Punch Card Settings</legend>
                                        <input id="inputsubmit5" type="button" name="editmystery" value="Mystery Punches" onClick = "location.href='mysterypunch_edit.jsp'"/><br><br>
                                        <input id="inputsubmit5" type="button" name="timerestriction" value="Time Restriction" onClick = "setButtonClick('time');editUser(1);"/><br><br>
                                        <input id="inputsubmit6" type="button" name="noofexpirydays" value="Punch Card Expiry" onClick = "setButtonClick('expire');editUser(1);"/><br><br>
                                        <input id="inputsubmit7" type="button" name="minimumpurchase" value="Minimum Purchase" onClick = "setButtonClick('minval');editUser(1);"/><br><br>
					<%--<input id="inputsubmit1" type="button" name="spqrcode" value="Special QR Code" onClick="window.open ('<%=orange_code_pdf%>','Special','toolbar=no, location=no, directories=no, status=no, menubar=yes, scrollbars=no, copyhistory=no, width=500, height=400'); return false" /><br><br>--%>
                                        <%--<input id="inputsubmit2" type="button" name="flyer" value="Flyer" onClick="window.open('flyerpage.jsp', 'child', 'height=500,width=650,resizable=no,toolbar=no,menubar=yes'); return false" /><br><br>--%>
                                        
					<%--<input id="inputsubmit1" type="button" name="spqrcode" value="Special QR Code" onClick="PopupCenter('<%=orange_code_pdf%>', 'Special',500,400); return false"/><br><br>--%>
                                        <%--<input id="inputsubmit2" type="button" name="flyer" value="Flyer" onClick="PopupCenter('<%=flyer_pdf%>', 'Fyer',720,450); return false"/><br><br>--%>
					</fieldset>
				</form>
			</div>
		</div>
</td>
</tr>
   
            <tr><td><BR><BR></td></tr>
<tr><td align="center">
        <div id="login" class="boxed">
			<h2 class="title">Profile Settings</h2>
			<div class="content">
                            <%--<input id="inputsubmit5" type="button" name="editmystery" value="Mystery Punches" onClick = "location.href='mysterypunch_edit.jsp'"/><br><br>--%>
                            <input id="inputsubmit3" type="button" name="editprofile" value="View/Edit Profile" onClick = "location.href='edit_business_profile.jsp'"/><br><br>
                            <input id="inputsubmit4" type="button" name="changepassword" value="Change Password" onClick = "location.href='change_password.jsp'"/><br><br>
                            <%--<input id="inputsubmit5" type="button" name="timerestriction" value="Time Restriction" onClick = "setButtonClick('time');editUser(1);"/><br><br>
                            <input id="inputsubmit6" type="button" name="noofexpirydays" value="Punch Card Expiry" onClick = "setButtonClick('expire');editUser(1);"/><br><br>
                            <input id="inputsubmit7" type="button" name="minimumpurchase" value="Minimum Purchase" onClick = "setButtonClick('minval');editUser(1);"/><br><br>--%>

                            <form id="form2" name="changetime" method="get" action="edit_bus_setting">
                                <input type="hidden" value="<%=restriction_time%>" id="timechange" name="tc" />
                                <input type="hidden" value="<%=expirydays%>" id="expirydays" name="ed" />
                                <input type="hidden" value="<%=minimumvalue%>" id="minvalue" name="mv" />
                                <input type="hidden" value="" id="buttonval" name="buttonval">
                                <input type="hidden" value="<%=valueofeachpunch%>" id="punchcardval" name="punchcardval" />

				</form>
                        </div>
		</div>
        </td></tr>
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