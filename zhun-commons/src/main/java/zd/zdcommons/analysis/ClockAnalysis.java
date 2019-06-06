package zd.zdcommons.analysis;

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
        ResultMessage rs = null;
        //Listmessge
        ArrayList<String> list = new ArrayList<String>();
        list.add("未打卡");
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

            rs = new ResultMessage();
            rs.setTError("准确性错误");
            rs.setDUID(duid);
            rs.setDUName(map.get("YD5-dUName").toString());
            rs.setMessge(list);
            rs.setDarea(map.get("YD5-area").toString());
            rs.setXcount(count);
        }
        return rs;//表示打卡成功 返回空值
    }
}
