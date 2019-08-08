package zd.zdcommons.read;

import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.resouce.TableDealWith;
import zd.zdcommons.serviceImp.ReadExcelImp;

import java.util.List;
import java.util.Map;

public class ReadIntegrityExcelV2 implements ReadExcelImp {
    @Override
    public List<Map<String, String>> getExcel(MultipartFile shishi) {
        TableDealWith dealWith = TableDealWith.getInstance(2, null, 1, new String[]{"Site Rollout Plan"});
        dealWith.getRead(shishi);
        return dealWith.getListRsourcecs();
    }
}
