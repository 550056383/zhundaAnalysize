package com.test;

import org.junit.Test;
import zd.zdcommons.utils.PinYinUtils;
import zd.zdcommons.utils.StringFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述 :
 *
 * @author Jack Chen
 * @version 1.0
 * @date 2019/8/5 13:30
 */
public class DemoTest {

    //对表名和字段进行处理
    static List<String[]> newlist = new ArrayList<String[]>();

    public static List<String[]> stringToArr(String s) {
        //s:string1.xlsx--Sheet1, 回款金额, 大于, null, null, 99
        String[] split = s.split(",");
        for (String s1 : split) {
            System.out.println("000000000" + s1);
        }
        split[0] = StringFormat.uuid(split[0].trim());
        split[1] = PinYinUtils.hanziToPinyin(split[1], "");
        if (split.length != 3) {
            if (split[3].trim().equals("null") || split[3].trim().equals("")) {
                split[3] = null;
            } else {
                split[3] = StringFormat.uuid(split[3].trim());
            }
            split[4] = PinYinUtils.hanziToPinyin(split[4], "");
        }
        newlist.add(split);
        return newlist;
    }

    public static void clearList() {
        newlist.clear();
    }

    @Test
    public void test() throws Exception {
        String s = "[{major=[[1.xlsx--Sheet1, 派工进度, 等于, 1.xlsx--Sheet1, 派工进度, 13], [1.xlsx--Sheet1, 总金额, 大于, 1.xlsx--Sheet1, 总金额, 如2]], vice=[[1.xlsx--Sheet1, 派工进度, 大于, 1.xlsx--Sheet1, 总金额, 14], [1.xlsx--Sheet1, 总金额, 大于, 1.xlsx--Sheet1, 总金额, 4231]], title=条件1, xshow=true}, {major=[[1.xlsx--Sheet1, PO号, 大于, 1.xlsx--Test, 办事处, 41], [1.xlsx--Test, 是否及时验收, 大于等于, 1.xlsx--Sheet1, 实际完工日期, 2341]], vice=[[1.xlsx--Sheet1, 是否及时验收, 大于等于, 1.xlsx--Sheet1, 回款金额, 35], [1.xlsx--Sheet1, 实际完工日期, 大于等于, 1.xlsx--Test, 实际完工日期, 4124]], title=条件2, xshow=true}]";
        String s2 = "[{major=[[1.xlsx--Test, 总金额, 大于, 1.xlsx--Sheet1, 总金额, 10000], [1.xlsx--Sheet1, 总金额, 等于, 1.xlsx--Sheet1, 派工进度, 4231]], vice=[[1.xlsx--Test, 总金额, 等于, 1.xlsx--Sheet2, 派工进度, 4214], [1.xlsx--Sheet1, 实际完工日期, 小于等于, 1.xlsx--Sheet1, 是否及时验收, 4]], title=条件1, xshow=true}]";
        List<String[]> list = new ArrayList<String[]>();
        List<List<String[]>> listList = new ArrayList<>();
        String[] split = s.split("},");//一个自定义字段的条件为一个值
        for (String arr : split) {
            String[] arr2 = arr.split("]],");//一个自定义字段的条件分割为主条件,附条件,其他信息

            for (String s1 : arr2) {
                String[] split1 = s1.split("],");
                for (int i = 0; i < split1.length; i++) {
                    String replace = split1[i].replace("[", "").replace("]", "").replace("{", "").replace("}", "");
                    String substring1 = replace.substring(replace.indexOf("=") + 1);
                    split1[i] = substring1.trim();
                    System.out.println(split1[i]);
                    System.out.println("-------------");
                }

                list.add(split1);
            }
            System.out.println("==================");
            System.out.println("list = " + list.size());
            listList.add(list);
            list = new ArrayList<String[]>();
        }
        System.out.println(" listList= " + listList.size());
        List<String[]> strings = listList.get(0);
        String[] strings11 = strings.get(0);
        for (String s1 : strings11) {
            System.out.println(s1);
        }
        String[] strings12 = strings.get(1);
        for (String s1 : strings12) {
            System.out.println(s1);
        }
        String[] strings13 = strings.get(2);

    }

    @Test
    public void test3() {
        String s = "[{major=[[1.xlsx--Test, 总金额, 大于, 1.xlsx--Sheet1, 总金额, 10000], [1.xlsx--Sheet1, 总金额, 等于, 1.xlsx--Sheet1, 派工进度, 4231]], vice=[[1.xlsx--Test, 总金额, 等于, 1.xlsx--Sheet2, 派工进度, 4214], [1.xlsx--Sheet1, 实际完工日期, 小于等于, 1.xlsx--Sheet1, 是否及时验收, 4]], title=条件1, xshow=true}]";

        List<String[]> list = new ArrayList<String[]>();
        String[] split = s.split("]],");
        for (String s1 : split) {
            String[] split1 = s1.split("],");
            for (int i = 0; i < split1.length; i++) {
                String replace = split1[i].replace("[", "").replace("]", "").replace("{", "").replace("}", "");
                String substring1 = replace.substring(replace.indexOf("=") + 1);
                split1[i] = substring1.trim();
                System.out.println(split1[i]);
            }
            System.out.println("-------------------------");
            list.add(split1);
        }
        for (String[] ss : list) {
            System.out.println(ss);
        }


    }

}
