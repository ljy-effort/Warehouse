package com.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.entity.Material;

import java.util.List;
import java.util.Map;


public interface MaterialService extends IService<Material> {
    List<Map<String, Object>> listMaterials();
    List<Map<String, Object>> searchMaterials(String searchCode, String searchName);
}