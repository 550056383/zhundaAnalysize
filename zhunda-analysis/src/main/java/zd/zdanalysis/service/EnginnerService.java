package zd.zdanalysis.service;

import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.Utils;
import zd.zdcommons.pojo.ExcelTable;
import zd.zdcommons.resouce.ExceclResouce;
import zd.zdcommons.utils.Md5;
import zd.zdcommons.utils.PinYinUtils;
import zd.zdcommons.wirte.WriteNewExcel;

import java.util.ArrayList;
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
                    }
                   String uuid = "ch" + Md5.md5(s.getSheetName()).substring(0, 8) + "en";
                        // 创建临时表
                        dataService.createTables(uuid, str);

                        //存入数据到临时表
                    List<List<String>> resource = s.getResource();
                        dataService.insetData(uuid,resource);

                        List<Map<String, Object>> maps1 = dataService.selectResult(uuid);
                        //写入Excel
                        WriteNewExcel writeNewExcel = new WriteNewExcel();
                        WriteNewExcel.writeExcecl(sTitle, maps1, uuid, "");
                   s.setResource(null);
                    }
                list=null;
                ExceclResouce.clear();
                }
            return listEx;
        }

    public String getSetup(Map<String, Object> map) {

        //关联设置项表查询
        // List<Map<String, Object>> mapList = dataService.selectTables(map);

        //输出表字段
        List<String> arrr = dataService.selectTableCell(map);

        //条件设置
        List<String> list = dataService.selectByWriteRules(map);
        return null;
    }
    }