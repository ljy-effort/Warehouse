package com.wms.controller;

import com.wms.common.ExcelExportUtil;
import com.wms.common.Result;
import com.wms.entity.*;
import com.wms.service.InventoryService;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    //共享数据结构，用于存储计算结果
    private List<InventorySearchResult> inventorySearchResults = new ArrayList<>();

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
    //之前的旧版接口，未实现分页
    @GetMapping("/searchs")
    public ResponseEntity<?> searchRecord(@RequestParam(required = false) Integer goodsId,
                                          @RequestParam("startTime") String startTime,
                                          @RequestParam("endTime") String endTime
                                          ) {
        try {
            List<Map<String, Object>> records;
            if (goodsId == null) {
                records = inventoryService.findRecordsForAllMaterials(startTime, endTime);
            } else {
                records = inventoryService.findRecordsByGoodsAndDate(goodsId, startTime, endTime);
            }

            //将数据存储，为导出做准备
           // List<InventorySearchResult> inventorySearchResults = new ArrayList<>();

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

                InventorySearchResult result = materialResults.getOrDefault(currentGoodsId, new InventorySearchResult(currentGoodsId,0, BigDecimal.ZERO, goodsCode, goodsName, stockCount));
                result.setTotalCount(result.getTotalCount() + count.intValue());
                if (count >= 0) {
                    result.setTotalAmount(result.getTotalAmount().add(amount));
                } else {
                    result.setTotalAmount(result.getTotalAmount().subtract(amount));
                }
                materialResults.put(currentGoodsId, result);
            }

            // 将计算结果添加到共享的数据结构中
            inventorySearchResults.clear(); // 清空之前的计算结果
            inventorySearchResults.addAll(materialResults.values());

            // 直接返回materialResults，不包装在Result对象中
            if (materialResults.isEmpty()) {
                return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
            }

            if (goodsId != null) {
                InventorySearchResult result = materialResults.getOrDefault(goodsId, new InventorySearchResult(goodsId,0, BigDecimal.ZERO, "", "", 0));
                return new ResponseEntity<>(Collections.singletonList(result), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(materialResults.values(), HttpStatus.OK);
            }
        } catch (Exception e) {
            // 可以选择返回一个错误信息的Map，或者自定义的错误响应体
            return new ResponseEntity<>("查询失败： " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //导出excel
    @GetMapping("/exportInventory")
    public void exportInventory(HttpServletResponse response,
                                @RequestParam("startTime") String startTime,
                                @RequestParam("endTime") String endTime) throws IOException {
        // 使用共享的数据结构进行导出
        if (inventorySearchResults.isEmpty()) {
            // 如果没有计算结果，可以返回一个错误或空的Excel文件
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=null.xlsx");
            return;
        }

        // 定义表头和列名
        String[] headers = {"ID", "物料编码", "物料名称", "现库存", "总数量", "总金额"};
        String[] columns = {"goodsId", "goodsCode", "goodsName", "stockCount", "totalCount", "totalAmount"};

//        // 简化时间格式为YYYYMMDD
//        String startDateFormat = startTime.split(" ")[0].replace("-", "");
//        String endDateFormat = endTime.split(" ")[0].replace("-", "");
//
//        // 生成工作表名称
//        String fileName = "Inventory_" + startDateFormat + "_" + endDateFormat;

        // 将结果转换为Map列表
        List<Map<String, Object>> data = inventorySearchResults.stream().map(result -> {
            Map<String, Object> row = new HashMap<>();
            row.put("goodsId", result.getGoodsId());
            row.put("goodsCode", result.getGoodsCode());
            row.put("goodsName", result.getGoodsName());
            row.put("stockCount", result.getStockCount());
            row.put("totalCount", result.getTotalCount());
            row.put("totalAmount", result.getTotalAmount());
            return row;
        }).collect(Collectors.toList());

        // 调用工具类导出Excel
        ExcelExportUtil.exportExcel(response, "fileName", data, headers, columns);
    }
    //导出某个物料某个时间段的出入库记录表
    @GetMapping("/exportMaterialRecords")
    public void exportMaterialRecords(HttpServletResponse response,
                                      @RequestParam("goodsId") Integer goodsId,
                                      @RequestParam("startTime") String startTime,
                                      @RequestParam("endTime") String endTime) throws IOException {
        // 查询数据
        List<RecordDetail> records = inventoryService.findRecordsByGoodsAndDateWithDetails(goodsId, startTime, endTime);

        // 定义表头和列名
        String[] headers = {"ID", "物料编码", "物料名称", "操作人", "数量", "金额", "操作时间"};
        String[] columns = {"id", "goodsCode", "goodsName", "username", "count", "amount", "createtime"};

        // 将结果转换为Map列表
        List<Map<String, Object>> data = records.stream().map(record -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id", record.getId());
            row.put("goodsCode", record.getGoodscode());
            row.put("goodsName", record.getGoodsname());
            row.put("username", record.getUsername());
            row.put("count", record.getCount());
            row.put("amount", record.getAmount());
            row.put("createtime", record.getCreatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return row;
        }).collect(Collectors.toList());

//        // 生成文件名 前端完成
//        String startDateFormat = startTime.split(" ")[0].replace("-", "");
//        String endDateFormat = endTime.split(" ")[0].replace("-", "");
//        String fileName = "MaterialRecords_" + startDateFormat + "_" + endDateFormat;

        // 调用工具类导出Excel
        ExcelExportUtil.exportExcel(response, "fileName", data, headers, columns);
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

    // 新增分页接口
    @GetMapping("/searchspaginated")
    public ResponseEntity<?> searchRecordPaginated(@RequestParam("startTime") String startTime,
                                                   @RequestParam("endTime") String endTime,
                                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            // 获取所有记录
            List<Map<String, Object>> records = inventoryService.findRecordsForAllMaterials(startTime, endTime);

            // 进行聚合计算
            Map<Integer, InventorySearchResult> materialResults = new HashMap<>();
            for (Map<String, Object> record : records) {
                Integer goodsId = (Integer) record.get("goods");
                Integer count = (Integer) record.get("count");
                BigDecimal amount = (BigDecimal) record.get("amount");

                String goodsCode = inventoryService.getGoodsCode(goodsId);
                String goodsName = inventoryService.getGoodsName(goodsId);
                int stockCount = inventoryService.getStockCount(goodsId);

                InventorySearchResult result = materialResults.getOrDefault(goodsId, new InventorySearchResult(goodsId, 0, BigDecimal.ZERO, goodsCode, goodsName, stockCount));
                result.setTotalCount(result.getTotalCount() + count.intValue());
                if (count >= 0) {
                    result.setTotalAmount(result.getTotalAmount().add(amount));
                } else {
                    result.setTotalAmount(result.getTotalAmount().subtract(amount));
                }
                materialResults.put(goodsId, result);
            }

            // 将计算结果添加到共享的数据结构中
            inventorySearchResults.clear(); // 清空之前的计算结果
            inventorySearchResults.addAll(materialResults.values());

            // 获取所有聚合结果
            List<InventorySearchResult> allResults = new ArrayList<>(materialResults.values());

            // 计算总记录数
            int total = allResults.size();

            // 计算分页的起始索引和结束索引
            int start = (pageNum - 1) * pageSize;
            int end = Math.min(start + pageSize, total);

            // 截取分页数据
            List<InventorySearchResult> pageResults = allResults.subList(start, end);

            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("data", pageResults);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("查询失败： " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 新增物料详细记录的分页接口
    @GetMapping("/recordspaginated")
    public ResponseEntity<?> getPaginatedRecords(@RequestParam("goodsId") Integer goodsId,
                                                 @RequestParam("startTime") String startTime,
                                                 @RequestParam("endTime") String endTime,
                                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            // 获取所有记录
            List<RecordDetail> records = inventoryService.findRecordsByGoodsAndDateWithDetails(goodsId, startTime, endTime);

            // 计算总记录数
            int total = records.size();

            // 计算分页的起始索引和结束索引
            int start = (pageNum - 1) * pageSize;
            int end = Math.min(start + pageSize, total);

            // 截取分页数据
            List<RecordDetail> pageRecords = records.subList(start, end);

            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("data", pageRecords);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("查询失败： " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}