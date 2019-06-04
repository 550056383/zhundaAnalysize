package zd.zdcommons.serviceImp;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ReadExcelImp {
    List<Map<String, Object>> getExcel(MultipartFile shishi);
}
