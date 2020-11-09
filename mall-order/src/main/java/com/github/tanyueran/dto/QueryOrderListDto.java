package com.github.tanyueran.dto;

import com.github.tanyueran.entity.PageQueryDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页查询")
public class QueryOrderListDto extends PageQueryDto {

    @ApiModelProperty("状态")
    private Integer status;

}
