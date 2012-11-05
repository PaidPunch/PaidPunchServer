/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app;

import com.db.DataAccess;
import com.db.DataAccessControler;
import com.jspservlets.signup_paidpunch_add;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.server.Constants;
import com.server.Feedbean;
import com.server.SAXParserExample;
import com.server.aczreqElements;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import org.xml.sax.InputSource;

/**
 *
 * @author qube26
 */
public class FB_login extends HttpServlet {

    ServletConfig config = null;
    private Vector userdata, userinfo;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    ServletContext context;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            response.setContentType("text/html;charset=UTF-8");
            List list;
            try {
                config = getServletConfig();
                context = config.getServletContext();
                Constants.loadJDBCConstants(context);
            } catch (Exception e) {
                Constants.logger.error(e);
            }
            try {
                ServletInputStream in = request.getInputStream();

                SAXParserExample example = new SAXParserExample();


                int info;
                StringBuffer sb = new StringBuffer();
                while ((info = in.read()) != -1) {
                    char temp;
                    temp = (char) info;
                    sb.append(temp);
                }
                Constants.logger.info("-------------paid_punch---------------");

                Constants.logger.info("XML File" + sb);
                String xmldata = new String(sb);
                xmldata = xmldata.trim();
                InputSource iSource = new InputSource(new StringReader(xmldata));

                example.parseDocument(iSource);
                list = example.getData();
                aczreqElements arz = (aczreqElements) list.get(0);
                String reqtype = arz.getTxtype();




                if (reqtype.equalsIgnoreCase("FB-LOGIN-REQ")) {
                    String name, email, fbid, sessionid;
                    DataAccess da = new DataAccess();
                    name = arz.getName();
                    email = arz.getEmail();
                    fbid = arz.getFbid();
                    sessionid = arz.getSessionid();
                    boolean res = da.check_fbid(fbid);
                    //res true means already reg OR  false means first time login
                    Vector app_user;
                    String app_id;
                    String isproifleid="false";
                    if (res) {
                        da.fb_login(fbid, email, name, sessionid);
                        Vector userinfo = DataAccessControler.getDataFromTable("app_user", "FBid", fbid);
                        app_user = (Vector) userinfo.get(0);
                        app_id =""+app_user.get(0);
                        isproifleid=""+app_user.get(13);


                    } else {
                        da.fb_Registration(fbid, email, name, sessionid);
                        Vector userinfo = DataAccessControler.getDataFromTable("app_user", "FBid", fbid);
                       String temp=name;
                        String namearray[]=temp.split(" ");
              String lastName="";
              if(namearray.length>1)
              {
                lastName=namearray[namearray.length-1];
                char firstChar=lastName.charAt(0);
                //lastName=""+firstChar;
                //namearray[namearray.length-1]=lastName;
              }
              temp="";
              for(int index=0;index<namearray.length-1;index++)
              {
                  temp=temp+namearray[index];
                  temp=temp+" ";
              }
              temp=temp.trim();
              // email function call

                        app_user = (Vector) userinfo.get(0);
                        app_id =""+app_user.get(0);
                         signup_paidpunch_add  emailsender=new signup_paidpunch_add();
                                 emailsender.sendConfirmationEmail(email,temp);
                        
                    }
                    xmllogin(response, "Login Successful", app_id, sessionid,isproifleid);
                   

                }
            } catch (Exception ex) {
                Constants.logger.error(ex);
            }
        } catch (Exception e) {
            Constants.logger.error(e);
        }


    }

    private void xmllogin(HttpServletResponse p_response, String mesage, String user_id, String session,String iscreated) {
      
        String statusCode = "00";
        String statusMessage = mesage;

        try {
            PrintWriter out = p_response.getWriter();

            Constants.logger.info("Respones userid   " + user_id);
            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);

            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            out.print("<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + "<statusCode>" + statusCode + "</statusCode>");
            String respons = "<userid>" + user_id + "</userid>"
                    + "<sessionid>" + session + "</sessionid>"
                    + "<is_profileid_created>" + iscreated + "</is_profileid_created>";
            ;
            out.print(respons);
            Constants.logger.info("respons" + respons);


            out.print("<statusMessage>" + statusMessage + "</statusMessage>");


            out.print("</paidpunch-resp>");


        } catch (Exception e) {
            Constants.logger.error(e);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    private void feeds(List list, HttpServletResponse response) {
        ArrayList<Feedbean> feedlist = new ArrayList<Feedbean>();
        DataAccess da = new DataAccess();
        feedlist = da.getdownloadfeed();
        ArrayList<String> fbid = new ArrayList<String>();
        fbid.add("100002527741735");
        fbid.add("538761849");
        fbid.add("520588660");
        fbid.add("500431977");
        fbid.add("100003516975847");
        fbid.add("100002952444313");
        fbid.add("100000923580850");
        fbid.add("9890");
        fbid.add("1234");
        fbid.add("100002527741735");
        ArrayList<Feedbean> frendlist = new ArrayList<Feedbean>();
        for (int i = 0; i < feedlist.size(); i++) {
            String temp = feedlist.get(i).getFbid();
            for (int j = 0; j < fbid.size(); j++) {
                if (temp.equalsIgnoreCase(fbid.get(j))) {
                    feedlist.get(i).setIsmyfrend(true);
                    frendlist.add(feedlist.get(i));
                    feedlist.remove(i);
                }
            }
        }
    }
}
