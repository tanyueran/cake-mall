package com.github.tanyueran.service.impl;

import com.github.tanyueran.entity.CakeNews;
import com.github.tanyueran.mapper.CakeNewsMapper;
import com.github.tanyueran.service.CloudCakeNewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单消息表 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
public class CakeNewsServiceImpl extends ServiceImpl<CakeNewsMapper, CakeNews> implements CloudCakeNewsService {

}
