package org.activiti.designer.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

/**
 * Created by lejingw on 14-6-26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
@ActiveProfiles("development")
public class ActiveProfileTest {
    @Autowired
    private DataSource dataSource;

    @Test
    public void test1(){
        System.out.println(dataSource);
    }
}
