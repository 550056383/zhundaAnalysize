package zd.zdcommons.read;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.resouce.TableDealWith;
import zd.zdcommons.serviceImp.ReadExcelImp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadIntegrityExcelV2 implements ReadExcelImp {
    @Override
    public List<Map<String, Object>> getExcel(MultipartFile shishi) {
        TableDealWith dealWith = TableDealWith.getInstance(2, null, 1, new String[]{"Site Rollout Plan"});
        dealWith.getRead(shishi);
        return dealWith.getListRsourcecs();
    }
}
