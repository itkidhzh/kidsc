package com.kidsc.goods.service;

import java.math.BigDecimal;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
public interface GoodsService {
    BigDecimal decrGoodsStore(Long goodsId, Integer buyNum);

    void incrGoodsStore(Long goodsId, Integer buyNum);
}
