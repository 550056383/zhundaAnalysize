package zd.zdanalysis.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import zd.zdanalysis.pojo.ProjectInfo;
import zd.zdcommons.pojo.Majors;
import zd.zdcommons.pojo.TableRelevance;
import zd.zdcommons.pojo.Vice;
import zd.zdcommons.pojo.Write;

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
    //查询是否存在该表名
    String selectTableByName(@Param("tablename") String name);

    //删除临时表
    void deleteTableByName(@Param("name") String name);
    //动态创建临时表
    void createTables(String table, String[] cloums);

    //查询临时表中的数据
    List<Map<String,Object>>  selectResult(@Param("tables") String table);

    //插入数据到临时表
    void insetDatas(String table, String string);

      int selectResults(@Param("tables") String table);

    //关联设置项表查询
    List<Map<String, Object>> selectTables(String s0, String s1, String s2, String s3);

    //输出表字段,多个字段来自多个表
    List<String> selectTableDada(@Param("write") Write write);

    //输出表字段,多个字段来自同一个表
    List<Map<String, Object>> selectTableDada2(@Param("writeList") List<Write> writeList);

    List<Map<String, Object>> selectByStatement(@Param("name") String str0, @Param("tag") int tag, @Param("tableRelevance") List<TableRelevance> tableRelevances, @Param("writeList") List<Write> writeList, @Param("majorsList") List<Majors> mapList, @Param("viceList") List<Vice> viceList);

}
