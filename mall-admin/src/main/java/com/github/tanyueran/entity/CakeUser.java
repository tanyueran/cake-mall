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
 *
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cake_user")
@ApiModel(value = "CakeUser对象", description = "")
public class CakeUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "角色主键")
    @TableField("cake_user_role_id")
    @NotNull(message = "角色不可为空")
    private String cakeUserRoleId;

    @ApiModelProperty(value = "昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "用户姓名")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "头像")
    @TableField("head_img")
    private String headImg;

    @ApiModelProperty(value = "用户账号")
    @TableField("user_code")
    @NotNull(message = "账号不可为空")
    private String userCode;

    @ApiModelProperty(value = "用户密码")
    @TableField("user_pwd")
    private String userPwd;

    @ApiModelProperty(value = "账户金额")
    @TableField("money")
    private Double money;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @TableField("status")
    @ApiModelProperty(value = "激活状态（0激活，1冻结）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
