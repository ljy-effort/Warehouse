package com.wms.mapper.oracle;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wms.entity.Material;
import com.wms.entity.MaterialSearchParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;



import java.util.List;
import java.util.Map;

@Mapper
@DS("oracle")
public interface MaterialMapper extends BaseMapper<Material> {

     IPage<Map<String, Object>> pageCC(IPage<?> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

     IPage<Map<String, Object>> searchMaterials(IPage<?> page, @Param("params") MaterialSearchParam params);


     List<Map<String, Object>> listMaterials();
}