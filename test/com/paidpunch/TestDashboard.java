package com.paidpunch;

import junit.framework.Assert;

import org.junit.Test;

import com.google.gson.Gson;
import com.paidpunch.dashboard.DataAccessV2;
import com.paidpunch.dashboard.PunchesPerYearMonth;

public class TestDashboard {

    @Test
    public void testPunchesPerYearMonth() {
        PunchesPerYearMonth punchesPerYearMonth = new PunchesPerYearMonth();
        punchesPerYearMonth.add("2012", "02", 5);
        punchesPerYearMonth.add("2012", "03", 2);
        punchesPerYearMonth.add("2012", "04", 8);
        punchesPerYearMonth.add("2012", "08", 10);
        
        Gson gson = new Gson();
        System.out.println();
        Assert.assertEquals("{\"punchesPerYearMonth\":[{\"year\":\"2012\",\"month\":\"02\",\"punches\":5},{\"year\":\"2012\",\"month\":\"03\",\"punches\":2},{\"year\":\"2012\",\"month\":\"04\",\"punches\":8},{\"year\":\"2012\",\"month\":\"08\",\"punches\":10}]}", gson.toJson(punchesPerYearMonth));
    }
    
    @Test
    public void testGetPunchesPerYearMonth() {
        PunchesPerYearMonth punchesPerYearMonth = DataAccessV2.getPunchesPerYearMonth();
        Assert.assertFalse(punchesPerYearMonth.isEmpty());
    }

}
