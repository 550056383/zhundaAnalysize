package zd.zdcommons.abstractFactory;

import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.serviceImp.ReadExcelImp;

public abstract  class AnalysisAbstractFactory {
    public abstract AnalysisImp getAnalysis(String analysis);
    public abstract ReadExcelImp getExcel(String excelname);
}
