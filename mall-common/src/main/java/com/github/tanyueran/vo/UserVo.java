package com.github.tanyueran.vo;

import com.github.tanyueran.entity.User;
import com.github.tanyueran.entity.UserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户信息对象")
public class UserVo {
    @ApiModelProperty("用户")
    private User user;
    @ApiModelProperty("用户角色")
    private UserRole userRole;
}