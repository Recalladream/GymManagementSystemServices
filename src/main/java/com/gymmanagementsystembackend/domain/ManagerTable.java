package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("manager")
public class ManagerTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "manager_id")
    private String managerId;
    @TableField(value = "account")
    private String account;
    @TableField(value = "password")
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
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

    @Override
    public String toString() {
        return "ManagerTable{" +
                "id=" + id +
                ", managerId='" + managerId + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
