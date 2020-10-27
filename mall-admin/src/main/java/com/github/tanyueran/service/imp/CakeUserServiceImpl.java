package com.github.tanyueran.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.dto.LoginDto;
import com.github.tanyueran.dto.RegisterDto;
import com.github.tanyueran.entity.CakeUser;
import com.github.tanyueran.entity.CakeUserRole;
import com.github.tanyueran.mapper.CakeUserMapper;
import com.github.tanyueran.mapper.CakeUserRoleMapper;
import com.github.tanyueran.service.CakeUserRoleService;
import com.github.tanyueran.service.CakeUserService;
import com.github.tanyueran.utils.JwtUtils;
import com.github.tanyueran.utils.RsaUtil;
import com.github.tanyueran.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
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

    @Override
    public String login(LoginDto loginDto) throws Exception {
        QueryWrapper<CakeUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_code", loginDto.getUsername());
        wrapper.eq("user_pwd", loginDto.getPassword());
        CakeUser user = cakeUserMapper.selectOne(wrapper);
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
    public Boolean addUser(CakeUser cakeUser) throws Exception {
        cakeUser.setStatus(0);
        // 检测账号是否可以使用
        QueryWrapper<CakeUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_code", cakeUser.getUserCode());
        CakeUser user = cakeUserMapper.selectOne(wrapper);
        if (user != null) {
            throw new Exception("账号已被使用，请更换");
        }
        int i = cakeUserMapper.insert(cakeUser);
        return i == 1;
    }

    @Override
    public Boolean editUser(CakeUser cakeUser) throws Exception {
        cakeUser.setCreateTime(null);
        cakeUser.setUpdateTime(null);
        cakeUser.setUserPwd(null);
        cakeUser.setStatus(0);
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
        return i == 1;
    }

    @Override
    public Boolean initUserPwd(Long userId) {
        CakeUser cakeUser = new CakeUser();
        cakeUser.setId(userId);
        cakeUser.setUserPwd(initPasswordStr);
        int i = cakeUserMapper.updateById(cakeUser);
        return i == 1;
    }

    @Override
    public Boolean freezeUser(Long userId) {
        CakeUser cakeUser = new CakeUser();
        cakeUser.setId(userId);
        cakeUser.setStatus(1);
        int i = cakeUserMapper.updateById(cakeUser);
        return i == 1;
    }
}
