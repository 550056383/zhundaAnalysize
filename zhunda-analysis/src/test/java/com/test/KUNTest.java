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
    private static int count=0;
    private static String sheetx="";
    private static String[] preStrs=null;
    private static String[] titleStr=null;
    private static LinkedHashMap<String, String> resource=new LinkedHashMap<String, String>();
    private static List<Map<String,Object>> listRsourcecs=new CopyOnWriteArrayList<Map<String,Object>>();
    @Test
    public void Test1(){
        String filePath="E:\\Zhunda\\测试\\7月31号.xlsx";
        File file = new File(filePath);
        TableDealWith dealWith = TableDealWith.getInstance(2, null, 1, new String[]{"Site Rollout Plan"});
        dealWith.getRead(file);
        listRsourcecs = dealWith.getListRsourcecs();
        for (Map<String,Object> map: KUNTest.listRsourcecs){
            for (Map.Entry entry:map.entrySet()){
                System.out.println("键："+entry.getKey()+"-=-值："+entry.getValue());
            }
        }
    }
//    private InputStream getInputStream(File file){
//        FileInputStream inputStream=null;
//        try {
//            inputStream= new FileInputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return inputStream;
//    }
//    public static void getResource(List<String> list,String sheetName){
//        String[] strs = list.toArray(new String[list.size()]);
//        if(count<2){
//            if(count==0){
//                preStrs=strs;
//            }
//            else {
//                for (int i=0;i<strs.length;i++){
//                    preStrs[i]=strs[i]!=""?preStrs[i]+"--"+strs[i]:preStrs[i];
//                }
//                titleStr=preStrs;
//            }
//        }
//        else if (count>2){
//            for (int i=0;i<strs.length;i++){
//                resource.put(titleStr[i],strs[i]);
//            }
//            listRsourcecs.add(resource);
//            resource= new LinkedHashMap<String, String>();
//        }
//        //赋值
//        if (sheetName!=sheetx){
//            sheetx=sheetName;
//            count=0;
//        }
//        count++;
//    }

}
