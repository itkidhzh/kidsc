package com.kidsc.user.service.impl;

import com.kidsc.user.mapper.AddressMapper;
import com.kidsc.user.model.Address;
import com.kidsc.user.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName
 * @Description: TODO
 * @Author: kidhzh@outlook.com
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Override
    public List<Address> getUserAddress(long userId) {

        List<Address> userAddressList = addressMapper.getUserAddress(userId);

        return userAddressList;
    }
}
