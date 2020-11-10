package com.github.tanyueran.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.tanyueran.dto.QueryOrderListDto;
import com.github.tanyueran.entity.CakeOrder;
import com.github.tanyueran.vo.CakeOrderVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 蛋糕订单表 Mapper 接口
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
public interface CakeOrderMapper extends BaseMapper<CakeOrder> {

    /**
     * 分页查询订单
     *
     * @param page
     * @param queryOrderListDto
     * @return
     */
    Page<CakeOrderVo> orderPageQuery(Page<CakeOrderVo> page, @Param("dto") QueryOrderListDto queryOrderListDto);


    /**
     * 查询个人订单总金额
     *
     * @param userId
     * @return
     */
    Double totalMoneyByUserId(String userId);
}
