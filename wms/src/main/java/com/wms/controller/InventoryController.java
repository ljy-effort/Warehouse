package com.wms.controller;

import com.wms.common.Result;
import com.wms.entity.*;
import com.wms.service.InventoryService;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/materials")
    public Result getAllMaterials() {
        try {
            List<Material> materials = inventoryService.getAllMaterials();
            return Result.suc(materials);
        } catch (Exception e) {
            return Result.fail("获取所有物料失败：" + e.getMessage());
        }
    }

    @GetMapping("/searchs")
    public Result searchRecord(@RequestParam(required = false) Integer goodsId,
                               @RequestParam("startTime") String startTime,
                               @RequestParam("endTime") String endTime) {
        try {
            List<Map<String, Object>> records;
            if (goodsId == null) {
                records = inventoryService.findRecordsForAllMaterials(startTime, endTime);
            } else {
                records = inventoryService.findRecordsByGoodsAndDate(goodsId, startTime, endTime);
            }

            Map<Integer, InventorySearchResult> materialResults = new HashMap<>();
            for (Map<String, Object> record : records) {
                if (record.get("goods") == null || record.get("count") == null || record.get("amount") == null) {
                    continue;
                }
                Integer currentGoodsId = (Integer) record.get("goods");
                Integer count = (Integer) record.get("count");
                BigDecimal amount = (BigDecimal) record.get("amount");

                // 查询物料编码和名称
                String goodsCode = inventoryService.getGoodsCode(currentGoodsId);
                String goodsName = inventoryService.getGoodsName(currentGoodsId);
                int stockCount = inventoryService.getStockCount(currentGoodsId);

                InventorySearchResult result = materialResults.getOrDefault(currentGoodsId, new InventorySearchResult(0, BigDecimal.ZERO, goodsCode, goodsName, stockCount));
                result.setTotalCount(result.getTotalCount() + count.intValue());
                if (count >= 0) {
                    result.setTotalAmount(result.getTotalAmount().add(amount));
                } else {
                    result.setTotalAmount(result.getTotalAmount().subtract(amount));
                }
                materialResults.put(currentGoodsId, result);
            }

            if (materialResults.isEmpty()) {
                return Result.suc(new HashMap<>());
            }

            if (goodsId != null) {
                return Result.suc(materialResults.getOrDefault(goodsId, new InventorySearchResult(0, BigDecimal.ZERO, "", "", 0)));
            } else {
                return Result.suc(materialResults);
            }
        } catch (Exception e) {
            return Result.fail("查询失败： " + e.getMessage());
        }
    }
    @GetMapping("/records")
    public Result getRecordsByGoodsIdWithDetails(
            @RequestParam("goodsId") Integer goodsId,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
        try {
            List<RecordDetail> records = inventoryService.findRecordsByGoodsAndDateWithDetails(goodsId, startTime, endTime);
            return Result.suc(records);
        } catch (Exception e) {
            return Result.fail("查询记录失败：" + e.getMessage());
        }
    }
}