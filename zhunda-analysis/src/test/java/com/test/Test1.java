package com.test;

import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.AnalysisApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  AnalysisApplication.class)
public class Test1 {
    @Test
    public void Testx() throws IOException {
        File file = new File("E:\\Zhunda\\工程\\PURCHASE_ORDER_20190611150319（华为系统订单信息）.xlsx");
        System.out.println(file.getAbsolutePath());
        FileInputStream fileInput = new FileInputStream(file);
        MultipartFile toMultipartFile = new MockMultipartFile("file",file.getName(),"text/plain", IOUtils.toByteArray(fileInput));
        toMultipartFile.getInputStream();
        long start = System.currentTimeMillis();

        final List<List<String>> table = new LinkedList<List<String>>();

        new ExcelEventParser(file.getAbsolutePath()).setHandler(new ExcelEventParser.SimpleSheetContentsHandler(){

            private List<String> fields;

            @Override
            public void endRow(int rowNum) {
                if(rowNum == 0){
                    // 第一行中文描述忽略
                }else if(rowNum == 1){
                    // 第二行字段名
                    fields = row;
                }else {
                    // 数据
                        table.add(row);

//                    if (rowNum < 20) {
//                        System.out.println("row.toString() = " + row.toString());
//                        List<String> list = table.get(rowNum-2);
//                        for (String str : list) {
//                                System.out.println("str = " + str+"-----"+rowNum);
//                            }
//                    }
//                    for (List<String> str : table) {
//                        System.out.println("str.toString() = " + str.toString());
//                    }
                }

            }
        }).parse();

        long end = System.currentTimeMillis();

        System.err.println(table.size());
        System.err.println(end - start);

        for (int i = 0; i < 20; i++) {
        List<String> strings = table.get(i);//70528
            for (String str : strings) {
                System.out.print("最终str = " + str.toString());
            }
            System.out.println("---------------------换一行");
        }

        System.out.println("得到第一行数据 = " + table.get(0).toString());
        System.out.println("得到最后一行数据 = " + table.get(70528).toString());



    }
    @Test
    public void test_t(){
        ExcelParser excelParser = new ExcelParser();
        File tempFile = new File("E:\\Zhunda\\工程\\PURCHASE_ORDER_20190611150319（华为系统订单信息）.xlsx");
//传入一个路径产生流再将流传入工具类，返回解析对象，Excel的所有数据就被解析到List<String[]> 里面，遍历list任由你处置。
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempFile);
            ExcelParser parse = excelParser.parse(inputStream);
            List<String[]> datas = parse.getDatas();
            String[] strings = datas.get(0);
            for (String str:strings){
                System.out.println("-----"+str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void TestList() {
        final List<List<String>> table = new ArrayList<List<String>>();
        for (int i = 0; i < 80000; i++) {
            List<String> objects = new LinkedList<String>();
            objects.add("" + i);
            table.add(objects);
        }
        System.out.println("table = " + table.get(0));
        System.out.println("table = " + table.get(7999));
    }
}
