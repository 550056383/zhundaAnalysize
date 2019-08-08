package zd.zdcommons.resouce;

import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.excel.ExcelXlsxAndDefaultHandlerV2;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName TableDealWith
 * @Author chenkun
 * @TIME 2019/7/31 0031-15:58
 */
public class TableDealWith {
    //输出配置
    private static int count=0;
    private static String sheetx="";
    private static String[] preStrs=null;
    private static String[] titleStr=null;
    private static LinkedHashMap<String, String> resource = null;
    private static List<Map<String, String>> listRsourcecs = null;

    //外部设置
    private static int titleNum=1;
    private static String[] realRules=null;
    private static int interva=0;
    private static String[] sheetNames=null;


    public void getRead(MultipartFile file){
        InputStream inputStream = getInputStream(file);
        String fileName = file.getOriginalFilename();
        if(fileName.endsWith("xlsx")){
            new ExcelXlsxAndDefaultHandlerV2().process(inputStream,sheetNames,file.getName());
        }else if (fileName.endsWith("xls")){
            //new ExcelXlsWithHSSFListener().process();
        };

    }

    private InputStream getInputStream(MultipartFile file){
        InputStream inputStream=null;
        try {
            inputStream= file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }
    public static void getResource(List<String> list, String sheetName){
        String[] strs = list.toArray(new String[list.size()]);
        if(count<titleNum){
            if(count==0){
                preStrs=strs;
            }
            else {
                for (int i=0;i<strs.length;i++){
                    preStrs[i]=strs[i]!=""?preStrs[i]+"--"+strs[i]:preStrs[i];
                }
                titleStr=preStrs;
            }
        }
        else if (count>(titleNum+interva-1)){
            for (int i=0;i<strs.length;i++){
                resource.put(titleStr[i],strs[i]);
            }
            listRsourcecs.add(resource);
            resource= new LinkedHashMap<String, String>();
        }
        //赋值
        if (sheetName!=sheetx){
            sheetx=sheetName;
            count=0;
        }
        count++;
    }

    private static class LayHolder{
        private static final TableDealWith INSTANCE= new TableDealWith();
    }

    private TableDealWith(){}

    public static final TableDealWith getInstance(int titleNumx, String[] realRulesx,int intervax,String[] sheetNamesx) {
        titleNum=titleNumx;
        realRules=realRulesx;
        interva=intervax;
        sheetNames=sheetNamesx;
        resource = new LinkedHashMap<String, String>();
        listRsourcecs = new CopyOnWriteArrayList<Map<String, String>>();
        count = 0;
        return LayHolder.INSTANCE;
    }

    public  List<Map<String, String>> getListRsourcecs() {
        return listRsourcecs;
    }
}
