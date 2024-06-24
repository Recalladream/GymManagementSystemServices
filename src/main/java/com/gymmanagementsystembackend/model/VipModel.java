package com.gymmanagementsystembackend.model;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class VipModel {
    private String vipId;
    private String type;
    private String name;
    private int age;
    private String sex;
    private String phone;
    private float height;
    private float weight;
    private float bloodPressure;
    private float heartRate;
    private int numberClass;
    private String identityCard;
    private String joinDate;
    private String expirationDate;

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(float bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public float getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(float heartRate) {
        this.heartRate = heartRate;
    }

    public int getNumberClass() {
        return numberClass;
    }

    public void setNumberClass(int numberClass) {
        this.numberClass = numberClass;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "VipModel{" +
                ", vipId='" + vipId + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", bloodPressure=" + bloodPressure +
                ", heartRate=" + heartRate +
                ", numberClass=" + numberClass +
                ", identityCard='" + identityCard + '\'' +
                ", joinDate='" + joinDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                '}';
    }
}
