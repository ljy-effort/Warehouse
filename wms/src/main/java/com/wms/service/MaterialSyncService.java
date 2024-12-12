package com.wms.service;

import com.wms.entity.Goods;

import java.util.List;

public interface MaterialSyncService {
    /**
     * 同步物料信息从Oracle到MySQL数据库。
     */
    void syncMaterials();
}