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
@RequestMapper("/authorization/script")
public class AuthorizationScriptHandler implements RequestHandler {

    @Autowired
    private AuthorizationRightsService authorizationRightsService;

    /**
     * 授权脚本
     */
    @RequestMapper("/addRights")
    @Validate
    public boolean addRights(@NotNull Long scriptId, @NotNull List<Long> groupIds) throws BaseException {
        authorizationRightsService.authorize(DataObject.SCRIPT ,scriptId, groupIds, DataOperation.ALL, false);
        return true;
    }

    /**
     * 授权脚本
     */
    @RequestMapper("/updateRights")
    @Validate
    public boolean updateRights(@NotNull Long scriptId, @NotNull List<Long> groupIds) throws BaseException {
        authorizationRightsService.authorize(DataObject.SCRIPT ,scriptId, groupIds, DataOperation.ALL, true);
        return true;
    }

    /**
     * 移除授权
     */
    @RequestMapper("/removeRights")
    @Validate
    public boolean removeRights(@NotNull Long scriptId) throws BaseException {
        authorizationRightsService.deleteByObjectId(DataObject.SCRIPT, scriptId);
        return true;
    }

    @RequestMapper("/checkOne")
    @Validate
    public boolean checkOne(@NotNull Long scriptId){
        return authorizationRightsService.checkPermisson(DataObject.SCRIPT, scriptId, DataOperation.ALL);
    }

    @RequestMapper("/checkMany")
    @Validate
    public boolean checkMany(@NotEmpty List<Long> scriptIds){
        return authorizationRightsService.checkPermisson(DataObject.SCRIPT, scriptIds, DataOperation.ALL);
    }

    @RequestMapper("/getAllIds")
    @Validate
    public List<Long> getAllIds() throws BizException {
        return authorizationRightsService.getObjectIds(DataObject.SCRIPT, DataOperation.ALL);
    }


    @RequestMapper("/getGroups")
    @Validate
    public List<Group> getGroups(Long scriptId) throws BaseException {
        List<GroupModel> groupModels = authorizationRightsService.getGroupsByObjectId(DataObject.SCRIPT, scriptId);
        return JsonUtil.convert(groupModels, List.class, Group.class);
    }
}
