package com.github.tanyueran.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(value="CakeUser对象", description="")
public class CakeUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "角色主键")
    @TableField("cake_user_role_id")
    private Long cakeUserRoleId;

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

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;


}
