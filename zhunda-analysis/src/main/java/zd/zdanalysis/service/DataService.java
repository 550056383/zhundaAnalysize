package zd.zdanalysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zd.zdanalysis.mapper.ProjectInfoMapper;
import zd.zdanalysis.pojo.ProjectInfo;

import java.util.*;

/**
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/15 16:49
 */
@Service
public class DataService {
    @Autowired
    ProjectInfoMapper projectInfoMapper;
    //动态创建临时表
   public  void createTables(String table,String[] strTitle){
   projectInfoMapper.createTables(table,strTitle);
    }

    //查询临时表中的数据
    public  List<Map<String,Object>>  selectResult(String table){
        List<Map<String,Object>> list = projectInfoMapper.selectResult(table);
        for (Map<String,Object> map:list) {
            if (map == null || "".equals(map)) {
                continue;
            }
        /*   for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "===" + entry.getValue());
            }
            System.out.println("----------查询临时表数据-----------");*/
        }
        return list;
    }


    public int selectResults(String table){
        int i = projectInfoMapper.selectResults(table);
        System.out.println("查询总数:"+i);
        return  i;
    }

    //插入数据到临时表
    public  void insetData(String table,Map<Integer,List<String>> maps) {
        for (Map.Entry<Integer, List<String>> s : maps.entrySet()) {
            List<String> list=s.getValue();

          System.out.println("标题是:" + s.getKey() + "-------" + "结果是:" + s.getValue());
            //projectInfoMapper.insetData(table, list);
        }
            maps.clear();
        }

    public void insetData(String uuid, String[] str, Map<Integer, List<String>> maps) {
    }
}
