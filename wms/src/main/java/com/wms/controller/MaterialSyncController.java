package com.wms.controller;

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
            materialSyncService.syncMaterials();
            return ResponseEntity.ok("同步操作已触发");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("同步操作失败: " + e.getMessage());
        }
    }
}
