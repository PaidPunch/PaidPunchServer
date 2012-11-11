package com.jspservlets;

import com.jspservlets.ImageCreator;
import com.mysql.jdbc.Statement;
import com.server.Constants;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import javax.servlet.ServletConfig;
import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

/**
 * @author Shahid
 */
public class SignupCheckEmail extends HttpServlet {

    ServletConfig config = null;
    ServletContext context;
    HttpSession session = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here out.println("<html>"); out.println("<head>");
             * out.println("<title>Servlet signup_business_emailcheck</title>"); out.println("</head>");
             * out.println("<body>"); out.println("<h1>Servlet signup_business_emailcheck at " + request.getContextPath
             * () + "</h1>"); out.println("</body>"); out.println("</html>");
             */
        } finally {
            out.close();
        }
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {

    }

    // <editor-fold defaultstate="collapsed"
    // desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            config = getServletConfig();

            context = config.getServletContext();
        } catch (Exception e) {
            com.server.Constants.logger.error(e.getMessage());
        }
        com.server.Constants.loadJDBCConstants(context);
        String code = "00";
        String email_id = "", business_name = "";
        try {
            email_id = request.getParameter("email");
            business_name = request.getParameter("businessname");
            business_name = StringUtils.replace(business_name, "'", "\\'");
            // business_name = StringUtils.replace(business_name, "'", "\\'");
            // business_name.replaceAll(Pattern.quote("'"), "\'");
            // business_name.replaceAll("\'", "\\'");
            System.out.println("Business Name After Replacing" + business_name);
        } catch (Exception e) {
            com.server.Constants.logger.error("Error in signupbus_emailcheck.java while checking business"
                    + e.getMessage());
        }

        PrintWriter out = response.getWriter();
        if (request.getParameter("email") != null && request.getParameter("businessname") != null) {
            code = getEmailBusiness(email_id, business_name);
        }

        /*
         * The value to be returned for code is between the following 3: "00" : Email id and Business not registered
         * before "01" : Email id has already been registered "03" : Business Name has already been registered by that
         * name
         */
        out.println(code);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // processRequest(request, response);
        int maxFileSize = 5000 * 1024;
        int maxMemSize = 5000 * 1024;
        String logofolderPath = "", actualFolderPath = "";
        // logofolderPath = request.getRealPath("uploaded_logo_images");
        // logofolderPath="/usr/share/apache-tomcat-7.0.19/webapps/paid_punch/uploaded_logo_images";
        logofolderPath = com.server.Constants.LOGO_TEMP_PATH;
        actualFolderPath = com.server.Constants.logoActualPath;
        session = request.getSession(false);
        String path = "", businessname = "", businessdesc = "", email = "", password = "", datafile = "", contactname = "";
        String businessaddress = "", city = "", state = "", pincode = "", countrycode = "", contactnumber = "", latitude = "", longitude = "";
        PrintWriter out = response.getWriter();
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            com.server.Constants.logger.info("File Not Uploaded");

        } else {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // maximum size that will be stored in memory
            factory.setSizeThreshold(maxMemSize);
            // Location to save data that is larger than maxMemSize.
            // factory.setRepository(new File("c:\\temp"));
            factory.setRepository(new File("\\var\\tomcat7\\temp"));
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(maxFileSize);
            List items = null;

            try {
                items = upload.parseRequest(request);
                // com.server.Constants.logger.info("items: " + items);

            } catch (FileUploadException e) {
                e.printStackTrace();
            }
            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (item.isFormField()) {
                    String fieldname = item.getFieldName();
                    com.server.Constants.logger.info("name: " + fieldname);
                    String fieldvalue = item.getString();
                    com.server.Constants.logger.info("value: " + fieldvalue);

                    if (fieldname.equalsIgnoreCase("email")) {
                        email = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("password")) {
                        password = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("businessname")) {
                        businessname = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("businessdesc")) {
                        businessdesc = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("businessaddress")) {
                        businessaddress = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("city")) {
                        city = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("state")) {
                        state = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("pincode")) {
                        pincode = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("countrycode")) {
                        countrycode = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("latitude")) {
                        latitude = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("longitude")) {
                        longitude = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("contactname")) {
                        contactname = fieldvalue;
                    } else if (fieldname.equalsIgnoreCase("contactnumber")) {
                        contactnumber = fieldvalue;
                    }

                } else {
                    try {
                        String itemName = item.getName();
                        if (itemName == null || itemName == "") {
                            // path = "C:\\Documents and Settings\\admin\\My Documents\\Paid Punch\\uploaded files\\" +
                            // "images\\paidpunchlogo.png";
                            path = logofolderPath + "paidpunchlogo.png";
                            com.server.Constants.logger.info("path : " + path);
                        }
                        else {
                            Random generator = new Random();
                            int r = Math.abs(generator.nextInt());

                            String reg = "[.*]";
                            String replacingtext = "";
                            com.server.Constants.logger.info("Text before replacing is:-" + itemName);
                            Pattern pattern = Pattern.compile(reg);
                            Matcher matcher = pattern.matcher(itemName);
                            StringBuffer buffer = new StringBuffer();

                            while (matcher.find()) {
                                matcher.appendReplacement(buffer, replacingtext);
                            }
                            int IndexOf = itemName.indexOf(".");
                            String domainName = itemName.substring(IndexOf);
                            com.server.Constants.logger.info("domainName: " + domainName);
                            String imageb4removingspace = buffer.toString();
                            imageb4removingspace = imageb4removingspace.replaceAll(" ", "");

                            // String finalimage = buffer.toString() + "_" + r + domainName;
                            String finalimage = imageb4removingspace + "_" + r + domainName;

                            // String finalimage = businessname;

                            com.server.Constants.logger.info("Final Image===" + finalimage);
                            actualFolderPath = actualFolderPath + finalimage;
                            com.server.Constants.logger.info("Image Path ===" + actualFolderPath);
                            File savedFile = new File(logofolderPath + finalimage);
                            com.server.Constants.logger.info("Issue with path" + logofolderPath + finalimage);
                            item.write(savedFile);
                            com.server.Constants.logger.info("Coming Here");
                            path = savedFile.getPath();

                            long sizeofuploadedimage = savedFile.length();
                            if (sizeofuploadedimage > 10000) {
                                compress_file(savedFile, finalimage, path);
                            }

                            long sizeofuploadedimage1 = savedFile.length();

                            com.server.Constants.logger.info("********************FIle Uploaded Path" + path);
                            com.server.Constants.logger.info("********************Actual Uploaded Path"
                                    + actualFolderPath);

                        }

                    } catch (Exception e) {
                        com.server.Constants.logger.error("Error while uploading Logo Image : "
                                + e.getMessage().toString());
                    }
                } // else
            } // while(itr)

            session.setAttribute("businessname", businessname);
            session.setAttribute("businessdesc", businessdesc);
            session.setAttribute("email", email);
            session.setAttribute("password", password);
            session.setAttribute("filepath", path);
            session.setAttribute("businessaddress", businessaddress);
            session.setAttribute("city", city);
            session.setAttribute("state", state);
            session.setAttribute("pincode", pincode);
            session.setAttribute("countrycode", countrycode);
            session.setAttribute("contactnumber", contactnumber);
            session.setAttribute("contactname", contactname);
            session.setAttribute("actuallogopath", actualFolderPath);
            session.setAttribute("latitude", latitude);
            session.setAttribute("longitude", longitude);
            com.server.Constants.logger.info(businessname + "\n" + businessdesc + "\n" + email + "\n" + password + "\n"
                    + path + "\n" + businessaddress + "\n" + city + "\n" + countrycode + "\n" + contactname + "\n"
                    + contactnumber + "\n" + actualFolderPath + "\n" + latitude + "\n" + longitude);
            // System.out.println(businessname+"\n"+businessdesc+"\n"+email+"\n"+password+"\n"+path+"\n"+businessaddress+"\n"+city+"\n"+countrycode+"\n"+contactname+"\n"+contactnumber+"\n"+actualFolderPath+"\n"+latitude+"\n"+longitude);
        } // else(!multipart)

        response.sendRedirect("signup_paidpunch.jsp");
    } // doPost

    /**
     * Returns a short description of the servlet.
     * 
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public void compress_file(File newFile, String ImageString, String imagePath) throws FileNotFoundException,
            IOException {

        int size = (int) newFile.length();

        byte[] bytes = new byte[size];
        DataInputStream dis = new DataInputStream(new FileInputStream(newFile));
        int read = 0;
        int numRead = 0;
        while (read < bytes.length && (numRead = dis.read(bytes, read, bytes.length - read)) >= 0) {
            read = read + numRead;
        }

        ImageCreator imgcr = new ImageCreator();
        byte[] returnedbytes = imgcr.getResizeImage(bytes, 300, 300);
        try
        {
            FileOutputStream fos = new FileOutputStream(imagePath);
            // String strContent = "Write File using Java FileOutputStream example !";

            /*
             * To write byte array to a file, use void write(byte[] bArray) method of Java FileOutputStream class.
             * 
             * This method writes given byte array to a file.
             */

            fos.write(returnedbytes);

            /*
             * Close FileOutputStream using, void close() method of Java FileOutputStream class.
             */

            fos.close();

        } catch (FileNotFoundException ex) {
            com.server.Constants.logger.error("File Not Found Exception : " + ex.getMessage().toString());
        } catch (IOException ioe) {
            com.server.Constants.logger.error("IOException : " + ioe.getMessage().toString());
        } catch (Exception e) {
            com.server.Constants.logger.error("Exception : " + e.getMessage().toString());
        }
    }

    public String getEmailBusiness(String emailid, String businessname) throws ServletException {// ,String
                                                                                                 // businessname) throws
                                                                                                 // ServletException{
        DBConnection db = null;
        Statement stmt = null;
        ResultSet rs = null;
        String status = "00";

        try {
            db = new DBConnection();
            stmt = db.stmt;
            String query1 = "SELECT * from business_users where email_id ='" + emailid + "'";
            rs = stmt.executeQuery(query1);
            com.server.Constants.logger.info("The select query is " + query1);
            // displaying records
            if (rs.next()) {
                status = "01";
            }
            else {
                String query2 = "SELECT * from business_users where business_name ='" + businessname + "'";

                rs = stmt.executeQuery(query2);
                com.server.Constants.logger.info("The select query is " + query2);
                if (rs.next()) {
                    status = "02";
                }
            }
        } catch (SQLException e) {
            com.server.Constants.logger.error("Error in Sql in signup_business_emailcheck.java in getEmailBusiness "
                    + e.getMessage());
            status = "02";
            throw new ServletException("SQL Exception.", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    // Constants.logger.info("Closing rs Statement ");
                    rs = null;
                }
                db.closeConnection();

            } catch (SQLException e) {
                com.server.Constants.logger.error("Error in closing SQL in signup_business_emailcheck.java"
                        + e.getMessage());
            }
        }
        return status;
    }

}