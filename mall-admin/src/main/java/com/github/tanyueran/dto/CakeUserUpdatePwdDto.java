package com.github.tanyueran.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("密码更新对象")
public class CakeUserUpdatePwdDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "密码不可为空")
    private String id;

    @ApiModelProperty(value = "用户旧密码")
    @NotNull(message = "密码不可为空")
    private String oldUserPwd;

    @ApiModelProperty(value = "用户新密码")
    @NotNull(message = "密码不可为空")
    private String newUserPwd;

    @ApiModelProperty(value = "确认密码")
    @NotNull(message = "确认密码不可为空")
    private String newUserPwd2;

}
