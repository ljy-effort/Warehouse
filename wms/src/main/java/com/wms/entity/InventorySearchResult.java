package com.wms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@TableName("inventorySearchResult")
    public class InventorySearchResult {
        private int totalCount;
        private BigDecimal totalAmount;

    public InventorySearchResult(int totalCount, BigDecimal totalAmount) {
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

        // Getters and Setters
    }
