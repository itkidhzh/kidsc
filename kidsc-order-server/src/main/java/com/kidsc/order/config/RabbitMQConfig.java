package com.kidsc.order.config;

import org.springframework.amqp.core.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public FanoutExchange orderPayExchange(){
        return new FanoutExchange("orderPayExchange");
    }

    @Bean
    public Queue pointQueue(){
        return new Queue("pointQueue");
    }

    @Bean
    public Queue wuLiuQueue(){
        return new Queue("wuLiuQueue");
    }

    @Bean
    public Binding pointBinding(){
        return new Binding("pointQueue", Binding.DestinationType.QUEUE,"orderPayExchange","",null);
    }

    @Bean
    public Binding wuLiuBinding(){
        return new Binding("wuLiuQueue", Binding.DestinationType.QUEUE,"orderPayExchange","",null);

    }

    @Bean
    public Queue orderQueue(){
        Map<String,Object> argument = new HashMap<String,Object>();
        argument.put("x-dead-letter-exchange","deadLetterExchange");
        argument.put("x-dead-letter-routing-key","deadLetterKey");
        return new Queue("orderQueue",true,false,false,argument);
    }
    @Bean
    public FanoutExchange orderExchange(){
        return new FanoutExchange("orderExchange");
    }

    @Bean
    public Binding orderBinding(){
        return new Binding("orderQueue", Binding.DestinationType.QUEUE,"orderExchange","",null);
    }

    @Bean
    public Queue deadLetterQueue(){
        return new Queue("deadLetterQueue");
    }

    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange("deadLetterExchange");
    }

    @Bean
    public Binding deadLetterBinding(){
        return new Binding("deadLetterQueue", Binding.DestinationType.QUEUE,"deadLetterExchange","deadLetterKey",null);
    }

}
