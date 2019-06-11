package com.test;

import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.AnalysisApplication;
import zd.zdcommons.analysis.ClockAnalysis;
import zd.zdcommons.pojo.ResultMessage;
import zd.zdcommons.read.ReadIntegrityExcel;
import zd.zdcommons.read.ReadclockExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  AnalysisApplication.class)
public abstract class test3 {



    @Test
    public void gets() throws IOException {

        File file2 = new File("D:\\工作文件\\开发文件\\clock.xlsx");
        File file3 = new File("D:\\工作文件\\开发文件\\test.xlsx");
        FileInputStream fileInputStream = new FileInputStream(file2);
        FileInputStream fileInputStream3 = new FileInputStream(file3);

        MultipartFile multipartFile =new MockMultipartFile("file", file2.getName(), "text/plain",
        IOUtils.toByteArray(fileInputStream));

        MultipartFile multipartFile3 =new MockMultipartFile("file", file3.getName(), "text/plain",
                IOUtils.toByteArray(fileInputStream3));

        ReadIntegrityExcel integrityExcel = new ReadIntegrityExcel();
        ReadclockExcel ddsd =   new  ReadclockExcel();
        List<Map<String, Object>> excel = integrityExcel.getExcel(multipartFile3);//实施
        List<Map<String, Object>> maps = ddsd.getExcel(multipartFile);//打卡

        Map<String, Object> map = excel.get(0);//实施的一条数据
        ClockAnalysis clockAnalysis = new ClockAnalysis();
       // ResultMessage res = clockAnalysis.clockfenxi(map, excel);


       // System.out.println("获取的最终结果"+res);


    }
}
