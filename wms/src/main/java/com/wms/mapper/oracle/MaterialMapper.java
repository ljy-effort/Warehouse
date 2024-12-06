package com.wms.mapper.oracle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.entity.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MaterialMapper extends BaseMapper<Material> {
     List<Map<String, Object>> listMaterials();
}