package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("give_lessons")
public class GiveLessonsTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "course_id")
    private String courseId;
    @TableField(value = "coach_id")
    private String coachId;
    @TableField(value = "time")
    private String time;
    @TableField(value = "number")
    private int number;
    @TableField(value = "period")
    private String period;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "GiveLessonsTable{" +
                "id=" + id +
                ", courseId='" + courseId + '\'' +
                ", coachId='" + coachId + '\'' +
                ", time='" + time + '\'' +
                ", number=" + number +
                ", period='" + period + '\'' +
                '}';
    }
}
