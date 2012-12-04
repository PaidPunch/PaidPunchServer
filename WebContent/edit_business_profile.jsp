<%--
    Document   : business_admin_user
    Created on : Dec 13, 2011, 5:47:02 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.server.Constants"%>
<%@page import="com.server.Utility"%>
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

    var map = null;
 var geocoder = null;
function initialize1() {
      
	if (GBrowserIsCompatible()) {
            alert("here");
		map = new GMap2(document.getElementById("map_canvas"));
		map.setCenter(new GLatLng(32.715329, -117.157255), 4);
		geocoder = new GClientGeocoder();
	}
}

function getLatLng (point) {
     var matchll = /\(([-.\d]*), ([-.\d]*)/.exec( point );
      if ( matchll ) {
       var lat = parseFloat( matchll[1] );
       var lon = parseFloat( matchll[2] );
       lat = lat.toFixed(6);
       lon = lon.toFixed(6);

      } else {
       var message = "<b>Error extracting info from</b>:" + point + "";
       var messagRoboGEO = message;
      }

     return new GLatLng(lat, lon);
 }

    function searchPlace(place) {
	if (geocoder) {

		geocoder.getLatLng(place, function(point) {

			if (!point) {
				alert(place + " not found. Please enter Lat, Long manually or find by visiting http://itouchmap.com/latlong.html");
			} else {

				var latLng = getLatLng (point);
				var info = "<h3>"+place+"</h3>Latitude: "+latLng.lat()+"  Longitude:"+latLng.lng();
                                document.user.latitude.value = latLng.lat();
                                document.user.longitude.value = latLng.lng();
				var marker = new GMarker(point);
                                map.setCenter(new GLatLng(latLng.lat(), latLng.lng()), 11);
				map.addOverlay(marker);
				marker.openInfoWindowHtml(info);

			}
		});
	}
}

    function getAddress(){

        if(document.user.businessaddress.value==""||document.user.businessaddress.value==null){
              alert("Please enter Address ");
              return false;
         }

        if(document.user.countrycode.value=="-1"){
               alert("Please select Country ");
               return false;
        }

         if(document.user.state.value=="-1"){
              alert("Please select State ");
              return false;
         }
         if(document.user.city.value=="-1"){
              alert("Please select City ");
              return false;
         }

         if(document.user.pincode.value==null||document.user.pincode.value==""){
             alert("Please enter Zipcode ");
             return false;
         }else{
               var pincode = document.user.pincode.value;
               if(blankspac(pincode)==false)
               {
                    alert("Blank Spaces Not Allowed in Pin Code");
                     return false;
               }

                if(checkAlphaNumerics(pincode)==false)
                {
                    alert("Pin Code should be numeric");
                    return false;
               }

               if(pincode.length<5)
               {
                    alert("Enter atleast 5 digit Pin Code");
                    return false;
               }
         }

         var address = document.user.businessaddress.value;
         var city = document.user.city.value;
         var state = document.user.state.value;
         var country = document.user.countrycode.value;
         var zipcode = document.user.pincode.value;
         if(country=="USA"){
             country = "United States of America";
         }else if(country=="IND"){
             country = "India";
         }

         var add = address +', '+ city +', '+ state +', '+ country +', '+ zipcode;
         searchPlace(add);

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
        }

		function validateemail()
		{

			if(document.user.businessdesc.value=="")
			{
				alert("Please Enter all the entries marked with * ");
                                return false;
			}

                        if(document.user.businessaddress.value==""||document.user.pincode.value==""||document.user.contactnumber.value==""||document.user.contactname.value==""||document.user.latitude.value==""||document.user.longitude.value=="")
			{
				alert("Please Enter all the entries marked with * ");
                                return false;
			}

                        if(document.user.state.value=="-1"){
                            alert("Please select State ");
                            return false;
                        }
                        if(document.user.city.value=="-1"){
                            alert("Please select City ");
                            return false;
                        }
                        
                         if(document.user.contactnumber.value==null||document.user.contactnumber.value==""){
                        }
                        else{
                            var mobileno = document.user.contactnumber.value;

                            if(blankspac(mobileno)==false)
                            {
                              alert("Blank Spaces Not Allowed in Contact Number");
                                return false
                            }

                            if(checkAlphaNumerics(mobileno)==false)
                            {  alert("Contact Number should be numeric");
                                return false;
                            }

                            if(mobileno.length<10)
                                {
                                    alert("Enter atleast 10 digit Contact Number");
                                    return false;
                                }
                        }

                        if(document.user.pincode.value==null||document.user.pincode.value==""){
                        }
                        else{
                            var pincode = document.user.pincode.value;

                            if(blankspac(pincode)==false)
                            {
                              alert("Blank Spaces Not Allowed in Pin Code");
                                return false
                            }

                            if(checkAlphaNumerics(pincode)==false)
                            {  alert("Pin Code should be numeric");
                                return false;
                            }

                            if(pincode.length<5)
                                {
                                    alert("Enter atleast 5 digit Pin Code");
                                    return false;
                                }
                        }

                        if(document.user.latitude.value==null||document.user.latitude.value==""){
                        }
                        else{
                            var latitude = document.user.latitude.value;

                            if(blankspac(latitude)==false)
                            {
                              alert("Blank Spaces Not Allowed in Latitude");
                                return false
                            }

                            if(checkAlphaNumerics(latitude)==false)
                            {  alert("Latitude should be numeric");
                                return false;
                            }


                        }

                        if(document.user.longitude.value==null||document.user.longitude.value==""){
                        }
                        else{
                            var longitude = document.user.longitude.value;

                            if(blankspac(longitude)==false)
                            {
                              alert("Blank Spaces Not Allowed in Longitude");
                                return false
                            }

                            if(checkAlphaNumerics(longitude)==false)
                            {  alert("Longitude should be numeric");
                                return false;
                            }


                        }

			return true;
		}





        function limitText(limitField, limitNum) {
            if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
            }
    }

    function setEnabled(){

        if(document.user.edit.value=="Save"){
            
            if(validateemail()==true){
                document.forms["user"].submit();
            }
        }else{
            if(document.user.edit.value=="Edit"){
            document.user.businessdesc.disabled = false;
            document.user.businessaddress.disabled = false;
            document.user.city.disabled = false;
            document.user.state.disabled = false;
            document.user.pincode.disabled = false;
            document.user.countrycode.disabled = false;
            document.user.contactname.disabled = false;
            document.user.contactnumber.disabled = false;
            document.user.latitude.disabled = false;
            document.user.longitude.disabled = false;
            document.user.pincode.disabled = false;
            document.user.latitude.disabled = false;
            document.user.longitude.disabled = false;
            document.user.edit.value="Save";

            }
        }
    }

    function showState(str){


      if (typeof XMLHttpRequest != "undefined"){
        xmlHttp= new XMLHttpRequest();
      }
      else if (window.ActiveXObject){
        xmlHttp= new ActiveXObject("Microsoft.XMLHTTP");
      }
      if (xmlHttp==null){
         alert("Browser does not support XMLHTTP Request")
      return;
      }
      var url="state.jsp";
      url +="?count=" +str;
      xmlHttp.onreadystatechange = stateChange;
      xmlHttp.open("GET", url, true);
      xmlHttp.send(null);
      }

      function stateChange(){
          if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete"){
           document.getElementById("state").innerHTML=xmlHttp.responseText;
           document.getElementById("city").innerHTML="<select name='city' id='city'> <option value='-1'>Select</option></select>";

        }
      }


      function showCity(str){

      if (typeof XMLHttpRequest != "undefined"){
         xmlHttp= new XMLHttpRequest();
      }
      else if (window.ActiveXObject){
        xmlHttp= new ActiveXObject("Microsoft.XMLHTTP");
        }
      if (xmlHttp==null){
        alert("Browser does not support XMLHTTP Request")
      return;
      }
      var url="city.jsp";
      url +="?count=" +str;
      xmlHttp.onreadystatechange = stateChange1;
      xmlHttp.open("GET", url, true);
      xmlHttp.send(null);
      }
      function stateChange1(){
          if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete"){
            document.getElementById("city").innerHTML=xmlHttp.responseText
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
	margin-bottom: 1em;
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

#login a {
	font-size: 1.5em;
	color: #EA672E;

}

