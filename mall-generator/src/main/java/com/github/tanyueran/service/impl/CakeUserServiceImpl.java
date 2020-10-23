package com.github.tanyueran.service.impl;

import com.github.tanyueran.entity.CakeUser;
import com.github.tanyueran.mapper.CakeUserMapper;
import com.github.tanyueran.service.CloudCakeUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
public class CakeUserServiceImpl extends ServiceImpl<CakeUserMapper, CakeUser> implements CloudCakeUserService {

}
