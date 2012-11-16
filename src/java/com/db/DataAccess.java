package com.db;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import com.server.Constants;
import com.server.FeedBean;

/**
 * @author qube26
 */
public class DataAccess {

    public String login(String user, String password, String sessionid) throws UnsupportedEncodingException {
        byte[] test = user.getBytes("UTF-8");
        user = new String(test, "UTF-8");
        Vector ids = new Vector();
        String result = "02";
        ids.add("username");
        ids.add("Password");
        Vector rowdata = new Vector();
        rowdata.add(user);
        rowdata.add(password);
        String userid = "";
        try {
            Vector dataFromTable = DataAccessController.getDataFromTable("app_user");
            // DataAccessControler.getDataFromTable(result, ids, ids)
            for (int j = 0; j < dataFromTable.size(); j++) {
                Vector data = (Vector) dataFromTable.elementAt(j);
                // email

                String username = (String) data.elementAt(2);
                // already login
                String status = "" + data.elementAt(6);

                if (username.equals(user))
                { // password
                    if ((data.elementAt(11) == null) || data.elementAt(11).toString().equalsIgnoreCase("N"))
                    {
                        String pass = "" + data.elementAt(4);
                        if (pass.equals(password)) {
                            // //only one login condition
                            // if (status.equalsIgnoreCase("Y")) {
                            // return "03";
                            // }
                            userid = "" + data.elementAt(0);
                            // email verifection code
                            String verification = "" + data.elementAt(7);
                            if (verification.equals("N")) {
                                return "04";
                            }
                            // int res = DataAccessControler.updatetDataToTable("app_user", "user_id", userid,
                            // "user_status", "Y");
                            int res = DataAccessController.updatetDataToTable("app_user", "user_id", userid,
                                    "sessionid", sessionid);
                            if (res == -1) {
                                return "03";
                            }
                            return userid;
                        }
                        return "01";
                    }
                }
            }
        } catch (Exception ex) {
            Constants.logger.error(ex.getMessage());
        }
        return result;
    }

    public Vector merchant_login(String user, String password) throws UnsupportedEncodingException {
        byte[] test = user.getBytes("UTF-8");
        Vector rowdata = new Vector();
        user = new String(test, "UTF-8");
        // Vector ids = new Vector();
        String userid = "";
        try {
            Vector dataFromTable = DataAccessController.getDataFromTable("business_users");
            for (int j = 0; j < dataFromTable.size(); j++) {
                Vector data = (Vector) dataFromTable.elementAt(j);
                // email
                String username = (String) data.elementAt(2);
                // already login
                // String status = "" + data.elementAt(6);

                if (username.equals(user)) {
                    // password
                    String pass = "" + data.elementAt(3);
                    if (pass.equals(password)) {
                        userid = "" + data.elementAt(0);
                        rowdata.add("00");
                        rowdata.add(userid);
                        rowdata.add(data.elementAt(1));
                        rowdata.add(data.elementAt(7));

                        Vector puchdata = (Vector) DataAccessController.getDataFromTable("punch_card",
                                "business_userid", userid).elementAt(0);
                        rowdata.add(puchdata.elementAt(2));
                        rowdata.add(puchdata.elementAt(3));
                        rowdata.add(puchdata.elementAt(4));

                        return rowdata;
                    }
                    rowdata.add("01");
                    return rowdata;
                }
            }
        } catch (Exception ex) {
            Constants.logger.error(ex.getMessage());
        }
        rowdata.add("02");
        return rowdata;
    }

    public String logout(String userid) {
        Vector ids = new Vector();
        String result = "02";
        try {
            int res = DataAccessController.updatetDataToTable("app_user", "user_id", userid, "user_status", "N");
            if (res == 1) {
                return userid;
            }
            return "01";
        } catch (Exception ex) {
            Constants.logger.error(ex.getMessage());
        }
        return result;
    }

    public String UserRegistration(Vector rowdata) throws SQLException {
        String email = (String) rowdata.elementAt(1);
        String userid = "";
        boolean b = DataAccessController.getUserValidation("app_user", "email_id", email, "N");
        if (b) {
            return "01";
        }
        int i = DataAccessController.insertData("app_user", rowdata);
        if (i == -1) {
            return "02";
        } else {
            return "00";
        }
    }

