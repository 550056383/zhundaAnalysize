package zd.zdcommons.analysis;

import zd.zdcommons.pojo.ErrorCount;
import zd.zdcommons.pojo.Message;
import zd.zdcommons.pojo.ReverseMes;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.pojo.ResultMessage;

import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.util.*;

import static zd.zdcommons.Utils.getFormate_A;
import static zd.zdcommons.Utils.getInteger;

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
        long count5g=0;
        //获取数据
        //第一个规则
        if(!resource.get("YD5-SC-actualEndDate").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(duidstr,resource,list5g, count,titleMap,false,"YD5-SC-actualEndDate",false);//返回的是为空的字段的集合
            list5g = (ArrayList<String>) map.get("error");
            count = Long.parseLong(map.get("count").toString());
        }
        //第二个规则
        if(!resource.get("YD5-IC-actualEndDate").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(icad,resource,list5g, count,titleMap,false,"YD5-IC-actualEndDate",false);//返回的是为空的字段的集合
            list5g = (ArrayList<String>) map.get("error");
        }
        //第三个规则
        if(!resource.get("YD5-AAU-actualEndDate").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(aau,resource,list5g, count,titleMap,false,"YD5-AAU-actualEndDate",false);//返回的是为空的字段的集合
            list5g = (ArrayList<String>) map.get("error");
        }
        //第四个规则
        if(!resource.get("YD5-receptionDate").toString().isEmpty()) {
            HashMap<String,Object> map = getCO(fiveg,resource,list5g, count,titleMap,false,"YD5-receptionDate",false);//返回的是为空的字段的集合
            list5g = (ArrayList<String>) map.get("error");
        }
        //第五个规则
        if((!resource.get("YD5-AAU-actualEndDate").toString().isEmpty())) {
            if(resource.get("YD5-problemClassification").equals("已开通有告警")||(!resource.get("YD5-problemClassification").toString().isEmpty())){
                System.out.println("开通AUU，问题分类的值="+resource.get("YD5-problemClassification").toString()+" ：：：");
                list5g.add(titleMap.get("YD5-problemClassification").toString()+"  --的值不是 空 或 已开通有告警 ，AUU开通已有值");
            }

        }
        if((resource.get("YD5-AAU-actualEndDate").toString().isEmpty())) {
            if(!resource.get("YD5-problemClassification").equals("已开通有告警")||(resource.get("YD5-problemClassification").toString().isEmpty())){
                list5g.add(titleMap.get("YD5-problemClassification").toString()+"  --的值是 空 或 已开通有告警 ，AUU开通也没有值");
            }
            //list5g.add(titleMap.get("YD5-problemClassification").toString());
        }
        //添加5g
        if (list5g.size()>0){
            count5g=list5g.size();//填入构造
            message=get5G(message,list5g);
            meslist.add(message);
        }
        long count1800fdd=0;
        //生成listAnchor
        ArrayList<String> listAnchor = new ArrayList<String>();
        if (resource.get("M1800-whetherPlanningFDD").equals("是")){
            //第六个规则
            if(!resource.get("M1800-installationFDD").toString().isEmpty()) {
                HashMap<String,Object> map = getCO(fddanzhang,resource,listAnchor, count,titleMap,false,"M1800-installationFDD",false);//返回的是为空的字段的集合
                listAnchor = (ArrayList<String>) map.get("error");
            }
            //第七个规则
            if(!resource.get("M1800-openedFDD").toString().isEmpty()) {
                HashMap<String,Object> map = getCO(fddkt,resource,listAnchor, count,titleMap,false,"M1800-openedFDD",false);//返回的是为空的字段的集合
                listAnchor = (ArrayList<String>) map.get("error");
            }

            //第八个规则
            if(!resource.get("M1800-deliveryDateFDD").toString().isEmpty()) {
                HashMap<String,Object> map = getCO(fddjy,resource,listAnchor, count,titleMap,false,"M1800-deliveryDateFDD",false);//返回的是为空的字段的集合
                listAnchor = (ArrayList<String>) map.get("error");
                //count = (long) map.get("count");
            }
            //第九个规则
            if((!resource.get("M1800-openedFDD").toString().isEmpty())) {
                //isEmpty true 判断非空，false判断空
                HashMap<String,Object> map = getCO(anchorType,resource,listAnchor, count,titleMap,false,"M1800-openedFDD",false);//返回的是为空的字段的集合
                listAnchor = (ArrayList<String>) map.get("error");
                HashMap<String,Object> map1 = getCO(anchorRevceType,resource,listAnchor, count,titleMap,true,"M1800-openedFDD",false);//返回的是为空的字段的集合
                listAnchor = (ArrayList<String>) map1.get("error");
            }
            if((resource.get("M1800-openedFDD").toString().isEmpty())) {
                HashMap<String,Object> map = getCO(anchorType1,resource,listAnchor, count,titleMap,true,"M1800-openedFDD",true);//返回的是为空的字段的集合
                listAnchor = (ArrayList<String>) map.get("error");
                HashMap<String,Object> map1 = getCO(anchorRevceType,resource,listAnchor, count,titleMap,false,"M1800-openedFDD",true);//返回的是为空的字段的集合
                listAnchor = (ArrayList<String>) map1.get("error");
            }
        }
        //添加F1800
        if(listAnchor.size()>0){
            count1800fdd=listAnchor.size();
            message = get1800Anchor(message, listAnchor);
            meslist.add(message);
        }
        long count3d=0;
        //生成listMimo
        ArrayList<String> listMimo = new ArrayList<String>();
        if (getInteger(resource.get("MIMO-planningNumber").toString())>0){//有规划量才校验
            //第十个规则
            if(!resource.get("MIMO-installationDate").toString().isEmpty()) {
                HashMap<String,Object> map = getCO(mimoaz,resource,listMimo, count,titleMap,false,"MIMO-installationDate",false);//返回的是为空的字段的集合
                listMimo = (ArrayList<String>) map.get("error");
            }
            //第十一个规则
            if(!resource.get("MIMO-completionDate").toString().isEmpty()) {
                HashMap<String,Object> map = getCO(mimoawc,resource,listMimo, count,titleMap,false,"MIMO-completionDate",false);//返回的是为空的字段的集合
                listMimo = (ArrayList<String>) map.get("error");
            }
            //第十二个规则
            if(!resource.get("MIMO-deliveryDate").toString().isEmpty()) {
                HashMap<String,Object> map = getCO(mimojy,resource,listMimo, count,titleMap,false,"MIMO-deliveryDate",false);//返回的是为空的字段的集合
                listMimo = (ArrayList<String>) map.get("error");
            }
            //第十三个规则
            if((!resource.get("MIMO-completionDate").toString().isEmpty())) {
                //isEmpty true判断非空 ，false判断空
                HashMap<String,Object> map = getCO(mimoType,resource,listMimo, count,titleMap,false,"MIMO-completionDate",false);//返回的是为空的字段的集合
                listMimo = (ArrayList<String>) map.get("error");
                HashMap<String,Object> map1 = getCO(mimoRevceType,resource,listMimo, count,titleMap,true,"MIMO-completionDate",false);//返回的是为空的字段的集合
                listMimo = (ArrayList<String>) map1.get("error");
            }
            if((resource.get("MIMO-completionDate").toString().isEmpty())) {
                HashMap<String,Object> map = getCO(mimoType1,resource,listMimo, count,titleMap,true,"MIMO-completionDate",true);//返回的是为空的字段的集合
                listMimo = (ArrayList<String>) map.get("error");
                HashMap<String,Object> map1 = getCO(mimoRevceType,resource,listMimo, count,titleMap,false,"MIMO-completionDate",true);//返回的是为空的字段的集合
                listMimo = (ArrayList<String>) map1.get("error");
            }
        }

        //添加3D-MIMO
        if (listMimo.size()>0){
            count3d=listMimo.size();
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
            resultm.setXcount(count5g+count3d+count1800fdd);
            resultm.setCount3d(count3d);
            resultm.setCount5g(count5g);
            resultm.setCount1800fdd(count1800fdd);
        }
        return resultm;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, Object>> lis, String strTitle) {
        return null;
    }

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
            "M1800-constructionPlanFDD","M1800-whetherPlanningFDD","M1800-arrivalDateFDD",
            "M1800-installationFDD"};
    //第七个规则所需字段
    private static final String[] fddkt = {"M1800-installationFDD","M1800-baseStationNameFDD",
            "MIMO-transmissionBandwidthe4G","M1800-openedFDD"};
    //第八个规则所需字段
    private static final String[] fddjy = {"M1800-openedFDD","M1800-deliveryCompletionDateFFD","M1800-deliveryDateFDD"};
    //第十个规则所需字段
    private static final String[] mimoaz = {"MIMO-miMO3DID","MIMO-planningNumber",
            "MIMO-miMO3DGoodsQuantity","MIMO-installationDate"};
    //第十一个规则
    private static final String[] mimoawc = {"MIMO-installationDate",
            "MIMO-transmissionBandwidthe4G","MIMO-baseStationName","MIMO-completionDate","MIMO-openTypeStandTarget","MIMO-openTypeStand"};
    //第十二个规则
    private static final String[] mimojy = {"MIMO-completionDate","MIMO-deliveryDate","MIMO-miMO3DDate"};

    //双转规则

    private static final String[] anchorType={"M1800-nmNEIDFDD","M1800-baseStationNameFDD"};
    private static final String[] anchorType1={"M1800-nmNEIDFDD"};
    private static final String[] anchorRevceType={"M1800-questionClassificationFDD"};
    private static final String[] mimoType={"MIMO-nmNEID","MIMO-baseStationName","MIMO-openTypeStandTarget","MIMO-openTypeStand"};
    private static final String[] mimoType1={"MIMO-nmNEID","MIMO-openTypeStandTarget","MIMO-openTypeStand"};
    private static final String[] mimoRevceType={"MIMO-questionClassification"};
    /**
     * 得到错误信息和出错次数的方法
     *
     * @param s 包含判断的字段的数组
     * @param map 读取的表格数据
     * @param befolist 错误信息的集合
     * @param count 出错的次数
     * @return 返回一个map集合包含错误信息和出错次数
     */
    /***
     *
     * @param s 包含判断的字段的数组
     * @param map 读取的表格数据
     * @param befolist 错误信息的集合
     * @param count 记录错误数
     * @param titleMap 中文标题
     * @param isEmpty 判断所需要字段(正确情况是否为空)
     * @param judge 主条件所关联字段
     * @param isnull 主条件是否为空
     * @return
     */
    public static HashMap<String,Object> getCO(String [] s, Map<String, Object> map, List<String> befolist, long count,Map<String,Object> titleMap,boolean isEmpty,String judge,boolean isnull) {
        List<String> list = befolist;
        for(int i = 0;i<s.length;i++) {
            if(isEmpty?(!map.get(s[i]).toString().isEmpty()):(map.get(s[i]).toString().isEmpty())) {
                list.add(titleMap.get(judge)+" -"+(isnull?"未录入":"已录入")+"，---"+titleMap.get(s[i]).toString()+(isEmpty?"不为空":"为空"));
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
