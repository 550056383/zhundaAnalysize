package zd.zdcommons;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import zd.zdcommons.abstractFactory.AnalysisAbstractFactory;
import zd.zdcommons.analysis.ClockAnalysis;
import zd.zdcommons.analysis.Complete;
import zd.zdcommons.analysis.Logic;
import zd.zdcommons.pojo.Message;
import zd.zdcommons.pojo.Pageto;
import zd.zdcommons.pojo.ResultMessage;

import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.serviceImp.ReadExcelImp;

public class Utils {
    //对应数组
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
    //数组
    private static String shu [] ={"YD5-customerSiteID","YD5-customerSiteName","YD5-dUID","YD5-dUName","YD5-area","YD5-Subcontractor","YD5-supervisionUnit","YD5-SC-baselineStartDate","YD5-SC-baselineEndDate","YD5-SC-planStartDate","YD5-SC-planEndDate","YD5-SC-actualStartDate","YD5-SC-actualEndDate","YD5-SC-owner","YD5-SC-approveState","YD5-SC-deliveryAttachmentRequired","YD5-SC-deliveryAttachmentUploaded","YD5-SC-accumulation","YD5-SC-remarks","YD5-SC-delayReason","YD5-AAU-baselineStartDate",
            "YD5-AAU-baselineEndDate","YD5-AAU-planStartDate","YD5-AAU-planEndDate","YD5-AAU-actualStartDate","YD5-AAU-actualEndDate","YD5-AAU-owner","YD5-AAU-approveState","YD5-AAU-deliveryAttachmentRequired","YD5-AAU-deliveryAttachmentUploaded","YD5-AAU-accumulation","YD5-AAU-remarks","YD5-AAU-delayReason","YD5-residentialBroadband","YD5-transmissionEquipped","YD5-deliveryType","YD5-Nmneid","YD5-nROPO","YD5-rruToneNumber","YD5-rruHardwareNumber","YD5-spectrum","YD5-nroServiceContract",
            "YD5-scenario","YD5-auuArrivalQuantity","YD5-problemClassification","YD5-RFI-baselineEndDate","YD5-RFI-planEndDate","YD5-RFI-actualEndDate","YD5-RFI-owner","YD5-RFI-approveState","YD5-RFI-deliveryAttachmentRequired","YD5-RFI-deliveryAttachmentUploaded","YD5-RFI-accumulation","YD5-RFI-remarks","YD5-RFI-delayReason","YD5-MOS-baselineEndDate","YD5-MOS-planEndDate","YD5-MOS-actualEndDate","YD5-MOS-owner","YD5-MOS-approveState","YD5-MOS-deliveryAttachmentRequired","YD5-MOS-deliveryAttachmentUploaded",
            "YD5-MOS-accumulation","YD5-MOS-remarks","YD5-MOS-delayReason","YD5-completionDate","YD5-receptionDate","YD5-IC-baselineEndDate","YD5-IC-planEndDate","YD5-IC-actualEndDate","YD5-IC-owner","YD5-IC-approveState","YD5-IC-deliveryAttachmentRequired","YD5-IC-deliveryAttachmentUploaded","YD5-IC-accumulation","YD5-IC-remarks","YD5-IC-delayReason","YD5-engineeringServiceMode","YD5-planningNumber","YD5-tianmianTransformation","YD5-dcFuse","YD5-acInduction","YD5-design","YD5-bbuToneNumber",
            "YD5-bbuESN","YD5-bbuSiteID","YD5-bbuSiteName","YD5-bbuScenario","YD5-bbuHardwareNumber","YD5-deliveryRegion","YD5-nmNEName","YD5-rruSiteID","YD5-rruSiteName","YD5-rruScenario","YD5-rruBoxNo","YD5-standingType","YD5-productModel","YD5-contractConnection","YD5-remoteStationType","YD5-standard","YD5-transmissionBandwidth","YD5-nroSubcontractor","YD5-standingType2","MIMO-miMO3DDate","MIMO-miMO3DID","MIMO-miMO3DGoodsQuantity","MIMO-deliveryDate","MIMO-questionClassification",
            "MIMO-planningNumber","MIMO-installationDate","MIMO-completionDate","MIMO-nmNEID","MIMO-openTypeStand","MIMO-openTypeStandTarget","MIMO-baseStationName","MIMO-transmissionBandwidthe4G","MIMO-transmissionAvailable4G","M1800-deliveryDateFDD","M1800-programNumberFDD","M1800-constructionPlanFDD","M1800-questionClassificationFDD","M1800-whetherPlanningFDD","M1800-arrivalDateFDD","M1800-deliveryCompletionDateFFD","M1800-installationFDD","M1800-openedFDD","M1800-nmNEIDFDD","M1800-baseStationNameFDD"};


    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    //获得原标题
    public Map<String,Object> getTitle(){
        HashMap<String, Object> titleMap = new HashMap<String, Object>();
        for (int i=0;i<shu.length;i++){
            titleMap.put(shu[i],yuan[i]);
        }
        return titleMap;
    };
    //获取Work表
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
    //转换日期Excel
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
    //写文档
    public static  void writeExcel(List<ResultMessage> rem,String name) {
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
    //得到唯一标识代码
    public String getUUId(){
        String uuid = UUID.randomUUID().toString();   //转化为String对象
        uuid = uuid.replace("-", ""); //因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        return uuid;
    }
    //判断系统返回存储
    public static String getOS(){
        String save="F:\\test\\";
        String os = System.getProperty("os.name");
        if(!os.toLowerCase().startsWith("win")){
            save="/opt/zhundaSave/";
            System.out.println("There is Linux");
        }
        return save;
    }
    //下载文件
    public static Boolean getDownload(OutputStream os,String fileName){
        String save = getOS();
        File file = new File(save+fileName);
        System.out.println();
        if (file.exists()) {
            System.out.println("文件存在");
            //方大缓存读取
            byte[] buffer = new byte[1000000];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("导出读取完成");
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    bis.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("导出文档不存在");
        return false;
    }
    //得到读取数据
    public Map<String,List<Map<String, Object>>> getExcelResource(String[] strName,Map<String,MultipartFile> mapFile){
        //判断是否传值
        if(strName.length<1 || mapFile.isEmpty()){
            return null;
        }
        HashMap<String, List<Map<String, Object>>> resourcemap = new HashMap<String, List<Map<String, Object>>>();
        //创建读取抽象工厂
        AnalysisAbstractFactory analysisAbstractFactory = FactoryProducer.getFactory("Read");
        //动态读取
        for (String name:strName){
            ReadExcelImp readexcel = analysisAbstractFactory.getExcel(name);
            List<Map<String, Object>> excel = readexcel.getExcel(mapFile.get(name));
            resourcemap.put(name,excel);
        }
        return resourcemap;
    }

    //进行分析
    public Pageto getPageto(Map<String,List<Map<String, Object>>> reeouce){
        //创建页面返回结果
        Pageto pt = new Pageto();
        //完整性计数
        long iCCount = 0;
        //准确性计数
        long iACount = 0;
        //逻辑性计数
        long iLCont = 0;
        //创建返回结果
        List<ResultMessage> reslist = new ArrayList<ResultMessage>();
        //创建区域出错计数
        HashMap<String, HashMap<String, Object>> areacount = new HashMap<String, HashMap<String, Object>>();
        //创建读取抽象工厂
        AnalysisAbstractFactory analysisFactory = FactoryProducer.getFactory("Analysisze");
        //动态实施分析
        if(reeouce.get("SHISHI")!=null){
            AnalysisImp complete = analysisFactory.getAnalysis("Complete");
            AnalysisImp logic = analysisFactory.getAnalysis("Logic");
            AnalysisImp daka =null;
            if (reeouce.get("DAKA")!=null){
                daka=analysisFactory.getAnalysis("DAKA");
            }
            for (Map map:reeouce.get("SHISHI")){
                ResultMessage resultC = complete.getIntegrityAnalysis(map, getTitle());
                if (resultC != null) {
                    iCCount++;
                    reslist.add(resultC);
                }
                ResultMessage resultL = logic.getIntegrityAnalysis(map,getTitle());
                if (resultL != null) {
                    iLCont++;
                    reslist.add(resultL);
                }
                //判断是否打卡开启
                if(reeouce.get("DAKA")!=null){
                    ResultMessage resultD = daka.getIntegrityAnalysis(map,reeouce.get("DAKA"));
                    if (resultD != null) {
                        iACount++;
                        reslist.add(resultD);
                    }
                }
            }
            List<Map<String, Object>> shishi = reeouce.get("SHISHI");
            List<Map<String, Object>> daka1 = reeouce.get("DAKA");
            //调用区域错误计数方法得到数据写入pageto
            areacount = getAreacount(shishi, daka1, getTitle());
        }
        final String uuId = getUUId();
        // 写入流
        writeExcel(reslist, uuId);
        pt.setUId(uuId);
        pt.setResultms(reslist);
        pt.setIACount(iACount);
        pt.setICCount(iCCount);
        pt.setILCount(iLCont);
        //设置区域出错
        pt.setAreacount(areacount);
        return pt;
    }
    //区域错误计数方法
    public HashMap<String, HashMap<String, Object>> getAreacount(List < Map < String, Object >> shishilis, List
        < Map < String, Object >> dakalis, Map < String, Object > title){
        HashMap<String, HashMap<String, Object>> zuizhong = new HashMap<String, HashMap<String, Object>>();//最终得到的数据
        for (Map<String, Object> shishi : shishilis) {
            long wcount = 0;
            long lcount = 0;
            long zcount = 0;
            ResultMessage res = new Complete().getIntegrityAnalysis(shishi, title);//完整性错误返回
            ResultMessage analysis = new Logic().getIntegrityAnalysis(shishi, title);//逻辑返回
            ResultMessage resultMessage = new ClockAnalysis().getIntegrityAnalysis(shishi, dakalis);//打卡的返回
            HashMap<String, Object> zuihashMap = new HashMap<String, Object>();
            String area = (String) shishi.get("YD5-area");//区域
            if (zuizhong.size() == 0) {
                if(dakalis!=null){
                    if (resultMessage != null) {//表示存在一个打卡错误即准确性错误
                        zcount = zcount + 1;
                    }
                }

                if (res != null) {
                    wcount = wcount + res.getXcount();
                }
                if (analysis != null) {
                    lcount = lcount + analysis.getXcount();
                }
                zuihashMap.put("准确性", zcount);
                zuihashMap.put("完整性", wcount);
                zuihashMap.put("逻辑性", lcount);
                zuizhong.put(area, zuihashMap);
                continue;
            }
            //判断当前的区域是否已经存在
            HashMap<String, Object> baocuomap = zuizhong.get(area);
            if (baocuomap == null) {
                if(dakalis!=null){
                    if (resultMessage != null) {//表示存在一个打卡错误即准确性错误
                        zcount = zcount + 1;
                    }
                }

                if (res != null) {
                    wcount = wcount + res.getXcount();
                }
                if (analysis != null) {
                    lcount = lcount + analysis.getXcount();
                }
                zuihashMap.put("准确性", zcount);
                zuihashMap.put("完整性", wcount);
                zuihashMap.put("逻辑性", lcount);
                zuizhong.put(area, zuihashMap);
                continue;
            }
            //表示已经存在
            HashMap<String, Object> baocuo = zuizhong.get(area);
            //表示存在一个打卡错误即准确性错误
            if(dakalis!=null){
                if (resultMessage != null) {
                    zcount = (Long) baocuo.get("准确性") + resultMessage.getXcount();
                } else {
                    zcount = (Long) baocuo.get("准确性");
                }
            }
            if (res != null) {
                wcount = (Long) baocuo.get("完整性") + res.getXcount();
            } else {
                wcount = (Long) baocuo.get("完整性");
            }
            if (analysis != null) {
                lcount = (Long) baocuo.get("逻辑性") + analysis.getXcount();
            } else {
                lcount = (Long) baocuo.get("逻辑性");
            }
            zuihashMap.put("准确性", zcount);
            zuihashMap.put("完整性", wcount);
            zuihashMap.put("逻辑性", lcount);
            zuizhong.put(area, zuihashMap);
            continue;
        }
        return zuizhong;

    }
}
