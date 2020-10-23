package com.github.tanyueran.service.impl;

import com.github.tanyueran.entity.CakeOrder;
import com.github.tanyueran.mapper.CakeOrderMapper;
import com.github.tanyueran.service.CloudCakeOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 蛋糕订单表 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
public class CakeOrderServiceImpl extends ServiceImpl<CakeOrderMapper, CakeOrder> implements CloudCakeOrderService {

}
