package com.github.tanyueran.vo;

import com.github.tanyueran.entity.CakeUser;
import com.github.tanyueran.entity.CakeUserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户信息对象")
public class UserInfoVo {
    @ApiModelProperty("用户")
    private CakeUser cakeUser;
    @ApiModelProperty("用户角色")
    private CakeUserRole cakeUserRole;
}
