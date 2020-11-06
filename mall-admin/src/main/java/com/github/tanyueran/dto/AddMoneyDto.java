package com.github.tanyueran.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("添加金额")
public class AddMoneyDto implements Serializable {
    @ApiModelProperty("账户id")
    @NotNull(message = "账户id 不可为空")
    private String cakeUserId;

    @ApiModelProperty("充值的金额")
    @NotNull(message = "充值金额不可为空")
    private Double money;
}
