package com.shsnc.base.user;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

/**
 * Created by houguangqiang on 2017/6/7.
 */
@Rollback(true)
@ContextConfiguration({"/beans.xml"})
public class BaseUserTest extends AbstractTransactionalJUnit4SpringContextTests {


    @Before
    public void before(){

    }

    @After
    public void After (){

    }
}
