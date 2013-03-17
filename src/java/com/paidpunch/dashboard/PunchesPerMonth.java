package com.paidpunch.dashboard;

import java.util.ArrayList;
import java.util.List;

public class PunchesPerMonth {
    
    private List<PunchMonth> punchesPerMonth = null;
    
    public PunchesPerMonth() {
        punchesPerMonth = new ArrayList<PunchMonth>();
    }
    
    public void add(String month, Integer punches) {
        punchesPerMonth.add(new PunchMonth(month, punches));
    }
    
    public boolean isEmpty() {
        return punchesPerMonth.isEmpty();
    }
    
    private class PunchMonth {
        @SuppressWarnings("unused")
        private String month = null;
        @SuppressWarnings("unused")
        private Integer punches = null;
        
        public PunchMonth(String month, Integer punches) {
            this.month = month;
            this.punches = punches;
        }
    }
    
}