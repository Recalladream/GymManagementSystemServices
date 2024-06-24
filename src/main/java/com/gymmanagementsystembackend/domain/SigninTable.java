package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("signin")
public class SigninTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "vip_id")
    private String vipId;
    @TableField(value = "course_id")
    private String courseId;
    @TableField(value = "coach_id")
    private String coachId;
    @TableField(value = "class_time")
    private String classTime;
    @TableField(value = "sign_time")
    private String signTime;
    @TableField(value = "period")
    private String period;
    @TableField(value = "sign")
    private String sign;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "SigninTable{" +
                "id=" + id +
                ", vipId='" + vipId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", coachId='" + coachId + '\'' +
                ", classTime='" + classTime + '\'' +
                ", signTime='" + signTime + '\'' +
                ", period='" + period + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
