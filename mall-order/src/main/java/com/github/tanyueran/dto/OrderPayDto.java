package com.github.tanyueran.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("订单付款")
public class OrderPayDto {

    @ApiModelProperty("付款信息")
    @NotNull(message = "付款信息不可为空")
    private PayDto payDto;

    @ApiModelProperty("订单id")
    @NotNull(message = "订单id不可为空")
    private String orderId;
}
