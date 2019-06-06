package zd.zdcommons.analysis;

import zd.zdcommons.pojo.Message;
import zd.zdcommons.pojo.ResultMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 打卡分析的类
 */
public class ClockAnalysis {

    /**
     *
     * @param map 实施的一条数据
     * @param lis 打卡的数据表
     */
    public ResultMessage clockfenxi(Map<String, Object> map,List<Map<String, Object>> lis){

        //结果信息的实体类对象
        //为空======
        ResultMessage resultm = null;
        Message message = null;
        List<Message> meslist=new ArrayList<Message>();
        ArrayList<String> list = new ArrayList<String>();
        //记录count数即错误数
        long count=0;

        if(lis.size()<1){
            ResultMessage r = new ResultMessage();
            r.setTError("表型错误");
            return r;

        }
        String duid = (String) map.get("YD5-dUID");
        for (Map<String, Object> li : lis) {
            if((duid.equals(li.get("DUID").toString()))){ //表示打卡
                return null;
            }
            resultm = new ResultMessage();
            resultm.setTError("准确性错误");
            resultm.setDUID(duid);
            resultm.setDUName(map.get("YD5-dUName").toString());
            resultm.setDarea(map.get("YD5-area").toString());
            resultm.setXcount(count);
            //添加错误
            message = get5G(message, list);
            meslist.add(message);
            resultm.setMessge(meslist);
        }
        return resultm;//表示打卡成功 返回空值
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
