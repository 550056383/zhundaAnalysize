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
            for (Map.Entry<String, Object> map1 : map.entrySet()) {
                System.out.println("-------" + map1.getValue());
            }
            System.out.println("=========================");
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
          System.out.println("list: " + list.size());
            projectInfoMapper.insetData(table, list);
          System.out.println("--------------------------");
        }

        }

    public void insetData(String uuid, String[] str, Map<Integer, List<String>> maps) {
    }

    //关联设置项表查询
    public List<Map<String, Object>> selectTables(Map<String, Object> maps) {
        String tableAssocia = (String) maps.get("tableAssocia").toString();
        String replace = tableAssocia.replace("{", "").replace("}", "").replace("[", "").replace("]", "").toString();
        String[] split = replace.split(",");
        String[] arr = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            String str = split[i].substring(split[i].indexOf("=") + 1);
            arr[i] = str;
        }
        System.out.println(arr[0] + arr[1] + arr[2] + arr[3]);
        List<Map<String, Object>> list = projectInfoMapper.selectTables("a", "aid", "b", "bid");
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
        return list;
    }

    //输出表字段
    public List<String> selectTableCell(Map<String, Object> maps) {
        String tableCell = (String) maps.get("tableCell").toString();

        List<String> list = projectInfoMapper.selectTableCell("a", "name");
        for (String s : list) {
            System.out.println(s);
        }
        return null;
    }

    //条件设置
    List<String> selectByWriteRules(Map<String, Object> maps) {
        String string = maps.get("writeRules").toString();

        projectInfoMapper.selectByWriteRules("a", "zje", ">", "b", "hk", "10");

        return null;
    }
}
