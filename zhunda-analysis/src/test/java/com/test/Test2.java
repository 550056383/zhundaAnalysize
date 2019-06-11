package com.test;

import org.apache.http.entity.ContentType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.AnalysisApplication;
import zd.zdcommons.FactoryProducer;
import zd.zdcommons.abstractFactory.AnalysisAbstractFactory;
import zd.zdcommons.analysis.Complete;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  AnalysisApplication.class)
public class Test2 {


    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    //表头获取
    @Test
    public void test01() throws Exception {
        File file = new File("E:\\Zhunda\\tex.xls");
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        List<Map<String, Object>> maps = readExcelV2(multipartFile);
    }
    @Test
    public void testLenght(){

        System.out.println("yuan ==="+shu[42]);
    }
    public static List<Map<String,Object>> readExcelV2(MultipartFile file) throws IOException {
        //创建类型接受
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //检查文件
//        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回

        if(workbook != null){

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheet("Site Rollout Plan");
            if (sheet==null){
                sheet = workbook.getSheetAt(0);
            }
            String sheetName = sheet.getSheetName();
            System.out.println("sheetName:::::::::::"+sheetName);
            //获得当前sheet的开始行
            int firstRowNum  = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            System.out.println("结束行:"+lastRowNum);
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
                //int lastCellNum = row.getLastCellNum();
                int lastCellNum =128;
//                System.out.println("开始列：："+firstCellNum);
//                System.out.println("结束列：："+lastCellNum);
                String[] cells = new String[row.getPhysicalNumberOfCells()];
                //循环当前行
                String test="";
                for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                    Cell cell = row.getCell(cellNum);
                    String s = " - ";
                    if(cell != null){
                        //让日期类型转换成天数
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        s = cell.toString().trim();
                    }
                    if (s.equals("宏站")){
                        System.out.println("shu=="+shu[cellNum] +"cellNum::"+cellNum);
                    }
                    test+="\""+s+"\""+",";
                }
                File writeName = new File("E:\\Iproject\\output.txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
                FileWriter writer = new FileWriter(writeName);
                BufferedWriter out = new BufferedWriter(writer);
                out.write(test); // \r\n即为换行
                out.flush(); // 把缓存区内容压入文件
            }
            try {
                workbook.close();
            } catch (Exception e) {
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
    private static String shu[] = {"YD5-customerSiteID","YD5-customerSiteName","YD5-dUID",
            "YD5-dUName","YD5-area","YD5-Subcontractor","YD5-supervisionUnit","YD5-SC-baselineStartDate",
            "YD5-SC-baselineEndDate","YD5-SC-planStartDate","YD5-SC-planEndDate","YD5-SC-actualStartDate",
            "YD5-SC-actualEndDate","YD5-SC-owner","YD5-SC-approveState","YD5-SC-deliveryAttachmentRequired",
            "YD5-SC-deliveryAttachmentUploaded","YD5-SC-accumulation","YD5-SC-remarks",
            "YD5-SC-delayReason","YD5-AAU-baselineStartDate","YD5-AAU-baselineEndDate","YD5-AAU-planStartDate",
            "YD5-AAU-planEndDate","YD5-AAU-actualStartDate","YD5-AAU-actualEndDate",
            "YD5-AAU-owner","YD5-AAU-approveState","YD5-AAU-deliveryAttachmentRequired",
            "YD5-AAU-deliveryAttachmentUploaded","YD5-AAU-accumulation","YD5-AAU-delayReason",
            "YD5-residentialBroadband","YD5-transmissionEquipped","YD5-deliveryType","YD5-Nmneid",
            "YD5-nROPO","YD5-rruToneNumber","YD5-rruHardwareNumber","YD5-spectrum","YD5-nroServiceContract",
            "YD5-scenario","YD5-auuArrivalQuantity","YD5-problemClassification","YD5-RFI-baselineEndDate",
            "YD5-RFI-planEndDate","YD5-RFI-actualEndDate","YD5-RFI-owner","YD5-RFI-approveState",
            "YD5-RFI-deliveryAttachmentRequired","YD5-RFI-deliveryAttachmentUploaded","YD5-RFI-accumulation",
            "YD5-RFI-remarks","YD5-RFI-delayReason","YD5-MOS-baselineEndDate","YD5-MOS-planEndDate",
            "YD5-MOS-actualEndDate","YD5-MOS-owner","YD5-MOS-approveState","YD5-MOS-deliveryAttachmentRequired",
            "YD5-MOS-deliveryAttachmentUploaded","YD5-MOS-accumulation","YD5-MOS-remarks","YD5-MOS-delayReason",
            "YD5-completionDate","YD5-receptionDate","YD5-IC-baselineEndDate","YD5-IC-planEndDate",
            "YD5-IC-actualEndDate","YD5-IC-owner","YD5-IC-approveState","YD5-IC-deliveryAttachmentRequired",
            "YD5-IC-deliveryAttachmentUploaded","YD5-IC-accumulation","YD5-IC-remarks",
            "YD5-IC-delayReason","YD5-engineeringServiceMode","YD5-planningNumber","YD5-tianmianTransformation",
            "YD5-dcFuse","YD5-acInduction","YD5-design","YD5-bbuToneNumber","YD5-bbuESN","YD5-bbuSiteID",
            "YD5-bbuSiteName","YD5-bbuScenario","YD5-bbuHardwareNumber","YD5-deliveryRegion",
            "YD5-nmNEName","YD5-rruSiteID","YD5-rruSiteName","YD5-rruScenario","YD5-rruBoxNo","YD5-standingType",
            "YD5-productModel","YD5-contractConnection","YD5-remoteStationType","YD5-standard",
            "YD5-transmissionBandwidth","YD5-nroSubcontractor","YD5-standingType2",
            "MIMO-miMO3DDate","MIMO-miMO3DID","MIMO-miMO3DGoodsQuantity","MIMO-deliveryDate",
            "MIMO-questionClassification","","MIMO-planningNumber","MIMO-installationDate","MIMO-completionDate",
            "MIMO-nmNEID","MIMO-openTypeStand","MIMO-openTypeStandTarget","MIMO-baseStationName","MIMO-transmissionBandwidthe4G",
            "MIMO-transmissionAvailable4G","M1800-deliveryDateFDD","M1800-programNumberFDD",
            "M1800-constructionPlanFDD","M1800-questionClassificationFDD","M1800-whetherPlanningFDD",
            "M1800-arrivalDateFDD","M1800-deliveryCompletionDateFFD","M1800-installationFDD","M1800-openedFDD",
            "M1800-nmNEIDFDD","M1800-baseStationNameFDD"};
    private static String shu1 [] ={"YD5-customerSiteID","YD5-customerSiteName","YD5-dUID","YD5-dUName","YD5-area","YD5-Subcontractor","YD5-supervisionUnit","YD5-SC-baselineStartDate","YD5-SC-baselineEndDate","YD5-SC-planStartDate","YD5-SC-planEndDate","YD5-SC-actualStartDate","YD5-SC-actualEndDate","YD5-SC-owner","YD5-SC-approveState","YD5-SC-deliveryAttachmentRequired","YD5-SC-deliveryAttachmentUploaded","YD5-SC-accumulation","YD5-SC-remarks","YD5-SC-delayReason","YD5-AAU-baselineStartDate",
            "YD5-AAU-baselineEndDate","YD5-AAU-planStartDate","YD5-AAU-planEndDate","YD5-AAU-actualStartDate","YD5-AAU-actualEndDate","YD5-AAU-owner","YD5-AAU-approveState","YD5-AAU-deliveryAttachmentRequired","YD5-AAU-deliveryAttachmentUploaded","YD5-AAU-accumulation","YD5-AAU-remarks","YD5-AAU-delayReason","YD5-residentialBroadband","YD5-transmissionEquipped","YD5-deliveryType","YD5-Nmneid","YD5-nROPO","YD5-rruToneNumber","YD5-rruHardwareNumber","YD5-spectrum","YD5-nroServiceContract",
            "YD5-scenario","YD5-auuArrivalQuantity","YD5-problemClassification","YD5-RFI-baselineEndDate","YD5-RFI-planEndDate","YD5-RFI-actualEndDate","YD5-RFI-owner","YD5-RFI-approveState","YD5-RFI-deliveryAttachmentRequired","YD5-RFI-deliveryAttachmentUploaded","YD5-RFI-accumulation","YD5-RFI-remarks","YD5-RFI-delayReason","YD5-MOS-baselineEndDate","YD5-MOS-planEndDate","YD5-MOS-actualEndDate","YD5-MOS-owner","YD5-MOS-approveState","YD5-MOS-deliveryAttachmentRequired","YD5-MOS-deliveryAttachmentUploaded",
            "YD5-MOS-accumulation","YD5-MOS-remarks","YD5-MOS-delayReason","YD5-completionDate","YD5-receptionDate","YD5-IC-baselineEndDate","YD5-IC-planEndDate","YD5-IC-actualEndDate","YD5-IC-owner","YD5-IC-approveState","YD5-IC-deliveryAttachmentRequired","YD5-IC-deliveryAttachmentUploaded","YD5-IC-accumulation","YD5-IC-remarks","YD5-IC-delayReason","YD5-engineeringServiceMode","YD5-planningNumber","YD5-tianmianTransformation","YD5-dcFuse","YD5-acInduction","YD5-design","YD5-bbuToneNumber",
            "YD5-bbuESN","YD5-bbuSiteID","YD5-bbuSiteName","YD5-bbuScenario","YD5-bbuHardwareNumber","YD5-deliveryRegion","YD5-nmNEName","YD5-rruSiteID","YD5-rruSiteName","YD5-rruScenario","YD5-rruBoxNo","YD5-standingType","YD5-productModel","YD5-contractConnection","YD5-remoteStationType","YD5-standard","YD5-transmissionBandwidth","YD5-nroSubcontractor","YD5-standingType2","MIMO-miMO3DDate","MIMO-miMO3DID","MIMO-miMO3DGoodsQuantity","MIMO-deliveryDate","MIMO-questionClassification",
            "MIMO-planningNumber","MIMO-installationDate","MIMO-completionDate","MIMO-nmNEID","MIMO-openTypeStand","MIMO-openTypeStandTarget","MIMO-baseStationName","MIMO-transmissionBandwidthe4G","MIMO-transmissionAvailable4G","M1800-deliveryDateFDD","M1800-programNumberFDD","M1800-constructionPlanFDD","M1800-questionClassificationFDD","M1800-whetherPlanningFDD","M1800-arrivalDateFDD","M1800-deliveryCompletionDateFFD","M1800-installationFDD","M1800-openedFDD","M1800-nmNEIDFDD","M1800-baseStationNameFDD"};
    private static final String[] yuan = {"Customer Site ID","Customer Site Name","DU ID",
            "DU Name","行政区域","Subcontractor","督导单位","Software Commissioning-Baseline Start Date",
            "Software Commissioning-Baseline End Date",
            "Software Commissioning-Plan Start Date","Software Commissioning-Plan End Date",
            "Software Commissioning-Actual Start Date","Software Commissioning-Actual End Date",
            "Software Commissioning-Owner","Software Commissioning-Approve State",
            "Software Commissioning-Delivery Attachment Required",
            "Software Commissioning-Delivery Attachment Uploaded",
            "Software Commissioning-Accumulation",
            "Software Commissioning-Remarks","Software Commissioning-Delay Reason",
            "AAU开通-Baseline Start Date","AAU开通-Baseline End Date","AAU开通-Plan Start Date",
            "AAU开通-Plan End Date","AAU开通-Actual Start Date","AAU开通-Actual End Date","AAU开通-Owner",
            "AAU开通-Approve State",
            "AAU开通-Delivery Attachment Required","AAU开通-Delivery Attachment Uploaded",
            "AAU开通-Accumulation","AAU开通-Remarks","AAU开通-Delay Reason","5G小区带宽","5G传输具备","Delivery Type",
            "NM NE ID","NRO PO","RRU软调数量","RRU硬件数量","频段","NRO服务合同号","场景","5G AAU到货数量",
            "5G问题分类","Ready For Installation-Baseline End Date","Ready For Installation-Plan End Date",
            "Ready For Installation-Actual End Date",
            "Ready For Installation-Owner","Ready For Installation-Approve State","Ready For Installation-Delivery Attachment Required",
            "Ready For Installation-Delivery Attachment Uploaded","Ready For Installation-Accumulation",
            "Ready For Installation-Remarks","Ready For Installation-Delay Reason",
            "Material On Site-Baseline End Date","Material On Site-Plan End Date",
            "Material On Site-Actual End Date","Material On Site-Owner","Material On Site-Approve State",
            "Material On Site-Delivery Attachment Required","Material On Site-Delivery Attachment Uploaded",
            "Material On Site-Accumulation",
            "Material On Site-Remarks","Material On Site-Delay Reason","5G交优完成日期","5G 交优接收日期",
            "Installation-Completed-Baseline End Date","Installation-Completed-Plan End Date"
            ,"Installation-Completed-Actual End Date","Installation-Completed-Owner",
            "Installation-Completed-Approve State","Installation-Completed-Delivery Attachment Required",
            "Installation-Completed-Delivery Attachment Uploaded","Installation-Completed-Accumulation",
            "Installation-Completed-Remarks","Installation-Completed-Delay Reason",
            "工程服务方式","5G规划编号","天面改造进展","直流空开熔丝","交流引入","是否有设计图纸","BBU软调数量",
            "BBU ESN","BBU Site ID","BBU Site Name","BBU交付场景","BBU硬件数量","Delivery Region",
            "NM NE Name","RRU Site ID","RRU Site Name","RRU交付场景","RRU框号","站型","产品型号",
            "合同挂接方式","拉远站类型","制式","5G传输带宽","NRO Subcontractor","站 型2","3D-MIMO交优完成日期",
            "3D-MIMO规划编号","3D-MIMO到货数量","3D-MIMO 交优接收日期","3D-MIMO问题分类"
            ,"3D-MIMO规划数量","3D-MIMO安装时间","3D-MIMO开通完成日期","3D-MIMO NM NE ID","3D-MIMO开通站型",
            "3D-MIMO目标站型（设计图纸）","3D-MIMO网管基站名称","4G传输带宽","4G传输具备","FDD1800 交优接收日期",
            "FDD1800规划编号","FDD1800施工计划","锚点FDD1800问题分类","FDD1800是否规划","FDD1800到货日期",
            "FDD1800交优完成日期","FDD1800安装","FDD1800开通","FDD1800 NM NE ID","FDD1800网管基站名称"};
}
