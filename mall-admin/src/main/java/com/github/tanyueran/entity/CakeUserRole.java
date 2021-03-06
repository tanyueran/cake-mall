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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cake_user_role")
@ApiModel(value = "CakeUserRole对象", description = "")
public class CakeUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    @NotNull(message = "id不可为空")
    private String id;

    @ApiModelProperty(value = "角色code")
    @TableField("role_code")
    @NotBlank(message = "角色code不可为空")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    @TableField("role_name")
    @NotBlank(message = "角色名称不可为空")
    private String roleName;

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
