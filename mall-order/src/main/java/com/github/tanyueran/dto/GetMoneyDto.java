package com.github.tanyueran.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("收钱dtl")
public class GetMoneyDto {
    @ApiModelProperty("收款人账号")
    @NotBlank(message = "收款人账号不可为空")
    private String userId;

    @ApiModelProperty("收款金额")
    @NotNull(message = "收款金额不可为空")
    private Double money;
}
