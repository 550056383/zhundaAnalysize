package zd.zdcommons.serviceImp;

import zd.zdcommons.pojo.ResultMessage;

import java.util.List;
import java.util.Map;

public interface AnalysisImp {
    //实现实施
    ResultMessage getIntegrityAnalysis(Map<String, Object> resource, Map<String, Object> titleMap);
    ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, Object>> lis);
}
