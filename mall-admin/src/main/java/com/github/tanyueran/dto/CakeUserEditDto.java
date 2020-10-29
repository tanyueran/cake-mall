package com.github.tanyueran.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("编辑对象dto")
public class CakeUserEditDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

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

}