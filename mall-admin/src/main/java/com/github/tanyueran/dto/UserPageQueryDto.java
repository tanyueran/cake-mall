package com.github.tanyueran.dto;

import com.github.tanyueran.entity.PageQueryDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("用户列表查询对象")
public class UserPageQueryDto extends PageQueryDto {

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("激活状态（0激活，1冻结）")
    private List<Integer> status;

    @ApiModelProperty("角色id集合")
    private List<String> cakeUserRoleId;

}
