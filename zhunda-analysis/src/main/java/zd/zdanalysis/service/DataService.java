package zd.zdanalysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zd.zdanalysis.mapper.ProjectInfoMapper;
import zd.zdcommons.pojo.Majors;
import zd.zdcommons.pojo.Vice;
import zd.zdcommons.utils.PinYinUtils;
import zd.zdcommons.utils.StringFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/15 16:49
 */
@Service("dataService")
public class DataService {
    @Autowired
    ProjectInfoMapper projectInfoMapper;
    //动态创建临时表
   public  void createTables(String table,String[] strTitle){
           //projectInfoMapper.deleteTableByName(table);
           projectInfoMapper.createTables(table, strTitle);


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
    List<String>    selectByWriteRules(Map<String, Object> maps) {
        String writeRules = maps.get("writeRules").toString();
         int num =0;
        int tag = 0;
        //字符串转数组
        List<String[]> list= StringFormat.getString4(writeRules);
        List<String[]> newlist=new ArrayList<>();
        List<String[]> newlist2=new ArrayList<>();

        List<Majors> majorsList=new ArrayList<>();
        List<Vice> viceList=new ArrayList<>();

         String[] arr1 = list.get(0);
         if (!arr1[0].isEmpty()){
             for (String s : arr1) {
                 List<String[]> list1 = StringFormat.stringToArr(s);
                 newlist=list1;
             }

             for (String[] arr:newlist){
                 Majors majors = new Majors();
                 majors.setTable1(arr[0]);
                 majors.setField1(arr[1]);
                 majors.setConditions(arr[2]);
                 majors.setTable2(arr[3]);
                 majors.setField2(arr[4]);
                 majors.setValue(arr[5]);
                 System.out.println(majors);
                 //标记便于拼接SQL使用
                 switch (majors.getConditions().trim()) {
                     case "大于":
                         num = 1;
                         break;
                     case "等于":
                         num = 2;
                         break;
                     case "小于":
                         num = 3;
                         break;
                     case "大于等于":
                         num = 4;
                         break;
                     case "小于等于":
                         num = 5;
                         break;
                     case "不等于":
                         num = 6;
                         break;
                 }
                 //标记一组规则中表名是否相同 ,相同标记为1,判断一组规则中只有一个表时,标记为1
                 if (majors.getTable1().equals(majors.getTable2()) || majors.getTable2() == null) {
                     tag = 1;
                 }else {
                     tag = 2;
                 }

                 majorsList.add(majors);

             }
         }



        System.out.println("-----------------------------");

        String[] arr2 = list.get(1);
        System.out.println("arr2.length"+arr2.length);
        if (!arr2[0].isEmpty()){
            for (String s : arr2) {
                List<String[]> list1 = StringFormat.stringToArr(s);
                newlist2=list1;
            }

            for (String[] arr:newlist2){
                Vice vice = new Vice();
                vice.setTable1(arr[0]);
                vice.setField1(arr[1]);
                vice.setConditions(arr[2]);
                vice.setTable2(arr[3]);
                vice.setField2(arr[4]);
                System.out.println(arr.length);
                vice.setValue(arr[5]);
                System.out.println(vice);
                //标记一组规则中表名是否相同
                viceList.add(vice);
            }
        }
        System.out.println(num + "===========" + tag);
        List<Map<String, Object>> resultlist = projectInfoMapper.selectByWriteRules(majorsList, viceList, num, tag);
        System.out.println("结果有:" + resultlist.size());
        for (Map<String, Object> map1 :resultlist ) {
            System.out.println(map1);
        }

        return null;
    }
}
