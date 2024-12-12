package com.wms.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.entity.Material;
import com.wms.entity.MaterialSearchParam;
import com.wms.mapper.oracle.MaterialMapper;
import com.wms.service.MaterialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.val;
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
    public IPage<Map<String, Object>> pageCC(IPage<?> page, Wrapper<?> wrapper) {
        return materialMapper.pageCC(page, wrapper);
    }

    @Override
    public List<Map<String, Object>> listMaterials() {
        // 执行数据库查询
        return baseMapper.listMaterials();
    }

    @Override
    public IPage<Map<String, Object>> searchMaterials(IPage<?> page, MaterialSearchParam params) {
//        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("SEGMENT1", searchCode).or().like("DESCRIPTION", searchName);
//        MaterialSearchParam params = new MaterialSearchParam();
//        params.setSearchCode(searchCode);
//        params.setSearchName(searchName);
        return materialMapper.searchMaterials(page, params);
    }
}