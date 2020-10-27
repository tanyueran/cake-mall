package com.github.tanyueran.controller;


import com.github.tanyueran.dto.LoginDto;
import com.github.tanyueran.dto.RegisterDto;
import com.github.tanyueran.entity.CakeUser;
import com.github.tanyueran.service.CakeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@RestController
@RequestMapping("/user")
@Api(tags = "CakeUserController", description = "用户操作模块")
public class CakeUserController {

    @Resource
    private CakeUserService cakeUserService;

    @PostMapping("/login")
    @ApiOperation("登录")
    public String login(@RequestBody @Valid LoginDto loginDto) throws Exception {
        return cakeUserService.login(loginDto);
    }

    @PostMapping("/register")
    @ApiOperation("注册")
    // 注意：注册只能注册普通用户
    public Boolean register(@RequestBody @Valid RegisterDto registerDto) throws Exception {
        return cakeUserService.register(registerDto);
    }

    @PostMapping("/add")
    @ApiOperation("添加用户")
    public Boolean addUser(@RequestBody @Valid CakeUser cakeUser) throws Exception {
        return cakeUserService.addUser(cakeUser);
    }

    @PutMapping("/edit")
    @ApiOperation("编辑用户")
    public Boolean editUser(@RequestBody @Valid CakeUser cakeUser) throws Exception {
        return cakeUserService.editUser(cakeUser);
    }

    @PutMapping("/initPwd/{id}")
    @ApiOperation("初始化用户账号")
    public Boolean initUserPwd(@PathVariable("id") Long id) {
        return cakeUserService.initUserPwd(id);
    }

    @PutMapping("/freeze/{id}")
    @ApiOperation("冻结账号")
    public Boolean freezeUser(@PathVariable("id") Long id) {
        return cakeUserService.freezeUser(id);
    }
}