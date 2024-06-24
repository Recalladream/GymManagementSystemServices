package com.gymmanagementsystembackend.model;

public class UserModel {
    private String userId;
    private String account;
    private String password;
    private String screenName;
    private String name;
    private int age;
    private String sex;
    private String phone;
    private String identityCard;
    private String JoinDate;

    private String vipId;
    private String type;
    private String vipJoinDate;
    private String expirationDate;
    private float height;
    private float weight;
    private float bloodPressure;
    private float heartRate;
    private int numberClass;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getJoinDate() {
        return JoinDate;
    }

    public void setJoinDate(String joinDate) {
        JoinDate = joinDate;
    }

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

    public String getVipJoinDate() {
        return vipJoinDate;
    }

    public void setVipJoinDate(String vipJoinDate) {
        this.vipJoinDate = vipJoinDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
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

    @Override
    public String toString() {
        return "UserModel{" +
                "userId='" + userId + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", screenName='" + screenName + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", identityCard='" + identityCard + '\'' +
                ", JoinDate='" + JoinDate + '\'' +
                ", vipId='" + vipId + '\'' +
                ", vipJoinDate='" + vipJoinDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", bloodPressure=" + bloodPressure +
                ", heartRate=" + heartRate +
                ", numberClass=" + numberClass +
                '}';
    }
}
