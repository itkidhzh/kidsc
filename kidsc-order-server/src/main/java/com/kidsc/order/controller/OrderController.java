package com.kidsc.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.kidsc.order.config.AlipayConfig;
import com.kidsc.order.model.Orders;
import com.kidsc.order.service.OrdersService;
import com.kidsc.commons.Code;
import com.kidsc.commons.JsonResult;
import com.kidsc.order.service.RemoteUserService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.json.Json;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@RestController
@CrossOrigin
public class OrderController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private OrdersService ordersService;


    @GetMapping("/addOrder")
    public Object addOrder(Long goodsId,Integer buyNum,String token){



        JsonResult<Long> jsonResult = remoteUserService.getUserId(token);


        if (jsonResult.getCode().equals(Code.NO_LOGIN.getCode())){
            return new JsonResult(Code.NO_LOGIN,"没有登录");
        }
        Long userId = jsonResult.getResult();
        Object order = ordersService.addOrder(goodsId,buyNum,token,userId);

        if (order == null){
            return new JsonResult(Code.NO_GOODS_STORE,null);
        }

        return new JsonResult(Code.OK,order);
    }

    @GetMapping("/confirmOrderInfo")
    public Object confirmOrderInfo(Long orderId){

        System.out.println(orderId);
        List<Map> orderInfoList = ordersService.getOrderInfoByOrderId(orderId);

        return new JsonResult(Code.OK,orderInfoList);
    }

    @PostMapping("/pay")
    public Object pay(String orderNo, Long orderId, BigDecimal actualMoney,Long addressId,String token,Integer point,String payType) throws AlipayApiException {

        JsonResult<Long> jsonResult = remoteUserService.getUserId(token);
        if (jsonResult == null){
            return new JsonResult(Code.NO_LOGIN,null);
        }

        Long userId = jsonResult.getResult();
        Map map = new HashMap();
        map.put("id",orderId);
        map.put("addressId",addressId);
        map.put("point",point);
        map.put("orderNo",orderNo);
        String orderJson = JSONObject.toJSONString(map);
        Duration timeOut = Duration.ofSeconds(30 * 60);

        // 为了确保订单存入redis以及redis备份订单信息同时完成，可以使用redis的事务将两个操作放到一个事务中
        stringRedisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                // 开启事务
                redisOperations.multi();
                redisOperations.opsForValue().setIfAbsent((K)("orderPay:" + orderNo),(V)(orderJson), timeOut);
                redisOperations.opsForZSet().add((K)("orders"),(V)orderJson,System.currentTimeMillis());
                // 提交事务
                return redisOperations.exec();
            }
        });

        // 存放订单信息到redis 防止用户支付成功后无法修改配送，防止掉单

        // 即使没有这个判断支付宝也不会允许重复支付
        //if (!flag){
        //    return "不能重复支付";
        //}
        if (payType.equals("zfb")){
            return zfbPay(orderNo,orderId,actualMoney,addressId,token,payType);
        }

        return "准备支付";
    }



    @RequestMapping("/paySuccess")
    public String paySuccess(String out_trade_no,BigDecimal total_amount){

        String orderJson = stringRedisTemplate.opsForValue().get("orderPay:" + out_trade_no);

        Orders orders = JSONObject.parseObject(orderJson, Orders.class);
        // 设置订单状态为待发货状态
        orders.setActualMoney(total_amount);
        orders.setStatus(1);

        ordersService.orderPay(orders);

        return "支付成功";
    }

    @RequestMapping("/checkOrderPay")
    public Object checkOrderPay(String orders) throws AlipayApiException {
        Orders orderJson = JSONObject.parseObject(orders, Orders.class);

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();

        bizContent.put("out_trade_no",orderJson.getOrderNo());
//bizContent.put("trade_no", "2014112611001004680073956707");
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
            Object jsonObject = JSONObject.parseObject(response.getBody()).get("alipay_trade_query_response");
            Object payStatus = JSONObject.parseObject(jsonObject.toString()).get("trade_status");

            ordersService.checkOrderPay(payStatus,orderJson);


        } else {
            System.out.println("调用失败");
            System.out.println(response.getBody());
        }

        return new JsonResult(Code.OK,null);
    }

    private String zfbPay(String orderNo, Long orderId, BigDecimal actualMoney,Long addressId, String token, String payType) throws AlipayApiException {

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(AlipayConfig.notify_url);
        //同步跳转地址，仅支持http/https
        request.setReturnUrl(AlipayConfig.return_url);
        /******必传参数******/
        JSONObject bizContent = new JSONObject();
        //商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no", orderNo);
        //支付金额，最小值0.01元
        bizContent.put("total_amount",actualMoney);
        //订单标题，不可使用特殊符号
        bizContent.put("subject", "kid商城订单支付");
        //电脑网站支付场景固定传值FAST_INSTANT_TRADE_PAY
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

        /******可选参数******/
        //bizContent.put("time_expire", "2022-08-01 22:00:00");

        //// 商品明细信息，按需传入
        //JSONArray goodsDetail = new JSONArray();
        //JSONObject goods1 = new JSONObject();
        //goods1.put("goods_id", "goodsNo1");
        //goods1.put("goods_name", "子商品1");
        //goods1.put("quantity", 1);
        //goods1.put("price", 0.01);
        //goodsDetail.add(goods1);
        //bizContent.put("goods_detail", goodsDetail);

        //// 扩展信息，按需传入
        //JSONObject extendParams = new JSONObject();
        //extendParams.put("sys_service_provider_id", "2088511833207846");
        //bizContent.put("extend_params", extendParams);

        // 设置请求参数
        request.setBizContent(bizContent.toString());
        // 发送请求，获取用户支付页面
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
            return response.getBody();
        } else {
            System.out.println("调用失败");
        }

        return "支付成功";
    }
}
