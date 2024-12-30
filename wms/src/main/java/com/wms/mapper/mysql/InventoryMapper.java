package com.wms.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InventoryMapper extends BaseMapper<InventorySearchResult> {
    List<Map<String, Object>> findRecordsByGoodsAndDate(@Param("goodsId") Integer goodsId,
                                        @Param("startTime") String startTime,
                                        @Param("endTime") String endTime);

    List<Material> getAllMaterials();

    List<Map<String, Object>> findRecordsForAllMaterials(@Param("startTime") String startTime,@Param("endTime") String endTime);

    // 获取所有物料的出入库记录以及物料编码、名称和库存数量
    List<Map<String, Object>> findRecordsWithGoodsInfo(@Param("startTime") String startTime, @Param("endTime") String endTime);

    Map<String, Object> getGoodsInfo(@Param("goodsId") Integer goodsId);

    List<RecordDetail> findRecordsByGoodsAndDateWithDetails(@Param("goodsId") Integer goodsId,
                                                            @Param("startTime") String startTime,
                                                            @Param("endTime") String endTime);

}