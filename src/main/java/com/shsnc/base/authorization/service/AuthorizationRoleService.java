

package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.mapper.AuthorizationInfoModelMapper;
import com.shsnc.base.authorization.mapper.AuthorizationRoleModelMapper;
import com.shsnc.base.authorization.model.AuthorizationInfoModel;
import com.shsnc.base.authorization.model.AuthorizationRoleModel;
import com.shsnc.base.util.StringUtil;
import com.shsnc.base.util.config.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Elena on 2017/6/7.
 */
@Service
public class AuthorizationRoleService {

    @Autowired
    private AuthorizationRoleModelMapper authorizationRoleModelMapper;

    /**
     * 添加 角色 信息
     *
     * @param authorizationRoleModel
     * @return
     * @throws Exception
     */
    public Integer addAuthorizationRoleModel(AuthorizationRoleModel authorizationRoleModel) throws Exception {
        if (isRoleName(authorizationRoleModel)) {
            throw new BizException("角色名称重复");
        }
        authorizationRoleModel.setCreateTime(new Date().getTime());
        if (authorizationRoleModel.getIsBuilt() == null) {
            authorizationRoleModel.setIsBuilt(AuthorizationRoleModel.EnumIsBuilt.FALSE.getValue());
        }
        if (authorizationRoleModel.getOrders() != null) {
            authorizationRoleModel.setOrders(1);
        }
        Integer roleId = authorizationRoleModelMapper.addAuthorizationRoleModel(authorizationRoleModel);
        if (roleId != null) {
            return roleId;
        } else {
            throw new BizException("角色添加失败");
        }
    }

    /**
     * 添加 角色 信息
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
                if (isRoleName(authorizationRoleModel)) {
                    throw new BizException("角色名称重复");
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
    @Transactional
    public boolean batchDeleteAuthorizationRole(List<Integer> roleIdList) throws Exception {
        if (!CollectionUtils.isEmpty(roleIdList)) {
            roleIdList.forEach(authorizationId -> {
                //TODO 验证权限是否 使用
            });
            return authorizationRoleModelMapper.batchDeleteAuthorizationRole(roleIdList) > 0;
        } else {
            throw new BizException("请选择需要删除的数据");
        }
    }

    /**
     * 查询列表
     *
     * @param authorizationRoleModel
     * @return
     * @throws Exception
     */
    public List<AuthorizationRoleModel> getAuthorizationRoleModelList(AuthorizationRoleModel authorizationRoleModel) throws Exception {
        return authorizationRoleModelMapper.getAuthorizationRoleModelList(authorizationRoleModel);
    }

    /**
     * 查询对象
     *
     * @param roleId
     * @return
     * @throws Exception
     */
    public AuthorizationRoleModel getAuthorizationRoleByRoleId(Integer roleId) throws Exception {
        if (roleId == null) {
            throw new BizException("无效数据");
        }
        AuthorizationRoleModel authorizationRoleModel = authorizationRoleModelMapper.getAuthorizationRoleModelByRoleId(roleId);
        return authorizationRoleModel;
    }

    /**
     * 角色名称是否重复(true重复,false不重复)
     *
     * @param authorizationRoleModel
     * @return
     */
    public boolean isRoleName(AuthorizationRoleModel authorizationRoleModel) throws BizException {
        Integer roleId = authorizationRoleModel.getRoleId();
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
     * 验证数据是否重复
     *
     * @param roleId
     * @param list
     * @return
     */
    private boolean checkDataRepetition(Integer roleId, List<AuthorizationRoleModel> list) {
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
