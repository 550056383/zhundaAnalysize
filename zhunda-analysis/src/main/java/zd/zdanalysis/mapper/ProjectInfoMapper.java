package zd.zdanalysis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.spring.annotation.MapperScan;
import zd.zdanalysis.pojo.ProjectInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/15 15:27
 */
@org.apache.ibatis.annotations.Mapper
public interface ProjectInfoMapper extends Mapper<ProjectInfo> {
    //动态创建临时表
    void createTables(String table, String[] cloums);

    //查询临时表中的数据
    List<Map<String,String>>   selectResult(@Param("tables") String table);

    //插入数据到临时表
    void insetData(String table,String[] array,Map<Integer,String> map);

      int selectResults(@Param("tables") String table);
}
