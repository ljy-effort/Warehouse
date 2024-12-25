package com.wms.mapper.mysql;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wms.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.entity.MaterialSearchParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
@Mapper
@DS("mysql")
public interface GoodsMapper extends BaseMapper<Goods> {
    IPage<Map<String, Object>> pageCC(IPage<?> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    //t添加批量保存方法
    @DS("mysql")
    int insertBatchSomeColumn(List <Goods> list);
    // 根据物料编码查询商品

    Goods findGoodsByCode(@Param("goodsCode") String goodsCode);

    IPage<Map<String, Object>> searchGoods(IPage<?> page, @Param("params") MaterialSearchParam params);
}
