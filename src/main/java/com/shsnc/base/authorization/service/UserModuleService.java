package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.mapper.AuthorizationGroupRoleRelationModelMapper;
import com.shsnc.base.authorization.mapper.AuthorizationUserRoleRelationModelMapper;
import com.shsnc.base.user.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户模块服务接口
 * Created by Elena on 2017/6/9.
 */
@Component
public class UserModuleService {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthorizationUserRoleRelationModelMapper authorizationUserRoleRelationModelMapper;
    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     */
    public List<Long> getRoleIdsByUserId(Long userId) {
        Set<Long> roleIds = new HashSet<>();
        List<Long> userRoleIds = authorizationUserRoleRelationModelMapper.getRoleIdByUserId(userId);
        LOG.debug("根据用户【{}】获取用户直属角色【{}】", userId, userRoleIds);

        userRoleIds.forEach(roleId -> {
            roleIds.add(roleId);
        });
        LOG.debug("根据用户【{}】获取所有角色 角色【{}】", userId, roleIds);
        return new ArrayList<>(roleIds);
    }
}
