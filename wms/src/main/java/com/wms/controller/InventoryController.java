package com.wms.controller;

import com.wms.common.Result;
import com.wms.entity.InventorySearchResult;
import com.wms.entity.Record;
import com.wms.service.InventoryService;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/search")
    public Result searchRecords(@RequestParam("goodsId") Integer goodsId,
                                @RequestParam("startTime") String startTime,
                                @RequestParam("endTime") String endTime) {
        try {
            List<Map<String, Object>> records = inventoryService.findRecordsByGoodsAndDate(goodsId, startTime, endTime);
            int totalCount = 0;
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (Map<String, Object> record : records) {
                Integer count = (Integer) record.get("count");
                BigDecimal amount = (BigDecimal) record.get("amount");

                totalCount += count; // 累加数量，不管正负

                if (count >= 0) {
                    totalAmount = totalAmount.add(amount); // 入库，累加金额
                } else {
                    totalAmount = totalAmount.subtract(amount); // 出库，累减金额
                }
            }

            InventorySearchResult result = new InventorySearchResult(totalCount, totalAmount);
            return Result.suc(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("查询失败： " + e.getMessage());
        }
    }
}