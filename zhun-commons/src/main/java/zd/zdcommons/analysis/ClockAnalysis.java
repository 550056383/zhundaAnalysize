package zd.zdcommons.analysis;

import zd.zdcommons.pojo.ResultMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        ResultMessage r = new ResultMessage();
        //Listmessge
        ArrayList<String> list = new ArrayList<String>();
        //记录count数即错误数
        long count=0;

        String duid = (String) map.get("YD5-dUID");
        for (Map<String, Object> li : lis) {
            if(duid.equals(li.get("DUID"))){//表示实施中的duid在打卡数据表中存在
                    r.setTError("打卡完成");
                    r.setDUID(duid);
                    r.setDUName(map.get("YD5-dUName").toString());
                    r.setMessge(null);
                    r.setDarea(map.get("YD5-area").toString());
                    r.setXcount(count);
                    return r;
            }
        }
        //表示实施的数据在打卡中不存在
        r.setTError("未打卡");
        r.setDUID(duid);
        r.setDUName(map.get("YD5-dUName").toString());
        r.setMessge(null);
        r.setDarea(map.get("YD5-area").toString());
        count++;
        r.setXcount(count);
        return r;
    }
}
