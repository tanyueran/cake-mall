package com.github.tanyueran.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.dto.*;
import com.github.tanyueran.entity.CakeUser;
import com.github.tanyueran.entity.CakeUserRole;
import com.github.tanyueran.mapper.CakeUserMapper;
import com.github.tanyueran.mapper.CakeUserRoleMapper;
import com.github.tanyueran.service.CakeUserRoleService;
import com.github.tanyueran.service.CakeUserService;
import com.github.tanyueran.utils.JwtUtils;
import com.github.tanyueran.utils.MD5Utils;
import com.github.tanyueran.utils.RsaUtil;
import com.github.tanyueran.vo.UserInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
@Transactional
public class CakeUserServiceImpl extends ServiceImpl<CakeUserMapper, CakeUser> implements CakeUserService {

    @Resource
    private CakeUserMapper cakeUserMapper;

    @Resource
    private CakeUserRoleMapper cakeUserRoleMapper;

    @Resource
    private CakeUserRoleService cakeUserRoleService;

    // 初始化的密码值
    @Value("${self.password}")
    private String initPasswordStr;

    // redis账号信息的前缀
    @Value("${self.user_prefix}")
    private String redis_prefix;

    // 私钥
    @Value("${self.key.private}")
    private String privateKey;

    @Autowired
    private RedisTemplate redisTemplate;

    public String loginUtil(CakeUser user) throws Exception {
        user.setUserPwd(null);
        CakeUserRole role = cakeUserRoleMapper.selectById(user.getCakeUserRoleId());
        UserInfoVo infoVo = new UserInfoVo();
        infoVo.setCakeUser(user);
        infoVo.setCakeUserRole(role);
        // 保存到redis中
        redisTemplate.opsForValue().set(redis_prefix + user.getUserCode(), infoVo);
        // 返回token
        PrivateKey key = RsaUtil.getPrivateKey(this.privateKey);
        Map<String, String> map = new HashMap<>();
        map.put("userCode", user.getUserCode());
        // 过期时间
        Date future = new Date();
        future.setTime(new Date().getTime() + 24 * 60 * 60 * 1000);
        String token = JwtUtils.genToken(map, (RSAPrivateKey) key, future);
        return token;
    }

    @Override
    public String login(LoginDto loginDto) throws Exception {
        QueryWrapper<CakeUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_code", loginDto.getUsername());
        wrapper.eq("user_pwd", loginDto.getPassword());
        CakeUser user = cakeUserMapper.selectOne(wrapper);
        if (user == null) {
            throw new Exception("账号或密码错误");
        }
        // 判断账号是否被冻结
        if (user.getStatus() != 0) {
            throw new Exception("该账号已被冻结，请联系管理员！");
        }
        CakeUserRole role = cakeUserRoleService.getById(user.getCakeUserRoleId());
        if (!role.getRoleCode().equals("user")) {
            throw new Exception("您不是管理员无法登录管理系统");
        }
        return loginUtil(user);
    }

    @Override
    public String loginForManager(LoginDto loginDto) throws Exception {
        QueryWrapper<CakeUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_pwd", loginDto.getPassword());
        wrapper.eq("user_code", loginDto.getUsername());
        CakeUser user = cakeUserMapper.selectOne(wrapper);
        if (user == null) {
            throw new Exception("账号或密码错误");
        }
        CakeUserRole role = cakeUserRoleService.getById(user.getCakeUserRoleId());
        if (!role.getRoleCode().equals("manager")) {
            throw new Exception("您不是管理员无法登录管理系统");
        }
        return loginUtil(user);
    }


    @Override
    public Boolean register(RegisterDto registerDto) throws Exception {
        // 初始化角色id
        QueryWrapper<CakeUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code", "user");
        CakeUserRole role = cakeUserRoleService.getOne(wrapper);
        CakeUser cakeUser = new CakeUser();
        cakeUser.setId(registerDto.getId());
        cakeUser.setCakeUserRoleId(role.getId());
        cakeUser.setUserName(registerDto.getUserName());
        cakeUser.setNickname(registerDto.getNickname());
        cakeUser.setUserCode(registerDto.getUserCode());
        cakeUser.setUserPwd(registerDto.getUserPwd());
        cakeUser.setHeadImg(registerDto.getHeadImg());
        cakeUser.setMoney(0d);
        cakeUser.setStatus(0);
        return addUser(cakeUser);
    }

    @Override
    public UserInfoVo getUserInfoByUserCode(String userCode) throws Exception {
        UserInfoVo userInfoVo = (UserInfoVo) redisTemplate.opsForValue().get(redis_prefix + userCode);
        return userInfoVo;
    }

    @Override
    public Boolean addUser(CakeUser cakeUser) throws Exception {
        cakeUser.setStatus(0);
        // 检测账号是否可以使用
        QueryWrapper<CakeUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_code", cakeUser.getUserCode());
        CakeUser user = cakeUserMapper.selectOne(wrapper);
        if (user != null) {
            throw new Exception("账号已被使用，请更换");
        }
        cakeUser.setUserPwd(MD5Utils.encodeMD5Hex(initPasswordStr));
        int i = cakeUserMapper.insert(cakeUser);
        return i == 1;
    }

