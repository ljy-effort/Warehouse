package com.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.entity.*;

import java.util.List;
import java.util.Map;

public interface InventoryService extends IService<InventorySearchResult> {
    List<Map<String, Object>> findRecordsByGoodsAndDate(Integer goodsId, String startTime, String endTime);
    List<Material> getAllMaterials();

    List<Map<String, Object>> findRecordsForAllMaterials(String startTime, String endTime);

    List<Map<String, Object>> findRecordsWithGoodsInfo(String startTime, String endTime);

    String getGoodsCode(Integer goodsId);
    String getGoodsName(Integer goodsId);
    int getStockCount(Integer goodsId);

    List<RecordDetail> findRecordsByGoodsAndDateWithDetails(Integer goodsId, String startTime, String endTime);
}