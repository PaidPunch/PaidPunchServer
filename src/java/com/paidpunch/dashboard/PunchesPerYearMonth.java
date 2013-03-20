package com.paidpunch.dashboard;

import java.util.ArrayList;
import java.util.List;

public class PunchesPerYearMonth {
    
    private List<PunchYearMonth> punchesPerYearMonth = null;
    
    public PunchesPerYearMonth() {
        punchesPerYearMonth = new ArrayList<PunchYearMonth>();
    }
    
    public void add(String year, String month, Integer punches) {
        punchesPerYearMonth.add(new PunchYearMonth(year, month, punches));
    }
    
    public boolean isEmpty() {
        return punchesPerYearMonth.isEmpty();
    }
    
    private class PunchYearMonth {
        @SuppressWarnings("unused")
        private String year = null;
        @SuppressWarnings("unused")
        private String month = null;
        @SuppressWarnings("unused")
        private Integer value = null;
        
        public PunchYearMonth(String year, String month, Integer value) {
            this.year = year;
            this.month = month;
            this.value = value;
        }
    }
    
}