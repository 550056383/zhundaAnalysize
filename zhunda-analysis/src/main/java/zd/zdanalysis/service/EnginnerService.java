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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

                // List<Map<String, Object>> maps1 = dataService.selectResult(uuid);
                //写入Excel
                       /* WriteNewExcel writeNewExcel = new WriteNewExcel();
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
     *方法描述 : 实现两张表进行关联,多个输出字段,一个自定义字段,一个主条件(选填可有可无)的查询
     * @author Jack Chen
     * @date 2019/8/9 14:37
     * @param map
     * @param response
     * @param request
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     **/
    public List<Map<String, Object>> getSetup(Map<String, Object> map, HttpServletResponse response, HttpServletRequest request) {
        Object o = null;

        //条件设置
        List<Map<String, Object>> mapList = dataService.selectByWriteRules(map);

        //得到想要的自定义字段
        String writeRules = map.get("writeRules").toString();
        //字符串转数组
        List<String[]> list = StringFormat.getString4(writeRules);
        String[] arrs = list.get(0);
        String[] split = list.get(2)[0].split(",");
        String zidingyi = split[0];
        List<Majors> writeRules1 = dataService.getWriteRules(arrs);
        for (Majors majors : writeRules1) {
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
        WriteNewExcel.writeExcecl(arr, mapList, "分析结果表", "");
        // getDownload( response);
        return null;
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


    public List<Map<String, Object>> getSetup2(Map<String, Object> map, HttpServletResponse response, HttpServletRequest request) {

        List<Map<String, Object>> mapList = dataService.selectByStatement(map);
        Object o = null;
        //得到想要的自定义字段
        String writeRules = map.get("writeRules").toString();
        //字符串转数组
        List<String[]> list = StringFormat.getString4(writeRules);
        String[] arrs = list.get(0);
        String[] split = list.get(2)[0].split(",");
        String zidingyi = split[0];
        List<Majors> writeRules1 = dataService.getWriteRules(arrs);
        for (Majors majors : writeRules1) {
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
        WriteNewExcel.writeExcecl(arr, mapList, "分析结果表", "");
        // getDownload( response);
        return null;
    }
}