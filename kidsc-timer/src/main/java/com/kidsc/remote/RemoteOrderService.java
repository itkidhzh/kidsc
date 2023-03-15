package com.kidsc.remote;

import com.kidsc.commons.JsonResult;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@FeignClient(name = "OrdersService")
public interface RemoteOrderService {
    @RequestMapping("/checkOrderPay")
    JsonResult checkOrderPay(@RequestParam String orders);
}
