package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.config.RedisConstants;
import com.shsnc.base.authorization.mapper.AuthorizationResourcePropertyModelMapper;
import com.shsnc.base.authorization.model.AuthorizationResourcePropertyModel;
import com.shsnc.base.authorization.model.AuthorizationRoleModel;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.StringUtil;
import com.shsnc.base.util.config.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elena on 2017/6/9.
 */
@Service
public class AuthorizationResourcePropertyService {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    private String Redis_List_key = RedisConstants.RESOURCE_RESOURCE_PROPERTY_LIST;

    @Autowired
    private AuthorizationResourcePropertyModelMapper authorizationResourcePropertyModelMapper;

    public Long addAuthorizationResourcePropertyModel(AuthorizationResourcePropertyModel authorizationResourcePropertyModel) throws Exception {
        if (authorizationResourcePropertyModel != null) {

            if (authorizationResourcePropertyModel.getParentId() == null)
                authorizationResourcePropertyModel.setParentId(new Long(0));

            //TODO 校验同资源下面 属性名称不能重复
            Long id = authorizationResourcePropertyModelMapper.addAuthorizationResourcePropertyModel(authorizationResourcePropertyModel);
            if (id != null) {
                RedisUtil.remove(Redis_List_key);
                return id;
            } else {
                throw new BizException("属性添加失败");
            }
        } else {
            LOG.error("插入对象为空");
            throw new BizException("属性添加失败");
        }
    }

    public boolean editAuthorizationResourcePropertyModel(AuthorizationResourcePropertyModel authorizationResourcePropertyModel) throws Exception {
        if (authorizationResourcePropertyModel != null && authorizationResourcePropertyModel.getId() != null) {
            AuthorizationResourcePropertyModel editAuthorizationRoleModel = authorizationResourcePropertyModelMapper.getAuthorizationResourcePropertyModelById(authorizationResourcePropertyModel.getId());
            if (editAuthorizationRoleModel != null) {
                BeanUtils.copyProperties(authorizationResourcePropertyModel, editAuthorizationRoleModel);
                Integer count = authorizationResourcePropertyModelMapper.editAuthorizationResourcePropertyModel(editAuthorizationRoleModel);
                if (count != null && count > 0) {
                    RedisUtil.remove(Redis_List_key);
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
     * @param idList
     * @return
     * @throws Exception
     */
    @Transactional
    public boolean batchDeleteAuthorizationRole(List<Long> idList) throws Exception {
        if (!CollectionUtils.isEmpty(idList)) {
            for (int i = 0; i < idList.size(); i++) {
                Long id = idList.get(i);
                AuthorizationResourcePropertyModel authorizationResourcePropertyModel = authorizationResourcePropertyModelMapper.getAuthorizationResourcePropertyModelById(id);
                if (authorizationResourcePropertyModel != null) {
                    //TODO 是否可以删除
                   /* if (!CollectionUtils.isEmpty(authorizationUserRoleRelationModelMapper.getUserIdByRoleId(roleId))) {
                        String roleName = authorizationRoleModel.getRoleName();
                        throw new BizException("角色【" + roleName + "】存在使用的用户");
                    }*/
                } else {
                    throw new BizException("无效数据");
                }
            }
            return authorizationResourcePropertyModelMapper.batchDeleteAuthorizationResourceProperty(idList) > 0;
        } else {
            throw new BizException("请选择需要删除的数据");
        }
    }

    /**
     * 获取列表
     *
     * @return
     * @throws Exception
     */
    public List<AuthorizationResourcePropertyModel> getAuthorizationResourcePropertyModelList(AuthorizationResourcePropertyModel authorizationResourcePropertyModel) throws BizException {
        return authorizationResourcePropertyModelMapper.getAuthorizationResourcePropertyModelList(authorizationResourcePropertyModel);
    }

    /**
     * 获取列表(支持缓存)
     *
     * @return
     * @throws Exception
     */
    public List<AuthorizationResourcePropertyModel> getAuthorizationResourcePropertyModelRedisList() throws BizException {
        List<AuthorizationResourcePropertyModel> list = null;
        try {
            String json = RedisUtil.getString(Redis_List_key);
            if (StringUtil.isNotEmpty(json)) {
                list = JsonUtil.convert(json, List.class, AuthorizationResourcePropertyModel.class);
            }
            if (CollectionUtils.isEmpty(list)) {
                list = authorizationResourcePropertyModelMapper.getAuthorizationResourcePropertyModelList(null);
                RedisUtil.saveString(Redis_List_key, JsonUtil.toJsonString(list));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 获取对象
     *
     * @return
     * @throws Exception
     */
    public AuthorizationResourcePropertyModel getAuthorizationResourcePropertyModelById(Long id) throws BizException {
        if (id == null) {
            throw new BizException("选择数据");
        }
        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = null;
        List<AuthorizationResourcePropertyModel> list = getAuthorizationResourcePropertyModelRedisList();
        for (int i = 0; i < list.size(); i++) {
            AuthorizationResourcePropertyModel item = list.get(i);
            if (item.getId() == id) {
                return item;
            }
        }
        return authorizationResourcePropertyModel;
    }


    /**
     * 获取当前属性的所有父节点(包含自己)
     *
     * @return
     * @throws Exception
     */
    public List<Long> getAuthorizationResourcePropertyModelPidById(Long id) throws BizException {
        if (id == null) {
            throw new BizException("选择数据");
        }
        List<Long> ids = new ArrayList<>();
        recursionIds(ids, id);
        return ids;
    }

    /**
     * 递归获取所有父 资源属性
     *
     * @param ids
     * @param id
     * @throws BizException
     */
    private void recursionIds(List<Long> ids, Long id) throws BizException {
        ids.add(id);
        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = getAuthorizationResourcePropertyModelById(id);
        if (authorizationResourcePropertyModel != null && authorizationResourcePropertyModel.getParentId() != 0) {
            authorizationResourcePropertyModel = getAuthorizationResourcePropertyModelById(authorizationResourcePropertyModel.getParentId());
            if (authorizationResourcePropertyModel != null) {
                if (authorizationResourcePropertyModel.getParentId() == 0) {
                    ids.add(authorizationResourcePropertyModel.getId());
                } else {
                    recursionIds(ids, authorizationResourcePropertyModel.getId());
                }
            }
        }
    }
}

