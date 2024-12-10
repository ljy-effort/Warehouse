package com.wms.controller;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;

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
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String goodscode = (String)param.get("goodscode");
        String name = (String)param.get("name");
        String goodstype = (String)param.get("goodstype");
        String storage = (String)param.get("storage");
        String roleId = (String)param.get("roleId");
        String userId = (String)param.get("userId");

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper();
        queryWrapper.apply(" a.goods=b.id and b.storage=c.id and b.goodsType=d.id ");

        if("2".equals(roleId)){
           // queryWrapper.eq(Record::getUserid,userId);
            queryWrapper.apply(" a.userId= "+userId);
        }
        //增加的goodscode处理逻辑
        if(StringUtils.isNotBlank(goodscode) && !"null".equals(goodscode)){
            queryWrapper.apply(" b.material_code LIKE {0}", "%" + goodscode + "%");
        }

        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            queryWrapper.like("b.name",name);
        }
        if(StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)){
            queryWrapper.eq("d.id",goodstype);
        }
        if(StringUtils.isNotBlank(storage) && !"null".equals(storage)){
            queryWrapper.eq("c.id",storage);
        }

        IPage result = recordService.pageCC(page,queryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }
    //新增
    //新增
    @PostMapping("/save")//
    public Result save(@RequestBody Record record) {
        Goods goods = goodsService.getById(record.getGoods());
        BigDecimal amount = record.getAmount();//获取金额
        if(goods == null) {
            return Result.fail("商品不存在");
        }
        record.setGoodscode(goods.getGoodscode());//设置物料编码
        int n = record.getCount();
        //出库
        if ("2".equals(record.getAction())) {
            n = -n;
            record.setCount(n);
        }

        // 验证金额是否为非负数
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            return Result.fail("金额必须为非负数");
        }

        System.out.println(goods.getCount() + n);
        if ((goods.getCount() + n) < 0)
            return Result.fail("仓库物品剩余"+goods.getCount()+"件");
        else {
            int num = goods.getCount() + n;
            goods.setCount(num);
            goodsService.updateById(goods);
            return recordService.save(record) ? Result.suc() : Result.fail();
        }

    }
}
