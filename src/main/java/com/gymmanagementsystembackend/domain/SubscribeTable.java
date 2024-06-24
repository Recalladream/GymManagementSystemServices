package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("subscribe")
public class SubscribeTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "vip_id")
    private String vipId;
    @TableField(value = "course_id")
    private String courseId;
    @TableField(value = "coach_id")
    private String coachId;
    @TableField(value = "time")
    private String time;
    @TableField(value = "period")
    private String period;

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
        return "SubscribeTable{" +
                "id=" + id +
                ", vipId='" + vipId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", coachId='" + coachId + '\'' +
                ", time='" + time + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}
