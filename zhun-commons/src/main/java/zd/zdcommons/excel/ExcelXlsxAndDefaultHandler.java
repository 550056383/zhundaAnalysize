package zd.zdcommons.excel;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import zd.zdcommons.resouce.ExceclResouce;
import zd.zdcommons.serviceImp.ExcelDrivenImp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ExcelXlsxAndDefaultHandler
 * @Author chenkun
 * @TIME 2019/7/1 0001-9:33
 */

public class ExcelXlsxAndDefaultHandler extends DefaultHandler implements ExcelDrivenImp {
    //记录数据总条数
    private int total=0;
    //判断单元格是否有值
    private boolean isValueCell;
    //缓存及下标
    private String lastContents;
    //共享表
    private SharedStringsTable sst;
    //单元格编号
    private String cellPosition;
    //字段标题
    private LinkedHashMap<String, String> rowTitle=new LinkedHashMap<String, String>();
    //数据的存放
    private LinkedHashMap<String, String> rowContents=new LinkedHashMap<String, String>();
    //数据的存放
    private LinkedHashMap<String, String> rowBefore=new LinkedHashMap<String, String>();

    //是否返回标题
    private Boolean fagTitle=true;
    //判断是否是空字段
    String Cellpx=null;
    /*
     * 第二次改进
     */
    //Cell的类型
    enum CellDataType{
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
    }
    //Cell的类型及默认类型
    private ExcelXlsxAndDefaultHandler.CellDataType cellDataType=ExcelXlsxAndDefaultHandler.CellDataType.SSTINDEX;
    //单元格
    private StylesTable stylesTable;
    //单元格日期格式的索引
    private short formatIndex;
    //日期格式字符串
    private String formatString;
    private CellDataType nextDataType = CellDataType.SSTINDEX;
    //定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
    private String preRef = null, ref = null;


