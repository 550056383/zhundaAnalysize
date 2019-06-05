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
        //为空======
        ResultMessage r = null;
        //Listmessge
        ArrayList<String> list = new ArrayList<String>();
        //记录count数即错误数
        long count=0;

        String duid = (String) map.get("YD5-dUID");
        for (Map<String, Object> li : lis) {
            if(!(duid.equals(li.get("DUID")))){ //表示未打卡
                list.add("未打卡");
                r = new ResultMessage();
                r.setTError("准确性错误");
                r.setDUID(duid);
                r.setDUName(map.get("YD5-dUName").toString());
                r.setMessge(list);
                r.setDarea(map.get("YD5-area").toString());
                count++;
                r.setXcount(count);
                return r;
            }
        }
        return r;//表示打卡成功 返回空值
    }
}