#login a:hover {
	text-decoration: none;
	color: #11A6D4;
}



#login label {
        display: block;
	font: normal medium Calibri;
        font-weight: bold;
        float:left;
        font-size: 15px;
        color : #323232
}

#login #inputtext1, #login #inputtext2, #login #inputtext3, #login #inputtext4, #login #inputtext5, #login #inputtext6, #login #inputtext7, #login #inputtext8, #login #inputtext9, #login #inputtext11, #login #inputtext12, #login #inputtext13 , #login #latitude, #login #longitude{
	width: 188px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	float: right;
}

#login #inputtext10,#login #state , #login #city  {
        width: 200px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	float: right;
}

#login #inputfile1 {

	margin-bottom: .5em;
	font: normal small Calibri;
	color: #999999;
	float: right;
}


#inputsubmit3 {
	width : 67px;

	font-size: 15px;
        color : #ffffff;
        margin-right: 40px;
	font-family: Calibri;
        font-weight: bold;
        background: url(images/button_base.png) repeat;
}

#inputsubmit2 {
	width : 67px;

	font-size: 15px;
        color : #ffffff;
        margin-right: 40px;
	font-family: Calibri;
        font-weight: bold;
        background: url(images/button_base.png) repeat;
}

#inputsubmit1{
	width : 67px;

	font-size: 15px;
        color : #ffffff;
        margin-right: 10px;
	font-family: Calibri;
        font-weight: bold;
        background: url(images/button_base.png) repeat;
}

