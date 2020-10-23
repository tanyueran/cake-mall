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
 * 蛋糕表
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cake_product")
@ApiModel(value="CakeProduct对象", description="蛋糕表")
public class CakeProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "分类id")
    @TableField("cake_product_categories_id")
    private Long cakeProductCategoriesId;

    @ApiModelProperty(value = "蛋糕名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "蛋糕图片英文逗号分割，最多五个")
    @TableField("cake_imgs")
    private String cakeImgs;

    @ApiModelProperty(value = "蛋糕详情")
    @TableField("detail")
    private String detail;

    @ApiModelProperty(value = "删除状态(0未删除，1删除)")
    @TableField("delete_status")
    private Integer deleteStatus;

    @ApiModelProperty(value = "是否推荐状态（0不推荐，1推荐）")
    @TableField("recommend_status")
    private Integer recommendStatus;

    @ApiModelProperty(value = "蛋糕价格")
    @TableField("price")
    private Double price;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;


}
