package zd.zdcommons.resouce;

import java.util.ArrayList;
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
    public  static void getTitle(Map<String,String> map){
        List<String> list = new CopyOnWriteArrayList<String>();
        for (Map.Entry<String,String> entry :map.entrySet()){
            //System.out.println("entry.getValue() = " + entry.getValue());
            list.add(entry.getValue());
        }
        strTitle=list.toArray(new String[list.size()]);
    }
    //存入数据库
    public static void  getResource(Map<String,String> map){

    }
}
