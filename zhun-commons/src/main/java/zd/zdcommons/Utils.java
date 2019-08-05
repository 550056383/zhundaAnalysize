package zd.zdcommons;


import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.abstractFactory.AnalysisAbstractFactory;
import zd.zdcommons.analysis.ClockAnalysis;
import zd.zdcommons.analysis.Complete;
import zd.zdcommons.analysis.Logic;
import zd.zdcommons.excel.ExcelEventParser;
import zd.zdcommons.pojo.*;
import zd.zdcommons.resouce.ExceclResouce;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.serviceImp.ExcelDrivenImp;
import zd.zdcommons.serviceImp.ReadExcelImp;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
                workbook =new XSSFWorkbook(is);
            }
        } catch (Exception e) {
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
            DateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
            currentCellValue = formater.format(dd);
        }
        return currentCellValue;
    }
    public static int importByExcelForBack(String value){
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
        Date date =null;
        int valuedate=0;
        date = getFormate_A(value);
        if (date==null){
           date = getFormate_B(value);
        }
        valuedate = (int) ((date.getTime() / 1000 / 60 / 60 / 24)+25568);
        return valuedate;
    };

    public static Date getFormate_A(String value){
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
        Date date =null;
        try {
            date=f.parse(value);
        } catch (ParseException e) {
    //        System.out.println("yyyy/MM/dd，报错:"+value);
//            e.printStackTrace();
        }
        return date;
    }
    public static Date getFormate_B(String value){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date =null;
        try {
            date=f.parse(value);
        } catch (ParseException e) {
            //System.out.println("yyyy-MM-dd，报错:"+value);
            e.printStackTrace();
        }
        return date;
    }

    public static Date getFormate_C(String value){
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy");
        Date date =null;
        try {
            date=f.parse(value);
        } catch (ParseException e) {
            //System.out.println("yyyy-MM-dd，报错:"+value);
            e.printStackTrace();
        }
        return date;
    }

    //写文档
    public static  void writeExcel(List<ResultMessage> rem,String name) {
        System.out.println("write Excel is comming");
        //第一步，创建一个workbook对应一个excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        XSSFSheet sheet = workbook.createSheet("分析列表");
        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        XSSFRow row = sheet.createRow(0);
        //第四步，创建单元格，设置表头
        XSSFCell cell = row.createCell(0);
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
                    oneData.add(rem.get(i).getDUID());
                    oneData.add(rem.get(i).getDUName());
                    oneData.add(rem.get(i).getDarea());
                    oneData.add(rem.get(i).getTError());
                    oneData.add(mes.get(z).getTitle());
                    oneData.add(messages.get(j));
                    //添加一行
                    XSSFRow row1 = sheet.createRow(cellsum + 1);
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
    public Map<String,List<Map<String, String>>> getExcelResource(String[] strName,Map<String,MultipartFile> mapFile){
        //判断是否传值
        if(strName.length<1 || mapFile.isEmpty()){
            return null;
        }
        HashMap<String, List<Map<String, String>>> resourcemap = new HashMap<String, List<Map<String, String>>>();
        //创建读取抽象工厂
        AnalysisAbstractFactory analysisAbstractFactory = FactoryProducer.getFactory("Read");
        //动态读取
        for (String name:strName){
            ReadExcelImp readexcel = analysisAbstractFactory.getExcel(name);
            List<Map<String, String>> excel = readexcel.getExcel(mapFile.get(name));
            resourcemap.put(name,excel);
        }
        return resourcemap;
    }

    //半自动读取数据
    public synchronized List<ExcelTable>  getExcelResource(MultipartFile file, int num, String[] readrules, String primarykey){
        int total=0;
        String filename = file.getOriginalFilename();
        //创建读取抽象工厂
        AnalysisAbstractFactory analysisAbstractFactory = FactoryProducer.getFactory("ExcelDriver");
        if(filename.endsWith(xls)){
            ExcelDrivenImp xlsDriver = analysisAbstractFactory.getDriver("XLS");
            total=xlsDriver.process(getFileInputStream(file),num,readrules,primarykey,filename);
        }else if (filename.endsWith(xlsx)){
            ExcelDrivenImp xlsxDriver = analysisAbstractFactory.getDriver("XLSX");
            total = xlsxDriver.process(getFileInputStream(file), num, readrules, primarykey,filename);
        }
        //拿取标题头
        return ExceclResouce.getListExcel();
    }

    //进行分析
    public Pageto getPageto(Map<String,List<Map<String, String>>> reeouce){
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
        //创建读取抽象工厂
        AnalysisAbstractFactory analysisFactory = FactoryProducer.getFactory("Analysisze");
        //创建区域出错计数
        List<ReverseMes> areacount=null;
        String strArea[]=null;
        //动态实施分析
        if(reeouce.get("SHISHI")!=null){
            AnalysisImp complete = analysisFactory.getAnalysis("Complete");
            AnalysisImp logic = analysisFactory.getAnalysis("Logic");
            AnalysisImp daka =null;
            if (reeouce.get("DAKA")!=null){
                daka=analysisFactory.getAnalysis("DAKA");
            }
            HashMap<String, TempCount> xun = new HashMap<String, TempCount>();//临时数据属性存放
            for (Map map:reeouce.get("SHISHI")){
                ResultMessage resultC = complete.getIntegrityAnalysis(map);
                if (resultC != null) {
                    iCCount++;
                    reslist.add(resultC);
                    getTempCount(xun,resultC,"--c");
                }
                ResultMessage resultL = logic.getIntegrityAnalysis(map);
                if (resultL != null) {
                    iLCont++;
                    reslist.add(resultL);
                    getTempCount(xun,resultL,"--l");
                }
                //判断是否打卡开启
                if(reeouce.get("DAKA")!=null){
                    ResultMessage resultD = daka.getIntegrityAnalysis(map,reeouce.get("DAKA"));
                    if (resultD != null) {
                        iACount++;
                        reslist.add(resultD);
                        getTempCount(xun,resultD,"--a");
                    }
                }
            }
            Map<String, ReverseMes> inte = new HashMap<String, ReverseMes>();//地区信息的三种状态行信息（"5G","3D-MIMO","瞄点1800FDD"）

            String [] strType={"5G","3D-MIMO","瞄点1800FDD"};//自动数组
            //整合类型
            for(Map.Entry<String,TempCount> entry:xun.entrySet()){
                //先合并 地区
                String stacName_type = entry.getKey();
                int i =stacName_type.indexOf("--");
                //得到地区名字
                String tempname = stacName_type.substring(0, i);
                //（c l a ）完整,逻辑，准确
                String tempType = stacName_type.substring(stacName_type.length()-1,stacName_type.length());
                //每个站有三种类型（"5G","3D-MIMO","瞄点1800FDD"）
                for(String str:strType){
                    if(inte.get(tempname+str)!=null){
                        ReverseMes rev = inte.get(tempname+str);//得到ReverseMes
                        ErrorCount errorCount=rev.getError();//解析错误类型（c l a ）
                        if(str.equals("5G")){
                            errorCount=getError(errorCount,entry.getValue().getCount5g(),tempType);
                        }else if (str.equals("3D-MIMO")){
                            errorCount=getError(errorCount,entry.getValue().getCount3d(),tempType);
                        }else {
                            errorCount=getError(errorCount,entry.getValue().getCount1800fdd(),tempType);
                        }
                        inte.put(tempname+str,rev);
                    }
                    else{
                        ReverseMes rev=null;
                        if(str.equals("5G")){
                            rev = getRevMes(entry.getValue().getCount5g(), tempname, tempType, "5G");
                        }else if (str.equals("3D-MIMO")){
                            rev = getRevMes(entry.getValue().getCount3d(), tempname, tempType, "3D-MIMO");
                        }else {
                            rev = getRevMes(entry.getValue().getCount1800fdd(), tempname, tempType, "瞄点1800FDD");
                        }
                        //System.out.println(tempname);
                        inte.put(tempname+str,rev);
                    }
                }
            }
            System.out.println("inte seize==" +inte.size());
            //创建areacount对象
            areacount=Arrays.asList(new ReverseMes[inte.size()]);
           // ReverseMes rev[]=new ReverseMes[100];
            //地区数组=
             strArea=new String[(inte.size()/3)];
            //地区下标
            int index=0;
            Map<String, Integer> areaIndex = new HashMap<String, Integer>();
            //接收3d_List
            List<ReverseMes> rev3ds=new ArrayList<ReverseMes>();
            //接收fdd_lits
            List<ReverseMes> rev1800fdds=new ArrayList<ReverseMes>();
            //排序
            for(Map.Entry<String,ReverseMes> entry:inte.entrySet()){
                if(entry.getKey().endsWith("5G")){
                    areacount.set(index,entry.getValue());//添加输出集合中(asList 后无法Add和remove)
                    strArea[index]=entry.getValue().getArea();//相机集合填入
                    areaIndex.put(entry.getValue().getArea(),index);//键值对存入下标
                    index++;//更新下标
                }else if(entry.getKey().endsWith("3D-MIMO")){
                    rev3ds.add(entry.getValue());//临时存入
                }else {
                    rev1800fdds.add(entry.getValue());//临时存入
                }
            }
            //向areacount添加3D和1800fdd
            for (ReverseMes rev :rev3ds){
                int i = areaIndex.get(rev.getArea());//得到当前地区下标
                areacount.set((index+i),rev);//得到该存入的下标，并存入
            }
            for (ReverseMes rev:rev1800fdds){
                int i = areaIndex.get(rev.getArea());//得到当前地区下标
                areacount.set((2*index)+i,rev);//得到该存入的下标，并存入
            }
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
        pt.setStrArea(strArea);
        return pt;
    }

    //TempCount 临时拆分
    public void getTempCount(Map<String, TempCount> xun,ResultMessage result,String aType){
        if(xun.get(result.getDarea()+aType)!=null){
            TempCount tempCount= xun.get(result.getDarea()+aType);
            tempCount.setCount3d(tempCount.getCount3d()+result.getCount3d());
            tempCount.setCount5g(tempCount.getCount5g()+result.getCount5g());
            tempCount.setCount1800fdd(tempCount.getCount3d()+result.getCount1800fdd());
            xun.put(result.getDarea()+aType,tempCount);
        }else{
            xun.put(result.getDarea()+aType,new TempCount(result.getCount5g(),result.getCount3d(),result.getCount1800fdd()));
        }
    }

    //得到输出类型
    public ReverseMes getRevMes(Long count,String areaName, String type,String staTepy){
        ReverseMes rev = new ReverseMes();
        if("5G".equals(staTepy)){
            rev.setId("5G");
        }else if("3D-MIMO".equals(staTepy)){
            rev.setId("3D-MIMO");
        }else {
            rev.setId("锚点1800M");
        }
        rev.setArea(areaName);
        ErrorCount error =new ErrorCount();
        error = getError(error, count, type);
        rev.setError(error);
        return rev;
    }

    //得到记错类型
    public ErrorCount getError(ErrorCount error,Long count,String type){
        if(type.equals("c")){
            error.setComplete_err(count);
        }else if(type.equals("l")){
            error.setLogic_err(count);
        }else {
            error.setAccurate_err(count);
        }
        return error;
    }




    //区域错误计数方法
    public HashMap<String, HashMap<String, Object>> getAreacount(List < Map < String, Object >> shishilis, List
        < Map < String, String >> dakalis, Map < String, Object > title){
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



    public String[] getExcelTitle(MultipartFile file,String sheetName,int indexTitle)  {
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

    public List<String> getLotTitle(MultipartFile file){

        long start = System.currentTimeMillis();
        final List<List<String>> table = new LinkedList<List<String>>();
        final List<String>[] fields = new List[]{new CopyOnWriteArrayList<String>()};
        new ExcelEventParser(getFileInputStream(file)).setHandler(new ExcelEventParser.SimpleSheetContentsHandler(){
            private List<String> field;
            @Override
            public void endRow(int rowNum) {
                if(rowNum == 0){
                    // 第一行中文描述忽略
                    fields[0] =row;
                }else if(rowNum == 1){
                    // 第二行字段名
                    field = row;
                    //fields[0] =row;
                }
            }
        }).parse();
        long end = System.currentTimeMillis();
        System.err.println(table.size());
        System.err.println(end - start);
        return fields[0];
    }

    /**
     * 两种文件流解析
     * */
    public InputStream getFileInputStream(MultipartFile file){
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    };
    public InputStream getFileInputStream(File file){
        InputStream InputStream = null;
        try {
            InputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return InputStream;
    };

    /*
     *int强转出错
     */
    public static int getInteger(String s){
        int i =0;
        try {
            i = Integer.parseInt(s);
        }catch (Exception e){
            //e.printStackTrace();如果是日期会一直报错先注释了
        }finally {
            return i;
        }
    }

}
