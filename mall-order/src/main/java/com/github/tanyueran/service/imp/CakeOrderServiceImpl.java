package com.github.tanyueran.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.entity.CakeOrder;
import com.github.tanyueran.mapper.CakeOrderMapper;
import com.github.tanyueran.service.CakeOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 蛋糕订单表 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
@Transactional
public class CakeOrderServiceImpl extends ServiceImpl<CakeOrderMapper, CakeOrder> implements CakeOrderService {

}
