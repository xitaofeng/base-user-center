package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.authorization.config.DataObject;
import com.shsnc.base.authorization.config.DataOperation;
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
import java.util.List;

/**
 * @author houguangqiang
 * @date 2017-08-09
 * @since 1.0
 */
@Component
@LoginRequired
@RequestMapper("/authorization/internal/right")
public class AuthorizationRightInternalHandler implements RequestHandler {

    @Autowired
    private AuthorizationRightsService authorizationRightsService;

    /**
     * 新增授权
     */
    @RequestMapper("/addRights")
    @Validate
    public boolean addRights(@NotNull String sourceType, @NotNull Long sourceId, @NotNull List<Long> groupIds) throws BaseException {
        authorizationRightsService.authorize(DataObject.of(sourceType), sourceId, groupIds, DataOperation.ALL, false);
        return true;
    }

    /**
     * 更新授权
     */
    @RequestMapper("/updateRights")
    @Validate
    public boolean updateRights(@NotNull String sourceType, @NotNull Long sourceId, @NotNull List<Long> groupIds) throws BaseException {
        authorizationRightsService.authorize(DataObject.of(sourceType),sourceId, groupIds, DataOperation.ALL, true);
        return true;
    }

    /**
     * 移除授权
     */
    @RequestMapper("/removeRights")
    @Validate
    public boolean removeRights(@NotNull String sourceType, @NotNull Long sourceId) throws BaseException {
        authorizationRightsService.deleteByObjectId(DataObject.of(sourceType), sourceId);
        return true;
    }

    @RequestMapper("/checkOne")
    @Validate
    public boolean checkOne(@NotNull String sourceType, @NotNull Long sourceId) throws BizException {
        return authorizationRightsService.checkPermisson(DataObject.of(sourceType), sourceId, DataOperation.ALL);
    }

    @RequestMapper("/checkMany")
    @Validate
    public boolean checkMany(@NotNull String sourceType, @NotEmpty List<Long> sourceIds) throws BizException {
        return authorizationRightsService.checkPermisson(DataObject.of(sourceType), sourceIds, DataOperation.ALL);
    }

    @RequestMapper("/getAllIds")
    @Validate
    public List<Long> getAllIds(@NotNull String sourceType) throws BizException {
        return authorizationRightsService.getObjectIds(DataObject.of(sourceType), DataOperation.ALL);
    }

    @RequestMapper("/getGroups")
    @Validate
    public List<Group> getGroups(@NotNull String sourceType, Long sourceId) throws BaseException {
        List<GroupModel> groupModels = authorizationRightsService.getGroupsByObjectId(DataObject.of(sourceType), sourceId);
        return JsonUtil.convert(groupModels, List.class, Group.class);
    }

    @RequestMapper("/getGroupIds")
    @Validate
    public List<Long> getGroupIds(@NotNull String sourceType, Long sourceId) throws BaseException {
        return authorizationRightsService.getGroupIdsByObjectId(DataObject.of(sourceType), sourceId);
    }

}
