package zd.zdanalysis.service;

import net.sf.json.JSONArray;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.Utils;
import zd.zdcommons.resouce.ExceclResouce;
import zd.zdcommons.utils.PinYinUtils;
import zd.zdcommons.wirte.WriteNewExcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EnginnerService {
    @Autowired
    private DataService dataService;


    public List<String> getMessage(MultipartFile file){
        Utils utils = new Utils();
        List<String> title = utils.getLotTitle(file);
        return title;
    }
    public List<String[]> getMove( MultipartFile[] files,String reads){


        MultipartFile file = null;
        int num = 1;
        String[] rules = null;
        String primarykey = null;
        String name = null;
        List<String[]> listTitle=new ArrayList<>();


        JSONArray jsonArray = JSONArray.fromObject(reads);
        if (jsonArray.size() > 0) {
            //遍历每一个数组
            for (int i = 0; i < jsonArray.size(); i++) {
                file = files[i];
                JSONArray jsonArrays = jsonArray.getJSONArray(i);
                Object[] objects = jsonArrays.toArray();

                num = (int) objects[0];
                rules= objects[1].toString().replace("[", "").replace("]", "").split(",");
                System.out.println("规则:"+rules);
                primarykey = (String) objects[2];
                name = (String) objects[3];

                Utils utils = new Utils();
                List<String[]>  titles = utils.getExcelResource(file, num, rules, primarykey);
                //listTitle.add(titles);
                /*for (String s:titles){
                    System.out.println(s);
                }*/
                for (String[] strings:titles){
                       //汉字转拼音字母
                       int length = strings.length;
                       String[] str = new String[length];
                       for (int j = 0; j < length; j++) {
                           String s1 = PinYinUtils.hanziToPinyin(strings[j], "");
                           str[j] = s1;
                           System.out.println(s1);
                       }
                    String uuid ="ch" + UUID.randomUUID().toString().substring(0, 8) + "en";

                    //创建临时表
                    System.out.println("创建临时表");
                    dataService.createTables(uuid, str);

                    //拿到map数据
                   List< Map<Integer, List<String>>> maps = ExceclResouce.getResources();
                    for (Map<Integer, List<String>> map:maps){

                        //存入数据到临时表
                        System.out.println("存入");
                        dataService.insetData(uuid, map);
                    }


                    //查询
                    System.out.println("查询临时表");
                    //List<Map<String, Object>> maps1 = dataService.selectResult(uuid);
                    //写入Excel
                    WriteNewExcel writeNewExcel=new WriteNewExcel();
                    System.out.println("开始写入数据");

                    //WriteNewExcel.writeExcecl(strings,maps1,uuid,"");
                }

            }
        }
        return listTitle;
    }
}
