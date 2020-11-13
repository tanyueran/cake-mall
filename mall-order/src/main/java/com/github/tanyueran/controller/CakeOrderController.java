package com.github.tanyueran.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.tanyueran.config.CreateOrderScheduleUtil;
import com.github.tanyueran.dto.*;
import com.github.tanyueran.service.CakeOrderService;
import com.github.tanyueran.vo.CakeOrderVo;
import com.github.tanyueran.vo.OrderCollcetionInfoVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

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
@Slf4j
public class CakeOrderController {

    @Autowired
    private CakeOrderService cakeOrderService;

    @Autowired
    private CreateOrderScheduleUtil createOrderScheduleUtil;


    @PostMapping("/create")
    @ApiOperation("创建订单")
    @PreAuthorize("hasRole('user')")
    public Boolean createOrder(@RequestBody @Valid CreateOrderDto createOrderDto) throws Exception {
        Boolean ok = cakeOrderService.createOrder(createOrderDto);
        if (ok) {
            OrderTaskDto dto = new OrderTaskDto();
            dto.setOrderId(createOrderDto.getId());
            dto.setStartTime(new Date());
            // 开启定时器
            createOrderScheduleUtil.addTask(createOrderDto.getId(), dto);
        }
        return ok;
    }

    @PostMapping("/pageQuery")
    @ApiOperation("分页查询订单")
    public Page<CakeOrderVo> pageQueryOrders(@RequestBody @Valid QueryOrderListDto queryOrderListDto) {
        Page<CakeOrderVo> cakeOrderVoPage = cakeOrderService.queryOrderByPage(queryOrderListDto);
        return cakeOrderVoPage;
    }

    @ApiOperation("查询个人用户一些订单相关基本信息")
    @GetMapping("/orderCollectionInfo/{userId}")
    public OrderCollcetionInfoVo getOrderCollectionInfo(@PathVariable("userId") String userId) {
        return cakeOrderService.getOrderCollectionInfo(userId);
    }

    @ApiOperation("根据订单id查询订单信息")
    @GetMapping("/orderDetail/{id}")
    public CakeOrderVo getOrderInfoByOrderId(@PathVariable("id") String orderId) {
        return cakeOrderService.getOrderInfoById(orderId);
    }

    @ApiOperation("订单付款")
    @PostMapping("/order/pay")
    @PreAuthorize("hasRole('user')")
    public Boolean payMoney(@RequestBody @Valid OrderPayDto orderPayDto) throws Exception {
        // todo:此处支付和状态改变应该是两个独立分开的，以后修改
        return cakeOrderService.order2Status10(orderPayDto);
    }

    @ApiOperation("拒绝订单")
    @PostMapping("/refuseOrder/{id}")
    @PreAuthorize("hasRole('manager')")
    public Boolean refuseOrder(@PathVariable("id") String orderId) throws Exception {
        return cakeOrderService.refuseOrder(orderId);
    }

    @ApiOperation("接受订单")
    @PostMapping("/giveOrder")
    public Boolean giveOrder(@RequestBody @Valid GiveOrderDto giveOrderDto) throws Exception {
        return cakeOrderService.giveOrder(giveOrderDto);
    }

    @ApiOperation("发货")
    @PostMapping("/send")
    public Boolean send(@RequestBody @Valid GiveOrderDto giveOrderDto) throws Exception {
        return cakeOrderService.send(giveOrderDto);
    }

    @ApiOperation("订单完成")
    @PostMapping("/orderOver")
    public Boolean orderOver(@RequestBody @Valid GiveOrderDto giveOrderDto) throws Exception {
        return cakeOrderService.orderOver(giveOrderDto);
    }
}
