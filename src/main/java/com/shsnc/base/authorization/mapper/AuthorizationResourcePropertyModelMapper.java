package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationResourcePropertyModel;
import com.shsnc.base.authorization.model.condition.AuthorizationResourcePropertyCondition;
import com.shsnc.base.util.sql.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Elena on 2017/6/9.
 */
public interface AuthorizationResourcePropertyModelMapper {

    int addAuthorizationResourcePropertyModel(AuthorizationResourcePropertyModel authorizationResourcePropertyModel);

    Integer editAuthorizationResourcePropertyModel(AuthorizationResourcePropertyModel authorizationResourcePropertyModel);

    Integer batchDeleteAuthorizationResourceProperty(List<Long> idList);

    AuthorizationResourcePropertyModel getAuthorizationResourcePropertyModelById(Long propertyId);

    List<AuthorizationResourcePropertyModel> getAuthorizationResourcePropertyModelList(@Param("condition") AuthorizationResourcePropertyCondition condition);

    List<AuthorizationResourcePropertyModel> getCountByResourceTypeAndPropertyValue(@Param("resourceType") Integer resourceType, @Param("propertyValue") Integer propertyValue);

    /**
     * 总数获取
     *
     * @param condition
     * @return
     */
    Integer getTotalCountByCondition(@Param("condition") AuthorizationResourcePropertyCondition condition);

    /**
     * 分页查询
     *
     * @param condition
     * @param pagination
     * @return
     */
    List<AuthorizationResourcePropertyModel> getPageByCondition(@Param("condition") AuthorizationResourcePropertyCondition condition, @Param("pagination") Pagination pagination);

}
