package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationInfoModel;
import com.shsnc.base.authorization.model.condition.AuthorizationInfoCondition;
import com.shsnc.base.util.sql.Pagination;
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
    int addAuthorizationInfo(AuthorizationInfoModel authorizationInfoModel);

    /**
     * 编辑权限信息 返回数据编辑成功数据条数
     *
     * @param authorizationInfoModel
     * @return
     */
    Integer editAuthorizationInfo(AuthorizationInfoModel authorizationInfoModel);

    /**
     * 编辑权限信息 返回数据编辑成功数据条数
     *
     * @param authorizationId     权限id
     * @param authorizationStatus 权限状态
     * @return
     */
    Integer editAuthorizationStatus(@Param("authorizationId") Long authorizationId, @Param("authorizationStatus") Integer authorizationStatus);

    /**
     * 批量删除
     *
     * @param authorizationIdList
     * @return
     */
    Integer batchDeleteAuthorization(List<Long> authorizationIdList);

    /**
     * 获取单一对象数据
     *
     * @param authorizationId
     * @return
     */
    AuthorizationInfoModel getAuthorizationByAuthorizationId(Long authorizationId);

    /**
     * 获取单一对象数据
     *
     * @param authorizationCode
     * @return
     */
    AuthorizationInfoModel getAuthorizationByAuthorizationCode(@Param("authorizationCode") String authorizationCode);

    /**
     * 获取列表数据
     *
     * @param condition
     * @return
     */
    List<AuthorizationInfoModel> getAuthorizationList(AuthorizationInfoCondition condition);

    /**
     * 根据权限名称获取列表
     *
     * @param authorizationName
     * @return
     */
    List<AuthorizationInfoModel> getListByAuthorizationName(String authorizationName);


    /**
     * 根据权限编码获取列表
     *
     * @param authorizationCode
     * @return
     */
    List<AuthorizationInfoModel> getListByAuthorizationCode(String authorizationCode);

    /**
     * 总数获取
     * @param condition
     * @return
     */
    Integer getTotalCountByCondition(@Param("condition") AuthorizationInfoCondition condition);

    /**
     * 分页查询
     * @param condition
     * @param pagination
     * @return
     */
    List<AuthorizationInfoModel> getPageByCondition(@Param("condition") AuthorizationInfoCondition condition, @Param("pagination") Pagination pagination);



    /**
     * 获取权限编码列表
     *
     * @return
     */
    List<String> getAuthorizationCodeList();
}
