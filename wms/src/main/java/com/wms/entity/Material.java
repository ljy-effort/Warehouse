package com.wms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("material")
public class Material {
    @TableId
    private Long id;
    private String code;
    private String name;
    // 省略其他字段和getter/setter方法
}
