package com.kidsc.goods.mapper;

import com.kidsc.goods.model.GoodsAttribute;

public interface GoodsAttributeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsAttribute record);

    int insertSelective(GoodsAttribute record);

    GoodsAttribute selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsAttribute record);

    int updateByPrimaryKey(GoodsAttribute record);
}