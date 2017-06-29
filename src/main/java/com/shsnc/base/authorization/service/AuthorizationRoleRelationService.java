package com.shsnc.base.authorization.service;

import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.UserInfo;
import com.shsnc.base.authorization.mapper.*;
import com.shsnc.base.authorization.model.AuthorizationRoleRelationModel;
import com.shsnc.base.util.config.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private AuthorizationUserRoleRelationModelMapper authorizationUserRoleRelationModelMapper;

    @Autowired
    private AuthorizationGroupRoleRelationModelMapper authorizationGroupRoleRelationModelMapper;

    @Autowired
    private AuthorizationInfoModelMapper authorizationInfoModelMapper;

    @Autowired
    private AuthorizationRoleModelMapper authorizationRoleModelMapper;

    @Autowired
    private UserModuleService userModuleService;

    /**
     * 批量插入数据返回 插入条数
     *
     * @param roleId
     * @param authorizationIdList
     * @return
     */
    @Transactional
    public boolean roleBatchAuthorization(Long roleId, List<Long> authorizationIdList) throws BizException {
        if (roleId == null) {
            throw new BizException("选择授权的角色");
        }
        if (authorizationRoleModelMapper.getAuthorizationRoleModelByRoleId(roleId) == null) {
            throw new BizException("无效角色");
        }
        if (CollectionUtils.isEmpty(authorizationIdList)) {
            throw new BizException("选择授权的权限");
        }
        //删除该角色的所有权限 ，然后重新添加该角色的权限
        authorizationRoleRelationModelMapper.deleteAuthorizationRoleRelationModelByRoleId(roleId);

        List<AuthorizationRoleRelationModel> authorizationRoleRelationModels = new ArrayList<>();
        for (int i = 0; i < authorizationIdList.size(); i++) {
            Long authorizationId = authorizationIdList.get(i);
            if (authorizationInfoModelMapper.getAuthorizationByAuthorizationId(authorizationId) == null) {
                throw new BizException("无效权限数据");
            }
            AuthorizationRoleRelationModel authorizationRoleRelationModel = new AuthorizationRoleRelationModel();
            authorizationRoleRelationModel.setAuthorizationId(authorizationId);
            authorizationRoleRelationModel.setRoleId(roleId);
            authorizationRoleRelationModels.add(authorizationRoleRelationModel);
        }
        return authorizationRoleRelationModelMapper.batchAddAuthorizationRoleRelationModel(authorizationRoleRelationModels) > 0;
    }

    /**
     * 根据角色ID 获取 角色所有权限
     *
     * @param roleId
     * @return
     */
    public List<String> getAuthorizationIdByRoleId(Long roleId) {
        return authorizationRoleRelationModelMapper.getAuthorizationIdByRoleId(roleId);
    }

    /**
     * 根据用户ID 获取 用户拥有的所有权限
     * userId 为空取当前登录用户
     *
     * @param userId
     * @return
     */
    public List<String> getAuthorizationCodeByUserId(Long userId) throws BizException {
        if (userId == null) {
            UserInfo userInfo = ThreadContext.getUserInfo();
            if (userInfo != null) {
                userId = userInfo.getUserId();
            } else {
                throw new BizException("用户超时,请先登录");
            }
        }

        List<Long> roleIds = userModuleService.getRoleIdsByUserId(userId);

        if (!CollectionUtils.isEmpty(roleIds)) {
            return authorizationRoleRelationModelMapper.getAuthorizationCodeByRoleIds(roleIds);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 验证用户是否拥有该权限编码
     *
     * @param userId
     * @param authorizationCode
     * @return
     */
    public boolean userHaveAuthorization(Long userId, String authorizationCode) throws BizException {
        if (authorizationCode == null) {
            throw new BizException("选择权限");
        }
        List<String> authorizationCodeList = getAuthorizationCodeByUserId(userId);
        return authorizationCodeList.contains(authorizationCode);
    }
}
