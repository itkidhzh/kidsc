package com.kidsc.goods.service;

import com.kidsc.commons.PageBean;
import com.kidsc.goods.model.Evaluate;


import java.util.List;
import java.util.Map;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
public interface EvaluateService {
    PageBean<List<Evaluate>> getEvaluateListPage(Long goodsId, String evaluateLevel, Long pageNo, Long pageSize);


    Map countEvaluateNum(Long goodsId);
}
