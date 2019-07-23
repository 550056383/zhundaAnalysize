package com.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import zd.zdcommons.resouce.ExceclResouce;
import zd.zdcommons.serviceImp.ExcelDrivenImp;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ExcelXlsWithHSSFListener
 * @Author chenkun
 * @TIME 2019/7/2 -11:27
 */

public class ExcelXlsWithHSSFListener implements HSSFListener  {

    private ArrayList boundSheetRecords = new ArrayList();

    //当前行
    private int curRow=0;
    //当前列
    private int curColumn=0;
    //总列数
    private int contMaxCol=0;
    //2003工作簿的sst记录
    private SSTRecord sstRecord;

    //字段标题
    private LinkedHashMap<String, String> rowTitle=new LinkedHashMap<String, String>();
    //数据的存放
    private LinkedHashMap<String, String> rowContents=new LinkedHashMap<String, String>();
    //上条数据返回数据
    private LinkedHashMap<String, String> rowBefore=new LinkedHashMap<String, String>();
    //POIFS文件系统
    private POIFSFileSystem fileSystem;
    //HSSF监听对象
    private FormatTrackingHSSFListener formatListener;
    //HSSF数据格式转换器
    private final HSSFDataFormatter formatter = new HSSFDataFormatter();
    //Miss记录监听对象
    private MissingRecordAwareHSSFListener listener;
    /**
     * 用于转换formulas
     */
    private EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener;
    //excel2003工作簿
    private HSSFWorkbook stubWorkbook;
    //表索引
    private int sheetIndex = 0;
    /**
     * 是否输出formula，还是它对应的值
     */
    private Boolean outputFormulaValues=true;

    private BoundSheetRecord[] orderedBSRs;
    //设置标题头长短
    private int titleNum=0;
    //设置主键
    private String primaryKey="";
    //设置读取配置
    private String[] ruleOverLay=null;
    private String[] ruleJoint=null;
    //返回标题(如果sheetName 改变，则返回)
    private String sxName;
    //表名
    private String sheetName;
    public int process(InputStream inputStream,int num,String[] read,String primarykey){
        try {
            //给标题行数赋值(默认从0开始为第一行)
            titleNum=num-1;
            primaryKey=primarykey;
            getRuleRead(read);//读取规则
            //POIFS文件系统创建
            fileSystem = new POIFSFileSystem(inputStream);
            //初始化MissingRecordAwareHSSFListener Miss记录监听对象（监听的事当前的HSSFListener的实现类）
            listener = new MissingRecordAwareHSSFListener(this);
            //初始化 FormatTrackingHSSFListener 对象转换格式（添加监听对象）
            formatListener = new FormatTrackingHSSFListener(listener);
            //创建事件工厂
            HSSFEventFactory factory = new HSSFEventFactory();
            //HSSF请求
            HSSFRequest request = new HSSFRequest();
            //添加监听（必须进行格式化，不然没数据）
            request.addListenerForAllRecords(formatListener);
            if (outputFormulaValues) {
                request.addListenerForAllRecords(formatListener);
            } else {
                workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(formatListener);
                request.addListenerForAllRecords(workbookBuildingListener);
            }
            factory.processWorkbookEvents(request,fileSystem);//执行(这里-开始执行 processRecord(Record record))
            //追加最后一行数据
            ExceclResouce.getResource(rowBefore);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return curRow;
    }
    public void processRecord(Record record) {
        switch (record.getSid()){
            case BoundSheetRecord.sid:
                boundSheetRecords.add(record);
                break;
            case BOFRecord.sid:
                BOFRecord br = (BOFRecord) record;
                if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                    //如果有需要，则建立子工作簿
                    if (workbookBuildingListener != null && stubWorkbook == null) {
                        stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                    }
                    if (orderedBSRs == null) {
                        orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                    }
                    sheetName = orderedBSRs[sheetIndex].getSheetname();
                    sheetIndex++;
                }
                break;
            case SSTRecord.sid://sstRecord SST记录（需要初始化，不然不知道存哪里，导致没数据）
                sstRecord=(SSTRecord)record;
                break;
            case BlankRecord.sid: //单元格为空白
                BlankRecord brec = (BlankRecord) record;
                break;
            case BoolErrRecord.sid: //单元格为布尔类型
                BoolErrRecord berec = (BoolErrRecord) record;
                curRow = berec.getRow();
                curColumn = berec.getColumn();
                rowContents.put(rowTitle.get(curColumn),berec.getBooleanValue() + "");
                break;
            case FormulaRecord.sid://单元格为公式类型
                FormulaRecord frec = (FormulaRecord) record;
                break;
            case StringRecord.sid: //单元格中公式的字符串
                StringRecord srec = (StringRecord) record;
                break;
            case LabelRecord.sid:
                LabelRecord lrec = (LabelRecord) record;
                break;
            case LabelSSTRecord.sid: //单元格为字符串类型（也是我们获取String类型数据的地方）
                LabelSSTRecord lsrec = (LabelSSTRecord) record;
                curRow= lsrec.getRow();//当前行
                curColumn = lsrec.getColumn();//列
                //在共享表获取值
                String value = sstRecord.getString(lsrec.getSSTIndex()).toString().trim();
                if(curRow<=titleNum){//默认第一行为标题
                    if(rowTitle.get(curColumn)!=null){
                        rowTitle.put(curColumn+"",value+"--"+rowTitle.get(curColumn));
                    }else {
                        rowTitle.put(curColumn+"",value);
                    }
                }else {
                    rowContents.put(rowTitle.get(curColumn+""),value);
                }
                break;
            case NumberRecord.sid: //单元格为数字类型
                NumberRecord numrec = (NumberRecord) record;
                curRow = numrec.getRow();
                curColumn = numrec.getColumn();

                Double valueDouble=((NumberRecord)numrec).getValue();
                //查看数据格式
                String formatString=formatListener.getFormatString(numrec);
                //判断是否存在这种日期天数类型
                if (formatString.contains("m/d/yy") || formatString.contains("yyyy/mm/dd") || formatString.contains("yyyy/m/d")){
                    formatString="yyyy-MM-dd";
                }
                int formatIndex=formatListener.getFormatIndex(numrec);

                //保留两位
                BigDecimal b = new BigDecimal(valueDouble);
                valueDouble=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                value=formatter.formatRawCellContents(valueDouble, formatIndex, formatString).trim();
                //判断是否为空
                value = value.equals("") ? "" : value;
                //添加值
                rowContents.put(rowTitle.get(curColumn+""),value);
                break;
            default:
                break;
        }
        //空值的操作
        if (record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
            curRow  = mc.getRow();
            curColumn = mc.getColumn();
            //给空填值
            rowContents.put(rowTitle.get(curColumn+""),"");
        }
        //换行
        if (record instanceof LastCellOfRowDummyRecord){
            if (curRow>titleNum) { //判断是否是第二条数据，我们返回前一条数据
                if (sxName!=sheetName){//返回数据
                    ExceclResouce.getTitle(rowTitle);
                    sxName=sheetName;
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
                    ExceclResouce.getResource(rowBefore);//每条数据，则用getShow方法返回
                }
                //末尾为空补全
                if(contMaxCol>curColumn){
                    for (int i=0;i<(contMaxCol-curColumn);i++){
                        String title = rowTitle.get((curColumn + i + 1)+"");
                        rowContents.put(title,"");
                    }
                }
            }
            //得到最大列数
            if (curRow==0){
                contMaxCol=curColumn;
            }
            //给前一条数据赋值
            rowBefore=rowContents;
            rowContents= new LinkedHashMap<String, String>();
            curRow++;
        }
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
}
