package zd.zdcommons.facotry;

import zd.zdcommons.abstractFactory.AnalysisAbstractFactory;
import zd.zdcommons.analysis.ClockAnalysis;
import zd.zdcommons.analysis.Complete;
import zd.zdcommons.analysis.Logic;
import zd.zdcommons.excel.ExcelXlsWithHSSFListener;
import zd.zdcommons.excel.ExcelXlsxAndDefaultHandler;
import zd.zdcommons.serviceImp.AnalysisImp;
import zd.zdcommons.serviceImp.ExcelDrivenImp;
import zd.zdcommons.serviceImp.ReadExcelImp;

/**
 * @ClassName ExcelDriverFactory
 * @Author chenkun
 * @TIME 2019/7/12 0012-17:19
 */
public class ExcelDriverFactory extends AnalysisAbstractFactory {
    @Override
    public AnalysisImp getAnalysis(String analysis) {
        return null;
    }

    @Override
    public ReadExcelImp getExcel(String excelname) {
        return null;
    }

    @Override
    public ExcelDrivenImp getDriver(String EndName) {
        if(EndName.equalsIgnoreCase("XLS")){
            System.out.println("创建 XLS");
            return new ExcelXlsWithHSSFListener();
        } else if(EndName.equalsIgnoreCase("XLSX")){
            System.out.println("创建 XLSX");
            return new ExcelXlsxAndDefaultHandler();
        }
        return null;
    }
}
