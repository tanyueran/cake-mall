package com.github.tanyueran.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("付款对象")
public class PayDto {
    @ApiModelProperty("付款人主键")
    @NotBlank(message = "付款人主键不可为空")
    private String cakeUserId;

    @ApiModelProperty("付款金额")
    @NotBlank(message = "付款金额不可为空")
    private Double payPrice;
}

