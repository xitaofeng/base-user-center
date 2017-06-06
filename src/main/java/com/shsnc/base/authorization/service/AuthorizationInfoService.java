package com.shsnc.base.authorization.service;


import com.shsnc.base.authorization.config.AuthorizationConstant;
import com.shsnc.base.authorization.mapper.AuthorizationInfoModelMapper;
import com.shsnc.base.authorization.model.AuthorizationInfoModel;
import com.shsnc.base.util.config.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    /**
     * 添加 权限 信息
     *
     * @param authorizationInfoModel
     * @return
     * @throws Exception
     */
    public AuthorizationInfoModel addAuthorizationInfo(AuthorizationInfoModel authorizationInfoModel) throws Exception {
        if (isAuthorizationName(AuthorizationConstant.BasicOperation.ADD, authorizationInfoModel.getAuthorizationName())) {
            throw new BizException("权限名称重复");
        }
        if (isAuthorizationName(AuthorizationConstant.BasicOperation.ADD, authorizationInfoModel.getAuthorizationCode())) {
            throw new BizException("权限编码重复");
        }
        authorizationInfoModel.setCreateTime(new Date().getTime());
        if (authorizationInfoModel.getAuthorizationStatus() == null) {
            authorizationInfoModel.setAuthorizationStatus(AuthorizationInfoModel.AuthorizationStatus.START.getAuthorizationStatus());
        }
        Integer authorizationId = authorizationInfoModelMapper.addAuthorizationInfo(authorizationInfoModel);
        if (authorizationId != null) {
            return authorizationInfoModel;
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
    public AuthorizationInfoModel editAuthorizationInfo(AuthorizationInfoModel authorizationInfoModel) throws Exception {
        if (isAuthorizationName(AuthorizationConstant.BasicOperation.EDIT, authorizationInfoModel.getAuthorizationName())) {
            throw new BizException("权限名称重复");
        }
        if (isAuthorizationName(AuthorizationConstant.BasicOperation.EDIT, authorizationInfoModel.getAuthorizationCode())) {
            throw new BizException("权限编码重复");
        }
        if (authorizationInfoModel.getAuthorizationStatus() == null) {
            authorizationInfoModel.setAuthorizationStatus(AuthorizationInfoModel.AuthorizationStatus.START.getAuthorizationStatus());
        }
        Integer authorizationId = authorizationInfoModelMapper.addAuthorizationInfo(authorizationInfoModel);
        if (authorizationId != null) {
            return authorizationInfoModel;
        } else {
            throw new BizException("权限添加失败");
        }
    }

    /**
     * 权限名称是否重复(true重复,false不重复)
     *
     * @param authorizationName
     * @return
     */
    public boolean isAuthorizationName(AuthorizationConstant.BasicOperation basicOperation, String authorizationName) {
        List<AuthorizationInfoModel> list = authorizationInfoModelMapper.getListByAuthorizationName(authorizationName);
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        if (AuthorizationConstant.BasicOperation.ADD == basicOperation) {
            return list.size() > 0;
        }

        if (AuthorizationConstant.BasicOperation.EDIT == basicOperation) {
            for (int i = 0; i < list.size(); i++) {
                AuthorizationInfoModel tempAuthorizationInfoModel = list.get(i);
                if (tempAuthorizationInfoModel.getAuthorizationName() != authorizationName) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 权限编码是否重复(true重复,false不重复)
     *
     * @param authorizationCode
     * @return
     */
    public boolean isAuthorizationCode(AuthorizationConstant.BasicOperation basicOperation, String authorizationCode) {
        List<AuthorizationInfoModel> list = authorizationInfoModelMapper.getListByAuthorizationCode(authorizationCode);
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        if (AuthorizationConstant.BasicOperation.ADD == basicOperation) {
            return list.size() > 0;
        }

        if (AuthorizationConstant.BasicOperation.EDIT == basicOperation) {
            for (int i = 0; i < list.size(); i++) {
                AuthorizationInfoModel tempAuthorizationInfoModel = list.get(i);
                if (tempAuthorizationInfoModel.getAuthorizationCode() != authorizationCode) {
                    return true;
                }
            }
        }
        return false;
    }
}
