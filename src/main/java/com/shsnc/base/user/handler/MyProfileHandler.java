package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import org.springframework.stereotype.Component;

/**
 * Created by houguangqiang on 2017/6/8.
 */
@Component
@RequestMapper("/user/myProfile")
public class MyProfileHandler implements RequestHandler {
}
