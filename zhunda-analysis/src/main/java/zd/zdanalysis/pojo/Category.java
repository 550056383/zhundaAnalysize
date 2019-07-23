package zd.zdanalysis.pojo;

import lombok.Data;

/**
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/19 10:15
 */
@Data
public class Category {
    //分类ID
    private Integer id;
    //分类名称
    private String name;
    //父id
    private Integer parentId;
    //是否是父节点
    private  Integer isParent;

}
