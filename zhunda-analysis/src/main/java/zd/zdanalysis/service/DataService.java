package zd.zdanalysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zd.zdanalysis.mapper.ProjectInfoMapper;
import zd.zdcommons.utils.PinYinUtils;
import zd.zdcommons.utils.StringFormat;

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
       String s1 = projectInfoMapper.selectTableByName(table);
       if (s1 == null) {
           projectInfoMapper.createTables(table, strTitle);
       } else {
           projectInfoMapper.deleteTableByName(table);
           projectInfoMapper.createTables(table, strTitle);
       }


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
            projectInfoMapper.insetData(table, list);
        }

        }

    public void insetData(String uuid, String[] str, Map<Integer, List<String>> maps) {
    }

    //关联设置项表查询
    public List<Map<String, Object>> selectTables(Map<String, Object> maps) {
        String tableAssocia = (String) maps.get("tableAssocia").toString();
        //字符串转数组
        List<String[]> list = StringFormat.getString1(tableAssocia);
        for (String[] arr : list) {
            String str0 = StringFormat.uuid(arr[0]);
            String str2 = StringFormat.uuid(arr[2]);
            String s1 = PinYinUtils.hanziToPinyin(arr[1], "");
            String s3 = PinYinUtils.hanziToPinyin(arr[3], "");
            List<Map<String, Object>> mapList = projectInfoMapper.selectTables(str0, s1, str2, s3);
            for (Map<String, Object> map : mapList) {
                System.out.println(map);
                System.out.println("关联设置项表查询");
            }
        }
        return null;
    }

    //输出表字段
    public List<String> selectTableCell(Map<String, Object> maps) {
        String tableCell = (String) maps.get("tableCell").toString();
        //字符串转数组
        List<String[]> list = StringFormat.getString2(tableCell);

        for (String[] arr : list) {

            String str0 = StringFormat.uuid(arr[0]);
            System.out.println(str0);
            String s1 = PinYinUtils.hanziToPinyin(arr[1], "");
            List<String> list1 = projectInfoMapper.selectTableCell(str0, s1);
            for (String s : list1) {
                System.out.println(s);
            }
        }

        return null;
    }

    //条件设置
    List<String> selectByWriteRules(Map<String, Object> maps) {
   /*     String writeRules = maps.get("writeRules").toString();
        //字符串转数组
        List<String[]> list= StringFormat.getString3(writeRules);
        for (String[] arr:list){
            for (String a:arr){
                System.out.println(a);
            }
            String str0 =StringFormat.uuid(arr[0]);
            String str3 =StringFormat.uuid(arr[3]);
            System.out.println(str0);
            System.out.println(str3);
            String s1 = PinYinUtils.hanziToPinyin(arr[1], "");
            String s3 = PinYinUtils.hanziToPinyin(arr[3], "");

            String s11 = arr[2];
            String s12 = arr[4];

            List<String> list1 = projectInfoMapper.selectTableCell(str0, s1);
            for (String s : list1) {
                System.out.println(s);
            }
        }*/
        List<Map<String, Object>> list = projectInfoMapper.selectByWriteRules("a", "zje", ">", "b", "hk", "10");
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }

        return null;
    }
}
