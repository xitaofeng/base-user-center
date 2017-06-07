package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationInfoModel;
import com.sun.xml.internal.txw2.annotation.XmlNamespace;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Elena on 2017/6/5.
 */
@Repository
public interface AuthorizationInfoModelMapper {

    /**
     * 添加权限信息 返回数据ID
     *
     * @param authorizationInfoModel
     * @return
     */
    public Integer addAuthorizationInfo(AuthorizationInfoModel authorizationInfoModel);

    /**
     * 编辑权限信息 返回数据编辑成功数据条数
     *
     * @param authorizationInfoModel
     * @return
     */
    public Integer editAuthorizationInfo(AuthorizationInfoModel authorizationInfoModel);

    /**
     * 编辑权限信息 返回数据编辑成功数据条数
     *
     * @param authorizationId     权限id
     * @param authorizationStatus 权限状态
     * @return
     */
    public Integer editAuthorizationStatus(@Param("authorizationId") Integer authorizationId, @Param("authorizationStatus") Integer authorizationStatus);

    /**
     * 批量删除
     *
     * @param authorizationIdList
     * @return
     */
    public Integer batchDeleteAuthorization(List<Integer> authorizationIdList);

    /**
     * 获取单一对象数据
     *
     * @param authorizationId
     * @return
     */
    public AuthorizationInfoModel getAuthorizationByAuthorizationId(Integer authorizationId);

    /**
     * 获取列表数据
     *
     * @param authorizationInfoModel
     * @return
     */
    public List<AuthorizationInfoModel> getAuthorizationList(AuthorizationInfoModel authorizationInfoModel);

    /**
     * 根据权限名称获取列表
     *
     * @param authorizationName
     * @return
     */
    public List<AuthorizationInfoModel> getListByAuthorizationName(String authorizationName);


    /**
     * 根据权限编码获取列表
     *
     * @param authorizationCode
     * @return
     */
    public List<AuthorizationInfoModel> getListByAuthorizationCode(String authorizationCode);
}