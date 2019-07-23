package zd.zdanalysis.mapper;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import zd.zdanalysis.pojo.Category;

/**
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/19 10:50
 */
@Repository
@org.apache.ibatis.annotations.Mapper
public interface ItemMapper extends Mapper<Category> {

}
