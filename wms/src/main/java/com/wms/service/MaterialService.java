package com.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.entity.Material;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wms.entity.MaterialSearchParam;

import java.util.List;
import java.util.Map;


import java.util.List;
import java.util.Map;


public interface MaterialService extends IService<Material> {
    IPage<Map<String, Object>> pageCC(IPage<?> page, Wrapper<?> wrapper);

    List<Map<String, Object>> listMaterials();
    IPage<Map<String, Object>> searchMaterials(IPage<?> page, MaterialSearchParam params);
}