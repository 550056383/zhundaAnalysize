package zd.zdcommons.read;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.serviceImp.ReadExcelImp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadIntegrityExcel implements ReadExcelImp {
    //数组
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
            int firstRowNum  = 3;
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            System.out.println("结束行:"+lastRowNum);
            //循环除了第一行的所有行 1
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
                for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                    Cell cell = row.getCell(cellNum);
                    String s = "";
                    if(cell != null){
                        //让日期类型转换成天数
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        s = cell.toString().trim();
                    }
                    map.put(shu[cellNum],s);
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
