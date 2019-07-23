package zd.zdanalysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import zd.zdanalysis.mapper.BrandMapper;
import zd.zdanalysis.mapper.ItemMapper;
import zd.zdanalysis.pojo.Brand;
import zd.zdanalysis.pojo.Category;

import java.util.List;

/**
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/19 10:46
 */
@Service
public class ItemService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private BrandMapper brandMapper;
    //查询分类信息
    public List<Category>  findCategory(Integer pid){
        Example example=new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isParent",pid);
        List<Category> list = itemMapper.selectByExample(example);
        return list;
    }

    //查询所有品牌信息
    public  List<Brand> findAllBrand(){
        List<Brand> brands = brandMapper.selectAll();
        return brands;
    }
    //通过品牌id删除
    public void deleteById(Integer id){
        brandMapper.deleteByPrimaryKey(id);
    }
}
