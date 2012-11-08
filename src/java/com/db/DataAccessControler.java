package com.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;
import com.server.Constants;
import com.server.Utility;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataAccessControler {

    public static Connection createConnection() {
        Connection l_conn = null;
        try {

            Class.forName(Constants.JDBC_DRIVER).newInstance();
            l_conn = DriverManager.getConnection(Constants.JDBC_URL, Constants.USERID, Constants.PASSWORD);
            // Class.forName("com.mysql.jdbc.Driver").newInstance();
            // l_conn = DriverManager.getConnection ("jdbc:mysql://localhost:3307/ipl","root", "root");
            // Constants.logger.info("Database connection established");
            return l_conn;
        } catch (Exception e) {
            Constants.logger.error("", e);
            e.printStackTrace();
        }
        return l_conn;
    }

    public static String getPassword(String email) throws SQLException
    {
        String Password = "";
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            l_conn = createConnection();
            l_prepStat = l_conn
                    .prepareStatement("SELECT password FROM app_user WHERE email_id=? and  isfbaccount='N';");
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
            l_prepStat.setString(1, email);

            Constants.logger.info("l_callStat ::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                for (int col = 1; col <= l_noColumns; col++) {
                    Password = "" + l_rs.getObject("password");
                    l_rs.getObject("password");
                }

            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("Error : " + e.getMessage());
        }

        return Password;

    }

    public static Vector getDataFromTable(String p_table, String p_id, String p_value) throws SQLException {
        Vector l_result = null;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            l_conn = createConnection();
            l_prepStat = l_conn.prepareStatement("SELECT * FROM " + p_table + " WHERE " + p_id + "=?;");
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
            l_prepStat.setString(1, p_id);

            if (p_value != null) {
                l_prepStat.setString(1, p_value);
            } else {
                l_prepStat.setString(1, "%");
            }

            Constants.logger.info("l_callStat ::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                Vector l_row = new Vector();
                for (int col = 1; col <= l_noColumns; col++) {
                    l_row.addElement(l_rs.getObject(col));
                }
                l_result.addElement(l_row);
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("Error : " + e.getMessage());
        }

        return l_result;
    }

    // added to get all the data from the table
    public static Vector getDataFromTable(String p_table) throws SQLException {
        Vector l_result = null;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            l_conn = createConnection();
            l_prepStat = l_conn.prepareStatement("SELECT * FROM " + p_table);
            Constants.logger.info("l_callStat::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                Vector l_row = new Vector();
                for (int col = 1; col <= l_noColumns; col++) {
                    l_row.addElement(l_rs.getObject(col));
                }
                l_result.addElement(l_row);
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("" + e.getMessage());
        }
        return l_result;
    }

    public static boolean getData_punch_card_tracker(String downloadid, String time, String date) throws SQLException {
        boolean l_result = true;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            l_conn = createConnection();
            String query = "SELECT * FROM punch_card_tracker where tracker_date='" + date
                    + "' AND punch_card_downloadid=" + downloadid + " And tracker_time >='" + time + "';";
            l_prepStat = l_conn.prepareStatement(query);
            Constants.logger.info("l_callStat::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            // l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                l_result = false;
                // Vector l_row = new Vector();
                // for (int col = 1; col <= l_noColumns; col++) {
                // l_row.addElement(l_rs.getObject(col));
                // }
                // l_result.addElement(l_row);
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("" + e.getMessage());
        }
        return l_result;
    }

    public static int insertDataToTable(String p_table, Vector p_rowData) {
        Constants.logger.info("p_rowData.size()::{}" + p_rowData.size());

        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            Constants.logger.error("" + e.getMessage());
        }
        StringBuffer l_query = new StringBuffer("INSERT INTO " + p_table + " VALUES(");
        for (int i = 0; i < p_rowData.size(); i++) {
            l_query.append("?");
            if (i == p_rowData.size() - 1) {
                l_query.append(");");
            } else {
                l_query.append(",");
            }
        }
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            Constants.logger.info("l_callStat::{}" + l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("" + e.getMessage());
        }
        for (int i = 0; i < p_rowData.size(); i++) {
            try {
                l_prepStat.setObject(i + 1, p_rowData.elementAt(i));
            } catch (SQLException e) {
                Constants.logger.error("" + e.getMessage());
            }
        }
        Constants.logger.info("inserting  data");
        // System.out.println("table "+l_prepStat.toString());
        try {
            result = l_prepStat.executeUpdate();
        } catch (SQLException e) {
            Constants.logger.error("" + e.getMessage());
        } finally {
            try {
                l_conn.close();
            } catch (SQLException ex) {
                Constants.logger.error("", ex);
            }
        }
        return result;
    }

    public static int updatetDataToTable(String p_table, String p_id, String p_idValue, String p_field, String p_value) {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            Constants.logger.error("" + e.getMessage());
        }
        StringBuffer l_query = new StringBuffer("UPDATE " + p_table + " SET " + p_field + "=? WHERE " + p_id + "=?");

        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            l_prepStat.setObject(1, p_value);
            l_prepStat.setObject(2, p_idValue);
            Constants.logger.info("l_callStat::{}" + l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());

        try {
            result = l_prepStat.executeUpdate();
            Constants.logger.info("sql executeUpdate() result" + result);
        } catch (SQLException e) {
            Constants.logger.error("" + e.getMessage());
        } finally {
            try {
                l_conn.close();
            } catch (SQLException ex) {
                Constants.logger.error("", ex);
            }
        }
        return result;
    }

    public static int insert_punchcard_download(String p_table, Vector p_rowData, String isfreepunch) {
        Constants.logger.info("p_rowData.size()::{}" + p_rowData.size());

        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String l_query;
        if (isfreepunch.equalsIgnoreCase("true"))
        {
            l_query = "insert into  "
                    + p_table
                    + "  (app_user_id, punch_card_id, punch_used,download_date,download_time,isfreepunch,exipre_date) values(?,?,?,?,?,?,?)";

        }
        else {
            l_query = "insert into  "
                    + p_table
                    + "  (app_user_id, punch_card_id, punch_used,download_date,download_time,payment_id,isfreepunch,exipre_date) values(?,?,?,?,?,?,?,?)";
        }
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        for (int i = 0; i < p_rowData.size(); i++) {
            try {
                l_prepStat.setObject(i + 1, p_rowData.elementAt(i));
            } catch (SQLException e) {
                Constants.logger.error("", e);
            }
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

    public static int insert_punchcard_tracker(String p_table, Vector p_rowData, String ismystrypunch) {
        Constants.logger.info("p_rowData.size()::{}" + p_rowData.size());

        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String l_query = "";
        // if(ismystrypunch.equalsIgnoreCase("true"))
        // {
        // l_query = "insert into  " + p_table +
        // "  (app_user_id, punch_card_id,tracker_date,tracker_time,punch_card_downloadid,is_mystery_punch,mystery_punchid) values(?,?,?,?,?,?,?)";
        //
        // }
        // else
        // {
        l_query = "insert into  "
                + p_table
                + "  (app_user_id, punch_card_id,tracker_date,tracker_time,punch_card_downloadid,is_mystery_punch) values(?,?,?,?,?,?)";
        // }
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        for (int i = 0; i < p_rowData.size(); i++) {
            try {
                l_prepStat.setObject(i + 1, p_rowData.elementAt(i));
            } catch (SQLException e) {
                Constants.logger.error("", e);
            }
        }
        Constants.logger.info("insert data in" + p_table);

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

    public static int insert_issue_code(String p_table, Vector p_rowData) {
        Constants.logger.info("p_rowData.size()::{}" + p_rowData.size());

        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String l_query = "insert into  " + p_table + "  (business_id,code,date,time,status) values(?,?,?,?,?)";
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        for (int i = 0; i < p_rowData.size(); i++) {
            try {
                l_prepStat.setObject(i + 1, p_rowData.elementAt(i));
            } catch (SQLException e) {
                Constants.logger.error("", e);
            }
        }
        Constants.logger.info("insert data in" + p_table);

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

    public static int insert_feedback_data(String p_table, Vector p_rowData) {
        Constants.logger.info("p_rowData.size()::{}" + p_rowData.size());

        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String l_query = "insert into  " + p_table
                + "  (app_user_id,feedback_data,feedback_date,feedback_time) values(?,?,?,?)";
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        for (int i = 0; i < p_rowData.size(); i++) {
            try {
                l_prepStat.setObject(i + 1, p_rowData.elementAt(i));
            } catch (SQLException e) {
                Constants.logger.error("", e);
            }
        }
        Constants.logger.info("insert data in" + p_table);

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

    public static int insertData(String p_table, Vector p_rowData) {
        Constants.logger.info("p_rowData.size()::{}" + p_rowData.size());

        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // StringBuffer l_query = new StringBuffer("INSERT INTO "+p_table+" VALUES(");
        // for(int i=0; i<p_rowData.size(); i++){
        // l_query.append("?");
        // if(i == p_rowData.size()-1)
        // l_query.append(");");
        // else
        // l_query.append(",");
        // }// insert consumer history table
        String l_query = "insert into  "
                + p_table
                + "  (username, email_id, mobile_no,password,pincode,user_status,isemailverified,time,date,isfbaccount) values(?,?,?,?,?,?,?,?,?,?)";
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        for (int i = 0; i < p_rowData.size(); i++) {
            try {
                l_prepStat.setObject(i + 1, p_rowData.elementAt(i));
            } catch (SQLException e) {
                Constants.logger.error("", e);
            }
        }
        Constants.logger.info("insert data in" + p_table);

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

    public static Vector punchcardlist(String userid) throws ParseException {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null, mysrt_pre_ste;
        int result = -1;

        try {
            l_conn = createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // old query
        StringBuffer l_query = new StringBuffer(
                "SELECT  b.business_userid,b.business_name,b.bussiness_logo,addre.address_line1,p.punch_card_id,p.punch_card_name,p.no_of_punches_per_card,p.value_of_each_punch,p.selling_price_of_punch_card,p.effective_discount,paid.punch_used,b.buss_desc,paid.punch_card_downloadid,p.disc_value_of_each_punch,p.expiry_date,b.logo_path,b.is_free_punch,p.is_mystery_punch,paid.isfreepunch,paid.mystery_punchid,paid.exipre_date FROM business_users b,punch_card p,punchcard_download paid,bussiness_address addre where b.business_userid=p.business_userid AND p.punch_card_id=paid.punch_card_id And b.business_userid=addre.business_id  and paid.app_user_id='"
                        + userid + "';");

        // StringBuffer l_query = new StringBuffer("SELECT  b.business_userid,b.business_name,"
        // + "b.bussiness_logo,b.address,p.punch_card_id,p.punch_card_name,"
        // + "p.no_of_punches_per_card,p.value_of_each_punch,p.selling_price_of_punch_card,"
        // + "p.effective_discount,paid.punch_used,b.buss_desc,paid.punch_card_downloadid,"
        // + "p.disc_value_of_each_punch,p.expiry_date,b.logo_path,b.is_free_punch,p.is_mystery_punch,paid.isfreepunch,"
        // + "paid.mystery_punchid,my.value_of_myst_punch FROM paidpunch.business_users b,"
        // + "paidpunch.punch_card p,paidpunch.punchcard_download paid,"
        // + " mystery_punch my where b.business_userid=p.business_userid AND p.punch_card_id=paid.punch_card_id And  "
        // + "paid.mystery_punchid=my.mystery_id And "
        // + "paid.app_user_id='"+userid+" ' ;");

        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        ResultSet l_rs = null, mystry_rs = null;
        Vector l_result = new Vector();
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());

        } catch (Exception e) {
            Constants.logger.error("", e);
        }
        try {
            l_rs = l_prepStat.executeQuery();
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }

        try {
            int l_noColumns = 0;
            ResultSetMetaData l_rsmd = null;
            l_rsmd = l_rs.getMetaData();
            l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                boolean mystryused = false;
                Vector l_row = new Vector();
                for (int col = 1; col <= l_noColumns; col++) {

                    l_row.addElement(l_rs.getObject(col));
                    if (col == 15) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date match_Start_Date = new java.util.Date(
                                ((java.sql.Date) l_rs.getObject(col)).getTime());
                        String strDate = dateFormat.format(new java.util.Date());
                        java.util.Date l_scan_Date = dateFormat.parse(strDate);
                        if (Utility.isAfterDateTime(l_scan_Date, match_Start_Date))
                        {
                            l_row.addElement(true);
                        }
                        else
                        {
                            l_row.addElement(false);
                        }
                    }
                }

                String mystquesy = "select count(*) FROM punch_card_tracker where app_user_id=" + userid
                        + " and punch_card_id=" + l_row.elementAt(4) + " and punch_card_downloadid="
                        + l_row.elementAt(12) + " and is_mystery_punch='true'; ;";

                mysrt_pre_ste = l_conn.prepareStatement(mystquesy);
                mystry_rs = mysrt_pre_ste.executeQuery();
                if (mystry_rs.next())
                {
                    int temp = Integer.parseInt("" + mystry_rs.getObject(1));
                    if (temp > 0)
                    {
                        mystryused = true;
                    }
                    else
                    {
                        mystryused = false;
                    }

                    l_row.add(mystryused);
                }
                if (!mystryused)
                {
                    l_result.addElement(l_row);
                }
            }
            l_rs.close();
            l_prepStat.close();

        } catch (Exception ex) {
            Constants.logger.error("", ex);
        } finally {
            try {
                l_conn.close();
            } catch (SQLException ex) {
                Constants.logger.error("", ex);
            }
        }
        return l_result;
    }

    public static Vector punches_search() {
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        StringBuffer l_query = new StringBuffer(
                "SELECT b.business_userid,b.business_name,p.qrcode,addre.latitude,addre.longitude,p.punchcard_category,b.modification_timestamp,addre.city,addre.state,addre.country,addre.zipcode,addre.address_line1 "
                        + "FROM business_users b,punch_card p,bussiness_address addre "
                        + "where p.business_userid=b.business_userid and p.business_userid=addre.business_id and p.expiry_date>='"
                        + sqlDate + "' and b.busi_enabled='Y' and b.isemailverified='Y';");
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        ResultSet l_rs = null;
        Vector l_result = new Vector();
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());

        } catch (Exception e) {
            Constants.logger.error("", e);
        }
        try {
            l_rs = l_prepStat.executeQuery();
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }

        try {
            int l_noColumns = 0;
            ResultSetMetaData l_rsmd = null;
            l_rsmd = l_rs.getMetaData();
            l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                Vector l_row = new Vector();
                for (int col = 1; col <= l_noColumns; col++) {
                    l_row.addElement(l_rs.getObject(col));
                }
                l_result.addElement(l_row);
            }

            l_rs.close();
            l_prepStat.close();

        } catch (SQLException ex) {
            Constants.logger.error("", ex);
        } finally {
            try {
                l_conn.close();
            } catch (SQLException ex) {
                Constants.logger.error("", ex);
            }
        }
        return l_result;
    }

    public static int updateDataToTable(String p_table, String p_id, String p_idValue, Vector p_fields, Vector p_rowData) {
        Constants.logger.info("p_rowData.size()::{}" + p_rowData.size());

        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            Constants.logger.error("", e);
        }
        StringBuffer l_query = new StringBuffer("UPDATE " + p_table + " SET ");
        for (int i = 0; i < p_fields.size(); i++) {
            l_query.append((String) p_fields.elementAt(i) + "=?");
            if (i != p_rowData.size() - 1) {
                l_query.append(", ");
            }
        }
        l_query.append(" WHERE " + p_id + "=?");
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            Constants.logger.info("l_callStat::{}" + l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        try {
            for (int i = 0; i < p_rowData.size(); i++) {
                l_prepStat.setObject(i + 1, p_rowData.elementAt(i));
            }
            l_prepStat.setObject(p_rowData.size() + 1, p_idValue);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        Constants.logger.info("l_prepStat::{}" + l_prepStat);
        System.out.print(l_prepStat);

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

    public static int updateDataToTable(String p_table, Vector p_ids, Vector p_idValues, Vector p_fields,
            Vector p_rowData) {
        Constants.logger.info("p_rowData.size()::{}" + p_rowData.size());

        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            Constants.logger.error("", e);
        }
        StringBuffer l_query = new StringBuffer("UPDATE " + p_table + " SET ");
        for (int i = 0; i < p_fields.size(); i++) {
            l_query.append((String) p_fields.elementAt(i) + "=?");
            if (i != p_rowData.size() - 1) {
                l_query.append(", ");
            }
        }
        l_query.append(" WHERE ");

        for (int i = 0; i < p_ids.size(); i++) {
            l_query.append((String) p_ids.elementAt(i) + "=?");
            if (i != p_ids.size() - 1) {
                l_query.append(" and ");
            }
        }

        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            Constants.logger.info("l_callStat::{}" + l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        try {
            for (int i = 0; i < p_rowData.size(); i++) {
                l_prepStat.setObject(i + 1, p_rowData.elementAt(i));
            }
            for (int i = 0; i < p_idValues.size(); i++) {
                l_prepStat.setObject(p_rowData.size() + i + 1, p_idValues.elementAt(i));
            }

        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        Constants.logger.info("l_prepStat::{}" + l_prepStat);

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

    public static Vector getDataFromTable(String p_table, Vector p_ids, Vector p_idValues) {
        // Constants.logger.info("p_ids.size()::{}"+p_ids.size());
        Vector l_result = new Vector();

        try {
            Connection l_conn = null;
            PreparedStatement l_prepStat = null;
            int result = -1;
            try {
                l_conn = createConnection();
            } catch (Exception e) {
                Constants.logger.error("", e);
            }
            StringBuffer l_query = new StringBuffer("SELECT * FROM " + p_table + " WHERE ");

            for (int i = 0; i < p_ids.size(); i++) {
                l_query.append((String) p_ids.elementAt(i) + "=?");
                if (i != p_ids.size() - 1) {
                    l_query.append(" and ");
                }
            }

            Constants.logger.info("l_query.toString()::{}" + l_query.toString());
            try {
                l_prepStat = l_conn.prepareStatement(l_query.toString());
                Constants.logger.info("l_callStat::{}" + l_prepStat);
            } catch (SQLException e) {
                Constants.logger.error("", e);
            }
            try {
                for (int i = 0; i < p_idValues.size(); i++) {
                    l_prepStat.setObject(i + 1, p_idValues.elementAt(i));
                }

            } catch (SQLException e) {
                Constants.logger.error("", e);
            }
            Constants.logger.info("l_prepStat::{}" + l_prepStat);

            ResultSet l_rs = null;
            try {
                l_rs = l_prepStat.executeQuery();
            } catch (SQLException ex) {
                Constants.logger.error("", ex);
            }
            l_result = new Vector();

            try {
                int l_noColumns = 0;
                ResultSetMetaData l_rsmd = null;
                l_rsmd = l_rs.getMetaData();
                l_noColumns = l_rsmd.getColumnCount();

                while (l_rs.next()) {
                    Vector l_row = new Vector();
                    for (int col = 1; col <= l_noColumns; col++) {
                        l_row.addElement(l_rs.getObject(col));
                    }
                    l_result.addElement(l_row);
                }

                l_rs.close();
                l_prepStat.close();

            } catch (SQLException ex) {
                Constants.logger.error("", ex);

            } finally {
                try {
                    l_conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessControler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return l_result;
    }

    public static boolean getUserValidation(String p_table, String p_id, String p_pword, String acounttype)
            throws SQLException {
        Vector l_result = null;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        String user_id_from_db = "";
        boolean result = false;
        try {
            l_conn = createConnection();
            l_prepStat = l_conn.prepareStatement("SELECT * FROM " + p_table + " WHERE " + p_id
                    + "=? and isfbaccount=?;");
            Constants.logger.info("l_callStat::{}" + l_prepStat);
            // l_prepStat.setString(1, p_id);

            if (p_pword != null) {
                l_prepStat.setString(1, p_pword);
            } else {
                l_prepStat.setString(1, "%");
            }

            l_prepStat.setString(2, acounttype);
            Constants.logger.info("l_callStat ::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                result = true;
            }

            l_rs.close();
            l_prepStat.close();

        } catch (Exception e) {
            l_prepStat.close();
            l_conn.close();
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

    public static boolean getUserValidation(String p_table, String p_id, String p_pword) throws SQLException {
        Vector l_result = null;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        String user_id_from_db = "";
        boolean result = false;
        try {
            l_conn = createConnection();
            l_prepStat = l_conn.prepareStatement("SELECT * FROM " + p_table + " WHERE " + p_id + "=? ;");
            Constants.logger.info("l_callStat::{}" + l_prepStat);
            // l_prepStat.setString(1, p_id);

            if (p_pword != null) {
                l_prepStat.setString(1, p_pword);
            } else {
                l_prepStat.setString(1, "%");
            }

            Constants.logger.info("l_callStat ::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                result = true;
                // Vector l_row = new Vector();
                // for (int col = 1; col <= l_noColumns; col++) {
                // l_row.addElement(l_rs.getObject(col));
                // }
                // user_id_from_db = l_rs.getString(4);
                // l_result.addElement(l_row);
            }

            l_rs.close();
            l_prepStat.close();

        } catch (Exception e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("", e);
        } finally {
            try {
                l_conn.close();
            } catch (SQLException ex) {
                Constants.logger.error("", ex);
            }
        }

        // if (user_id_from_db.equals(p_pword)) {
        // result = true;
        // return result;
        // }

        return result;

    }

    public static boolean getDataFromTable(String p_table, String p_id, String p_password, String p_value,
            String p_pword) throws SQLException {
        Vector l_result = null;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        boolean temp = false;
        try {
            l_conn = createConnection();
            l_prepStat = l_conn.prepareStatement("SELECT * FROM " + p_table + " WHERE " + p_id + "=? and " + p_password
                    + "=?;");
            Constants.logger.info("l_callStat::{}" + l_prepStat);
            // l_prepStat.setString(1, p_id);

            if (p_value != null) {
                l_prepStat.setString(1, p_value);
            } else {
                l_prepStat.setString(1, "%");
            }

            if (p_pword != null) {
                l_prepStat.setString(2, p_pword);
            } else {
                l_prepStat.setString(2, "%");
            }

            Constants.logger.info("l_callStat ::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                Vector l_row = new Vector();
                for (int col = 1; col <= l_noColumns; col++) {
                    l_row.addElement(l_rs.getObject(col));
                }
                l_result.addElement(l_row);
                temp = true;
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("", e);
            return false;
        }
        String db_user_name = "";
        if (temp == true)
        {
            db_user_name = (String) l_result.elementAt(0);
        }
        boolean result = false;

        if (db_user_name.equals(p_value)) {
            result = true;
            return result;
        }

        return result;

    }

    public static boolean getDataTable(String p_table, String p_id, String p_password, String p_value, String p_pword)
            throws SQLException {
        Vector l_result = null;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        boolean temp = false;
        try {
            l_conn = createConnection();
            l_prepStat = l_conn.prepareStatement("SELECT * FROM " + p_table + " WHERE " + p_id + "=? and " + p_password
                    + "=?;");
            Constants.logger.info("l_callStat::{}" + l_prepStat);
            // l_prepStat.setString(1, p_id);

            if (p_value != null) {
                l_prepStat.setString(1, p_value);
            } else {
                l_prepStat.setString(1, "%");
            }

            if (p_pword != null) {
                l_prepStat.setString(2, p_pword);
            } else {
                l_prepStat.setString(2, "%");
            }

            Constants.logger.info("l_callStat ::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                Vector l_row = new Vector();
                for (int col = 1; col <= l_noColumns; col++) {
                    l_row.addElement(l_rs.getObject(col));
                }
                l_result.addElement(l_row);
                temp = true;
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("", e);
            return false;
        }
        String db_user_name = "";
        if (temp == true)
        {
            return temp;
        }
        else
        {
            return temp;

        }

    }

    public static boolean getchecker(String p_table, String p_id, String p_value) throws SQLException {
        Vector l_result = null;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        boolean temp = false;
        try {
            l_conn = createConnection();
            l_prepStat = l_conn.prepareStatement("SELECT * FROM " + p_table + " WHERE " + p_id + "=? ;");
            Constants.logger.info("l_callStat::{}" + l_prepStat);
            // l_prepStat.setString(1, p_id);

            if (p_value != null) {
                l_prepStat.setString(1, p_value);
            } else {
                l_prepStat.setString(1, "%");
            }

            Constants.logger.info("l_callStat ::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                Vector l_row = new Vector();
                for (int col = 1; col <= l_noColumns; col++) {
                    l_row.addElement(l_rs.getObject(col));
                }
                l_result.addElement(l_row);
                temp = true;
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("", e);
            return false;
        }
        String db_user_name = "";
        if (temp == true)
        {
            return temp;
        }
        else
        {
            return temp;

        }

    }

    public static Boolean getDataFromTableDisplay(String p_table, String p_id, String p_value) throws SQLException {

        Vector l_result = null;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        boolean temp = false;
        try {
            l_conn = createConnection();
            l_prepStat = l_conn.prepareStatement("SELECT * FROM " + p_table + " WHERE " + p_id + "=? ;");
            Constants.logger.info("l_callStat::{}" + l_prepStat);
            // l_prepStat.setString(1, p_id);

            if (p_value != null) {
                l_prepStat.setString(1, p_value);
            } else {
                l_prepStat.setString(1, "%");
            }

            Constants.logger.info("l_callStat ::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                Vector l_row = new Vector();
                for (int col = 1; col <= l_noColumns; col++) {
                    l_row.addElement(l_rs.getObject(col));
                }
                l_result.addElement(l_row);
                temp = true;
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();

            Constants.logger.error("", e);
            return false;
        }
        String db_user_name = "";
        if (temp == true)
        {
            return temp;
        }
        else
        {
            return temp;

        }

    }

    public static int getMaxFieldValue(String p_table, String p_field, Vector p_ids, Vector p_idValues) {
        int l_result = 1;
        Constants.logger.info("p_ids.size()::{}" + p_ids.size());
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;

        try {
            l_conn = createConnection();
        } catch (Exception e) {
            Constants.logger.error("", e);
        }
        StringBuffer l_query = new StringBuffer("SELECT MAX(" + p_field + ") FROM " + p_table + " WHERE ");

        for (int i = 0; i < p_ids.size(); i++) {
            l_query.append((String) p_ids.elementAt(i) + "=?");
            if (i != p_ids.size() - 1) {
                l_query.append(" and ");
            }
        }

        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            Constants.logger.info("l_callStat::{}" + l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        try {
            for (int i = 0; i < p_idValues.size(); i++) {
                l_prepStat.setObject(i + 1, p_idValues.elementAt(i));
            }

        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        Constants.logger.info("l_prepStat::{}" + l_prepStat);

        ResultSet l_rs = null;
        try {
            l_rs = l_prepStat.executeQuery();
        } catch (SQLException ex) {
            Constants.logger.error("", ex);
        }
        try {
            if (l_rs.next()) {
                l_result = l_rs.getInt(1);
            }

            l_rs.close();
            l_prepStat.close();
        } catch (SQLException ex) {
            Constants.logger.error("", ex);

        } finally {
            try {
                l_conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataAccessControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return l_result;
    }

    public static int deleteFromTable(String p_table, String p_id, String p_idValue) {

        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            Constants.logger.error("", e);
        }
        StringBuffer l_query = new StringBuffer("delete from " + p_table + " where " + p_id + "=?");
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            Constants.logger.info("l_callStat::{}" + l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        try {

            l_prepStat.setString(1, p_idValue);

        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());

        try {
            result = l_prepStat.executeUpdate();
        } catch (SQLException e) {
            Constants.logger.error("", e);
        } finally {
            try {
                l_conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataAccessControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * added to get distinct values for one columan
     * 
     * @param p_table
     *            (Table Name)
     * @param field
     *            (field)
     * @return
     * @throws SQLException
     */
    public static List<Vector> getDataFromTable(String p_table, String field) throws SQLException {
        List<Vector> l_result = new ArrayList();
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            l_conn = createConnection();
            l_prepStat = l_conn.prepareStatement("SELECT distinct " + field + " FROM " + p_table);
            Constants.logger.info("l_callStat::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {

                Vector l_row = new Vector();
                for (int col = 1; col <= l_noColumns; col++) {
                    l_row.addElement(l_rs.getObject(col));
                }

                l_result.add(l_row);
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (Exception e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("", e);
        }
        return l_result;
    }

    public static boolean barcodechecker(String busid, String barcodevalue) throws SQLException {
        boolean l_result = true;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            l_conn = createConnection();
            String query = "SELECT b.business_userid,p.barcode_status"
                    + " FROM business_users b,punch_card card,punch_card_tracker p"
                    + " where b.business_userid ='" + busid + "' and"
                    + " b.business_userid=card.business_userid and"
                    + " card.punch_card_id=p.punch_card_id and"
                    + "  p.barcode_value='" + barcodevalue + "';";
            l_prepStat = l_conn.prepareStatement(query);
            Constants.logger.info("l_callStat::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            // l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                l_rs.getObject(1);
                String staus = "" + l_rs.getObject(2);
                if (staus.equalsIgnoreCase("scan"))
                {
                    l_result = true;
                }
                else
                {
                    l_result = false;
                }
                // Vector l_row = new Vector();
                // for (int col = 1; col <= l_noColumns; col++) {
                // l_row.addElement(l_rs.getObject(col));
                // }
                // l_result.addElement(l_row);
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("" + e.getMessage());
        }
        return l_result;
    }

    public static void barcodevarify(String table_name, Vector rowdata, Vector userinfo) {

    }

    public static boolean buy_punch_verifyer(String tablename, String vrifyno, String businessid) throws SQLException {
        boolean l_result = true;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            l_conn = createConnection();
            String query = "SELECT * FROM marchant_code m where code='" + vrifyno
                    + "' and business_id=(select business_userid from punch_card where punch_card_id='" + businessid
                    + "');";
            l_prepStat = l_conn.prepareStatement(query);
            Constants.logger.info("l_callStat::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            // l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();

            while (l_rs.next()) {
                l_result = false;
                // query =
                // "delete from paidpunch.marchant_code where  code='"+vrifyno+"' and business_id='"+businessid+"';";
                // l_prepStat = l_conn.prepareStatement(query);
                // l_prepStat.executeQuery();
                // l_conn.commit();
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("" + e.getMessage());
        }
        return l_result;

    }

    public static boolean buy_code_verify(String code, String bus_id, String strTime, String strDate)
            throws SQLException {
        boolean l_result = true;
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        try {
            l_conn = createConnection();
            String query = "SELECT * FROM marchant_code m where code='" + code + "'  and date='" + strDate
                    + "' AND  time >='" + strTime
                    + "' and business_id=(select business_userid from punch_card where punch_card_id='" + bus_id
                    + "');";
            l_prepStat = l_conn.prepareStatement(query);
            Constants.logger.info("l_callStat::{}" + l_prepStat);

            ResultSet l_rs = l_prepStat.executeQuery();
            // l_result = new Vector();
            ResultSetMetaData l_rsmd = l_rs.getMetaData();
            int l_noColumns = l_rsmd.getColumnCount();
            while (l_rs.next()) {
                l_result = false;
                query = "delete from marchant_code where code='" + code
                        + "' and business_id=(select business_userid from punch_card where punch_card_id='" + bus_id
                        + "');";
                l_prepStat = l_conn.prepareStatement(query);
                Constants.logger.info("delete query{}" + l_prepStat);
                // l_prepStat.executeQuery();
                l_prepStat.execute();
                // l_conn.commit();
            }

            l_rs.close();
            l_prepStat.close();
            l_conn.close();
        } catch (SQLException e) {
            l_prepStat.close();
            l_conn.close();
            Constants.logger.error("" + e.getMessage());
        }
        return l_result;

    }

    public static String insert_payment(Vector p_rowData) {
        Constants.logger.info("p_rowData.size()::{}" + p_rowData.size());
        String id = "";
        Connection l_conn = null;
        PreparedStatement l_prepStat = null;
        int result = -1;
        try {
            l_conn = createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String l_query = "";
        // if(ismystrypunch.equalsIgnoreCase("true"))
        // {
        // l_query = "insert into  " + p_table +
        // "  (app_user_id, punch_card_id,tracker_date,tracker_time,punch_card_downloadid,is_mystery_punch,mystery_punchid) values(?,?,?,?,?,?,?)";
        //
        // }
        // else
        // {
        l_query = "insert into  payment_details  (punchcard_id, appuser_id,token,response) values(?,?,?,?)";
        // }
        Constants.logger.info("l_query.toString()::{}" + l_query.toString());
        try {
            l_prepStat = l_conn.prepareStatement(l_query.toString());
            // Constants.logger.info("l_callStat::{}"+l_prepStat);
        } catch (SQLException e) {
            Constants.logger.error("", e);
        }
        for (int i = 0; i < p_rowData.size(); i++) {
            try {
                l_prepStat.setObject(i + 1, p_rowData.elementAt(i));
            } catch (SQLException e) {
                Constants.logger.error("", e);
            }
        }

        try {
            result = l_prepStat.executeUpdate();
            PreparedStatement get_prepStat = l_conn.prepareStatement("Select LAST_INSERT_ID() from payment_details ;");
            ResultSet l_rs = get_prepStat.executeQuery();
            l_rs.next();
            id = l_rs.getObject(1).toString();
        } catch (SQLException e) {
            Constants.logger.error("", e);
        } finally {
            try {
                l_conn.close();
            } catch (SQLException ex) {
                Constants.logger.error("", ex);
            }
        }
        return id;
    }

}