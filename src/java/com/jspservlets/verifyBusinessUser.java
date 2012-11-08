package com.jspservlets;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.net.MalformedURLException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.Color;
import org.apache.commons.codec.binary.Base64;

/**
 * @author Shahid
 */
public class verifyBusinessUser extends HttpServlet {

    ServletConfig config = null;
    ServletContext context;
    DBConnection db;
    Connection conn = null;
    PreparedStatement pstmt = null;
    Statement st = null;
    ResultSet rs = null;
    String RESULT = "";
    String RESULT1 = "";
    String images_path = "";
    int no_of_punches_per_card = 0;
    float discountvalue = 0, value_of_each_punch = 0;;
    int disc = 0;
    // String acutalvalue = "" + 7;
    // String discountvalue = "" + 10;
    float sp_of_PunchCard = 0;
    float cost_price = 0;
    // String orange_qrcode_value = "";
    String flyerqrCode = "";
    String bussname = "";
    Document document = null;
    String send = "";
    PdfWriter writer = null;

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
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            config = getServletConfig();
            context = config.getServletContext();

        } catch (Exception e) {

        }
        com.server.Constants.loadJDBCConstants(context);

        String bususerid = "", email_id = "";

        PrintWriter out = response.getWriter();
        try {
            bususerid = request.getParameter("uid");
            // bususerid = "2";
            email_id = request.getParameter("email");
            // email_id = "shahid1311@gmail.com";

            com.server.Constants.logger.info("The Business User with UserId=" + bususerid + " and Email=" + email_id);

            getBusinessInfo(bususerid);
            RESULT = com.server.Constants.Pdf_Write_Path + bussname + "_flyer.pdf";
            com.server.Constants.logger.info("Pdf_Path : " + RESULT);
            // RESULT1 = com.server.Constants.Pdf_Write_Path+bussname+"_orangecode.pdf";
            images_path = com.server.Constants.imagePath;
            com.server.Constants.logger.info("Images Path for pdf : " + images_path);
            try {

                // createFlyerPdf(RESULT);
                // createOrangeQRPdf(RESULT1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            boolean flag = userVerified(bususerid, email_id);
            if (flag) {
                response.sendRedirect("login.jsp");
            }
            else {
                out.println("User could not be Validated");
            }
        } finally {
            out.close();
        }
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
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(verifyBusinessUser.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(verifyBusinessUser.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void getBusinessInfo(String bussiness_id) throws ServletException, UnsupportedEncodingException {
        ResultSet rs = null;
        try {
            db = new DBConnection();
            st = db.stmt;
            String query = "Select business_name from business_users where business_userid =" + bussiness_id;
            com.server.Constants.logger.info("The query is " + query);
            System.out.println("The query is " + query);
            rs = st.executeQuery(query);
            if (rs.next()) {
                bussname = rs.getString(1);
            }

            String query1 = "Select * from punch_card where business_userid =" + bussiness_id;
            com.server.Constants.logger.info("The query is " + query1);
            System.out.println("The query is " + query1);
            rs = st.executeQuery(query1);
            if (rs.next()) {
                no_of_punches_per_card = rs.getInt("no_of_punches_per_card");
                value_of_each_punch = rs.getFloat("value_of_each_punch");
                sp_of_PunchCard = rs.getFloat("selling_price_of_punch_card");
                discountvalue = rs.getFloat("effective_discount");

                flyerqrCode = rs.getString("qrcode");
            }

            send = URLEncoder.encode(flyerqrCode, "UTF-8");

            cost_price = no_of_punches_per_card * value_of_each_punch;
            disc = (int) discountvalue;

        } catch (SQLException sqle) {

            com.server.Constants.logger.error("Error in Sql" + sqle.getMessage());
            throw new ServletException("SQL Exception.", sqle);
        } finally {
            try {
                if (st != null) {
                    st.close();
                    // Constants.logger.info("Closing  Statement ");
                    pstmt = null;
                }

                if (rs != null) {
                    rs.close();
                    com.server.Constants.logger.info("Closing rs Statement ");
                    rs = null;
                }
                db.closeConnection();
            } catch (SQLException e) {
                com.server.Constants.logger.error("Error in Sql" + e.getMessage());
            }
        }

    }

    public boolean userVerified(String bususerid, String email_id) throws SQLException, ServletException {
        // String INSERT_RECORD = "Update into app_distributor_hits(download_available) values(?)";
        boolean fl = true;
        try {
            db = new DBConnection();
            st = db.stmt;
            String query = "Update business_users set isemailverified = 'Y' where business_userid =" + bususerid
                    + " and email_id='" + email_id + "'";
            com.server.Constants.logger.info("The query is " + query);
            System.out.println("The query is " + query);
            if (st.executeUpdate(query) == 0)
                fl = false;
            else
                fl = true;

        } catch (SQLException sqle) {
            fl = false;
            com.server.Constants.logger.error("Error in Sql" + sqle.getMessage());
            throw new ServletException("SQL Exception.", sqle);
        } finally {
            try {
                if (st != null) {
                    st.close();
                    // Constants.logger.info("Closing  Statement ");
                    pstmt = null;
                }

                /*
                 * if(rs != null) { rs.close(); Constants.logger.info("Closing rs Statement "); rs = null; }
                 */
                db.closeConnection();
            } catch (SQLException e) {
                com.server.Constants.logger.error("Error in Sql" + e.getMessage());
            }
        }
        return fl;
    }

    /**
     * Creates a PDF document.
     * 
     * @param filename
     *            the path to the new PDF document
     * @param locale
     *            Locale in case you want to create a Calendar in another language
     * @param year
     *            the year for which you want to make a calendar
     * @throws DocumentException
     * @throws IOException
     */

    public void createFlyerPdf(String filename) throws IOException, DocumentException {
        // step 1
        Rectangle pageSize = new Rectangle(700, 470);
        document = new Document(pageSize);
        // step 2
        writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        PdfPTable table;

        PdfContentByte canvas = writer.getDirectContent();

        // draw the background
        drawImageFlyer(canvas);
        // create a table with 1 columns
        table = new PdfPTable(1);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.setTotalWidth(500);
        table.addCell(getBusinessCell());
        // complete the table
        table.completeRow();
        // write the table to an absolute position
        table.writeSelectedRows(0, -1, 170, 460, canvas);

        table = new PdfPTable(6);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.setTotalWidth(680);

        float headerwidths[] = { 55f, 170f, 55f, 160f, 55f, 165f };
        table.setWidths(headerwidths);
        // table.setWidthPercentage(100);

        // add the name of the month
        // table.getDefaultCell().setBackgroundColor(Color.WHITE);
        String blankcell = " ";
        table.addCell(getDataCell(blankcell));

        String text_one = "Download the PaidPunch App and scan this QR code";
        table.addCell(getDataCell(text_one));

        table.addCell(getDataCell(blankcell));

        String text_two = "Pay the Cashier $" + sp_of_PunchCard + " and receive " + no_of_punches_per_card + " $"
                + value_of_each_punch + " vouchers of store credit ($" + cost_price + " value)";
        table.addCell(getDataCell(text_two));

        table.addCell(getDataCell(blankcell));
        String text_three = "Every time you visit us, open the PaidPunch app and use a $" + value_of_each_punch
                + " PaidPunch towards your purchase";
        table.addCell(getDataCell(text_three));

        table.completeRow();
        // write the table to an absolute position
        table.writeSelectedRows(0, -1, 0, 300, canvas);

        table = new PdfPTable(1);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.setTotalWidth(50);
        // int headerwidths[] = {4, 30,4, 30,4,30 };
        // table.setWidths(headerwidths);
        // table.setWidthPercentage(100);

        // add the name of the month
        String off_text = "$" + value_of_each_punch + "\nOFF";
        table.addCell(getOffCell(off_text));
        // complete the table
        table.completeRow();
        // write the table to an absolute position
        table.writeSelectedRows(0, -1, 520, 160, canvas);

        document.newPage();
        // }
        // step 5
        document.close();
    }

    public void createOrangeQRPdf(String filename) throws IOException, DocumentException {
        // step 1
        Rectangle pageSize = new Rectangle(450, 320);
        document = new Document(pageSize);
        // step 2
        writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT1));
        // step 3
        document.open();
        // step 4
        PdfPTable table;

        PdfContentByte canvas = writer.getDirectContent();

        // draw the background
        drawImageOrangePdf(canvas);
        // create a table with 1 columns
        table = new PdfPTable(1);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.setTotalWidth(450);
        String attn = "ATTENTION : Keep this QR code hidden and allow the customer to scan it only after receiving payment.\n Scanning this code will issue a PaidPunch Card to the customer.";
        table.addCell(getAttentionText(attn));

        // complete the table
        table.completeRow();
        // write the table to an absolute position
        table.writeSelectedRows(0, -1, 0, 40, canvas);

        table = new PdfPTable(1);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.setTotalWidth(450);
        String busname = "Issuing QR Code for " + bussname;
        table.addCell(getIssuingText(busname));

        // complete the table
        table.completeRow();
        // write the table to an absolute position
        table.writeSelectedRows(0, -1, 40, 325, canvas);

        document.newPage();
        // }
        // step 5
        document.close();
    }

    /**
     * Draws the image of the month to the calendar.
     * 
     * @param canvas
     *            the direct content layer
     * @param calendar
     *            the month (to know which picture to use)
     * @throws DocumentException
     * @throws IOException
     */
    public void drawImageFlyer(PdfContentByte canvas) throws DocumentException, IOException {
        // get the image
        // Image img =
        // Image.getInstance(String.format("C:\\Documents and Settings\\admin\\My Documents\\NetBeansProjects\\trial\\src\\java\\com\\trial\\flyer_bg.png"));
        Image img = Image.getInstance(String.format(images_path + "flyer_bg12.png"));
        img.scalePercent(110);
        // img.scaleToFit(650, 500);
        // img.setAbsolutePosition((PageSize.A4.getHeight() - img.getScaledWidth()) / 2, (PageSize.A4.getWidth() -
        // img.getScaledHeight()) / 2);
        img.setAbsolutePosition(0, 0);
        canvas.addImage(img);
        // canvas.saveState();
        Image img1 = Image.getInstance(images_path + "flyer_logo.JPG");
        // img1.scaleToFit(100,100);
        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(40, 370);
        canvas.addImage(img1);

        img1 = Image.getInstance(images_path + "mb_save.png");
        img1.scaleToFit(180, 180);
        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(470, 50);
        canvas.addImage(img1);

        img1 = Image.getInstance(images_path + "flyer_card_combined.png");
        img1.scaleToFit(160, 110);
        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(280, 90);
        canvas.addImage(img1);

        img1 = Image.getInstance(images_path + "one.jpg");
        // img1.scaleToFit(33,33);
        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(18, 260);
        canvas.addImage(img1);

        img1 = Image.getInstance(images_path + "two.jpg");
        // img1.scaleToFit(35,35);
        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(250, 260);
        canvas.addImage(img1);

        img1 = Image.getInstance(images_path + "three.jpg");
        // img1.scaleToFit(35,35);
        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(468, 260);
        canvas.addImage(img1);
        // add metadata\
        // canvas.saveState();
        // canvas.restoreState();

        img1 = Image.getInstance("http://api.qrserver.com/v1/create-qr-code/?data=" + send + "&amp;size=100x100");
        img1.scaleToFit(180, 180);
        // img1.scalePercent(100);
        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(50, 30);
        canvas.addImage(img1);

    }

    /**
     * Creates a PdfPCell with the name of the month
     * 
     * @param calendar
     *            a date
     * @param locale
     *            a locale
     * @return a PdfPCell with rowspan 7, containing the name of the month
     */
    public PdfPCell getBusinessCell() throws BadElementException, MalformedURLException, IOException, DocumentException {
        PdfPCell cell = new PdfPCell();

        cell.setBorder(Rectangle.NO_BORDER);
        cell.setUseDescender(true);

        FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, new Color(244, 123, 39));

        Paragraph title11 = new Paragraph("Save ", FontFactory.getFont(FontFactory.HELVETICA, 32, Font.BOLD, new Color(
                244, 123, 39)));
        title11.setAlignment("right");

        title11.add(new Chunk(disc + "%", FontFactory.getFont(FontFactory.HELVETICA, 32, Font.NORMAL, new Color(129,
                130, 133))));

        title11.add(new Chunk(" at ", FontFactory.getFont(FontFactory.HELVETICA, 32, Font.NORMAL, new Color(244, 123,
                39))));

        title11.add(new Chunk(bussname, FontFactory.getFont(FontFactory.HELVETICA, 32, Font.NORMAL, new Color(129, 130,
                133))));
        cell.addElement(title11);

        Paragraph line2 = new Paragraph("",
                FontFactory.getFont(FontFactory.HELVETICA, 32, Font.NORMAL, new Color(244, 123, 39)));
        line2.setAlignment("right");

        line2.add(new Chunk(" with", FontFactory.getFont(FontFactory.HELVETICA, 32, Font.NORMAL,
                new Color(244, 123, 39))));
        line2.add(new Chunk(" Paid", FontFactory.getFont(FontFactory.HELVETICA, 32, Font.NORMAL, new Color(129, 130,
                133))));
        line2.add(new Chunk("Punch", FontFactory.getFont(FontFactory.HELVETICA, 32, Font.NORMAL,
                new Color(244, 123, 39))));

        cell.addElement(line2);
        return cell;
    }

    /**
     * Creates a PdfPCell for a specific day
     * 
     * @param calendar
     *            a date
     * @param locale
     *            a locale
     * @return a PdfPCell
     */

    public PdfPCell getDataCell(String cell_text) throws BadElementException, MalformedURLException, IOException,
            DocumentException {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setUseDescender(true);
        Paragraph title11 = new Paragraph(cell_text, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL,
                new Color(90, 90, 90)));
        title11.setAlignment("left");
        cell.addElement(title11);
        return cell;
    }

    public PdfPCell getOffCell(String cell_text) throws BadElementException, MalformedURLException, IOException,
            DocumentException {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setUseDescender(true);
        // Paragraph title11 = new Paragraph(cell_text, FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, new
        // Color(244, 123, 39)));

        Paragraph title11 = new Paragraph(cell_text, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,
                new Color(244, 123, 39)));
        title11.setAlignment("center");

        // title11.add(new Chunk("\nOff",FontFactory.getFont(FontFactory.HELVETICA, 18, Font.NORMAL, new Color(244, 123,
        // 39))));
        cell.addElement(title11);
        return cell;
    }

    public void drawImageOrangePdf(PdfContentByte canvas) throws DocumentException, IOException {
        // get the image
        // Image img =
        // Image.getInstance(String.format("C:\\Documents and Settings\\admin\\My Documents\\NetBeansProjects\\trial\\src\\java\\com\\trial\\flyer_bg.png"));
        Image img = Image.getInstance(String.format(images_path + "flyer_bg.png"));
        img.scalePercent(110);
        // img.scaleToFit(650, 500);
        // img.setAbsolutePosition((PageSize.A4.getHeight() - img.getScaledWidth()) / 2, (PageSize.A4.getWidth() -
        // img.getScaledHeight()) / 2);
        img.setAbsolutePosition(0, 0);
        canvas.addImage(img);

        Image img1 = Image.getInstance(images_path + "orange_gradient.png");

        img1.scaleAbsoluteHeight(25f);
        img1.scaleAbsoluteWidth(450f);
        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(0, 295);
        canvas.addImage(img1);

        img1 = Image.getInstance(images_path + "orange_gradient.png");

        img1.scaleAbsoluteHeight(200f);
        img1.scaleAbsoluteWidth(200f);
        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(130, 70);
        canvas.addImage(img1);

        // img1 =
        // Image.getInstance("http://api.qrserver.com/v1/create-qr-code/?data="+orange_qrcode_value+"&amp;size=100x100");
        img1.scaleAbsoluteHeight(170f);
        img1.scaleAbsoluteWidth(170f);

        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(145, 85);
        canvas.addImage(img1);

        img1 = Image.getInstance(images_path + "arrows.png");

        // img.setAbsolutePosition((100 - img.getScaledWidth()) / 2, (100 - img.getScaledHeight()) / 2);
        img1.setAbsolutePosition(10, 300);
        canvas.addImage(img1);

    }

    public PdfPCell getAttentionText(String cell_text) throws BadElementException, MalformedURLException, IOException,
            DocumentException {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setUseDescender(true);
        // Paragraph title11 = new Paragraph(cell_text, FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, new
        // Color(244, 123, 39)));

        Paragraph title11 = new Paragraph("ATTENTION : ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD,
                new Color(90, 90, 90)));
        title11.setAlignment("center");
        title11.add(new Chunk("Keep this QR code hidden and allow the customer to scan it", FontFactory.getFont(
                FontFactory.HELVETICA, 8, Font.NORMAL, new Color(90, 90, 90))));
        title11.add(new Chunk(" only after receiving payment.\n", FontFactory.getFont(FontFactory.HELVETICA, 8,
                Font.BOLD, new Color(90, 90, 90))));
        title11.add(new Chunk("Scanning this code will issue a PaidPunch Card to the customer.", FontFactory.getFont(
                FontFactory.HELVETICA, 8, Font.NORMAL, new Color(90, 90, 90))));
        // title11.add(new Chunk("\nOff",FontFactory.getFont(FontFactory.HELVETICA, 18, Font.NORMAL, new Color(244, 123,
        // 39))));
        cell.addElement(title11);
        return cell;
    }

    public PdfPCell getIssuingText(String cell_text) throws BadElementException, MalformedURLException, IOException,
            DocumentException {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setUseDescender(true);
        // Paragraph title11 = new Paragraph(cell_text, FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, new
        // Color(244, 123, 39)));

        Paragraph title11 = new Paragraph(cell_text, FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD,
                new Color(255, 255, 255)));
        title11.setAlignment("left");

        // title11.add(new Chunk("\nOff",FontFactory.getFont(FontFactory.HELVETICA, 18, Font.NORMAL, new Color(244, 123,
        // 39))));
        cell.addElement(title11);
        return cell;
    }

}