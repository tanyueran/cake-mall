package com.github.tanyueran.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.tanyueran.dto.CreateOrderDto;
import com.github.tanyueran.dto.GiveOrderDto;
import com.github.tanyueran.dto.OrderPayDto;
import com.github.tanyueran.dto.QueryOrderListDto;
import com.github.tanyueran.entity.CakeOrder;
import com.github.tanyueran.vo.CakeOrderVo;
import com.github.tanyueran.vo.OrderCollcetionInfoVo;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

/**
 * <p>
 * 蛋糕订单表 服务类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
public interface CakeOrderService extends IService<CakeOrder> {

    /**
     * 获取所有的状态0的订单
     *
     * @return
     */
    List<CakeOrder> getAllStatus0Order();

    /**
     * 创建订单
     *
     * @param createOrderDto
     * @return
     */
    Boolean createOrder(CreateOrderDto createOrderDto) throws Exception;


    /**
     * 分页查询订单列表
     *
     * @param queryOrderListDto
     * @return
     */
    Page<CakeOrderVo> queryOrderByPage(QueryOrderListDto queryOrderListDto);

    /**
     * 查询个人的订单信息集合
     *
     * @param userId
     * @return
     */
    OrderCollcetionInfoVo getOrderCollectionInfo(String userId);


    /**
     * 订单创建超时，长时间未付款，改变订单状态
     *
     * @param userId
     * @return
     */
    Boolean orderCreatedOvertime(String userId);

    /**
     * 根据订单id查询订单详情
     *
     * @param orderId
     * @return
     */
    CakeOrderVo getOrderInfoById(String orderId);

    /**
     * 订单付款及状态改变
     *
     * @param orderPayDto
     * @return
     */
    Boolean order2Status10(OrderPayDto orderPayDto) throws Exception;


    /**
     * 管理员拒绝订单，并退款
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    Boolean refuseOrder(String orderId) throws Exception;

    /**
     * 接受订单
     *
     * @param giveOrderDto
     * @return
     */
    Boolean giveOrder(GiveOrderDto giveOrderDto) throws Exception;

    /**
     * 发货
     *
     * @param giveOrderDto
     * @return
     */
    Boolean send(GiveOrderDto giveOrderDto) throws Exception;

    /**
     * 完成订单
     *
     * @param giveOrderDto
     * @return
     */
    Boolean orderOver(GiveOrderDto giveOrderDto) throws Exception;
}
