package com.kidsc.order.service;

import com.kidsc.commons.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@FeignClient("GoodsService")
public interface RemoteGoodsService {
    @GetMapping("/decrGoodsStore")
    JsonResult<BigDecimal> decrGoodsStore(@RequestParam Long goodsId,@RequestParam Integer buyNum);

    @GetMapping("/incrGoodsStore")
    void incrGoodsStore(@RequestParam Long goodsId,@RequestParam Integer buyNum);
}
