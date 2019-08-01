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
 * @ClassName TableDealWith
 * @Author chenkun
 * @TIME 2019/7/31 0031-15:58
 */
public class TableDealWith {
    //输出配置
    private static int count=0;
    private static String sheetx="";
    private static String[] preStrs=null;
    private static String[] titleStr=null;
    private static LinkedHashMap<String, String> resource=new LinkedHashMap<String, String>();
    private static List<Map<String,String>> listRsourcecs=new CopyOnWriteArrayList<Map<String,String>>();

    //外部设置
    private static int titleNum=2;
    private static String[] realRules=null;
    private static int interva=1;
    private static String[] sheetNames={"Site Rollout Plan"};
    @Test
    public void  Test(){
        String filePath="E:\\Zhunda\\测试\\工程所有信息表2019-01-02.xls";
        File file = new File(filePath);
        getRead(file);
        for (Map<String,String> map:listRsourcecs){
            for (Map.Entry entry:map.entrySet()){
                System.out.println("键："+entry.getKey()+"-=-值："+entry.getValue());
            }
        }
    }

    public void getRead(File file){
        InputStream inputStream = getInputStream(file);
        String fileName = file.getName();
        if(fileName.endsWith("xlsx")){
            new ExcelXlsxAndDefaultHandler().process(inputStream,sheetNames,file.getName());
        }else if (fileName.endsWith("xls")){
            //new ExcelXlsWithHSSFListener().process();
        };

    }

    private InputStream getInputStream(File file){
        FileInputStream inputStream=null;
        try {
            inputStream= new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static void getResource(List<String> list, String sheetName){
        String[] strs = list.toArray(new String[list.size()]);
        if(count<titleNum){
            if(count==0){
                preStrs=strs;
            }
            else {
                for (int i=0;i<strs.length;i++){
                    preStrs[i]=strs[i]!=""?preStrs[i]+"--"+strs[i]:preStrs[i];
                }
                titleStr=preStrs;
            }
        }
        else if (count>(titleNum+interva-1)){
            for (int i=0;i<strs.length;i++){
                resource.put(titleStr[i],strs[i]);
            }
            listRsourcecs.add(resource);
            resource= new LinkedHashMap<String, String>();
        }
        //赋值
        if (sheetName!=sheetx){
            sheetx=sheetName;
            count=0;
        }
        count++;
    }
}
