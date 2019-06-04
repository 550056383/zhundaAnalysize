package zd.zdcommons.serviceImp;

import zd.zdcommons.pojo.ResultMessage;

import java.util.Map;

public interface AnalysisImp {
    ResultMessage getIntegrityAnalysis(Map<String, Object> resource,Map<String,Object> titleMap);
}
