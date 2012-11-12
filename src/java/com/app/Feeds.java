package com.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.db.DataAccess;
import com.server.Constants;
import com.server.FeedBean;

/**
 * @author qube26
 */
public class Feeds extends HttpServlet {

    private static final long serialVersionUID = 3798267969248139682L;
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
    ServletContext context;
    ServletConfig config = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        response.setContentType("text/html;charset=UTF-8");
        ArrayList<FeedBean> frendlist = new ArrayList<FeedBean>();
        try {
            config = getServletConfig();
            context = config.getServletContext();
            Constants.loadJDBCConstants(context);
        } catch (Exception e) {
            Constants.logger.error(e);
        }

        try {
            ServletInputStream in = request.getInputStream();
            int info;
            StringBuffer sb = new StringBuffer();

            while ((info = in.read()) != -1) {
                char temp;
                temp = (char) info;
                sb.append(temp);
            }
            Constants.logger.info(sb);

            ArrayList<String> facebookIds = new ArrayList<String>();
            facebookIds = parseFriendList(sb);

            frendlist = feeds(facebookIds, response);
            feed_Json_Response("00", "successful", frendlist, response);
        } catch (Exception e) {
            feed_Json_Response("01", "could not fetch your facebook friend. Please try again.", frendlist, response);
            Constants.logger.info(e.toString());
        } finally {
            out.close();
        }
    }

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
    }

    private ArrayList<String> parseFriendList(StringBuffer sb) {
        String data = sb.toString();
        JSONObject json = null;
        ArrayList<String> facebookIds = new ArrayList<String>();
        try {
            json = (JSONObject) JSONSerializer.toJSON(data);
        } catch (net.sf.json.JSONException e) {
            // data=data+"\"}}";
            try {
                json = (JSONObject) JSONSerializer.toJSON(data);
            } catch (net.sf.json.JSONException exe) {
                Constants.logger.error(exe.toString());
            }
        }
        if (json != null) {
            JSONArray nameArray = json.names();
            JSONArray valArray = json.toJSONArray(nameArray);
            JSONArray friendJsonArray = valArray.getJSONArray(0);
            for (int i = 0; i < friendJsonArray.size(); i++) {
                JSONObject innerarray = friendJsonArray.getJSONObject(i);
                facebookIds.add(innerarray.getString("id"));
            }
        }
        return facebookIds;
    }

    private ArrayList<FeedBean> feeds(ArrayList<String> fbid, HttpServletResponse response) {
        ArrayList<FeedBean> feedlist = new ArrayList<FeedBean>();
        try {
            DataAccess da = new DataAccess();
            feedlist = da.getDownloadFeed();
            Constants.logger.info("feed size" + feedlist.size());
            ArrayList<FeedBean> friends = new ArrayList<FeedBean>();
            for (int i = 0; i < feedlist.size(); i++) {
                if (feedlist.get(i).getIsfbaccount().equalsIgnoreCase("Y")) {
                    String temp = feedlist.get(i).getFbid();
                    for (int j = 0; j < fbid.size(); j++) {
                        if (temp.equalsIgnoreCase(fbid.get(j))) {
                            feedlist.get(i).setIsMyFriend(true);
                            // frendlist.add(feedlist.get(i));
                            // feedlist.remove(i);
                        }
                    }
                }
            }
            // name convert into short like Visha Bansal is now Vishal B,Siddu will Siddu no chang.
            for (int i = 0; i < feedlist.size(); i++) {
                String temp = feedlist.get(i).getName();
                String namearray[] = temp.split(" ");
                String lastName = "";
                if (namearray.length > 1) {
                    lastName = namearray[namearray.length - 1];
                    char firstChar = lastName.charAt(0);
                    lastName = "" + firstChar;
                    namearray[namearray.length - 1] = lastName.toUpperCase();
                }
                temp = "";
                for (int index = 0; index <= namearray.length - 1; index++) {
                    temp = temp + namearray[index];
                    temp = temp + " ";
                }
                temp = temp.trim();
                if (namearray.length > 1) {
                    temp = temp + ".";
                }
                feedlist.get(i).setName(temp);
            }
            Constants.logger.info(friends.size());

            return feedlist;
        } catch (Exception e) {
            Constants.logger.info(e.toString());
        }
        return feedlist;
    }

    private void feed_Json_Response(String code, String msg, ArrayList<FeedBean> frendlist, HttpServletResponse response) {
        PrintWriter out;
        Constants.logger.info("json response list" + frendlist.size());
        try {
            out = response.getWriter();
            JSONObject object = new JSONObject();

            response.setHeader("Content-Disposition", "attachement; filename= response.json");
            // String jsonres = "{\"statusCode\":\"" + 00 + " \",\"statusMessage\":\"" + "successful" + "\"";
            object.put("statusCode", code);
            object.put("statusMessage", msg);
            if (code.equalsIgnoreCase("00")) {
                JSONArray arrayObj = new JSONArray();
                Constants.logger.info("json Object created ");
                {
                    for (int j = 0; j < frendlist.size(); j++) {
                        JSONObject subnode = new JSONObject();
                        FeedBean bean = frendlist.get(j);
                        // Vector data =new Vector();
                        // String bid = "" + data.elementAt(0);
                        int noofpunch = Integer.parseInt(bean.getNo_of_punches_per_card());
                        float value = Float.parseFloat(bean.getPunchValue());
                        float sell = Float.parseFloat(bean.getPunchCardPrice());
                        float dis_card = (noofpunch * value) - sell;
                        float actual_value_of_punch = Float.parseFloat(bean.getPunchDiscountValue());
                        // String qrcode = "" + data.elementAt(2);
                        subnode.put("fbid", bean.getFbid());
                        subnode.put("action", bean.getAction());
                        subnode.put("name", bean.getName());
                        subnode.put("price", noofpunch * value);
                        subnode.put("buss_name", bean.getBusinessName());
                        subnode.put("no_of_punches", bean.getNo_of_punches_per_card());
                        subnode.put("Value_of_each_punch", bean.getPunchValue());
                        subnode.put("actual_value_of_punch", bean.getPunchDiscountValue());
                        subnode.put("selling_price", bean.getPunchCardPrice());
                        subnode.put("offer", bean.getOffer());
                        subnode.put("isfriend", bean.isMyFriend());
                        subnode.put("timestamp", bean.getTimestamp());
                        subnode.put("discount_value_of_each_punch", value - actual_value_of_punch);
                        subnode.put("discount_on_card", dis_card);
                        arrayObj.add(subnode);
                    }
                    object.put("feedlist", arrayObj);
                }
            }
            out.print(object.toString());
            Constants.logger.info("RESPonse" + object.toString());
        } catch (Exception e) {
            Constants.logger.info(e.toString());
        }
    }
}