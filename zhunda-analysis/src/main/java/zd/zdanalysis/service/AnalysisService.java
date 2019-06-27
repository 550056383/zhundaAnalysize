package zd.zdanalysis.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.Utils;
import zd.zdcommons.pojo.Pageto;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {


    public Pageto getAnalysis(Map<String,MultipartFile> mapfile) {
        Utils utils = new Utils();
        String strName="";
        if(mapfile.get("SHISHI")!=null){
            strName="SHISHI";
        }
        if(mapfile.get("DAKA")!=null){

            strName="SHISHI,DAKA";
        }
        String[] split = strName.split(",");
        //读取数据
        Map<String, List<Map<String, Object>>> excelResource = utils.getExcelResource(split, mapfile);
        //输出数据
        Pageto pageto = utils.getPageto(excelResource);
        return pageto;
    }

    public String getDownload(HttpServletResponse response, String uid) {
        Utils utils = new Utils();
        String fileName = uid + ".xls";
        try {
            OutputStream outputStream = response.getOutputStream();
            Boolean down = utils.getDownload(outputStream, fileName);
            if (down) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                return "文件下载完成";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "文件下载失败";
    }

    public String[] getTitle(MultipartFile file,String sheetName,int cout){
        Utils utils = new Utils();
        String[] excelTitle = utils.getExcelTitle(file, sheetName, cout);
        return excelTitle;
    };
}
