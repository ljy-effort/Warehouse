package com.wms.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wms.entity.Material;
import com.wms.mapper.oracle.MaterialMapper;
import com.wms.service.MaterialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@DS("oracle")
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {
    @Resource
    private MaterialMapper materialMapper;

    @Override
    public List<Map<String, Object>> listMaterials() {
        // 执行数据库查询
        return materialMapper.listMaterials();
    }
}