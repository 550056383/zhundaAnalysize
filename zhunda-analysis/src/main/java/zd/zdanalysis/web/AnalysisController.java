package zd.zdanalysis.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.service.AnalysisService;
import zd.zdcommons.pojo.Pageto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    @Autowired
    private AnalysisService analysisService;

    @PostMapping
    public  ResponseEntity<Pageto> getAnalysis(@RequestParam("shishi") MultipartFile shishi, @RequestParam("daka") MultipartFile daka) {
        Map<String, MultipartFile> map = new HashMap<String, MultipartFile>();
        map.put("SHISHI",shishi);
        map.put("DAKA",daka);
        return ResponseEntity.ok(analysisService.getAnalysis(map));
    };
    @PutMapping
    public  ResponseEntity<Pageto> getAnalysis(@RequestParam("shishi") MultipartFile shishi) {
        Map<String, MultipartFile> map = new HashMap<String, MultipartFile>();
        map.put("SHISHI",shishi);
        return ResponseEntity.ok(analysisService.getAnalysis(map));
    };
    @PutMapping("/title")
    public ResponseEntity<String[]> getTitle(@RequestParam("file") MultipartFile file,String sheetName,int index){
        return ResponseEntity.ok(analysisService.getTitle(file,sheetName,index));
    };
    @GetMapping("/download")
    public ResponseEntity<String> downloadFile( HttpServletResponse response,@RequestParam("uid") String uid) {
        return ResponseEntity.ok(analysisService.getDownload(response,uid));
    }
}
