package com.paidpunch.dashboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class DataAccessV2 {

    private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final static String JDBC_URL_TEST = "jdbc:mysql://paidpunchtest.csepczasc6nf.us-west-2.rds.amazonaws.com:3306/paidpunchprod";
    private final static String JDBC_URL_PROD = "jdbc:mysql://paidpunchprod.csepczasc6nf.us-west-2.rds.amazonaws.com:3306/paidpunchprod";
    private final static String USERID_TEST = "paidpunch";
    private final static String USERID_PROD = "paidpunchprod";
    private final static String PASSWORD_TEST = "Biscuit-1";
    private final static String PASSWORD_PROD = "Biscuit-1";
    
    public final static String STATS_PURCHASED_PUNCHES_PER_YEAR_MONTH = "purchasedPunchesPerYearMonth";
    public final static String STATS_USED_PUNCHES_PER_YEAR_MONTH = "usedPunchesPerYearMonth";
    public final static String STATS_UNIQUE_USERS_PER_YEAR_MONTH = "uniqueUsersPerYearMonth";
    public final static String STATS_COUNT_UNIQUE_USERS_WHO_PURCHASED_PUNCHES = "countUniqueUsersWhoPurchasedPunches";
    public final static String STATS_COUNT_PURCHASED_PUNCHES = "countPurchasedPunches";
    public final static String STATS_PURCHASED_PUNCHES_PER_USER = "purchasedPunchesPerUser";
    public final static String STATS_USED_PUNCHES_PER_USER = "usedPunchesPerUser";
    
    public static Logger logger = Logger.getLogger(DataAccessV2.class.getName());

    public static Connection createConnection() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            if (isProduction()) {
                conn = DriverManager.getConnection(JDBC_URL_PROD, USERID_PROD, PASSWORD_PROD);
            } else {
                conn = DriverManager.getConnection(JDBC_URL_TEST, USERID_TEST, PASSWORD_TEST);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return conn;
    }

    private static boolean isProduction() {
        Object o; // assumes FALSE if the value isn't declared
        try {
            o = (new InitialContext()).lookup("java:comp/env/isProduction");
        } catch (NamingException e) {
            o = Boolean.FALSE;
        }
        return o == null ? Boolean.FALSE : (Boolean) o;
    }
    
    public static Object getStats(String stats) {
        if (stats.equals(STATS_PURCHASED_PUNCHES_PER_YEAR_MONTH)) {
            return getPurchasedPunchesPerYearMonth();
        } else if (stats.equals(STATS_USED_PUNCHES_PER_YEAR_MONTH)) {
            return getUsedPunchesPerYearMonth();
        } else if (stats.equals(STATS_UNIQUE_USERS_PER_YEAR_MONTH)) {
            return getUniqueUsersPerYearMonth();
        } else if (stats.equals(STATS_COUNT_UNIQUE_USERS_WHO_PURCHASED_PUNCHES)) {
            return getCountUniqueUsersWhoPurchasedPunches();
        } else if (stats.equals(STATS_COUNT_PURCHASED_PUNCHES)) {
            return getCountPurchasedPunches();
        } else if (stats.equals(STATS_PURCHASED_PUNCHES_PER_USER)) {
            return getPurchasedPunchesPerUser();
        } else if (stats.equals(STATS_USED_PUNCHES_PER_USER)) {
            return getUsedPunchesPerUser();
        }
        return null;
    }
    
    private static Object getUsedPunchesPerUser() {
        PunchesPerUser usedPunchesPerYearMonth = new PunchesPerUser();
        Connection conn = createConnection();
        PreparedStatement prepStmt;
        try {
            prepStmt = conn.prepareStatement(
                    "SELECT pt.app_user_id, a.username AS NAME, a.email_id AS EMAIL, COUNT(*) AS USED_PUNCHES " +
                            "FROM punch_card_tracker pt, app_user a " +
                            "WHERE pt.app_user_id = a.user_id " +
                            "GROUP BY app_user_id " +
                            "ORDER BY USED_PUNCHES DESC;");
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("NAME");
                String email = rs.getString("EMAIL");
                Integer punches = rs.getInt("USED_PUNCHES");
                usedPunchesPerYearMonth.add(name, email, punches);
            }
            rs.close();
            prepStmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return usedPunchesPerYearMonth;
    }

    private static PunchesPerUser getPurchasedPunchesPerUser() {
        PunchesPerUser purchasedPunchesPerYearMonth = new PunchesPerUser();
        Connection conn = createConnection();
        PreparedStatement prepStmt;
        try {
            prepStmt = conn.prepareStatement(
                    "SELECT pd.app_user_id, a.username AS NAME, a.email_id AS EMAIL, COUNT(*) AS PURCHASED_PUNCHES " +
                            "FROM punchcard_download pd, app_user a " +
                            "WHERE isfreepunch = 'false' AND pd.app_user_id = a.user_id " +
                            "GROUP BY app_user_id " +
                            "ORDER BY PURCHASED_PUNCHES DESC;");
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("NAME");
                String email = rs.getString("EMAIL");
                Integer punches = rs.getInt("PURCHASED_PUNCHES");
                purchasedPunchesPerYearMonth.add(name, email, punches);
            }
            rs.close();
            prepStmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return purchasedPunchesPerYearMonth;
    }

    private static Count getCountPurchasedPunches() {
        Count count = new Count();
        Connection conn = createConnection();
        PreparedStatement prepStmt;
        try {
            prepStmt = conn.prepareStatement(
                    "SELECT COUNT(*) as PURCHASED_PUNCHES " +
                            "FROM punchcard_download " +
                            "WHERE isfreepunch = 'false'");
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                Integer purchasedPunches = rs.getInt("PURCHASED_PUNCHES");
                count.setCount(purchasedPunches);
            }
            rs.close();
            prepStmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return count;
    }

    private static Count getCountUniqueUsersWhoPurchasedPunches() {
        Count count = new Count();
        Connection conn = createConnection();
        PreparedStatement prepStmt;
        try {
            prepStmt = conn.prepareStatement(
                    "SELECT COUNT(DISTINCT app_user_id) as UNIQUE_USERS " +
                            "FROM punchcard_download " +
                            "WHERE isfreepunch = 'false'");
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                Integer uniqueUsers = rs.getInt("UNIQUE_USERS");
                count.setCount(uniqueUsers);
            }
            rs.close();
            prepStmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return count;
    }

    public static PunchesPerYearMonth getPurchasedPunchesPerYearMonth() {
        PunchesPerYearMonth purchasedPunchesPerYearMonth = new PunchesPerYearMonth();
        Connection conn = createConnection();
        PreparedStatement prepStmt;
        try {
            prepStmt = conn.prepareStatement(
                    "SELECT YEAR(download_date) AS YEAR, MONTH(download_date) AS MONTH, COUNT(*) AS PUNCHES " +
                            "FROM punchcard_download " +
                            "WHERE isfreepunch = 'false' " +
                            "GROUP BY YEAR(download_date), MONTH(download_date);");
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                String year = rs.getString("YEAR");
                String month = String.format("%02d", rs.getInt("MONTH"));
                Integer punches = rs.getInt("PUNCHES");
                purchasedPunchesPerYearMonth.add(year, month, punches);
            }
            rs.close();
            prepStmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return purchasedPunchesPerYearMonth;
    }
    
    public static PunchesPerYearMonth getUsedPunchesPerYearMonth() {
        PunchesPerYearMonth usedPunchesPerYearMonth = new PunchesPerYearMonth();
        Connection conn = createConnection();
        PreparedStatement prepStmt;
        try {
            prepStmt = conn.prepareStatement(
                    "SELECT YEAR(tracker_date) AS YEAR, MONTH(tracker_date) AS MONTH, COUNT(*) AS PUNCHES " +
                            "FROM punch_card_tracker " +
                            "GROUP BY YEAR(tracker_date), MONTH(tracker_date);");
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                String year = rs.getString("YEAR");
                String month = String.format("%02d", rs.getInt("MONTH"));
                Integer punches = rs.getInt("PUNCHES");
                usedPunchesPerYearMonth.add(year, month, punches);
            }
            rs.close();
            prepStmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return usedPunchesPerYearMonth;
    }
    
    private static UsersPerYearMonth getUniqueUsersPerYearMonth() {
        UsersPerYearMonth uniqueUsersPerYearMonth = new UsersPerYearMonth();
        Connection conn = createConnection();
        PreparedStatement prepStmt;
        try {
            prepStmt = conn.prepareStatement(
                    "SELECT YEAR(Date) AS YEAR, MONTH(Date) AS MONTH, COUNT(*) AS UNIQUE_USERS " +
                            "FROM app_user " +
                            "GROUP BY YEAR(Date), MONTH(Date);");
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                String year = rs.getString("YEAR");
                String month = String.format("%02d", rs.getInt("MONTH"));
                Integer users = rs.getInt("UNIQUE_USERS");
                uniqueUsersPerYearMonth.add(year, month, users);
            }
            rs.close();
            prepStmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return uniqueUsersPerYearMonth;
    }

}
