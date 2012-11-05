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
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>

<%@page import="java.util.GregorianCalendar"%>
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
<script language="javaScript" type="text/javascript" src="scripts/calendar.js"></script>
<link href="cssfiles/calendar.css" rel="stylesheet" type="text/css">
<script language="javascript">

        function setRadioSelected(userid){

           var radios = document.getElementsByName('userid');
            var val;
            for (var i = 0; i < radios.length; i++) {
               if (radios[i].type == 'radio' && radios[i].value==userid) {
                // get value, set checked flag or do whatever you need to
                     radios[i].checked = true;
                     setHiddenValue(userid);
                     break;

                }

            }

}

            function checkIfRadioSelected(){
                var radios = document.getElementsByName('userid');
                var val;
                for (var i = 0; i < radios.length; i++) {

                     if (radios[i].type == 'radio' && radios[i].checked) {
                    // get value, set checked flag or do whatever you need to
                         val = radios[i].value;

                    }

                }
                if(val==null){
                    alert("No User Selected");
                    return false;
                }

                return true;
            }

            function setHiddenVals(useridpassed){
                
                document.userlist.radioval.value= useridpassed;
                
            }

            
            function viewBusinessClicked(){
                if(checkIfRadioSelected()==true){
                    document.forms["userlist"].submit();
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

	margin-top:10px;
	margin-bottom: 20px;
	background: #F7F7F7 ;
	border: 1px solid #CCCCCC;
	width:850px;
        font-family: Calibri;

}

.boxed .title {
	height: 20px;
	padding: 7px 0 0 7px;
	background: url(images/orange_gradient.png) repeat-x 10px 50%;
	text-transform: uppercase;
	font-size: 18px;
	color: #FFFFFF;
	text-align: left;
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
	padding: 15px 0;

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


#label {
        margin-left: 5px;
	font: normal medium Calibri;
        font-size:15px;
        color : #323232;
        font-weight: bold;

}

#label2 {

	margin-bottom: .5em;

	font: normal medium Calibri;
        font-size:15px;
        color : #323232;
        font-weight: bold;


}


#label1 {

        margin-right: 5px;
	font: normal medium Calibri;
        font-size:15px;
        color : #F47B27;
        font-weight: bold;

}

#label3 {

	margin-bottom: .5em;
        margin-right: 5px;
        margin-left: 10px;
	font: normal medium Calibri;
        font-size:15px;
        color : #F47B27;
        font-weight: bold;

}

#login #inputtext1{
	width: 180px;
	margin-bottom: .5em;
	padding: 2px 5px;
	border: 1px solid #CCCCCC;
	font: normal small Calibri;
	color: #323232;
        font-weight: bold;

}






#inputsubmit1, #inputsubmit2, #inputsubmit3 {
	width : 67px;
	font-size: 15px;
        color : #ffffff;
        margin-right: 25px;
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
    height: 25px;
	position:fixed;
       bottom:0;
   width:100%;
   background: #F47B27 url(images/orange_gradient.png) repeat-x 0 0;

}


div.tableContainer {
	clear: both;
	border: 0px solid #963;
	height: 330px;
	overflow: auto;
	width:850px;
}



/* set table header to a fixed position. WinIE 6.x only                                       */
/* In WinIE 6.x, any element with a position property set to relative and is a child of       */
/* an element that has an overflow property set, the relative value translates into fixed.    */
/* Ex: parent element DIV with a class of tableContainer has an overflow property set to auto */
thead.fixedHeader tr {
	position: relative
}


/* make the TH elements pretty */
thead.fixedHeader th {

	border-left: 0px solid #EB8;
	border-right: 0px solid #B74;
	border-top: 0px solid #EB8;
	font-weight: bold;
	padding: 2px 3px 3px 4px;
	
        color:#F47B27;
        font-size: 16px;
        font-family: Calibri;
}

/* make the A elements pretty. makes for nice clickable headers                */
thead.fixedHeader a, thead.fixedHeader a:link, thead.fixedHeader a:visited {
	color: #FFF;
	display: block;
	text-decoration: none;
	width: 100%
}

/* make the A elements pretty. makes for nice clickable headers                */
/* WARNING: swapping the background on hover may cause problems in WinIE 6.x   */
thead.fixedHeader a:hover {
	color: #FFF;
	display: block;
	text-decoration: underline;
	width: 100%
}



/* make TD elements pretty. Provide alternating classes for striping the table */
/* http://www.alistapart.com/articles/zebratables/                             */
tbody.scrollContent td, tbody.scrollContent tr.normalRow td {
	background: #FFF;
	font-size: 14px;
        color: #323232;
	padding: 2px 3px 3px 4px;
        font-family: Calibri;
        height: 30px;
}

tbody.scrollContent tr.alternateRow td {
	background: #EEE;
	font-size: 14px;
        color: #323232;
	padding: 2px 3px 3px 4px;
        font-family: Calibri;
        height: 30px;
}

tbody.scrollContent td {
	width: 30px
}

tbody.scrollContent td + td {
	width: 200px
}

tbody.scrollContent td + td + td {
	width: 250px
}

