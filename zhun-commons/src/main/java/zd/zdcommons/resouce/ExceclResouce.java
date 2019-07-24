package zd.zdcommons.resouce;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.core.env.MapPropertySource;
import zd.zdcommons.pojo.ExcelTable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName ExceclResouce
 * @Author chenkun
 * @TIME 2019/7/12 0012-9:55
 */
public class ExceclResouce {
    private static String sheetNamex="";
    private static List<ExcelTable> listExcel=new CopyOnWriteArrayList<ExcelTable>();
    private static ExcelTable table=null;
    private static List<List<String>> recources=null;
    public  static void getTitle(Map<String,String> map,String sheetName){
        List<String> list = new CopyOnWriteArrayList<String>();
        for (Map.Entry<String,String> entry :map.entrySet()){
            list.add(entry.getValue());
        }
        String[] strings=list.toArray(new String[list.size()]);
        if(!sheetNamex.equals(sheetName)){
            table=new ExcelTable();
            listExcel.add(table);
            table.setTitle(strings);
            table.setSheetName(sheetName);
            recources=new CopyOnWriteArrayList<List<String>>();
            table.setResource(recources);
            sheetNamex=sheetName;
        }
    }

    //存入数据库

    public  static void  getResource(Map<String,String> map,String SheetName){
        List<String> recource = new CopyOnWriteArrayList<String>();
        for (Map.Entry<String,String> entry:map.entrySet()){
            recource.add(entry.getValue());
        }
        recources.add(recource);
    }

    public static List<ExcelTable> getListExcel() {
        return listExcel;
    }
}
