package com.kidsc.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kidsc.commons.Code;
import com.kidsc.commons.JsonResult;
import com.kidsc.order.mapper.OrderInfoMapper;
import com.kidsc.order.mapper.OrdersMapper;
import com.kidsc.order.model.OrderInfo;
import com.kidsc.order.model.Orders;
import com.kidsc.order.service.OrdersService;
import com.kidsc.order.service.RemoteGoodsService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private RemoteGoodsService remoteGoodsService;

    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    @Transactional
    @GlobalTransactional
    public Object addOrder(Long goodsId, Integer buyNum, String token, Long userId) {
        // 减少商品库存
        JsonResult<BigDecimal> result = remoteGoodsService.decrGoodsStore(goodsId,buyNum);

        if (result.getCode().equals(Code.NO_GOODS_STORE)){
            // 表示减少库存失败，库存不足
            return new JsonResult(Code.NO_GOODS_STORE,"没有库存");
        }
        // 到这里表示减少库存成功，开始添加订单
        Orders orders = new Orders();

        orders.setStatus(0);
        orders.setUserId(userId);
        orders.setOrderNo(UUID.randomUUID().toString().replaceAll("-",""));
        orders.setCreateTime(System.currentTimeMillis());
        orders.setOrdersMoney(result.getResult().multiply(new BigDecimal(buyNum)));

        ordersMapper.insertSelective(orders);

        //System.out.println(10/0);

        // 添加订单完成后添加订单详情
        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setAmount(buyNum);
        orderInfo.setGoodsId(goodsId);
        orderInfo.setOrdersId(orders.getId());
        orderInfo.setPrice(result.getResult());

        orderInfoMapper.insertSelective(orderInfo);

        Map messageMap = new HashMap();
        messageMap.put("orders",orders);
        messageMap.put("orderInfo",orderInfo);
        String orderMessageJson = JSONObject.toJSONString(messageMap);
        MessageProperties properties = new MessageProperties();
        properties.setExpiration("20000");
        Message message = new Message(orderMessageJson.getBytes(),properties);

        amqpTemplate.send("orderExchange","",message);
        return orders;
    }

    @Override
    public List<Map> getOrderInfoByOrderId(Long orderId) {
        return orderInfoMapper.selectOrderInfoByOrdersId(orderId);
    }

    @Override
    public void orderPay(Orders orders) {
        // 支付成功，修改订单信息
        ordersMapper.updateByPrimaryKeySelective(orders);
        // 调用RabbitMQ通知物流系统发货，通知积分系统增加积分
        amqpTemplate.convertAndSend("orderPayExchange","", JSONObject.toJSONString(orders));
    }

    @Override
    public void checkOrderPay(Object payStatus, Orders orderJson) {
        if ("TRADE_SUCCESS".equals(payStatus)){
            // 支付成功，修改订单信息
            ordersMapper.updateByPrimaryKeySelective(orderJson);
            // 调用RabbitMQ通知物流系统发货，通知积分系统增加积分
            amqpTemplate.convertAndSend("orderPayExchange","", JSONObject.toJSONString(orderJson));
        }
    }

    @Override
    @Transactional
    @GlobalTransactional
    public void cancelOrder(Orders orders, OrderInfo orderInfo) {
        orderInfoMapper.deleteByOrderId(orderInfo.getOrdersId());
        ordersMapper.deleteByPrimaryKey(orders.getId());

        remoteGoodsService.incrGoodsStore(orderInfo.getGoodsId(),orderInfo.getAmount());
    }
}
