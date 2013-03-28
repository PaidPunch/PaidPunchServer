package com.paidpunch.dashboard;

import java.util.ArrayList;
import java.util.List;

public class UsersPerYearMonth {
    
    private List<UserYearMonth> usersPerYearMonth = null;
    
    public UsersPerYearMonth() {
        usersPerYearMonth = new ArrayList<UserYearMonth>();
    }
    
    public void add(String year, String month, Integer users) {
        usersPerYearMonth.add(new UserYearMonth(year, month, users));
    }
    
    public boolean isEmpty() {
        return usersPerYearMonth.isEmpty();
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