package com.wms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class RecordDetail {
    private Integer id;
    private Integer goods;
    private String goodscode;
    private String goodsname;
    private String username;
    private Integer count;
    private BigDecimal amount;
    private LocalDateTime createtime;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoods() {
        return goods;
    }

    public void setGoods(Integer goods) {
        this.goods = goods;
    }

    public String getGoodscode() {
        return goodscode;
    }

    public void setGoodscode(String goodscode) {
        this.goodscode = goodscode;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }

    public RecordDetail(Integer id, Integer goods, String goodscode, String goodsname, String username, Integer count, BigDecimal amount, LocalDateTime createtime) {
        this.id = id;
        this.goods = goods;
        this.goodscode = goodscode;
        this.goodsname = goodsname;
        this.username = username;
        this.count = count;
        this.amount = amount;
        this.createtime = createtime;
    }
    // ... 省略其他字段的getter和setter方法 ...
}
