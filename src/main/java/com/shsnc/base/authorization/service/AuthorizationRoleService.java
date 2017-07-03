

package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.config.AuthorizationConstant;
import com.shsnc.base.authorization.mapper.AuthorizationGroupRoleRelationModelMapper;
import com.shsnc.base.authorization.mapper.AuthorizationRoleModelMapper;
import com.shsnc.base.authorization.mapper.AuthorizationUserRoleRelationModelMapper;
import com.shsnc.base.authorization.model.AuthorizationRoleModel;
import com.shsnc.base.authorization.model.condition.AuthorizationRoleCondition;
import com.shsnc.base.util.StringUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Elena on 2017/6/7.
 */
@Service
public class AuthorizationRoleService {

    @Autowired
    private AuthorizationRoleModelMapper authorizationRoleModelMapper;

    @Autowired
    private AuthorizationUserRoleRelationModelMapper authorizationUserRoleRelationModelMapper;

    @Autowired
    private AuthorizationGroupRoleRelationModelMapper authorizationGroupRoleRelationModelMapper;

    /**
     * 添加 角色 信息
     *
     * @param authorizationRoleModel
     * @return
     * @throws Exception
     */
    public Long addAuthorizationRoleModel(AuthorizationRoleModel authorizationRoleModel) throws Exception {
        if (isRoleName(authorizationRoleModel)) {
            throw new BizException("角色名称重复");
        }
        if (isRoleCode(authorizationRoleModel)) {
            throw new BizException("角色编码重复");
        }
        if (authorizationRoleModel.getIsBuilt() == null) {
            authorizationRoleModel.setIsBuilt(AuthorizationRoleModel.EnumIsBuilt.FALSE.getValue());
        }
        if (authorizationRoleModel.getOrders() != null) {
            authorizationRoleModel.setOrders(1);
        }
        authorizationRoleModel.setCreateTime(System.currentTimeMillis());
        int count = authorizationRoleModelMapper.addAuthorizationRoleModel(authorizationRoleModel);
        if (count > 0) {
            return authorizationRoleModel.getRoleId();
        } else {
            throw new BizException("角色添加失败");
        }
    }

    /**
     * 编辑 角色 信息
     *
     * @param authorizationRoleModel
     * @return
     * @throws Exception
     */
    public boolean editAuthorizationRoleModel(AuthorizationRoleModel authorizationRoleModel) throws Exception {
        if (authorizationRoleModel != null && authorizationRoleModel.getRoleId() != null) {
            AuthorizationRoleModel editAuthorizationRoleModel = authorizationRoleModelMapper.getAuthorizationRoleModelByRoleId(authorizationRoleModel.getRoleId());
            if (editAuthorizationRoleModel != null) {
                BeanUtils.copyProperties(authorizationRoleModel, editAuthorizationRoleModel);
                if (isRoleName(editAuthorizationRoleModel)) {
                    throw new BizException("角色名称重复");
                }
                if (isRoleCode(editAuthorizationRoleModel)) {
                    throw new BizException("角色编码重复");
                }
                if (authorizationRoleModel.getIsBuilt() == null) {
                    authorizationRoleModel.setIsBuilt(AuthorizationRoleModel.EnumIsBuilt.FALSE.getValue());
                }
                Integer count = authorizationRoleModelMapper.editAuthorizationRoleModel(authorizationRoleModel);
                if (count != null && count > 0) {
                    return true;
                } else {
                    throw new BizException("数据编辑失败");
                }
            } else {
                throw new BizException("编辑数据不存在");
            }
        } else {
            throw new BizException("选择编辑数据");
        }
    }

    /**
     * 删除权限信息
     *
     * @param roleIdList
     * @return
     * @throws Exception
     */
    public boolean batchDeleteAuthorizationRole(List<Long> roleIdList) throws Exception {
        if (!CollectionUtils.isEmpty(roleIdList)) {
            for (Long roleId : roleIdList) {
                AuthorizationRoleModel authorizationRoleModel = authorizationRoleModelMapper.getByRoleId(roleId);
                if (authorizationRoleModel != null) {
                    if (AuthorizationRoleModel.EnumIsBuilt.TRUE.getValue() == authorizationRoleModel.getIsBuilt()) {
                        throw new BizException("内置数据不支持删除");
                    }
                    if (!CollectionUtils.isEmpty(authorizationUserRoleRelationModelMapper.getUserIdByRoleId(roleId))) {
                        String roleName = authorizationRoleModel.getRoleName();
                        throw new BizException("角色【" + roleName + "】存在使用的用户");
                    }
                    if (!CollectionUtils.isEmpty(authorizationGroupRoleRelationModelMapper.getGroupIdByRoleId(roleId))) {
                        String roleName = authorizationRoleModel.getRoleName();
                        throw new BizException("角色【" + roleName + "】存在使用的用户组");
                    }
                } else {
                    throw new BizException("无效数据");
                }
            }
            return authorizationRoleModelMapper.batchDeleteAuthorizationRole(roleIdList) > 0;
        } else {
            throw new BizException("请选择需要删除的数据");
        }
    }

