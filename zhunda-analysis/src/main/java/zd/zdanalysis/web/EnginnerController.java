package zd.zdanalysis.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.service.EnginnerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enginner")
public class EnginnerController {
    @Autowired
    private EnginnerService enginnerService;
    @PutMapping("title")
    public ResponseEntity<List<String>> getTitle(@RequestParam("file") MultipartFile file){
        System.out.println("file.getName() = " + file.getName());
        return ResponseEntity.ok(enginnerService.getMessage(file));
    };
    @PostMapping()
    public ResponseEntity<String[]> getTest(@RequestParam("file") MultipartFile file,int num,String[] readrules,String primarykey){
        //String[] move = enginnerService.getMove(file, num, readrules, primarykey);
        return ResponseEntity.ok(enginnerService.getMove(file, num, readrules, primarykey));
    };
}
