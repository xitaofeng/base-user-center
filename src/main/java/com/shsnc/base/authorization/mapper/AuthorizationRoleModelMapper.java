package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationRoleModel;
import com.shsnc.base.authorization.model.condition.AuthorizationRoleCondition;
import com.shsnc.base.util.sql.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by Elena on 2017/6/7.
 */
public interface AuthorizationRoleModelMapper {

    /**
     * 添加权限角色信息 返回数据ID
     *
     * @param authorizationRoleModel
     * @return
     */
    int addAuthorizationRoleModel(AuthorizationRoleModel authorizationRoleModel);

    /**
     * 编辑权限角色信息 返回数据编辑成功数据条数
     *
     * @param authorizationRoleModel
     * @return
     */
    Integer editAuthorizationRoleModel(AuthorizationRoleModel authorizationRoleModel);

    /**
     * 批量删除
     *
     * @param roleIdList
     * @return
     */
    Integer batchDeleteAuthorizationRole(List<Long> roleIdList);

    /**
     * 获取单一对象数据
     *
     * @param roleId
     * @return
     */
    AuthorizationRoleModel getAuthorizationRoleModelByRoleId(Long roleId);

    /**
     * 获取列表数据
     *
     * @param condition
     * @return
     */
    List<AuthorizationRoleModel> getAuthorizationRoleModelList(@Param("condition")AuthorizationRoleCondition condition);

    /**
     * 根据角色名称获取列表
     *
     * @param roleName
     * @return
     */
    List<AuthorizationRoleModel> getListByRoleName(String roleName);

    /**
     * 角色编码
     *
     * @param roleCode
     * @return
     */
    List<AuthorizationRoleModel> getListByRoleCode(String roleCode);

    /**
     * 根据roleId获取对象
     *
     * @param roleId
     * @return
     */
    AuthorizationRoleModel getByRoleId(Long roleId);

    /**
     * 总数获取
     *
     * @param condition
     * @return
     */
    Integer getTotalCountByCondition(@Param("condition") AuthorizationRoleCondition condition);

    /**
     * 分页查询
     *
     * @param condition
     * @param pagination
     * @return
     */
    List<AuthorizationRoleModel> getPageByCondition(@Param("condition") AuthorizationRoleCondition condition, @Param("pagination") Pagination pagination);

    List<AuthorizationRoleModel> getByRoleIds(@Param("roleIds") Collection<Long> roleIds);
}
