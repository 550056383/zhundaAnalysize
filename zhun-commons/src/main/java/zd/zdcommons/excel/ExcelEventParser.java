package zd.zdcommons.excel;

import org.apache.poi.hssf.usermodel.examples.HSSFReadWrite;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者：iteye_8851
 *
 * */
public class ExcelEventParser {
    private String filename;
    private SheetContentsHandler handler;
    private InputStream inputStream;
    public ExcelEventParser(InputStream inputStream){
        //this.filename = filename;

        this.inputStream = inputStream;
    }

    public ExcelEventParser setHandler(SheetContentsHandler handler) {
        this.handler = handler;
        return this;
    }

    public void parse(){
        OPCPackage pkg = null;
        InputStream sheetInputStream = null;

        try {
            // pkg = OPCPackage.open(filename, PackageAccess.READ);
            pkg=OPCPackage.open(inputStream);
            XSSFReader xssfReader = new XSSFReader(pkg);
            StylesTable styles = xssfReader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
            sheetInputStream = xssfReader.getSheetsData().next();

            processSheet(styles, strings, sheetInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }finally {
            if(sheetInputStream != null){
                try {
                    sheetInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            if(pkg != null){
                try {
                    pkg.close();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
    }
    private void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream) throws SAXException, ParserConfigurationException, IOException{
        XMLReader sheetParser = SAXHelper.newXMLReader();

        if(handler != null){
            sheetParser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, handler, false));
        }else{
            sheetParser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, new SimpleSheetContentsHandler(), false));
        }

        sheetParser.parse(new InputSource(sheetInputStream));
    }
    public static class SimpleSheetContentsHandler implements SheetContentsHandler{
        protected List<String> row = new LinkedList<String>();
        public void startRow(int rowNum) {
            row = new LinkedList<String>();
//            row.clear();
        }


        public void endRow(int rowNum) {
            System.err.println(rowNum + " : " + row);
        }

        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            row.add(formattedValue);
        }


        public void headerFooter(String text, boolean isHeader, String tagName) {

        }
    }

}
