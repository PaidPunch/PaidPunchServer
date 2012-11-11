package com.app;

import com.db.DataAccessController;
import com.server.Constants;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author qube26
 */
public class SessionHandler {
    public boolean sessionidverify(String userid, String sessionid)
    {
        boolean verify = false;
        try {

            verify = DataAccessController.getDataTable("app_user", "user_id", "sessionid", userid, sessionid);

        } catch (SQLException ex) {
            Constants.logger.error(ex.toString());
        }
        return verify;
    }

    public int insertsessionid(String userid, String sessionid)
    {
        int verify = -1;
        try {

            verify = DataAccessController.updatetDataToTable("app_user", "user_id", userid, "sessionid", sessionid);

        } catch (Exception ex) {
            Constants.logger.error(ex.toString());
        }
        return verify;
    }

    public String genratesessionid()
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
        System.out.println("Generated : " + randomNumber + " length " + ("" + randomNumber).length());
        return "" + randomNumber;
    }
}