    public boolean check_fbid(String id) {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            DataAccessController dac = new DataAccessController();
            l_conn = dac.createConnection();
            l_prepStat = l_conn.prepareStatement("SELECT user_id FROM app_user a where fbid=?;");
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
            l_prepStat.setString(1, id);

            Constants.logger.info("l_callStat ::{}" + l_prepStat);
            ResultSet l_rs = l_prepStat.executeQuery();
            if (l_rs.next()) {
                l_rs.close();
                l_prepStat.close();
                l_conn.close();
                return true;
            } else {
                l_rs.close();
                l_prepStat.close();
                l_conn.close();
                return false;
            }
        } catch (SQLException e) {
            try {
                l_prepStat.close();
                l_conn.close();
                Constants.logger.error("Error : " + e.getMessage());
                return false;
            } catch (Exception ex) {
                Constants.logger.error("Error : " + ex.getMessage());
                return false;
            }
        }
    }

    public boolean isFreePunch(String punchId, String appUserId) {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            DataAccessController dac = new DataAccessController();
            l_conn = dac.createConnection();
            l_prepStat = l_conn.prepareStatement("select * from punchcard_download where app_user_id=" + appUserId
                    + " and isfreepunch='true' and punch_card_id='" + punchId + "';");
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
            Constants.logger.info("l_callStat ::{}" + l_prepStat);
            ResultSet l_rs = l_prepStat.executeQuery();
            if (l_rs.next()) {
                l_rs.close();
                l_prepStat.close();
                l_conn.close();
                return true;
            } else {
                l_rs.close();
                l_prepStat.close();
                l_conn.close();
                return false;
            }
        } catch (SQLException e) {
            try {
                l_prepStat.close();
                l_conn.close();
                Constants.logger.error("Error : " + e.getMessage());
                return false;
            } catch (Exception ex) {
                Constants.logger.error("Error : " + ex.getMessage());
                return false;
            }
        }
    }

    public boolean fb_Registration(String fbid, String email, String name, String sessionId) {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            java.sql.Time time = new java.sql.Time(new Date().getTime());
            java.sql.Date date = new java.sql.Date(new Date().getTime());
            String l_query = "insert into app_user(username,email_id,user_status,isemailverified,Date,Time,sessionid,isfbaccount,fbid)values('"
                    + name
                    + "','"
                    + email
                    + "','Y','Y','"
                    + date
                    + "','"
                    + time
                    + "','"
                    + sessionId
                    + "','Y','"
                    + fbid
                    + "');";
            Constants.logger.info("l_callStat ::{}" + l_query);
            DataAccessController dac = new DataAccessController();
            l_conn = dac.createConnection();
            l_prepStat = l_conn.prepareStatement(l_query);
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
            Constants.logger.info("l_callStat ::{}" + l_prepStat);
            int l_rs = l_prepStat.executeUpdate();
            if (l_rs != 0) {
                l_prepStat.close();
                l_conn.close();
                return true;
            } else {
                l_prepStat.close();
                l_conn.close();
                return false;
            }
        } catch (SQLException e) {
            try {
                l_prepStat.close();
                l_conn.close();
                Constants.logger.error("Error : " + e.getMessage());
                return false;
            } catch (SQLException ex) {
                Constants.logger.error("Error : " + ex.getMessage());
                return false;
            }
        }
    }

    public boolean fb_login(String fbid, String email, String name, String sessionid) {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            java.sql.Time time = new java.sql.Time(new Date().getTime());
            java.sql.Date date = new java.sql.Date(new Date().getTime());
            String l_query = "UPDATE app_user SET username=?,email_id=?,user_status=?,Date=?,Time=?,sessionid=? WHERE FBid ="
                    + fbid + ";";
            Constants.logger.info("l_callStat ::{}" + l_query);
            DataAccessController dac = new DataAccessController();
            l_conn = dac.createConnection();
            l_prepStat = l_conn.prepareStatement(l_query);
            l_prepStat.setObject(1, name);
            l_prepStat.setObject(2, email);
            l_prepStat.setObject(3, "Y");
            l_prepStat.setObject(4, date);
            l_prepStat.setObject(5, time);
            l_prepStat.setObject(6, sessionid);
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
            Constants.logger.info("l_callStat ::{}" + l_prepStat);
            int l_rs = l_prepStat.executeUpdate();
            l_conn.close();
            return true;
        } catch (SQLException e) {
            try {
                l_prepStat.close();
                l_conn.close();
                Constants.logger.error("Error : " + e.getMessage());
                return false;
            } catch (SQLException ex) {
                Constants.logger.error("Error : " + ex.getMessage());
                return false;
            }
        }
    }

    public static int update_profileid(String p_table, String userid, String profileid, String isprofilecreated) {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            DataAccessController dac = new DataAccessController();
            l_conn = dac.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String l_query;
        l_query = "update app_user  set isprofile_created=?,profile_id=? where user_id =? ;";
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            l_prepStat.setObject(1, isprofilecreated);
            l_prepStat.setObject(2, profileid);
            l_prepStat.setObject(3, userid);
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        Constants.logger.info("insert data in");
        try {
            result = l_prepStat.executeUpdate();
        } catch (SQLException e) {
            Constants.logger.error("", e);
        } finally {
            try {
                l_conn.close();
            } catch (SQLException ex) {
                Constants.logger.error("", ex);
            }
        }
        return result;
    }

    public Vector getProfileId(String userId) {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            DataAccessController dac = new DataAccessController();
            l_conn = dac.createConnection();
            l_prepStat = l_conn.prepareStatement("select profile_id from app_user where user_id='" + userId + "';");
            // Constants.logger.info("l_callStat::{}"+l_prepStat.toString());
            Vector profile = new Vector();
            ResultSet l_rs = l_prepStat.executeQuery();
            if (l_rs.next()) {
                profile.add(l_rs.getObject(1).toString());
                l_rs.close();
                l_prepStat.close();
                l_conn.close();
                return profile;
            } else {
                l_rs.close();
                l_prepStat.close();
                l_conn.close();
                return profile;
            }
        } catch (SQLException e) {
            try {
                l_prepStat.close();
                l_conn.close();
                Constants.logger.error("Error : " + e.getMessage());
                return null;
            } catch (Exception ex) {
                Constants.logger.error("Error : " + ex.getMessage());
                return null;
            }
        }
    }

    public String getPaymentId() {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            DataAccessController dac = new DataAccessController();
            l_conn = dac.createConnection();
            l_prepStat = l_conn.prepareStatement("Select LAST_INSERT_ID() from payment_details ;");
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
            Constants.logger.info("l_callStat ::{}" + l_prepStat);
            ResultSet l_rs = l_prepStat.executeQuery();
            l_rs.next();
            String id = l_rs.getObject(1).toString();
            l_rs.close();
            l_prepStat.close();
            l_conn.close();
            return id;
        } catch (SQLException e) {
            try {
                l_prepStat.close();
                l_conn.close();
                Constants.logger.error("Error : " + e.getMessage());
                return "";
            } catch (Exception ex) {
                Constants.logger.error("Error : " + ex.getMessage());
                return "";
            }
        }
    }

    public static int insert_user_feeds(String buss_id, String userid, String activity, String ismysterypunch,
            String mysteryid) {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            DataAccessController dac = new DataAccessController();
            l_conn = dac.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String l_query;
        l_query = "insert into user_feeds (punchcard_id,user_id,activity,ismysterypunch,mystery_id)values(?,?,?,?,?);";
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            l_prepStat.setObject(1, buss_id);
            l_prepStat.setObject(2, userid);
            l_prepStat.setObject(3, activity);
            l_prepStat.setObject(4, ismysterypunch);
            l_prepStat.setObject(5, mysteryid);
            Constants.logger.info("l_callStat::{}" + l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        Constants.logger.info("insert data in" + l_prepStat.toString());
        try {
            result = l_prepStat.executeUpdate();
        } catch (SQLException e) {
            Constants.logger.error("", e);
        } finally {
            try {
                l_conn.close();
            } catch (SQLException ex) {
                Constants.logger.error("", ex);
            }
        }
        return result;
    }

    public ArrayList<FeedBean> getDownloadFeed() {
        Connection l_conn = null;
        ArrayList<FeedBean> feedlist = new ArrayList<FeedBean>();
        PreparedStatement l_prepStat = null;
        DataAccessController dac = new DataAccessController();
        try {
            l_conn = dac.createConnection();
            l_prepStat = l_conn
                    .prepareStatement("SELECT feeds.feeds_id,feeds.punchcard_id,feeds.user_id,feeds.activity,feeds.ismysterypunch,feeds.mystery_id,feeds.timestamp,"
                            + "appuser.fbid,appuser.username,"
                            + "card.punch_card_name,card.no_of_punches_per_card,card.value_of_each_punch,card.selling_price_of_punch_card,card.effective_discount,appuser.isfbaccount,card.disc_value_of_each_punch "
                            + "FROM"
                            + " user_feeds feeds,app_user appuser,punch_card card "
                            + "where"
                            + " appuser.user_id=feeds.user_id and  feeds.punchcard_id=card.punch_card_id "
                            + "ORDER BY `feeds_id` DESC LIMIT 30;");
            Constants.logger.info("l_callStat ::{}" + l_prepStat);
            ResultSet l_rs = l_prepStat.executeQuery();
            while (l_rs.next()) {
                // Constants.logger.info("data fetched");

                FeedBean bean = new FeedBean();
                bean.setPunchCardId(l_rs.getObject(2).toString());

                bean.setAppUserId(l_rs.getObject(3).toString());
                bean.setAction(l_rs.getObject(4).toString());
                String action = l_rs.getObject(4).toString();
                if (action.equalsIgnoreCase("Mystery")) {
                    bean.setMysteryPunchId(l_rs.getObject(6).toString());
                    String mysteryid = l_rs.getObject(6).toString().toString();
                    Vector mysterydata = (Vector) DataAccessController.getDataFromTable("mystery_punch", "mystery_id",
                            mysteryid).elementAt(0);
                    bean.setAction("Mystery");
                    bean.setOffer(mysterydata.elementAt(2).toString());
                }
                else {
                    bean.setAction(action);
                }
                if (l_rs.getObject(5) != null) {
                    bean.setIsMysteryPunch(l_rs.getObject(5).toString());
                }
                if (l_rs.getObject(7) != null) {
                    bean.setTimestamp(l_rs.getObject(7).toString());
                }
                if (l_rs.getObject(8) != null) {
                    bean.setFbid(l_rs.getObject(8).toString());
                }
                bean.setName(l_rs.getObject(9).toString());
                bean.setBusinessName(l_rs.getObject(10).toString());
                bean.setPunchesPerCard(l_rs.getObject(11).toString());
                bean.setPunchValue(l_rs.getObject(12).toString());
                bean.setPunchCardPrice(l_rs.getObject(13).toString());
                bean.setEffectiveDiscount(l_rs.getObject(14).toString());

                if (l_rs.getObject(15) == null) {
                    bean.setIsFacebookAccount("");
                } else {
                    bean.setIsFacebookAccount(l_rs.getObject(15).toString());
                }
                if (l_rs.getObject(16) != null) {
                    bean.setPunchDiscountValue(l_rs.getObject(16).toString());
                }
                bean.setIsMyFriend(false);
                feedlist.add(bean);
                // Constants.logger.error("add in list");
            }
            // Constants.logger.error("data parsed");
            l_rs.close();
            l_prepStat.close();
            l_conn.close();
            return feedlist;
        } catch (SQLException e) {
            try {
                l_prepStat.close();
                l_conn.close();
                Constants.logger.error("Error : " + e.getMessage());
                return feedlist;
            } catch (Exception ex) {
                Constants.logger.error("Error : " + ex.getMessage());
                return feedlist;
            }
        }
    }
    
    public interface ResultSetHandler
    {
         void handle(ResultSet results, Object returnObj) throws SQLException;
    }
    
    public static void queryDatabase(String queryString, Object returnObj, ResultSetHandler handler) throws SQLException 
    {
    	Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        ResultSet l_rs = null;
        try 
        {
            //l_conn = DataAccessController.createConnection();
        	Class.forName(com.server.Constants.JDBC_DRIVER);
        	l_conn = (Connection) DriverManager.getConnection(com.server.Constants.JDBC_URL, com.server.Constants.USERID,
                    com.server.Constants.PASSWORD);
            l_prepStat = l_conn.prepareStatement(queryString);
            Constants.logger.info("l_callStat ::{}" + l_prepStat);
            l_rs = l_prepStat.executeQuery();
            handler.handle(l_rs, returnObj);
            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } 
        catch (SQLException e) 
        {
            try 
            {
                l_prepStat.close();
                l_conn.close();
                Constants.logger.error("Error : " + e.getMessage());
            } catch (Exception ex) {
                Constants.logger.error("Error : " + ex.getMessage());
            }
        }
        catch (Exception e) 
        {
        	Constants.logger.error("" + e.getMessage());
        	e.printStackTrace();
        }

    }
}