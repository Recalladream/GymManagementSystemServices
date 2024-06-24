package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("coach")
public class CoachTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "coach_id")
    private String coachId;
    @TableField(value = "name")
    private String name;
    @TableField(value = "age")
    private int age;
    @TableField(value = "sex")
    private String sex;
    @TableField(value = "salary")
    private float salary;
    @TableField(value = "identity_card")
    private String identityCard;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
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

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    @Override
    public String toString() {
        return "CoachTable{" +
                "id=" + id +
                ", coachId='" + coachId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", salary=" + salary +
                ", identityCard='" + identityCard + '\'' +
                '}';
    }
}
