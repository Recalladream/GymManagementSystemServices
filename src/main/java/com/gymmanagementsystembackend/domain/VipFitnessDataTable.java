package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("vip_fitness_data")
public class VipFitnessDataTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "vip_id")
    private String vipId;
    @TableField(value = "height")
    private float height;
    @TableField(value = "weight")
    private float weight;
    @TableField(value = "blood_pressure")
    private float bloodPressure;
    @TableField(value = "heart_rate")
    private float heartRate;
    @TableField(value = "number_class")
    private int numberClass;

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

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(float bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public float getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(float heartRate) {
        this.heartRate = heartRate;
    }

    public int getNumberClass() {
        return numberClass;
    }

    public void setNumberClass(int numberClass) {
        this.numberClass = numberClass;
    }

    @Override
    public String toString() {
        return "VipFitnessDataTable{" +
                "id=" + id +
                ", vipId='" + vipId + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", bloodPressure=" + bloodPressure +
                ", heartRate=" + heartRate +
                ", numberClass=" + numberClass +
                '}';
    }
}
