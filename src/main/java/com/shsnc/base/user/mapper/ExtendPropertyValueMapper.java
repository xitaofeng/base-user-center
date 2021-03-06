package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.ExtendPropertyValueModel;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface ExtendPropertyValueMapper {
    int deleteByPrimaryKey(Long propertyValueId);

    int insert(ExtendPropertyValueModel record);

    int insertSelective(ExtendPropertyValueModel record);

    ExtendPropertyValueModel selectByPrimaryKey(Long propertyValueId);

    int updateByPrimaryKeySelective(ExtendPropertyValueModel record);

    int updateByPrimaryKey(ExtendPropertyValueModel record);

    List<ExtendPropertyValueModel> findExtendPropertyValueList(ExtendPropertyValueModel extendPropertyValueModel);

    int insertExtendPropertyValueList(@Param("extendPropertyValueModels") List<ExtendPropertyValueModel> extendPropertyValueModels);

    List<Long> getPropertyIdByUserIds(@Param("userIds") Collection<Long> userIds);

    List<ExtendPropertyValueModel> getPropertyByUserIds(@Param("userIds") Collection<Long> userIds);

    ExtendPropertyValueModel getByUserIdAndPropertyId(@Param("userId") Long userId, @Param("propertyValueId") Long propertyValueId);
}