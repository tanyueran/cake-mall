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

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 蛋糕表
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cake_product")
@ApiModel(value = "CakeProduct对象", description = "蛋糕表")
public class CakeProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "分类id")
    @TableField("cake_product_categories_id")
    @NotNull(message = "类型不可为空")
    private String cakeProductCategoriesId;

    @ApiModelProperty(value = "蛋糕名称")
    @TableField("name")
    @NotNull(message = "蛋糕名称不可为空")
    private String name;

    @ApiModelProperty(value = "蛋糕图片英文逗号分割，最多五个")
    @TableField("cake_imgs")
    @NotNull(message = "蛋糕图片不可为空")
    private String cakeImgs;

    @ApiModelProperty(value = "蛋糕详情")
    @TableField("detail")
    @NotNull(message = "蛋糕详情不可为空")
    private String detail;

    @ApiModelProperty(value = "删除状态(0未删除，1删除)")
    @TableField("delete_status")
    private Integer deleteStatus;

    @ApiModelProperty(value = "是否推荐状态（0不推荐，1推荐）")
    @TableField("recommend_status")
    private Integer recommendStatus;

    @ApiModelProperty(value = "蛋糕价格")
    @TableField("price")
    @NotNull(message = "蛋糕价格不可为空")
    private Double price;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
