package com.wms.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.Material;
import com.wms.entity.MaterialSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wms.service.MaterialService;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/search")
    public IPage<Map<String, Object>> searchMaterials(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      MaterialSearchParam params) {
        Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
        return materialService.searchMaterials(page, params);
    }

    @GetMapping("/list")
    public List<Map<String, Object>> listMaterials() {
        return materialService.listMaterials();
    }

    @GetMapping("/listPage")
    public IPage<Map<String, Object>> listMaterials(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
        return materialService.pageCC(page, queryWrapper);
    }
}