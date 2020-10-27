package com.github.tanyueran.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("登录对象")
public class LoginDto {
    @ApiModelProperty("账号")
    @NotNull(message = "账号不可为空")
    private String username;
    @ApiModelProperty("密码")
    @NotNull(message = "密码不可为空")
    private String password;
}
