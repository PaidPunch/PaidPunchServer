<%--
    Document   : newjsp
    Created on : Oct 25, 2011, 12:53:32 PM
    Author     : rohan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.server.Constants"%>
<%@page import="com.jspservlets.DBConnection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.mysql.jdbc.Statement"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>

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
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <style>

        
        tr.normalRow td {
	background: #FFF;
        

}

    tr.alternateRow td {
	background: #EEE;
        

}
          
          table {
            border: 0px solid black; 
            width: 550px;
            height: 50%;
            font-family: Calibri;
          }

          
          #tablePagination {
            background-color: #DCDCDC;
            font-size: 0.8em;
            padding: 0px 5px;
            height: 20px;
            width:540px;
            font-family: Calibri;
          }

          #tablePagination_paginater {
            margin-left: auto;
            margin-right: auto;
          }

          #tablePagination img {
            padding: 0px 2px;
          }

          #tablePagination_perPage {
            float: left;
          }

          #tablePagination_paginater {
            float: right;
          }

    </style>
     <script src="scripts/jQueryNew.js" type="text/javascript"></script>
    <script>
        /**
         * tablePagination - A table plugin for jQuery that creates pagination elements
         *
         * http://neoalchemy.org/tablePagination.html
         *
         * Copyright (c) 2009 Ryan Zielke (neoalchemy.com)
         * licensed under the MIT licenses:
         * http://www.opensource.org/licenses/mit-license.php
         *
         * @name tablePagination
         * @type jQuery
         * @param Object settings;
         *      firstArrow - Image - Pass in an image to replace default image. Default: (new Image()).src="./images/first.gif"
         *      prevArrow - Image - Pass in an image to replace default image. Default: (new Image()).src="./images/prev.gif"
         *      lastArrow - Image - Pass in an image to replace default image. Default: (new Image()).src="./images/last.gif"
         *      nextArrow - Image - Pass in an image to replace default image. Default: (new Image()).src="./images/next.gif"
         *      rowsPerPage - Number - used to determine the starting rows per page. Default: 5
         *      currPage - Number - This is to determine what the starting current page is. Default: 1
         *      optionsForRows - Array - This is to set the values on the rows per page. Default: [5,10,25,50,100]
         *      ignoreRows - Array - This is to specify which 'tr' rows to ignore. It is recommended that you have those rows be invisible as they will mess with page counts. Default: []
         *
         *
         * @author Ryan Zielke (neoalchemy.org)
         * @version 0.2
         * @requires jQuery v1.2.3 or above
         */

        (function($){

            $.fn.tablePagination = function(settings) {
                var defaults = {
                    firstArrow : (new Image()).src="images/First.png",
                    prevArrow : (new Image()).src="images/Previous.png",
                    lastArrow : (new Image()).src="images/Last.png",
                    nextArrow : (new Image()).src="images/Next_1.png",
                    rowsPerPage : 25,
                    currPage : 1,
                    optionsForRows : [25,50,75,100],
                    ignoreRows : []
                };
                settings = $.extend(defaults, settings);

                return this.each(function() {
                    var table = $(this)[0];
                    var totalPagesId, currPageId, rowsPerPageId, firstPageId, prevPageId, nextPageId, lastPageId
                    if (table.id) {
                        totalPagesId = '#'+table.id+'+#tablePagination #tablePagination_totalPages';
                        currPageId = '#'+table.id+'+#tablePagination #tablePagination_currPage';
                        rowsPerPageId = '#'+table.id+'+#tablePagination #tablePagination_rowsPerPage';
                        firstPageId = '#'+table.id+'+#tablePagination #tablePagination_firstPage';
                        prevPageId = '#'+table.id+'+#tablePagination #tablePagination_prevPage';
                        nextPageId = '#'+table.id+'+#tablePagination #tablePagination_nextPage';
                        lastPageId = '#'+table.id+'+#tablePagination #tablePagination_lastPage';
                    }
                    else {
                        totalPagesId = '#tablePagination #tablePagination_totalPages';
                        currPageId = '#tablePagination #tablePagination_currPage';
                        rowsPerPageId = '#tablePagination #tablePagination_rowsPerPage';
                        firstPageId = '#tablePagination #tablePagination_firstPage';
                        prevPageId = '#tablePagination #tablePagination_prevPage';
                        nextPageId = '#tablePagination #tablePagination_nextPage';
                        lastPageId = '#tablePagination #tablePagination_lastPage';
                    }

                    var possibleTableRows = $.makeArray($('tbody tr', table));
                    var tableRows = $.grep(possibleTableRows, function(value, index) {
                        return ($.inArray(value, defaults.ignoreRows) == -1);
                    }, false)

                    var numRows = tableRows.length
                    var totalPages = resetTotalPages();
                    var currPageNumber = (defaults.currPage > totalPages) ? 1 : defaults.currPage;
                    if ($.inArray(defaults.rowsPerPage, defaults.optionsForRows) == -1)
                        defaults.optionsForRows.push(defaults.rowsPerPage);


                    function hideOtherPages(pageNum) {
                        if (pageNum==0 || pageNum > totalPages)
                            return;
                        var startIndex = (pageNum - 1) * defaults.rowsPerPage;
                        var endIndex = (startIndex + defaults.rowsPerPage - 1);
                        $(tableRows).show();
                        for (var i=0;i<tableRows.length;i++) {
                            if (i < startIndex || i > endIndex) {
                                $(tableRows[i]).hide()
                            }
                        }
                    }

                    function resetTotalPages() {
                        var preTotalPages = Math.round(numRows / defaults.rowsPerPage);
                        var totalPages = (preTotalPages * defaults.rowsPerPage < numRows) ? preTotalPages + 1 : preTotalPages;
                        if ($(totalPagesId).length > 0)
                            $(totalPagesId).html(totalPages);
                        return totalPages;
                    }

                    function resetCurrentPage(currPageNum) {
                        if (currPageNum < 1 || currPageNum > totalPages)
                            return;
                        currPageNumber = currPageNum;
                        hideOtherPages(currPageNumber);
                        $(currPageId).val(currPageNumber)
                    }

                    function resetPerPageValues() {
                        var isRowsPerPageMatched = false;
                        var optsPerPage = defaults.optionsForRows;
                        optsPerPage.sort(function (a,b){return a - b;});
                        var perPageDropdown = $(rowsPerPageId)[0];
                        perPageDropdown.length = 0;
                        for (var i=0;i<optsPerPage.length;i++) {
                            if (optsPerPage[i] == defaults.rowsPerPage) {
                                perPageDropdown.options[i] = new Option(optsPerPage[i], optsPerPage[i], true, true);
                                isRowsPerPageMatched = true;
                            }
                            else {
                                perPageDropdown.options[i] = new Option(optsPerPage[i], optsPerPage[i]);
                            }
                        }
                        if (!isRowsPerPageMatched) {
                            defaults.optionsForRows == optsPerPage[0];
                        }
                    }

                    function createPaginationElements() {
                        var htmlBuffer = [];
                        htmlBuffer.push("<div id='tablePagination'>");
                        htmlBuffer.push("<span id='tablePagination_perPage'>");
                        htmlBuffer.push("<select id='tablePagination_rowsPerPage'><option value='5'>5</option></select>");
                        htmlBuffer.push("per page");
                        htmlBuffer.push("</span>");
                        htmlBuffer.push("<span id='tablePagination_paginater'>");
                        htmlBuffer.push("<img id='tablePagination_firstPage' src='"+defaults.firstArrow+"'>");
                        htmlBuffer.push("<img id='tablePagination_prevPage' src='"+defaults.prevArrow+"'>");
                        htmlBuffer.push("Page");
                        htmlBuffer.push("<input id='tablePagination_currPage' type='input' value='"+currPageNumber+"' size='1'>");
                        htmlBuffer.push("of <span id='tablePagination_totalPages'>"+totalPages+"</span>");
                        htmlBuffer.push("<img id='tablePagination_nextPage' src='"+defaults.nextArrow+"'>");
                        htmlBuffer.push("<img id='tablePagination_lastPage' src='"+defaults.lastArrow+"'>");
                        htmlBuffer.push("</span>");
                        htmlBuffer.push("</div>");
                        return htmlBuffer.join("").toString();
                    }

                    if ($(totalPagesId).length == 0) {
                        $(this).after(createPaginationElements());
                    }
                    else {
                        $('#tablePagination_currPage').val(currPageNumber);
                    }
                    resetPerPageValues();
                    hideOtherPages(currPageNumber);

                    $(firstPageId).bind('click', function (e) {
                        resetCurrentPage(1)
                    });

                    $(prevPageId).bind('click', function (e) {
                        resetCurrentPage(currPageNumber - 1)
                    });

                    $(nextPageId).bind('click', function (e) {
                        resetCurrentPage(currPageNumber + 1)
                    });

                    $(lastPageId).bind('click', function (e) {
                        resetCurrentPage(totalPages)
                    });

                    $(currPageId).bind('change', function (e) {
                        resetCurrentPage(this.value)
                    });

                    $(rowsPerPageId).bind('change', function (e) {
                        defaults.rowsPerPage = parseInt(this.value, 10);
                        totalPages = resetTotalPages();
                        resetCurrentPage(1)
                    });

                })
            };
        })(jQuery);
    </script>

     <script>
         $(document).ready(function()
            {
                $('table').tablePagination({});
            });
     </script>
    <body>

