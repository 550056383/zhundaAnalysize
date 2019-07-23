package zd.zdanalysis.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zd.zdanalysis.mapper.BrandMapper;
import zd.zdanalysis.pojo.Brand;
import zd.zdanalysis.pojo.Category;
import zd.zdanalysis.service.ItemService;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.util.List;

/**
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/19 10:35
 */
@Controller
@RequestMapping("api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;
    /**
     *方法描述 : 查询分类信息
     * @author Jack Chen
     * @date 2019/7/19 16:06
     * @param pid
     * @param
     * @return java.lang.String
     **/
    @ResponseBody
    @RequestMapping("/category/list")
    public String findCategory(@Param("pid") Integer pid ) throws Exception{
        List<Category> all = itemService.findCategory(pid);
        ObjectMapper mapper=new ObjectMapper();
        String string = mapper.writeValueAsString(all);
       /* JSONObject object=JSONObject.fromObject(all);
       JSONObject.toBean(, )*/
        return string;
    }
    /**
     *方法描述 : 查询所有品牌信息
     * @author Jack Chen
     * @date 2019/7/19 16:05
     * @param
     * @return java.lang.String
     **/
    @ResponseBody
    @RequestMapping("brand/findAll")
    public String findAllBrand() throws Exception{
        List<Brand> allBrand = itemService.findAllBrand();
        ObjectMapper mapper=new ObjectMapper();
        String string = mapper.writeValueAsString(allBrand);
        return string;
    }
    /**
     *方法描述 : 通过品牌id删除
     * @author Jack Chen
     * @date 2019/7/19 16:03
     * @param
     * @return void
     **/
    @DeleteMapping("/brand")
    public void deleteById(@Param("id") Integer id){
        System.out.println("id是:"+id);
        itemService.deleteById(id);
        System.out.println("删除完成.......");
    }
    
    /**
     *方法描述 :
     * @author Jack Chen
     * @date 2019/7/19 16:51 
     * @param  
     * @return void
     **/
    @RequestMapping("upload")
    public void uploadImg(){
        
    }
}
