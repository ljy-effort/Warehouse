package com.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.entity.InventorySearchResult;
import com.wms.entity.Record;

import java.util.List;
import java.util.Map;

public interface InventoryService extends IService<InventorySearchResult> {
    List<Map<String, Object>> findRecordsByGoodsAndDate(Integer goodsId, String startTime, String endTime);
}