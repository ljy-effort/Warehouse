package com.wms.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.mapper.mysql.GoodsMapper;
import com.wms.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.service.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private RecordService recordService; // 注入RecordService

    @Override
    public IPage pageCC(IPage<Goods> page, Wrapper wrapper) {
        return goodsMapper.pageCC(page, wrapper);
    }

    @Override
    public Goods findGoodsByCode(String goodsCode){
        return goodsMapper.selectOne(new QueryWrapper<Goods>().eq("goodscode", goodsCode));
    }
    @Override
    public boolean addInventory(String goodsCode, int quantity, BigDecimal amount) {
        Goods goods = findGoodsByCode(goodsCode);
        if (goods == null) {
            throw new RuntimeException("物料不存在");
        }
        // 更新库存数量
        goods.setCount(goods.getCount() + quantity);
        goodsMapper.updateById(goods);

        // 创建入库记录
        Record record = new Record();
        record.setGoods(goods.getId());
        record.setCount(quantity);
        record.setAmount(amount);
        record.setAction("in"); // 入库操作
        recordService.save(record);
        return true;
    }
    @Override
    public boolean subtractInventory(String goodsCode, int quantity, BigDecimal amount) {
        Goods goods = findGoodsByCode(goodsCode);
        if (goods == null || goods.getCount() < quantity) {
            throw new RuntimeException("物料不存在或库存不足");
        }
        // 更新库存数量
        goods.setCount(goods.getCount() - quantity);
        goodsMapper.updateById(goods);

        // 创建出库记录
        Record record = new Record();
        record.setGoods(goods.getId());
        record.setCount(quantity);
        record.setAmount(amount);
        record.setAction("out"); // 出库操作
        recordService.save(record);
        return true;
    }
}
