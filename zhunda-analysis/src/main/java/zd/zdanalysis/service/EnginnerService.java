package zd.zdanalysis.service;

import com.sun.media.sound.SoftTuning;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.pojo.JsonRootBean;
import zd.zdcommons.Utils;
import zd.zdcommons.resouce.ExceclResouce;
import zd.zdcommons.utils.PinYinUtils;
import zd.zdcommons.wirte.WriteNewExcel;

import javax.print.DocFlavor;
import java.io.File;
import java.net.SocketTimeoutException;
import java.util.*;

@Service
public class EnginnerService {
    @Autowired
    private DataService dataService;

    public List<String> getMessage(MultipartFile file) {
        Utils utils = new Utils();
        List<String> title = utils.getLotTitle(file);
        return title;
    }

    public List<String[]> getMove(MultipartFile[] files, String reads) {

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
                primarykey = (String) objects[2];
                name = (String) objects[3];

                Utils utils = new Utils();
                String[]  titles = utils.getExcelResource(file, num, rules, primarykey);
               listTitle.add(titles);

                //汉字转拼音字母
                int length = titles.length;
                String[] str = new String[length];
                for (int j = 0; j < length; j++) {
                    String s1 = PinYinUtils.hanziToPinyin(titles[j], "");
                    str[j] = s1;
                }

                String uuid ="ch" + UUID.randomUUID().toString().substring(0, 8) + "en";

                //创建临时表
                System.out.println("创建临时表");
                dataService.createTables(uuid, str);

                //拿到map数据
                Map<Integer, List<String>> maps = ExceclResouce.getResources();

                //存入数据到临时表
                System.out.println("存入");
                dataService.insetData(uuid, maps);

                //查询
                System.out.println("查询临时表");
                List<Map<String, Object>> maps1 = dataService.selectResult(uuid);
                //写入Excel
                WriteNewExcel writeNewExcel=new WriteNewExcel();
                System.out.println("开始写入数据");

                WriteNewExcel.writeExcecl(titles,maps1,uuid,"");
            }
        }
        return listTitle;
    }
}