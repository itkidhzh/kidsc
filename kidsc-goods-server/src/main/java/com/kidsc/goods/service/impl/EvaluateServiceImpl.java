package com.kidsc.goods.service.impl;

import com.kidsc.commons.PageBean;
import com.kidsc.goods.mapper.EvaluateMapper;
import com.kidsc.goods.model.Evaluate;
import com.kidsc.goods.service.EvaluateService;


import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@Service
public class EvaluateServiceImpl implements EvaluateService{

    @Resource
    private EvaluateMapper evaluateMapper;

    @Override
    public PageBean<List<Evaluate>> getEvaluateListPage(Long goodsId, String evaluateLevel, Long pageNo, Long pageSize) {

        PageBean pageBean = new PageBean(pageNo,pageSize);
        List<Evaluate> list;
        long totalNum;

        if (evaluateLevel.equals("img")){
            totalNum = evaluateMapper.countEvaluateHaveImgByGoodsId(goodsId,evaluateLevel);

            pageBean.setTotalNum(totalNum);


            list = evaluateMapper.selectEvaluateHaveImgListPage(goodsId,pageBean.getSkipNum(),pageBean.getPageSize(),evaluateLevel);

        }else {
            totalNum = evaluateMapper.countEvaluateByGoodsId(goodsId,evaluateLevel);

            pageBean.setTotalNum(totalNum);
            //pageBean.setPageCount(totalNum/pageSize > 0 ? totalNum/pageSize + 1 : 1);

            list = evaluateMapper.selectEvaluateListPage(goodsId,pageBean.getSkipNum(),pageBean.getPageSize(),evaluateLevel);
        }


        pageBean.setData(list);

        return pageBean;
    }

    @Override
    public Map countEvaluateNum(Long goodsId) {

        return evaluateMapper.countEvaluateNum(goodsId);
    }


}
