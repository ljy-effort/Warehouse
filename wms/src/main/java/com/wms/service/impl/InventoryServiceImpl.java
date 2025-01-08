package com.wms.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.entity.*;
import com.wms.mapper.mysql.InventoryMapper;
import com.wms.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, InventorySearchResult> implements InventoryService {
    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public List<Map<String, Object>> findRecordsByGoodsAndDate(Integer goodsId, String startTime, String endTime) {
        return inventoryMapper.findRecordsByGoodsAndDate(goodsId, startTime, endTime);
    }
    @Override
    public List<Material> getAllMaterials() {
        // 调用mapper方法获取所有物料信息
        return inventoryMapper.getAllMaterials();
    }
    @Override
    public List<Map<String, Object>> findRecordsForAllMaterials(String startTime, String endTime) {
        // 调用mapper方法获取所有物料的出入库记录
        return inventoryMapper.findRecordsForAllMaterials(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> findRecordsWithGoodsInfo(String startTime, String endTime) {
        return inventoryMapper.findRecordsWithGoodsInfo(startTime, endTime);
    }
    @Override
    public String getGoodsCode(Integer goodsId) {
        Map<String, Object> goodsInfo = inventoryMapper.getGoodsInfo(goodsId);
        return (String) goodsInfo.get("goodscode");
    }
    @Override
    public String getGoodsName(Integer goodsId) {
        Map<String, Object> goodsInfo = inventoryMapper.getGoodsInfo(goodsId);
        return (String) goodsInfo.get("name");
    }
    @Override
    public int getStockCount(Integer goodsId) {
        Map<String, Object> goodsInfo = inventoryMapper.getGoodsInfo(goodsId);
        return (Integer) goodsInfo.get("count");
    }
    @Override
    public List<RecordDetail> findRecordsByGoodsAndDateWithDetails(Integer goodsId, String startTime, String endTime){
        return inventoryMapper.findRecordsByGoodsAndDateWithDetails(goodsId, startTime, endTime);
    }

}