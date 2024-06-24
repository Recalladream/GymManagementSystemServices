package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("material")
public class MaterialTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "material_id")
    private String materialId;
    @TableField(value = "name")
    private String name;
    @TableField(value = "num")
    private int num;
    @TableField(value = "unit_price")
    private float unitPrice;
    @TableField(value = "total_price")
    private float totalPrice;
    @TableField(value = "destroy")
    private String destroy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDestroy() {
        return destroy;
    }

    public void setDestroy(String destroy) {
        this.destroy = destroy;
    }

    @Override
    public String toString() {
        return "MaterialTable{" +
                "id=" + id +
                ", materialId='" + materialId + '\'' +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", destroy='" + destroy + '\'' +
                '}';
    }
}
