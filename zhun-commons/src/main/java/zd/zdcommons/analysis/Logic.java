package zd.zdcommons.analysis;


import org.apache.commons.lang3.StringUtils;
import zd.zdcommons.pojo.Message;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.pojo.ResultMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static zd.zdcommons.Utils.*;

public class Logic implements AnalysisImp {
    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, Object>> lis) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, Object>> lis, String strTitle) {
        return null;
    }
    private Message message = null;//创建对象;

    //记录count数
    private long count=0;
    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> resource,Map<String,Object> titleMap) {
        ResultMessage resultm = null;
        List<Message> meslist=new ArrayList<Message>();//放在外面不会更新新对象，导致指针总是指向最后一条数据
        long count5g=0;
        long count3d=0;
        long count1800fdd=0;
        meslist=getLogic(logic5g,resource,titleMap,"5g",true,meslist);
        count5g=meslist.size();
        if (resource.get("M1800-whetherPlanningFDD").equals("是")){
            meslist=getLogic(logicFDD1800,resource,titleMap,"fd1800",false,meslist);
            count3d=(meslist.size()-count5g);
        }
        if (getInteger(resource.get("MIMO-planningNumber").toString())>0){
            meslist=getLogic(Logic3d,resource,titleMap,"3d",false,meslist);
            count1800fdd=(meslist.size()-count5g-count3d);
        }
        if(meslist.size()>0){
            resultm=getMessge(resource,meslist,meslist.size(),resultm,count5g,count3d,count1800fdd);
        }
        return resultm;
    }
    /**
    * @Author:         chenkun
     *@param
    * @date          2019/6/20  17:55
    */
    public List<Message> getLogic(String[]logicstr,Map<String, Object> resource,Map<String,Object> titleMap,String mesType,boolean custom,List<Message> meslist){
        ArrayList<String> list = new ArrayList<String>();
        //判断空性
        Map<String, Object> aNull = getNull(logicstr, resource, count, list, titleMap, custom, mesType, meslist);
        int index = (Integer) aNull.get("index");
        meslist=(List<Message>)aNull.get("meslist");
        if(list.size()==0){//说明有空集合
            //逻辑性
            meslist=getDateLogic(logicstr, resource, count, list, titleMap, index, custom, mesType, meslist);
        }
        return meslist;
    }
    //判断是否为空
    public Map<String,Object> getNull(String[] logic,Map<String, Object> resource,long count,ArrayList<String> list
            ,Map<String,Object> titleMap,boolean custom,String mesType,List<Message> meslist){
        Map<String, Object> map = new HashMap<String, Object>();
        //计数错误量
        int countx=0;
        //获取校验下标
        int index = 0;
        //判断开关
        boolean lot=false;
        //获取数据
        for(int i= logic.length-1;i>=0;i--){
            String mes = resource.get(logic[i]).toString();
            //开启填入
            if (lot){
                if (StringUtils.isBlank(mes)||mes.equals("N/A")){
                    countx++;
                    list.add(titleMap.get(logic[i])+"为空");
                }
            }
            if(!StringUtils.isBlank(mes)&&!mes.equals("N/A")){
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
        if(list.size()>0){
            if (mesType.equals("5g")){
                message = get5G(message, list);
            }else if (mesType.equals("3d")){
                message = get3DMIMO(message, list);
            }else {
                message = get1800Anchor(message, list);
            }
            meslist.add(message);
        }
        map.put("index",index);
        map.put("meslist",meslist);
        return map;
    }
    //判断是否逻辑正常
    public List<Message> getDateLogic(String[]logicStr,Map<String, Object> resource,long count,ArrayList<String> list
            ,Map<String,Object> titleMap,int index,boolean custom,String mesType,List<Message> meslist){
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
            int qian = getInteger(resource.get(logicStr[first]).toString());
            String qianTime="";
            //System.out.println("数组是："+logicStr[first]+"  first=:"+first);
            //如果是日期类型转换为天数
            if(qian==0){
                qian = importByExcelForBack(resource.get(logicStr[first]).toString());
                qianTime=resource.get(logicStr[first]).toString();
            }else{
                qianTime = importByExcelForDate(resource.get(logicStr[first]).toString());
            }
//            后面
            int hou = getInteger(resource.get(logicStr[after]).toString());
            String houTime ="";
            if(hou==0){
                hou = importByExcelForBack(resource.get(logicStr[after]).toString());
                houTime=resource.get(logicStr[after]).toString();
            }else {
                houTime=importByExcelForDate(resource.get(logicStr[after]).toString());
            }
            //开启自定义设置
            if(custom){
                if (i < 2) {
                     hou = Integer.parseInt(resource.get(logicStr[2]).toString());
                     houTime = importByExcelForDate(resource.get(logicStr[2]).toString());
                    if(qian>hou){
                        list.add(titleMap.get(logicStr[first])+": "+qianTime+"  | 大于 |"+titleMap.get(logicStr[2])+"："+houTime);
                    }
                }else {
                    if(qian>hou){
                        list.add(titleMap.get(logicStr[first])+": "+qianTime+"  | 大于 |"+titleMap.get(logicStr[after])+"："+houTime);
                    }
                }
            }else {
                if(qian>hou){
                    list.add(titleMap.get(logicStr[first])+": "+qianTime+"  | 大于 |"+titleMap.get(logicStr[after])+"："+houTime);
                }
            }
        }
        if(list.size()>0){
            if (mesType.equals("5g")){
                message = get5G(message, list);
            }else if (mesType.equals("3d")){
                message = get3DMIMO(message, list);
            }else {
                message = get1800Anchor(message, list);
            }
            meslist.add(message);
        }
        return meslist;
    }

    private final static String [] logic5g={"YD5-RFI-actualEndDate","YD5-MOS-actualEndDate","YD5-IC-actualEndDate","YD5-SC-actualEndDate"};
    private final static String [] Logic3d={"MIMO-installationDate","MIMO-completionDate","MIMO-miMO3DDate","MIMO-deliveryDate"};
    private final static String [] logicFDD1800={"M1800-arrivalDateFDD","M1800-installationFDD","M1800-openedFDD","M1800-deliveryCompletionDateFFD","M1800-deliveryDateFDD"};


    private final static ResultMessage getMessge(Map<String, Object> resource, List<Message> mes, long count,ResultMessage resultm,long count5g,long count3d,long count1800fdd){
        resultm=new ResultMessage();
        resultm.setTError("逻辑性错误");
        resultm.setDUID(resource.get("YD5-dUID").toString());
        resultm.setDUName(resource.get("YD5-dUName").toString());
        resultm.setDarea(resource.get("YD5-area").toString());
        resultm.setMessge(mes);
        resultm.setXcount(count);
        resultm.setCount1800fdd(count1800fdd);
        resultm.setCount5g(count5g);
        resultm.setCount3d(count3d);
        return resultm;
    };
    private final static Message get5G(Message mes,List<String> list){
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
