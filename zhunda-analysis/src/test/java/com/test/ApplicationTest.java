package com.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.AnalysisApplication;
import zd.zdanalysis.pojo.*;
import zd.zdcommons.pojo.ResultMessage;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=AnalysisApplication.class)
public class ApplicationTest {

    @Test
    public void Test1() throws Exception {

        File file = new File("E:\\Zhunda\\test.xlsx");
        List<Map<String, Object>> maps = readExcel(file);
        //记录次数
        int count=0;
        for(Map e:maps){
            ResultMessage r = getIntegrityAnalysize(e);
            if(r!=null){
                count++;
                //进入输出文档中
            }
        }
    };
    @Test
    public void Test2() throws ParseException {
        String s = importByExcelForDate(43553 + "");
        System.out.println(s);
    }

    public static String importByExcelForDate(String value) {//value就是它的天数
        String currentCellValue = "";
        if(value != null && !value.equals("")){
            Calendar calendar = new GregorianCalendar(1900,0,-1);
            Date d = calendar.getTime();
            Date dd = DateUtils.addDays(d,Integer.valueOf(value));
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            currentCellValue = formater.format(dd);
        }
        return currentCellValue;
    }
    public static String getDayForDate(String value){
        Calendar calendar = new GregorianCalendar(1900,0,-1);
        Date d = calendar.getTime();
        Date dd = DateUtils.addDays(d,Integer.valueOf(value));
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        return "";
    }
    public static final String[] mes={"YD5-customerSiteID","YD5-customerSiteName","YD5-dUID","YD5-dUName",
            "YD5-area","YD5-SC-planEndDate","YD5-SC-actualEndDate","YD5-SC-deliveryAttachmentRequired",
            "YD5-SC-deliveryAttachmentUploaded","YD5-residentialBroadband","YD5-transmissionEquipped",
            "YD5-deliveryType","YD5-Nmneid","YD5-nROPO","YD5-rruToneNumber","YD5-rruHardwareNumber",
            "YD5-spectrum","YD5-nroServiceContract","YD5-scenario","YD5-auuArrivalQuantity",
            "YD5-problemClassification","YD5-RFI-actualEndDate","YD5-MOS-planEndDate","YD5-MOS-actualEndDate",
            "YD5-IC-planEndDate","YD5-IC-actualEndDate","YD5-engineeringServiceMode","YD5-planningNumber",
            "YD5-tianmianTransformation","YD5-dcFuse","YD5-acInduction","YD5-design","YD5-bbuToneNumber",
            "YD5-bbuESN","YD5-bbuSiteID","YD5-bbuSiteName","YD5-bbuScenario","YD5-bbuHardwareNumber",
            "YD5-deliveryRegion","YD5-nmNEName","YD5-rruSiteID","YD5-rruSiteName","YD5-rruScenario",
            "YD5-rruBoxNo","YD5-standingType","YD5-productModel","YD5-contractConnection","YD5-remoteStationType",
            "YD5-standard","YD5-transmissionBandwidth","YD5-nroSubcontractor","YD5-standingType2","MIMO-miMO3DDate",
            "MIMO-miMO3DID","MIMO-miMO3DGoodsQuantity","MIMO-deliveryDate","MIMO-questionClassification",
            "MIMO-planningNumber","MIMO-installationDate","MIMO-completionDate","MIMO-nmNEID","MIMO-openTypeStand",
            "MIMO-openTypeStandTarget","MIMO-baseStationName","MIMO-transmissionBandwidthe4G",
            "MIMO-transmissionAvailable4G","M1800-deliveryDateFDD","M1800-programNumberFDD",
            "M1800-constructionPlanFDD","M1800-questionClassificationFDD","M1800-whetherPlanningFDD",
            "M1800-arrivalDateFDD","M1800-deliveryCompletionDateFFD","M1800-installationFDD","M1800-openedFDD",
            "M1800-nmNEIDFDD","M1800-baseStationNameFDD"};

    public ResultMessage getIntegrityAnalysize(Map<String, String> resource){
        //结果信息
        ResultMessage r = null;
        //Listmessge
        ArrayList<String> list = new ArrayList<String>();
        //获取数据
        for(int i=0;i<mes.length;i++){
            if (StringUtils.isBlank(resource.get(mes[i]).toString())){
                list.add(mes[i]);
            }
        }
        if(list.size()>0){
            r=new ResultMessage();
            r.setDUID(resource.get("YD5-dUID").toString());
            r.setDUName(resource.get("YD5-dUName").toString());
          //  r.setMessge(list);
        }
        return r;
    }

//    public Date getSimpleDate(String d1){
//        DateFormat bf = new SimpleDateFormat("dd-MM-yyyy");
//        Date t1=new Date();
//        try {
//            t1=bf.parse(d1);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return t1;
//    };

