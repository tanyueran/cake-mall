package com.github.tanyueran.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.dto.*;
import com.github.tanyueran.entity.CakeUser;
import com.github.tanyueran.entity.CakeUserRole;
import com.github.tanyueran.entity.User;
import com.github.tanyueran.entity.UserRole;
import com.github.tanyueran.mapper.CakeUserMapper;
import com.github.tanyueran.mapper.CakeUserRoleMapper;
import com.github.tanyueran.service.CakeUserRoleService;
import com.github.tanyueran.service.CakeUserService;
import com.github.tanyueran.utils.JwtUtils;
import com.github.tanyueran.utils.MD5Utils;
import com.github.tanyueran.utils.RsaUtil;
import com.github.tanyueran.vo.CakeUserVo;
import com.github.tanyueran.vo.UserInfoVo;
import com.github.tanyueran.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
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
    @Value("${self.key.public}")
    private String publicKey;

    @Autowired
    private RedisTemplate redisTemplate;

    public String loginUtil(CakeUser user) throws Exception {
        user.setUserPwd(null);
        CakeUserRole role = cakeUserRoleMapper.selectById(user.getCakeUserRoleId());
        UserVo userVo = new UserVo();
        User u = JSONObject.parseObject(JSONObject.toJSONString(user), User.class);
        UserRole r = JSONObject.parseObject(JSONObject.toJSONString(role), UserRole.class);
        userVo.setUser(u);
        userVo.setUserRole(r);
        // 保存到redis中
        redisTemplate.opsForValue().set(redis_prefix + user.getUserCode(), userVo);
        // 返回token
        PrivateKey key = RsaUtil.getPrivateKey(this.privateKey);
        Map<String, String> map = new HashMap<>();
        map.put("userCode", user.getUserCode());
        // 过期时间
        Date future = new Date();
        future.setTime(new Date().getTime() + 48 * 60 * 60 * 1000);
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
            throw new Exception("管理员不可登录此系统");
        }
        return loginUtil(user);
    }

    @Override
    public String refreshToken(String token) throws Exception {
        Map<String, String> map = null;
        try {
            map = JwtUtils.verifyToken(token, (RSAPublicKey) RsaUtil.getPublicKey(publicKey));
        } catch (Exception e) {
            log.error("刷新token时，解析token出错：", e);
            throw new Exception("token 无效");
        }
        String userCode = map.get("userCode");
        // 返回token
        PrivateKey key = RsaUtil.getPrivateKey(this.privateKey);
        Map<String, String> m = new HashMap<>();
        m.put("userCode", userCode);
        // 过期时间
        Date future = new Date();
        future.setTime(new Date().getTime() + 48 * 60 * 60 * 1000);
        String newToken = JwtUtils.genToken(m, (RSAPrivateKey) key, future);
        return newToken;
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
        UserVo userVo = (UserVo) redisTemplate.opsForValue().get(redis_prefix + userCode);
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setCakeUser(JSONObject.parseObject(JSONObject.toJSONString(userVo.getUser()), CakeUser.class));
        userInfoVo.setCakeUserRole(JSONObject.parseObject(JSONObject.toJSONString(userVo.getUserRole()), CakeUserRole.class));
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
        UserVo vo = (UserVo) redisTemplate.opsForValue().get(redis_prefix + cakeUserEditDto.getUserCode());

        if (vo != null) {
            redisTemplate.delete(redis_prefix + cakeUserEditDto.getUserCode());
            User user = vo.getUser();
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
    public IPage<CakeUserVo> getUserByPage(UserPageQueryDto userPageQueryDto) {
        IPage<CakeUser> page = new Page<>();
        page.setCurrent(userPageQueryDto.getPage())
                .setSize(userPageQueryDto.getSize());
        QueryWrapper<CakeUser> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isEmpty(userPageQueryDto.getKeyword())) {
            userPageQueryDto.setKeyword("");
        }
        if (((userPageQueryDto.getStatus() != null) && (userPageQueryDto.getStatus().size() > 0))
                && (userPageQueryDto.getCakeUserRoleId() != null && userPageQueryDto.getCakeUserRoleId().size() > 0)) {
            queryWrapper.in("status", userPageQueryDto.getStatus()).and(e -> e.in("cake_user_role_id", userPageQueryDto.getCakeUserRoleId()));
        } else if (((userPageQueryDto.getStatus() != null) && (userPageQueryDto.getStatus().size() > 0)) && !(userPageQueryDto.getCakeUserRoleId() != null && userPageQueryDto.getCakeUserRoleId().size() > 0)) {
            queryWrapper.in("status", userPageQueryDto.getStatus());
        } else if (!((userPageQueryDto.getStatus() != null) && (userPageQueryDto.getStatus().size() > 0)) && (userPageQueryDto.getCakeUserRoleId() != null && userPageQueryDto.getCakeUserRoleId().size() > 0)) {
            queryWrapper.in("cake_user_role_id", userPageQueryDto.getCakeUserRoleId());
        }
        queryWrapper.and(e -> e.like("nickname", userPageQueryDto.getKeyword())
                .or()
                .like("user_name", userPageQueryDto.getKeyword())
                .or()
                .like("user_code", userPageQueryDto.getKeyword()));
        cakeUserMapper.selectPage(page, queryWrapper);
        // 去掉密码
        List<CakeUser> collect = page.getRecords().stream().map(user -> {
            user.setUserPwd(null);
            return user;
        }).collect(Collectors.toList());
        page.setRecords(collect);
        // 所有的角色
        List<CakeUserRole> allRoles = cakeUserRoleService.getAllRoles();
        Map<String, CakeUserRole> map = new HashMap<>();
        allRoles.forEach(item -> {
            map.put(item.getId(), item);
        });
        IPage<CakeUserVo> iPage = new Page<>();
        iPage.setPages(page.getPages());
        iPage.setTotal(page.getTotal());
        iPage.setCurrent(page.getCurrent());
        iPage.setSize(page.getSize());
        List<CakeUserVo> list = new ArrayList<>(page.getRecords().size());
        page.getRecords().forEach(item -> {
            String str = JSONObject.toJSONString(item);
            CakeUserVo cakeUserVo = JSONObject.parseObject(str, CakeUserVo.class);
            CakeUserRole role = JSONObject.parseObject(JSONObject.toJSONString(map.get(item.getCakeUserRoleId())), CakeUserRole.class);
            cakeUserVo.setCakeUserRole(role);
            list.add(cakeUserVo);
        });
        iPage.setRecords(list);
        return iPage;
    }

    @Override
    public Boolean addMoney(AddMoneyDto addMoneyDto) throws Exception {
        String userId = addMoneyDto.getCakeUserId();
        if (userId == null) {
            throw new Exception("充值的账号不可为空");
        }
        CakeUser cakeUser = cakeUserMapper.selectById(userId);
        // 原金额 + 充值金额
        Double money = cakeUser.getMoney();
        BigDecimal bigDecimal = BigDecimal.valueOf(money);
        Double plusMoney = addMoneyDto.getMoney();
        BigDecimal bigDecimal1 = BigDecimal.valueOf(plusMoney);
        BigDecimal add = bigDecimal.add(bigDecimal1).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        CakeUser cakeUser1 = new CakeUser();
        cakeUser1.setId(userId);
        cakeUser1.setMoney(add.doubleValue());
        int i = cakeUserMapper.updateById(cakeUser1);
        Boolean b = i == 1;
        if (b) {
            // 更新redis 的内容
            UserVo vo = (UserVo) redisTemplate.opsForValue().get(redis_prefix + cakeUser.getUserCode());
            if (vo != null) {
                redisTemplate.delete(redis_prefix + cakeUser.getUserCode());
                User user = vo.getUser();
                user.setMoney(cakeUser1.getMoney());
                redisTemplate.opsForValue().set(redis_prefix + cakeUser.getUserCode(), vo);
            }
        }
        return b;
    }

    @Override
    public Boolean pay(PayDto payDto) throws Exception {
        QueryWrapper<CakeUser> wrapper = new QueryWrapper<>();
        wrapper.eq("id", payDto.getCakeUserId())
                .eq("user_pwd", payDto.getUserPwd());
        CakeUser cakeUser = cakeUserMapper.selectOne(wrapper);
        if (cakeUser == null) {
            throw new Exception("该用户不存在,或者密码错误");
        }
        if (cakeUser.getMoney() < payDto.getPayPrice()) {
            throw new Exception("余额不足");
        }
        BigDecimal payPrice = BigDecimal.valueOf(payDto.getPayPrice());
        BigDecimal price = BigDecimal.valueOf(cakeUser.getMoney());
        BigDecimal subtract = price.subtract(payPrice);
        CakeUser newUser = new CakeUser();
        newUser.setId(cakeUser.getId());
        newUser.setMoney(subtract.doubleValue());
        int i = cakeUserMapper.updateById(newUser);
        return i == 1;
    }
}
