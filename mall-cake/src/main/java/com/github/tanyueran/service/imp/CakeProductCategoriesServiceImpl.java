package com.github.tanyueran.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.entity.CakeProductCategories;
import com.github.tanyueran.mapper.CakeProductCategoriesMapper;
import com.github.tanyueran.service.CakeProductCategoriesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 蛋糕类型表 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
public class CakeProductCategoriesServiceImpl extends ServiceImpl<CakeProductCategoriesMapper, CakeProductCategories> implements CakeProductCategoriesService {

}
