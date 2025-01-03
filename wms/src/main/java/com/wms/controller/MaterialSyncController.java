package com.wms.controller;


import com.wms.common.Result;
import com.wms.service.MaterialSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material-sync")
public class MaterialSyncController {

    @Autowired
    private MaterialSyncService materialSyncService;

    @PostMapping("/trigger-sync")
    public ResponseEntity<?> triggerSync() {
        try {
            int updatedCount = materialSyncService.syncMaterials();
            // 使用Result类的静态方法来创建成功响应
            return ResponseEntity.ok(Result.suc(updatedCount, (long) updatedCount));
        } catch (Exception e) {
            // 使用Result类的静态方法来创建失败响应
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.fail("同步操作失败: " + e.getMessage()));
        }
    }
}
