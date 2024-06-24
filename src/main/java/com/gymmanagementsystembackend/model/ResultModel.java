package com.gymmanagementsystembackend.model;

public class ResultModel {
    private String code;
    private Object data;
    private String mes;
    public ResultModel() {
    }

    public ResultModel(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ResultModel(String code, Object data, String mes) {
        this.code = code;
        this.data = data;
        this.mes = mes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
}
