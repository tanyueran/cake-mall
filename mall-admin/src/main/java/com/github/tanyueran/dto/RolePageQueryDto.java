package com.github.tanyueran.dto;

import com.github.tanyueran.entity.PageQueryDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("角色分页查询对象")
public class RolePageQueryDto extends PageQueryDto {

    @ApiModelProperty("关键字")
    private String keyword;
}