    //设置标题头长短
    private int titleNum=0;
    //设置主键
    private String primaryKey="";
    //设置读取配置
    private String[] ruleOverLay=null;
    private String[] ruleJoint=null;
    //定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
    public int process(InputStream inputStream,int num,String[] read,String primarykey){
        OPCPackage pkg =null;
        try {
            //给标题行数赋值(默认从0开始为第一行)
            titleNum=num-1;
            primaryKey=primarykey;
            getRuleRead(read);//读取规则
            pkg = OPCPackage.open(inputStream);
            XSSFReader r = new XSSFReader(pkg);
            sst = r.getSharedStringsTable();
            XMLReader parser =XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
            parser.setContentHandler(this);
            InputStream sheet = r.getSheet("rId1");
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            ExceclResouce.getResource(rowBefore);//最后一条数据调回
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    /***
     * 执行顺序 - 第一
     * frist
     * 目标：判断单元格是否有值给isValueCell赋值
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("c")){
            cellPosition = attributes.getValue("r");//得到单元格编号 如 AF12
            String cellType = attributes.getValue("t");//是否存在值(s,null 为返回值)
            //System.out.println("编号："+cellPosition+"\t 是否存在："+cellType);
            //判断值是否存在
            if(cellType!=null&& cellType.equals("s")){
                isValueCell = true;
            } else {
                isValueCell = false;
            }
         }
        //把上次缓存清空
        lastContents="";
    }

    /***
     * 执行顺序 - 第三
     * third
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(isValueCell){
            //获取缓存大小
            int idx = Integer.parseInt(lastContents);
            //给缓存赋value 值
            lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            //关闭isValueCell
            isValueCell=false;
        }
        //System.out.println("cellPosition = " + cellPosition+" qName"+qName);
        if (qName.equals("v")){
            //判断有值坐标
             Cellpx=cellPosition;
            //数据读取结束后，将单元格坐标,内容存入map中
            //把数据装在Map里
            String cellPost = getStr(cellPosition);//拿取坐标列
            if (total>titleNum){//拿取数据
                if(fagTitle){//返回标题
                    ExceclResouce.getTitle(rowTitle);
                    fagTitle=false;
                }
                rowContents.put(rowTitle.get(cellPost),lastContents);
            }else {//拿取表头
                if (rowTitle.get(cellPost)!=null){
                    rowTitle.put(cellPost,rowTitle.get(cellPost)+"--"+lastContents);
                }else {
                    rowTitle.put(cellPost,lastContents);
                }
            }
        }else if (qName.equals("row")){//换行
            //System.out.println("ok");
            String beValue=rowBefore.get(primaryKey);
            String conValue = rowContents.get(primaryKey);
            if(StringUtils.isNotBlank(beValue)&&beValue.equals(conValue)){
                //叠加，拼接
                rowContents= getOverlay(ruleOverLay);
                rowContents= getJoint(ruleJoint);
            }else {
                ExceclResouce.getResource(rowBefore);//每条数据，则用getShow方法返回
            }
            total++;
            rowBefore=rowContents;
            rowContents= new LinkedHashMap<String, String>();
        }else if (qName.equals("c")){
            if(!Cellpx.equals(cellPosition)){
                rowContents.put(rowTitle.get(getStr(cellPosition)),"");
            }
        }
    }

    /***
     * 执行顺序 - 第二
     * second
     * 目标：增加缓存
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        lastContents+=new String(ch,start,length);
    }
    //特殊读取解析
    public void  getRuleRead(String[] read){
        List<String> overlay = new ArrayList<String>();
        List<String> joint = new ArrayList<String>();
        for (int i=0;i<read.length-1;i+=2){
            if(read[i+1].trim().equals("叠加")){
                overlay.add(read[i]);
            }else if(read[i+1].trim().equals("拼接")){
                joint.add(read[i]);
            }
        }
        ruleOverLay = overlay.toArray(new String[overlay.size()]);
        ruleJoint  = joint.toArray(new String[joint.size()]);
    }
    //得到非数字部分
    public String getStr(String ref){
        String s = ref.replaceAll("\\d+", "");
        return s;
    }
    //叠加
    public LinkedHashMap<String,String> getOverlay(String[]str){
        LinkedHashMap<String,String> map=rowBefore;
        for (Map.Entry<String,String> entry:rowContents.entrySet()){
            for (String strTitle:str){
                if(entry.getKey().equals(strTitle)){
                    double beforeValue = Double.parseDouble(rowBefore.get(strTitle));
                    double contenValue = Double.parseDouble("".equals(entry.getValue())?"0.00":entry.getValue());
                    map.put(strTitle,(contenValue+beforeValue)+"");
                };
            }
        }
        return map;
    }
    //拼接
    public LinkedHashMap<String,String> getJoint(String[]str){
        LinkedHashMap<String,String> map=rowBefore;
        for (Map.Entry<String,String> entry:rowContents.entrySet()){
            for (String strTitle:str){
                if(entry.getKey().equals(strTitle)){
                    String beforeValue = rowBefore.get(strTitle);
                    String contenValue = entry.getValue();
                    map.put(strTitle,(contenValue+","+beforeValue));
                };
            }
        }
        return map;
    }
    /*
    public void  getNextType(Attributes attributes){
        cellPosition = attributes.getValue("r");//得到单元格编号 如 AF12
        String cellType = attributes.getValue("t");//是否存在值(s,null 为返回值)
        //System.out.println("编号："+cellPosition+"\t 是否存在："+cellType);
        if (cellType.equals("b")){
            cellDataType= CellDataType.BOOL;
        }else if (cellType.equals("e")){
            cellDataType= CellDataType.ERROR;
        }else if (cellType.equals("inlineStr")){
            cellDataType= CellDataType.INLINESTR;
        }else if (cellType.equals("s")){
            cellDataType= CellDataType.SSTINDEX;
        }else if(cellType.equals("str")){
            cellDataType= CellDataType.FORMULA;
        }
        //处理日期
        if(cellType!=null&& cellType.equals("s")){
            isValueCell = true;
        } else {
            isValueCell = false;
        }
    }
    public int getRowsNum(String maxRef){
        //去除数字
        String s = getStr(maxRef);
        //记录第几个空格
        int len = s.length();
        //确定多少列
        int sum=0;
        for (int i=len;i>0;i--){
            //获取字母，转ascll码,计算(A是65，所以模上64)
            int c=((int)s.charAt(i-1))%64;
            //如果不是最后一位字母我们就*26 个字母，否则拼加
            if(i>len){
                sum+=(c*26);
            }else{
                sum+=c;
            }
        }
        return sum;
    }
     */
}
