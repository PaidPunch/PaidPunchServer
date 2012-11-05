/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jspservlets;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

/**
 *
 * @author admin
 */
public class DBConnection {
     public Connection con = null;
     public Statement stmt = null;

    public DBConnection() throws ServletException{
        try {
             
 //             Constants.logger.info("Making Connection With Database ");
              Class.forName(com.server.Constants.JDBC_DRIVER);
              con =(Connection) DriverManager.getConnection(com.server.Constants.JDBC_URL,com.server.Constants.USERID,com.server.Constants.PASSWORD);
              stmt = (Statement) con.createStatement();

      } catch (SQLException e) {
                com.server.Constants.logger.error("Error in Sql"+e.getMessage());
             throw new ServletException("SQL Exception.", e);
      } catch (ClassNotFoundException e) {
                com.server.Constants.logger.error("Error in JDBC DRiver"+e.getMessage());
             throw new ServletException("JDBC Driver not found.", e);
      } finally {

      }
    }
  public void makeConnection() throws ServletException{
      
//              out.close();
 }

  public void closeConnection() throws ServletException{
       try {
  //          Constants.logger.info("Closing Connection With Database ");
            if(stmt != null) {
                      stmt.close();
                      stmt = null;
                  }

            if(con != null) {
               con.close();
                con = null;
            }
        } catch (SQLException ex) {
            com.server.Constants.logger.error("Error in Sql"+ex.getMessage());
//                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
  }

}