tbody.scrollContent td + td + td +td {
	width: 200px
}

tbody.scrollContent td + td + td + td + td{
	width: 200px
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
                    <li  ><a href="business_admin_user.jsp">Home</a>

                    </li>
                    <li ><a href="manage_business.jsp">Businesses</a>

                    </li>
                    <li class="navtext-sel"><a href="overview_business.jsp">Users</a>

                    </li>

                </ul>
                <a href="signout.jsp" style="color: #323232; float: right; height: 32px; margin-top: 10px; margin-left: 0;">Signout</a>

        </div>
        <br>
<%
    long totalusersRegistered=0,totalusers24hrs=0,totalusersweek=0,totalusersmonth=0;
    java.sql.Date current = null;
    java.sql.Date sqlDate24 = null;
    java.sql.Date sqlDateWeek = null;
    java.sql.Date sqlDateMonth = null;

         Date time1=new Date();
         Calendar cal1 =  Calendar.getInstance();
         cal1.setTime(time1);
         Date date=cal1.getTime();
         current = new java.sql.Date(date.getTime());
         

          cal1.add(Calendar.DATE,-1);
          date=cal1.getTime();
          sqlDate24 = new java.sql.Date(date.getTime());

          cal1 = Calendar.getInstance();
          cal1.setTime(time1);
          cal1.add(Calendar.DATE,-7);
          date=cal1.getTime();
          sqlDateWeek = new java.sql.Date(date.getTime());

          cal1 = Calendar.getInstance();
          cal1.setTime(time1);
          cal1.add(Calendar.DATE,-30);
          date=cal1.getTime();
          sqlDateMonth = new java.sql.Date(date.getTime());




            DBConnection db = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                               db = new DBConnection();
                               stmt = db.stmt;

                               try{

                               String query = "SELECT COUNT(*) FROM app_user";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalusersRegistered = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }
                               com.server.Constants.logger.info("\nTotal App Users"+totalusersRegistered);


                             try{

                               //String query = "SELECT COUNT(*) FROM punchcard_download";
                                   String query = "SELECT count(*) FROM app_user WHERE DATE >= '"+sqlDate24+"' AND DATE <= '"+current+"';";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalusers24hrs = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }
                               com.server.Constants.logger.info("\nTotal App Users in Last 24 Hours"+totalusers24hrs);

                              try{

                               String query = "SELECT count(*) FROM app_user WHERE DATE > '"+sqlDateWeek+"' AND DATE <= '"+current+"';";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalusersweek = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }
                              com.server.Constants.logger.info("\nTotal App Users in Last Week"+totalusersweek);

                              try{

                               String query = "SELECT count(*) FROM app_user WHERE DATE > '"+sqlDateMonth+"' AND DATE <= '"+current+"';";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);

                              if(rs.next()){
                                totalusersmonth = rs.getLong(1);
                              }
                              }catch(SQLException e){
                                  com.server.Constants.logger.error("Error in Sql in qrCode_display.jsp in getting QR CODE Value "+e.getMessage());
                                 throw new ServletException("SQL Exception.", e);
                              }
                             com.server.Constants.logger.info("\nTotal App Users in Last Month"+totalusersmonth);

                               

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


	<table style="font-family: Arial,Helvetica,sans-serif; padding-top: 10px; padding-bottom: 50px;" width="100%">
            <tbody>
            <tr>


<td valign="top" align="center">

		<div id="login" class="boxed">
                    <h2 class="title"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/>User Overview for PaidPunch</h2>
			<div class="content">


                                <Table width="98%">
                                    <TR ><TD id="label" width="30%" align="left">Total Users Registered</TD><TD width="3%" id="label2">:</TD><TD width="15%" id="label1" align="left"><%=totalusersRegistered%></TD><TD width="30%" id="label" align="left">Total Users Registered in last Week</TD><TD width="3%" id="label2">:</TD><TD width="15%" id="label1" align="left"><%=totalusersweek%></TD></TR>
                                    <TR><TD width="30%" id="label" align="left">Total Users Registered in last 24 hours</TD><TD width="3%" id="label2">:</TD><TD width="15%" id="label1" align="left"><%=totalusers24hrs%></TD><TD id="label" width="30%" align="left">Total Users Registered in last Month</TD><TD width="3%" id="label2">:</TD><TD width="15%" id="label1" align="left"><%=totalusersmonth%></TD></TR>
                                </Table>

			</div>
		</div>
</td>
</tr>

            <form id="form1" name="userlist" action="user_detail_view_admin.jsp" method="post">
            <tr><td valign="top" align="center">
                    <input id="hidden1" type="hidden" value="" name="radioval" readonly="readonly"/>
                    
            <div id="busilist" class="boxed">
                <h2 class="title"><img id="arrow" src="images/arrows.png" alt="arrows" style="padding-right: 15px;"/>Application User List</h2>
			<div class="content">


                         <div id="tableContainer" class="tableContainer">
