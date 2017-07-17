package com.shsnc.base.authorization.bean;

import com.shsnc.api.core.validation.ValidationType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 数据授权对象
 * Created by Elena on 2017/6/12.
 */
public class DataAuthorization implements Serializable {

    @NotNull(groups = ValidationType.Add.class)
    private Long resourceId;

    private List<DataAuthorizationRole> authRoleList;

    private List<DataAuthorizationUser> authUserList;

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public List<DataAuthorizationRole> getAuthRoleList() {
        return authRoleList;
    }

    public void setAuthRoleList(List<DataAuthorizationRole> authRoleList) {
        this.authRoleList = authRoleList;
    }

    public List<DataAuthorizationUser> getAuthUserList() {
        return authUserList;
    }

    public void setAuthUserList(List<DataAuthorizationUser> authUserList) {
        this.authUserList = authUserList;
    }
}
