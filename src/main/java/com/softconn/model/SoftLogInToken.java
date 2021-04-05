package com.softconn.model;

import com.softconn.util.SoftUtil;

public class SoftLogInToken {

    public static Builder Builder = new Builder();
    private final String userID;
    private final String userPw;

    public static class Builder {
        private String userID;
        private String userPw;

        public Builder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public Builder setUserPw(String userPw) {
            this.userPw = userPw;
            return this;
        }

        public SoftLogInToken build() {
            return new SoftLogInToken(userID, userPw);
        }

    }

    public SoftLogInToken(String userID, String userPw) {
        this.userID = userID;
        this.userPw = SoftUtil.hashPw(userPw);
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPw() {
        return userPw;
    }
}
