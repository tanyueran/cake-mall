package com.github.tanyueran.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.tanyueran.dto.CategoryQueryDto;
import com.github.tanyueran.entity.CakeProduct;
import com.github.tanyueran.service.CakeProductService;
import com.github.tanyueran.vo.CakeProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 蛋糕表 前端控制器
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@RestController
@RequestMapping("/product")
public class CakeProductController {

    @Resource
    private CakeProductService cakeProductService;


    @ApiOperation("蛋糕新增")
    @PostMapping("/add")
    @PreAuthorize("hasRole('manager')")
    public Boolean addCake(@RequestBody @Valid CakeProduct cakeProduct) {
        return cakeProductService.addCake(cakeProduct);
    }

    @ApiOperation("蛋糕编辑")
    @PutMapping("/update")
    @PreAuthorize("hasRole('manager')")
    public Boolean editCake(@RequestBody @Valid CakeProduct cakeProduct) {
        return cakeProductService.updateCake(cakeProduct);
    }

    @ApiOperation("删除蛋糕")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('manager')")
    public Boolean removeCakeById(@PathVariable("id") String cakeId) {
        return cakeProductService.removeCakeById(cakeId);
    }

    @ApiOperation("分页查询蛋糕列表")
    @PostMapping("/getPage")
    public IPage<CakeProductVo> getQueryPage(@RequestBody @Valid CategoryQueryDto categoryQueryDto) {
        return cakeProductService.getCakeByPage(categoryQueryDto);
    }

    @ApiOperation("根据蛋糕id查询详情")
    @GetMapping("/detail/{id}")
    public CakeProductVo getDetailById(@PathVariable("id") String cakeId) {
        return cakeProductService.getCakeDetailInfoById(cakeId);
    }


}
