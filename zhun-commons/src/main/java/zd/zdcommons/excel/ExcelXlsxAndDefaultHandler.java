package zd.zdcommons.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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
    //记录条数
    private int total=0;
    //当前列
    private int curColumn=0;
    //判断单元格是否有值
    private boolean isValueCell;
    //缓存及下标
    private String lastContents;
    //共享表
    private SharedStringsTable sst;
    //坐标前后
    private String ref=null; private String preRef=null;
    private String maxRef=null;
    //字段标题
    private LinkedHashMap<String, String> rowTitle=new LinkedHashMap<String, String>();
    //数据的存放
    private LinkedHashMap<String, String> rowContents=new LinkedHashMap<String, String>();
    //数据的存放
    private LinkedHashMap<String, String> rowBefore=new LinkedHashMap<String, String>();



    /*
     * 第二次改进
     */
    //Cell的类型
    enum CellDataType{
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
    }
    //Cell的类型及默认类型
    private ExcelXlsxAndDefaultHandler.CellDataType cellDataType= ExcelXlsxAndDefaultHandler.CellDataType.SSTINDEX;
    //单元格
    private StylesTable stylesTable;
    //单元格日期格式的索引
    private short formatIndex;
    //日期格式字符串
    private String formatString;
    //单元格类型
    private CellDataType nextDataType = CellDataType.SSTINDEX;
    //日期类型转换
    private final DataFormatter formatter = new DataFormatter();
    //设置标题头长短
    private int titleNum=0;
    //设置主键
    private String primaryKey="";
    //设置读取配置
    private String[] ruleOverLay=null;
    private String[] ruleJoint=null;
    //定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格

    //判断是否填入一个坐标
    private Boolean isPreFlag=false;
    private Boolean isEndFlag=false;
    private Boolean flag=true;
    private Boolean firstFag=false;
    private Boolean isStartFlag = false;

    private int sheetIndex=0;
    private String sheetName="";
    private Boolean titlFlag=true;
    public int process(InputStream inputStream,int num,String[] read,String primarykey,String fileName){
        OPCPackage pkg =null;
        try {
            //给标题行数赋值(默认从0开始为第一行)
            titleNum=num-1;
            primaryKey=primarykey;
            getRuleRead(read);//读取规则
            pkg = OPCPackage.open(inputStream);
            XSSFReader r = new XSSFReader(pkg);
            stylesTable = r.getStylesTable();
            sst = r.getSharedStringsTable();
            XMLReader parser =XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
            parser.setContentHandler(this);
            XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) r.getSheetsData();
            while (sheets.hasNext()) { //遍历sheet
                curColumn = 0; //标记初始行为第一行
                sheetIndex++;
                InputStream sheet = sheets.next(); //sheets.next()和sheets.getSheetName()不能换位置，否则sheetName报错
                sheetName = sheets.getSheetName();
                sheetName=fileName+"--"+sheetName;
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource); //解析excel的每条记录，在这个过程中startElement()、characters()、endElement()这三个函数会依次执行
                sheet.close();
                rowTitle=new LinkedHashMap<String, String>();
                total=0;
                titlFlag=true;
            }
            ExceclResouce.getResource(rowBefore,sheetName);//最后一条数据调回
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
            if(preRef==null){
                preRef=attributes.getValue("r");
                isStartFlag = true;
            }else{
                //拿取上次的值
                if(isPreFlag){
                    preRef=ref;
                }
            }
            ref = attributes.getValue("r");//得到单元格编号 如 AF12
            this.setNextDataType(attributes);//设置读取方式
            //String cellType = attributes.getValue("t");//是否存在值(s,null 为返回值)
            //判断值是否存在
            if("t".equals(qName)){
                isValueCell = true;
            } else {
                isValueCell = false;
            }
         }
        //把上次缓存清空
        isPreFlag=false;
        isEndFlag=false;
        lastContents="";
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
        isEndFlag=true;
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
            String value = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            if(total>titleNum){
                rowContents.put(rowTitle.get(curColumn+""),value);
            }else{
                if(rowTitle.get(curColumn+"")!=null){
                    rowTitle.put(curColumn+"",value+"--"+rowTitle.get(curColumn+""));
                }else {
                    rowTitle.put(curColumn+"",value);
                }
            }
            //关闭isValueCell
            isValueCell=false;
            isPreFlag=true;
            //更新列
            curColumn++;
        } else if (qName.equals("v")) {
            if (ref.contains("A")) {
                isStartFlag = false;
            }
            String value = this.getDataValue(lastContents.trim(), "");
            if(value.contains("\"")){
                value=value.substring(1,value.length()-1);
            }
            //补全中间
            if(!ref.equals(preRef)){
                int len = 0;
                if (isStartFlag) {
                    len = countNullCell(ref, "A") + 1;
                    isStartFlag = false;
                } else {
                    len = countNullCell(ref, preRef);
                }
                //是否是标题
                for (int i = 0; i < (firstFag?(len+1):len); i++) {
                    rowContents.put(rowTitle.get(curColumn+""),"");
                    curColumn++;
                }
                firstFag=false;
            }
            if(total>titleNum){
                rowContents.put(rowTitle.get(curColumn+""),value);
            }else{
                if(rowTitle.get(curColumn+"")!=null){
                    rowTitle.put(curColumn+"",value+"--"+rowTitle.get(curColumn+""));
                }else {
                    rowTitle.put(curColumn+"",value);
                }
            }
            isPreFlag=true;
            curColumn++;
        }
        else if (qName.equals("row")){//换行
            if (total==0) {
                 maxRef= ref;
            }
            if (total > titleNum) {
                if (titlFlag) {//返回数据
                    ExceclResouce.getTitle(rowTitle,sheetName);
                    titlFlag=false;
                    flag = false;
                } else {
                    flag = true;
                }
                String beValue = rowBefore.get(primaryKey);
                String conValue = rowContents.get(primaryKey);
                if(StringUtils.isNotBlank(beValue) &&beValue.equals(conValue)){
                    //叠加，拼接，覆盖
                    for (Map.Entry<String,String> entry:rowContents.entrySet()){
                        getOverlay(entry);
                        getJoint(entry);
                        //如果是空
                        if(StringUtils.isBlank(entry.getValue())){
                            rowContents.put(entry.getKey(),rowBefore.get(entry.getKey()));
                        }
                    }
                }else {
                    if(flag){
                        ExceclResouce.getResource(rowBefore,sheetName);//每条数据，则用getShow方法返回
                    }
                }
            }

            //补全一行尾部可能缺失的单元格
            if (maxRef != null) {
                int len = -1;
                //前一单元格，true则不是文本空字符串，false则是文本空字符串
                if(isEndFlag){
                    len =countNullCell(maxRef,ref);
                }else{
                    len = countNullCell(maxRef, preRef);
                }
                for (int i = 0; i <= len; i++) {
                    rowContents.put(rowTitle.get(curColumn+""),"");
                    curColumn++;
                }
            }
            rowBefore=rowContents;
            rowContents= new LinkedHashMap<String, String>();
            preRef = null;
            ref = null;
            flag=false;
            total++;
            curColumn=0;
        }
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
    public int getInt(String ref){
        int i = Integer.parseInt(ref.replaceAll("[^0-9]", ""));
        return i;
    }
    //叠加
    public void getOverlay(Map.Entry<String,String> entry){
        for (String strTitle:ruleOverLay){
            if(entry.getKey().equals(strTitle)){
                double beforeValue = Double.parseDouble(rowBefore.get(strTitle));
                double contenValue = Double.parseDouble(entry.getValue());
                rowContents.put(strTitle,String.format("%.2f",(contenValue+beforeValue)));
            };
        }
    };
    //拼接
    public void getJoint(Map.Entry<String,String> entry){
        for (String strTitle:ruleJoint){
            if(entry.getKey().equals(strTitle)){
                String beforeValue = rowBefore.get(strTitle);
                String contenValue = entry.getValue();
                rowContents.put(strTitle,(contenValue+","+beforeValue));
            };
        }
    }

    /**
     * 处理数据类型
     *
     * @param attributes
     */
    public void setNextDataType(Attributes attributes) {
        nextDataType = CellDataType.NUMBER; //cellType为空，则表示该单元格类型为数字
        formatIndex = -1;
        formatString = null;
        String cellType = attributes.getValue("t"); //单元格类型
        String cellStyleStr = attributes.getValue("s"); //
        String columnData = attributes.getValue("r"); //获取单元格的位置，如A1,B1

        if ("b".equals(cellType)) { //处理布尔值
            nextDataType = CellDataType.BOOL;
        } else if ("e".equals(cellType)) {  //处理错误
            nextDataType = CellDataType.ERROR;
        } else if ("inlineStr".equals(cellType)) {
            nextDataType = CellDataType.INLINESTR;
        } else if ("s".equals(cellType)) { //处理字符串
            nextDataType = CellDataType.SSTINDEX;
        } else if ("str".equals(cellType)) {
            nextDataType = CellDataType.FORMULA;
        }

        if (cellStyleStr != null) { //处理日期
            int styleIndex = Integer.parseInt(cellStyleStr);
            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
            formatIndex = style.getDataFormat();
            formatString = style.getDataFormatString();
            if (formatString.contains("m/d/yyyy") || formatString.contains("yyyy/mm/dd")
                    || formatString.contains("yyyy/m/d") ||formatString.contains("m/d/yy")) {
                nextDataType = CellDataType.DATE;
                formatString = "yyyy-MM-dd";
            }

            if (formatString == null) {
                nextDataType = CellDataType.NULL;
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }
    }
    /**
     * 对解析出来的数据进行类型处理
     * @param value   单元格的值，
     *                value代表解析：BOOL的为0或1， ERROR的为内容值，FORMULA的为内容值，INLINESTR的为索引值需转换为内容值，
     *                SSTINDEX的为索引值需转换为内容值， NUMBER为内容值，DATE为内容值
     * @param thisStr 一个空字符串
     * @return
     */
    @SuppressWarnings("deprecation")
    public String getDataValue(String value, String thisStr) {
        switch (nextDataType) {
            // 这几个的顺序不能随便交换，交换了很可能会导致数据错误
            case BOOL: //布尔值
                char first = value.charAt(0);
                thisStr = first == '0' ? "FALSE" : "TRUE";
                break;
            case ERROR: //错误
                thisStr = "\"ERROR:" + value.toString() + '"';
                break;
            case FORMULA: //公式
                thisStr = '"' + value.toString() + '"';
                break;
            case INLINESTR:
                XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                thisStr = rtsi.toString();
                rtsi = null;
                break;
            case SSTINDEX: //字符串
                String sstIndex = value.toString();
                try {
                    int idx = Integer.parseInt(sstIndex);
                    XSSFRichTextString rtss = new XSSFRichTextString(sst.getEntryAt(idx));//根据idx索引值获取内容值
                    thisStr = rtss.toString();
                    //有些字符串是文本格式的，但内容却是日期

                    rtss = null;
                } catch (NumberFormatException ex) {
                    thisStr = value.toString();
                }
                break;
            case NUMBER: //数字
                if (formatString != null) {
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();
                } else {
                    thisStr = value;
                }
                thisStr = thisStr.replace("_", "").trim();
                break;
            case DATE: //日期
                thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                // 对日期字符串作特殊处理，去掉T
                thisStr = thisStr.replace("T", " ");
                break;
            default:
                thisStr = " ";
                break;
        }
        return thisStr;
    }

    public int countNullCell(String ref, String preRef) {
        //excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
        String xfd = ref.replaceAll("\\d+", "");
        String xfd_1 = preRef.replaceAll("\\d+", "");

        xfd = fillChar(xfd, 3, '@', true);
        xfd_1 = fillChar(xfd_1, 3, '@', true);

        char[] letter = xfd.toCharArray();
        char[] letter_1 = xfd_1.toCharArray();
        int res = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
        return res - 1;
    }

    public String fillChar(String str, int len, char let, boolean isPre) {
        int len_1 = str.length();
        if (len_1 < len) {
            if (isPre) {
                for (int i = 0; i < (len - len_1); i++) {
                    str = let + str;
                }
            } else {
                for (int i = 0; i < (len - len_1); i++) {
                    str = str + let;
                }
            }
        }
        return str;
    }
}
