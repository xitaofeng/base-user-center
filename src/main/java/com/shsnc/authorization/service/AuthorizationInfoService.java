package com.shsnc.authorization.service;

import com.shsnc.authorization.bean.AuthorizationInfo;
import com.shsnc.authorization.config.AuthorizationConstant;
import com.shsnc.authorization.mapper.AuthorizationInfoModelMapper;
import com.shsnc.authorization.model.AuthorizationInfoModel;
import com.shsnc.base.util.DateTimeUtil;
import com.sun.org.apache.regexp.internal.REUtil;
import org.omg.CORBA.SystemException;
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
        if(isAuthorizationName(AuthorizationConstant.BasicOperation.ADD,null,authorizationInfoModel.getAuthorizationName())){
            throw new Exception("权限名称重复");
        }
        if(isAuthorizationName(AuthorizationConstant.BasicOperation.ADD,null,authorizationInfoModel.getAuthorizationCode())){
            throw new Exception("权限编码重复");
        }
        authorizationInfoModel.setCreateTime(new Date().getTime());
        if (authorizationInfoModel.getAuthorizationStatus() == null) {
            authorizationInfoModel.setAuthorizationStatus(AuthorizationInfoModel.AuthorizationStatus.START.getAuthorizationStatus());
        }
        Integer authorizationId = authorizationInfoModelMapper.addAuthorizationInfo(authorizationInfoModel);
        if (authorizationId != null) {
            return authorizationInfoModel;
        } else {
            throw new Exception("权限添加失败");
        }
    }

    /**
     * 权限名称是否重复(true重复,false不重复)
     * @param authorizationName
     * @return
     */
    public boolean isAuthorizationName(AuthorizationConstant.BasicOperation basicOperation, Integer authorizationId,String authorizationName) {
        if(AuthorizationConstant.BasicOperation.ADD == basicOperation){
            return authorizationInfoModelMapper.getListByAuthorizationName(authorizationName).size() > 0 ;
        }
        if(AuthorizationConstant.BasicOperation.EDIT == basicOperation){
            List<AuthorizationInfoModel> list = authorizationInfoModelMapper.getListByAuthorizationName(authorizationName);
            if(!CollectionUtils.isEmpty(list)){

            }
        }
        return true;
    }

    /**
     * 权限编码是否重复(true重复,false不重复)
     * @param authorizationCode
     * @return
     */
    public boolean isAuthorizationCode(String authorizationCode) {
        return true;
    }
}
