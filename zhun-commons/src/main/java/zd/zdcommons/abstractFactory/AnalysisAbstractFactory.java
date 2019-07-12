package zd.zdcommons.abstractFactory;

import zd.zdcommons.resouce.ExceclResouce;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.serviceImp.ExcelDrivenImp;
import zd.zdcommons.serviceImp.ReadExcelImp;

public abstract  class AnalysisAbstractFactory {
    public abstract AnalysisImp getAnalysis(String analysis);
    public abstract ReadExcelImp getExcel(String excelname);
    public abstract ExcelDrivenImp getDriver(String EndName);
}
