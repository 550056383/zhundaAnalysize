package com.test;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述 :
 *
 * @author Jack Chen
 * @version 1.0
 * @date 2019/8/5 13:30
 */
public class DemoTest {

    @Test
    public void test(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        System.out.println(contextPath);
    }

}
