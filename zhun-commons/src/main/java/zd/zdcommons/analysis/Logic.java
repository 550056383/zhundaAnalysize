package zd.zdcommons.analysis;


import org.apache.commons.lang3.StringUtils;
import zd.zdcommons.Utils;
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
    private static ResultMessage resultm = null;
    private Message message = null;
    private List<Message> meslist=new ArrayList<Message>();

    //记录count数
    private long count=0;
    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> resource,Map<String,Object> titleMap) {
        System.out.println("Logic进来的");
        count+=getLogic(logic5g,resource,titleMap,"5g",true);
        if (resource.get("M1800-whetherPlanningFDD").equals("是")){
            count+=getLogic(logicFDD1800,resource,titleMap,"fd1800",false);
        }
        if (getInteger(resource.get("MIMO-planningNumber").toString())>0){
            count+=getLogic(Logic3d,resource,titleMap,"3d",false);
        }
        resultm=getMessge(resource,meslist,count);
        return resultm;
    }
    /**
    * @Author:         chenkun
     *@param
    * @date          2019/6/20  17:55
    */
    public int getLogic(String[]logicstr,Map<String, Object> resource,Map<String,Object> titleMap,String mesType,boolean custom){
        ArrayList<String> list = new ArrayList<String>();
        //判断空性
        Map<String, Integer> aNull = getNull(logicstr, resource, count, list, titleMap, custom, mesType);
        int countx=aNull.get("countx");
        int index = aNull.get("index");
        if(index>0){//没有校验下标，说明没有没有要判定的则返回
            //逻辑性
            countx+=getDateLogic(logicstr, resource, count, list, titleMap, index, custom, mesType);
        }
        return countx;
    }
    //判断是否为空
    public Map<String,Integer> getNull(String[] logic,Map<String, Object> resource,long count,ArrayList<String> list
            ,Map<String,Object> titleMap,boolean custom,String mesType){
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
        HashMap<String, Integer> resuMap = new HashMap<String, Integer>();
        resuMap.put("index",index);
        resuMap.put("countx",countx);
        return resuMap;
    }
    //判断是否逻辑正常
    public int getDateLogic(String[]logicStr,Map<String, Object> resource,long count,ArrayList<String> list
            ,Map<String,Object> titleMap,int index,boolean custom,String mesType){
        int countx=0;
        for (int i=index;i>0;i--){
            int qian = getInteger(resource.get(logicStr[i-1]).toString());
            String qianTime="";
            //如果是日期类型转换为天数
            if(qian==0){
                qian = importByExcelForBack(resource.get(logicStr[i - 1]).toString());
                qianTime=resource.get(logicStr[i - 1]).toString();
            }else{
                qianTime = importByExcelForDate(resource.get(logicStr[i-1]).toString());
            }
//            后面
            int hou = getInteger(resource.get(logicStr[i]).toString());
            String houTime ="";
            if(hou==0){
                hou = importByExcelForBack(resource.get(logicStr[i]).toString());
                houTime=resource.get(logicStr[i]).toString();
            }else {
                houTime=importByExcelForDate(resource.get(logicStr[i]).toString());
            }
            //开启自定义设置
            if(custom){
                if (i < 2) {
                     hou = Integer.parseInt(resource.get(logicStr[2]).toString());
                     houTime = importByExcelForDate(resource.get(logicStr[2]).toString());
                    if(qian>hou){
                        countx++;
                        list.add(titleMap.get(logicStr[i-1])+": "+qianTime+"  | 大于 |"+titleMap.get(logicStr[2])+"："+houTime);
                    }
                }else {
                    if(qian>hou){
                        countx++;
                        list.add(titleMap.get(logicStr[i-1])+": "+qianTime+"  | 大于 |"+titleMap.get(logicStr[i])+"："+houTime);
                    }
                }

            }else {
                if(qian>hou){
                    countx++;
                    list.add(titleMap.get(logicStr[i-1])+": "+qianTime+"  | 大于 |"+titleMap.get(logicStr[i])+"："+houTime);
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
        return countx;
    }

    private final static String [] logic5g={"YD5-RFI-actualEndDate","YD5-MOS-actualEndDate","YD5-IC-actualEndDate","YD5-SC-actualEndDate"};
    private final static String [] Logic3d={"MIMO-installationDate","MIMO-completionDate","MIMO-miMO3DDate","MIMO-deliveryDate"};
    private final static String [] logicFDD1800={"M1800-arrivalDateFDD","M1800-installationFDD","M1800-openedFDD","M1800-deliveryCompletionDateFFD","M1800-deliveryDateFDD"};


    private final static ResultMessage getMessge(Map<String, Object> resource, List<Message> mes, long count){
        resultm=new ResultMessage();
        resultm.setTError("逻辑性错误");
        resultm.setDUID(resource.get("YD5-dUID").toString());
        resultm.setDUName(resource.get("YD5-dUName").toString());
        resultm.setDarea(resource.get("YD5-area").toString());
        resultm.setMessge(mes);
        resultm.setXcount(count);
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
