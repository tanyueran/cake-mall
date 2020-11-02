package com.github.tanyueran.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.dto.CategoryQueryDto;
import com.github.tanyueran.entity.CakeProductCategories;
import com.github.tanyueran.mapper.CakeProductCategoriesMapper;
import com.github.tanyueran.service.CakeProductCategoriesService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 蛋糕类型表 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
@Transactional
public class CakeProductCategoriesServiceImpl extends ServiceImpl<CakeProductCategoriesMapper, CakeProductCategories> implements CakeProductCategoriesService {

    @Resource
    private CakeProductCategoriesMapper cakeProductCategoriesMapper;

    @Override
    public Boolean addCategories(CakeProductCategories cakeProductCategories) throws Exception {
        cakeProductCategories.setUpdateTime(null);
        cakeProductCategories.setCreateTime(null);
        QueryWrapper<CakeProductCategories> wrapper = new QueryWrapper<>();
        wrapper.eq("code", cakeProductCategories.getCode());
        CakeProductCategories categories = cakeProductCategoriesMapper.selectOne(wrapper);
        if (categories != null) {
            throw new Exception("code已被使用，请更换");
        }
        int insert = cakeProductCategoriesMapper.insert(cakeProductCategories);
        return insert == 1;
    }

    @Override
    public Boolean removeCategoryById(String categoryId) {
        int i = cakeProductCategoriesMapper.deleteById(categoryId);
        return i == 1;
    }

    @Override
    public Boolean editCategory(CakeProductCategories cakeProductCategories) throws Exception {
        cakeProductCategories.setCreateTime(null);
        cakeProductCategories.setUpdateTime(null);
        // 先通过id查出数据中的数据
        CakeProductCategories categoriesInDB = cakeProductCategoriesMapper.selectById(cakeProductCategories.getId());
        // 判断数据中的code，是否改变，
        // ---改变，则判断是否可用
        // ---没有改变，直接更新
        if (!categoriesInDB.getCode().equals(cakeProductCategories.getCode())) {
            QueryWrapper<CakeProductCategories> wrapper = new QueryWrapper<>();
            wrapper.eq("code", cakeProductCategories.getCode());
            CakeProductCategories categories = cakeProductCategoriesMapper.selectOne(wrapper);
            if (categories != null) {
                throw new Exception("该类型code已被使用，请更换");
            }
        }
        int i = cakeProductCategoriesMapper.updateById(cakeProductCategories);
        return i == 1;
    }

    @Override
    public IPage<CakeProductCategories> getCategoriesByPage(CategoryQueryDto categoryQueryDto) {
        IPage<CakeProductCategories> page = new Page<>(categoryQueryDto.getPage(), categoryQueryDto.getSize());
        if (Strings.isEmpty(categoryQueryDto.getKeywords())) {
            categoryQueryDto.setKeywords("");
        }
        QueryWrapper<CakeProductCategories> wrapper = new QueryWrapper<>();
        wrapper.like("name", categoryQueryDto.getKeywords())
                .or()
                .like("code", categoryQueryDto.getKeywords())
                .or()
                .like("remark", categoryQueryDto.getKeywords())
                .orderByDesc("create_time");
        cakeProductCategoriesMapper.selectPage(page, wrapper);
        return page;
    }

    @Override
    public List<CakeProductCategories> getAllCategories() {
        return cakeProductCategoriesMapper.selectList(null);
    }
}
