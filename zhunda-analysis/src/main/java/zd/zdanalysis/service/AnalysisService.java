package zd.zdanalysis.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.FactoryProducer;
import zd.zdcommons.Utils;
import zd.zdcommons.abstractFactory.AnalysisAbstractFactory;
import zd.zdcommons.analysis.Complete;
import zd.zdcommons.analysis.Logic;
import zd.zdcommons.facotry.AnalysisFactory;
import zd.zdcommons.facotry.ReadFacotory;
import zd.zdcommons.pojo.Pageto;
import zd.zdcommons.pojo.ResultMessage;
import zd.zdcommons.read.ReadIntegrityExcel;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.serviceImp.ReadExcelImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

    public Pageto getAnalysis(MultipartFile shishi, MultipartFile daka) {
        Utils utils = new Utils();
        //创建分析抽象工厂
        AnalysisFactory analysisFactory = (AnalysisFactory) FactoryProducer.getFactory("Analysisze");
        //打开完整性分析
        Complete complete = (Complete) analysisFactory.getAnalysis("Complete");
        //打开逻辑处理
        Logic logic =(Logic) analysisFactory.getAnalysis("Logic");
        //创建读取抽象工厂
        ReadFacotory readFactory = (ReadFacotory) FactoryProducer.getFactory("Read");
        ReadIntegrityExcel integrityExcel = (ReadIntegrityExcel) readFactory.getExcel("SHISHI");
        List<Map<String, Object>> maps = integrityExcel.getExcel(shishi);
        //获取原来标题
        Map<String, Object> titleMap = utils.getTitle();
        Pageto pt = new Pageto();

        //完整性计数
        long iCCount=0;
        //准确性计数
        long iACount=0;
        //逻辑性计数
        long iLCont=0;
        //信息
        List<ResultMessage> reslist=new ArrayList<ResultMessage>();
        for (Map t :maps){
            ResultMessage resultC = complete.getIntegrityAnalysis(t,titleMap);
            if(resultC!=null){
                iCCount++;
                reslist.add(resultC);
                //System.out.println(resultC);
            }
            ResultMessage resultL = logic.getIntegrityAnalysis(t,titleMap);
            if(resultL!=null){
                iLCont++;
                reslist.add(resultL);
                //System.out.println(resultL);
            }
        }
        final String uuId = utils.getUUId();
//       写入流
        utils.writeExcel(reslist,uuId);
        pt.setUId(uuId);
        pt.setResultms(reslist);
        pt.setIACount(iACount);
        pt.setICCount(iCCount);
        pt.setILCount(iLCont);
        return pt;
    }
}
