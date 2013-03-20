package com.paidpunch.dashboard;

import java.util.ArrayList;
import java.util.List;

public class UniqueUsersPerYearMonth {
    
    private List<UserYearMonth> uniqueUsersPerYearMonth = null;
    
    public UniqueUsersPerYearMonth() {
        uniqueUsersPerYearMonth = new ArrayList<UserYearMonth>();
    }
    
    public void add(String year, String month, Integer users) {
        uniqueUsersPerYearMonth.add(new UserYearMonth(year, month, users));
    }
    
    public boolean isEmpty() {
        return uniqueUsersPerYearMonth.isEmpty();
    }
    
    private class UserYearMonth {
        @SuppressWarnings("unused")
        private String year = null;
        @SuppressWarnings("unused")
        private String month = null;
        @SuppressWarnings("unused")
        private Integer value = null;
        
        public UserYearMonth(String year, String month, Integer value) {
            this.year = year;
            this.month = month;
            this.value = value;
        }
    }
    
}