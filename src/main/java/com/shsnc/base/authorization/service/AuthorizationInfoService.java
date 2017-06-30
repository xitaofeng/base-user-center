package com.shsnc.base.authorization.service;


import com.shsnc.base.authorization.mapper.AuthorizationInfoModelMapper;
import com.shsnc.base.authorization.mapper.AuthorizationRoleRelationModelMapper;
import com.shsnc.base.authorization.model.AuthorizationInfoModel;
import com.shsnc.base.authorization.model.AuthorizationInfoModel.EnumAuthorizationStatus;
import com.shsnc.base.authorization.model.condition.AuthorizationInfoCondition;
import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.user.model.UserInfoCondition;
import com.shsnc.base.util.StringUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Elena on 2017/6/5.
 */
@Service
public class AuthorizationInfoService {

    @Autowired
    private AuthorizationInfoModelMapper authorizationInfoModelMapper;

    @Autowired
    private AuthorizationRoleRelationModelMapper authorizationRoleRelationModelMapper;

    /**
     * 添加 权限 信息
     *
     * @param authorizationInfoModel
     * @return
     * @throws Exception
     */
    public Long addAuthorizationInfo(AuthorizationInfoModel authorizationInfoModel) throws Exception {
        if (isAuthorizationName(authorizationInfoModel)) {
            throw new BizException("权限名称重复");
        }
        if (isAuthorizationCode(authorizationInfoModel)) {
            throw new BizException("权限编码重复");
        }
        authorizationInfoModel.setCreateTime(new Date().getTime());
        if (authorizationInfoModel.getAuthorizationStatus() == null) {
            authorizationInfoModel.setAuthorizationStatus(EnumAuthorizationStatus.ENABLED.getAuthorizationStatus());
        }
        int count = authorizationInfoModelMapper.addAuthorizationInfo(authorizationInfoModel);
        if (count > 0) {
            return authorizationInfoModel.getAuthorizationId();
        } else {
            throw new BizException("权限添加失败");
        }
    }

