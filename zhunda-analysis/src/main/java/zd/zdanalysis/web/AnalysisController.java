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
    public String downloadFile(HttpServletRequest request, HttpServletResponse response,@RequestParam("uid") String uid) {
        System.out.println("导出Excel进入");
        String fileName = uid+".xls";// 文件名
        System.out.println("fileName::"+fileName);
        if (fileName != null) {
            //设置文件路径
            File file = new File("F:\\test\\"+fileName);
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                System.out.println("文件存在");
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                //方大缓存读取
                byte[] buffer = new byte[1000000];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {

                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("导出Excel读取完成");
                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        System.out.println("导出Excel文档不存在");
        return "下载失败";
    }
    //@GetMapping("/download")

}
