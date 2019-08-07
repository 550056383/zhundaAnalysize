package zd.zdanalysis.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.service.DataService;
import zd.zdanalysis.service.EnginnerService;
import zd.zdcommons.pojo.ExcelTable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ResponseEntity<List<Map<String, Object>>> getSetup(@RequestBody Map<String, Object> map, HttpServletResponse response, HttpServletRequest request) {
        System.out.println(map.size());
        for (Map.Entry<String,Object> entry:map.entrySet()){
            System.out.println(entry.getKey()+"---------"+entry.getValue());
        }
        List<Map<String, Object>> mapList = enginnerService.getSetup(map, response, request);

        return ResponseEntity.ok(mapList);
    }

    @GetMapping("/download")
    public ResponseEntity<String> downloadFile(HttpServletResponse response, @RequestParam("uid") String uid) {
        return ResponseEntity.ok(enginnerService.getDownloads(response, uid));
    }
}
