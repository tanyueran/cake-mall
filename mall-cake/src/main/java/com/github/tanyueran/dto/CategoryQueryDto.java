package com.github.tanyueran.dto;

import com.github.tanyueran.entity.PageQueryDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("蛋糕类型分页查询对象")
public class CategoryQueryDto extends PageQueryDto {

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("分类主键")
    private List<String> categoriesId;
}
