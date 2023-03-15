package com.kidsc.order.mapper;

import com.kidsc.order.model.OrderInfo;

import java.util.List;
import java.util.Map;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    OrderInfo getOrderInfoByOrderId(Long orderId);

    List<Map> selectOrderInfoByOrdersId(Long orderId);

    void deleteByOrderId(Long id);
}