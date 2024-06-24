package com.gymmanagementsystembackend.model;

import com.gymmanagementsystembackend.domain.GiveLessonsTable;

public class GetPageModel {
    private String vipId;
    private String vipName;
    private String coachId;
    private String coachName;
    private String courseId;
    private String courseName;
    private String materialId;
    private String materialName;
    private String classTime;
    private String period;
    private String sign;
    private int page;
    private int size;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "GetPageModel{" +
                "vipId='" + vipId + '\'' +
                ", vipName='" + vipName + '\'' +
                ", coachId='" + coachId + '\'' +
                ", coachName='" + coachName + '\'' +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", materialId='" + materialId + '\'' +
                ", materialName='" + materialName + '\'' +
                ", classTime='" + classTime + '\'' +
                ", period='" + period + '\'' +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
