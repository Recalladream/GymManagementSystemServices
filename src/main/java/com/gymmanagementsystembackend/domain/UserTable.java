package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user")
public class UserTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "user_id")
    private String userId;
    @TableField(value = "screen_name")
    private String screenName;
    @TableField(value = "name")
    private String name;
    @TableField(value = "age")
    private int age;
    @TableField(value = "sex")
    private String sex;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "identity_card")
    private String identityCard;

    @TableField(value = "vip_id")
    private String vipId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    @Override
    public String toString() {
        return "UserTable{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", screenName='" + screenName + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", identityCard='" + identityCard + '\'' +
                ", vipId='" + vipId + '\'' +
                '}';
    }
}
