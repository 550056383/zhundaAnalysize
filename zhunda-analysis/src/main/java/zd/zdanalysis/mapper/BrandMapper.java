package zd.zdanalysis.mapper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import zd.zdanalysis.pojo.Brand;

/**
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/19 15:28
 */
@Repository
@org.apache.ibatis.annotations.Mapper
public interface BrandMapper extends Mapper<Brand> {
}
