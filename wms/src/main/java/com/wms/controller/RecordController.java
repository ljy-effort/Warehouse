package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.ExcelExportUtil;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.*;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String goodscode = (String) param.get("goodscode");
        String name = (String) param.get("name");
        String goodstype = (String) param.get("goodstype");
        String storage = (String) param.get("storage");
        String roleId = (String) param.get("roleId");
        String userId = (String) param.get("userId");

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper();
        queryWrapper.apply(" a.goods=b.id and b.storage=c.id and b.goodsType=d.id ");

        if ("2".equals(roleId)) {
            // queryWrapper.eq(Record::getUserid,userId);
            queryWrapper.apply(" a.userId= " + userId);
        }
        //增加的goodscode处理逻辑
        if (StringUtils.isNotBlank(goodscode) && !"null".equals(goodscode)) {
            queryWrapper.apply(" b.material_code LIKE {0}", "%" + goodscode + "%");
        }

        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            queryWrapper.like("b.name", name);
        }
        if (StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)) {
            queryWrapper.eq("d.id", goodstype);
        }
        if (StringUtils.isNotBlank(storage) && !"null".equals(storage)) {
            queryWrapper.eq("c.id", storage);
        }

        IPage result = recordService.pageCC(page, queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }

    //新增
    //新增
    @PostMapping("/save")//
    public Result save(@RequestBody Record record) {
        Goods goods = goodsService.getById(record.getGoods());
        BigDecimal amount = record.getAmount();//获取金额
        if (goods == null) {
            return Result.fail("商品不存在");
        }
        record.setGoodscode(goods.getGoodscode());//设置物料编码
        int n = record.getCount();
        //出库
        if ("2".equals(record.getAction())) {
            n = -n;
            record.setCount(n);
        }

//        // 验证金额是否为非负数
//        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
//            return Result.fail("金额必须为非负数");
//        }

        System.out.println(goods.getCount() + n);
        if ((goods.getCount() + n) < 0)
            return Result.fail("仓库物品剩余" + goods.getCount() + "件");
        else {
            int num = goods.getCount() + n;
            goods.setCount(num);
            goodsService.updateById(goods);
            return recordService.save(record) ? Result.suc() : Result.fail();
        }

    }

    //根据物料编码保存
    @PostMapping("/saveRecord")
    public Result save(@RequestBody RecordDTO recordDTO) {
        try {
            System.out.println("Received recordDTO: " + recordDTO);
            Goods goods = goodsService.findGoodsByCode(recordDTO.getGoodsCode());
            if (goods == null) {
                return Result.fail("商品不存在");
            }
            int quantity = recordDTO.getQuantity(); // 获取数量
            BigDecimal amount = recordDTO.getAmount(); // 获取金额

            // 验证金额是否为非负数
            if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
                return Result.fail("金额必须为非负数");
            }

//            // 入库操作
//            if ("in".equals(recordDTO.getType())) {
//               // goodsService.addInventory(recordDTO.getGoodsCode(), quantity, amount);
//            }
            // 出库操作
             if ("out".equals(recordDTO.getType())) {
                 // 将数量转换为负数
                 quantity = -quantity;
                 // 验证库存是否足够
                 if ((goods.getCount() + quantity) < 0) {
                     return Result.fail("仓库物品 '" + goods.getName() + "' 剩余 " + goods.getCount() + "件，不足以完成出库");
                 }
             }
             //更新库存数量
            goods.setCount(goods.getCount() + quantity);//更新库存数量
            goodsService.updateGoods(goods);

            // 创建Record对象并保存
            Record record = new Record();
            record.setGoods(goods.getId());
            record.setCount(quantity);
            record.setAmount(amount);
            record.setUserid(recordDTO.getUserid()); // 设置申请人ID
            record.setAdminId(recordDTO.getAdminId()); // 设置操作人ID
            return recordService.save(record) ? Result.suc() : Result.fail();
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
    //导出记录表到excel
    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws IOException{
       // try {
        List<RecordExc> records = recordService.getAllRecordsWithAssociations();


        // 将记录转换为Map列表
        List<Map<String, Object>> data = records.stream().map(record -> {
            Map<String, Object> row = new HashMap<>();
            row.put("ID", record.getId());
            row.put("物料编码", record.getGoodscode());
            row.put("物料名称", record.getGoodsname());
            row.put("仓库", record.getStoragename());
            row.put("分类", record.getGoodstypename());
            row.put("操作人", record.getAdminname());
            row.put("数量", record.getCount());
            row.put("金额", record.getAmount().doubleValue()); // 假设金额是BigDecimal类型
            row.put("操作时间", record.getCreatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            row.put("备注", record.getRemark());
            return row;
        }).collect(Collectors.toList());


        // 定义表头和列名
        String[] headers = {"ID", "物料编码", "物料名称", "仓库", "分类", "操作人", "数量", "金额", "操作时间", "备注"};
        String[] columns = {"ID", "物料编码", "物料名称", "仓库", "分类", "操作人", "数量", "金额", "操作时间", "备注"};
//            String[] columns = {"id", "goodscode", "goodsname", "storagename", "goodstypename", "adminname", "count", "amount", "createtime", "remark"};

            // 调用工具类导出Excel
            ExcelExportUtil.exportExcel(response, "Records", data, headers, columns);

    }
}