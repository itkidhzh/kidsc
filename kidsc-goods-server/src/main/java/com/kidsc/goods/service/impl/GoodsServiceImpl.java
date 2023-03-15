package com.kidsc.goods.service.impl;

import com.kidsc.commons.Code;
import com.kidsc.commons.JsonResult;
import com.kidsc.goods.mapper.GoodsInfoMapper;
import com.kidsc.goods.service.GoodsService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsInfoMapper goodsInfoMapper;

    @Override
    @GlobalTransactional
    public BigDecimal decrGoodsStore(Long goodsId, Integer buyNum) {
        // 这一行代码会被并发操作
        int result = goodsInfoMapper.decrGoodsStore(goodsId,buyNum);
        if (result == 0){
            return null;
        }
        BigDecimal price = goodsInfoMapper.selectPriceByGoodsId(goodsId);
        return price;
    }

    @Override
    @GlobalTransactional
    public void incrGoodsStore(Long goodsId, Integer buyNum) {
        goodsInfoMapper.incrGoodsStore(goodsId,buyNum);
    }

}