    /**
     * 添加 权限 信息
     *
     * @param authorizationInfoModel
     * @return
     * @throws Exception
     */
    public boolean editAuthorizationInfo(AuthorizationInfoModel authorizationInfoModel) throws Exception {
        if (authorizationInfoModel != null && authorizationInfoModel.getAuthorizationId() != null) {
            AuthorizationInfoModel editAuthorizationInfoModel = authorizationInfoModelMapper.getAuthorizationByAuthorizationId(authorizationInfoModel.getAuthorizationId());
            if (editAuthorizationInfoModel != null) {
                BeanUtils.copyProperties(authorizationInfoModel, editAuthorizationInfoModel);
                if (isAuthorizationName(authorizationInfoModel)) {
                    throw new BizException("权限名称重复");
                }
                if (isAuthorizationCode(authorizationInfoModel)) {
                    throw new BizException("权限编码重复");
                }
                if (authorizationInfoModel.getAuthorizationStatus() == null) {
                    authorizationInfoModel.setAuthorizationStatus(EnumAuthorizationStatus.ENABLED.getAuthorizationStatus());
                }
                Integer count = authorizationInfoModelMapper.editAuthorizationInfo(authorizationInfoModel);
                if (count != null && count > 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new BizException("编辑数据不存在");
            }
        } else {
            throw new BizException("选择编辑数据");
        }
    }

    /**
     * 更改权限信息状态
     *
     * @param authorizationId
     * @param enumAuthorizationStatus
     * @return
     * @throws Exception
     */
    public boolean editAuthorizationInfoModelStatus(Long authorizationId, EnumAuthorizationStatus enumAuthorizationStatus) throws BizException {
        if (authorizationId != null) {
            if (authorizationInfoModelMapper.getAuthorizationByAuthorizationId(authorizationId) != null) {
                return authorizationInfoModelMapper.editAuthorizationStatus(authorizationId, enumAuthorizationStatus.getAuthorizationStatus()) > 0;
            } else {
                throw new BizException("无效数据");
            }
        } else {
            throw new BizException("选择数据");
        }
    }

    /**
     * 删除权限信息
     *
     * @param authorizationIdList
     * @return
     * @throws Exception
     */
    public boolean batchDeleteAuthorizationInfo(List<Long> authorizationIdList) throws Exception {
        if (!CollectionUtils.isEmpty(authorizationIdList)) {
            for (int i = 0; i < authorizationIdList.size(); i++) {
                Long authorizationId = authorizationIdList.get(i);
                if (!CollectionUtils.isEmpty(authorizationRoleRelationModelMapper.getRoleIdByAuthorizationId(authorizationId))) {
                    String authorizationName = authorizationId + "";
                    AuthorizationInfoModel authorizationInfoModel = authorizationInfoModelMapper.getAuthorizationByAuthorizationId(authorizationId);
                    if (authorizationInfoModel != null) {
                        authorizationName = authorizationInfoModel.getAuthorizationName();
                    }
                    throw new BizException("权限【" + authorizationName + "】存在使用的角色");
                }
            }
            return authorizationInfoModelMapper.batchDeleteAuthorization(authorizationIdList) > 0;
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
    public List<AuthorizationInfoModel> getAuthorizationList(AuthorizationInfoCondition condition) throws Exception {
        return authorizationInfoModelMapper.getAuthorizationList(condition);
    }

    /**
     * 分页查询
     * @param condition
     * @param pagination
     * @return
     */
    public QueryData getAuthorizationPageList(AuthorizationInfoCondition condition, Pagination pagination) {
        QueryData queryData = new QueryData(pagination);
        int totalCount = authorizationInfoModelMapper.getTotalCountByCondition(condition);
        queryData.setRowCount(totalCount);
        List<AuthorizationInfoModel> list = authorizationInfoModelMapper.getPageByCondition(condition, pagination);
        queryData.setRecords(list);
        return queryData;
    }

    /**
     * 查询对象
     *
     * @param authorizationId
     * @return
     * @throws Exception
     */
    public AuthorizationInfoModel getAuthorizationByAuthorizationId(Long authorizationId) throws Exception {
        if (authorizationId == null) {
            throw new BizException("无效数据");
        }
        AuthorizationInfoModel authorizationInfoModel = authorizationInfoModelMapper.getAuthorizationByAuthorizationId(authorizationId);
        return authorizationInfoModel;
    }

    /**
     * 权限名称是否重复(true重复,false不重复)
     *
     * @param authorizationInfoModel
     * @return
     */
    public boolean isAuthorizationName(AuthorizationInfoModel authorizationInfoModel) throws BizException {
        Long authorizationId = authorizationInfoModel.getAuthorizationId();
        String authorizationName = authorizationInfoModel.getAuthorizationName();
        if (StringUtil.isNotEmpty(authorizationName)) {
            List<AuthorizationInfoModel> list = authorizationInfoModelMapper.getListByAuthorizationName(authorizationName);
            if (CollectionUtils.isEmpty(list)) {
                return false;
            }
            //authorizationId == null 添加否则编辑
            return checkDataRepetition(authorizationId, list);
        } else {
            throw new BizException("权限名称不能为空");
        }
    }

    /**
     * 权限编码是否重复(true重复,false不重复)
     *
     * @param authorizationInfoModel
     * @return
     */
    public boolean isAuthorizationCode(AuthorizationInfoModel authorizationInfoModel) throws BizException {
        Long authorizationId = authorizationInfoModel.getAuthorizationId();
        String authorizationCode = authorizationInfoModel.getAuthorizationCode();
        if (StringUtil.isNotEmpty(authorizationCode)) {
            List<AuthorizationInfoModel> list = authorizationInfoModelMapper.getListByAuthorizationCode(authorizationCode);
            if (CollectionUtils.isEmpty(list)) {
                return false;
            }
            return checkDataRepetition(authorizationId, list);
        } else {
            throw new BizException("权限编码不能为空");
        }
    }

    /**
     * 验证数据是否重复
     *
     * @param authorizationId
     * @param list
     * @return
     */
    private boolean checkDataRepetition(Long authorizationId, List<AuthorizationInfoModel> list) {
        //authorizationId == null 添加否则编辑
        if (authorizationId != null) {
            boolean isRepetition = false;
            for (int i = 0; i < list.size(); i++) {
                AuthorizationInfoModel tempAuthorizationInfoModel = list.get(i);
                if (authorizationId != tempAuthorizationInfoModel.getAuthorizationId()) {
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
