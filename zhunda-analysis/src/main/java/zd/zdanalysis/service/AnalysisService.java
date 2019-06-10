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
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {


    public Pageto getAnalysis(MultipartFile shishi, MultipartFile daka) {
        Utils utils = new Utils();

        //创建分析抽象工厂
        AnalysisFactory analysisFactory = (AnalysisFactory) FactoryProducer.getFactory("Analysisze");
        //创建读取抽象工厂
        ReadFacotory readFactory = (ReadFacotory) FactoryProducer.getFactory("Read");
        ReadIntegrityExcel integrityExcel = (ReadIntegrityExcel) readFactory.getExcel("SHISHI");
        ReadclockExcel readclockExcel = (ReadclockExcel) readFactory.getExcel("DAKA");
        //读取文档
        List<Map<String, Object>> maps = integrityExcel.getExcel(shishi);
        List<Map<String, Object>> dakalist = readclockExcel.getExcel(daka);
        //打开完整性分析
        Complete complete = (Complete) analysisFactory.getAnalysis("Complete");
        //打开逻辑处理
        Logic logic = (Logic) analysisFactory.getAnalysis("Logic");
        //打卡打卡处理
        ClockAnalysis clock =(ClockAnalysis) analysisFactory.getAnalysis("DAKA");
        //ClockAnalysis clock = new ClockAnalysis();

        //获取原来标题
        Map<String, Object> titleMap = utils.getTitle();
        Pageto pt = new Pageto();

        //完整性计数
        long iCCount = 0;
        //准确性计数
        long iACount = 0;
        //逻辑性计数
        long iLCont = 0;

        //信息
        List<ResultMessage> reslist = new ArrayList<ResultMessage>();
        for (Map map : maps) {
            ResultMessage resultC = complete.getIntegrityAnalysis(map, titleMap);
            if (resultC != null) {
                iCCount++;
                reslist.add(resultC);
                //System.out.println(resultC);
            }
            ResultMessage resultL = logic.getIntegrityAnalysis(map, titleMap);
            if (resultL != null) {
                iLCont++;
                reslist.add(resultL);
                //System.out.println(resultL);
            }
            ResultMessage resultD = clock.clockfenxi(map, dakalist);
            if (resultD != null) {
                iACount++;
                reslist.add(resultD);
                //System.out.println(resultL);
            }
        }

        final String uuId = utils.getUUId();
//       写入流
        utils.writeExcel(reslist, uuId);
        pt.setUId(uuId);
        pt.setResultms(reslist);
        pt.setIACount(iACount);
        pt.setICCount(iCCount);
        pt.setILCount(iLCont);
        return pt;
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
