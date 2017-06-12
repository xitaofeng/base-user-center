package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import org.springframework.stereotype.Component;

/**
 * Created by houguangqiang on 2017/6/11.
 */
@Component
@RequestMapper("/user/loginHistory")
@LoginRequired
public class LoginHistoryHandler implements RequestHandler {
}
