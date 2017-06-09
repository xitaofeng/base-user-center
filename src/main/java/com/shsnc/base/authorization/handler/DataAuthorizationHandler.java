package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Elena on 2017/6/7.
 * 数据权限处理器
 */
@Component
@RequestMapper("/data/authorization")
public class DataAuthorizationHandler implements RequestHandler {


    @RequestMapper("/user")
    public boolean userAuth(@NotNull Integer roleId, @NotEmpty List<Integer> authorizationIdList) {
        return false;
    }


    @RequestMapper("/role")
    public boolean auth(@NotNull Integer roleId, @NotEmpty List<Integer> authorizationIdList) {
        return false;
    }
}
