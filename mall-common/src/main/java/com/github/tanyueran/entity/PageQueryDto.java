package com.github.tanyueran.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("分页查询对象")
@Data
public class PageQueryDto {
    @ApiModelProperty("第几页")
    @NotNull(message = "查询的页号不可为空")
    private Integer page;

    @ApiModelProperty("页容量")
    @NotNull(message = "查询的页容量不可为空")
    private Integer size;

}
