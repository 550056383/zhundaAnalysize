package zd.zdanalysis.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 *类描述: 品牌管理实体类
 * @author Jack Chen
 * @version 1.0
 * @date 2019/7/19 15:09
 */
@Data
public class Brand {
    @Id
    private  Integer id;
    private String name;
    private String image;
    private String letter;
    private String categories;
}
