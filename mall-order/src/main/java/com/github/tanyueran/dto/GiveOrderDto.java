package com.github.tanyueran.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("接受订单")
public class GiveOrderDto {

    @ApiModelProperty("操作人id")
    @NotBlank(message = "操作人id不可为空")
    private String userId;

    @ApiModelProperty("订单id")
    @NotBlank(message = "订单id不可为空")
    private String orderId;
}
