package com.kidsc.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.kidsc.commons.Code;
import com.kidsc.commons.JsonResult;
import com.kidsc.user.model.Address;
import com.kidsc.user.model.User;
import com.kidsc.user.service.AddressService;
import com.kidsc.user.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@RestController
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private AddressService addressService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("/login")
    public Object login(User user){

        long errNum = 0;

        String loginErrNum = stringRedisTemplate.opsForValue().get("loginErrNum:" + user.getPhone());

        if (loginErrNum != null && Long.valueOf(loginErrNum) >= 5){
            return new JsonResult(Code.ERROR,"账号今日已被冻结","");
        }

        int result = userService.login(user);

        if (result != 0){
            errNum = stringRedisTemplate.opsForValue().increment("loginErrNum:" + user.getPhone());
            if (errNum == 1){
                lockUser(user.getPhone());
            }
            return new JsonResult(Code.ERROR,"账号密码不匹配",result);
        }

        String token = UUID.randomUUID().toString().replaceAll("-","");

        Duration timeOut = Duration.ofSeconds(30*60);

        stringRedisTemplate.opsForValue().set("userToken：" + token, JSONObject.toJSONString(user),timeOut);

        Map resultData = new HashMap();

        resultData.put("token",token);
        resultData.put("phone",user.getPhone());
        resultData.put("id",user.getId());
        resultData.put("nickName",user.getNickName());



        return new JsonResult(Code.OK,"登录成功",resultData);
    }
    @GetMapping("/getUserId")
    public Object getUserId(String token){
        String userJson = stringRedisTemplate.opsForValue().get("userToken：" + token);

        if (userJson == null){
            return new JsonResult(Code.NO_LOGIN,null);
        }
        Duration timeOut = Duration.ofSeconds(30*60);
        stringRedisTemplate.expire("userToken：" + token,timeOut);
        Long userId = JSONObject.parseObject(userJson).getBigInteger("id").longValue();

        return new JsonResult(Code.OK,userId);
    }

    private void lockUser(String phone) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(calendar.HOUR,0);
        calendar.set(calendar.MINUTE,0);
        calendar.set(calendar.SECOND,0);

        calendar.add(calendar.DAY_OF_MONTH,1);

        Duration timeout = Duration.ofSeconds(calendar.getTimeInMillis()/1000 - System.currentTimeMillis()/1000);

        stringRedisTemplate.expire("loginErrNum:" + phone,timeout);
    }

    @PostMapping("/reg")
    public Object reg(User user){

        if (user.getPhone() == null){
            return new JsonResult(Code.ERROR,"手机号不能为空","");
        }
        if (user.getPassword() == null){
            return new JsonResult(Code.ERROR,"密码不能为空","");
        }

        int result = userService.regUser(user);

        int id = userService.selIdByPhone(user.getPhone());

        if (result == 1){
            return new JsonResult(Code.ERROR,"手机号已存在请重新注册","");
        }
        String token = UUID.randomUUID().toString().replaceAll("-","");

        Map resultData = new HashMap();

        resultData.put("token",token);
        resultData.put("id",id);
        resultData.put("phone",user.getPhone());
        resultData.put("nickName",user.getNickName());


        return new JsonResult(Code.OK,"注册成功",resultData);
    }
    @GetMapping("/confirmUserAddress")
    public Object confirmUserAddress(String token){

        String userJson = stringRedisTemplate.opsForValue().get("userToken：" + token);
        if (userJson == null){
            return new JsonResult(Code.NO_LOGIN,"没有登录");
        }

        JSONObject jsonObject = JSONObject.parseObject(userJson);

        long userId = jsonObject.getBigInteger("id").longValue();

        List<Address> result = addressService.getUserAddress(userId);

        return new JsonResult(Code.OK,result);
    }
}
