package com.wms.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wms.mapper.oracle.MaterialMapper;
import com.wms.mapper.mysql.GoodsMapper;
import com.wms.entity.Goods;
import com.wms.service.MaterialSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaterialSyncServiceImpl implements MaterialSyncService {


    @Autowired

    private MaterialMapper materialMapper; // Oracle数据库的Mapper

    @Autowired
    private GoodsMapper goodsMapper; // MySQL数据库的Mapper

    /**
     * 同步物料信息从Oracle到MySQL数据库。
     */
    @DS("oracle")
    @Transactional
    @Override
    public void syncMaterials() {
        // 从Oracle数据库获取所有物料信息
        List<Map<String, Object>> materialsFromOracle = materialMapper.listMaterials();

        // 将Oracle的物料信息转换为Goods对象
        List<Goods> goodsList = materialsFromOracle.stream()
                .map(this::convertMaterialToGoods)
                .collect(Collectors.toList());


        // 批量保存到MySQL数据库
        int batchSize = 50; // 设置每批处理的数据量
        for (int i = 0; i < 20; i += batchSize) {
            int end = Math.min(i + batchSize, goodsList.size());
            List<Goods> subList = goodsList.subList(i, end);
            goodsMapper.insertBatchSomeColumn(subList);
        }
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
        goods.setStorage(8); // 默认设置为物料仓库
        goods.setGoodstype(4); // 默认设置为“无”类别
        goods.setCount(0); // 默认数量设置为0

        return goods;
    }
}
