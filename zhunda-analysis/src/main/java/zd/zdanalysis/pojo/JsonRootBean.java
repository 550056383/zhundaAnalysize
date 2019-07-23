package zd.zdanalysis.pojo;

import lombok.Data;

import java.util.List;

/**
 * 类描述 :
 *
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/22 14:02
 */
@Data
public class JsonRootBean {

    private int num;
    private List<List<String>> rules;
    private String name;
    private String primarykey;
    ;
}
