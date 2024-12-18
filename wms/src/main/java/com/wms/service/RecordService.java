package com.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wms
 * @since 2022-10-16
 */
public interface RecordService extends IService<Record> {

    IPage pageCC(IPage<Record> page, Wrapper wrapper);

    boolean save(Record record);

    List<Record> findRecordsByGoodsIdAndDateRange(Integer goodsId, LocalDate startDate, LocalDate endDate);

    List<Record> findRecordsByGoodsAndDate(Integer goodsId, String startTime, String endTime);
}
