<%-- 
    Document   : signup_paidpunch
    Created on : Oct 6, 2011, 6:18:40 PM
    Author     : Shahid
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>


<%@page import="java.util.GregorianCalendar"%>
<%
String secretcode="";
 try{
            secretcode = session.getAttribute("secretcode").toString();


        }catch(Exception e){
        }

         if(secretcode==null||secretcode==""){
             response.sendRedirect("login.jsp");
           }else{
     %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<head>

<link href='images/paidpunchlogo.png' rel='icon' type='image/png'/>


<title>PaidPunch</title>

<meta name="keywords" content="" />
<meta name="description" content="" />
<link media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script type="text/javascript" src="scripts/jquery.numeric.js"></script>
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
        font-family: Calibri;
        
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


/* Content > Sidebar > Login */

#login {
}

#login label {
	font: normal medium Calibri;
        float:left;
        font-size: 15px;
        color : #323232;
        font-weight: bold;
}

#login label1 {
        padding: 2px 5px;
	margin-bottom: .5em;
	font: normal medium Calibri;
        color : #323232;
       
}

#login label1 {
        padding: 2px 5px;
	margin-bottom: .5em;
	font: normal medium Calibri;
        color : #323232;

}

#login label3 {
        padding: 2px 5px;
	margin-bottom: .5em;
	font: normal medium Calibri;
        color : #323232;
        font-size: 18px;
        font-weight: bold;
        float: right;
        
}

#textboxfloat {
    float:right;
}

#login #inputtext1 {
	width: 188px;
        height: 20px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	float: right;
}

#login #inputtext7 {
	width: 120px;
        height: 15px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	float: right;
        text-align: center;
}

#login #categoryselect {
	width: 120px;
        height: 23px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	float: right;
        text-align: center;
}

#login #check {
	width: 15px;
        height: 15px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	float: right;
        
}

#login #inputtext5, #login #inputtext6, #login #inputtext8, #login #inputtext13, #login #inputtext14  {
	width: 30px;
        height: 15px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	float: right;
}

#login #inputtext2 {
	width: 20px;
        height: 15px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	float: right;
}

#login #inputtext4 {
	width: 30px;
        height: 15px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
}

#login #inputtext3 {
	width: 20px;
        height: 15px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	
}


#login #inputtext9, #login #inputtext10 {
	width: 20px;
        height: 15px;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
	
        margin-bottom: .5em;
}


#login #inputfile1 {
	
	margin-bottom: .5em;
	font: normal small Calibri;
	color: #000000;
	float: right;
        
}


#inputsubmit1 {
		width : 67px;

	font-size: 15px;
        color : #ffffff;
        
	font-family: Calibri;
        font-weight: bold;
        background: url(images/button_base.png) repeat;
        float: right;
}


#inputcancel {
		width : 67px;

	font-size: 15px;
        color : #ffffff;
        margin-left: 100px;
	font-family: calibri;
        font-weight: bold;
        background: url(images/button_base.png) repeat;
}

