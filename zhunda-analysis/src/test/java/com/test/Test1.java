package com.test;

import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.AnalysisApplication;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  AnalysisApplication.class)
public class Test1 {
    @Test
    public void Testx() throws IOException {
        File file = new File("E:\\Zhunda\\工程\\PURCHASE_ORDER_20190611150319（华为系统订单信息）.xlsx");
        System.out.println(file.getAbsolutePath());
        FileInputStream fileInput = new FileInputStream(file);
        MultipartFile toMultipartFile = new MockMultipartFile("file",file.getName(),"text/plain", IOUtils.toByteArray(fileInput));
        InputStream inputStream = toMultipartFile.getInputStream();
        long start = System.currentTimeMillis();

        final List<List<String>> table = new LinkedList<List<String>>();
        final List<String>[] fields = new List[]{new CopyOnWriteArrayList<String>()};
        new ExcelEventParser(inputStream).setHandler(new ExcelEventParser.SimpleSheetContentsHandler(){
            private List<String> field;
            @Override
            public void endRow(int rowNum) {
                if(rowNum == 0){
                    // 第一行中文描述忽略
                    fields[0] =row;
                }else if(rowNum == 1){
                    // 第二行字段名
                    field = row;
                    //fields[0] =row;
                }else {
                    // 数据
                        table.add(row);
                }
            }
        }).parse();

        long end = System.currentTimeMillis();

        System.err.println(table.size());
        System.err.println(end - start);
        List<String> field = fields[0];
        for (int i=0;i<field.size();i++){
            System.out.println(field.get(i));
        }

        System.out.println("得到第一行数据 = " + table.get(0).toString());
        System.out.println("得到最后一行数据 = " + table.get(70528).toString());



    }
}
