package com.github.tanyueran.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.entity.CakeProduct;
import com.github.tanyueran.mapper.CakeProductMapper;
import com.github.tanyueran.service.CakeProductService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 蛋糕表 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
public class CakeProductServiceImpl extends ServiceImpl<CakeProductMapper, CakeProduct> implements CakeProductService {

}
