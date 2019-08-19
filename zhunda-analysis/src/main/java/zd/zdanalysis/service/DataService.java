package zd.zdanalysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zd.zdanalysis.mapper.ProjectInfoMapper;
import zd.zdcommons.pojo.Majors;
import zd.zdcommons.pojo.TableRelevance;
import zd.zdcommons.pojo.Vice;
import zd.zdcommons.pojo.Write;
import zd.zdcommons.resouce.ExceclResouce;
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
       projectInfoMapper.deleteTableByName(table);//如果表存在则删除
           projectInfoMapper.createTables(table, strTitle);
    }

    //查询临时表中的数据
    public  List<Map<String,Object>>  selectResult(String table){
        List<Map<String,Object>> list = projectInfoMapper.selectResult(table);
        /*for (Map<String,Object> map:list){
            for (Map.Entry<String, Object> map1 : map.entrySet()) {
                System.out.println(map1.getKey()+"-------" + map1.getValue());
            }
            System.out.println("=========================");
        }*/
        return list;
    }

    //查询某张表总记录条数
    public int selectResults(String table){
        int i = projectInfoMapper.selectResults(table);
        System.out.println("查询总数:"+i);
        return  i;
    }

    //对批量插入数量进行控制
    public void insetData(String table, List<List<String>> lists, String[] titles) {
        System.out.println("开始每一张表的数据插入..............");
        System.out.println("数据有多少条:" + lists.size());
        long l = System.currentTimeMillis();
        int size = lists.size();
        int unitNum = 2000;
        int startIndex = 0;
        int endIndex = 0;
        while (size > 0) {
            if (size > unitNum) {
                endIndex = startIndex + unitNum;
            } else {
                endIndex = startIndex + size;
            }
            List<List<String>> insertData = lists.subList(startIndex, endIndex);
            insetDatas(table, insertData, titles);
            System.out.println("2000条数据插入完毕...........");
            size = size - unitNum;
            startIndex = endIndex;
        }
        long l1 = System.currentTimeMillis();
        System.out.println("一张表插入时间:" + (l1 - l));
    }

    //一次性批量插入数量,第二版
    public void insetDatas(String table, List<List<String>> lists, String[] titles) {
        System.out.println("每次批量插入数量:" + lists.size());
        int length = titles.length;
        StringBuffer initial = new StringBuffer("(");
        for (List<String> list : lists) {
            for (int j = 0; j < length; j++) {
                String mes = list.get(j) == null ? "" : list.get(j);
                if (j == (length - 1)) {
                    initial.append('"' + mes.trim() + '"' + "),");
                } else {
                    initial.append('"' + mes.trim() + '"' + ",");
                }
            }
            initial = initial.append("(");
        }
        String string = initial.toString();
        string = string.substring(0, string.length() - 2);
        try {
            projectInfoMapper.insetDatas(table, string);
        } catch (RuntimeException e) {
            new RuntimeException("存入数据出现异常!");
            ExceclResouce.clear();
        }


    }

    /**
     * 方法描述 : 多张表关联,多个输出字段,多个自定义输出字段,多个主条件,多个附条件,终极版
     *
     * @param maps
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author Jack Chen
     * @date 2019/8/12 09:43
     **/
    List<List<Map<String, Object>>> selectByStatement(Map<String, Object> maps) {

        List<Write> writeList = selectTableCell(maps);//获取输出字段集合
        //判定输出字段的表名是否相同
        int tag;
        int num = 0;
        String temp = writeList.get(0).getTable();
        for (int i = 0; i < writeList.size(); i++) {
            if (temp.equals(writeList.get(i).getTable())) {
                num++;
            }
        }
        if (num != writeList.size()) {
            tag = 2;//表名不同
        } else {
            tag = 1;//表名相同
        }

        //条件设置的条件配置集合
        String writeRules = maps.get("writeRules").toString();
        List<List<String[]>> listList = StringFormat.getString5(writeRules);

        //添加主/副条件对象以及对应装配的集合
        List<String[]> newlist = new ArrayList<>();
        List<String[]> newlist2 = new ArrayList<>();
        List<Majors> majorsList = new ArrayList<>();
        List<Vice> viceList = new ArrayList<>();

        List<Map<String, Object>> mapList = new ArrayList<>();//接收每组条件配置查询结果
        List<List<Map<String, Object>>> lists = new ArrayList<>();//多个条件配置查询结果的集合

        //得到关联表对象的list集合
        List<TableRelevance> tableRelevances = tableAssocia(maps);

        //以关联表第一张表为主的表名
        String table = getTable(maps);
        if (table == null) {//为null代表单张表,无法进行关联,就取输出字段表名
            table = writeList.get(0).getTable();//
        }

        //遍历多组条件配置
        for (List<String[]> strings : listList) {
            //主条件
            String[] arr1 = strings.get(0);//主条件内容
            if (!arr1[0].isEmpty()) {//避免null异常
                for (String s : arr1) {
                    List<String[]> list1 = StringFormat.stringToArr(s);//多个主条件转换成一个主条件为一个list
                    newlist = list1;
                }

                //遍历主条件集合并将每个主条件进行封装
                for (String[] arr : newlist) {
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
                        majors.setValue(arr[5].trim());
                        majors.setTags("1");//标记追加条件有没有,1:有,2:无
                    } else {
                        majors.setTags("2");
                    }

                    //标记便于拼接SQL使用,每个主条件必定有一个比较条件
                    switch (majors.getConditions().trim()) {
                        case "大于":
                            majors.setConditions("1");
                            break;
                        case "等于":
                            majors.setConditions("2");
                            break;
                        case "小于":
                            majors.setConditions("3");
                            break;
                        case "大于等于":
                            majors.setConditions("4");
                            break;
                        case "小于等于":
                            majors.setConditions("5");
                            break;
                        case "不等于":
                            majors.setConditions("6");
                            break;
                    }
                    //标记一组规则中表名是否相同 ,相同标记为1,判断一组规则中只有一个表时,标记为1
                    if (majors.getTable1().equals(majors.getTable2()) || majors.getTable2() == null) {
                        majors.setTag("1");
                    } else {
                        majors.setTag("2");
                    }
                    System.out.println("majors条件:" + majors);
                    majorsList.add(majors);

                }
            }
            StringFormat.clearList();//清除字符串转数组造成的缓存

            //副件条件处理
            String[] arr2 = strings.get(1);
            if (!arr2[0].isEmpty()) {
                for (String s : arr2) {
                    List<String[]> list1 = StringFormat.stringToArr(s);
                    newlist2 = list1;
                }

                for (String[] arr : newlist2) {
                    Vice vice = new Vice();
                    vice.setTable1(arr[0]);
                    vice.setField1(arr[1]);
                    vice.setConditions(arr[2]);
                    if (arr.length != 3) {
                        vice.setTable2(arr[3]);
                        vice.setField2(arr[4]);
                    }
                    //若arr追加条件没有,arr实际只有5个值
                    if (arr.length == 6) {
                        vice.setValue(arr[5].trim());
                        vice.setTags("1");
                    } else {
                        vice.setTags("2");
                    }
                    //标记便于拼接SQL使用
                    switch (vice.getConditions().trim()) {
                        case "大于":
                            vice.setConditions("1");
                            break;
                        case "等于":
                            vice.setConditions("2");
                            break;
                        case "小于":
                            vice.setConditions("3");
                            break;
                        case "大于等于":
                            vice.setConditions("4");
                            break;
                        case "小于等于":
                            vice.setConditions("5");
                            break;
                        case "不等于":
                            vice.setConditions("6");
                            break;
                    }
                    //标记一组规则中表名是否相同 ,相同标记为1,判断一组规则中只有一个表时,标记为1
                    if (vice.getTable1().equals(vice.getTable2()) || vice.getTable2() == null) {
                        vice.setTag("1");
                    } else {
                        vice.setTag("2");
                    }
                    System.out.println("vice条件:" + vice);
                    viceList.add(vice);
                }
            }
            StringFormat.clearList();

            //查询每组条件配置
            mapList = projectInfoMapper.selectByStatement(table, tag, tableRelevances, writeList, majorsList, viceList);
          /*for (Map<String,Object> map:mapList){
                System.out.println(map);
            }*/
            System.out.println("查询结果数:" + mapList.size());
            lists.add(mapList);
            //清除避免造成数据缓存
            majorsList.clear();
            viceList.clear();
        }
        return lists;
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

    //输出表字段,表名,字段经过处理
    public List<Write> selectTableCell(Map<String, Object> maps) {
        List<Write> writeList = new ArrayList<>();
        String tableCell = (String) maps.get("tableCell").toString();
        //字符串转数组
        List<String[]> list = StringFormat.getString2(tableCell);
        for (int i = 0; i < list.size() - 1; i++) {
            String[] arr = list.get(i);
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

    //输出表字段,表名,字段不经过处理,用于写入
    public List<Write> selectTableCells(Map<String, Object> maps) {
        List<Write> writeList = new ArrayList<>();
        String tableCell = (String) maps.get("tableCell").toString();
        //字符串转数组
        List<String[]> list = StringFormat.getString2(tableCell);
        for (int i = 0; i < list.size() - 1; i++) {
            String s1 = list.get(i)[0];
            String s2 = list.get(i)[1];
            Write write = new Write();
            write.setTable(s1);
            write.setField(s2);
            writeList.add(write);
        }
        return writeList;
    }

    //条件设置对表名和字段不进行处理,用于写入
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
                    majors.setValue(arr[5].trim());
                }
                majorsList.add(majors);
            }
            StringFormat.clearList();
        }

        return majorsList;
    }

    //关联表处理
    public List<TableRelevance> tableAssocia(Map<String, Object> maps) {
        List<TableRelevance> tableRelevances = new ArrayList<>();
        String tableAssocia = (String) maps.get("tableAssocia").toString();
        List<String[]> lists = StringFormat.getString1(tableAssocia);//字符串转数组
        if (lists.size() != 0) {
            String[] arrs = lists.get(0);
            String str0 = StringFormat.uuid(arrs[0].trim());
            //一个list:表名--字段<==>表名--字段
            for (String[] arr : lists) {
                TableRelevance tableRelevance = new TableRelevance();
                tableRelevance.setTable1(StringFormat.uuid(arr[0].trim()));
                tableRelevance.setFields1(PinYinUtils.hanziToPinyin(arr[1], ""));
                tableRelevance.setTable2(StringFormat.uuid(arr[2]).trim());
                tableRelevance.setFields2(PinYinUtils.hanziToPinyin(arr[3], ""));
                tableRelevances.add(tableRelevance);
            }
        }

        return tableRelevances;
    }

    //以哪一张表 为主,进行多表关联
    public String getTable(Map<String, Object> maps) {
        String str0 = null;
        String tableAssocia = (String) maps.get("tableAssocia").toString();
        List<String[]> lists = StringFormat.getString1(tableAssocia); //字符串转数组
        List<TableRelevance> tableRelevances = new ArrayList<>();

        if (lists.size() != 0) {//若lists.size()=0则是上传Excel表是单张表,没有关联表
            String[] arrs = lists.get(0);
            str0 = StringFormat.uuid(arrs[0].trim());
        }
        return str0;
    }

    //查询单张表输出字段
    public List<List<String>> selectTableDatas(List<Write> writeList) {
        List<List<String>> data = new ArrayList<>();
            for (Write w : writeList) {
                List<String> strings = projectInfoMapper.selectTableDada(w);
                data.add(strings);
            }
        return data;
    }

    //查询单张表输出字段
    public List<Map<String, Object>> selectTableDatas2(List<Write> writeList) {
        List<Map<String, Object>> listmap = projectInfoMapper.selectTableDada2(writeList);
        return listmap;
    }
}
