package com.wms.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.entity.InventorySearchResult;
import com.wms.entity.Record;
import com.wms.mapper.mysql.InventoryMapper;
import com.wms.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}