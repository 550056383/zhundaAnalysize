package zd.zdanalysis.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.FactoryProducer;
import zd.zdcommons.Utils;
import zd.zdcommons.analysis.ClockAnalysis;
import zd.zdcommons.analysis.Complete;
import zd.zdcommons.analysis.Logic;
import zd.zdcommons.facotry.AnalysisFactory;
import zd.zdcommons.facotry.ReadFacotory;
import zd.zdcommons.pojo.Pageto;
import zd.zdcommons.pojo.ResultMessage;
import zd.zdcommons.read.ReadIntegrityExcel;
import zd.zdcommons.read.ReadclockExcel;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {


    public Pageto getAnalysis(Map<String,MultipartFile> mapfile) {
        Utils utils = new Utils();
        //String strName[]={};
        String strName="";
        if(mapfile.get("SHISHI")!=null){
            //strName=new String[]{"SHISHI"};
            strName="SHISHI";
        }
        if(mapfile.get("DAKA")!=null){
            //strName=new String[]{"SHISHI","DAKA"};
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

    ;
}
