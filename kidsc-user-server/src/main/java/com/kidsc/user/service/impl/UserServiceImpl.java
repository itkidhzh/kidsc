package com.kidsc.user.service.impl;

import com.kidsc.user.mapper.UserMapper;
import com.kidsc.user.model.User;
import com.kidsc.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int login(User user) {

        User dbUser = userMapper.selectUserByPhone(user.getPhone());

        System.out.println(dbUser.getNickName());


        // 登录的手机在数据中不存在,表示手机号不存在
        if (dbUser == null){
            return 1;
        }
        // 登录的密码不存在于数据库中，表示密码错误
        if (!dbUser.getPassword().equals(user.getPassword()))
        return 2;
        // 把数据库的数据拷贝到user中
        BeanUtils.copyProperties(dbUser,user);

        return 0;
    }

    @Override
    public int regUser(User user) {

        try {
            userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            return 1;
        }

        return 0;
    }

    @Override
    public int selIdByPhone(String phone) {
        int id = userMapper.selectIdByPhone(phone);
        return id;
    }
}