#inputsubmit3 {
		width : 67px;
        
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
   height: 25px;
   background: #F7F7F7 url(images/orange_gradient.png) repeat-x;
}


    </style>

  <script language="javascript">
        function checkAlphaNumerics(checkString) {
                var regExp = /^[A-Za-z]$/;
                if(checkString!= null && checkString!= "")
                {
                  for(var i = 0; i < checkString.length; i++)
                  {
                    if ( checkString.charAt(i).match(regExp))
                    {
                      return false;
                    }
                  }
                }
                else
                {
                  return false;
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

        

        function submitform(){

                if(calculateDiscount(1)==true){
                    var category = document.user.categoryselect.value;
                    if(category=='-1'){
                            alert("Select Category");
                            return false;
                     }else{
                          document.forms["user"].submit();
                     }
                   
                }


        }
		function validatefields(from)
		{
                        if(document.user.punchcardname.value=="")
			{
				alert("Enter Punch Card Name");
				document.user.punchcardname.focus();
				return false;
			}
                       
                        var noofpunches=document.user.noofpunches.value;
                        
                       var punchvalue=document.user.punchvalue.value;

                        var sellingprice=document.user.sppunchcard.value;

                        var expiresin = document.user.expiresin.value;
                        var timerestrict = document.user.restrictiontime.value;

                        var minpurval = document.user.minpurval.value;
                        
                        var punchvaluedecimal=document.user.punchvalue_decimal.value;
                        if (punchvaluedecimal==null || punchvaluedecimal==""){
                            punchvaluedecimal = '00';
                            <%--punchvaluedecimal = '';--%>
                            document.user.punchvalue_decimal.value = punchvaluedecimal;
                        }else if(punchvaluedecimal.toString().length==1){
                            punchvaluedecimal = punchvaluedecimal+'0';
                            document.user.punchvalue_decimal.value = punchvaluedecimal;
                        }

                        var sellingpricedecimal=document.user.sppunchcard_decimal.value;
                        if (sellingpricedecimal==null || sellingpricedecimal==""){
                            sellingpricedecimal = '00';
                            document.user.sppunchcard_decimal.value = sellingpricedecimal;
                        }else if(sellingpricedecimal.toString().length==1){
                            sellingpricedecimal = sellingpricedecimal+'0';
                            document.user.sppunchcard_decimal.value = sellingpricedecimal;
                        }
                        var punchval =  punchvalue + '.'+punchvaluedecimal;
                        var sellingval =  sellingprice + '.'+sellingpricedecimal;

                        punchval = parseFloat(punchval);
                        sellingval = parseFloat(sellingval);

                        if(blankspac(noofpunches)==false||blankspac(punchvalue)==false||blankspac(sellingprice)==false||blankspac(expiresin)==false||blankspac(minpurval)==false||blankspac(timerestrict)==false)
                        {
                          alert("Blank Spaces are not allowed");
                            return false
                        }

                        if (noofpunches==null || noofpunches=="")
                        {
                         alert("Enter Number of Punches per Card");
                         return false;
                        }
    
                        if(checkAlphaNumerics(noofpunches)==false)
                        {  alert("Number of Punches should be Numeric");
                            return false;
                        }

                        if(noofpunches==0){
                            alert("Number of Punches cannot be 0");
                            return false;
                        }
                        if (punchvalue==null || punchvalue=="")
                        {
                         alert("Enter Punch Value");
                         return false;
                        }

                        if(checkAlphaNumerics(punchvalue)==false)
                        {  alert("Punch Value should be Numeric");
                            return false;
                        }

                        
                        if(punchval==0){
                            alert("Punch Value cannot be 0");
                            return false;
                        }

                        <%--if(punchvalue==0){
                            alert("Punch Value cannot be 0");
                            return false;
                        }--%>

                        if (sellingprice==null || sellingprice=="")
                        {
                         alert("Enter Selling Price of Punch Card");
                         return false;
                        }

                        if(checkAlphaNumerics(sellingprice)==false)
                        {  alert("Selling Price of Punch Card should be Numeric");
                            return false;
                        }

                        if(sellingval==0){
                            alert("Selling Price of Punch Card cannot be 0");
                            return false;
                        }

                        if(from==1){
                            if (expiresin==null || expiresin=="")
                            {
                             alert("Enter Number of days for Punch Card expires");
                             return false;
                            }

                            if(checkAlphaNumerics(expiresin)==false)
                            {  alert("Punch Card expires value should be Numeric");
                                return false;
                            }
                            expiresin = parseInt(expiresin);
                            if(expiresin<30){
                                alert("Punch Card Expires value cannot be less than 30");
                                return false;
                            }

                            if (minpurval==null || minpurval=="")
                            {
                             alert("Enter Number for Minimum Purchase value");
                             return false;
                            }

                            if(checkAlphaNumerics(minpurval)==false)
                            {  alert("Minimum Purchase value should be Numeric");
                                return false;
                            }
                            minpurval = parseInt(minpurval);
                            if(minpurval<punchval){
                                alert("Minimum Purchase value cannot be more than value of each punch");
                                return false;
                            }

                            if (timerestrict==null || timerestrict=="")
                            {
                             alert("Enter Number for Time Restriction");
                             return false;
                            }

                            if(checkAlphaNumerics(timerestrict)==false)
                            {  alert("Time Restriction value should be Numeric");
                                return false;
                            }
                            timerestrict = parseInt(timerestrict);
                            if(timerestrict==0){
                                alert("Time Restriction value cannot be 0");
                                return false;
                            }
                        }



			return true;
		}

                function calculateDiscount(from)
		{
                    
                    var noofpunches=document.user.noofpunches.value;
                   
                    var punchvalue=document.user.punchvalue.value;
                   
                    var sellingprice=document.user.sppunchcard.value;



                    var discount=0;
                    
                    if(validatefields(from)==true){
                        var punchvaluedecimal=document.user.punchvalue_decimal.value;
                        var sellingpricedecimal=document.user.sppunchcard_decimal.value;
                        
                        var punchval =  punchvalue + '.'+punchvaluedecimal;
                        var sellingval =  sellingprice + '.'+sellingpricedecimal;
                        
                        punchval = parseFloat(punchval);
                        sellingval = parseFloat(sellingval);

                        noofpunches = parseInt(noofpunches);
                        
                        <%--punchvalue = parseInt(punchvalue);

                        sellingprice = parseInt(sellingprice);
                        
                        var costprice = noofpunches * punchvalue ;

                        var discprice_of_each_punch = sellingprice / noofpunches;

                        if(costprice<sellingprice){
                            alert("Selling Price Cannot be Greater than the Actual Cost of Punch Card");
                            document.user.sppunchcard.value="";
                            document.user.sppunchcard.focus;
                            return false;
                        }else{
                            discount = ((costprice-sellingprice)/costprice)*100;
                            discount = parseInt(discount);
                            document.user.discount.value=discount;
                            var roundedNumber = roundNumber(discprice_of_each_punch,2);
                            document.user.disc_value_each_punch.value = roundedNumber;
                        }--%>

                        var costprice = noofpunches * punchval;
                        
                        var discprice_of_each_punch = sellingval / noofpunches;

                        if(costprice<sellingval){
                            alert("Selling Price Cannot be Greater than the Actual Cost of Punch Card");
                            document.user.sppunchcard.value="";
                            document.user.sppunchcard.focus;
                            return false;
                        }else{
                            var punchval =  punchvalue + '.'+punchvaluedecimal;
                            var sellingval =  sellingprice + '.'+sellingpricedecimal;
                            document.user.float_val_of_punch.value = punchval;
                            document.user.float_val_of_punchcard.value = sellingval;
                            discount = ((costprice-sellingval)/costprice)*100;
                            discount = parseInt(discount);
                            document.user.discount.value=discount;
                            var roundedNumber = roundNumber(discprice_of_each_punch,2);
                            document.user.disc_value_each_punch.value = roundedNumber;
                        }

                        return true;
                    }else{
                        return false;
                    }

                }

                function roundNumber(num, dec) {
                    var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
                    return result;
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
            if(calculateDiscount(1)==true){
                  <%--  document.forms["user"].submit();
                }
            if(validateemail()==true){--%>
               if(xmlhttp) {
                   var punch_card_name = document.getElementById("inputtext1");
                   <%--var business_name = document.getElementById("inputtext4");--%>
                    xmlhttp.open("GET","signup_paidpunch_add?punchcardname="+punch_card_name.value,true);//+"&businessname="+business_name.value,true); //getname will be the servlet name
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

                   alert("Punch Card Name already exists");
                   document.user.punchcardname.value="";
                   document.user.punchcardname.focus();
               }
               <%--else if(resptext==02){
                   alert("Business with that name already exists");
                   document.user.businessname.value="";
                   document.user.business_name.focus();
               }--%>
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
        
        
        java.sql.Date sqlDate = null;
         

  /**
   * A calendar formatting object, used throughout. Note that other forms of
   * the Calendar constructor let you pass in Locale, TimeZone, or both, or
   * yy,mm,dd,[hh, mm [, ss]] You can also set your own Daylight Saving rules,
   * fiddle the Gregorian cutover of 1582, and probably the phase of the moon!
   */
            Date time1=new Date();
            Calendar cal1 =  Calendar.getInstance();
            cal1.setTime(time1);
//          int hour= cal1.HOUR;
          cal1.add(Calendar.YEAR,1);
          cal1.add(Calendar.DATE,-1);
          Date date=cal1.getTime();
          System.out.println(""+time1);
          sqlDate = new java.sql.Date(date.getTime());
          System.out.println("<br>"+sqlDate);

  Date timeNow = new Date();
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(timeNow);
    String expiryDate = (calendar.get(Calendar.YEAR)+1)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+(calendar.get(Calendar.DATE)-1);
    System.out.println("Date" + expiryDate);
    String business_name="";
    System.out.println("ERA: " + calendar.get(Calendar.ERA));
        System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
        System.out.println("DAY_OF_MONTH: "+ calendar.get(Calendar.DAY_OF_MONTH));
       
               try{
                 business_name = session.getAttribute("businessname").toString();
                 }catch(Exception e){
                 }
     %>
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
			<h2 align="center"><strong>Customize your PaidPunch card</strong></h2>


		</div>

	</div>


	<table style="font-family: Calibri; font-weight: bold; padding-top: 10px; padding-bottom: 50px;" width="100%">
	<tbody><tr>


<td valign="top" align="center">

		<div id="login" class="boxed">
			<h2 class="title">PaidPunch Card Details</h2>
			<div class="content">
				<%--<form id="form1" name="user" method="post" action="signup_paidpunch_add">--%>
                                <form id="form1" name="user" method="post" action="mysterypunch_creation.jsp">
 
					<fieldset>
					<legend>Paid Punch Card Details</legend>
                                        
					<%--<label for="inputtext1">Punch Card Name<font color="#FF6699">*</font>:</label>--%><input id="inputtext1" type="hidden" name="punchcardname" value="<%=business_name%>" maxlength="25"/>
                                        <input id="inputtext11" type="hidden" name="float_val_of_punch" value="" maxlength="25"/>
                                        <input id="inputtext12" type="hidden" name="float_val_of_punchcard" value="" maxlength="25"/>
					<label for="inputtext2">Number of Punches Per Card<font color="#FF6699">*</font>:</label><input id="inputtext2" type="text" name="noofpunches" value="" maxlength="2" class="positive-integer"/><br><br>
                                        <label for="inputtext3">Value of Each Punch<font color="#FF6699">*</font>:</label><div id="textboxfloat"><label1 for="inputtext3">$</label1><input id="inputtext3" type="text" name="punchvalue" value="" maxlength="2" class="positive-integer"/><label2 for="inputtext9">.</label2><input id="inputtext9" type="text" name="punchvalue_decimal" value="" maxlength="2" class="positive-integer"/></div><br><br>
					<label for="inputtext4">Selling Price of PaidPunch Card<font color="#FF6699">*</font>:</label><div id="textboxfloat"><label1 for="inputtext4">$</label1><input id="inputtext4" type="text" name="sppunchcard" value="" maxlength="4" class="positive-integer"/><label2 for="inputtext10">.</label2><input id="inputtext10" type="text" name="sppunchcard_decimal" value="" maxlength="2" class="positive-integer"/></div><br><br>
					<label for="inputtext5">Effective Discount:</label><div id="textboxfloat"><input id="inputsubmit3" type="button" name="calculate" value="Calculate" onClick = "calculateDiscount(0);"/><label1 for="inputtext5">%</label1><input id="inputtext5" type="text" name="discount" value="" readonly="readonly"/></div><br><br>
                                        <%--<label for="inputtext6">Value of Each Punch after Discount:</label><div id="textboxfloat"><label1 for="inputtext6">$</label1>--%><input id="inputtext6" type="hidden" name="disc_value_each_punch" value="" maxlength="4" readonly="readonly"/>
					<label for="inputtext13">Punch Card Expires(in days):</label><input id="inputtext13" type="text" name="expiresin" maxlength="3" value="30" class="positive-integer"/><br><br>
                                        <label for="inputtext7"></label><input id="inputtext7" type="hidden" name="expirydate" value="<%=sqlDate%>" readonly="readonly"/>
                                        <label for="inputtext8">Restriction Time(in mins):</label><input id="inputtext8" type="text" name="restrictiontime" value="60" maxlength="3" class="positive-integer"/><br><br>
                                        <label for="inputtext14">Minimum Purchase Value:</label><label2 for="inputtext14"></label2><input id="inputtext14" type="text" name="minpurval" maxlength="3" value="" class="positive-integer"/><br><br>
                                        <label for="chkbox">Free Punch:</label><input name="freepunch" type="checkbox" id="check" value="" checked><br><br>
                                        <label for="category">Category:</label><select name="categoryselect" onchange="showState(this.value)" id="categoryselect"><option style="font-family: Calibri;" value="-1">Select</option><option style="font-family: Calibri;" value="eat">Eat</option><option style="font-family: Calibri;" value="drink">Drink</option><option style="font-family: Calibri;" value="relax">Relax</option><option style="font-family: Calibri;" value="essentials">Essentials</option></select><br><br><br>
                                        
                                        <input id="inputsubmit1" type="button" name="inputsubmit1" value="Proceed" onClick = "return submitform();"/>  <input id="inputcancel" type="reset" name="cancel" value="Reset" onClick="form.reset()"/>
					</fieldset>
                                </form>
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
			</div>
		</div>
</td>
</tr>

<tr align="center">

</tr>


<tr><td><br></td></tr>
</tbody></table>


</div>
<div id="footer">
	<p id="legal"></p>
</div>
</body>
</html>
<%
    }
%>