<table class="scrollTable" border="0" cellpadding="0" cellspacing="0" width="100%">
<thead class="fixedHeader">

    <%


    String user_name[];
    String user_email[];
    String user_zipcode[];
    String user_number[];
    int userId[];
    String email_verified[];
    String signupDate[];
    String signupTime[];
    String isfb_user[];
    String updateDate[];


           // DBConnection db = null;
           // Statement stmt = null;
           // ResultSet rs = null;

            try {
                               db = new DBConnection();
                               stmt = db.stmt;

                               String query = "SELECT app_user.user_id,app_user.username,app_user.email_id,app_user.mobile_no, app_user.pincode,app_user.isemailverified,app_user.Date,app_user.Time,app_user.isfbaccount FROM app_user";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                              rs.last();
                              int row = rs.getRow();

                              userId = new int[row];
                              user_name = new String[row];
                              user_email = new String[row];
                              user_zipcode = new String[row];
                              user_number = new String[row];
                              isfb_user = new String[row];
                              email_verified = new String[row];
                              signupDate = new String[row];
                              signupTime = new String[row];
                              updateDate = new String[row];
                               //String query = "SELECT punch_card_name,qrcode from punch_card where business_userid=(Select business_userid from business_users where email_id='"+username+"')";
                               //query = "SELECT business_users.business_userid,business_users.business_name,punch_card.punch_card_name,punch_card.expiry_date FROM business_users,punch_card where business_users.role='user' and punch_card.business_userid=business_users.business_userid";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                              // displaying records
                                int i=0;

                              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                              while(rs.next()){
                               // int busid = rs.getInt(1);
                                userId[i] = rs.getInt(1);
                                user_name[i] = rs.getString(2);
                                user_email[i] = rs.getString(3);
                                user_number[i] = rs.getString(4);
                                user_zipcode[i] = rs.getString(5);
                                email_verified[i] = rs.getString(6);
                                signupDate[i] = rs.getDate(7).toString();
                                signupTime[i] = rs.getTime(8).toString();

                                updateDate[i] = signupDate[i]+" " +signupTime[i];

                                Date date1 = (Date)sdf.parse(updateDate[i]);
                                Calendar cal2 = Calendar.getInstance();
                                cal2.setTime(date1);
                                cal2.add(Calendar.HOUR, -7);
                                System.out.println("Current Time1 : "+sdf.format(cal2.getTime()));
                                updateDate[i] = sdf.format(cal2.getTime());

                                isfb_user[i] = rs.getString(9);
                                if(user_zipcode[i]==null){
                                    user_zipcode[i] = "";
                                }
                                if(user_number[i] ==null){
                                    user_number[i] = "";
                                }

                                System.out.println(userId[i]+" "+user_name[i]+" "+user_email[i]+" "+user_number[i]+" "+user_zipcode[i]+" "+email_verified);
                                i++;
                              }




                      }catch (SQLException e) {
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


<tr class="alternateRow" >
    <th width="4%"></th>
		<th align="left" width="22%">Name</th>
		<th align="left" width="22%">Email</th>
                <th align="center" width="10%">Contact</th>
		<th align="center" width="10%">Zipcode</th>
                <th align="center" width="10%">Facebook User</th>
                <th align="center" width="22%">Signedup On</th>
                

               
	</tr>
</thead>
<tbody class="scrollContent">
    <%
            for(int i=1;i<=userId.length;i++){
                if(i % 2 != 0){
    %>
    <tr class="normalRow" valign="middle">
        <td align="center"><input type="radio" class="radiodata" name="userid" value="<%=userId[i-1]%>" onClick="setHiddenVals(<%=userId[i-1]%>);"></td> <%--javascript:busilist.radioval.value= '<%=businessId[i-1]%>' " ></td>--%>
		<td align="left"><%=user_name[i-1]%></td>
                <td align="left"><%=user_email[i-1]%></td>
                <td align="center"><%=user_number[i-1]%></td>
                <td align="center"><%=user_zipcode[i-1]%></td>
                <td align="center"><%=isfb_user[i-1]%></td>
                <td align="center"><%=updateDate[i-1]%></td>
               
	</tr>
        <%
                }else{
%>
<tr class="alternateRow" valign="middle">
                <td  align="center" ><input type="radio" class="radiodata" name="userid" value="<%=userId[i-1]%>" onClick="setHiddenVals(<%=userId[i-1]%>);"></td> <%-- javascript:busilist.radioval.value= '<%=businessId[i-1]%>'"></td>--%>
		<td align="left"><%=user_name[i-1]%></td>
                <td align="left"><%=user_email[i-1]%></td>
                <td align="center"><%=user_number[i-1]%></td>
                <td align="center"><%=user_zipcode[i-1]%></td>
                <td align="center"><%=isfb_user[i-1]%></td>
                <td align="center"><%=updateDate[i-1]%></td>
                
	</tr>
	<%
                }
            }
        %>


</tbody>
</table>
</div>


			</div>
		</div>
     </td></tr>


<tr align="center">


<td valign="top" align="center" >
    <input id="inputsubmit1" type="button" name="view_btn" value="View" onClick="viewBusinessClicked();"/>
    <BR><BR>
</td>

</tr>



<tr><td><br><br><br></td></tr>
</form>
</tbody></table>




<div id="footer">
	<p id="legal"></p>
</div>
</body>
</html>
<%
}
%>