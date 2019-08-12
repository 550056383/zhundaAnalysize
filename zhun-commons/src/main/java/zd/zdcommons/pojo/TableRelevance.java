package zd.zdcommons.pojo;

import lombok.Data;

/**
 * 类描述 : 将多表关联,表--字段与表---字段进行封装
 *
 * @author Jack Chen
 * @version 1.0
 * @date 2019/8/9 15:16
 */
@Data
public class TableRelevance {
    private String table1;
    private String fields1;
    private String table2;
    private String fields2;

}
