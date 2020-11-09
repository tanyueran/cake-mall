package com.github.tanyueran.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("添加订单dto")
public class CreateOrderDto {

    @ApiModelProperty("订单主键")
    @NotBlank(message = "订单主键不可为空")
    private String id;

    @ApiModelProperty("购买蛋糕主键")
    @NotBlank(message = "蛋糕主键不可为空")
    private String cakeId;

    @ApiModelProperty("购买的蛋糕数量")
    @NotNull(message = "蛋糕数量不可为空")
    private Integer number;

    @ApiModelProperty("购买人id")
    @NotBlank(message = "购买人主键不可为空")
    private String buyUserId;

    @ApiModelProperty(value = "备注")
    private String remark;

}
