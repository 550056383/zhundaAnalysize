package com.test;

import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * 类描述 :
 *
 * @author Jack Chen
 * @version 1.0
 * @date 2019/8/5 13:30
 */
public class DemoTest {

    @Test
    public void test() throws Exception {
        File file = ResourceUtils.getFile("classpath:static");
        System.out.println(file);

    }


}