package com.github.tanyueran.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.dto.CategoryQueryDto;
import com.github.tanyueran.entity.CakeProduct;
import com.github.tanyueran.entity.CakeProductCategories;
import com.github.tanyueran.mapper.CakeProductCategoriesMapper;
import com.github.tanyueran.mapper.CakeProductMapper;
import com.github.tanyueran.service.CakeProductService;
import com.github.tanyueran.vo.CakeProductVo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 蛋糕表 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
@Transactional
public class CakeProductServiceImpl extends ServiceImpl<CakeProductMapper, CakeProduct> implements CakeProductService {

    @Resource
    private CakeProductMapper cakeProductMapper;

    @Resource
    private CakeProductCategoriesMapper cakeProductCategoriesMapper;

    @Override
    public Boolean addCake(CakeProduct cakeProduct) {
        cakeProduct.setCreateTime(null);
        cakeProduct.setUpdateTime(null);
        cakeProduct.setDeleteStatus(0);
        int insert = cakeProductMapper.insert(cakeProduct);
        return insert == 1;
    }

    @Override
    public Boolean removeCakeById(String cakeId) {
        CakeProduct cakeProduct = new CakeProduct();
        cakeProduct.setId(cakeId);
        cakeProduct.setDeleteStatus(1);
        int i = cakeProductMapper.updateById(cakeProduct);
        return i == 1;
    }

    @Override
    public Boolean updateCake(CakeProduct cakeProduct) {
        cakeProduct.setCreateTime(null);
        cakeProduct.setUpdateTime(null);
        cakeProduct.setDeleteStatus(0);
        int i = cakeProductMapper.updateById(cakeProduct);
        return i == 1;
    }

    @Override
    public IPage<CakeProductVo> getCakeByPage(CategoryQueryDto categoryQueryDto) {
        IPage<CakeProduct> page = new Page<>();
        page.setSize(categoryQueryDto.getSize());
        page.setCurrent(categoryQueryDto.getPage());
        if (Strings.isEmpty(categoryQueryDto.getKeywords())) {
            categoryQueryDto.setKeywords("");
        }
        QueryWrapper<CakeProduct> wrapper = new QueryWrapper<>();
        if (categoryQueryDto.getCategoriesId() != null && categoryQueryDto.getCategoriesId().size() > 0) {
            wrapper.in("cake_product_categories_id", categoryQueryDto.getCategoriesId());
        }
        wrapper.and(e -> e.eq("delete_status", 0));
        wrapper.and(e -> e.like("name", categoryQueryDto.getKeywords())
                .or()
                .like("detail", categoryQueryDto.getKeywords())
                .or()
                .like("remark", categoryQueryDto.getKeywords()));
        cakeProductMapper.selectPage(page, wrapper);
        IPage<CakeProductVo> ipage = new Page<>();
        ipage.setCurrent(page.getCurrent());
        ipage.setSize(page.getSize());
        ipage.setTotal(page.getTotal());
        ipage.setPages(page.getPages());
        List<CakeProductCategories> categories = cakeProductCategoriesMapper.selectList(null);
        Map<String, CakeProductCategories> map = new HashMap<>();
        categories.forEach(item -> {
            map.put(item.getId(), item);
        });

        List<CakeProductVo> list = new ArrayList<>();
        page.getRecords().forEach(item -> {
            String categoryId = item.getCakeProductCategoriesId();
            if (categoryId != null) {
                CakeProductVo cakeProductVo = JSONObject.parseObject(JSONObject.toJSONString(item), CakeProductVo.class);
                cakeProductVo.setCakeProductCategories(
                        JSONObject.parseObject(
                                JSONObject.toJSONString(map.get(categoryId)), CakeProductCategories.class
                        )
                );
                list.add(cakeProductVo);
            }
        });
        ipage.setRecords(list);
        return ipage;
    }
}
