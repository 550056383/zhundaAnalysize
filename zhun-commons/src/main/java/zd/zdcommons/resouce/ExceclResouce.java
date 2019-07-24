package zd.zdcommons.resouce;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.core.env.MapPropertySource;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName ExceclResouce
 * @Author chenkun
 * @TIME 2019/7/12 0012-9:55
 */
public class ExceclResouce {

    private static List<String[]> strTitle=new LinkedList<String[]>();
    private static String sheetNamex;
    private static Map<Integer,List<String>> maps=new HashMap<Integer, List<String>>();
    static Integer count=1;
    private  static  Boolean tag=false;
    private  static  String[] str=new String[strTitle.size()];
    private  static List<Map<Integer,List<String>>> listmap=new LinkedList<Map<Integer, List<String>>>();

    public  static void getTitle(Map<String,String> map,String sheetName){
        List<String> list = new CopyOnWriteArrayList<String>();
        for (Map.Entry<String,String> entry :map.entrySet()){
            System.out.println(entry.getKey()+"====================="+entry.getValue());
            list.add(entry.getValue());
        }
        System.out.println("sheetName"+sheetName);

        String[] strings=list.toArray(new String[list.size()]);
        strTitle.add(strings);
        sheetNamex=sheetName;
        tag=!tag;
        //list.clear();
    }

    //存入数据库

    public  static void  getResource(Map<String,String> map,String SheetName){

        System.out.println(sheetNamex);
        System.out.println(SheetName);
       if (tag){
           listmap.add(maps);
           maps.clear();
           List<String> list = new CopyOnWriteArrayList<String>();
           for (Map.Entry<String,String> entry :map.entrySet()){
               System.out.println("entry.getValue() = " + entry.getValue());
               list.add(entry.getValue());
           }
           System.out.println("---------------------------------");
           if(list.isEmpty()){
               count=1;
               maps.clear();
               list.clear();
           }else {
               maps.put(count, list);
           }
       }else {
           listmap.add(maps);
           maps.clear();
           List<String> list = new CopyOnWriteArrayList<String>();
           for (Map.Entry<String,String> entry :map.entrySet()){
               System.out.println("entry.getValue() = " + entry.getValue());
               list.add(entry.getValue());
           }
           System.out.println("---------------------------------");
           if(list.isEmpty()){
               count=1;
               maps.clear();
               list.clear();
           }else {
               maps.put(count, list);
           }
       }
            count++;
    }

    public static List<String[]> getStrTitle() {
        return strTitle;
    }

    public static void setStrTitle(List<String[]> strTitle) {
        ExceclResouce.strTitle = strTitle;
    }

   public static List<Map<Integer, List<String>>> getResources(){
        return listmap;
   }
}
