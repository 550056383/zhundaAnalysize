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
import static zd.zdcommons.Utils.importByExcelForBack;

public class LogicV2 implements AnalysisImp {

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, Object>> lis, String strTitle) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> resource,Map<String,Object> titleMap) {
        return null;
    }
    private Message message = null;//创建对象;
    List<Message> meslist=null;
    //记录count数
    private long count=0;

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, String>> lis) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, String> resource) {
        ResultMessage resultm = null;
        meslist=new ArrayList<Message>();//放在外面不会更新新对象，导致指针总是指向最后一条数据
        long count5g=0;
        long count3d=0;
        long count1800fdd=0;
        //获取数据
        String activities = resource.get("Activities Flow Name");
        if (activities.contains("5G MBB")){
            meslist=getLogic(logicM5g,resource,"5gM",true,meslist);
            count5g=meslist.size();
            if (resource.get("FDD1800是否规划").equals("是")){
                meslist=getLogic(logicAnchor,resource,"fd1800",false,meslist);
                count1800fdd=(meslist.size()-count5g);
            }
        }
        else if (activities.contains("5G ICS")){
            meslist=getLogic(logicR5g,resource,"5gR",true,meslist);
            count5g=meslist.size();
            if (resource.get("FDD1800是否规划").equals("是")){
                meslist=getLogic(logicAnchor,resource,"fd1800",false,meslist);
                count1800fdd=(meslist.size()-count5g);
            }
        }
        else if(activities.contains("3D-MIMO")){
            if (getInteger(resource.get("3D-MIMO规划数量"))>0){
                meslist=getLogic(logicMimo,resource,"3d",false,meslist);
                count3d=meslist.size();
            }
        }
        if(meslist.size()>0){
            resultm=new ResultMessage();
            resultm.setTError("逻辑性错误");
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

    /**
    * @Author:         chenkun
     *@param
    * @date          2019/6/20  17:55
    */
    public List<Message> getLogic(String[]logicstr,Map<String, String> resource,String mesType,boolean custom,List<Message> meslist){
        ArrayList<String> list = new ArrayList<String>();
        //判断空性
        Map<String, Object> aNull = getNull(logicstr, resource, list, custom, mesType, meslist);
        int index = (Integer) aNull.get("index");
        meslist=(List<Message>)aNull.get("meslist");
        if(list.size()==0){//说明有空集合
            //逻辑性
            meslist=getDateLogic(logicstr, resource, list, index, custom, mesType, meslist);
        }
        return meslist;
    }

    private static int getDateNum(Map<String, String> resource, String titleName, List list) {
        int i = importByExcelForBack(resource.get(titleName));
        if (i == 0) {
            list.add(resource.get("DU ID") + " 的 " + titleName + "存在异常，内容：" + resource.get(titleName));
        }
        return i;
    }

    //判断是否为空
    public Map<String,Object> getNull(String[] logic,Map<String, String> resource,ArrayList<String> list,
                                      boolean custom,String mesType,List<Message> meslist){
        Map<String, Object> map = new HashMap<String, Object>();
        //计数错误量
        int countx=0;
        //获取校验下标
        int index = 0;
        //判断开关
        boolean lot=false;
        //获取数据
        for(int i= logic.length-1;i>=0;i--){
            String mes = resource.get(logic[i]);
            //开启填入
            if (lot){
                if (StringUtils.isBlank(mes)){
                    countx++;
                    list.add((logic[i])+"为空");
                }
            }
            if(!StringUtils.isBlank(mes)){
                //判断自定义、、时间靠后有值时候开启判断
                if(custom){
                    if(lot==false&&i>1){
                        lot=true;
                        index=i;
                    }
                }else{
                    if(lot==false){
                        lot=true;
                        index=i;
                    }
                }
            }
        }

        addMeslist(list,mesType);
        map.put("index",index);
        map.put("meslist",meslist);
        return map;
    }

    private final static String[] logicM5g = {"Ready For Installation--Actual End Date", "Material On Site--Actual End Date",
            "Installation-Completed--Actual End Date", "AAU开通--Actual End Date",
            "Software Commissioning--Actual End Date"};
    private final static String[] logicR5g = {"Ready For Installation--Actual End Date", "Material On Site--Actual End Date",
            "Installation-Completed--Actual End Date", "Software Commissioning--Actual End Date"};
    private final static String[] logicAnchor = {"FDD1800到货日期", "FDD1800安装", "FDD1800开通", "FDD1800交优完成日期", "FDD1800 交优接收日期", "FDD1800单验完成"};
    private final static String[] logicMimo = {"3D-MIMO安装时间", "Software Commissioning--Actual End Date", "3D-MIMO交优完成日期",
            "3D-MIMO 交优接收日期", "3D-MIMO单验完成日期"};
    private final static String[] logic5g = {"Ready For Installation--Actual End Date", "Material On Site--Actual End Date", "Installation-Completed--Actual End Date", "Software Commissioning--Actual End Date"};
    private final static String[] Logic3d = {"MIMO-installationDate", "MIMO-completionDate", "MIMO-miMO3DDate", "MIMO-deliveryDate"};
    private final static String[] logicFDD1800 = {"M1800-arrivalDateFDD", "M1800-installationFDD", "M1800-openedFDD", "M1800-deliveryCompletionDateFFD", "M1800-deliveryDateFDD"};

    //判断是否逻辑正常
    public List<Message> getDateLogic(String[]logicStr,Map<String, String> resource,ArrayList<String> list
            ,int index,boolean custom,String mesType,List<Message> meslist){
        for (int i=index;i>0;i--){
            int first=i-1;
            int after=i;
            if(resource.get(logicStr[after]).equals("现网已具备"))continue;//如果现网已具备 则无法比较 后时间
            while (resource.get(logicStr[first]).equals("现网已具备")){
                first--;//向前推时间
                if (first<0)return meslist;
            }
            if(resource.get(logicStr[after]).equals("NA"))continue;//如果现网已具备 则无法比较 后时间
            while (resource.get(logicStr[first]).equals("NA")){
                first--;//向前推时间
                if (first<0)return meslist;
            }
            if(resource.get(logicStr[after]).equals("N/A"))continue;//如果现网已具备 则无法比较 后时间
            while (resource.get(logicStr[first]).equals("N/A")){
                first--;//向前推时间
                if (first<0)return meslist;
            }
            if(resource.get(logicStr[after]).equals("是"))continue;//如果现网已具备 则无法比较 后时间
            while (resource.get(logicStr[first]).equals("是")){
                first--;//向前推时间
                if (first<0)return meslist;
            }


            int qian = getDateNum(resource, logicStr[first], list);
            if (qian == 0) continue;
            String qianTime = resource.get(logicStr[first]);
            ;
            //如果是日期类型转换为天数
//            后面
            int hou = getDateNum(resource, logicStr[after], list);
            if (hou == 0) continue;
            String houTime = resource.get(logicStr[after]);
            //开启自定义设置
            if(custom){
                if (i < 2) {
                    hou = getDateNum(resource, logicStr[2], list);
                    if (hou == 0) continue;
                    houTime = resource.get(logicStr[after]);
                    if(qian>hou){
                        list.add(logicStr[first]+": "+qianTime+"  | 大于 |"+logicStr[2]+"："+houTime);
                    }
                }else {
                    if(qian>hou){
                        list.add(logicStr[first]+": "+qianTime+"  | 大于 |"+logicStr[after]+"："+houTime);
                    }
                }
            }else {
                if(qian>hou){
                    list.add(logicStr[first]+": "+qianTime+"  | 大于 |"+logicStr[after]+"："+houTime);
                }
            }
        }
        //添加
        addMeslist(list,mesType);
        return meslist;
    }
    private final  void addMeslist(List list,String mesType){
        if(list.size()>0){
            if (mesType.equals("5gM")){
                message = get5GM(message, list);
            }else if(mesType.equals("5gR")){
                message = get5GR(message, list);
            }else if (mesType.equals("3d")){
                message = get3DMIMO(message, list);
            }else {
                message = get1800Anchor(message, list);
            }
            meslist.add(message);
        }
    }
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
