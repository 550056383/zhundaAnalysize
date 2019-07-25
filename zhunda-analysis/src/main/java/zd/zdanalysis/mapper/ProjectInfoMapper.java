package zd.zdanalysis.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import zd.zdanalysis.pojo.ProjectInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/15 15:27
 */
@Repository
@org.apache.ibatis.annotations.Mapper
public interface ProjectInfoMapper extends Mapper<ProjectInfo> {
    //动态创建临时表
    void createTables(String table, String[] cloums);

    //查询临时表中的数据
    List<Map<String,Object>>  selectResult(@Param("tables") String table);

    //插入数据到临时表
    void insetData(String table, List<String> list);

      int selectResults(@Param("tables") String table);

    //关联设置项表查询
    List<Map<String, Object>> selectTables(String s0, String s1, String s2, String s3);

    List<String> selectTableCell(String s0, String s1);

    //条件设置
    List<String> selectByWriteRules(String s0, String s1, String s2, String s3, String s4, String s5);
}
