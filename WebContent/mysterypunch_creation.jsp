<%-- 
    Document   : mysterypunch_creation
    Created on : Apr 2, 2012, 2:42:23 AM
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
 <script type="text/javascript" src="scripts/jquery-1-add.js"></script>
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
	width:700px;

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
	padding-bottom: 10px;
        padding-left: 10px;
        padding-right: 10px;
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

#login label2 {
        padding: 2px 5px;
	margin-bottom: .5em;
	font: normal medium Calibri;
        color : #323232;
        font-size: 18px;
        font-weight: bold;

}


#login #mysteryvalue1, #login #mysteryvalue2, #login #mysteryvalue3, #login #mysteryvalue4, #login #mysteryvalue5, #login #mysteryvalue6, #login #mysteryvalue7, #login #mysteryvalue8, #login #mysteryvalue9, #login #mysteryvalue10 {
	width: 275px;
        height: 15px;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
        margin-left: 10px;
        margin-right: 10px;
        margin-bottom: 5px;
        margin-top: 5px;
}

#probability1, #probability2, #probability3, #probability4,#probability5, #probability6, #probability7, #probability8, #probability9, #probability10 ,#totalprobabilty{
	width: 25px;
        height: 15px;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
        margin-left: 10px;
        margin-right: 10px;
        margin-bottom: 5px;
        margin-top: 5px;
}

#inputsubmit1 {
        width : 67px;
	font-size: 15px;
 	font-family: Calibri;
        font-weight: bold;
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

#TextBoxesGroup {
    float: left;
    border: 1px solid #CCCCCC;
    
}

#add_delete_group {
    float: left;
}


#TextBoxDiv1, #TextBoxDiv3, #TextBoxDiv5, #TextBoxDiv7, #TextBoxDiv9 {
    background: #ffffff;
    padding-left: 5px;
    padding-right: 5px;
    color: #000000;

}
#TextBoxDiv2, #TextBoxDiv4, #TextBoxDiv6, #TextBoxDiv8, #TextBoxDiv10 {
    background: #EEE;
    padding-left: 5px;
    padding-right: 5px;
    color: #000000;
}

