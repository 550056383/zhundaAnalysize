package zd.zdanalysis.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jndi.toolkit.url.UrlUtil;
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
import zd.zdcommons.wirte.WriteNewExcel;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;
import java.net.MalformedURLException;
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
    };
@PostMapping()
public   ResponseEntity<List<String[]>> getTest(@RequestParam("files") MultipartFile[] files,String reads){
    //HashMap<String, Object> map = new HashMap<String, Object>();
    List<String[]> move = enginnerService.getMove(files, reads);

    return ResponseEntity.ok(move);
}
    @PostMapping("/cond")
    public ResponseEntity<String> getSetup(@RequestBody Map<String,Object> map){
        System.out.println(map.size());
        return null;
    }
}
