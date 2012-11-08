package com.app;

import com.db.DataAccess;
import com.db.DataAccessControler;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.server.Constants;
import com.server.SAXParserExample;
import com.server.aczreqElements;
import com.server.mysteryBean;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class mysterypunch extends HttpServlet {

    static final int h = 5;
    static final int m = 3;
    static final int l = 2;

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
        List list;
        response.setContentType("text/html;charset=UTF-8");

        ServletContext context;
        String msg;

        response.setContentType("text/html;charset=UTF-8");

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
            if (reqtype.equalsIgnoreCase("Mystery-REQ")) {

                sessionhandler session = new sessionhandler();
                boolean b = session.sessionidverify(arz.getUserId(), arz.getSessionid());
                if (!b) {
                    mysteryxml(response, "400", "You have logged in from another device", "");
                    return;
                }

                String userid = arz.getUserId();
                String punchcardid = arz.getPunchCardID();
                String punchcarddownloadid = arz.getPunch_card_downloadid();

                Vector punchcard_download = (Vector) DataAccessControler.getDataFromTable("punchcard_download",
                        "punch_card_downloadid", punchcarddownloadid).elementAt(0);
                if (punchcard_download.elementAt(8) != null) {
                    String mysteryid = punchcard_download.elementAt(8).toString();
                    Vector mystery_data = (Vector) DataAccessControler.getDataFromTable("mystery_punch", "mystery_id",
                            mysteryid).elementAt(0);
                    String offer_data = mystery_data.elementAt(2).toString();
                    mysteryxml(response, "00", "successfully", offer_data);
                    return;
                }
                mysteryBean bean = create_mystery(userid, punchcardid);
                if (bean == null) {
                    mysteryxml(response, "01", "No offer.", "");
                    return;
                }
                System.out.print(bean.toString());
                int res = DataAccessControler.updatetDataToTable("punchcard_download", "punch_card_downloadid",
                        punchcarddownloadid, "mystery_punchid", bean.getMysteryid());
                if (res == 1) {
                    DataAccess da = new DataAccess();
                    // need some changes hear

                    da.insert_user_feeds(punchcardid, userid, "Mystery", "Y", bean.getMysteryid());
                    mysteryxml(response, "00", "successfully", bean.getOffer());
                    return;
                } else {
                    mysteryxml(response, "01", "Failed to process request", bean.getOffer());
                    return;
                }
            }
            mysteryxml(response, "01", "Wrong request type.", "");

        } catch (SQLException ex) {
            Constants.logger.info(ex.toString());
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

    private mysteryBean create_mystery(String userid, String punchcardid) {
        ArrayList<mysteryBean> beanlist = new ArrayList<mysteryBean>();

        try {
            Vector field_name = new Vector();
            field_name.add("punch_card_id");
            field_name.add("mysterypunchvalid");
            Vector field_value = new Vector();
            field_value.add(punchcardid);
            field_value.add("Y");
            Vector mystry_data = DataAccessControler.getDataFromTable("mystery_punch", field_name, field_value);
            int size = mystry_data.size();
            if (size == 0) {
                mysteryBean bean = null;
                return bean;
            }
            size = size * h;
            for (int index = 0; index < mystry_data.size(); index++) {
                mysteryBean bean = new mysteryBean();
                Vector data = (Vector) mystry_data.elementAt(index);
                bean.setMysteryid(data.elementAt(0).toString());
                bean.setPunch_card_id(data.elementAt(1).toString());
                bean.setOffer(data.elementAt(2).toString());
                bean.setPro(data.elementAt(3).toString());
                String mysterypr = data.elementAt(3).toString();
                int punchloop = 0;
                punchloop = Integer.parseInt(bean.getPro());
                // if (mysterypr.equalsIgnoreCase("H")) {
                // punchloop = h;
                // }
                // if (mysterypr.equalsIgnoreCase("m")) {
                // punchloop = m;
                // }
                // if (mysterypr.equalsIgnoreCase("l")) {
                // punchloop = l;
                // }
                for (int pro = 0; pro < punchloop; pro++) {
                    beanlist.add(bean);
                }
            }

            System.out.print(beanlist.size());

        } catch (Exception ex) {
            Constants.logger.info(ex.toString());
        }

        Random random = new Random();
        int pos = createRandomInteger(0, beanlist.size(), random);
        while (true) {
            if (pos > 99 || pos < 0) {
                pos = createRandomInteger(0, beanlist.size(), random);
            } else {
                break;
            }
        }
        mysteryBean bean = beanlist.get(pos);
        return bean;
    }

    private static int createRandomInteger(int aStart, long aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        // get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        System.out.print("range>>>>>>>>>>>" + range);
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        System.out.print("fraction>>>>>>>>>>>>>>>>>>>>" + fraction);
        int randomNumber = (int) (fraction + aStart);
        System.out.print("Generated : " + randomNumber);
        return randomNumber;
    }

    private void mysteryxml(HttpServletResponse p_response, String statusCode, String statusMessage, String offer) {
        try {
            PrintWriter out = p_response.getWriter();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("paidpunch-resp");
            doc.appendChild(rootElement);

            // staff elements

            // shorten way
            // staff.setAttribute("id", "1");

            // firstname elements
            Element firstname = doc.createElement("statusCode");
            firstname.appendChild(doc.createTextNode(statusCode));
            rootElement.appendChild(firstname);

            // lastname elements
            Element lastname = doc.createElement("statusMessage");
            lastname.appendChild(doc.createTextNode(statusMessage));
            rootElement.appendChild(lastname);

            // nickname elements
            Element nickname = doc.createElement("offer");
            nickname.appendChild(doc.createTextNode(offer));
            rootElement.appendChild(nickname);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            // StreamResult result = new StreamResult(new File("C:\\file.xml"));

            // Output to console for testing
            StreamResult result = new StreamResult(out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (Exception tfe) {
            tfe.printStackTrace();
        }

    }
}