package zd.zdcommons.analysis;

import zd.zdcommons.pojo.ResultMessage;
import zd.zdcommons.serviceImp.AnalysisImp;

import java.util.List;
import java.util.Map;

public class ExistAnalysis implements AnalysisImp {
    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> resource, Map<String, Object> titleMap) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, String> resource) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, Object>> lis, String strTitle) {
        return null;
    }

    @Override
    public ResultMessage getIntegrityAnalysis(Map<String, Object> map, List<Map<String, String>> lis) {
        return null;
    }
}
