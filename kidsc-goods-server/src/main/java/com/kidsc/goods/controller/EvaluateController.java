package com.kidsc.goods.controller;

import com.google.inject.internal.asm.$ClassWriter;
import com.kidsc.goods.model.Evaluate;
import com.kidsc.goods.service.EvaluateService;
import com.kidsc.goods.service.GoodsService;
import com.kidsc.commons.Code;
import com.kidsc.commons.JsonResult;
import com.kidsc.commons.PageBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@RestController
@CrossOrigin
public class EvaluateController {
    @Resource
    private EvaluateService evaluateService;


    @GetMapping("/getEvaluateListPage")
    public Object getEvaluateListPage(Long goodsId,String evaluateLevel,Long pageNo,Long pageSize){

        PageBean<List<Evaluate>> pageBean = evaluateService.getEvaluateListPage(goodsId,evaluateLevel,pageNo,pageSize);


        return new JsonResult<PageBean>(Code.OK,pageBean);
    }
    @GetMapping("/countEvaluateNum")
    public Object countEvaluateNum(Long goodsId){

        Map map = evaluateService.countEvaluateNum(goodsId);

        return new JsonResult<Map>(Code.OK,map);
    }

}
