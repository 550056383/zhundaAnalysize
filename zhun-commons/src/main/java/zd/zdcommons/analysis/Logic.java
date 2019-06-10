package zd.zdcommons.analysis;

import org.apache.commons.lang3.StringUtils;
import zd.zdcommons.pojo.Message;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.pojo.ResultMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static zd.zdcommons.Utils.importByExcelForDate;

public class Logic implements AnalysisImp {
    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, Object>> lis) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> resource,Map<String,Object> titleMap) {
        ResultMessage resultm = null;
        Message message = null;
        List<Message> meslist=new ArrayList<Message>();
        ArrayList<String> list = new ArrayList<String>();
        //记录count数
        long count=0;
        //获取数据
        for (String str :logicStr){
            String mes = resource.get(str).toString();
            if(StringUtils.isBlank(mes)||mes.equals("N/A")){
                list.add(titleMap.get(str)+"为空");
            }
        }
        if(list.size()>0){
            message = get5G(message, list);
            meslist.add(message);
            resultm=getMessge(resultm,resource,meslist,count);
            return resultm;
        }

        for (int i=0;i<logicStr.length-1;i++){

            int qian = Integer.parseInt(resource.get(logicStr[i]).toString());
            String qianTime = importByExcelForDate(resource.get(logicStr[i]).toString());
            int hou = Integer.parseInt(resource.get(logicStr[i+1]).toString());
            String houTime = importByExcelForDate(resource.get(logicStr[i+1]).toString());
            if(qian<hou){
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
