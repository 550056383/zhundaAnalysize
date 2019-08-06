package zd.zdcommons.wirte;

import jxl.write.WritableCellFormat;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/18 09:27
 */
public class WriteNewExcel {
    public static void writeExcecl(String []titles, List<Map<String,Object>> lisMap, String fileName, String shteeName){
        //第一步，创建一个workbook对应一个excel文件
        XSSFWorkbook workbook=new XSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        XSSFSheet sheet = workbook.createSheet(shteeName!=""?shteeName:"分析结果表");
        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        XSSFRow row = sheet.createRow(0);
        //HSSFCellStyle style =workbook.createCellStyle();
        //第四步，创建单元格，设置表头
        XSSFCell cell =null;
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
              //  System.out.println(entry.getKey()+entry.getValue());
                oneData.add(entry.getValue().toString().trim());
            }
            WritableCellFormat format = new WritableCellFormat();
            //添加一行
            XSSFRow row1 = sheet.createRow(cellsum + 1);
            CellStyle style = workbook.createCellStyle();
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(IndexedColors.AQUA.index);
            for (int j=0;j<oneData.size();j++){
                //System.out.println(oneData.get(j));
                XSSFCell cellx = row1.createCell(j);
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
            FileOutputStream outputStream = new FileOutputStream(save + "\\" + fileName + ".xlsx");
            //工作簿写入
            workbook.write(outputStream);
            //关闭流
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getOS(){
        String save = "";
        try {
            File files = ResourceUtils.getFile("classpath:static");
            save = files.toString();
            String os = System.getProperty("os.name");
            if (!os.toLowerCase().startsWith("win")) {
                save = "/opt/zhundaSave/";
                System.out.println("There is Linux");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return save;
    }
}
