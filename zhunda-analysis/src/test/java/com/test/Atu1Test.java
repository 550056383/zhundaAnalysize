package com.test;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import zd.zdanalysis.AnalysisApplication;
import zd.zdcommons.pojo.ResultMessage;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  AnalysisApplication.class)
public class Atu1Test {
    /**
     * 将数据写入到excel中
     */
    public static  void makeExcel(List<ResultMessage> rem) {

        //第一步，创建一个workbook对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        HSSFSheet sheet = workbook.createSheet("病例日期表");
        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow row = sheet.createRow(0);
        //第四步，创建单元格，设置表头
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("duId");
        cell = row.createCell(1);
        cell.setCellValue("duName");
        cell = row.createCell(2);
        cell.setCellValue("错误类型");
        cell = row.createCell(3);
        cell.setCellValue("错误信息");


        //第五步，写入数据
        for(int i=0;i<rem.size();i++) {
            List<String> oneData=new ArrayList<String>();
            //封装每一个属性
            oneData.add(rem.get(i).getDUID().toString());
            oneData.add(rem.get(i).getDUName().toString());
            oneData.add(rem.get(i).getTError().toString());
            oneData.add(rem.get(i).getMessge().toString());
            HSSFRow row1 = sheet.createRow(i + 1);
            for(int j=0;j<oneData.size();j++) {

                //创建单元格设值
                row1.createCell(j).setCellValue(oneData.get(j));
            }
        }

        //将文件保存到指定的位置
        try {
            FileOutputStream fos = new FileOutputStream("F:\\test\\result.xls");
            workbook.write(fos);
            System.out.println("写入成功");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getUUId(){
        String uuid = UUID.randomUUID().toString();   //转化为String对象
        uuid = uuid.replace("-", ""); //因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        return uuid;
    }
    @Test
    public void test1(){
        String uuid = UUID.randomUUID().toString();   //转化为String对象
        uuid = uuid.replace("-", ""); //因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        System.out.println(uuid);
    }
}
