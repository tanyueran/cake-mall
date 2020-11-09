package com.github.tanyueran.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.tanyueran.dto.CreateOrderDto;
import com.github.tanyueran.dto.QueryOrderListDto;
import com.github.tanyueran.service.CakeOrderService;
import com.github.tanyueran.vo.CakeOrderVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 蛋糕订单表 前端控制器
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@RestController
@RequestMapping("/action")
public class CakeOrderController {

    @Autowired
    private CakeOrderService cakeOrderService;


    @PostMapping("/create")
    @ApiOperation("创建订单")
    public Boolean createOrder(@RequestBody @Valid CreateOrderDto createOrderDto) throws Exception {
        return cakeOrderService.createOrder(createOrderDto);
    }

    @PostMapping("/pageQuery")
    @ApiOperation("分页查询订单")
    public Page<CakeOrderVo> pageQueryOrders(@RequestBody @Valid QueryOrderListDto queryOrderListDto) {
        return cakeOrderService.queryOrderByPage(queryOrderListDto);
    }


}
