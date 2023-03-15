package com.kidsc.timer;

import com.alibaba.fastjson.JSONObject;
import com.kidsc.commons.JsonResult;
import com.kidsc.remote.RemoteOrderService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@Component
public class KidscTimer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RemoteOrderService remoteOrderService;

    @Resource
    private AmqpTemplate amqpTemplate;

    /**
     * 定时任务，每五秒钟执行一次，用于扫描掉单数据信息
     */
    @Scheduled(cron = "* * * * * *")
    public void checkPay(){
        // 以当前时间之前的5分钟作为获取数据的最大分数
        long maxScore = System.currentTimeMillis() - 1000*60*5;
        // 从 ZSet 获取分数从0到5分钟之前的所有消息，这些消息5分钟都没有获取到，有可能掉单了
        Set<String> ordersJson = stringRedisTemplate.opsForZSet().rangeByScore("orders", 0, maxScore);


        ordersJson.forEach(orders ->
            {
                JsonResult jsonResult = remoteOrderService.checkOrderPay(orders);
                System.out.println(jsonResult.getCode());

                //if (jsonResult.getCode().equals("10000")){
                //    stringRedisTemplate.opsForZSet().remove("orders",orders);
                //}
            }
        );


    }
}
