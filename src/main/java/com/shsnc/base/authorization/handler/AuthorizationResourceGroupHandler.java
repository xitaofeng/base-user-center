package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.authorization.config.DataObject;
import com.shsnc.base.authorization.config.DataOperation;
import com.shsnc.base.authorization.model.AuthorizationRightsModel;
import com.shsnc.base.authorization.service.AuthorizationRightsService;
import com.shsnc.base.user.bean.Group;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houguangqiang
 * @date 2017-08-09
 * @since 1.0
 */
@Component
@LoginRequired
@RequestMapper("/authorization/internal/resourceGroup")
public class AuthorizationResourceGroupHandler implements RequestHandler {

    @Autowired
    private AuthorizationRightsService authorizationRightsService;

    /**
     * 新增授权
     */
    @RequestMapper("/addRights")
    @Validate
    public boolean addRights(@NotNull Long resourceGroupId, @NotNull List<Long> groupIds) throws BaseException {
        authorizationRightsService.authorize(DataObject.RESOURCE_GROUP, resourceGroupId, groupIds, DataOperation.ALL, false);
        return true;
    }

    /**
     * 更新授权
     */
    @RequestMapper("/updateRights")
    @Validate
    public boolean updateRights(@NotNull Long resourceGroupId, @NotNull List<Long> groupIds) throws BaseException {
        authorizationRightsService.authorize(DataObject.RESOURCE_GROUP, resourceGroupId, groupIds, DataOperation.ALL, true);
        return true;
    }

    /**
     * 移除授权
     */
    @RequestMapper("/removeRights")
    @Validate
    public boolean removeRights(@NotNull Long resourceGroupId) throws BaseException {
        authorizationRightsService.deleteByObjectId(DataObject.RESOURCE_GROUP, resourceGroupId);
        return true;
    }

    @RequestMapper("/checkOne")
    @Validate
    public boolean checkOne(@NotNull Long resourceGroupId){
        return authorizationRightsService.checkPermisson(DataObject.RESOURCE_GROUP, resourceGroupId, DataOperation.ALL);
    }

    @RequestMapper("/checkMany")
    @Validate
    public boolean checkMany(@NotEmpty List<Long> resourceGroupIds){
        return authorizationRightsService.checkPermisson(DataObject.RESOURCE_GROUP, resourceGroupIds, DataOperation.ALL);
    }

    @RequestMapper("/getAllIds")
    @Validate
    public List<Long> getAllIds() throws BizException {
        return authorizationRightsService.getObjectIds(DataObject.RESOURCE_GROUP, DataOperation.ALL);
    }

    @RequestMapper("/getGroups")
    @Validate
    public List<Group> getGroups(Long resourceGroupId) throws BaseException {
        List<GroupModel> groupModels = authorizationRightsService.getGroupsByObjectId(DataObject.RESOURCE_GROUP, resourceGroupId);
        return JsonUtil.convert(groupModels, List.class, Group.class);
    }
    
    @RequestMapper("/getResourceGroups")
    @Validate
    public List<Long> getResourceGroups(List<Long> groupIds) throws BaseException {
        
        List<Long> resourceGroupIds = authorizationRightsService.getRightsByGroupIds(DataObject.RESOURCE_GROUP, groupIds);
        
      
        return resourceGroupIds;
    }
    
    
}
