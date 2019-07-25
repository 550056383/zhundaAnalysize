package zd.zdanalysis.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.service.DataService;
import zd.zdanalysis.service.EnginnerService;
import zd.zdcommons.pojo.ExcelTable;

import java.util.List;
import java.util.Map;

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
    };
@PostMapping()
public   ResponseEntity<List<ExcelTable>> getTest(@RequestParam("files") MultipartFile[] files, String reads){
    //HashMap<String, Object> map = new HashMap<String, Object>();

    return ResponseEntity.ok(enginnerService.getMove(files, reads));
}
    @PostMapping("/cond")
    public ResponseEntity<String> getSetup(@RequestBody Map<String,Object> map){
        System.out.println(map.size());
        for (Map.Entry<String,Object> entry:map.entrySet()){
            System.out.println(entry.getKey()+"---------"+entry.getValue());
        }
        enginnerService.getSetup(map);
        return null;
    }
}
