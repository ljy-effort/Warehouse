package com.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.entity.MaterialSearchParam;
import com.wms.entity.Storage;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
public interface GoodsService extends IService<Goods> {
    IPage pageCC(IPage<Goods> page, Wrapper wrapper);
    Goods findGoodsByCode(String goodsCode);

    boolean addInventory(String goodsCode, int quantity, BigDecimal amount);

    boolean subtractInventory(String goodsCode, int quantity, BigDecimal amount);

    IPage<Map<String, Object>> searchGoods(IPage<?> page,@Param("params") MaterialSearchParam params);

    boolean updateGoods(Goods goods);

    boolean updateGoodsByCode(Goods goods);
}
