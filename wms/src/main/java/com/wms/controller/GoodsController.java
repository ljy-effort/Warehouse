package com.wms.controller;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.ExcelExportUtil;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Goods;
import com.wms.entity.MaterialSearchParam;
import com.wms.service.GoodsService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

   @Autowired
    private GoodsService goodsService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods){
        return goodsService.save(goods)?Result.suc():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods){
        return goodsService.updateById(goods)?Result.suc():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){
        return goodsService.removeById(id)?Result.suc():Result.fail();
    }

    @GetMapping("/search")//查询物料的信息
    public IPage<Map<String, Object>> searchGoods(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      MaterialSearchParam params) {
        Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
        return goodsService.searchGoods(page, params);
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        String goodstype = (String)param.get("goodstype");
        String storage = (String)param.get("storage");
        //添加物料编码查询参数
        String goodscode =(String) param.get("goodscode");

        Page<Goods> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Goods::getName,name);
        }
        if(StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)){
            lambdaQueryWrapper.eq(Goods::getGoodstype,goodstype);
        }
        if(StringUtils.isNotBlank(storage) && !"null".equals(storage)){
            lambdaQueryWrapper.eq(Goods::getStorage,storage);
        }
        //添加物料编码查询条件
        if(StringUtils.isNotBlank(goodscode) && !"null".equals(goodscode)){
            lambdaQueryWrapper.eq(Goods::getGoodscode,goodscode);
        }

        IPage result = goodsService.pageCC(page,lambdaQueryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }

    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        // 查询数据
        List<Map<String, Object>> data = goodsService.getExportData();

        // 定义表头和列名
        String[] headers = {"物料编码", "货名", "仓库", "分类", "库存", "备注"};
        String[] columns = {"goodscode", "name", "storageName", "typeName", "count", "remark"};

        // 调用工具类导出Excel
        ExcelExportUtil.exportExcel(response, "GoodsData", data, headers, columns);
    }
}
