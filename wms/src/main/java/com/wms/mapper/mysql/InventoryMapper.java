package com.wms.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
}