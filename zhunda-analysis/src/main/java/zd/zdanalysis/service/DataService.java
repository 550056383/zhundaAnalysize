package zd.zdanalysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zd.zdanalysis.mapper.ProjectInfoMapper;
import zd.zdanalysis.pojo.ProjectInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public  List<Map<String,String>>  selectResult(String table){
        List<Map<String,String>> list = projectInfoMapper.selectResult(table);
        for (Map<String,String> s:list){
            if(s==null|| "".equals(s)){
                continue;
            }
          /*  for (Map.Entry<String,String> entry:s.entrySet()){
                System.out.println(entry.getKey()+"==="+entry.getValue());
            }
           System.out.println("---------------------");*/
        }
        return list;
    }


    public int selectResults(String table){
        int i = projectInfoMapper.selectResults(table);
        System.out.println("查询总数:"+i);
        return  i;
    }

    //插入数据到临时表
    public  void insetData(String table,String[] str,Map<Integer,List<String>> maps){
        for (Map.Entry<Integer,List<String>>  s:maps.entrySet()){
            List<String> list = s.getValue();
            Map<Integer, String> map = new HashMap<>();
            Integer count=0;
            for (String li:list){
                map.put(count,li);
                count++;
            }
          /*  for (Map.Entry<Integer,String> entry:map.entrySet()){
                System.out.println(entry.getKey()+"-----insert--------"+entry.getValue());
            }
            System.out.println("------------------------");*/
            projectInfoMapper.insetData(table,str,map);
        }
        maps.clear();
    }

}
