package com.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.junit.Test;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @ClassName ReadAndTest
 * @Author chenkun
 * @TIME 2019/7/1 0001-9:35
 */
public class ReadAndTest {
    //excel2003扩展名
    public static final String EXCEL03_EXTENSION = ".xls";
    //excel2007扩展名
    public static final String EXCEL07_EXTENSION = ".xlsx";
    @Test
    public void Test1() throws Exception {
        //String fileName="E:/Zhunda/工程/工程所有信息表2019-01-02.xls";
        //String fileName="E:/Zhunda/工程/工程所有Test.xls";
        //String fileName="E:/Zhunda/工程/PURCHASE_ORDER_20190611150319（华为系统订单信息）.xlsx";
        //String fileName="E:/Zhunda/测试/问题PO单（7.04）.xlsx";
        String fileName="E:/Zhunda/测试/工程所有Test.xls";
        readExcel(fileName);
    }
    public static LinkedList<Map<String,String>> resouces=new LinkedList<Map<String, String>>();
    public static void getShow(LinkedHashMap<String, String> map){
        for (Map.Entry<String,String> entry:map.entrySet()){
            System.out.println("entry.getValue() = " + entry.getValue());
        }
//        try {
//            //System.out.println("百分比:"+NumberFormat.getIntegerInstance().parse(map.get("派工进度")).doubleValue());
//            if (getPercentToDouble(map.get("派工进度"))==100){
//                double huikuang = getDouble(map.get("回款金额"));
//                double zongjine = getDouble(map.get("总金额"));
//                //System.out.println(map.get("委托总量"));
//                if(huikuang<zongjine){
//                    System.out.println("PO号: "+map.get("PO号")+" 的未及时验收金额："+getDouble((zongjine-huikuang)+"")+"元");
//                    System.out.println("回款金额："+huikuang+"\t总金额:"+zongjine);
//                }else if (huikuang>zongjine){
//                    System.out.println("PO号: "+map.get("PO号")+" 的回款金额大于总金额，多超"+(huikuang-zongjine)+"元");
//                }
//            }else if (getPercentToDouble(map.get("派工进度"))>100){
//                System.out.println("PO号: "+map.get("PO号")+" 的派工进度有误");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
    @Test
    public void Test2(){
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("东方","-----------");
        map.put("Okkk1","-----------");
        map.put("陈快快快","-----------");
        getShow(map);
    }
    public static void readExcel(String fileName) throws Exception {
        int totalRows =0;
        File file = new File(fileName);
        FileInputStream inputStream = new FileInputStream(file);
        if (fileName.endsWith(EXCEL03_EXTENSION)) { //处理excel2003文件
            ExcelXlsWithHSSFListener excelXls=new ExcelXlsWithHSSFListener();
            totalRows =excelXls.process(inputStream,1,new String[]{"回款金额","叠加"},"PO号",fileName);
        } else if (fileName.endsWith(EXCEL07_EXTENSION)) {//处理excel2007文件
            ExcelXlsxAndDefaultHandler excelXlsxReader = new ExcelXlsxAndDefaultHandler();
            totalRows = excelXlsxReader.process(inputStream,1,new String[]{},"PO号",fileName);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
        System.out.println("发送的总行数：" + totalRows);
    }
    //S转换百分数
    public static Double getPercentToDouble(String s){
        Double value=null;
        try {
            //number 是INt 和Long的父类
            value = NumberFormat.getIntegerInstance().parse(s).doubleValue();
        } catch (Exception e) {//转换出错默认0.0
            value=0.0;
        }
        return value;
    }
    //S转Double
    public static Double getDouble(String s){
        Double value=null;
        try {
             value = Double.parseDouble(String.format("%.2f", Double.parseDouble(s)));
        }catch (NullPointerException e){
            value=0.0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }
}
