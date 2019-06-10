package zd.zdcommons.analysis;

import zd.zdcommons.pojo.Message;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.pojo.ResultMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Complete implements AnalysisImp {
    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, Object>> lis) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> resource,Map<String,Object> titleMap) {
        //结果信息的实体类
        ResultMessage resultm = null;
        Message message = null;
        List<Message> meslist=new ArrayList<Message>();
        ArrayList<String> list5g = new ArrayList<String>();

        //记录count数即错误数
        long count=0;
        //获取数据
        //第一个规则
        if(!resource.get("YD5-dUID").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(duidstr,resource,list5g, count,titleMap);//返回的是为空的字段的集合
            list5g = (ArrayList<String>) map.get("error");
            count = Long.parseLong(map.get("count").toString());
        }
        //第二个规则
        if(!resource.get("YD5-RFI-actualEndDate").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(icad,resource,list5g, count,titleMap);//返回的是为空的字段的集合
            list5g = (ArrayList<String>) map.get("error");
            count = Long.parseLong(map.get("count").toString());
        }
        //第三个规则
        if(!resource.get("YD5-AAU-actualEndDate").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(aau,resource,list5g, count,titleMap);//返回的是为空的字段的集合
            list5g = (ArrayList<String>) map.get("error");
            count = Long.parseLong(map.get("count").toString());
        }
        //第四个规则
        if(!resource.get("YD5-receptionDate").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(fiveg,resource,list5g, count,titleMap);//返回的是为空的字段的集合
            list5g = (ArrayList<String>) map.get("error");
            count = Long.parseLong(map.get("count").toString());
        }
        //第五个规则
        if((!resource.get("M1800-openedFDD").toString().isEmpty())&&(!resource.get("YD5-problemClassification").toString().isEmpty())) {
            list5g.add(titleMap.get("YD5-problemClassification").toString());
            count++;
        }
        if((resource.get("M1800-openedFDD").toString().isEmpty())&&(resource.get("YD5-problemClassification").toString().isEmpty())) {
            list5g.add(titleMap.get("YD5-problemClassification").toString());
            count++;
        }
        //添加5g
        if (list5g.size()>0){
            message=get5G(message,list5g);
            meslist.add(message);
        }

        //生成listAnchor
        ArrayList<String> listAnchor = new ArrayList<String>();
        //第六个规则
        if(!resource.get("M1800-installationFDD").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(fddanzhang,resource,listAnchor, count,titleMap);//返回的是为空的字段的集合
            listAnchor = (ArrayList<String>) map.get("error");
            count = Long.parseLong(map.get("count").toString());
        }
        //第七个规则
        if(!resource.get("M1800-openedFDD").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(fddkt,resource,listAnchor, count,titleMap);//返回的是为空的字段的集合
            listAnchor = (ArrayList<String>) map.get("error");
            count = Long.parseLong(map.get("count").toString());
        }

        //第八个规则
        if(!resource.get("M1800-deliveryDateFDD").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(fddjy,resource,listAnchor, count,titleMap);//返回的是为空的字段的集合
            listAnchor = (ArrayList<String>) map.get("error");
            //count = (long) map.get("count");
        }
        //第九个规则
        if((!resource.get("M1800-openedFDD").toString().isEmpty())&&(!resource.get("M1800-questionClassificationFDD").toString().isEmpty())) {
            listAnchor.add(titleMap.get("M1800-questionClassificationFDD").toString());
            count++;
        }
        if((resource.get("M1800-openedFDD").toString().isEmpty())&&(resource.get("M1800-questionClassificationFDD").toString().isEmpty())) {
            listAnchor.add(titleMap.get("M1800-questionClassificationFDD").toString());
            count++;
        }

        //添加F1800
        if(listAnchor.size()>0){
            message = get1800Anchor(message, listAnchor);
            meslist.add(message);
        }


        //生成listMimo
        ArrayList<String> listMimo = new ArrayList<String>();
        //第十个规则
        if(!resource.get("MIMO-installationDate").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(mimoaz,resource,listMimo, count,titleMap);//返回的是为空的字段的集合
            listMimo = (ArrayList<String>) map.get("error");
            count = Long.parseLong(map.get("count").toString());
        }
        //第十一个规则
        if(!resource.get("MIMO-completionDate").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(mimoawc,resource,listMimo, count,titleMap);//返回的是为空的字段的集合
            listMimo = (ArrayList<String>) map.get("error");
            count = Long.parseLong(map.get("count").toString());
        }
        //第十二个规则
        if(!resource.get("MIMO-deliveryDate").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(mimojy,resource,listMimo, count,titleMap);//返回的是为空的字段的集合
            listMimo = (ArrayList<String>) map.get("error");
            count = Long.parseLong(map.get("count").toString());
        }
        //第十三个规则
        if((!resource.get("MIMO-completionDate").toString().isEmpty())&&(!resource.get("M1800-questionClassificationFDD").toString().isEmpty())) {
            listMimo.add(titleMap.get("MIMO-questionClassification").toString());
            count++;
        }
        if((resource.get("MIMO-completionDate").toString().isEmpty())&&(resource.get("M1800-questionClassificationFDD").toString().isEmpty())) {
            listMimo.add(titleMap.get("MIMO-questionClassification").toString());
            count++;
        }
        //添加3D-MIMO
        if (listMimo.size()>0){
            message=get3DMIMO(message,listMimo);
            meslist.add(message);
        }

        //添加最终数据
        if(meslist.size()>0){
            resultm=new ResultMessage();
            resultm.setTError("完整性错误");
            resultm.setDarea(resource.get("YD5-area").toString());
            resultm.setDUID(resource.get("YD5-dUID").toString());
            resultm.setDUName(resource.get("YD5-dUName").toString());
            resultm.setMessge(meslist);
            resultm.setXcount(count);
        }
        return resultm;
    }

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
    private static String shu [] ={"YD5-customerSiteID" ,"YD5-customerSiteName","YD5-dUID	YD5-dUName","YD5-area",
            "YD5-Subcontractor","YD5-supervisionUnit","YD5-SC-baselineStartDate","YD5-SC-baselineEndDate",
            "YD5-SC-planStartDate","YD5-SC-planEndDate","YD5-SC-actualStartDate","YD5-SC-actualEndDate","YD5-SC-owner",
            "YD5-SC-approveState","YD5-SC-deliveryAttachmentRequired","YD5-SC-deliveryAttachmentUploaded",
            "YD5-SC-accumulation","YD5-SC-remarks","YD5-SC-delayReason","YD5-AAU-baselineStartDate","YD5-AAU-baselineEndDate",
            "YD5-AAU-planStartDate","YD5-AAU-planEndDate","YD5-AAU-actualStartDate","YD5-AAU-actualEndDate","YD5-AAU-owner",
            "YD5-AAU-approveState","YD5-AAU-deliveryAttachmentRequired","YD5-AAU-deliveryAttachmentUploaded",
            "YD5-AAU-accumulation","YD5-AAU-remarks","YD5-AAU-delayReason","YD5-residentialBroadband",
            "YD5-transmissionEquipped","YD5-deliveryType","YD5-Nmneid","YD5-nROPO","YD5-rruToneNumber","YD5-rruHardwareNumber",
            "YD5-spectrum","YD5-nroServiceContract","YD5-scenario","YD5-auuArrivalQuantity","YD5-problemClassification",
            "YD5-RFI-baselineEndDate","YD5-RFI-planEndDate","YD5-RFI-actualEndDate","YD5-RFI-owner","YD5-RFI-approveState",
            "YD5-RFI-deliveryAttachmentRequired","YD5-RFI-deliveryAttachmentUploaded","YD5-RFI-accumulation",
            "YD5-RFI-remarks","YD5-RFI-delayReason","YD5-MOS-baselineEndDate","YD5-MOS-planEndDate","YD5-MOS-actualEndDate",
            "YD5-MOS-owner","YD5-MOS-approveState",	"YD5-MOS-deliveryAttachmentRequired",
            "YD5-MOS-deliveryAttachmentUploaded","YD5-MOS-accumulation","YD5-MOS-remarks","YD5-MOS-delayReason",
            "YD5-completionDate",
            "YD5-receptionDate","YD5-IC-baselineEndDate","YD5-IC-planEndDate","YD5-IC-actualEndDate","YD5-IC-owner"
            ,"YD5-IC-approveState","YD5-IC-deliveryAttachmentRequired",	"YD5-IC-deliveryAttachmentUploaded",
            "YD5-IC-accumulation","YD5-IC-remarks",	"YD5-IC-delayReason","YD5-engineeringServiceMode","YD5-planningNumber",
            "YD5-tianmianTransformation",	"YD5-dcFuse",	"YD5-acInduction","YD5-design","YD5-bbuToneNumber","YD5-bbuESN",
            "YD5-bbuSiteID","YD5-bbuSiteName","YD5-bbuScenario","YD5-bbuHardwareNumber",	"YD5-deliveryRegion","YD5-nmNEName",
            "YD5-rruSiteID","YD5-rruScenario","YD5-rruBoxNo",
            "YD5-standingType","YD5-productModel","YD5-contractConnection","YD5-remoteStationType","YD5-standard",
            "YD5-transmissionBandwidth","YD5-nroSubcontractor","YD5-standingType2","MIMO-miMO3DDate","MIMO-miMO3DID",
            "MIMO-miMO3DGoodsQuantity","MIMO-deliveryDate",
            "MIMO-questionClassification","MIMO-planningNumber","MIMO-installationDate","MIMO-completionDate",
            "MIMO-nmNEID","MIMO-openTypeStand","MIMO-openTypeStandTarget","MIMO-baseStationName","MIMO-transmissionBandwidthe4G",
            "MIMO-transmissionAvailable4G",
            "M1800-deliveryDateFDD","M1800-programNumberFDD","M1800-constructionPlanFDD","M1800-questionClassificationFDD",
            "M1800-whetherPlanningFDD",
           "M1800-arrivalDateFDD","M1800-deliveryCompletionDateFFD","M1800-installationFDD",
            "M1800-openedFDD","M1800-nmNEIDFDD","M1800-baseStationNameFDD"
    };
    //第一个规则所需要判断的字段
    private static final String[] duidstr = {"YD5-dUID","YD5-customerSiteID","YD5-customerSiteName",
            "YD5-dUName","YD5-area","YD5-Subcontractor","YD5-spectrum","YD5-nroServiceContract",
            "YD5-scenario","YD5-standingType","YD5-engineeringServiceMode",
            //2019/6/10检查之后对这个字段添加了一个YD5-deliveryRegion和其他几个字段
            "YD5-contractConnection","YD5-standingType2","YD5-standard","YD5-deliveryRegion"};
    //第二个规则所需的字段
    private static final String[] icad = {"YD5-RFI-actualEndDate","YD5-MOS-actualEndDate","YD5-productModel",
            "YD5-remoteStationType","YD5-planningNumber","YD5-tianmianTransformation","YD5-dcFuse",
            "YD5-acInduction","YD5-design","YD5-rruHardwareNumber","YD5-IC-actualEndDate"};
    //第三个规则所需字段
    private static final String[] aau = {"YD5-IC-actualEndDate","YD5-deliveryType",
            "YD5-nROPO","YD5-rruToneNumber","YD5-residentialBroadband","YD5-transmissionEquipped",
            "YD5-bbuESN","YD5-bbuSiteID","YD5-bbuSiteName","YD5-rruScenario","YD5-nmNEName","YD5-rruSiteID",
            "YD5-rruSiteName","YD5-rruScenario","YD5-transmissionBandwidth",
            "YD5-nroSubcontractor","YD5-AAU-actualEndDate"};//2019/6/10检查之后对这个字段添加了D5-AAU-actualEndDate
    //第四个规则所需字段
    private static final String[] fiveg = {"YD5-completionDate","YD5-AAU-actualEndDate","YD5-receptionDate"};
    //第六个规则所需字段
    private static final String[] fddanzhang = {"MIMO-transmissionAvailable4G","M1800-programNumberFDD",
            "M1800-constructionPlanFDD","M1800-whetherPlanningFDD","M1800-nmNEIDFDD","M1800-arrivalDateFDD",
            "M1800-installationFDD"};
    //第七个规则所需字段
    private static final String[] fddkt = {"M1800-installationFDD","M1800-baseStationNameFDD",
            "MIMO-transmissionBandwidthe4G","M1800-openedFDD"};
    //第八个规则所需字段
    private static final String[] fddjy = {"M1800-openedFDD","M1800-deliveryCompletionDateFFD","YD5-receptionDate"};
    //第十个规则所需字段
    private static final String[] mimoaz = {"MIMO-miMO3DID","MIMO-planningNumber",
            "MIMO-nmNEID","MIMO-openTypeStandTarget","MIMO-openTypeStand","MIMO-miMO3DGoodsQuantity","MIMO-installationDate"};
    //第十一个规则
    private static final String[] mimoawc = {"MIMO-installationDate",
            "MIMO-transmissionBandwidthe4G","MIMO-baseStationName","MIMO-completionDate"};
    //第十二个规则
    private static final String[] mimojy = {"MIMO-completionDate","MIMO-deliveryDate","MIMO-miMO3DDate"};
    /**
     * 得到错误信息和出错次数的方法
     *
     * @param s 包含判断的字段的数组
     * @param map 读取的表格数据
     * @param befolist 错误信息的集合
     * @param count 出错的次数
     * @return 返回一个map集合包含错误信息和出错次数
     */
    public static HashMap<String,Object> getCO(String [] s, Map<String, Object> map, List<String> befolist, long count,Map<String,Object> titleMap) {
        List<String> list = befolist;
        for(int i = 0;i<s.length;i++) {
            if(map.get(s[i]).toString().isEmpty()) {
                list.add(titleMap.get(s[i]).toString());
                count++;
            }
        }
        HashMap<String,Object> hashMap = new HashMap<String ,Object>();
        hashMap.put("error", list);
        hashMap.put("count", count);
        return hashMap;
    }

    private final static Message get5G(Message mes, List<String> list){
        mes = new Message();
        mes.setAction("G");
        mes.setTitle("5G");
        mes.setMessages(list);
        return mes;
    }
    private final static Message get3DMIMO(Message mes,List<String> list){
        mes = new Message();
        mes.setAction("D");
        mes.setTitle("3D-MIMO");
        mes.setMessages(list);
        return mes;
    }
    private final static Message get1800Anchor(Message mes,List<String> list){
        mes = new Message();
        mes.setAction("M");
        mes.setTitle("锚点1800M");
        mes.setMessages(list);
        return mes;
    }
}
