package zd.zdanalysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zd.zdanalysis.mapper.ProjectInfoMapper;

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
    public  List<Map<String,Object>>  selectResult(String table){
        List<Map<String,Object>> list = projectInfoMapper.selectResult(table);
        for (Map<String,Object> map:list){
            }
        return list;
    }


    public int selectResults(String table){
        int i = projectInfoMapper.selectResults(table);
        System.out.println("查询总数:"+i);
        return  i;
    }

    //插入数据到临时表
    public  void insetData(String table,List<List<String>> lists) {
      for (List<String> list:lists){
            projectInfoMapper.insetData(table, list);
        }
        }

    public void insetData(String uuid, String[] str, Map<Integer, List<String>> maps) {
    }
}
