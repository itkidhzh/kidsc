package com.kidsc.goods.mapper;

import com.kidsc.goods.model.Evaluate;

import java.util.List;
import java.util.Map;

public interface EvaluateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Evaluate record);

    int insertSelective(Evaluate record);

    Evaluate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Evaluate record);

    int updateByPrimaryKeyWithBLOBs(Evaluate record);

    int updateByPrimaryKey(Evaluate record);

    long countEvaluateByGoodsId(Long goodsId,String evaluateLevel);

    List<Evaluate> selectEvaluateListPage(Long goodsId, Long skipNum, Long pageSize,String evaluateLevel);

    long countEvaluateHaveImgByGoodsId(Long goodsId, String evaluateLevel);

    List<Evaluate> selectEvaluateHaveImgListPage(Long goodsId, Long skipNum, Long pageSize, String evaluateLevel);


    Map countEvaluateNum(Long goodsId);
}