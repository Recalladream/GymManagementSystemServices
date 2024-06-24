package com.gymmanagementsystembackend.model;

public class RegisterModel {
    private String account;
    private String password;
    private String phone;
    private String code;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "RegisterModel{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
