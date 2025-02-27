package com.wms.entity;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

@Data
public class RecordDTO {

    @ApiModelProperty(value = "物料编码")
    private String goodsCode;

    @ApiModelProperty(value = "出入库的数量")
    private Integer quantity;

    @ApiModelProperty(value = "出入库的金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "出入库类型")
    private String type; // "in" 或 "out"

    @ApiModelProperty(value = "操作人id")
    private Integer adminId; // 添加操作人ID属性

    @ApiModelProperty(value = "申请人id")
    private Integer userid; // 添加申请人ID属性
    // getter和setter方法
}