    public static List<Map<String,Object>> readExcel(File file){
        String[] str = new String[128];
        //创建类型接受
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            InputStream inputStream = new FileInputStream(file);
            Workbook wb = null;
            String fileName = file.getName();

            String suffix = fileName.substring(fileName.indexOf(".") + 1);
            if ("xlsx".equals(suffix)) {//解析xlsx格式
                wb = new XSSFWorkbook(inputStream);
            } else if ("xls".equals(suffix)) {
                wb = new HSSFWorkbook(inputStream);//解析xls格式
            } else {
                return list;
            }
            Sheet sheet = wb.getSheetAt(0);//第一个工作表  ，第二个则为1，以此类推...
            //int firstRowIndex = sheet.getFirstRowNum();
            int firstRowIndex =0;
            int lastRowIndex = sheet.getLastRowNum();



            for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex ++){
                Row row = sheet.getRow(rIndex);

                Map<String, Object> map = new HashMap<String, Object>();

                if(row != null){
                    int firstCellIndex = row.getFirstCellNum();
                    int lastCellIndex = row.getLastCellNum();
                    for(int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex ++){
                        Cell cell = row.getCell(cIndex);
                        String value = "";
                        if(cell != null){
                            //让日期类型转换成天数
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            value = cell.toString();
                        }
                        //判断标题和数据
                        if(rIndex==0){
                            //标题
                            str[cIndex]=value;
                        }else{
                            //数据
                            map.put(str[cIndex],cell.toString());
                        }
                        System.out.print(value+"\t");
                    }

                    System.out.println();
                }
                if(rIndex!=0){
                    list.add(map);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return list;
    }
   // private static Logger logger  = Logger.getLogger(ApplicationTest.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    public static List<Map<String,Object>> readExcel(MultipartFile file) throws IOException{
        String[] str = new String[128];
        //创建类型接受
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //检查文件
//        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回

        if(workbook != null){

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum  = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了第一行的所有行
            for(int rowNum = firstRowNum;rowNum <= lastRowNum;rowNum++){
                //获得当前行
                Row row = sheet.getRow(rowNum);
                Map<String, Object> map = new HashMap<String, Object>();
                if(row == null){
                    continue;
                }
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获得当前行的列数
                int lastCellNum = row.getPhysicalNumberOfCells();
                String[] cells = new String[row.getPhysicalNumberOfCells()];
                //循环当前行
                for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                    Cell cell = row.getCell(cellNum);
                    String value = "";
                    if(cell != null){
                        //让日期类型转换成天数
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        value = cell.toString();
                    }
                    //判断标题和数据
                    if(rowNum==0){
                        //标题
                        str[cellNum]=value;
                    }else{
                        //数据
                        map.put(str[cellNum],cell.toString());
                    }
                    cells[cellNum] = getCellValue(cell);
                }
                if(rowNum!=0){
                    list.add(map);
                }

            }
            workbook.close();
        }
        return list;
    }
    public static void checkFile(MultipartFile file) throws IOException{
        //判断文件是否存在
        if(null == file){

            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){

            throw new IOException(fileName + "不是excel文件");
        }
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

    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }
    public static ResultMessage getIntegrityLogicAnalysize(Map<String, Object> resource){
        ResultMessage resultm =null;
        ArrayList<String> list = new ArrayList<String>();
        //记录count数
        long count=0;
        //获取数据
        String rfi = resource.get("YD5-RFI-actualEndDate").toString();
        String mos = resource.get("YD5-MOS-actualEndDate").toString();
        String ic = resource.get("YD5-IC-actualEndDate").toString();
        String sc = resource.get("YD5-SC-actualStartDate").toString();
        int boss=0;
        if(StringUtils.isBlank(rfi)){
            boss=1;
            list.add("YD5-RFI-actualEndDate为空");
        }
        if(StringUtils.isBlank(mos)){
            boss=1;
            list.add("YD5-MOS-actualEndDate为空");
        }
        if(StringUtils.isBlank(ic)){
            boss=1;
            list.add("YD5-IC-actualEndDate为空");
        }
        if(StringUtils.isBlank(sc)){
            boss=1;
            list.add("YD5-SC-actualStartDate为空");
        }
        if(boss>0){
            resultm= new ResultMessage();
            resultm.setTError("完整性错误");
            resultm.setDUID(resource.get("YD5-dUID").toString());
            resultm.setDUName(resource.get("YD5-dUName").toString());
            //resultm.setMessge(list);
            resultm.setXcount(count);
            return resultm;
        }

        String rfiTime = importByExcelForDate(rfi);
        String mosTime=importByExcelForDate(mos);
        String icTime = importByExcelForDate(ic);
        String scTime = importByExcelForDate(sc);

        if(Integer.parseInt(rfi)<Integer.parseInt(mos)){

            list.add("YD5-MOS-actualEndDate: "+mosTime+"  | 大于 |YD5-RFI-actualEndDate："+rfiTime);
        }
        if(Integer.parseInt(mos)<Integer.parseInt(ic)){

            list.add("YD5-IC-actualEndDate: "+icTime+"  | 大于 |YD5-MOS-actualEndDate："+mosTime);
        }
        if(Integer.parseInt(ic)<Integer.parseInt(sc)){

            list.add("YD5-SC-actualStartDate: "+scTime+"  | 大于 |YD5-IC-actualEndDate："+icTime);
        }
        if(list.size()>0){
            resultm= new ResultMessage();
            resultm.setTError("完整性错误");
            resultm.setDUID(resource.get("YD5-dUID").toString());
            resultm.setDUName(resource.get("YD5-dUName").toString());
           // resultm.setMessge(list);
            resultm.setXcount(count);
        }
        return resultm;
    }
}
