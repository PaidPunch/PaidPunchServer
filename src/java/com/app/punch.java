package com.app;

import com.db.DataAccess;
import com.db.DataAccessControler;
import com.server.SAXParserExample;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xml.sax.InputSource;
import com.server.Constants;
import com.server.Utility;
import com.server.aczreqElements;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import org.apache.axis.encoding.Base64;
import java.io.File;
import java.io.IOException;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * @author qube26
 */
public class punch extends HttpServlet {

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
        ServletConfig config = null;
        Vector card_list = null;

        try {

            response.setContentType("text/html;charset=UTF-8");

            ServletContext context;
            String msg;

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

                if (reqtype.equalsIgnoreCase("SEARCHBYBUSINESSNAME-REQ")) {
                    Search_By_Busines_Name(list, response);
                }
                if (reqtype.equalsIgnoreCase("GETUSERPUCNHCARD-REQ")) {
                    Punch_Card_List(list, response);
                }
                if (reqtype.equalsIgnoreCase("MARKPUNCHUSED-REQ")) {
                    Mark_Punch_Card(list, response, request);
                }

            } catch (Exception e) {
                Punch_Card_List_XML_Response(card_list, "01", "Failure", response);
                Constants.logger.error(e);
            } finally {

                out.close();
            }

        } catch (Exception e) {
            Punch_Card_List_XML_Response(card_list, "01", "Failure", response);
            Constants.logger.error(e);
        } finally {
            out.close();
        }

    }// <editor-fold defaultstate="collapsed"
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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * 
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void Punch_Card_List(List list, HttpServletResponse response) throws ParseException {
        aczreqElements arz = (aczreqElements) list.get(0);
        String userid = arz.getUserId();

        String sessionid = arz.getSessionid();
        Vector card_list = null;
        sessionhandler session = new sessionhandler();
        boolean b = session.sessionidverify(userid, sessionid);
        if (!b) {
            Punch_Card_List_XML_Response(card_list, "400", "You have logged in from another device", response);
            return;
        }

        card_list = DataAccessControler.punchcardlist(userid);

        if (card_list == null) {
            Punch_Card_List_XML_Response(card_list, "01", "Failed to process request", response);
        } else {
            Punch_Card_List_XML_Response(card_list, "00", "Successful", response);
        }
    }

    private void Search_By_Busines_Name(List list, HttpServletResponse response) throws SQLException {
        aczreqElements arz = (aczreqElements) list.get(0);
        // String buss_name=arz.getBusiness_name();

        Vector card_list = null;
        String sessionid = arz.getSessionid();
        String userid = arz.getUserId();
        sessionhandler session = new sessionhandler();
        boolean b = session.sessionidverify(userid, sessionid);
        if (!b) {
            Search_By_JSON_Response(card_list, "400", "You have logged in from another device", response);
            return;
        }
        card_list = DataAccessControler.punches_search();

        if (card_list == null) {
            Search_By_JSON_Response(card_list, "01", "Failed to process request", response);
        } else {
            Search_By_JSON_Response(card_list, "00", "Successful", response);
        }
    }

    private void Punch_Card_List_XML_Response(Vector punchcard, String statusCode, String statusMessage,
            HttpServletResponse p_response) {
        PrintWriter out = null;
        try {
            out = p_response.getWriter();

            String res = "";
            // Constants.logger.info("Respones userid   " + userid);
            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);
            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");

            if (statusCode.equalsIgnoreCase("00")) {

                res = "<?xml version='1.0' ?>" + "<paidpunch-resp>" + "<statusCode>" + statusCode + "</statusCode>"
                        + "<statusMessage>" + statusMessage + "</statusMessage>"
                        + "<punchcardlist>";
                for (int i = 0; i < punchcard.size(); i++) {

                    String punchexipre = "false";
                    Vector card_list = (Vector) punchcard.elementAt(i);
                    String expiredate = card_list.elementAt(21).toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    Date tempexipredate = dateFormat.parse(expiredate);

                    String strDate = dateFormat.format(new java.util.Date());
                    java.util.Date l_scan_Date = dateFormat.parse(strDate);
                    if (Utility.isAfterDateTime(l_scan_Date, tempexipredate)) {
                        punchexipre = "true";
                    }
                    // java.util.Date date = new java.util.Date(((java.sql.Date) card_list.elementAt(21)).getTime());
                    //
                    // String expiredate = dateFormat.format(date);

                    int val1 = Integer.parseInt("" + card_list.elementAt(6));
                    float val2 = Float.parseFloat("" + card_list.elementAt(7));
                    byte[] l_imageData = null;
                    l_imageData = (byte[]) card_list.elementAt(2);
                    Base64 baseimage = new Base64();
                    baseimage.encode(l_imageData);
                    String ismystery = card_list.elementAt(18).toString();
                    int remainpunch;
                    int totalpunch;
                    boolean free_card_add_list = true;
                    // free punch card case.
                    if (("" + card_list.elementAt(19)).equalsIgnoreCase("Y")
                            || ("" + card_list.elementAt(19)).equalsIgnoreCase("true")) {
                        int temp = Integer.parseInt("" + card_list.elementAt(10));
                        if (temp == 0) {
                            // free punch used so no need to add in list
                            remainpunch = 1;
                            free_card_add_list = false;
                        } else {

                            remainpunch = 0;
                        }
                        ismystery = "false";
                        totalpunch = 1;
                    } else {
                        remainpunch = Integer.parseInt("" + card_list.elementAt(6))
                                - Integer.parseInt("" + card_list.elementAt(10));
                        totalpunch = Integer.parseInt("" + card_list.elementAt(6));
                    }

                    if (free_card_add_list) {
                        res = res + "<punchcard>"
                                + "<bussinessid>" + card_list.elementAt(0) + "</bussinessid>"
                                + "<bussinessname>" + StringEscapeUtils.escapeXml(card_list.elementAt(1).toString())
                                + "</bussinessname>"
                                + "<punchcardid>" + card_list.elementAt(4) + "</punchcardid>"
                                + "<punchcardname>" + StringEscapeUtils.escapeXml(card_list.elementAt(5).toString())
                                + "</punchcardname>"
                                + "<pucnchcarddesc>" + StringEscapeUtils.escapeXml(card_list.elementAt(11).toString())
                                + "</pucnchcarddesc>"
                                + "<totalnoofpunches>" + totalpunch + "</totalnoofpunches>"
                                + "<eachpunchvalue>" + card_list.elementAt(7) + "</eachpunchvalue>"
                                + "<actualprice>" + val1 * val2 + "</actualprice>"
                                + "<sellingprice>" + card_list.elementAt(8) + "</sellingprice>"
                                + "<discount>" + card_list.elementAt(9) + "</discount>"
                                + "<totalpunchesused>" + remainpunch + "</totalpunchesused>"
                                + "<punch_card_downloadid>" + card_list.elementAt(12) + "</punch_card_downloadid>"
                                + "<discount_value_of_each_punch>" + card_list.elementAt(13)
                                + "</discount_value_of_each_punch>"
                                + "<expiredate>" + expiredate + "</expiredate>"
                                + "<punchexpire>" + punchexipre + "</punchexpire>"
                                + "<isfreepunch>" + card_list.elementAt(17) + "</isfreepunch>"
                                + "<is_mystery_punch>" + ismystery + "</is_mystery_punch>";
                        if (card_list.elementAt(20) == null) {
                            res = res + "<mysteryid></mysteryid>";
                        } else {
                            res = res + "<mysteryid>" + card_list.elementAt(20) + "</mysteryid>";
                        }

                        res = res + "<mystery_punch_used>" + card_list.elementAt(22) + "</mystery_punch_used>";
                        // Constants.logger.info("response->" + res);
                        // res=res+ "<bussinesslogo>" + new String(baseimage.encode(l_imageData)) + "</bussinesslogo>"
                        res = res + "<bussinesslogo>" + card_list.elementAt(16) + "</bussinesslogo>"
                                + "</punchcard>";
                        Constants.logger.info("response->" + res);
                    }
                }
                res = res + "</punchcardlist></paidpunch-resp>";
            } else {
                res = "<?xml version='1.0' ?>" + "<paidpunch-resp>" + "<statusCode>" + statusCode + "</statusCode>"
                        + "<statusMessage>" + statusMessage + "</statusMessage>" + "</paidpunch-resp>";
                Constants.logger.info("response->" + res);
            }
            out.print(res);

        } catch (ParseException ex) {
            Logger.getLogger(punch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Constants.logger.error(ex);
        } finally {
            out.close();
        }

    }

    private void Mark_Punch_Card(List list, HttpServletResponse response, HttpServletRequest request) {
        aczreqElements arz = (aczreqElements) list.get(0);
        String barcodeimage = null;
        String barcodevalue = "";
        String userid = arz.getUserId();
        String pid = arz.getPunchCardID();
        String down_id = arz.getPunch_card_downloadid();
        String is_mystery_punch = arz.getIs_mystery_punch();
        String mysteryid = "", value_mystery_punch = "";
        String exitrestatus_in_app = "";
        exitrestatus_in_app = arz.getExpirestatus();
        if (exitrestatus_in_app.equalsIgnoreCase("") || exitrestatus_in_app.isEmpty())
        {
            exitrestatus_in_app = "false";
        }
        String expirestatus = "false";
        try {
            String sessionid = arz.getSessionid();
            sessionhandler session = new sessionhandler();
            boolean sessionverify = session.sessionidverify(userid, sessionid);
            if (!sessionverify) {
                mark_Punch_Card_Xml(response, "400", "You have logged in from another device", -1, pid, barcodeimage);
                return;
            }
            Vector userinfo = (Vector) DataAccessControler.getDataFromTable("punchcard_download",
                    "punch_card_downloadid", down_id).elementAt(0);

            Vector punchdata = (Vector) DataAccessControler.getDataFromTable("punch_card", "punch_card_id", pid)
                    .elementAt(0);
            String restrict_min = "" + punchdata.elementAt(10);

            String expiredate = userinfo.elementAt(9).toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            Date tempexipredate = dateFormat.parse(expiredate);

            String strDate = dateFormat.format(new java.util.Date());
            java.util.Date l_scan_Date = dateFormat.parse(strDate);
            if (Utility.isAfterDateTime(l_scan_Date, tempexipredate)) {
                expirestatus = "true";
            }
            if (!(exitrestatus_in_app.equalsIgnoreCase(expirestatus)))
            {
                mark_Punch_Card_Xml(response, "401", " Punch Card Expired", -1, pid, barcodeimage);
                return;
            }
            boolean b = timelimitcheck(down_id, restrict_min);
            if (!b) {
                mark_Punch_Card_Xml(response, "01", "You just used a Punch. You must wait " + restrict_min
                        + " minutes before using another.", -1, pid, barcodeimage);
                return;
            }

            Vector punch_info = (Vector) DataAccessControler.getDataFromTable("punch_card", "punch_card_id", pid)
                    .elementAt(0);
            if (punch_info != null) {
                Vector fields = new Vector();
                fields.add("app_user_id");
                fields.add("punch_card_id");
                Vector fields_value = new Vector();
                fields_value.add(userid);
                fields_value.add(pid);
                Vector set_fields = new Vector();
                set_fields.add("punch_used");

                Vector set_fields_value = new Vector();
                Vector basefields = new Vector();
                basefields.add("punch_card_downloadid");
                Date time1 = new Date();

                java.sql.Date sqlDate = new java.sql.Date(time1.getTime());
                java.sql.Time time = new java.sql.Time(time1.getTime());
                Vector tracker = new Vector();
                tracker.add(userid);
                tracker.add(pid);
                tracker.add(sqlDate);
                tracker.add(time);
                tracker.add(down_id);
                synchronized (this) {

                    int total_punch = Integer.parseInt("" + punch_info.elementAt(2));
                    int remain = Integer.parseInt("" + userinfo.elementAt(3));
                    if (!is_mystery_punch.equalsIgnoreCase("true")) {
                        remain = remain - 1;
                    }
                    if (is_mystery_punch.equalsIgnoreCase("true")) {
                        if (remain > 1) { // mystery puch not allow
                            mark_Punch_Card_Xml(response, "01", "Use all punch for unlock mystery punch.", -1, pid,
                                    barcodeimage);
                            return;
                        }
                    }
                    if (!is_mystery_punch.equalsIgnoreCase("true")) {
                        if (Integer.parseInt("" + userinfo.elementAt(3)) <= 0) {
                            mark_Punch_Card_Xml(response, "01", "You have used all punches.", -1, pid, barcodeimage);
                            return;
                        }
                    }
                    int totalused = total_punch - remain;
                    set_fields_value.add(remain);
                    if (!is_mystery_punch.equalsIgnoreCase("true")) {
                        int res = DataAccessControler.updatetDataToTable("punchcard_download", "punch_card_downloadid",
                                down_id, "punch_used", "" + remain);
                        if (res == -1) {
                            mark_Punch_Card_Xml(response, "01", " Failed to process request.", -1, pid, barcodeimage);
                            return;
                        }
                    }
                    // barcodevalue = createRandomInteger();
                    // String barcodevalue="12345";

                    // boolean brcode_check_table = DataAccessControler.getchecker("punch_card_tracker",
                    // "barcode_value", barcodevalue);
                    // while (brcode_check_table) {
                    // barcodevalue = createRandomInteger();
                    // / brcode_check_table = DataAccessControler.getchecker("punch_card_tracker", "barcode_value",
                    // barcodevalue);
                    // }
                    tracker.add(is_mystery_punch);
                    // if(is_mystery_punch.equalsIgnoreCase("true"))
                    // {
                    // tracker.add(mysteryid);
                    // }
                    if (is_mystery_punch.equalsIgnoreCase("true")) {
                        mysteryid = userinfo.elementAt(8).toString();
                    }
                    int res = DataAccessControler.insert_punchcard_tracker("punch_card_tracker", tracker,
                            is_mystery_punch);
                    DataAccess da = new DataAccess();

                    da.insert_user_feeds(pid, userid, "Punched", "F", null);

                    // barcodeimages
                    // String path = getServletContext().getRealPath("barcodeimages");
                    // path = path + Constants.filereader + barcodevalue + ".jpg";
                    // File barcodefile = new File(path);
                    // Code128 barcode = new Code128();

                    // try {
                    // barcodecreate(path, barcodevalue);
                    // // barcode.setData(barcodevalue);
                    // // barcode.setX(3);
                    // // barcode.setY(100);
                    // //barcode.setBarcodeHeight(100);
                    // //barcode.setBarcodeWidth(100);
                    // // barcode.drawBarcode(barcodevalue);
                    // //barcode.drawBarcode(path);
                    // // BitMatrix matrix= cw.encode(barcodevalue, BarcodeFormat.CODE_39, 281,239, null);
                    // // MatrixToImageWriter.writeToFile(matrix, "png",barcodefile);
                    // } catch (Exception e) {
                    // Constants.logger.error(e);
                    // }

                    // FileReader fr = new FileReader(barcodefile);

                    // int len = (int) barcodefile.length();
                    // byte[] filebyte = new byte[len];
                    // int index = 0;
                    // int byt;
                    // // byte[] bytes = new byte[size];
                    // DataInputStream dis = new DataInputStream(new FileInputStream(barcodefile));
                    // int read = 0;
                    // int numRead = 0;
                    // while (read < filebyte.length && (numRead = dis.read(filebyte, read,
                    // filebyte.length - read)) >= 0) {
                    // read = read + numRead;
                    // }

                    // try {
                    // while ((byt = fr.read()) != -1) {
                    // filebyte[index] = (byte) byt;
                    // index++;
                    // }
                    // } catch (Exception e) {
                    // Constants.logger.error(e);
                    // }
                    // try{
                    // File f = new File("c://temp1");
                    // FileOutputStream fw = new FileOutputStream(f);
                    // for (int inde = 0; inde <len; inde++) {
                    // fw.write(filebyte[inde]);
                    // }
                    // fw.close();
                    // }
                    //
                    // catch(Exception e)
                    // {
                    // System.out.print(e.toString());
                    // }

                    Base64 baseimage = new Base64();
                    // barcodeimage = new String(baseimage.encode(filebyte));
                    if (res == -1) {
                        mark_Punch_Card_Xml(response, "01", " Failed to process request.", -1, pid, barcodeimage);
                        return;
                    }
                    if (expirestatus.equalsIgnoreCase("false")) {
                        if (userinfo.elementAt(7).toString().equalsIgnoreCase("true")) {
                            mark_Punch_Card_Xml(response, "00", "Punch used successfully", 1, pid, barcodeimage);// for
                                                                                                                 // free
                                                                                                                 // punch
                        } else {
                            mark_Punch_Card_Xml(response, "00", "Punch used successfully", totalused, pid, barcodeimage);
                        }
                    } else {
                        mark_Punch_Card_Xml(response, "03", "Punch used successfully", totalused, pid, barcodeimage);
                    }
                    return;
                }
            } else {
                mark_Punch_Card_Xml(response, "01", " Failed to process request.", -1, pid, barcodeimage);
                return;
            }
        } catch (Exception ex) {
            mark_Punch_Card_Xml(response, "01", " Failed to process request.", -1, pid, barcodeimage);
            Constants.logger.error(ex);
        }
    }

    private void mark_Punch_Card_Xml(HttpServletResponse response, String statusCode, String statusMessage,
            int totalused, String pid, String barcodeimage) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String res = "";
            res = res + "<?xml version='1.0' ?>"
                    + "<paidpunch-resp>";
            if (statusCode.equalsIgnoreCase("00") || statusCode.equalsIgnoreCase("03")) {
                res = res + "<statusCode>" + statusCode + "</statusCode>"
                        + "<statusMessage>" + statusMessage + "</statusMessage>"
                        + " <punchcardid>" + pid + "</punchcardid>"
                        + "<totalpunchesused>" + totalused + "</totalpunchesused>"
                        // + "<mystery_punch_value>"+mystrypunch+"</mystery_punch_value>"
                        + "<barcodevalue></barcodevalue>";

                res = res + "<barcodeimage></barcodeimage>"
                        + "</paidpunch-resp>";
            } else {
                res = res + "<statusCode>" + statusCode + "</statusCode>"
                        + "<statusMessage>" + statusMessage + "</statusMessage>"
                        + "</paidpunch-resp>";
                // Constants.logger.info(res);

            }
            Constants.logger.info(res);
            out.print(res);
        } catch (Exception ex) {
            Constants.logger.error(ex);
        }

    }

    private void Search_By_XML_Response(Vector punchcard, String statusCode, String statusMessage,
            HttpServletResponse p_response) {

        PrintWriter out = null;
        try {
            out = p_response.getWriter();

            String res = "";
            // Constants.logger.info("Respones userid   " + userid);
            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);
            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            if (statusCode.equalsIgnoreCase("01")) {
                res = "<?xml version='1.0' ?>" + "<paidpunch-resp>" + "<statusCode>" + statusCode + "</statusCode>"
                        + "<statusMessage>" + statusMessage + "</statusMessage>" + "</paidpunch-resp>";
            } else {
                res = "<?xml version='1.0' ?>" + "<paidpunch-resp>" + "<statusCode>" + statusCode + "</statusCode>"
                        + "<statusMessage>" + statusMessage + "</statusMessage>"
                        + "<punchcardlist>";
                for (int i = 0; i < punchcard.size(); i++) {
                    Vector card_list = (Vector) punchcard.elementAt(i);
                    int val1 = Integer.parseInt("" + card_list.elementAt(6));
                    int val2 = Integer.parseInt("" + card_list.elementAt(7));
                    byte[] l_imageData = null;
                    l_imageData = (byte[]) card_list.elementAt(2);
                    Base64 baseimage = new Base64();
                    baseimage.encode(l_imageData);
                    // int remainpunch = Integer.parseInt("" + card_list.elementAt(6)) - Integer.parseInt("" +
                    // card_list.elementAt(10));
                    res = res + "<punchcard>"
                            + "<bussinessid>" + card_list.elementAt(0) + "</bussinessid>"
                            + "<bussinessname>" + card_list.elementAt(1) + "</bussinessname>"
                            + "<bussinesslogo>" + new String(baseimage.encode(l_imageData)) + "</bussinesslogo>"
                            + "<punchcardid>" + card_list.elementAt(4) + "</punchcardid>"
                            + "<punchcardname>" + card_list.elementAt(5) + "</punchcardname>"
                            + "<pucnchcarddesc>" + card_list.elementAt(10) + "</pucnchcarddesc>"
                            + "<totalnoofpunches>" + card_list.elementAt(6) + "</totalnoofpunches>"
                            + "<eachpunchvalue>" + card_list.elementAt(7) + "</eachpunchvalue>"
                            + "<actualprice>" + val1 * val2 + "</actualprice>"
                            + "<sellingprice>" + card_list.elementAt(8) + "</sellingprice>"
                            + "<discount>" + card_list.elementAt(9) + "</discount>"
                            // + "<totalpunchesused>" + remainpunch + "</totalpunchesused>"
                            // + "<punch_card_downloadid>" + card_list.elementAt(12) + "</punch_card_downloadid>"
                            + "</punchcard>";

                }
                res = res + "</punchcardlist></paidpunch-resp>";
            }
            out.print(res);

            Constants.logger.info("response->" + res);
        } catch (IOException ex) {
            Constants.logger.error(ex);
        } finally {
            out.close();
        }

    }

    private void Search_By_JSON_Response(Vector res, String code, String message, HttpServletResponse p_response) {
        PrintWriter out;

        try {
            out = p_response.getWriter();
            JSONObject object = new JSONObject();

            p_response.setHeader("Content-Disposition", "attachement; filename= response.json");
            String jsonres = "{\"statusCode\":\"" + code + " \",\"statusMessage\":\"" + message + "\"";
            object.put("statusCode", code);
            object.put("statusMessage", message);
            JSONArray arrayObj = new JSONArray();

            if (code.equalsIgnoreCase("00")) {

                for (int j = 0; j < res.size(); j++) {
                    JSONObject subnode = new JSONObject();
                    Vector data = (Vector) res.elementAt(j);
                    String bid = "" + data.elementAt(0);
                    String bname = "" + data.elementAt(1);
                    String qrcode = "" + data.elementAt(2);
                    subnode.put("bussinessid", bid);
                    subnode.put("bussinessname", bname);
                    subnode.put("qrcode", qrcode);
                    subnode.put("latitude", data.elementAt(3));
                    subnode.put("longitude", data.elementAt(4));
                    subnode.put("category", data.elementAt(5));
                    subnode.put("modification_time", data.elementAt(6));
                    subnode.put("time", data.elementAt(6).toString());
                    subnode.put("city", data.elementAt(7));
                    subnode.put("state", data.elementAt(8));
                    subnode.put("country", data.elementAt(9));
                    subnode.put("pincode", data.elementAt(10).toString());
                    subnode.put("address",
                            data.elementAt(11).toString() + " " + data.elementAt(7) + " " + data.elementAt(8) + " "
                                    + data.elementAt(9) + " " + data.elementAt(10).toString());
                    arrayObj.add(subnode);
                }
                object.put("paidpunch", arrayObj);
            }
            out.print(object.toString());
            Constants.logger.info("RESPonse" + object.toString());
        } catch (Exception e) {
            Constants.logger.info(e.toString());
        }
    }

    private boolean timelimitcheck(String down_id, String mint) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean result = true;
        String strDate = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String strTime = timeFormat.format(new Date());
        try {
            java.util.Date l_scan_Date = dateFormat.parse(strDate);
            java.util.Date l_scan_Time = timeFormat.parse(strTime);
            int min = Integer.parseInt("-" + mint);
            // min=0-min;

            java.util.Date befor_time = Utility.HoureAdd(l_scan_Time, min);
            strTime = befor_time.toString();
            // l_scan_Time = timeFormat.parse(strTime);
            java.sql.Time time = new java.sql.Time(befor_time.getTime());
            java.sql.Date Date = new java.sql.Date(l_scan_Date.getTime());
            strTime = time.toString();
            strDate = Date.toString();
            result = DataAccessControler.getData_punch_card_tracker(down_id, strTime, strDate);

        } catch (SQLException ex) {
            Constants.logger.error(ex);
        }
        return (result);
    }

    public static String createRandomInteger() {
        Long aStart = 100000l;
        Long aEnd = 999999l;
        Random aRandom = new Random();
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        // get the range, casting to long to avoid overflow problems
        long range = aEnd - (long) aStart + 1;
        // System.out.println("range>>>>>>>>>>>" + range);
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        // System.out.println("fraction>>>>>>>>>>>>>>>>>>>>" + fraction);
        long randomNumber = fraction + (long) aStart;
        System.out.println("Generated : " + randomNumber + " length " + ("" + randomNumber).length());
        return "" + randomNumber;
    }

    public void barcodecreate(String path, String barcodevalue) {
        try {
            // Create the barcode bean
            Code128Bean bean = new Code128Bean();

            final int dpi = 150;
            // Configure the barcode generator
            bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); // makes the narrow

            // bar
            // width exactly
            // one pixel202798

            bean.setFontSize(6);

            bean.setHeight(40);
            bean.setModuleWidth(0.9);
            // Open output file
            File outputFile = new File(path);
            FileOutputStream out = new FileOutputStream(outputFile);
            try {
                // Set up the canvas provider for monochrome JPEG output
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(out,
                        "image/jpeg", dpi, BufferedImage.TYPE_BYTE_BINARY,
                        true, 0);

                // Generate the barcode
                bean.generateBarcode(canvas, barcodevalue);
                // Signal end of generation
                canvas.finish();
            } finally {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}