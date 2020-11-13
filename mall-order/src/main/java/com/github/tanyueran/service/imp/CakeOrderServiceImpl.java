package com.github.tanyueran.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.constant.OrderStatus;
import com.github.tanyueran.dto.*;
import com.github.tanyueran.entity.CakeOrder;
import com.github.tanyueran.entity.ResponseResult;
import com.github.tanyueran.mapper.CakeOrderMapper;
import com.github.tanyueran.service.CakeOrderService;
import com.github.tanyueran.service.CakeService;
import com.github.tanyueran.service.CakeUserService;
import com.github.tanyueran.vo.CakeOrderVo;
import com.github.tanyueran.vo.CakeProductVo;
import com.github.tanyueran.vo.OrderCollcetionInfoVo;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 蛋糕订单表 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
@Transactional
@Slf4j
public class CakeOrderServiceImpl extends ServiceImpl<CakeOrderMapper, CakeOrder> implements CakeOrderService {

    @Resource
    private CakeOrderMapper cakeOrderMapper;

    @Resource
    private CakeUserService cakeUserService;

    @Resource
    private CakeService cakeService;

    @Override
    public List<CakeOrder> getAllStatus0Order() {
        QueryWrapper<CakeOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("status", OrderStatus.CREATED_NOT_MONEY.getStatus());
        List<CakeOrder> cakeOrders = cakeOrderMapper.selectList(wrapper);
        return cakeOrders;
    }

    @Override
    public Boolean createOrder(CreateOrderDto createOrderDto) throws Exception {
        // 查询蛋糕的数据
        ResponseResult res = cakeService.getDetailById(createOrderDto.getCakeId());
        log.info("调用蛋糕接口的结果：");
        log.info(JSONObject.toJSONString(res));
        if (res.getCode() != 200) {
            throw new Exception("查询失败");
        }
        String str = JSONObject.toJSONString(res.getData());
        CakeProductVo cake = JSONObject.parseObject(str, CakeProductVo.class);
        // 添加订单
        CakeOrder cakeOrder = new CakeOrder();
        cakeOrder.setId(createOrderDto.getId());
        cakeOrder.setCakeProductId(createOrderDto.getCakeId());
        cakeOrder.setCreateUserId(createOrderDto.getBuyUserId());
        cakeOrder.setPrice(cake.getPrice());
        cakeOrder.setNumber(createOrderDto.getNumber());
        cakeOrder.setCreateTime(new Date());
        cakeOrder.setRemark(createOrderDto.getRemark());
        cakeOrder.setStatus(0);
        // 蛋糕总价
        BigDecimal price = BigDecimal.valueOf(cakeOrder.getPrice());
        BigDecimal number = BigDecimal.valueOf(cakeOrder.getNumber());
        BigDecimal multiply = price.multiply(number);
        cakeOrder.setTotalPrice(multiply.doubleValue());
        int insert = cakeOrderMapper.insert(cakeOrder);
        return insert == 1;
    }

    @Override
    public Page<CakeOrderVo> queryOrderByPage(QueryOrderListDto queryOrderListDto) {
        Page<CakeOrderVo> page = new Page<>(queryOrderListDto.getPage(), queryOrderListDto.getSize());
        return cakeOrderMapper.orderPageQuery(page, queryOrderListDto);
    }

    @Override
    public OrderCollcetionInfoVo getOrderCollectionInfo(String userId) {
        // 查询所有的订单数
        QueryWrapper<CakeOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("create_user_id", userId);
        Integer count = cakeOrderMapper.selectCount(wrapper);
        Double totalMoney = cakeOrderMapper.totalMoneyByUserId(userId);
        // 查询所有的金额数
        OrderCollcetionInfoVo vo = new OrderCollcetionInfoVo();
        vo.setTotalOrderNumber(count);
        vo.setTotalMoney(totalMoney);
        return vo;
    }

    @Override
    public Boolean orderCreatedOvertime(String userId) {
        CakeOrder cakeOrder = new CakeOrder();
        cakeOrder.setId(userId);
        cakeOrder.setStatus(OrderStatus.NOT_MONEY_ORDER_CANCEL.getStatus());
        cakeOrder.setStatus5Time(new Date());
        int i = cakeOrderMapper.updateById(cakeOrder);
        return i == 1;
    }

    @Override
    public CakeOrderVo getOrderInfoById(String orderId) {
        return cakeOrderMapper.selectOrderDetailById(orderId);
    }

