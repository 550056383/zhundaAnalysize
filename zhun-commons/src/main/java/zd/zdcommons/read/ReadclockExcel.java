package zd.zdcommons.read;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.serviceImp.ReadExcelImp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadclockExcel implements ReadExcelImp {

    //打卡表头数组
    private static final String dakab[] = {"Num", "UserName", "Company", "Clock-time", "Distance", "Address", "Note",
    "DUID","DU-name", "CustomersiteID", "Customersite-name", "Issite-punches", "Clock-type", "Up-downSite",
     "Ismore-inclockSite", "Reason-moreSite", "Otherreason-moreSite", "Isdistance-abnormal", "Activity-name", "Province", "Delivery-area",
     "Activity-streams", "Picture", "PassID", "Expiry-date", "Skill-level", "Site-number", "Facerecognition-results",
     "Offline-abnormal", "Fence-scene",
     "Facerecognition-pictureresults", "Custom-clock"};
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    @Override
    public List<Map<String, Object>> getExcel(MultipartFile file) {
        System.out.println("进入读取");
        //创建类型接受
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //检查文件
//        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        System.out.println("获取的对象----"+workbook);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回

        if(workbook != null){

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheet("打卡详情报表");
            if (sheet==null){
                System.out.println("是否获取这里");
                sheet = workbook.getSheetAt(0);
            }
            String sheetName = sheet.getSheetName();
            System.out.println("sheetName:::::::::::"+sheetName);
            //获得当前sheet的开始行
            int firstRowNum  = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了第一行的所有行
            for(int rowNum = 2;rowNum <= lastRowNum;rowNum++){
                //获得当前行
                Row row = sheet.getRow(rowNum);
                Map<String, Object> map = new HashMap<String, Object>();
                if(row == null){
                    continue;
                }
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获得当前行的列数
                int lastCellNum =32;
                String[] cells = new String[row.getPhysicalNumberOfCells()];
                //循环当前行
                for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                    Cell cell = row.getCell(cellNum);
                    String s = "";
                    if(cell != null){
                        //让日期类型转换成天数
                        cell.setCellType(CellType.STRING);
                        s = cell.toString().trim();

                    }
                    map.put(dakab[cellNum],s);
                }
                list.add(map);
            }
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith(xls)){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith(xlsx)){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return workbook;
    }
}
