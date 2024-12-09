package com.wms.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
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
    public List<Map<String, Object>> searchMaterials(@RequestParam("searchCode") String searchCode, @RequestParam("searchName") String searchName) {
        return materialService.searchMaterials(searchCode, searchName);
    }

    @GetMapping("/list")
    public List<Map<String, Object>> listMaterials() {
        return materialService.listMaterials();
    }
}