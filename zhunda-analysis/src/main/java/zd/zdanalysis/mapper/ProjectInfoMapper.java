package zd.zdanalysis.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import zd.zdanalysis.pojo.ProjectInfo;
import zd.zdcommons.pojo.Majors;
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
    void insetData(String table, List<String> list);

    void insetDatas(String table, String string);

      int selectResults(@Param("tables") String table);

    //关联设置项表查询
    List<Map<String, Object>> selectTables(String s0, String s1, String s2, String s3);

    List<String> selectTableCell(String s0, String s1);

    //条件设置
/*    List<Map<String, Object>> selectByWriteRules( Map<String,Object> map);*/
    List<Map<String, Object>> selectByWriteRules(String s0, String s1, String s2, String s3, @Param("writeList") List<Write> writeList, @Param("tags") int tags, @Param("majorsList") List<Majors> mapList, @Param("viceList") List<Vice> viceList, @Param("num") int num, @Param("tag") int tag);
}
