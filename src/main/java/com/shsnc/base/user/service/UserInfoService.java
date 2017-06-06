package com.shsnc.base.user.service;

import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.UserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class UserInfoService {

    @Autowired
    private UserInfoModelMapper userInfoModelMapper;

    public List<UserInfoModel> getList(UserInfoModel userInfoModel){

        return null;
    }
}
