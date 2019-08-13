package com.test;

import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName KUNTest
 * @Author chenkun
 * @TIME 2019/7/30 0030-14:06
 */

public class KUNTest {
    private static String sheetx="";
    private static List<Map<String,Object>> listRsourcecs=new CopyOnWriteArrayList<Map<String,Object>>();
    @Test
    public void Test1(){
        String filePath="E:\\Zhunda\\测试\\工程所有Test.xls";
        File file = new File(filePath);
        TableDealWith dealWith = TableDealWith.getInstance(1, 1, null);
        dealWith.getRead(file);
        listRsourcecs = dealWith.getListRsourcecs();
        sheetx=dealWith.getSheetx();
        for (Map<String,Object> map: KUNTest.listRsourcecs){
            for (Map.Entry entry:map.entrySet()){
                System.out.println("键："+entry.getKey()+"-=-值："+entry.getValue());
            }
        }
    }
    @Test
    public void xText(){
        String str="\"你好，吗哈哈哈\"";
        str=str.replaceAll("[\"]+","-");
        System.out.println(str);
    }

}
