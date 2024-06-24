package com.gymmanagementsystembackend.model;

public class UserLoginModel {
    String vipId;
    String token;

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserLoginModel{" +
                ", vipId='" + vipId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
