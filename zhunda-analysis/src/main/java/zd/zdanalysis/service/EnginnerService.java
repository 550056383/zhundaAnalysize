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
                   dataService.insetData(uuid, resource);

                   // List<Map<String, Object>> maps1 = dataService.selectResult(uuid);
                        //写入Excel
                       /* WriteNewExcel writeNewExcel = new WriteNewExcel();
                        WriteNewExcel.writeExcecl(sTitle, maps1, uuid, "");*/
                   s.setResource(null);
                    }
                list=null;
                ExceclResouce.clear();
                }
            return listEx;
        }

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
        System.out.println("writeRules1.size()" + writeRules1.size());
        for (Majors majors : writeRules1) {
            String s = majors.getTable1();
            String s1 = majors.getField1();
            String s2 = majors.getConditions();
            String s3 = majors.getTable2();
            String s4 = majors.getField2();
            String s5 = majors.getValue();
            if (s3.equals("null") && s4.equals("null")) {
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
            System.out.println(map1);
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