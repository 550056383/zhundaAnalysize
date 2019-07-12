package zd.zdcommons.wirte;

import jxl.write.WritableCellFormat;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class writeExcel {
    public static void writeExcecl(String []titles, List<Map<String,Object>> lisMap, String fileName, String shteeName){
        //第一步，创建一个workbook对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        HSSFSheet sheet = workbook.createSheet(shteeName!=""?shteeName:"分析结果表");
        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow row = sheet.createRow(0);
        //HSSFCellStyle style =workbook.createCellStyle();
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
            WritableCellFormat format = new WritableCellFormat();
            //添加一行
            HSSFRow row1 = sheet.createRow(cellsum + 1);
            CellStyle style = workbook.createCellStyle();
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(IndexedColors.AQUA.index);
            for (int j=0;j<oneData.size();j++){
                //System.out.println(oneData.get(j));
                HSSFCell cellx = row1.createCell(j);
                cellx.setCellValue(oneData.get(j));
                cellx.setCellStyle(style);
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
