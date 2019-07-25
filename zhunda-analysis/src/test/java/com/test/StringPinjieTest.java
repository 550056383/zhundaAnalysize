package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import zd.zdanalysis.AnalysisApplication;

/**
 * 类描述 :
 *
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/25 10:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnalysisApplication.class)
public class StringPinjieTest {
    /*tableAssocia---------[{table1=Sheet1, title1=PO号, table2=Test, title2=PO号}]
    tableCell---------{tableTitle=[[Sheet1, 总金额]], custTitle=[汇款多少]}
    writeRules---------[{major=[[Sheet1, 总金额, 大于, null, null, 500]], vice=[[]], title=汇款多少, xshow=true}]*/
    @Test
    public void test() {
        String s = "[{table1=Sheet1, title1=PO号, table2=Test, title2=PO号}]";
        String replace = s.replace("{", "").replace("}", "").replace("[", "").replace("]", "").toString();
        String[] split = replace.split(",");
        for (int i = 0; i < split.length; i++) {
            String substring1 = split[i].substring(split[i].indexOf("=") + 1);
            System.out.println(substring1);
        }

    }
}