    /**
     * 查询列表
     *
     * @param condition
     * @return
     * @throws Exception
     */
    public List<AuthorizationRoleModel> getAuthorizationRoleModelList(AuthorizationRoleCondition condition) throws Exception {
        return authorizationRoleModelMapper.getAuthorizationRoleModelList(condition);
    }

    public QueryData getPageList(AuthorizationRoleCondition condition, Pagination pagination) {
        QueryData queryData = new QueryData(pagination);
        int totalCount = authorizationRoleModelMapper.getTotalCountByCondition(condition);
        queryData.setRowCount(totalCount);
        List<AuthorizationRoleModel> list = authorizationRoleModelMapper.getPageByCondition(condition, pagination);
        queryData.setRecords(list);
        return queryData;
    }

    /**
     * 查询对象
     *
     * @param roleId
     * @return
     * @throws Exception
     */
    public AuthorizationRoleModel getAuthorizationRoleByRoleId(Long roleId) throws Exception {
        if (roleId == null) {
            throw new BizException("无效数据");
        }
        AuthorizationRoleModel authorizationRoleModel = authorizationRoleModelMapper.getAuthorizationRoleModelByRoleId(roleId);
        return authorizationRoleModel;
    }

    /**
     * 根据用户ID获取角色主键列表
     * @param userId
     * @return
     */
    public List<Long> getRoleIdsByUserId(Long userId) {
        return authorizationUserRoleRelationModelMapper.getRoleIdByUserId(userId);
    }


    /**
     * 是否为管理员
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean isAdmin(Long userId) throws BizException {
        if (userId == null) {
            throw new BizException("无效数据");
        }
        String roleCode = AuthorizationConstant.ROLE_CODE_ADMIN;
        return authorizationUserRoleRelationModelMapper.getCountByUserIdAndRoleCode(userId, roleCode) > 0;
    }

    /**
     * 是否为超级管理员
     *
     * @param userId
     * @return
     * @throws BizException
     */
    public boolean isSuperAdmin(Long userId) throws BizException {
        if (userId == null) {
            throw new BizException("无效数据");
        }
        String roleCode = AuthorizationConstant.ROLE_CODE_SUPER_ADMIN;
        return authorizationUserRoleRelationModelMapper.getCountByUserIdAndRoleCode(userId, roleCode) > 0;
    }

    /**
     * 角色名称是否重复(true重复,false不重复)
     *
     * @param authorizationRoleModel
     * @return
     */
    public boolean isRoleName(AuthorizationRoleModel authorizationRoleModel) throws BizException {
        Long roleId = authorizationRoleModel.getRoleId();
        String roleName = authorizationRoleModel.getRoleName();
        if (StringUtil.isNotEmpty(roleName)) {
            List<AuthorizationRoleModel> list = authorizationRoleModelMapper.getListByRoleName(roleName);
            if (CollectionUtils.isEmpty(list)) {
                return false;
            }
            //authorizationId == null 添加否则编辑
            return checkDataRepetition(roleId, list);
        } else {
            throw new BizException("权限名称不能为空");
        }
    }

    /**
     * 角色编码是否重复(true重复,false不重复)
     *
     * @param authorizationRoleModel
     * @return
     */
    public boolean isRoleCode(AuthorizationRoleModel authorizationRoleModel) throws BizException {
        Long roleId = authorizationRoleModel.getRoleId();
        String roleCode = authorizationRoleModel.getRoleCode();
        if (StringUtil.isNotEmpty(roleCode)) {
            List<AuthorizationRoleModel> list = authorizationRoleModelMapper.getListByRoleName(roleCode);
            if (CollectionUtils.isEmpty(list)) {
                return false;
            }
            return checkDataRepetition(roleId, list);
        } else {
            throw new BizException("权限编码不能为空");
        }
    }

    /**
     * 验证数据是否重复
     *
     * @param roleId
     * @param list
     * @return
     */
    private boolean checkDataRepetition(Long roleId, List<AuthorizationRoleModel> list) {
        //authorizationId == null 添加否则编辑
        if (roleId != null) {
            boolean isRepetition = false;
            for (int i = 0; i < list.size(); i++) {
                AuthorizationRoleModel authorizationRoleModel = list.get(i);
                if (roleId != authorizationRoleModel.getRoleId()) {
                    isRepetition = true;
                    break;
                }
            }
            return isRepetition;
        } else {
            return list.size() > 0;
        }
    }
}
