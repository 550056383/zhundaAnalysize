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
import zd.zdcommons.pojo.Message;
import zd.zdcommons.pojo.ResultMessage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  AnalysisApplication.class)
public class WriteExcelTest {
    @Test
    public  void Test1(){
        String []title={"PO","Ne","NE"};
        Map<String,Object> omap1=new HashMap<String, Object>();
        omap1.put("PO","Po-13456");
        omap1.put("Ne","NE-156888");
        omap1.put("NE","3成");
        Map<String,Object> omap2=new HashMap<String, Object>();
        omap2.put("PO","险你111");
        omap2.put("Ne","58同城");
        omap2.put("NE","2成");
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        mapList.add(omap1);
        mapList.add(omap2);
        writeExcecl(title,mapList,"测试文档","");
    }
    public static void writeExcecl(String []titles, List<Map<String,Object>> lisMap,String fileName,String shteeName){
        //第一步，创建一个workbook对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        HSSFSheet sheet = workbook.createSheet(shteeName!=""?shteeName:"分析结果表");
        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow row = sheet.createRow(0);
        //第四步，创建单元格，设置表头
        HSSFCell cell =null;
        for (int i=0;i<titles.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        int cellsum=0;
        //写数据
        //1.计算写入行数
        for(int i=0;i<lisMap.size();i++){
            Map<String, Object> objectMap = lisMap.get(i);
            //获得一条数据将每列存入list<String>
            List<String> oneData= new ArrayList<String>();
            //便利Map添加一行数据的每列数据
            for (Map.Entry<String,Object> entry:objectMap.entrySet()){
                oneData.add(entry.getValue().toString().trim());
            }

            //添加一行
            HSSFRow row1 = sheet.createRow(cellsum + 1);
            for (int j=0;j<oneData.size();j++){
                //System.out.println(oneData.get(j));
                row1.createCell(j).setCellValue(oneData.get(j));
            }
            //追加行数
            cellsum++;
        }
        //写入数据
        try {
            //创建存储位置
            String save = getOS();
            //创建输出流
            FileOutputStream outputStream= new FileOutputStream(save+fileName+".xls");
            //工作簿写入
            workbook.write(outputStream);
            //关闭流
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static  void writeExcel(List<ResultMessage> rem, String name) {
        System.out.println("write Excel is comming");
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
        cell.setCellValue("行政区域");
        cell = row.createCell(3);
        cell.setCellValue("错误类型");
        cell = row.createCell(4);
        cell.setCellValue("所属类型");
        cell = row.createCell(5);
        cell.setCellValue("错误信息");
        System.out.println("回传多少数据"+rem.size());

        int cellsum=0;
        //第五步，写入数据
        for(int i=0;i<rem.size();i++) {
            List<Message> mes = rem.get(i).getMessge();
            for (int z=0;z<mes.size();z++){
                //拿取错误类型5g，3D,1800瞄点
                List<String> messages = mes.get(z).getMessages();

                for (int j=0;j<messages.size();j++){
                    List<String> oneData=new ArrayList<String>();
                    //封装每一个属性
                    oneData.add(rem.get(i).getDUID().toString());
                    oneData.add(rem.get(i).getDUName().toString());
                    oneData.add(rem.get(i).getDarea().toString());
                    oneData.add(rem.get(i).getTError().toString());
                    oneData.add(mes.get(z).getTitle().toString());
                    oneData.add(messages.get(j));
                    //添加一行
                    HSSFRow row1 = sheet.createRow(cellsum + 1);
                    cellsum++;
                    for(int x=0;x<oneData.size();x++) {
                        //创建单元格设值
                        row1.createCell(x).setCellValue(oneData.get(x));
                    }
                }

            }


        }

        //将文件保存到指定的位置
        try {
            String save=getOS();

            System.out.println("save==="+save);
            System.out.println("file save address::"+save+name+".xls");
            FileOutputStream fos = new FileOutputStream(save+name+".xls");
            workbook.write(fos);
            System.out.println("write is ok");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getOS(){
        String save="F:\\test\\";
        String os = System.getProperty("os.name");
        if(!os.toLowerCase().startsWith("win")){
            save="/opt/zhundaSave/";
            System.out.println("There is Linux");
        }
        return save;
    }
}
