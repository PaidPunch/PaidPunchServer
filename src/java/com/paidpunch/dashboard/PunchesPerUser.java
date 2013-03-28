package com.paidpunch.dashboard;

import java.util.ArrayList;
import java.util.List;

public class PunchesPerUser {
    
    private List<PunchUserEmail> punchesPerUser = null;
    
    public PunchesPerUser() {
        punchesPerUser = new ArrayList<PunchUserEmail>();
    }
    
    public void add(String user, String email, Integer punches) {
        punchesPerUser.add(new PunchUserEmail(user, email, punches));
    }
    
    public boolean isEmpty() {
        return punchesPerUser.isEmpty();
    }
    
    private class PunchUserEmail {
        @SuppressWarnings("unused")
        private String user = null;
        @SuppressWarnings("unused")
        private String email = null;
        @SuppressWarnings("unused")
        private Integer value = null;
        
        public PunchUserEmail(String user, String email, Integer value) {
            this.user = user;
            this.email = email;
            this.value = value;
        }
    }
    
}