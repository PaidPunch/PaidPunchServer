<%-- 
    Document   : signup_business
    Created on : Oct 5, 2011, 1:27:05 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%

    String secretcode = "";
    try{
           secretcode = request.getParameter("secretcode");
    }catch(Exception e){
        response.sendRedirect("login.jsp");
    }
    if(secretcode==null||secretcode==""){
             response.sendRedirect("login.jsp");
          }
    else{
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<!-- DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" -->
<!--
Design by Free CSS Templates
http://www.freecsstemplates.org
Released for free under a Creative Commons Attribution 2.5 License
-->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link href='images/paidpunchlogo.png' rel='icon' type='image/png'/>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>PaidPunch</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script type="text/javascript" src="scripts/jquery.numeric.js"></script>
<script src="http://maps.google.com/maps?file=api&v=2&sensor=false&key=AIzaSyB1Oj3BIH1yQGvaWnyXsTjbSuV6cwhDs0o" type="text/javascript"></script>

<script type="text/javascript" language="javascript">

var map = null;
 var geocoder = null;
function initialize() {
        
	if (GBrowserIsCompatible()) {
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
				alert(place + " not found. Please enter Lat, Long manually or visit http://itouchmap.com/latlong.html");
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



    /***** CUSTOMIZE THESE VARIABLES *****/

  // width to resize large images to
        var maxWidth=100;
          // height to resize large images to
        var maxHeight=100;
          // valid file types
        var fileTypes=["png","jpg","jpeg"];
          // the id of the preview image tag
        var outImage="previewField";
          // what to display when the image is not valid
        var defaultPic="spacer.gif";

function preview(what){

  LimitedSize();
  var source=what.value;
  var ext=source.substring(source.lastIndexOf(".")+1,source.length).toLowerCase();
  for (var i=0; i<fileTypes.length; i++) 
      if (fileTypes[i]==ext)
          break;
  globalPic=new Image();
  
  if (i<fileTypes.length)
      globalPic.src=source;
  else {
    globalPic.src=defaultPic;
    alert("THAT IS NOT A VALID IMAGE\nPlease load an image with an extention of one of the following:\n\n"+fileTypes.join(", "));
    what.value="";
  }
  
  var x=parseInt(globalPic.width);
  var y=parseInt(globalPic.height);
  
  if (x>maxWidth||y>maxHeight) {
       alert("The logo size should not exceed 100*100")
  }


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

    function checkSpecialCharachters(checkString) {
       var iChars = "!#%^*()+=[]\\;,./{}|:<>?";

      for (var i = 0; i < checkString.length; i++) {
        if (iChars.indexOf(checkString.charAt(i)) != -1) {
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
                        if(document.user.email.value==""||document.user.password.value==""||document.user.confirmpassword.value==""||document.user.businessname.value==""||document.user.businessdesc.value=="")
			{
				alert("Please Enter all the entries marked with * ");
                                return false;
			}

                        if(document.user.businessaddress.value==""||document.user.pincode.value==""||document.user.contactnumber.value==""||document.user.contactname.value==""||document.user.latitude.value==""||document.user.longitude.value=="")
			{
				alert("Please Enter all the entries marked with * ");
                                return false;
			}
                        
                        var buss_name = document.user.businessname.value;
                        if(checkSpecialCharachters(buss_name)==false)
			{
                            alert("Special Charachters are not allowed in Business Name");
                            document.user.businessname.focus();
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
                        if(document.user.datafile.value==""){
                            alert ("Upload Business logo");
                            return false;
                        }

			if(!isemail(document.user.email.value))
			{
				document.user.email.focus();
				return false;
			}

			if(document.user.password.value!=document.user.confirmpassword.value)
			{
				alert("Password and Confirm Password do not match");
				document.user.password.focus();
				return false;
			}

                        if(document.user.contactnumber.value==null||document.user.contactnumber.value==""){
                        }
                        else{
                            var mobileno = document.user.contactnumber.value;

                            if(blankspac(mobileno)==false)
                            {
                              alert("Blank Spaces Not Allowed in Mobile Number");
                                return false
                            }

                            if(checkAlphaNumerics(mobileno)==false)
                            {  alert("Mobile Number should be numeric");
                                return false;
                            }

                            if(mobileno.length<10)
                                {
                                    alert("Enter atleast 10 digit Mobile Number");
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
                            {  alert("Pin COde should be numeric");
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


	function isemail()
	{

		if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(document.user.email.value))
		{
			return true;
		}
		alert("Email not valid");
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
                   var business_name = document.getElementById("inputtext4");
                    xmlhttp.open("GET","signup_business_email?email="+email_id.value+"&businessname="+business_name.value,true); //getname will be the servlet name
                    xmlhttp.onreadystatechange  = handleServerResponse;
                    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xmlhttp.send(null); //Posting txtname to Servlet
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
               else if(resptext==01){

                   alert("Email Address already exists");
                   document.user.email.value="";
                   document.user.email.focus();
               }
               else if(resptext==02){
                   alert("Business with that name already exists");
                   document.user.businessname.value="";
                   document.user.business_name.focus();
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

        function limitText(limitField, limitNum) {
            if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
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
              
         <%--   var newdiv = document.createElement("div");
            newdiv.innerHTML = xmlHttp.responseText;
            var container = document.getElementById("teststate");
            container.innerHTML="";
            container.appendChild(newdiv);
            alert(container.innerHTML);--%>
           document.getElementById("state").innerHTML=xmlHttp.responseText;
           <%--var newdiv1 = document.createElement("div");
            newdiv1.innerHTML = "<select name='city' id='city'> <option value='-1'>Select</option></select>";
            var container1 = document.getElementById("testcity");
            container1.innerHTML="";
            container1.appendChild(newdiv1);--%>
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
            
           <%-- var newdiv2 = document.createElement("div");
            newdiv2.innerHTML = xmlHttp.responseText;
            var container2 = document.getElementById("testcity");
            container2.innerHTML="";
            container2.appendChild(newdiv2);--%>
            
            document.getElementById("city").innerHTML=xmlHttp.responseText;
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
	font: normal small calibri;
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

h2 {    font-weight: bold;
	margin-bottom: .5em;
	font-size: 1.4em;
}

h3 {
	margin-bottom: 1em;
	font-size: 1em;
}


a {
	font-size: 1.2em;
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
	padding: 5px 0;
}


/* Content > Sidebar */

#sidebar {
	float: right;
	width: 220px;
}

/* Content > Sidebar > Login */

#login {
}

#login label {
        display: block;
	font: normal medium Calibri;
        font-weight: bold;
        float:left;
        font-size: 15px;
        color : #323232
}

#login #inputtext1, #login #inputtext2, #login #inputtext3, #login #inputtext4, #login #inputtext5, #login #inputtext6, #login #inputtext7, #login #inputtext8, #login #inputtext9, #login #inputtext11, #login #inputtext12 {
	width: 188px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	float: right;
}

#login #inputtext10 ,#login #state , #login #city{
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
        float: left;
	font-family: calibri;
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
}


/* Footer */

#footer {
	   position:fixed;
   bottom:0;
   width:100%;

   background: #F7F7F7 url(images/orange_gradient.png) repeat-x;
}

/* Footer > Legal */

#legal {
	margin: 0;
	padding: 5px;
	text-align: center;
	color: #FFFFFF;
	height: 15px;
}

#legal a {
	font-weight: bold;
	color: #FFFFFF;
}

/* Footer > Links */

#links {
	margin: 0;
	padding: 10px;
	text-align: center;
}

    </style>

</head>
<body onload="initialize();" onunload="GUnload();">

<div id="header">
	<div id="topmenu">

	</div>
	<div id="logo">
		<img src="images/paidpunch.png">
	</div>
</div>


<div id="content">
	<div id="main">
		<div id="welcome">
			<h2 align="center"><strong>Register your business</strong></h2>


		</div>

	</div>


	<table style="font-family: Calibri; padding-top: 10px; padding-bottom: 50px;" width="100%">
            <form id="form1" name="user" method="post" action="signup_business_email" enctype="multipart/form-data" >
            <input type="hidden" name="secretcode" value="<%=secretcode%>">
                
	<tbody><tr>


<td valign="top" align="center">
    
		<div id="login" class="boxed">
			<h2 class="title">Business Login Details</h2>
			<div class="content">
				
					<fieldset>
					<legend>Business Login Details</legend>
                                        
                                        <label for="inputtext1">Email<font color="#FF6699">*</font>:</label><input id="inputtext1" type="text" name="email" value="" maxlength="35"/><br><br>
					<label for="inputtext2">Password<font color="#FF6699">*</font>:</label><input id="inputtext2" type="password" name="password" value="" maxlength="20"/><br><br>
					<label for="inputtext3">Confirm Password<font color="#FF6699">*</font>:</label><input id="inputtext3" type="password" name="confirmpassword" value="" maxlength="20"/><br><br>


					</fieldset>

			</div>
		</div>
</td>
</tr>

<tr>


<td valign="top" align="center">

		<div id="login" class="boxed">
			<h2 class="title">Business Details</h2>
			<div class="content">

					<fieldset>
					<legend>Business Details</legend>
					<label for="inputtext4">Business Name<font color="#FF6699">*</font>:</label><input id="inputtext4" type="text" name="businessname" value="" maxlength="30"/><br><br>
					<label for="inputtext5">Description of Business<font color="#FF6699">*</font>:</label><textarea id="inputtext5" name="businessdesc" class="textarea" onKeyDown="limitText(this.form.businessdesc,100);" onKeyUp="limitText(this.form.businessdesc,100);" style="border: 1px solid #CCCCCC; width: 190px; height:50px;min-width: 190px; max-width: 190px; max-height: 50px;min-height:50px "></textarea><br><br><br><br><!--<input id="inputtext5" type="text" name="businessdesc" value="" maxlength="80"/><br><br>-->
                                        <label for="inputfile1">Logo<font color="#FF6699">*</font>:</label><input id="inputfile1" type="file" name="datafile" size="16" onchange="getSize(); preview(this);"><br><br>
					<label for="inputtext11">Contact Name<font color="#FF6699">*</font>:</label><input id="inputtext11" type="text" name="contactname" value="" maxlength="30"/><br><br>
					<label for="inputtext12">Contact No<font color="#FF6699">*</font>:</label><input id="inputtext12" type="text" name="contactnumber" value="" maxlength="15" class="positive-integer"/><br><br>
                                        <label for="inputtext6">Address<font color="#FF6699">*</font>:</label><input id="inputtext6" type="text" name="businessaddress" value="" maxlength="50"/><br><br>
                                        <label for="inputtext10">Country<font color="#FF6699">*</font>:</label><select name="countrycode" onchange="showState(this.value)" id="inputtext10" size="1"><option style="font-family: Calibri;" value="-1">Select</option><option style="font-family: Calibri;" value="USA">United States of America</option><option value="IND">India</option></select><br><br>
                                        <label for="state">State<font color="#FF6699">*</font>:</label><div id="teststate"><select name="state" id="state" onchange="showCity(this.value);"><option value="-1">Select</option></select></div><br><br>
                                        <label for="city">City<font color="#FF6699">*</font>:</label><div id="testcity"> <select name="city" id="city"> <option value="-1">Select</option></select></div><br><br>
					<label for="inputtext9">Zipcode<font color="#FF6699">*</font>:</label><input id="inputtext9" type="text" name="pincode" value="" maxlength="6" class="positive-integer"/><br><br>
                                        <%--<div><div style="float: left;"><div style="float: left;"><input id="google_map" type="button" name="google_map" value="Use Google Map" onclick="getAddress();" style="width: 120px; height:auto; text-align: center; "/></div><div style="float: right;">OR enter</div></div><div style="float: right; width: 200px;"><div style="float: left;"><label for="latitude">Lat<font color="#FF6699">*</font>:</label><input id="latitude" type="text" name="latitude" value="" maxlength="9" style="width: 60px;" /></div><div style="float: right;"><label for="longitude">Long<font color="#FF6699">*</font>:</label><input id="longitude" type="text" name="longitude" value="" maxlength="9" style="width: 60px;"/></div></div></div><br><br>--%>
                                        <table width="380px"><tr><td align="left" width="30%"><input id="google_map" type="button" name="google_map" value="Use Google Map" onclick="getAddress();" style="width: 120px; height:auto; text-align: center; "/></td><td width="10%" align="center">OR enter</td><td width="30%" align="left"><label for="latitude">Lat<font color="#FF6699">*</font>:</label><input id="latitude" type="text" name="latitude" value="" maxlength="9" style="width: 60px;" /></td><td width="30%" align="right"><label for="longitude">Long<font color="#FF6699">*</font>:</label><input id="longitude" type="text" name="longitude" value="" maxlength="9" style="width: 60px;"/></td></tr></table>
                                        <div id="map_canvas" style="width: 350px; height: 250px"></div>
                                        <br>

                                        <table width="380px"><tr><td width="33%" align="left"><input id="inputsubmit3" type="reset" name="cancel" value="Cancel" onClick = "location.href='login.jsp'"/></td><td width="33%" align="center"><input id="inputsubmit1" type="button" name="inputsubmit1" value="Proceed" onClick = "ajaxFunction();"/> </td><td width="33%" align="right"> <input id="inputcancel" type="button" name="cancel" value="Reset" onClick="form.reset()"/></td></tr></table>
					</fieldset>

			</div>
		</div>
</td>
</tr>
<tr align="center">


<td valign="top" >		
</td>

</tr>


</tbody></form>
        <script type="text/javascript">

                    $(".positive-integer").numeric({ decimal: false, negative: false }, function() { alert("Positive integers only"); this.value = ""; this.focus(); });
                    $("#remove").click(
                            function(e)
                            {
                                    e.preventDefault();
                                    $(".numeric,.integer,.positive").removeNumeric();
                            }
                    );
	</script>
        </table>


</div><br><br><br>
<div id="footer">
	<p id="legal"></p>
</div>
</body>
</html>
<%
    }
%>