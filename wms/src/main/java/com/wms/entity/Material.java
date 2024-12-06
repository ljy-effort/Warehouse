package com.wms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("material")
public class Material {
    @TableId
    private Long id;
    private String materialCode; // 修改字段名为materialCode
    private String materialName; // 修改字段名为materialName
    // 省略其他字段和getter/setter方法
    // 添加getter和setter方法
    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}