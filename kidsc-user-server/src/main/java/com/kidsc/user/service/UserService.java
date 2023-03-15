package com.kidsc.user.service;

import com.kidsc.user.model.User;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
public interface UserService {
    int login(User user);

    int regUser(User user);

    int selIdByPhone(String phone);


}