.backgroundOne {

 background: url(images/bgbck.png) repeat;
color : #808080;


}
.backgroundTwo {

background: url(images/button_base.png) repeat;
color : #ffffff;
}


    </style>

   

 <script language="javascript" type="text/javascript">
 function setHiddenValuemp(idpassed){
          document.user.radioval.value= idpassed;
   }

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



        function submitformmp(){

                if(validatefields()==true){
                    document.forms["user"].submit();
                }


        }
		function validatefields()
		{
                       var cnter=document.user.counterval.value;
                       
                       for(var i=1; i<cnter; i++){
                        var txtboxname1 = 'mysteryvalue'+i;
                        var val1 = document.getElementById(txtboxname1).value;
                        
                        if(val1==null||val1==""){
                            alert("Mystery Value should not be left blank");
                            return false;
                        }
                        

                        var txtboxname = 'probability'+i;
                        var val = document.getElementById(txtboxname).value;
                        
                        if(val==null||val==""){
                            alert("Probabilty should not be left blank");
                            return false;
                        }
                        if(checkAlphaNumerics(val)==false)
                        {  alert("Probability of Mystery Punches should be Numeric.");
                            return false;
                        }
                        
                        if(val==0){
                            alert("Probability cannot be 0");
                            return false;
                        }
                       }
			return true;
		}


           function setRadioSelectedmp(busid){
                    var radios = document.getElementsByName('mysterypunch');
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

          function checkIfRadioSelectedmp(){
                var radios = document.getElementsByName('mysterypunch');
                var val;
                for (var i = 0; i < radios.length; i++) {

                     if (radios[i].type == 'radio' && radios[i].checked) {
                    // get value, set checked flag or do whatever you need to
                         val = radios[i].value;

                    }

                }
                if(val==null){
                    alert("No Mystery Punch Selected");
                    return false;
                }

                return true;
            }


            function calculateTotalProf(){
                
                var cnter=document.user.counterval.value;
                
                var totalvalue = 0;
                for(var i=1; i<cnter; i++){
                    var txtboxname = 'probability'+i;
                    var val = document.getElementById(txtboxname).value;
                    if(val=="")
                        val = 0;
                    totalvalue = totalvalue+parseInt(val);
                }
                var myDiv = document.getElementById("inputsubmit1");

                if(totalvalue>100){
                    alert("The total probablity cannot be greater than 100");
                    document.getElementById("inputsubmit1").disabled = true;
                    myDiv.className += "backgroundOne";

                }else{
                    if(totalvalue==100){
                        document.getElementById("inputsubmit1").disabled = false;
                        myDiv.className = "backgroundTwo";
                    }else{
                        document.getElementById("inputsubmit1").disabled = true ;
                        myDiv.className = "backgroundOne";
                    }
                     document.user.totalprobabilty.value=totalvalue;
                }
                
            }

            function setCounterVal(counter){
                document.user.counterval.value= counter;
            }

            function submitButton(){
                alert("No Mystery Punch Selected");
            }

      
</script>

 <script type="text/javascript" language="javascript">

$(document).ready(function(){

    var counter=document.user.counterval.value;
   
    $("#addButton").click(function () {
       
	if(counter>10){
            alert("Max 10 values are allowed");
            return false;
	}

	var newTextBoxDiv = $(document.createElement('div')).attr("id", 'TextBoxDiv' + counter);

	newTextBoxDiv.after().html(<%--'<input type="radio" class="radiodata" name="mysterypunch" value="'+counter+'" onClick="setHiddenValuemp('+counter+');">' +--%> '<input type="text" name="mysteryvalue' + counter +'" id="mysteryvalue' + counter + '" value="" maxlength="100">'+'</td><td>'+ '<input type="text" name="probability' + counter +'" id="probability' + counter + '" value="" maxlength="2" class="positive-integer" onkeyup="calculateTotalProf();">%'+'<input type="hidden" name="ispaidpunchmystery'+counter+'" id="ispaidpunchmystery'+counter+'" value="N"/><br>');

      	newTextBoxDiv.appendTo("#TextBoxesGroup");


	counter++;
        setCounterVal(counter);
        calculateTotalProf();
     });

     $("#removeButton").click(function () {
         
	if(counter==2){
          alert("You cannot remove any more mystery punches");
          return false;
       }

         var r=confirm("Are you sure you want to delete?");
           if (r==true)
           {
            counter--;
            setCounterVal(counter);
            
            <%--var id = document.user.radioval.value;--%>
            <%--$("#TextBoxDiv" + id).remove();--%>
            $("#TextBoxDiv" + counter).remove();
            calculateTotalProf();
           }
           else
           {
                             
           }
       
	

     });

     $("#getButtonValue").click(function () {

	var msg = '';
	for(i=1; i<counter; i++){
   	  msg += "\n Textbox #" + i + " : " + $('#textbox' + i).val();
	}
    	  alert(msg);
     });

     $(".positive-integer").numeric({ decimal: false, negative: false }, function() { alert("Positive integers only"); this.value = ""; this.focus(); });
     $("#remove").click(
     function(e)
     {
             e.preventDefault();
             $(".numeric,.integer,.positive").removeNumeric();
    }
  );

});


</script>


</head>
<body>
    <%

    String punchCardName="",noofpunches="",valueofpunch="",selling_price_punch="",discount="",expirydate="",value_of_each_punch_disc="";
    String free_punch_value="", card_category="",minpurchaseval="",expireddays="";
            punchCardName = request.getParameter("punchcardname");
            noofpunches = request.getParameter("noofpunches");
//            valueofpunch = request.getParameter("punchvalue");
//            selling_price_punch = request.getParameter("sppunchcard");
            valueofpunch = request.getParameter("float_val_of_punch");
            selling_price_punch = request.getParameter("float_val_of_punchcard");
            discount = request.getParameter("discount");
            expirydate = request.getParameter("expirydate");
            value_of_each_punch_disc = request.getParameter("disc_value_each_punch");
            card_category = request.getParameter("categoryselect");
            String restrict = request.getParameter("restrictiontime");
            minpurchaseval = request.getParameter("minpurval");
            expireddays = request.getParameter("expiresin");

           free_punch_value = request.getParameter("freepunch");
              System.out.println("Value of Punches :\n"+free_punch_value);
            if(free_punch_value!= null)
                free_punch_value = "true";
            else
                free_punch_value = "false";

       /*     restriction_time = Integer.parseInt(restrict);
            disc_value_of_each_punch = (Float.valueOf(value_of_each_punch_disc)).floatValue();
            float_val_of_each_punch = (Float.valueOf(valueofpunch)).floatValue();
            float_val_of_each_punchcard = (Float.valueOf(selling_price_punch)).floatValue();*/

            System.out.println("Value of Punches :\n");
            System.out.println(punchCardName+"\n"+noofpunches+"\n"+valueofpunch+"\n"+selling_price_punch+"\n"+value_of_each_punch_disc+"\n"+restrict+"\n"+discount+"\n"+expirydate+"\n"+free_punch_value);
            com.server.Constants.logger.info(punchCardName+"\n"+noofpunches+"\n"+valueofpunch+"\n"+selling_price_punch+"\n"+value_of_each_punch_disc+"\n"+restrict+"\n"+discount+"\n"+expirydate+"\n"+free_punch_value);

            session.setAttribute("punchcardname",punchCardName);
            session.setAttribute("noofpunches",noofpunches);
            session.setAttribute("float_val_of_punch",valueofpunch);
            session.setAttribute("float_val_of_punchcard",selling_price_punch);
            session.setAttribute("discount",discount);
            session.setAttribute("expirydate",expirydate);
            session.setAttribute("disc_value_each_punch",value_of_each_punch_disc);
            session.setAttribute("restrictiontime",restrict);
            session.setAttribute("freepunch",free_punch_value);
            session.setAttribute("card_category",card_category);
            session.setAttribute("expireddays",expireddays);
            session.setAttribute("minpurchaseval",minpurchaseval);
 
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
			<h2 align="center"><strong>Customize your Mystery Punches here</strong></h2>
                </div>
        </div>
    <table style="font-family: Calibri; font-weight: bold; padding-top: 10px; padding-bottom: 50px;" width="100%">
	<tbody><tr>
            <td valign="top" align="center">
        	<div id="login" class="boxed">
			<h2 class="title">Mystery Punch Details</h2>
			<div class="content">
				<form id="form1" name="user" method="post" action="signup_paidpunch_add">

					<fieldset>
					<legend>Mystery Punch Details</legend>
                                        
                                        <div style="width:100%;"><div>
                                                <div style="width:60%; float: left" >

                                                    <input id="hidden1" type="hidden" value="" name="radioval" readonly="readonly"/>
                                                    <input id="hidden2" type="hidden" value="2" name="counterval" readonly="readonly"/>
                                                    <div id="TextHeader" style="margin-right: 5px;  color: #000000; font-family: Calibri; font-size: 15px;"><div style="float: left; margin-left: 35px;">Prize</div> <div style="float: right">Probability</div></div>
                                                    <div id='TextBoxesGroup'>
                                                        <div id="TextBoxDiv1">

                                                            <%--<input type="radio" class="radiodata" name="mysterypunch" value="1" onClick="setHiddenValuemp(1);"/>--%>
                                                            <input type='textbox' name='mysteryvalue1' id='mysteryvalue1' value="" maxlength="100"/>
                                                            <input type="textbox" name="probability1" id="probability1" value="" maxlength="3" class="positive-integer" onkeyup="calculateTotalProf();"/>%
                                                            <input type="hidden" name="ispaidpunchmystery1" id="ispaidpunchmystery1" value="N"/>
                                                        </div>
                                            
                                                    </div>
                                         
                                             </div>
                                             <div style="width:40%; float: right;" align="center"></div>
                                         </div>
                                         <div><div style="width:60%; float: left;">
                                                 <div>
                                                     <div id="add_delete_group" style="float: left;"><input type='button' value='+ Add' id='addButton' style="background: url(images/button_base.png) repeat; width: 70px; margin: 5px;">
                                                        <input type='button' value='Delete' id='removeButton' style="background-color: #f1f1f1; width: 70px; margin: 5px;">
                                                    </div>
                                                     <div style="margin-right: 10px; color:#000000; float: right">
                                                         Total<input type="textbox" id="totalprobabilty" name="totalprobabilty" value="0" maxlength="3" class="positive-integer" readonly="readonly" onkeyup="calculateTotalProf();">%
                                                     </div>
                                                 </div>
                                             </div>
                                             <div style="width:40%; float: right;" >
                                                 <div>
                                                     <div style="float: left; padding-top: 2px; padding-left: 2px;">
                                                         <img src="images/mystery_arrow.png" align="middle">
                                                     </div>
                                                     <div style="float: right; border: 1px solid #CCCCCC; background-color: #ffffff; padding: 2px 5px ; width: 225px;">
                                                         Must Total 100%. Increase probabilities or add more prizes.
                                                     </div>
                                                 </div>
                                             </div>
                                         </div><br>
                                         <div style="width:100%"><div style="float: left; margin-left: 300px; margin-top: 10px;"align="center">
                                                 <input id="inputsubmit1" type="button" class="backgroundOne" name="inputsubmit1" value="Sign Up" onClick = "return submitformmp();" disabled/>
                                             </div>
                                         </div>
                                         

                                     </div>
                                            
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