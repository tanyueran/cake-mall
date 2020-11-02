package com.github.tanyueran.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.tanyueran.dto.CategoryQueryDto;
import com.github.tanyueran.entity.CakeProduct;
import com.github.tanyueran.vo.CakeProductVo;

/**
 * <p>
 * 蛋糕表 服务类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
public interface CakeProductService extends IService<CakeProduct> {

    /**
     * 添加新的蛋糕
     *
     * @param cakeProduct
     * @return
     */
    Boolean addCake(CakeProduct cakeProduct);

    /**
     * 根据蛋糕id删除蛋糕（逻辑删除，修改蛋糕的删除状态）
     *
     * @param cakeId
     * @return
     */
    Boolean removeCakeById(String cakeId);

    /**
     * 编辑蛋糕
     *
     * @param cakeProduct
     * @return
     */
    Boolean updateCake(CakeProduct cakeProduct);


    /**
     * 分页查询蛋糕
     *
     * @param categoryQueryDto
     * @return
     */
    IPage<CakeProductVo> getCakeByPage(CategoryQueryDto categoryQueryDto);


}