    @Override
    public Boolean editUser(CakeUserEditDto cakeUserEditDto) throws Exception {
        CakeUser cakeUser = new CakeUser();
        cakeUser.setStatus(0);
        cakeUser.setId(cakeUserEditDto.getId());
        cakeUser.setNickname(cakeUserEditDto.getNickname());
        cakeUser.setHeadImg(cakeUserEditDto.getHeadImg());
        cakeUser.setUserCode(cakeUserEditDto.getUserCode());
        cakeUser.setUserName(cakeUserEditDto.getUserName());
        cakeUser.setRemark(cakeUserEditDto.getRemark());
        CakeUser userInDB = cakeUserMapper.selectById(cakeUser.getId());
        if (!userInDB.getUserCode().equals(cakeUser.getUserCode())) {
            // 检测账号是否可以使用
            QueryWrapper<CakeUser> wrapper = new QueryWrapper<>();
            wrapper.eq("user_code", cakeUser.getUserCode());
            CakeUser user = cakeUserMapper.selectOne(wrapper);
            if (user != null) {
                throw new Exception("账号已被使用，请更换");
            }
        }
        int i = cakeUserMapper.updateById(cakeUser);
        Boolean isOk = i == 1;
        // 更新redis 的内容
        UserInfoVo vo = (UserInfoVo) redisTemplate.opsForValue().get(redis_prefix + cakeUserEditDto.getUserCode());
        if (vo != null) {
            redisTemplate.delete(redis_prefix + cakeUserEditDto.getUserCode());
            CakeUser user = vo.getCakeUser();
            user.setUserName(cakeUserEditDto.getUserName());
            user.setUserCode(cakeUserEditDto.getUserCode());
            user.setHeadImg(cakeUserEditDto.getHeadImg());
            user.setNickname(cakeUserEditDto.getNickname());
            redisTemplate.opsForValue().set(redis_prefix + cakeUserEditDto.getUserCode(), vo);
        }
        return isOk;
    }

    @Override
    public Boolean updateUserPwd(CakeUserUpdatePwdDto cakeUserUpdatePwdDto) throws Exception {
        if (!cakeUserUpdatePwdDto.getNewUserPwd().equals(cakeUserUpdatePwdDto.getNewUserPwd2())) {
            throw new Exception("两次密码输入不一致");
        }
        UpdateWrapper<CakeUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("id", cakeUserUpdatePwdDto.getId())
                .eq("user_pwd", cakeUserUpdatePwdDto.getOldUserPwd());
        CakeUser cakeUser = new CakeUser();
        cakeUser.setUserPwd(cakeUserUpdatePwdDto.getNewUserPwd());
        int i = cakeUserMapper.update(cakeUser, updateWrapper);
        return i == 1;
    }

    @Override
    public Boolean initUserPwd(String userId) {
        CakeUser cakeUser = new CakeUser();
        cakeUser.setId(userId);
        cakeUser.setUserPwd(MD5Utils.encodeMD5Hex(initPasswordStr));
        int i = cakeUserMapper.updateById(cakeUser);
        return i == 1;
    }

    @Override
    public Boolean freezeUser(String userId) {
        CakeUser cakeUser = new CakeUser();
        cakeUser.setId(userId);
        cakeUser.setStatus(1);
        int i = cakeUserMapper.updateById(cakeUser);
        return i == 1;
    }

    @Override
    public Boolean unfreezeUser(String userId) {
        CakeUser cakeUser = new CakeUser();
        cakeUser.setId(userId);
        cakeUser.setStatus(0);
        int i = cakeUserMapper.updateById(cakeUser);
        return i == 1;
    }

    @Override
    public IPage<CakeUser> getUserByPage(UserPageQueryDto userPageQueryDto) {
        IPage<CakeUser> page = new Page<>();
        page.setCurrent(userPageQueryDto.getPage())
                .setSize(userPageQueryDto.getSize());
        QueryWrapper<CakeUser> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isEmpty(userPageQueryDto.getKeyword())) {
            userPageQueryDto.setKeyword("");
        }
        if ((userPageQueryDto.getStatus() != null) &&
                (userPageQueryDto.getStatus().size() > 0)) {
            queryWrapper.in("status", userPageQueryDto.getStatus());
        }
        queryWrapper.and(e -> e.like("nickname", userPageQueryDto.getKeyword())
                .or()
                .like("user_name", userPageQueryDto.getKeyword())
                .or()
                .like("user_code", userPageQueryDto.getKeyword()));
        cakeUserMapper.selectPage(page, queryWrapper);
        List<CakeUser> collect = page.getRecords().stream().map(user -> {
            user.setUserPwd(null);
            return user;
        }).collect(Collectors.toList());
        page.setRecords(collect);
        return page;
    }
}
