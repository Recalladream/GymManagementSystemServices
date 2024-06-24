package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("course")
public class CourseTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "course_id")
    private String courseId;
    @TableField(value = "type")
    private String type;
    @TableField(value = "name")
    private String name;

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

    @Override
    public String toString() {
        return "CourseTable{" +
                "id=" + id +
                ", courseId='" + courseId + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
