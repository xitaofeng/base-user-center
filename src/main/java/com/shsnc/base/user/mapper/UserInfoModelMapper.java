package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.UserInfoCondition;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.util.sql.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

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

    int updateUserInfoByUserIds(@Param("userInfoModel") UserInfoModel userInfoModel, @Param("userIds") Collection<Long> userIds);

    int getTotalCountByCondition(@Param("condition") UserInfoCondition condition);

    List<UserInfoModel> getPageByCondition(@Param("condition") UserInfoCondition condition, @Param("pagination") Pagination pagination);

    List<UserInfoModel> getListByCondition(@Param("condition")UserInfoCondition condition);

    List<UserInfoModel> getByUserIds(@Param("userIds") Collection<Long> userIds);
}