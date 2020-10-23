package com.github.tanyueran.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 蛋糕订单表
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cake_order")
@ApiModel(value="CakeOrder对象", description="蛋糕订单表")
public class CakeOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "蛋糕主键")
    @TableField("cake_product_id")
    private Long cakeProductId;

    @ApiModelProperty(value = "下单人主键")
    @TableField("create_user_id")
    private Long createUserId;

    @ApiModelProperty(value = "接单拒单操作人主键")
    @TableField("action_user_id")
    private Long actionUserId;

    @ApiModelProperty(value = "订单状态(	            0、已下单，未付款；	            5、未付款，订单取消；	            10、已付款，待发货；	            15、已拒单，订单取消；	            20、已接单，待配货；	            30、已配送，待收货；	            40、已收货，完成订单)下单后30分未付款，则取消订单；付款后30分钟未接单，则订单取消")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "订单创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "蛋糕个数")
    @TableField("number")
    private Integer number;

    @ApiModelProperty(value = "单价")
    @TableField("price")
    private Double price;

    @ApiModelProperty(value = "总价")
    @TableField("total_price")
    private Double totalPrice;

    @ApiModelProperty(value = "未付款订单取消时间")
    @TableField("status5_time")
    private Date status5Time;

    @ApiModelProperty(value = "付款时间")
    @TableField("status10_time")
    private Date status10Time;

    @ApiModelProperty(value = "订单被拒时间")
    @TableField("status15_time")
    private Date status15Time;

    @ApiModelProperty(value = "接单时间")
    @TableField("status20_time")
    private Date status20Time;

    @ApiModelProperty(value = "发货时间")
    @TableField("status30_time")
    private Date status30Time;

    @ApiModelProperty(value = "订单完成时间")
    @TableField("status40_time")
    private Date status40Time;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;


}
