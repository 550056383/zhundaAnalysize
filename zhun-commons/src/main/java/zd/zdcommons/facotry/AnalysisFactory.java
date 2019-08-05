package zd.zdcommons.facotry;

import zd.zdcommons.abstractFactory.AnalysisAbstractFactory;
import zd.zdcommons.analysis.*;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.serviceImp.ExcelDrivenImp;
import zd.zdcommons.serviceImp.ReadExcelImp;

public class AnalysisFactory extends AnalysisAbstractFactory {
    @Override
    public ExcelDrivenImp getDriver(String EndName) {
        return null;
    }

    @Override
    public AnalysisImp getAnalysis(String analysis) {
        if(analysis == null){
            return null;
        }
        if(analysis.equalsIgnoreCase("Complete")){
            System.out.println("创建Complete");
            return new CompleteV2();
        } else if(analysis.equalsIgnoreCase("Logic")){
            System.out.println("创建Logic");
            return new LogicV2();
        }else if(analysis.equalsIgnoreCase("DAKA")){
            System.out.println("创建DAKA");
            return new ClockAnalysis();
        }
        return null;
    }

    @Override
    public ReadExcelImp getExcel(String excelname) {
        return null;
    }
}
