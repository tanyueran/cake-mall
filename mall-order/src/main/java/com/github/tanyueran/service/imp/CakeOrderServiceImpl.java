package com.github.tanyueran.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.dto.CreateOrderDto;
import com.github.tanyueran.dto.QueryOrderListDto;
import com.github.tanyueran.entity.CakeOrder;
import com.github.tanyueran.entity.CakeProduct;
import com.github.tanyueran.entity.ResponseResult;
import com.github.tanyueran.mapper.CakeOrderMapper;
import com.github.tanyueran.service.CakeOrderService;
import com.github.tanyueran.service.CakeService;
import com.github.tanyueran.vo.CakeOrderVo;
import com.github.tanyueran.vo.CakeProductVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

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
    private CakeService cakeService;

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
}
