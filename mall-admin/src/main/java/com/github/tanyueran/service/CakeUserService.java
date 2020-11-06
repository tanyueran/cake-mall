package com.github.tanyueran.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.tanyueran.dto.*;
import com.github.tanyueran.entity.CakeUser;
import com.github.tanyueran.vo.CakeUserVo;
import com.github.tanyueran.vo.UserInfoVo;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
public interface CakeUserService extends IService<CakeUser> {

    /**
     * 普通用户登录
     *
     * @param loginDto
     * @return token 返回jwt的token值
     */
    String login(LoginDto loginDto) throws Exception;


    /**
     * 刷新token
     *
     * @param token
     * @return
     * @throws Exception
     */
    String refreshToken(String token) throws Exception;

    /**
     * 管理员登录
     *
     * @param loginDto
     * @return token 返回jwt的token值
     */
    String loginForManager(LoginDto loginDto) throws Exception;

    /**
     * 注册
     *
     * @param registerDto
     * @return
     * @throws Exception
     */
    Boolean register(RegisterDto registerDto) throws Exception;

    /**
     * 根据用户账号查询用户信息
     *
     * @param userCode 用户账号
     * @return
     * @throws Exception
     */
    UserInfoVo getUserInfoByUserCode(String userCode) throws Exception;

    /**
     * 添加用户
     *
     * @param cakeUser
     * @return
     */
    Boolean addUser(CakeUser cakeUser) throws Exception;

    /**
     * 编辑用户
     *
     * @param cakeUserEditDto
     * @return
     */
    Boolean editUser(CakeUserEditDto cakeUserEditDto) throws Exception;

    /**
     * 更新用户密码
     *
     * @param cakeUserUpdatePwdDto
     * @return
     * @throws Exception
     */
    Boolean updateUserPwd(CakeUserUpdatePwdDto cakeUserUpdatePwdDto) throws Exception;

    /**
     * 初始化用户密码
     *
     * @param userId
     * @return
     */
    Boolean initUserPwd(String userId);

    /**
     * 用户账号冻结
     *
     * @param userId
     * @return
     */
    Boolean freezeUser(String userId);

    /**
     * 用户账号解冻
     *
     * @param userId
     * @return
     */
    Boolean unfreezeUser(String userId);

    /**
     * 分页查询账户列表
     *
     * @param userPageQueryDto
     * @return
     */
    IPage<CakeUserVo> getUserByPage(UserPageQueryDto userPageQueryDto);

    /**
     * 添加金额
     *
     * @param addMoneyDto
     * @return
     */
    Boolean addMoney(AddMoneyDto addMoneyDto) throws Exception;

}
