package com.kidsc.user.service;

import com.kidsc.user.model.Address;

import java.util.List;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
public interface AddressService {

    List<Address> getUserAddress(long userId);
}
