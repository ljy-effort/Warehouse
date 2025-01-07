package com.wms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecordExc {
    private Integer id;
    private String goodscode;
    private String goodsname;
    private String storagename;
    private String goodstypename;
    private String adminname;
    private Integer count;
    private BigDecimal amount;
    private LocalDateTime createtime;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStoragename() {
        return storagename;
    }

    public void setStoragename(String storagename) {
        this.storagename = storagename;
    }

    public String getGoodstypename() {
        return goodstypename;
    }

    public void setGoodstypename(String goodstypename) {
        this.goodstypename = goodstypename;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RecordExc(Integer id, String goodscode, String goodsname, String storagename, String goodstypename, String adminname, Integer count, BigDecimal amount, LocalDateTime createtime, String remark) {
        this.id = id;
        this.goodscode = goodscode;
        this.goodsname = goodsname;
        this.storagename = storagename;
        this.goodstypename = goodstypename;
        this.adminname = adminname;
        this.count = count;
        this.amount = amount;
        this.createtime = createtime;
        this.remark = remark;
    }
// Getters and Setters
}
