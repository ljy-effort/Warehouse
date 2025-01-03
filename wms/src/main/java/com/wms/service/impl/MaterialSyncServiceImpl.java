package com.wms.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wms.common.Result;
import com.wms.mapper.oracle.MaterialMapper;
import com.wms.mapper.mysql.GoodsMapper;
import com.wms.entity.Goods;
import com.wms.service.MaterialSyncService;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


@Service
public class MaterialSyncServiceImpl implements MaterialSyncService {


    @Autowired

    private MaterialMapper materialMapper; // Oracle数据库的Mapper

    @Autowired
    private GoodsMapper goodsMapper; // MySQL数据库的Mapper

//    // 在类中添加
//    private static final Logger log = LoggerFactory.getLogger(MaterialSyncServiceImpl.class);

// 使用log.error("物料信息同步失败")来记录错误

    /**
     * 同步物料信息从Oracle到MySQL数据库。
     */
    @DS("oracle")
   // @Transactional
    @Override
    public int syncMaterials() {
        int updatedCount = 0;
        try {// 从Oracle数据库获取所有物料信息
            List<Map<String, Object>> materialsFromOracle = materialMapper.listMaterials();

            // 将Oracle的物料信息转换为Goods对象
            List<Goods> goodsList = materialsFromOracle.stream()
                    .map(this::convertMaterialToGoods)
                    .collect(Collectors.toList());

            //批量保存到Mysql数据库
            updatedCount = saveGoodsToMySQL(goodsList);

        } catch (Exception e) {
            log.print("物料信息同步失败");
            throw e; // 抛出异常，由@Transactional管理事务回滚
        }
        return updatedCount;
    }
    @Transactional
    @DS("mysql")
    public int saveGoodsToMySQL(List<Goods> goodsList) {
        // 批量保存到MySQL数据库
        int updatedCount = 0;//用于记录更新的数据条数
        int batchSize = 50; // 设置每批处理的数据量
        for (int i = 0; i < goodsList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, goodsList.size());
            List<Goods> subList = goodsList.subList(i, end);
            for (Goods goods : subList) {
                Goods existingGoods = goodsMapper.findGoodsByCode(goods.getGoodscode());
                if (existingGoods != null) {
                    // 如果名称不同，则更新名称
                    if (!existingGoods.getName().equals(goods.getName())) {
                        existingGoods.setName(goods.getName());
                        goodsMapper.updateGoodsByCode(existingGoods);
                        updatedCount++;
                    }
                } else {
                    // 插入新的Goods信息
                    goodsMapper.insertBatchSomeColumn(Collections.singletonList(goods));
                    updatedCount++;
                }
            }
            //goodsMapper.insertBatchSomeColumn(subList);
        }
        return updatedCount;
    }

    /**
     * 将Material对象转换为Goods对象。
     *
     * @param material Oracle物料信息
     * @return 转换后的Goods对象
     */
    private Goods convertMaterialToGoods(Map<String, Object> material) {
        Goods goods = new Goods();
        goods.setGoodscode((String) material.get("materialCode"));
        goods.setName((String) material.get("materialName"));
        goods.setStorage(1); // 默认设置为物料仓库
        goods.setGoodstype(1); // 默认设置为“无”类别
        goods.setCount(0); // 默认数量设置为0

        return goods;
    }
}
