package zd.zdcommons.facotry;

import zd.zdcommons.abstractFactory.AnalysisAbstractFactory;
import zd.zdcommons.read.ReadIntegrityExcel;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.serviceImp.ReadExcelImp;

public class ReadFacotory extends AnalysisAbstractFactory {
    @Override
    public ReadExcelImp getExcel(String excelname) {
        if(excelname == null){
            return null;
        }
        if(excelname.equalsIgnoreCase("SHISHI")){
            System.out.println("创建SHISHI");
            return new ReadIntegrityExcel();
        } else if(excelname.equalsIgnoreCase("daka")){
            System.out.println("创建daka");
            return new  ReadIntegrityExcel();
        }
        return null;
    }
    @Override
    public AnalysisImp getAnalysis(String analysis) {
        return null;
    }
}
