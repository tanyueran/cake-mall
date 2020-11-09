package com.github.tanyueran.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.tanyueran.dto.CreateOrderDto;
import com.github.tanyueran.dto.QueryOrderListDto;
import com.github.tanyueran.entity.CakeOrder;
import com.github.tanyueran.vo.CakeOrderVo;

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
     * 创建订单
     *
     * @param createOrderDto
     * @return
     */
    Boolean createOrder(CreateOrderDto createOrderDto) throws Exception;


    Page<CakeOrderVo> queryOrderByPage(QueryOrderListDto queryOrderListDto);
}