    @Override
    @GlobalTransactional(name = "order-pay-tx", rollbackFor = Exception.class)
    public Boolean order2Status10(OrderPayDto orderPayDto) throws Exception {
        // 查询出订单的金额
        CakeOrder cakeOrder = cakeOrderMapper.selectById(orderPayDto.getOrderId());
        if (cakeOrder == null) {
            throw new Exception("订单不存在");
        }
        PayDto dto = orderPayDto.getPayDto();
        dto.setPayPrice(cakeOrder.getTotalPrice());
        ResponseResult result = cakeUserService.pay(dto);
        if (result.getCode() != 200) {
            throw new Exception("付款出错:" + result.getMsg());
        }
        Boolean payOk = (Boolean) result.getData();
        if (payOk) {
            CakeOrder order = new CakeOrder();
            order.setId(orderPayDto.getOrderId());
            order.setStatus(OrderStatus.PAID_WAITING_SEND.getStatus());
            order.setStatus10Time(new Date());
            int i = cakeOrderMapper.updateById(order);
            return i == 1;
        }
        return false;
    }

    @Override
    @GlobalTransactional(name = "refuse-order-tx", rollbackFor = Exception.class)
    public Boolean refuseOrder(String orderId) throws Exception {
        CakeOrder cakeOrder = cakeOrderMapper.selectById(orderId);
        if (cakeOrder.getStatus() != OrderStatus.PAID_WAITING_SEND.getStatus()) {
            throw new Exception("该订单不可拒绝");
        }
        // 退钱
        GetMoneyDto dto = new GetMoneyDto();
        dto.setMoney(cakeOrder.getTotalPrice());
        dto.setUserId(cakeOrder.getCreateUserId());
        ResponseResult body = cakeUserService.getMoney(dto);
        Boolean ok = (Boolean) body.getData();
        // 修改状态
        if (ok) {
            CakeOrder order = new CakeOrder();
            order.setId(cakeOrder.getId());
            order.setStatus(OrderStatus.REFUSE_CANCEL.getStatus());
            order.setStatus15Time(new Date());
            int i = cakeOrderMapper.updateById(order);
            return i == 1;
        } else {
            throw new Exception("退款失败");
        }
    }

    @Override
    public Boolean giveOrder(GiveOrderDto giveOrderDto) throws Exception {
        CakeOrder cakeOrder = cakeOrderMapper.selectById(giveOrderDto.getOrderId());
        if (cakeOrder == null) {
            throw new Exception("订单不存在");
        }
        if (cakeOrder.getStatus() != OrderStatus.PAID_WAITING_SEND.getStatus()) {
            throw new Exception("该订单状态不可操作接受订单");
        }
        CakeOrder order = new CakeOrder();
        order.setId(cakeOrder.getId());
        order.setActionUserId(giveOrderDto.getUserId());
        order.setStatus(OrderStatus.GAVE_WAITING_SEND.getStatus());
        order.setStatus20Time(new Date());
        int i = cakeOrderMapper.updateById(order);
        return i == 1;
    }

    @Override
    public Boolean send(GiveOrderDto giveOrderDto) throws Exception {
        CakeOrder cakeOrder = cakeOrderMapper.selectById(giveOrderDto.getOrderId());
        if (cakeOrder == null) {
            throw new Exception("订单不存在");
        }
        if (cakeOrder.getStatus() != OrderStatus.GAVE_WAITING_SEND.getStatus()) {
            throw new Exception("该订单状态不可操作发货");
        }
        CakeOrder order = new CakeOrder();
        order.setId(cakeOrder.getId());
        order.setActionUserId(giveOrderDto.getUserId());
        order.setStatus(OrderStatus.SEND_WAITING_GET.getStatus());
        order.setStatus30Time(new Date());
        int i = cakeOrderMapper.updateById(order);
        return i == 1;
    }

    @Override
    public Boolean orderOver(GiveOrderDto giveOrderDto) throws Exception {
        CakeOrder cakeOrder = cakeOrderMapper.selectById(giveOrderDto.getOrderId());
        if (cakeOrder == null) {
            throw new Exception("订单不存在");
        }
        if (cakeOrder.getStatus() != OrderStatus.SEND_WAITING_GET.getStatus()) {
            throw new Exception("该订单状态不可操作完成");
        }
        CakeOrder order = new CakeOrder();
        order.setId(cakeOrder.getId());
        order.setActionUserId(giveOrderDto.getUserId());
        order.setStatus(OrderStatus.ORDER_SUCCESS.getStatus());
        order.setStatus40Time(new Date());
        int i = cakeOrderMapper.updateById(order);
        return i == 1;
    }
}
