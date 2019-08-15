package zd.zdanalysis.service;

import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.Utils;
import zd.zdcommons.pojo.ExcelTable;
import zd.zdcommons.pojo.Majors;
import zd.zdcommons.pojo.Write;
import zd.zdcommons.resouce.ExceclResouce;
import zd.zdcommons.utils.PinYinUtils;
import zd.zdcommons.utils.StringFormat;
import zd.zdcommons.wirte.WriteNewExcel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EnginnerService {
    @Autowired
    private DataService dataService;

    public List<String> getMessage(MultipartFile file){
        Utils utils = new Utils();
        List<String> title = utils.getLotTitle(file);
        return title;
    }

    /**
     * 方法描述 : 将解析得到的数据进行处理,进行创建临时表,存,查,写
     *
     * @param files 前台传入的文件数量
     * @param reads 前台传入的Excel表相关信息,如表名,子表名,
     * @return java.util.List<zd.zdcommons.pojo.ExcelTable>
     * @author Jack Chen
     * @date 2019/8/9 14:43
     **/
    public List<ExcelTable>  getMove(MultipartFile[] files, String reads) {


        MultipartFile file = null;
        int num = 1;
        String[] rules = null;
        String primarykey = null;
        String name = null;
        List<String[]> listTitle = new ArrayList<String[]>();
        Utils utils = new Utils();
        List<ExcelTable> listEx = new CopyOnWriteArrayList<ExcelTable>();

        JSONArray jsonArray = JSONArray.fromObject(reads);

        //遍历每一个数组
        for (int i = 0; i < jsonArray.size(); i++) {
            file = files[i];
            JSONArray jsonArrays = jsonArray.getJSONArray(i);
            Object[] objects = jsonArrays.toArray();

            num = Integer.parseInt(String.valueOf(objects[0]));
            rules = objects[1].toString().replace("[", "").replace("]", "").split(",");
            primarykey = (String) objects[2];
            name = (String) objects[3];
            List<ExcelTable> list = utils.getExcelResource(file, num, rules, primarykey);
            ((CopyOnWriteArrayList<ExcelTable>) listEx).addAllAbsent(list);
            System.out.println("开始读取每个表数据进行存储..............");
            long l = System.currentTimeMillis();
            for (ExcelTable s : list) {
                //汉字转拼音字母
                String[] sTitle = s.getTitle();
                int length = sTitle.length;
                String[] str = new String[length];
                for (int j = 0; j < length; j++) {
                    String s1 = PinYinUtils.hanziToPinyin(sTitle[j], "");
                    str[j] = s1;
                    System.out.println(s1);
                }
                String uuid = StringFormat.uuid(s.getSheetName());
                // 创建临时表
                dataService.createTables(uuid, str);
                //存入数据到临时表
                //List<List<String>> resource = s.getResource();
                List<List<String>> resource = s.getResource();
                dataService.insetData(uuid, resource, str);

                //List<Map<String, Object>> maps1 = dataService.selectResult(uuid);
                //System.out.println("查询记录数:" + maps1.size());
                dataService.selectResults(uuid);
                //写入Excel
                      /*  WriteNewExcel writeNewExcel = new WriteNewExcel();
                        WriteNewExcel.writeExcecl(sTitle, maps1, uuid, "");*/
                s.setResource(null);
            }

            list=null;
            ExceclResouce.clear();
            long l2 = System.currentTimeMillis();
            System.out.println("总共插入时间:" + (l2 - l));
        }
        return listEx;
    }

    /**
     *方法描述 : 下载条件分析得到的数据
     * @author Jack Chen
     * @date 2019/8/9 14:38
     * @param response
     * @param uid uid可有可无,用于前台传值,指定读取Excel表的名称
     * @return java.lang.String
     **/
    public String getDownloads(HttpServletResponse response, String uid) {
        Utils utils = new Utils();
        // String fileName = uid + ".xls";
        //暂时将表名固定,条件分析得到的数据生成表的表名
        String fileName = "分析结果表" + ".xlsx";
        try {
            OutputStream outputStream = response.getOutputStream();
            Boolean down = utils.getDownload(outputStream, fileName, response);
            if (down) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                return "文件下载完成";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "文件下载失败";
    }


    /**
     * 方法描述 : 多张表关联,多个输出字段,多个自定义字段,多个主条件,多个附条件,终极版
     *
     * @param maps
     * @param response
     * @param request
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author Jack Chen
     * @date 2019/8/14 11:24
     **/
    public List<Map<String, Object>> getSetup2(Map<String, Object> maps, HttpServletResponse response, HttpServletRequest request) {


        //只提交输出字段那块数据
        List<Write> writeList = dataService.selectTableCell(maps);//获取输出字段集合
        //以关联表第一张表为主的表名
        String table = dataService.getTable(maps);
        StringBuffer stringBuffer = new StringBuffer("");
        if (table == null) {//为null代表单张表,无法进行关联,就取输出字段表名
            for (int i = 0; i < writeList.size(); i++) {
                String table1 = writeList.get(i).getTable();
                if (i == 0) {
                    stringBuffer.append(table1);
                } else {
                    stringBuffer.append(".equals(");
                    stringBuffer.append(table1);
                    stringBuffer.append(")");
                }
            }
            boolean equals = stringBuffer.toString().equals(true);//判定多个输出字段表名是否相等
            if (false == equals) {
                List<List<String>> lists = dataService.selectTableDatas(writeList, false);//多个输出字段表名不相同
                WriteDataField(lists, maps);
            } else {
                List<List<String>> list = dataService.selectTableDatas(writeList, true);//多个输出字段表名相同
                WriteDataField(list, maps);
            }
        } else {

            int count = 1;
            String writeRules = maps.get("writeRules").toString();//取条件设置规则
            List<List<String[]>> list = StringFormat.getString5(writeRules);//转换成多组条件配置

            List<List<Map<String, Object>>> mapLists = dataService.selectByStatement(maps);//返回每组条件配置查询结果集

            for (int i = 0; i < mapLists.size(); i++) {
                WriteData(maps, mapLists.get(i), count, list.get(i));
                count++;
            }
        }
        return null;
    }

    /**
     * 方法描述 : 根据自定义条件将对应分析结果写入Excel
     *
     * @param map 前台传过来的条件集合
     * @param mapList   一个自定义输出字段对应的主副条件集合
     * @return void
     * @author Jack Chen
     * @date 2019/8/13 20:55
     **/

    public void WriteData(Map<String, Object> map, List<Map<String, Object>> mapList, int count, List<String[]> list){
        Object o = null;
        String[] arrs = list.get(0);//取主条件
        String[] split = list.get(2)[0].split(",");
        String zidingyi = split[0];//取自定义字段内容

        List<Majors> writeRules1 = dataService.getWriteRules(arrs);
        for (Majors majors : writeRules1) {
            System.out.println("majors = " + majors);
            String s = majors.getTable1();
            String s1 = majors.getField1();
            String s2 = majors.getConditions();
            String s3 = majors.getTable2();
            String s4 = majors.getField2();
            String s5 = majors.getValue();
            if (s3 == null) {
                String string = s + "表的" + s1 + s2 + s5;
                o = (Object) string;
            } else {
                String string = s + "表的" + s1 + s2 + s3 + "表的" + s4 + "并且" + s2 + s5;
                o = (Object) string;
            }
        }

        Map<String, Object> objectMap = new HashMap<>();
        for (Map<String, Object> map1 : mapList) {
            map1.put(zidingyi, o);
        }


        //得到想要的表头
        List<Write> writeList = dataService.selectTableCells(map);
        String[] arr = new String[writeList.size() + 1];
        for (int i = 0; i < writeList.size(); i++) {
            Write write = writeList.get(i);
            String s1 = write.getField().toString();
            String s2 = write.getTable().toString();
            String str = s2 + " ：" + s1;
            arr[i] = str;
        }
        arr[writeList.size()] = zidingyi;

        //写入Excel
        WriteNewExcel writeNewExcel = new WriteNewExcel();
        System.out.println("开始写入数据..........");
        WriteNewExcel.writeExcecl(arr, mapList, "分析结果表", "分析结果表" + count, count);
    }

    //只提交输出字段表数据写入Excel
    public void WriteDataField(List<List<String>> lists, Map<String, Object> maps) {
        List<Map<String, Object>> listmap = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap<>();
        //得到想要的表头
        List<Write> writeList = dataService.selectTableCells(maps);
        String[] arr = new String[writeList.size()];
        for (int i = 0; i < writeList.size(); i++) {
            Write write = writeList.get(i);
            String s1 = write.getField().toString();
            String s2 = write.getTable().toString();
            String str = s2 + " ：" + s1;
            arr[0] = str;

            for (String ss : lists.get(i)) {//取每个输出字段查询的结果
                map.put(str, ss);
                listmap.add(map);
            }
            System.out.println(listmap.size());
            //写入Excel
            WriteNewExcel writeNewExcel = new WriteNewExcel();
            System.out.println("第" + i + "次开始写入输出字段数据..........");
            WriteNewExcel.writeExcecl(arr, listmap, "输出字段表", "输出字段表" + (i + 1), (i + 1));
            map.clear();
            listmap.clear();

        }

    }
}