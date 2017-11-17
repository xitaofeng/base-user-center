package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.mapper.AuthorizationUserRoleRelationModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户模块服务接口
 * Created by Elena on 2017/6/9.
 */
@Component
public class UserModuleService {

    @Autowired
    private AuthorizationUserRoleRelationModelMapper authorizationUserRoleRelationModelMapper;
    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     */
    public List<Long> getRoleIdsByUserId(Long userId) {
        return authorizationUserRoleRelationModelMapper.getRoleIdByUserId(userId);
    }
}
