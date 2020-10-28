package com.github.tanyueran.controller;


import com.github.tanyueran.dto.LoginDto;
import com.github.tanyueran.dto.RegisterDto;
import com.github.tanyueran.entity.CakeUser;
import com.github.tanyueran.service.CakeUserService;
import com.github.tanyueran.utils.JwtUtils;
import com.github.tanyueran.utils.RsaUtil;
import com.github.tanyueran.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

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
@Slf4j
public class CakeUserController {

    @Resource
    private CakeUserService cakeUserService;

    @Value("${self.key.public}")
    private String publicKey;

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

    @GetMapping("/getInfo")
    @ApiOperation("获取用户信息")
    public UserInfoVo getUserInfo(HttpServletRequest request) throws Exception {
        log.info(publicKey);
        String token = request.getHeader("token");
        Map<String, String> map = JwtUtils.verifyToken(token, (RSAPublicKey) RsaUtil.getPublicKey(publicKey));
        String userCode = map.get("userCode");
        UserInfoVo userInfo = cakeUserService.getUserInfoByUserCode(userCode);
        return userInfo;
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
    public Boolean initUserPwd(@PathVariable("id") String id) {
        return cakeUserService.initUserPwd(id);
    }

    @PutMapping("/freeze/{id}")
    @ApiOperation("冻结账号")
    public Boolean freezeUser(@PathVariable("id") String id) {
        return cakeUserService.freezeUser(id);
    }
}