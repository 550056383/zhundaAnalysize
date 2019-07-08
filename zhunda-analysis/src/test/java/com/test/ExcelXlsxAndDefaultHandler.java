package com.test;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ExcelXlsxAndDefaultHandler
 * @Author chenkun
 * @TIME 2019/7/1 0001-9:33
 */

public class ExcelXlsxAndDefaultHandler extends DefaultHandler {
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

    //定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
    public int process(InputStream inputStream){
        OPCPackage pkg =null;
        try {
            pkg = OPCPackage.open(inputStream);
            XSSFReader r = new XSSFReader(pkg);
            sst = r.getSharedStringsTable();
            XMLReader parser =XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
            parser.setContentHandler(this);
            InputStream sheet = r.getSheet("rId1");
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
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

    public void  getNextType(Attributes attributes){
        cellPosition = attributes.getValue("r");//得到单元格编号 如 AF12
        String cellType = attributes.getValue("t");//是否存在值(s,null 为返回值)
        //System.out.println("编号："+cellPosition+"\t 是否存在："+cellType);
        if (cellType.equals("b")){
            cellDataType= CellDataType.BOOL;
        }else if (cellType.equals("e")){
            cellDataType=CellDataType.ERROR;
        }else if (cellType.equals("inlineStr")){
            cellDataType=CellDataType.INLINESTR;
        }else if (cellType.equals("s")){
            cellDataType=CellDataType.SSTINDEX;
        }else if(cellType.equals("str")){
            cellDataType=CellDataType.FORMULA;
        }
        //处理日期
        if(cellType!=null&& cellType.equals("s")){
            isValueCell = true;
        } else {
            isValueCell = false;
        }
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
        if (qName.equals("v")){
            //System.out.println("cellPosition:"+cellPosition+" and value:"+lastContents.trim());
            //数据读取结束后，将单元格坐标,内容存入map中
            if (total==0){
                rowTitle.put(getStr(cellPosition),lastContents);
            }else {
                rowContents.put(rowTitle.get(getStr(cellPosition)),lastContents);
            }
        }else if (qName.equals("row")){//换行
            //System.out.println("ok");
            ReadAndTest.getShow(rowContents);
            total++;
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
    public String getStr(String ref){
        String s = ref.replaceAll("\\d+", "");
        return s;
    }
}
