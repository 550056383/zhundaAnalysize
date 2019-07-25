package zd.zdcommons.serviceImp;


import java.io.InputStream;

public interface ExcelDrivenImp {
    int process(InputStream inputStream,int num,String[] read,String primarykey,String fileName);
}
