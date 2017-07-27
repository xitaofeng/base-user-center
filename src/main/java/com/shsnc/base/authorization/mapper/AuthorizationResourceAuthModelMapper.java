package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.bean.UserDataAuthorization;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Elena on 2017/6/9.
 */
public interface AuthorizationResourceAuthModelMapper {

    int addAuthorizationResourceAuthModel(AuthorizationResourceAuthModel authorizationResourceAuthModel);

    Integer addBatchAuthorizationResourceAuthModel(List<AuthorizationResourceAuthModel> authorizationResourceAuthModels);

    /**
     * 删除资源数据权限
     * @param resourceTypeCode
     * @param authType
     * @param authValue
     * @param resourceId
     * @return
     */
    Integer deleteResourceDataAuthorization(@Param("authType") Integer authType,@Param("authValue") Long authValue,@Param("resourceTypeCode") String resourceTypeCode,@Param("resourceId") Long resourceId);

    /**
     * 获取用户授权列表
     * @param userId
     * @param roleIds
     * @return
     */
    List<UserDataAuthorization> getUserDataAuthorization(@Param("userId") Long userId,@Param("roleIds") List<Long> roleIds);
}
