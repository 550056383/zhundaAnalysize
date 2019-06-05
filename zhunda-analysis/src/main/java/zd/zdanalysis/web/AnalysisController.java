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

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    @Autowired
    private AnalysisService analysisService;
    @PostMapping
    public  ResponseEntity<Pageto> getAnalysis(@RequestParam("shishi") MultipartFile shishi, @RequestParam("daka") MultipartFile daka) {
        return ResponseEntity.ok(analysisService.getAnalysis(shishi, daka));
    };
    @GetMapping("/download")
    public ResponseEntity<String> downloadFile( HttpServletResponse response,@RequestParam("uid") String uid) {
        return ResponseEntity.ok(analysisService.getDownload(response,uid));
    }
}
