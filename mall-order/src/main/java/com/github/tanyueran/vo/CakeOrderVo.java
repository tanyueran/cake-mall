package com.github.tanyueran.vo;

import com.github.tanyueran.entity.CakeOrder;
import com.github.tanyueran.entity.CakeProduct;
import com.github.tanyueran.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单vo")
public class CakeOrderVo extends CakeOrder {

    @ApiModelProperty("蛋糕对象")
    private CakeProduct cakeProduct;

    @ApiModelProperty("购买人")
    private User buyUser;
}
