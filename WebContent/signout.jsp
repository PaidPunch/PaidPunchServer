<%-- 
    Document   : signout
    Created on : Nov 2, 2011, 1:07:07 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            session.setAttribute("username", "");
            session.setAttribute( "username", null) ;
            response.sendRedirect("login.jsp");
        %>
    </body>
</html>
