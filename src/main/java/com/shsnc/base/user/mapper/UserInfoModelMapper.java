package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.UserInfoModel;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserInfoModelMapper {

    int deleteByPrimaryKey(Long userId);

    int insert(UserInfoModel record);

    int insertSelective(UserInfoModel record);

    UserInfoModel selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(UserInfoModel record);

    int updateByPrimaryKey(UserInfoModel record);

    List<UserInfoModel> findUserInfoList(UserInfoModel userInfoModel);

    UserInfoModel existUserInfo(UserInfoModel userInfoModel);

    List<Long> getUserIdsByUserIds(@Param("userIds") Collection<Long> userIds);
}