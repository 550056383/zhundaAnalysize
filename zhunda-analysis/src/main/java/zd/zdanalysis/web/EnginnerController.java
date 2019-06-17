package zd.zdanalysis.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zd.zdanalysis.service.EnginnerService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class EnginnerController {
    private EnginnerService enginnerService;
    @PutMapping("enginner")
    public ResponseEntity<String> getTitle(@RequestParam("file") MultipartFile file, String sheetName, int index){
        return ResponseEntity.ok(enginnerService.getMessage());
    };
}
