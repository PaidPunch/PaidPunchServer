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
    
    public final static String STATS_PUNCHES_PER_YEAR_MONTH = "punchesPerYearMonth";
    public final static String STATS_UNIQUE_USERS_PER_YEAR_MONTH = "uniqueUsersPerYearMonth";
    
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
        if (stats.equals(STATS_PUNCHES_PER_YEAR_MONTH)) {
            return getPunchesPerYearMonth();
        } else if (stats.equals(STATS_UNIQUE_USERS_PER_YEAR_MONTH)) {
            return getUniqueUsersPerYearMonth();
        }
        return null;
    }
    
    public static PunchesPerYearMonth getPunchesPerYearMonth() {
        PunchesPerYearMonth punchesPerYearMonth = new PunchesPerYearMonth();
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
                punchesPerYearMonth.add(year, month, punches);
            }
            rs.close();
            prepStmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return punchesPerYearMonth;
    }
    
    private static UniqueUsersPerYearMonth getUniqueUsersPerYearMonth() {
        UniqueUsersPerYearMonth uniqueUsersPerYearMonth = new UniqueUsersPerYearMonth();
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
