package com.gymmanagementsystembackend.model;

public class RenewModel {
    private String vipId;
    private int num;

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "RenewModel{" +
                "vipId='" + vipId + '\'' +
                ", num=" + num +
                '}';
    }
}
