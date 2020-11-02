package com.github.tanyueran.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.tanyueran.dto.CategoryQueryDto;
import com.github.tanyueran.entity.CakeProductCategories;

import java.util.List;

/**
 * <p>
 * 蛋糕类型表 服务类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
public interface CakeProductCategoriesService extends IService<CakeProductCategories> {
    /**
     * 添加类型
     *
     * @param CakeProductCategories
     * @return
     * @throws Exception
     */
    Boolean addCategories(CakeProductCategories cakeProductCategories) throws Exception;

    /**
     * 根据id删除类型
     *
     * @param categoryId
     * @return
     */
    Boolean removeCategoryById(String categoryId);

    /**
     * 编辑类型
     *
     * @param CakeProductCategories
     * @return
     */
    Boolean editCategory(CakeProductCategories cakeProductCategories) throws Exception;


    /**
     * 分页查询类型信息
     *
     * @param CategoryQueryDto
     * @return
     */
    IPage<CakeProductCategories> getCategoriesByPage(CategoryQueryDto categoryQueryDto);

    /**
     * 获取所有的类型
     *
     * @return
     */
    List<CakeProductCategories> getAllCategories();
}
