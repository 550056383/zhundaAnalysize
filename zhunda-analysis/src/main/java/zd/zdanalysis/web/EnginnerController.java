package zd.zdanalysis.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.service.DataService;
import zd.zdanalysis.service.EnginnerService;
import zd.zdcommons.Utils;
import zd.zdcommons.resouce.ExceclResouce;
import zd.zdcommons.utils.PinYinUtils;

import javax.sound.midi.Soundbank;
import java.net.SocketTimeoutException;
import java.util.*;

@RestController
@RequestMapping("/api/enginner")
public class EnginnerController {
    @Autowired
    private EnginnerService enginnerService;
    @Autowired
    private DataService dataService;

    @PutMapping("title")
    public ResponseEntity<List<String>> getTitle(@RequestParam("file") MultipartFile file) {
        System.out.println("file.getName() = " + file.getName());

        return ResponseEntity.ok(enginnerService.getMessage(file));
    }

    ;

    @PostMapping()
    public synchronized  ResponseEntity<String[]> getTest(@RequestParam("file") MultipartFile file, int num, String[] readrules, String primarykey) {

            String[] move = enginnerService.getMove(file, num, readrules, primarykey);

            //汉字转拼音字母
            int length = move.length;
            String[] str=new String[length];
            for(int i=0;i<length;i++){
                String s1 = PinYinUtils.hanziToPinyin(move[i],"");
                str[i]=s1;
               //System.out.println(str[i]);
            }

            String string = UUID.randomUUID().toString();
            String uuid = "ch"+string.substring(0, 8)+"en";
            System.out.println(uuid);
            //创建临时表
            dataService.createTables(uuid,str);

            //拿到map数据
            Map<Integer,List<String>> maps= ExceclResouce.getResources();

            //存入数据到临时表
            dataService.insetData(uuid,str,maps);

            //查询
            List<Map<String, String>> maps1 = dataService.selectResult(uuid);
       return ResponseEntity.ok(move);
    }
}
