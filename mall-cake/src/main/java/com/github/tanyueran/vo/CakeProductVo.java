package com.github.tanyueran.vo;

import com.github.tanyueran.entity.CakeProduct;
import com.github.tanyueran.entity.CakeProductCategories;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("蛋糕产品vo")
public class CakeProductVo extends CakeProduct {

    @ApiModelProperty(value = "蛋糕图片")
    private List<String> cakeFiles;

    @ApiModelProperty(value = "类型")
    private CakeProductCategories cakeProductCategories;

}