#inputcancel {
	width : 67px;
        float: right;
	font-size: 15px;
        color : #ffffff;
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
   height: 25px;
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
<%--<body onload="initialize1();" onunload="GUnload();">--%>
<body>

    <%
    

        DBConnection db1 = null;
            Statement stmt1 = null;
            ResultSet rs1 = null;
            int busid=0;
            String businessLogoPath = "";

            try {
                               db1 = new DBConnection();
                               stmt1 = db1.stmt;

                               try{
                                    String query = "SELECT business_userid FROM business_users where email_id='"+username+"'";
                                    rs1 = stmt1.executeQuery(query);
                                    com.server.Constants.logger.info("The select query is " + query);

                                   if(rs1.next()){
                                    busid = rs1.getInt(1);
                                    businessLogoPath = Utility.getBusinessLogoUrl(Integer.toString(busid));
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
<%
String business_name="",email_id="",busi_desc="",address="",discount="";
    String city="",state="",country="",pincode="",contactno="",contactname="", latitude="", longitude="";

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
                                com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                throw new ServletException("SQL Exception.", e);
                            }

                            System.out.println("BUsiness Name:"+business_name+"\nEmail_id: "+email_id+"\nBusi Desc: "+busi_desc+"\nAddress : "+address+"\nCity : "+city+"\nState: "+state+"\nCountry: "+country+"\nZipCode:"+pincode+"\nCOntact Name:"+contactname+"\nContatct Num : "+contactno+"\nLatitude : "+latitude+"\nLongitude : "+longitude);

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
<!--<td style="font-size: 30px; text-align: center; width: 50px; font-weight: bold; color: white;"></td>-->
<td valign="top" align="center">

		<div id="login" class="boxed">
                    <h1 class="title" style="font-family: Calibri; font-size: 18px;">Edit Profile</h1>
			<div class="content">
                            <form id="form1" name="user" action="edit_bus_setting" method="post">
					<fieldset>
					<legend>Edit Business Profile</legend>
                                        <table id="vd" width="100%"><TR><TD width="30%" rowspan="2" valign="middle" ><img src="<%=businessLogoPath%>" alt="pplogo" height="64px" width="64px" style="margin-left: 10px; margin-right: 10px;"></TD><TD style="font-family: Calibri; font-size: 22px; font-weight: bold; color: #323232; padding-top: 10px;"><%=business_name%></TD><TR><TD style="font-family: Calibri; font-size: 18px; font-weight: bold; color: #323232;"><%=email_id%></TD></TR></table><BR>
                                        <label for="inputtext5">Description of Business<font color="#FF6699">*</font>:</label><textarea id="inputtext5" name="businessdesc" class="textarea" onKeyDown="limitText(this.form.businessdesc,100);" onKeyUp="limitText(this.form.businessdesc,100);" style="border: 1px solid #CCCCCC; width: 190px; height:50px;min-width: 190px; max-width: 190px; max-height: 50px;min-height:50px " disabled="true"><%=busi_desc%></textarea><br><br><br><br><!--<input id="inputtext5" type="text" name="businessdesc" value="" maxlength="80"/><br><br>-->
                                        <%--<label for="inputfile1">Logo<font color="#FF6699">*</font>:</label><input id="inputfile1" type="file" name="datafile" size="17" onchange="preview(this)"><br><br>--%>
                                        <label for="inputtext11">Contact Name<font color="#FF6699">*</font>:</label><input id="inputtext11" type="text" name="contactname" value="<%=contactname%>" maxlength="30" disabled="true"/><br><br>
					<label for="inputtext12">Contact No<font color="#FF6699">*</font>:</label><input id="inputtext12" type="text" name="contactnumber" value="<%=contactno%>" maxlength="15" class="positive-integer" disabled="true"/><br><br>
                                        <label for="inputtext6">Address<font color="#FF6699">*</font>:</label><input id="inputtext6" type="text" name="businessaddress" value="<%=address%>" maxlength="50" disabled="true"/><br><br>
                                        <label for="inputtext10">Country<font color="#FF6699">*</font>:</label><select style="font-family: Calibri;" name="countrycode" onchange="showState(this.value)" id="inputtext10" size="1" disabled>
                                            <%
                                            String country_code ="";
                                            if(country.equalsIgnoreCase("USA")){
                                                country_code = "USA";
                                                    out.print("<option selected=\"true\" value=\"USA\">");
                                                    out.print("United States of America");
                                                    out.print("</option><option value=\"IND\">India</option>");
                                                }
                                                else if(country.equalsIgnoreCase("IND")){
                                                    country_code = "IND";
                                                    out.print("<option value=\"USA\">");
                                                    out.print("United States of America");
                                                    out.print("</option><option selected=\"true\" value=\"IND\">India</option>");
                                                }



%>
                                            </select><br><br>
                                            <label for="state">State<font color="#FF6699">*</font>:</label><select name="state" id="state" onchange="showCity(this.value);" disabled>

                                        <%

                                        db = new DBConnection();
                                        stmt = db.stmt;

                                         try{
                                         System.out.println("Country Code : "+country_code);
                                         //String query = "SELECT distinct District FROM city where CountryCode='"+country_code+"'";
                                         String query = "SELECT distinct District FROM city where CountryCode='"+country_code+"' order by District asc;";
                                         rs = stmt.executeQuery(query);
                                         com.server.Constants.logger.info("The select query is " + query);
                                         String state_list="";
                                         while(rs.next()){
                                             state_list = rs.getString("District");
                                                if(state_list.equalsIgnoreCase(state)){
                                                    out.print("<option selected=\"true\" value=\""+state_list+"\">");
                                                    out.print(""+state_list);
                                                    out.print("</option>");
                                                }
                                                else{
                                                    out.print("<option value=\""+state_list+"\">");
                                                    out.print(""+state_list);
                                                    out.print("</option>");
                                                }
                                          }
                                   %>

                                   </select><br><br>
                                   <label for="city">City<font color="#FF6699">*</font>:</label> <select name="city" id="city" disabled>
                                 <%
                                         String query1 = "SELECT distinct Name FROM city where District='"+state+"' order by Name asc;";
                                         rs = stmt.executeQuery(query1);
                                         com.server.Constants.logger.info("The select query is " + query);
                                         String city_list="";
                                         while(rs.next()){
                                             city_list = rs.getString("Name");
                                                if(city_list.equalsIgnoreCase(city)){
                                                    out.print("<option selected=\"true\" value=\""+city_list+"\">");
                                                    out.print(""+city_list);
                                                    out.print("</option>");
                                                }
                                                else{
                                                    out.print("<option value=\""+city_list+"\">");
                                                    out.print(""+city_list);
                                                    out.print("</option>");
                                                }
                                          }


                                        }catch(SQLException e){
                                            com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                            throw new ServletException("SQL Exception.", e);
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
                                        </select><br><br>
                                        <label for="inputtext9">Zipcode<font color="#FF6699">*</font>:</label><input id="inputtext9" type="text" name="pincode" value="<%=pincode%>" maxlength="6" class="positive-integer" disabled="true"/><br><br>
                                        <label for="latitude">Lat<font color="#FF6699">*</font>:</label><input id="latitude" type="text" name="latitude" value="<%=latitude%>" maxlength="9"  disabled /><br><br>
                                        <label for="longitude">Long<font color="#FF6699">*</font>:</label><input id="longitude" type="text" name="longitude" value="<%=longitude%>" maxlength="9"  disabled/><br><br>
                                        <%--<div><div style="float: left;"><input id="google_map" type="button" name="google_map" value="Use Google Map" onclick="getAddress();" style="width: 120px; height:auto; text-align: center; "/></div>OR enter<div style="width: 300px; float: left"><div style="float: left; width: 120px;"><label for="latitude">Lat<font color="#FF6699">*</font>:</label><input id="latitude" type="text" name="latitude" value="<%=latitude%>" maxlength="9" style="width: 60px; float: right;" disabled /></div><div style="float: right;"><label for="longitude">Long<font color="#FF6699">*</font>:</label><input id="longitude" type="text" name="longitude" value="<%=longitude%>" maxlength="9" style="width: 60px; margin-left: 20px;" disabled/></div></div></div><br><br>
                                        <div id="map_canvas" style="width: 350px; height: 250px"></div>
--%>
                                        <br>
                                        
                                        <input id="inputsubmit3" type="button" name="cancel" value="Cancel" onClick = "location.href='business_user_settings.jsp'"/><input id="inputsubmit1" type="button" name="edit" value="Edit" onClick = "setEnabled();"/>

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