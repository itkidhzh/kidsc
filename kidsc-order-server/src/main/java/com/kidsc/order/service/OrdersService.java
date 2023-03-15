package com.kidsc.order.service;

import com.kidsc.order.model.OrderInfo;
import com.kidsc.order.model.Orders;

import java.util.List;
import java.util.Map;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
public interface OrdersService {
    Object addOrder(Long goodsId, Integer buyNum, String token, Long userId);

    List<Map> getOrderInfoByOrderId(Long orderId);

    void orderPay(Orders orders);

    void checkOrderPay(Object payStatus, Orders orderJson);

    void cancelOrder(Orders orders, OrderInfo orderInfo);
}
