package com.wms.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.entity.InventorySearchResult;
import com.wms.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InventoryMapper extends BaseMapper<InventorySearchResult> {
    List<Map<String, Object>> findRecordsByGoodsAndDate(@Param("goodsId") Integer goodsId,
                                        @Param("startTime") String startTime,
                                        @Param("endTime") String endTime);
}