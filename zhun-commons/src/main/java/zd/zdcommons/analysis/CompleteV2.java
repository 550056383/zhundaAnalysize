package zd.zdcommons.analysis;

import org.apache.commons.lang3.StringUtils;
import zd.zdcommons.pojo.Message;
import zd.zdcommons.pojo.ResultMessage;
import zd.zdcommons.serviceImp.AnalysisImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static zd.zdcommons.Utils.getInteger;

public class CompleteV2 implements AnalysisImp {
    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, String>> lis) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, Object>> lis, String strTitle) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> resource, Map<String, Object> titleMap) {
        return null;
    }

    //结果信息的实体类
    private ResultMessage resultm = null;
    private Message message = null;
    private List<Message> meslist=new ArrayList<Message>();
    private ArrayList<String> listM5g = new ArrayList<String>();
    private ArrayList<String> listR5g = new ArrayList<String>();
    private ArrayList<String> listAnchor = new ArrayList<String>();
    private ArrayList<String> listMimo = new ArrayList<String>();
    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, String> resource) {
        resultm=null;
        meslist.clear();
        message=null;
        listMimo = new ArrayList<String>();
        listAnchor = new ArrayList<String>();
        listR5g = new ArrayList<String>();
        listM5g = new ArrayList<String>();
        //获取数据
        String activities = resource.get("Activities Flow Name");
        if (activities.contains("5G MBB")){
            //判断宏站
            getM5g(resource);
            //判断瞄点
            getAnchor(resource);
        }
        else if (activities.contains("5G ICS")){
            //判断宏站
            getR5g(resource);
            //判断瞄点
            getAnchor(resource);
        }
        else if(activities.contains("3D-MIMO")){
            getMiMo(resource);
        }
        long r5g=0;
        long m5g=0;
        if(listR5g.size()>0){
            r5g=listR5g.size();
            message = get5GR(message, listR5g);
            meslist.add(message);
        }
        if(listM5g.size()>0){
            m5g=listM5g.size();
            message = get5GR(message,listM5g);
            meslist.add(message);
        }
        long count5g=r5g+m5g;
        long count1800fdd=0;
        long count3d=0;
        if(listAnchor.size()>0){
            count1800fdd=listAnchor.size();
            message = get1800Anchor(message, listAnchor);
            meslist.add(message);
        }
        if(listMimo.size()>0){
            count1800fdd=listMimo.size();
            message = get1800Anchor(message, listMimo);
            meslist.add(message);
        }

        //添加3D-MIMO
        //添加最终数据
        if(meslist.size()>0){
            resultm=new ResultMessage();
            resultm.setTError("完整性错误");
            resultm.setDarea(resource.get("行政区域"));
            resultm.setDUID(resource.get("DU ID"));
            resultm.setDUName(resource.get("DU Name"));
            resultm.setMessge(meslist);
            resultm.setXcount(count5g+count3d+count1800fdd);
            resultm.setCount3d(count3d);
            resultm.setCount5g(count5g);
            resultm.setCount1800fdd(count1800fdd);
        }

        return resultm;
    }
    public void getM5g(Map<String,String> resource){
        //DU开通
        if(StringUtils.isNotBlank(resource.get("DU ID"))){
            getError("DU ID",false,stationM_DU,resource,true,"宏站");
        }
        //宏站安装
        if(StringUtils.isNotBlank(resource.get("Installation-Completed--Actual End Date"))){
            getError("Installation-Completed--Actual End Date",false,stationM_Install,resource,true,"宏站");
        }
        //AAU开通
        if(StringUtils.isNotBlank(resource.get("AAU开通--Actual End Date"))){
            getError("AAU开通--Actual End Date",false,stationM_Open,resource,true,"宏站");
        }
        if(StringUtils.isNotBlank(resource.get("5G 交优接收日期"))){
            getError("5G 交优接收日期",false,stationM_PayDate,resource,true,"宏站");
        }
        if(StringUtils.isNotBlank(resource.get("5G问题分类"))){
            getError("5G问题分类",false,stationM_Problem,resource,false,"宏站");
        }
        if(StringUtils.isBlank(resource.get("5G问题分类"))){
            getError("5G问题分类",true,stationM_Problem,resource,true,"宏站");
        }
    }
    public void getR5g(Map<String,String> resource){
        //DU开通
        if(StringUtils.isNotBlank(resource.get("DU ID"))){
            getError("DU ID",false,stationR_DU,resource,true,"室分");
        }
        //安装
        if(StringUtils.isNotBlank(resource.get("Installation-Completed--Actual End Date"))){
            getError("Installation-Completed--Actual End Date",false,stationR_Install,resource,true,"室分");
        }
        //室分开通
        if(StringUtils.isNotBlank(resource.get("Software Commissioning--Actual End Date"))){
            getError("Software Commissioning--Actual End Date",false,stationR_Open,resource,true,"室分");
        }
        if(StringUtils.isNotBlank(resource.get("5G 交优接收日期"))){
            getError("5G 交优接收日期",false,stationR_PayDate,resource,true,"室分");
        }
        if(StringUtils.isNotBlank(resource.get("5G问题分类"))){
            getError("5G问题分类",false,stationR_Problem,resource,false,"室分");
        }
        if(StringUtils.isBlank(resource.get("5G问题分类"))){
            getError("5G问题分类",true,stationR_Problem,resource,true,"室分");
        }
    }
    public void getAnchor(Map<String,String> resource){
        if(StringUtils.isNotBlank(resource.get("FDD1800安装"))){
            getError("FDD1800安装",false,anchor_Install,resource,true,"瞄点");
        }
        if(StringUtils.isNotBlank(resource.get("FDD1800开通"))){
            getError("FDD1800开通",false,anchor_Open,resource,true,"瞄点");
        }
        if(StringUtils.isNotBlank(resource.get("FDD1800 交优接收日期"))){
            getError("FDD1800 交优接收日期",false,anchor_Receive,resource,true,"瞄点");
        }
        if(StringUtils.isNotBlank(resource.get("锚点FDD1800问题分类"))){
            getError("锚点FDD1800问题分类",false,anchor_Problem,resource,false,"瞄点");
        }
        if(StringUtils.isBlank(resource.get("锚点FDD1800问题分类"))){
            getError("锚点FDD1800问题分类",true,anchor_Problem,resource,true,"瞄点");
        }

    }
    public void getMiMo(Map<String,String> resource){
        //安装
        if(!resource.get("3D-MIMO安装时间").isEmpty()){
            getError("3D-MIMO安装时间",false,mimo_Install,resource,true,"3D");
        }
        //开通
        if(!resource.get("Software Commissioning--Actual End Date").isEmpty()){
            getError("Software Commissioning--Actual End Date",false,mimo_Open,resource,true,"3D");
        }
        //3D-MIMO交优完成日期
        if(!resource.get("3D-MIMO 交优接收日期").isEmpty()){
            getError("3D-MIMO 交优接收日期",false,mimo_Receive,resource,true,"3D");
        }
        //开通
        if(!resource.get("3D-MIMO问题分类").isEmpty()){
            getError("3D-MIMO问题分类",false,mimo_Problem,resource,false,"3D");
        }
        if(resource.get("3D-MIMO问题分类").isEmpty()){
            getError("3D-MIMO问题分类",true,mimo_Problem,resource,true,"3D");
        }
    }

    //第二次修改
    //StationM
    private static final String [] stationM_DU={"Customer Site ID","Customer Site Name","DU Name","行政区域","Subcontractor",
    "频段","NRO服务合同号","场景","站 型","工程服务方式","合同挂接方式","站型","制式","Delivery Region"};
    private static final String[] stationM_Install={"Ready For Installation--Actual End Date","Material On Site--Actual End Date",
    "产品型号","拉远站类型","5G规划编号","天面改造进展","直流空开熔丝","交流引入","是否有设计图纸","RRU硬件数量"};
    private static final String[] stationM_Open={"Installation-Completed--Actual End Date","Delivery Type","NRO PO","RRU软调数量",
    "5G小区带宽","5G传输具备","BBU ESN","BBU Site ID","BBU Site Name","BBU交付场景","NM NE Name","RRU Site ID","RRU Site Name",
    "RRU交付场景","5G传输带宽","NRO Subcontractor"};
    private static final String[] stationM_PayDate={"AAU开通--Actual End Date","5G交优完成日期"};//注意
    private static final String[] stationM_Problem={"AAU开通--Actual End Date"};//双向

    //StationR
    private static final String[] stationR_DU={"Customer Site ID","Customer Site Name","DU Name","行政区域","Subcontractor",
    "NRO服务合同号","场景","站 型","工程服务方式","合同挂接方式","站型","制式","Delivery Region"};
    private static final String[] stationR_Install={"Ready For Installation--Actual End Date","Material On Site--Actual End Date",
    "5G规划编号","直流空开熔丝","交流引入","PRRU规划数量","RHUB规划数量","规划RRU/PRRU数","rHUB类型","RHUB实际安装数",
    "RRU/PRRU实际安装数","规划RHUB数","产品类型","产品型号","是否有设计图纸"};
    private static final String[] stationR_Open={"Installation-Completed--Actual End Date","Delivery Type","NRO PO","RRU软调数量",
    "5G小区带宽","5G传输具备","NM NE Name","5G传输带宽","NRO Subcontractor"};
    private static final String[] stationR_PayDate={"Software Commissioning--Actual End Date","5G交优完成日期"};
    private static final String[] stationR_Problem={"Software Commissioning--Actual End Date"};

    //3D-MIMO
    private static final String[] mimo_Install={"3D-MIMO规划编号","3D-MIMO规划数量","3D-MIMO NM NE ID","3D-MIMO目标站型（设计图纸）",
    "3D-MIMO开通站型","3D-MIMO到货数量"};
    private static final String[] mimo_Open={"3D-MIMO安装时间","4G传输带宽","3D-MIMO网管基站名称","Software Commissioning--Actual End Date"};
    private static final String[] mimo_Receive={"Software Commissioning--Actual End Date","3D-MIMO单验完成日期","3D-MIMO交优完成日期"};
    private static final String[] mimo_Problem={"Software Commissioning--Actual End Date"};

    //锚点FDD1800
    private static final String[] anchor_Install={"4G传输具备","FDD1800规划编号","FDD1800施工计划","FDD1800是否规划","FDD1800 NM NE ID",
    "FDD1800到货日期"};
    private static final String[] anchor_Open={"FDD1800安装","FDD1800网管基站名称","4G传输具备"};
    private static final String[] anchor_Receive={"FDD1800开通","FDD1800交优完成日期"};
    private static final String[] anchor_Problem={"FDD1800开通"};


    //双转规则


    /***
     *
     * @param conditions 当前条件字段
     * @param isState 当前字段状态（true：空,false:非空）
     * @param rules 规则数组
     * @param map 当前信息
     * @param type 当前类型（3D,室分,宏站,瞄点）
     * @param isNull 被判断的状态(true:空,false:非空)
     */
    public void getError(String conditions,Boolean isState,String[] rules,Map<String,String> map,Boolean isNull,String type){
        List<String> list = new ArrayList<String>();
        for(int i = 0;i<rules.length;i++) {
            if(isNull?StringUtils.isBlank(map.get(rules[i])):StringUtils.isNotBlank(map.get(rules[i]))){
                String str="当"+conditions+(isState?"未录入":"已录入")+"时，--"+rules[i]+(isNull?"为空":"不为空");
                list.add(str);
            }
        }
        if (type.equals("宏站")){
            listM5g.addAll(list);
        }else if(type.equals("瞄点")){
            listAnchor.addAll(list);
        }else if (type.equals("3D")){
            listMimo.addAll(list);
        }else if (type.equals("室分")){
            listR5g.addAll(list);
        }
    };
    private final static Message get5GM(Message mes, List<String> list){
        mes = new Message();
        mes.setAction("MBB");
        mes.setTitle("5G宏站");
        mes.setMessages(list);
        return mes;
    }
    private final static Message get5GR(Message mes, List<String> list){
        mes = new Message();
        mes.setAction("ICS");
        mes.setTitle("5G室分");
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
