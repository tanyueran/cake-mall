package com.github.tanyueran.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
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
@ApiModel(value = "CakeOrder对象", description = "蛋糕订单表")
public class CakeOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    @NotBlank(message = "订单主键不可为空")
    private String id;

    @ApiModelProperty(value = "蛋糕主键")
    @TableField("cake_product_id")
    @NotBlank(message = "蛋糕主键不可为空")
    private String cakeProductId;

    @ApiModelProperty(value = "下单人主键")
    @TableField("create_user_id")
    @NotBlank(message = "下单人主键不可为空")
    private String createUserId;

    @ApiModelProperty(value = "接单拒单操作人主键")
    @TableField("action_user_id")
    private String actionUserId;

    @ApiModelProperty(value = "订单状态(" +
            "0、已下单，未付款；" +
            "5、未付款，订单取消；" +
            "10、已付款，待发货；" +
            "15、已拒单，订单取消；" +
            "20、已接单，待配货；" +
            "30、已配送，待收货；" +
            "40、已收货，完成订单)下单后30分未付款，则取消订单；付款后30分钟未接单，则订单取消")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "订单创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "蛋糕个数")
    @TableField("number")
    @NotBlank(message = "蛋糕个数不可为空")
    private Integer number;

    @ApiModelProperty(value = "单价")
    @TableField("price")
    @NotBlank(message = "单价不可为空")
    private Double price;

    @ApiModelProperty(value = "总价")
    @TableField("total_price")
    private Double totalPrice;

    @ApiModelProperty(value = "未付款订单取消时间")
    @TableField("status5_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date status5Time;

    @ApiModelProperty(value = "付款时间")
    @TableField("status10_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date status10Time;

    @ApiModelProperty(value = "订单被拒时间")
    @TableField("status15_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date status15Time;

    @ApiModelProperty(value = "接单时间")
    @TableField("status20_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date status20Time;

    @ApiModelProperty(value = "发货时间")
    @TableField("status30_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date status30Time;

    @ApiModelProperty(value = "订单完成时间")
    @TableField("status40_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date status40Time;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;
}
