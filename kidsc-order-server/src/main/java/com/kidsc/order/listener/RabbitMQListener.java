package com.kidsc.order.listener;

import com.alibaba.fastjson.JSONObject;
import com.kidsc.order.model.OrderInfo;
import com.kidsc.order.model.Orders;
import com.kidsc.order.service.OrdersService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Set;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@Component
public class RabbitMQListener {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OrdersService ordersService;

    @RabbitListener(bindings = {@QueueBinding(value = @Queue("orderStatus"),exchange = @Exchange(name = "orderStatusExchange",type = "fanout"))})
        public void deleteOrderFromRedis(String message){
        //Set<String> orders = stringRedisTemplate.opsForZSet().range("orders", 0, 10);
        //System.out.println(message);
        //orders.forEach(order -> System.out.println(order));
        stringRedisTemplate.opsForZSet().remove("orders",message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue("deadLetterQueue"),exchange = @Exchange("deadLetterExchange"))})
    public void deadLetterListener(String message){

        HashMap jsonMap = JSONObject.parseObject(message, HashMap.class);
        JSONObject orderInfoJson = (JSONObject) jsonMap.get("orderInfo");
        JSONObject ordersJson = (JSONObject) jsonMap.get("orders");

        Orders orders = ordersJson.toJavaObject(Orders.class);
        OrderInfo orderInfo = orderInfoJson.toJavaObject(OrderInfo.class);

        ordersService.cancelOrder(orders,orderInfo);
    }

}
