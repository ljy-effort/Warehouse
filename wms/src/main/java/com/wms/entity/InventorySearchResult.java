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
        private String goodsCode; // 物料编码
        private String goodsName; // 物料名称
        private int stockCount; // 库存数量

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public InventorySearchResult(int totalCount, BigDecimal totalAmount, String goodsCode, String goodsName, int stockCount) {
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
        this.goodsCode = goodsCode;
        this.goodsName = goodsName;
        this.stockCount = stockCount;
    }

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
