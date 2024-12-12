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

    // getter和setter方法
}
