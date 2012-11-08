package merchant;

import com.db.DataAccessControler;
import com.server.Constants;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qube26
 */
public class barcodeverify extends HttpServlet {

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
            ServletConfig config = null;
            config = getServletConfig();
            ServletContext context = config.getServletContext();
            Constants.loadJDBCConstants(context);
            String buss = request.getParameter("business_name");
            String barcodevalue = request.getParameter("barcode");
            // String buss = "23";
            // String barcodevalue = "296835";
            Constants.logger.error("buss_id" + buss);
            Constants.logger.error("barcode_value" + barcodevalue);
            try {
                boolean b = DataAccessControler.barcodechecker(buss, barcodevalue);
                if (b) {
                    out.print("invalid barcode");
                } else {
                    Vector filed = new Vector();
                    filed.add("barcode_scan_time");
                    filed.add("barcode_scan_date");
                    filed.add("barcode_status");
                    Vector filedvalue = new Vector();
                    Date d = new Date();
                    java.sql.Date date = new java.sql.Date(d.getTime());
                    java.sql.Time time = new java.sql.Time(d.getTime());
                    filedvalue.add(time);
                    filedvalue.add(date);
                    filedvalue.add("scan");
                    int i = DataAccessControler.updateDataToTable("punch_card_tracker", "barcode_value", barcodevalue,
                            filed, filedvalue);

                }
            } catch (Exception ex) {
                Constants.logger.error(ex);
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
}
