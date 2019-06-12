package zd.zdcommons.analysis;


import org.apache.commons.lang3.StringUtils;
import zd.zdcommons.Utils;
import zd.zdcommons.pojo.Message;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.pojo.ResultMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Logic implements AnalysisImp {
    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, Object>> lis) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> resource,Map<String,Object> titleMap) {
        Utils utils = new Utils();
        ResultMessage resultm = null;
        Message message = null;
        List<Message> meslist=new ArrayList<Message>();
        ArrayList<String> list = new ArrayList<String>();
        //记录count数
        long count=0;
        //判断开关
        boolean lot=false;
        //获取校验下标
        int index=-1;
        //获取数据
        System.out.println("进入logic");
        for(int i= logicStr.length-1;i>=0;i--){
            String mes = resource.get(logicStr[i]).toString();
            if (lot){
                if (StringUtils.isBlank(mes)||mes.equals("N/A")){
                    count++;
                    list.add(titleMap.get(logicStr[i])+"为空");
                }
            }
            if(!StringUtils.isBlank(mes)&&!mes.equals("N/A")){
                if(lot==false){
                    lot=true;
                    index=i;
                }
            }
        }
//        for (String str :logicStr){
//            String mes = resource.get(str).toString();
//            if(StringUtils.isBlank(mes)||mes.equals("N/A")){
//                list.add(titleMap.get(str)+"为空");
//            }
//        }
        if(list.size()>0){
            message = get5G(message, list);
            meslist.add(message);
            resultm=getMessge(resultm,resource,meslist,count);
            return resultm;

        }
        if(index<2) {
            return resultm;
        }
        for (int i=index-1;i>0;i--){
            int qian = Integer.parseInt(resource.get(logicStr[i-1]).toString());
            String qianTime = utils.importByExcelForDate(resource.get(logicStr[i-1]).toString());
            int hou = Integer.parseInt(resource.get(logicStr[i]).toString());
            String houTime = utils.importByExcelForDate(resource.get(logicStr[i]).toString());
            if(qian>hou){
                count++;
                list.add(titleMap.get(logicStr[i])+": "+qianTime+"  | 大于 |"+titleMap.get(logicStr[i+1])+"："+houTime);
            }
        }
        if(list.size()>0){
            message = get5G(message, list);
            meslist.add(message);
            resultm=getMessge(resultm,resource,meslist,count);
        }
        return resultm;
    }

    private final static String [] logicStr={"YD5-RFI-actualEndDate","YD5-MOS-actualEndDate","YD5-IC-actualEndDate","YD5-SC-actualEndDate"};
    private final static ResultMessage getMessge(ResultMessage resultm, Map<String, Object> resource, List<Message> mes, long count){
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
