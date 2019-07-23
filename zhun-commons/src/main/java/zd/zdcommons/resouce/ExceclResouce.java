package zd.zdcommons.resouce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName ExceclResouce
 * @Author chenkun
 * @TIME 2019/7/12 0012-9:55
 */
public class ExceclResouce {
    private static String[] strTitle;
    private static Map<Integer,List<String>> maps=new HashMap<Integer, List<String>>();
    static Integer count=1;
    public  static void getTitle(Map<String,String> map){
        List<String> list = new CopyOnWriteArrayList<String>();
        for (Map.Entry<String,String> entry :map.entrySet()){
            //System.out.println("entry.getValue() = " + entry.getValue());
            list.add(entry.getValue());
        }
        strTitle=list.toArray(new String[list.size()]);
    }
    //存入数据库

    public  static void  getResource(Map<String,String> map){
        count++;
     List<String> list=new ArrayList<String>();
        int i=0;
        for (Map.Entry<String,String> entry :map.entrySet()){
         System.out.println("标题：="+entry.getKey()+":::值="+entry.getValue());

       list.add(entry.getValue());
        }
        System.out.println("---------------------------");
        if (list.isEmpty()){
            count=1;
            maps.clear();
            list.clear();
        }else {
            maps.put(count, list);
        }
    }

    public static String[] getStrTitle() {
        return strTitle;
    }

    public static void setStrTitle(String[] strTitle) {
        ExceclResouce.strTitle = strTitle;
    }

   public static Map<Integer,List<String>> getResources(){
        return maps;
   }
}
