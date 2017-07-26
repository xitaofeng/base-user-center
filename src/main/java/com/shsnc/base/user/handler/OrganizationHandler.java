package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.user.bean.Organization;
import com.shsnc.base.user.bean.OrganizationParam;
import com.shsnc.base.user.model.OrganizationCondition;
import com.shsnc.base.user.model.OrganizationModel;
import com.shsnc.base.user.service.OrganizationService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by houguangqiang on 2017/6/9.
 */
@Component
@RequestMapper("/user/organization")
public class OrganizationHandler implements RequestHandler {

    @Autowired
    private OrganizationService organizationService;

    private String[][] fieldMapping = {{"name","name"},{"code","code"}};

    @RequestMapper("/getPage")
    @Authentication("BASE_USER_ORGANIZATION_GET_PAGE")
    public QueryData getPage(OrganizationCondition condition, Pagination pagination){
        pagination.buildSort(fieldMapping);
        QueryData queryData = organizationService.getOrganizationPage(condition,pagination);
        return queryData.convert(Organization.class);
    }

    @RequestMapper("/getObject")
    @Validate
    @Authentication("BASE_USER_ORGANIZATION_GET_OBJECT")
    public Organization getObjet(@NotNull Long organizationId) throws BizException {
        OrganizationModel organizationModel = organizationService.getOrganization(organizationId);
        return JsonUtil.convert(organizationModel, Organization.class);
    }

    @RequestMapper("/getNodeList")
    @Authentication("BASE_USER_ORGANIZATION_GET_NODE_LIST")
    public List<Organization> getNodeList(Long parentId){
        List<OrganizationModel> organizationModels = organizationService.getNodeList(parentId);
        return JsonUtil.convert(organizationModels, List.class, Organization.class);
    }


    @RequestMapper("/add")
    @Authentication("BASE_USER_ORGANIZATION_ADD")
    @Validate(groups = ValidationType.Add.class)
    public Long add(OrganizationParam organization) throws BizException {
        OrganizationModel organizationModel = JsonUtil.convert(organization, OrganizationModel.class);
        return organizationService.addOrganization(organizationModel, organization.getParentId());
    }

    @RequestMapper("/update")
    @Authentication("BASE_USER_ORGANIZATION_UPDATE")
    @Validate(groups = ValidationType.Update.class)
    public boolean update(OrganizationParam organization) throws BizException {
        OrganizationModel organizationModel = JsonUtil.convert(organization, OrganizationModel.class);
        return organizationService.updateOrganization(organizationModel, organization.getParentId());
    }

    @RequestMapper("/delete")
    @Authentication("BASE_USER_ORGANIZATION_DELETE")
    public boolean delete(@NotNull Long organizationId) throws BizException {
        return organizationService.deleteOrganization(organizationId);
    }

    @RequestMapper("/deleteTree")
    @Authentication("BASE_USER_ORGANIZATION_DELETE_TREE")
    public boolean deleteTree(@NotNull Long organizationId) throws BizException {
        return organizationService.deleteOrganizationTree(organizationId);
    }

}
