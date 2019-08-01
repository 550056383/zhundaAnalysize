package zd.zdcommons.resouce;

import zd.zdcommons.pojo.ExcelTable;

import java.util.List;
import java.util.Map;
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
        //System.out.println("size::"+map.size());
        for (Map.Entry<String,String> entry:map.entrySet()){
           // System.out.println("标题："+entry.getKey()+"-值：："+entry.getValue());
            recource.add(entry.getValue());
        }
        //System.out.println("-------------------------------------------------------");
        recources.add(recource);
    }
    public static void clear(){
        listExcel.clear();
        sheetNamex="";
        recources=null;
        table=null;
    }
    public static List<ExcelTable> getListExcel() {
        return listExcel;
    }
}