<table id="menuTable" border="0" cellspacing="0" cellpadding="0">
<%

    String transaction_date="",transaction_time="", app_user_name="",isFreePunch="";
    String email_id = "";
    
    //long noOfPunchesRedeemed=0;

            DBConnection db = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                               db = new DBConnection();
                               stmt = db.stmt;

                               try{
                               int pid=0;
                               String query1 = "Select punch_card_id from punch_card where business_userid=(Select business_userid from business_users where email_id='"+username+"');";
                               rs = stmt.executeQuery(query1);
                               if(rs.next()){
                                     pid = rs.getInt(1);
                               }
                               //String query = "Select tracker_date,tracker_time,count(tracker_date) from punch_card_tracker where punch_card_id="+pid+" Group BY tracker_date,tracker_time order By tracker_date,tracker_time desc;";
                               //String query = "Select tracker_date,tracker_time,app_user.email_id,app_user.username from punch_card_tracker,app_user where punch_card_id="+pid+" and app_user.user_id=punch_card_tracker.app_user_id Order By tracker_date desc,tracker_time desc;";
                               String query = "Select tracker_date,tracker_time,app_user.email_id,app_user.username,punchcard_download.isfreepunch from punch_card_tracker,app_user,punchcard_download where punch_card_tracker.is_mystery_punch='false' and punch_card_tracker.punch_card_id="+pid+" and app_user.user_id=punch_card_tracker.app_user_id and punchcard_download.punch_card_downloadid=punch_card_tracker.punch_card_downloadid Order By tracker_date desc,tracker_time desc;";
                               rs = stmt.executeQuery(query);
                               com.server.Constants.logger.info("The select query is " + query);
                               int i=1;
                              if(!rs.next()){
                           %>
                              <tr class="normalRow" align="center">
                                  <td width="100%" style="font-family: Calibri; font-size: 20px; padding: 5px;">No Punches Redeemed</td>

                              </tr>
                              <%
                               }else{
                                   rs = stmt.executeQuery(query);
                                   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                              while(rs.next()){
                               transaction_date = rs.getDate(1).toString();
                               transaction_time = rs.getTime(2).toString();

                               String combined_date = transaction_date +" "+ transaction_time;

                               Date date = (Date)sdf.parse(combined_date);
                               Calendar cal1 = Calendar.getInstance();
                               cal1.setTime(date);
                               cal1.add(Calendar.HOUR, -7);
                                System.out.println("Current Time1 : "+sdf.format(cal1.getTime()));
                               String updatedTime = sdf.format(cal1.getTime());



                               email_id = rs.getString(3);
                               app_user_name = rs.getString(4);

                               isFreePunch = rs.getString(5);
                               if(isFreePunch.equalsIgnoreCase("true")){
                                    isFreePunch="Y";
                               }else{
                                    isFreePunch="N";
                               }
                               //noOfPunchesRedeemed = rs.getLong(3);
                              if(i%2!=0)
                                {
%>
                              <tr class="normalRow" align="center">
                                    <td width="30%"><%=updatedTime%></td>
                                    <%--<td width="12%"><%=transaction_time%></td>--%>
                                    <td width="30%"><%=app_user_name%></td>
                                    <td width="30%"><%=email_id%></td>
                                    <td width="10%"><%=isFreePunch%></td>
                              </tr>
                              <%
                              }else{
%>
                              <tr class="alternateRow" align="center">
                                    <td width="30%"><%=updatedTime%></td>
                                    <%--<td width="15%"><%=transaction_time%></td>--%>
                                    <td width="30%"><%=app_user_name%></td>
                                    <td width="30%"><%=email_id%></td>
                                     <td width="10%"><%=isFreePunch%></td>
                              </tr>
                              <%
                               }
                                i++;
                             }
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
            
        </table>


    </body>
</html>
<%
    }
%>