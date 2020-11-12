package com.github.tanyueran.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.constant.OrderStatus;
import com.github.tanyueran.dto.CreateOrderDto;
import com.github.tanyueran.dto.OrderPayDto;
import com.github.tanyueran.dto.PayDto;
import com.github.tanyueran.dto.QueryOrderListDto;
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
        dto.setPayPrice(cakeOrder.getPrice());
        ResponseResult result = cakeUserService.pay(dto);
        if (result.getCode() != 200) {
            throw new Exception("付款出错:" + result.getMsg());
        }
        Boolean payOk = (Boolean) result.getData();
        int j = 1/0;
        if (payOk) {
            CakeOrder order = new CakeOrder();
            order.setId(orderPayDto.getOrderId());
            order.setStatus(OrderStatus.PAID_WAITTING_SEND.getStatus());
            order.setStatus10Time(new Date());
            int i = cakeOrderMapper.updateById(order);
            return i == 1;
        }
        return false;
    }
}
