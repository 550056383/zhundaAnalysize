package zd.zdcommons.facotry;

import zd.zdcommons.abstractFactory.AnalysisAbstractFactory;
import zd.zdcommons.read.ReadIntegrityExcel;
import zd.zdcommons.read.ReadIntegrityExcelV2;
import zd.zdcommons.read.ReadclockExcel;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.serviceImp.ExcelDrivenImp;
import zd.zdcommons.serviceImp.ReadExcelImp;

public class ReadFacotory extends AnalysisAbstractFactory {
    @Override
    public ReadExcelImp getExcel(String excelname) {
        if(excelname == null){
            return null;
        }
        if(excelname.equalsIgnoreCase("SHISHI")){
            System.out.println("创建SHISHI");
            return new ReadIntegrityExcelV2();
        } else if(excelname.equalsIgnoreCase("DAKA")){
            System.out.println("创建DAKA");
            return new ReadclockExcel();
        }
        return null;
    }

    @Override
    public ExcelDrivenImp getDriver(String EndName) {
        return null;
    }

    @Override
    public AnalysisImp getAnalysis(String analysis) {
        return null;
    }
}
