package com.kidsc.goods.controller;

import com.kidsc.commons.Code;
import com.kidsc.commons.JsonResult;
import com.kidsc.goods.service.GoodsService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.json.Json;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@RestController
@CrossOrigin
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @GetMapping("/decrGoodsStore")
    public Object decrGoodsStore(Long goodsId,Integer buyNum){


        BigDecimal price = goodsService.decrGoodsStore(goodsId,buyNum);

        if (price == null){
            return new JsonResult(Code.NO_GOODS_STORE,"没有库存");
        }

        return new JsonResult<BigDecimal>(Code.OK,price);
    }

    @GetMapping("/incrGoodsStore")
    public void incrGoodsStore(Long goodsId,Integer buyNum){

        goodsService.incrGoodsStore(goodsId,buyNum);

    }

}
