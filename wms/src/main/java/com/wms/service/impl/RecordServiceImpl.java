package com.wms.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.entity.Record;
import com.wms.mapper.mysql.RecordMapper;
import com.wms.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wms
 * @since 2022-10-16
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Resource
    private RecordMapper recordMapper;
    @Override
    public IPage pageCC(IPage<Record> page, Wrapper wrapper) {
        return recordMapper.pageCC(page,wrapper);
    }

    @Override
    public List<Record> findRecordsByGoodsIdAndDateRange(Integer goodsId, LocalDate startDate, LocalDate endDate) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods", goodsId)
                .ge("createtime", startDate)
                .le("createtime", endDate);
        return list(queryWrapper);
    }
    @Override
    public boolean save(Record record) {
        // 保存记录前进行数据校验
        if (record.getGoods() == null || record.getCount() == null || record.getAmount() == null) {
            throw new RuntimeException("保存记录失败，商品ID、数量、金额不能为空");
        }
        // 直接调用ServiceImpl的save方法
        return baseMapper.insert(record) > 0;
    }


}
