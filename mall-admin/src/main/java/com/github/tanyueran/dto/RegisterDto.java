package com.github.tanyueran.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("注册dto")
public class RegisterDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "用户账号")
    @NotNull(message = "账号不可为空")
    private String userCode;

    @ApiModelProperty(value = "用户密码")
    @NotNull(message = "密码不可为空")
    private String userPwd;
}
