package com.github.tanyueran.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("个人订单数据总和")
public class OrderCollcetionInfoVo {
    @ApiModelProperty("个人花费的资金总和")
    private Double totalMoney;

    @ApiModelProperty("个人订单总和，含取消的订单")
    private Integer totalOrderNumber;
}
