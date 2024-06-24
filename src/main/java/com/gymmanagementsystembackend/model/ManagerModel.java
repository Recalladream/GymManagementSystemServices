package com.gymmanagementsystembackend.model;

public class ManagerModel {
    private String managerId;
    private String account;
    private String password;

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ManagerModel{" +
                "managerId='" + managerId + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
