package com.test;

import org.apache.poi.util.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.Set;

public class test4 {
     public static void main(String args[]) throws IOException {
         //   File file2 = new File("D:\\工作文件\\开发文件\\新测试\\打卡统计报表.xlsx");
         File file2 = new File("D:\\工作文件\\开发文件\\新测试\\tess.xlsx");//能出来
         FileInputStream fileInputStream = new FileInputStream(file2);
         MultipartFile multipartFile =new MockMultipartFile("file", file2.getName(),
         "text/plain", IOUtils.toByteArray(fileInputStream));
         ReadclockExcel ddsd =   new  ReadclockExcel();
         List<Map<String, Object>> maps = ddsd.getExcel(multipartFile);//打卡


         /*File file3 = new File("D:\\工作文件\\开发文件\\shishi2.xlsx");
         FileInputStream fileInputStream3 = new FileInputStream(file3);
         MultipartFile multipartFile3 =new MockMultipartFile("file", file3.getName(),
         "text/plain",IOUtils.toByteArray(fileInputStream3));
         ReadIntegrityExcel integrityExcel = new ReadIntegrityExcel();
         List<Map<String, Object>> excel = integrityExcel.getExcel(multipartFile3);//实施*/


         for (Map<String, Object> map : maps) {
             Set<Map.Entry<String, Object>> entries = map.entrySet();
             for (Map.Entry<String, Object> entry : entries) {
                 System.out.println("键---"+entry.getKey()+"---值----"+entry.getValue());
             }
         }


     }
}
