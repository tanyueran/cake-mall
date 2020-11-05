package com.github.tanyueran.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.tanyueran.dto.CategoryQueryDto;
import com.github.tanyueran.entity.CakeProductCategories;
import com.github.tanyueran.service.CakeProductCategoriesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 蛋糕类型表 前端控制器
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@RestController
@RequestMapping("/categories")
@Api(tags = "CakeProductCategoriesController", description = "蛋糕类型api")
public class CakeProductCategoriesController {

    @Autowired
    private CakeProductCategoriesService cakeProductCategoriesService;

    @PostMapping("/getByPage")
    @ApiOperation("蛋糕类型分页查询")
    public IPage<CakeProductCategories> getCategoriesByPage(@RequestBody @Valid CategoryQueryDto categoryQueryDto) {
        return cakeProductCategoriesService.getCategoriesByPage(categoryQueryDto);
    }

    @PostMapping("/add")
    @ApiOperation("添加蛋糕类型")
    @PreAuthorize("hasRole('manager')")
    public Boolean addCategory(@RequestBody @Valid CakeProductCategories cakeProductCategories) throws Exception {
        return cakeProductCategoriesService.addCategories(cakeProductCategories);
    }

    @PutMapping("/update")
    @ApiOperation("更新蛋糕类型")
    @PreAuthorize("hasRole('manager')")
    public Boolean updateCategory(@RequestBody @Valid CakeProductCategories cakeProductCategories) throws Exception {
        return cakeProductCategoriesService.editCategory(cakeProductCategories);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除类型")
    @PreAuthorize("hasRole('manager')")
    public Boolean deleteCategory(@PathVariable String id) {
        return cakeProductCategoriesService.removeCategoryById(id);
    }

    @GetMapping("/all")
    @ApiOperation("查询所有的类型")
    public List<CakeProductCategories> getAllCategories() {
        return cakeProductCategoriesService.getAllCategories();
    }


}
