package com.gymmanagementsystembackend.model;

public class SigninModel {
    private String vipId;
    private String vipName;
    private String coachId;
    private String coachName;
    private String courseId;
    private String courseName;
    private String classTime;
    private String signTime;
    private String period;
    private String signCode;
    private String sign;


    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSignCode() {
        return signCode;
    }

    public void setSignCode(String signCode) {
        this.signCode = signCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "SigninModel{" +
                "vipId='" + vipId + '\'' +
                ", vipName='" + vipName + '\'' +
                ", coachId='" + coachId + '\'' +
                ", coachName='" + coachName + '\'' +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", classTime='" + classTime + '\'' +
                ", signTime='" + signTime + '\'' +
                ", period='" + period + '\'' +
                ", signCode='" + signCode + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
