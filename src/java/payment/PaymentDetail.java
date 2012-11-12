package payment;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;
import java.util.Vector;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.authorize.CustomerProfileCommunication;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.app.SessionHandler;
import com.db.DataAccess;
import com.server.AccessRequest;
import com.server.Constants;
import com.server.SAXParserExample;

/**
 * @author qube26
 */
public class PaymentDetail extends HttpServlet {

    private static final long serialVersionUID = -4579389902624220314L;
    ServletConfig config = null;
    private Vector userdata, userinfo;

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
        ServletContext context;
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

                Constants.logger.info("XML File Recv in payment_detail");
                String xmldata = new String(sb);
                xmldata = xmldata.trim();
                InputSource iSource = new InputSource(new StringReader(xmldata));

                example.parseDocument(iSource);
                list = example.getData();
                AccessRequest arz = (AccessRequest) list.get(0);
                String reqtype = arz.getTxType();

                if (reqtype.equalsIgnoreCase("profile-REQ")) {
                    // String cardtype=arz.getCardtype();
                    CustomerProfileCommunication profile = new CustomerProfileCommunication();
                    Constants.logger.info("create profile request ");
                    Constants.logger.info("userID:" + arz.getUserId() + "   user name :" + arz.getName()
                            + " email ID: " + arz.getEmail());
                    Vector profile_data = profile.createCustomerProfileAuth(arz.getName(), arz.getEmail(),
                            arz.getCardNumber(), arz.getExpirationDate(), arz.getCvv());
                    Constants.logger.info("response from payment gatway");
                    Constants.logger.info("code :" + profile_data.elementAt(0).toString() + "  message:"
                            + profile_data.elementAt(2).toString() + "  profileid:"
                            + profile_data.elementAt(1).toString() + ":");

                    String code = profile_data.elementAt(0).toString();
                    if (code.equalsIgnoreCase("00")) {
                        String profileid = profile_data.elementAt(1).toString();
                        Vector get_profile_data = profile.getCustomerProfileAuth(profileid);
                        String get_code = get_profile_data.elementAt(0).toString();
                        String get_meas = get_profile_data.elementAt(1).toString();
                        if (code.equalsIgnoreCase("00")) {
                            String payid = get_profile_data.elementAt(2).toString();
                            String maskno = get_profile_data.elementAt(3).toString();
                            String userid = arz.getUserId();
                            DataAccess da = new DataAccess();
                            da.update_profileid("app_user", userid, profileid, "true");
                            getprofilexml(response, get_meas, code, maskno, payid);
                            // xml(response,"successful","00");
                            return;
                        }
                    } else {
                        xml(response, profile_data.elementAt(2).toString(), "01");
                        return;
                    }
                }
                if (reqtype.equalsIgnoreCase("Delete-Profile-REQ")) {
                    deleteProfile(list, response);
                    return;

                }
                if (reqtype.equalsIgnoreCase("Get-Profile-REQ")) {
                    String userid = arz.getUserId();
                    DataAccess da = new DataAccess();
                    Vector profile_info = da.getProfileId(userid);
                    if (profile_info == null)
                    {
                        xml(response, "Failed to process request.Please try again.", "01");
                        return;
                    }
                    String profileid = profile_info.elementAt(0).toString();
                    // String card_type=profile_info.elementAt(1).toString();
                    CustomerProfileCommunication profile = new CustomerProfileCommunication();
                    Vector profile_data = profile.getCustomerProfileAuth(profileid);
                    String code = profile_data.elementAt(0).toString();
                    String meas = profile_data.elementAt(1).toString();

                    if (code.equalsIgnoreCase("00")) {
                        String payid = profile_data.elementAt(2).toString();
                        String maskno = profile_data.elementAt(3).toString();
                        getprofilexml(response, meas, code, maskno, payid);
                        return;
                    } else {
                        String payid = "";
                        String maskno = "";
                        getprofilexml(response, meas, code, maskno, payid);
                        return;
                    }

                }

            } catch (SSLPeerUnverifiedException ssl)
            {
                Constants.logger.error(ssl);
                getprofilexml(response, "Failed to process request.Please try again.", "02", "", "");
                return;
            } catch (Exception ex) {
                Constants.logger.error(ex);
                getprofilexml(response, "Failed to process request.Please try again.", "02", "", "");
                return;
            }
        } catch (Exception e) {
            Constants.logger.error(e);
        }
    }

    private void deleteProfile(List list, HttpServletResponse response) {
        try {
            AccessRequest arz = (AccessRequest) list.get(0);
            String userid = arz.getUserId();

            String sessionid = arz.getSessionId();
            SessionHandler session = new SessionHandler();
            boolean b = session.sessionidverify(userid, sessionid);
            Constants.logger.info("Delete profile");
            if (b)
            {
                Constants.logger.info("userid:" + userid);

                DataAccess da = new DataAccess();
                Vector profile_info = da.getProfileId(userid);
                if (profile_info == null)
                {
                    xml(response, "Failed to process request.Please try again.", "01");
                    return;
                }
                String profileid = profile_info.elementAt(0).toString();
                CustomerProfileCommunication profile = new CustomerProfileCommunication();
                Vector profile_data = profile.testDeleteCustomerProfileRequest(profileid);
                Constants.logger.info("paymentgatway response   code:" + profile_data.elementAt(0).toString()
                        + " message: " + profile_data.elementAt(1).toString() + " "
                        + profile_data.elementAt(2).toString());
                String successcode = profile_data.elementAt(0).toString();
                if (successcode.equalsIgnoreCase("00"))
                {
                    int res = da.update_profileid("app_user", userid, "", "false");
                    if (res > 0)
                    {
                        deleteXml(response, "00", "Delete Successful", "false");
                        return;
                    }
                    else
                    {
                        xml(response, "Failed to process request.Please try again.", "01");
                        return;
                    }
                }
                else
                {
                    xml(response, profile_data.elementAt(2).toString(), "01");
                    return;
                }

            }
            else
            {
                xml(response, "You have logged in from another device", "400");
            }
        }

        catch (Exception e)
        {
            Constants.logger.info(e.toString());
            xml(response, "Failed to process request.Please try again.", "01");
            return;
        }
    }

    private void xml(HttpServletResponse p_response, String mesage, String code) {

        String statusCode = code;
        String statusMessage = mesage;

        try {
            PrintWriter out = p_response.getWriter();

            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);

            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            String resmes = "<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + "<statusCode>" + statusCode + "</statusCode>"
                    + "<statusMessage>" + statusMessage + "</statusMessage>";
            out.print(resmes);
            // out.print();

            out.print("</paidpunch-resp>");

        } catch (Exception e) {
            Constants.logger.error(e);
        }

    }

    private void deleteXml(HttpServletResponse p_response, String statusCode, String statusMessage,
            String isprofilecreated) {
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
            Element nickname = doc.createElement("is_profileid_created");
            nickname.appendChild(doc.createTextNode(isprofilecreated));
            rootElement.appendChild(nickname);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            // StreamResult result = new StreamResult(new File("C:\\file.xml"));

            // Output to console for testing
            StreamResult result = new StreamResult(out);

            transformer.transform(source, result);
            Constants.logger.info("Delete prfole xml");
            Constants.logger.info(result.toString());

        } catch (ParserConfigurationException pce) {
            Constants.logger.error(pce.toString());
        } catch (Exception tfe) {
            Constants.logger.error(tfe.toString());
        }

    }

    private void getprofilexml(HttpServletResponse p_response, String mesage, String code, String masked, String payid) {

        String statusCode = code;
        String statusMessage = mesage;

        try {
            PrintWriter out = p_response.getWriter();

            Constants.logger.info("statuscode" + statusCode);
            Constants.logger.info("statusmessage" + statusMessage);

            p_response.setHeader("Content-Disposition", "attachement; filename= response.xml");
            String xml = "<?xml version='1.0' ?>"
                    + "<paidpunch-resp>"
                    + "<statusCode>" + statusCode + "</statusCode>"
                    + "<masked>" + masked + "</masked>"
                    + "<paymentid>" + payid + "</paymentid>"
                    + "<statusMessage>" + statusMessage + "</statusMessage></paidpunch-resp>";
            Constants.logger.info("masked no" + masked + " code: " + statusCode);
            Constants.logger.info("masked no and paymentid send to mobile");

            out.print(xml);

        } catch (Exception e) {
            Constants.logger.error(e);
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