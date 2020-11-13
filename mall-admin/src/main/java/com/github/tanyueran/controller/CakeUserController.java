package com.github.tanyueran.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.tanyueran.dto.*;
import com.github.tanyueran.entity.CakeUser;
import com.github.tanyueran.service.CakeUserService;
import com.github.tanyueran.utils.JwtUtils;
import com.github.tanyueran.utils.RsaUtil;
import com.github.tanyueran.vo.CakeUserVo;
import com.github.tanyueran.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/refreshToken")
    @ApiOperation("刷新token")
    @PreAuthorize("hasRole('user')")
    public String refreshToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        return cakeUserService.refreshToken(token);
    }

    @PostMapping("/aLogin")
    @ApiOperation("管理平台登录")
    public String aLogin(@RequestBody @Valid LoginDto loginDto) throws Exception {
        return cakeUserService.loginForManager(loginDto);
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
        String token = request.getHeader("token");
        Map<String, String> map = JwtUtils.verifyToken(token, (RSAPublicKey) RsaUtil.getPublicKey(publicKey));
        String userCode = map.get("userCode");
        UserInfoVo userInfo = cakeUserService.getUserInfoByUserCode(userCode);
        return userInfo;
    }

    @PostMapping("/add")
    @ApiOperation("添加用户")
    @PreAuthorize("hasRole('manager')")
    public Boolean addUser(@RequestBody @Valid CakeUser cakeUser) throws Exception {
        return cakeUserService.addUser(cakeUser);
    }

    @PutMapping("/edit")
    @ApiOperation("编辑用户")
    public Boolean editUser(@RequestBody @Valid CakeUserEditDto cakeUser) throws Exception {
        return cakeUserService.editUser(cakeUser);
    }

    @PutMapping("/initPwd/{id}")
    @ApiOperation("初始化用户账号")
    @PreAuthorize("hasRole('manager')")
    public Boolean initUserPwd(@PathVariable("id") String id) {
        return cakeUserService.initUserPwd(id);
    }

    @PostMapping("/updatePwd")
    @ApiOperation("更新密码")
    public Boolean updatePwd(@RequestBody @Valid CakeUserUpdatePwdDto cakeUserUpdatePwdDto) throws Exception {
        return cakeUserService.updateUserPwd(cakeUserUpdatePwdDto);
    }

    @PutMapping("/freeze/{id}")
    @ApiOperation("冻结账号")
    @PreAuthorize("hasRole('manager')")
    public Boolean freezeUser(@PathVariable("id") String id) {
        return cakeUserService.freezeUser(id);
    }

    @PutMapping("/unfreeze/{id}")
    @ApiOperation("解冻账号")
    @PreAuthorize("hasRole('manager')")
    public Boolean unfreezeUser(@PathVariable("id") String id) {
        return cakeUserService.unfreezeUser(id);
    }

    @ApiOperation("分页查询用户数据")
    @PostMapping("/getByPage")
    @PreAuthorize("hasRole('manager')")
    public IPage<CakeUserVo> getUserListByPage(@RequestBody @Valid UserPageQueryDto userPageQueryDto) {
        return cakeUserService.getUserByPage(userPageQueryDto);
    }

    @ApiOperation("充值金额")
    @PostMapping("/addMoney")
    @PreAuthorize("hasRole('manager')")
    public Boolean addMoney(@RequestBody @Valid AddMoneyDto addMoneyDto) throws Exception {
        return cakeUserService.addMoney(addMoneyDto);
    }

    @ApiOperation("付钱")
    @PostMapping("/pay")
    @PreAuthorize("hasRole('user')")
    public Boolean pay(@RequestBody @Valid PayDto payDto) throws Exception {
        return cakeUserService.pay(payDto);
    }


    @ApiOperation("收款")
    @PostMapping("/getMoney")
    public Boolean getMoney(@RequestBody @Valid GetMoneyDto getMoneyDto) throws Exception {
        return cakeUserService.getMoney(getMoneyDto);
    }

}