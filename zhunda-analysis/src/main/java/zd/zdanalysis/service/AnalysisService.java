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

        if(daka==null){
            //表示打卡为空 调用实施文件的处理方法
            Pageto pageto = manageShishi(shishi);
            return pageto;
        }
        //否则表示打卡文件也存在 调用打卡方法
        Pageto pageto = manageDaka(shishi, daka);
        return pageto;
    }


    /**
     * 处理打卡和实施的方法 此时打卡和实施文档都上传了
     * @param shishi
     * @param daka
     * @return
     */
    public Pageto manageDaka(MultipartFile shishi, MultipartFile daka) {
        Utils utils = new Utils();
        //创建分析抽象工厂
        AnalysisFactory analysisFactory = (AnalysisFactory) FactoryProducer.getFactory("Analysisze");
        //打开完整性分析
        Complete complete = (Complete) analysisFactory.getAnalysis("Complete");
        //打开逻辑处理
        Logic logic = (Logic) analysisFactory.getAnalysis("Logic");
        //创建读取抽象工厂
        ReadFacotory readFactory = (ReadFacotory) FactoryProducer.getFactory("Read");
        ReadIntegrityExcel integrityExcel = (ReadIntegrityExcel) readFactory.getExcel("SHISHI");
        List<Map<String, Object>> maps = integrityExcel.getExcel(shishi);
        //针对打卡 2019/6/5 14.40 修改添加的
        ReadclockExcel ddsd = new ReadclockExcel();
        List<Map<String, Object>> dakamaps = ddsd.getExcel(daka);//打卡
        ClockAnalysis clockAnalysis = new ClockAnalysis();//打卡的分析
        //
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
        for (Map t : maps) {
            ResultMessage resultC = complete.getIntegrityAnalysis(t, titleMap);
            //打卡得到的ResultMessage 2019/6/5 14.40添加的代码
            //ArrayList<ResultMessage> clockfenxi = clockAnalysis.clockfenxi(t, dakamaps);
            //
            ResultMessage resultD = clockAnalysis.clockfenxi(t, dakamaps);
            if (resultD != null) {//表示存在没有打卡信息
                    iACount++;
                    reslist.add(resultD);//添加打卡的相关
            }
            //
            if (resultC != null) {
                iCCount++;
                reslist.add(resultC);
                //System.out.println(resultC);
            }
            ResultMessage resultL = logic.getIntegrityAnalysis(t, titleMap);
            if (resultL != null) {
                iLCont++;
                reslist.add(resultL);
                //System.out.println(resultL);
            }
        }
        //final String uuId = "dsdsd";
        final String uuId = utils.getUUId();
//       写入流
        System.out.println("得到的集合" + reslist + "uuid" + uuId);
        utils.writeExcel(reslist, uuId);
        pt.setUId(uuId);
        pt.setResultms(reslist);
        pt.setIACount(iACount);
        pt.setICCount(iCCount);
        pt.setILCount(iLCont);
        return pt;
    }

    /**
     * 处理实施文件的方法 此方法只有实施文档上传 没有打卡
     * @param shishi  上传的实施文档
     */
    public Pageto manageShishi(MultipartFile shishi){
        Utils utils = new Utils();
        //创建分析抽象工厂
        AnalysisFactory analysisFactory = (AnalysisFactory) FactoryProducer.getFactory("Analysisze");
        //打开完整性分析
        Complete complete = (Complete) analysisFactory.getAnalysis("Complete");
        //打开逻辑处理
        Logic logic = (Logic) analysisFactory.getAnalysis("Logic");
        //创建读取抽象工厂
        ReadFacotory readFactory = (ReadFacotory) FactoryProducer.getFactory("Read");
        ReadIntegrityExcel integrityExcel = (ReadIntegrityExcel) readFactory.getExcel("SHISHI");
        List<Map<String, Object>> maps = integrityExcel.getExcel(shishi);

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
        for (Map t : maps) {
            ResultMessage resultC = complete.getIntegrityAnalysis(t, titleMap);
            if (resultC != null) {
                iCCount++;
                reslist.add(resultC);
                //System.out.println(resultC);
            }
            ResultMessage resultL = logic.getIntegrityAnalysis(t, titleMap);
            if (resultL != null) {
                iLCont++;
                reslist.add(resultL);
                //System.out.println(resultL);
            }
            System.out.println("获取的东西" + reslist.get(0));
        }
        //final String uuId = "dsdsd";
        final String uuId = utils.getUUId();
        // 写入流
        System.out.println("得到的集合" + reslist + "uuid" + uuId);
        utils.writeExcel(reslist, uuId);
        pt.setUId(uuId);
        pt.setResultms(reslist);
        pt.setIACount(iACount);
        pt.setICCount(iCCount);
        pt.setILCount(iLCont);
        return pt;
    }




    public String getDownload(HttpServletResponse response, @RequestParam("uid") String uid) {
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
