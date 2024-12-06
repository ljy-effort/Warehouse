package com.wms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

    @TableName("inventory")
    public class Inventory {
        @TableId
        private Long id;
        private String materialCode;
        private String type;
        private Integer quantity;
        private Double amount;
        // 省略其他字段和getter/setter方法
    }

