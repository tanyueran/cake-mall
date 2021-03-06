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

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单消息表
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cake_news")
@ApiModel(value="CakeNews对象", description="订单消息表")
public class CakeNews implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "发送方id")
    @TableField("sender_id")
    private String senderId;

    @ApiModelProperty(value = "接受方id")
    @TableField("receiver_id")
    private String receiverId;

    @ApiModelProperty(value = "消息简介")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "消息内容")
    @TableField("message")
    private String message;

    @ApiModelProperty(value = "是否已读（0未读、1已读）")
    @TableField("is_read")
    private Integer isRead;

    @ApiModelProperty(value = "消息发送时间")
    @TableField("create_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "消息已读时间")
    @TableField("update_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
