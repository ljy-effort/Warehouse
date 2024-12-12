package com.wms.common;

import com.wms.service.MaterialSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MaterialSyncScheduler {

    @Autowired
    private MaterialSyncService materialSyncService;

    // 每天凌晨1点执行同步任务
    @Scheduled(cron = "0 0 1 * * ?")
    public void syncMaterials() {
        materialSyncService.syncMaterials();
    }
}