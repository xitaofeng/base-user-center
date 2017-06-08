package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.ExtendPropertyCondition;
import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.util.sql.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtendPropertyModelMapper {

    int deleteByPrimaryKey(Long propertyId);

    int insert(ExtendPropertyModel record);

    int insertSelective(ExtendPropertyModel record);

    ExtendPropertyModel selectByPrimaryKey(Long propertyId);

    int updateByPrimaryKeySelective(ExtendPropertyModel record);

    int updateByPrimaryKey(ExtendPropertyModel record);

    ExtendPropertyModel existExtendProperty(ExtendPropertyModel extendPropertyModel);

    int getTotalCountByCondition(@Param("condition") ExtendPropertyCondition condition);

    List<ExtendPropertyModel> getPageByCondition(@Param("condition") ExtendPropertyCondition condition, @Param("pagination") Pagination pagination);

    List<ExtendPropertyModel> getExtendPropertyList();

}