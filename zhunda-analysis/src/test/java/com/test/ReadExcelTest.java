package com.test;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.AnalysisApplication;

import java.io.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes =  AnalysisApplication.class)
public class ReadExcelTest {
    @Test
    public void  Test1() throws Exception {
        File file = new File("E:\\Zhunda\\工程\\PURCHASE_ORDER_20190611150319（华为系统订单信息）.xlsx");
        FileInputStream fileInput = new FileInputStream(file);
        MultipartFile toMultipartFile = new MockMultipartFile("file",file.getName(),"text/plain", IOUtils.toByteArray(fileInput));
        toMultipartFile.getInputStream();
        getExcelTitle(toMultipartFile,"sheet",1);
    }
    //打卡表头数组
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    public String[] getExcelTitle(MultipartFile file,String sheetName,int indexTitle) throws Exception {
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        String [] title={};
        if(workbook != null){
            //获得当前sheet工作表
            Sheet sheet =workbook.getSheet(sheetName);
                    //workbook.getSheet("Site Rollout Plan");
            if (sheet==null){
                sheet = workbook.getSheetAt(1);
            }
            String sheetTrueName = sheet.getSheetName();
            System.out.println("sheetName:::::::::::"+sheetTrueName);
            //获得当前sheet的开始行
            int firstRowNum  = sheet.getFirstRowNum();
            //得到标准头
            String ne="";
            //循环所有行
            for(int rowNum = firstRowNum;rowNum <indexTitle;rowNum++){
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if(row == null){
                    continue;
                }
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获得当前行的列数
                int lastCellNum =row.getLastCellNum();
                //System.out.println("lastCellNum=="+lastCellNum);
                if(rowNum==0) title=new String[lastCellNum];
                //循环当前行
                for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                    String value = getCellValueV2(sheet, rowNum, cellNum).trim();
                    if(!value.equals(title[cellNum])){
                        if(rowNum==0){
                            title[cellNum]=value;
                        }else {
                            title[cellNum]=title[cellNum]+"--"+value;
                        }
                    }
                }

            }
        }
        return title;
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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return workbook;
    }
    /**
     * 获取单元表格值
     * 
     * */
    public String getCellValueV2(Sheet sheet,int rowNum,int cellNum){
        if(isMergedRegion(sheet,rowNum,cellNum)){
            return getMergedRegionValue(sheet,rowNum,cellNum);
        }else{
            //获取当前行
            Row row = sheet.getRow(rowNum);
            row.getCell(cellNum).setCellType(CellType.STRING);
            return row.getCell(cellNum).getStringCellValue();
        }
    }
    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public  String getCellValue(Cell cell){
        if(cell == null) return "";
        return cell.getStringCellValue();
    }
    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public  String getMergedRegionValue(Sheet sheet ,int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();

        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    if(fCell == null) return "";
                    return fCell.getStringCellValue();
                }
            }
        }

        return null ;
    }
    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private  boolean isMergedRegion(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }
}
