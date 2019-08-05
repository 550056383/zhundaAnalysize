package zd.zdanalysis.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zd.zdanalysis.mapper.ProjectInfoMapper;
import zd.zdcommons.pojo.Majors;
import zd.zdcommons.pojo.Vice;
import zd.zdcommons.pojo.Write;
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
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    //动态创建临时表
   public  void createTables(String table,String[] strTitle){
           //projectInfoMapper.deleteTableByName(table);
           projectInfoMapper.createTables(table, strTitle);


    }

    //查询临时表中的数据
    public  List<Map<String,Object>>  selectResult(String table){
        List<Map<String,Object>> list = projectInfoMapper.selectResult(table);
      /*  for (Map<String,Object> map:list){
            for (Map.Entry<String, Object> map1 : map.entrySet()) {
                System.out.println("-------" + map1.getValue());
            }
            System.out.println("=========================");
        }*/

        return list;
    }


    public int selectResults(String table){
        int i = projectInfoMapper.selectResults(table);
        System.out.println("查询总数:"+i);
        return  i;
    }

    //插入数据到临时表
    public void insetData(String table, List<List<String>> lists) {
      for (List<String> list:lists){
            projectInfoMapper.insetData(table, list);
        }

        //projectInfoMapper

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



    //条件设置
    List<Map<String, Object>> selectByWriteRules(Map<String, Object> maps) {
        //关联设置的条件
        String tableAssocia = (String) maps.get("tableAssocia").toString();
        List<String[]> lists = StringFormat.getString1(tableAssocia);
        //目前只进行两张表的关联
        String[] arrs = lists.get(0);
        String str0 = StringFormat.uuid(arrs[0].trim());
        String str2 = StringFormat.uuid(arrs[2]).trim();
        String s1 = PinYinUtils.hanziToPinyin(arrs[1], "");
        String s3 = PinYinUtils.hanziToPinyin(arrs[3], "");

        //获取输出表字段
        List<Write> writeList = selectTableCell(maps);


        //条件设置的条件
        String writeRules = maps.get("writeRules").toString();
        //num:1代表> 2代表= .........
         int num =0;
        //1.表名相同或table2表为null默认进行表1查询 ,2:查询两表
        int tag = 0;
        //追加条件有无标记,1:有,2:无
        int tags = 0;
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
                 if (arr.length != 3) {
                     majors.setTable2(arr[3]);
                     majors.setField2(arr[4]);
                 }
                 //若arr追加条件没有,arr实际只有5个值
                 if (arr.length == 6) {
                     majors.setValue(arr[5]);
                     tags = 1;
                 } else {
                     tags = 2;
                 }
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

        //以下为附件条件处理,不完善,待用
       /* String[] arr2 = list.get(1);
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
        }*/

        List<Map<String, Object>> resultlist = projectInfoMapper.selectByWriteRules(str0, s1, str2, s3, writeList, tags, majorsList, viceList, num, tag);
        System.out.println("结果有:" + resultlist.size());
      /*  for (Map<String, Object> map1 :resultlist ) {
            System.out.println(map1);
        }*/

        return resultlist;
    }

    //输出表字段,表名,字段经过处理
    public List<Write> selectTableCell(Map<String, Object> maps) {
        List<Write> writeList = new ArrayList<>();
        String tableCell = (String) maps.get("tableCell").toString();
        //字符串转数组
        List<String[]> list = StringFormat.getString2(tableCell);

        for (String[] arr : list) {
            String s1 = StringFormat.uuid(arr[0]);
            String s2 = PinYinUtils.hanziToPinyin(arr[1], "");
            // List<String> list1 = projectInfoMapper.selectTableCell(str0, s1);
            Write write = new Write();
            write.setTable(s1);
            write.setField(s2);
            writeList.add(write);
        }
        return writeList;
    }

    //输出表字段,表名,字段不经过处理
    public List<Write> selectTableCells(Map<String, Object> maps) {
        List<Write> writeList = new ArrayList<>();
        String tableCell = (String) maps.get("tableCell").toString();
        //字符串转数组
        List<String[]> list = StringFormat.getString2(tableCell);

        for (String[] arr : list) {
            String s1 = arr[0];
            String s2 = arr[1];
            Write write = new Write();
            write.setTable(s1);
            write.setField(s2);
            writeList.add(write);
        }
        return writeList;
    }

    //条件设置对表名和字段不进行处理
    public List<Majors> getWriteRules(String[] arr1) {
        List<String[]> newlist = new ArrayList<>();
        List<Majors> majorsList = new ArrayList<>();
        if (!arr1[0].isEmpty()) {
            for (String s : arr1) {
                List<String[]> list1 = StringFormat.stringToArrs(s);
                newlist = list1;
            }

            for (String[] arr : newlist) {
                Majors majors = new Majors();
                majors.setTable1(arr[0]);
                majors.setField1(arr[1]);
                majors.setConditions(arr[2]);
                if (arr.length != 3) {
                    majors.setTable2(arr[3]);
                    majors.setField2(arr[4]);
                }
                if (arr.length == 6) {
                    majors.setValue(arr[5]);
                }
                System.out.println(majors);
                majorsList.add(majors);

            }
        }

        return majorsList;
    }
}
