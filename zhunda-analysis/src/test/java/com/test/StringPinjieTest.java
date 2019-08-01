package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import zd.zdanalysis.AnalysisApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述 :
 *
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/25 10:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnalysisApplication.class)
public class StringPinJieTest {
    /*    tableAssocia---------[{table1=1.xlsx--Test, title1=PO号, table2=1.xlsx--Sheet1, title2=PO号}]
        tableCell---------{tableTitle=[[1.xlsx--Sheet1, 总金额]], custTitle=[回款金额有多少]}
        writeRules---------
                [{major=[[1.xlsx--Test, 回款金额, 大于, 1.xlsx--Sheet2, 回款金额, 500]], vice=[[1.xlsx--Sheet1, 是否及时验收, 等于, null, null, 是]], title=回款金额有多少, xshow=true}]*/
    @Test
    public void test() {
        String s = "[{major=[[1.xlsx--Test, 总金额, 大于, 1.xlsx--Sheet1, 回款金额, 500]," +
                " [1.xlsx--Test, 是否及时验收, 等于, 1.xlsx--Sheet1, 是否及时验收, 是]], " +

                "vice=[[]], " +

                "title=多少, xshow=true}]";
        List<String[]> list = new ArrayList<String[]>();
        String[] split = s.split("]],");
        for (String s1 : split) {
            String[] split1 = s1.split("],");
            for (int i = 0; i < split1.length; i++){
                String replace = split1[i].replace("[", "").replace("]", "").replace("{", "").replace("}", "");
                String substring1 = replace.substring(replace.indexOf("=") + 1);
                split1[i] = substring1.trim();
            }
            System.out.println(split1.length);
            System.out.println(split1[0]);
            System.out.println(split1[0].isEmpty());
            list.add(split1);
        }
        System.out.println(list.size());
        for (String[] strings : list) {
            for (String s1 : strings) {
                System.out.println(s1);
            }
            System.out.println("-----------------------");

        }

    }
}
