package com.paidpunch;

import junit.framework.Assert;

import org.junit.Test;

import com.google.gson.Gson;
import com.paidpunch.dashboard.DataAccessV2;
import com.paidpunch.dashboard.PunchesPerMonth;

public class TestDashboard {

    @Test
    public void testPunchesPerMonth() {
        PunchesPerMonth punchesPerMonth = new PunchesPerMonth();
        punchesPerMonth.add("02/2012", 5);
        punchesPerMonth.add("03/2012", 2);
        punchesPerMonth.add("04/2012", 8);
        punchesPerMonth.add("08/2012", 10);
        
        Gson gson = new Gson();
        System.out.println();
        Assert.assertEquals("{\"punchesPerMonth\":[{\"month\":\"02/2012\",\"punches\":5},{\"month\":\"03/2012\",\"punches\":2},{\"month\":\"04/2012\",\"punches\":8},{\"month\":\"08/2012\",\"punches\":10}]}", gson.toJson(punchesPerMonth));
    }
    
    @Test
    public void testGetPunchesPerMonth() {
        PunchesPerMonth punchesPerMonth = DataAccessV2.getPunchesPerMonth();
        Assert.assertFalse(punchesPerMonth.isEmpty());
    }

}
