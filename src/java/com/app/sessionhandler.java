package com.app;

import java.sql.SQLException;
import java.util.Random;

import com.db.DataAccessController;
import com.server.SimpleLogger;

/**
 * @author qube26
 */
public class SessionHandler {
    
    public boolean sessionidverify(String userid, String sessionid)
    {
        boolean verify = false;
        try 
        {
            verify = DataAccessController.getDataTable("app_user", "user_id", "sessionid", userid, sessionid);
        } 
        catch (SQLException ex) 
        {
            SimpleLogger.getInstance().error(SessionHandler.class.getSimpleName(), ex.toString());
        }
        return verify;
    }

    public int insertsessionid(String userid, String sessionid)
    {
        int verify = -1;
        try {

            verify = DataAccessController.updatetDataToTable("app_user", "user_id", userid, "sessionid", sessionid);

        } 
        catch (Exception ex) 
        {
            SimpleLogger.getInstance().error(SessionHandler.class.getSimpleName(), ex.toString());
        }
        return verify;
    }

    public String generateSessionId()
    {
        String sessionid = "";
        sessionid = createRandomInteger();
        return sessionid;

    }

    private static String createRandomInteger() {
        Long aStart = 100000000000l;
        Long aEnd = 999999999999l;
        Random aRandom = new Random();
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        // get the range, casting to long to avoid overflow problems
        long range = aEnd - (long) aStart + 1;
        // System.out.println("range>>>>>>>>>>>" + range);
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        // System.out.println("fraction>>>>>>>>>>>>>>>>>>>>" + fraction);
        long randomNumber = fraction + (long) aStart;
        SimpleLogger.getInstance().info(SessionHandler.class.getSimpleName(), "Generated : " + randomNumber + " length " + ("" + randomNumber).length());
        return "" + randomNumber;
    }
}