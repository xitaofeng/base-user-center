package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.mapper.AuthorizationRoleRelationModelMapper;
import com.shsnc.base.authorization.model.AuthorizationRoleRelationModel;
import com.shsnc.base.util.config.BizException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elena on 2017/6/7.
 */
@Service
public class AuthorizationRoleRelationService {

    @Autowired
    private AuthorizationRoleRelationModelMapper authorizationRoleRelationModelMapper;

    /**
     * 批量插入数据返回 插入条数
     *
     * @param roleId
     * @param authorizationIdList
     * @return
     */
    public boolean roleBatchAuthorization(Integer roleId, List<Integer> authorizationIdList) throws BizException {
        if (roleId == null) {
            throw new BizException("选择授权的角色");
        }
        if (CollectionUtils.isEmpty(authorizationIdList)) {
            throw new BizException("选择授权的权限");
        }
        //删除该角色的所有权限 ，然后重新添加该角色的权限
        authorizationRoleRelationModelMapper.deleteAuthorizationRoleRelationModelByRoleId(roleId);

        List<AuthorizationRoleRelationModel> authorizationRoleRelationModels = new ArrayList<>();
        authorizationIdList.forEach(authorizationId -> {
            AuthorizationRoleRelationModel authorizationRoleRelationModel = new AuthorizationRoleRelationModel();
            authorizationRoleRelationModel.setAuthorizationId(authorizationId);
            authorizationRoleRelationModel.setRoleId(roleId);
            authorizationRoleRelationModels.add(authorizationRoleRelationModel);
        });
        return authorizationRoleRelationModelMapper.batchAddAuthorizationRoleRelationModel(authorizationRoleRelationModels) > 0;
    }

    /**
     * 根据角色ID 获取 角色所有权限
     *
     * @param roleId
     * @return
     */
    public List<Integer> getAuthorizationIdByRoleId(Integer roleId) {
        return authorizationRoleRelationModelMapper.getAuthorizationIdByRoleId(roleId);
    }
}
