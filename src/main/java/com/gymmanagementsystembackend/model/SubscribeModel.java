package com.gymmanagementsystembackend.model;

import java.util.Date;

public class SubscribeModel {
    private int id;
    private String vipId;
    private String vipName;
    private String coachId;
    private String coachName;
    private String courseId;
    private String courseName;
    private String time;
    private String period;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "SubscribeModel{" +
                "id=" + id +
                ", vipId='" + vipId + '\'' +
                ", vipName='" + vipName + '\'' +
                ", coachId='" + coachId + '\'' +
                ", coachName='" + coachName + '\'' +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", time='" + time + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}
