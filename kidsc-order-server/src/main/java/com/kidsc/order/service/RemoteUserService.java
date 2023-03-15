package com.kidsc.order.service;

import com.kidsc.commons.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@FeignClient("UserService")
public interface RemoteUserService {

    @GetMapping("/getUserId")
    JsonResult<Long> getUserId(@RequestParam String token);
}
