package zd.zdanalysis.pojo;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.command.BaseModel;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;

@Data
@Table(name = "user")  //表名
public class User extends BaseModel {
    /**
     * 属性上的注解定义了创建表时的各个字段的属性
     */
    @Column(name = "id",type = MySqlTypeConstant.INT,length = 11,isKey = true,isAutoIncrement = true)
    private int id;

    @Column(name = "name",type = MySqlTypeConstant.VARCHAR,length = 111)
    private String name;

    @Column(name = "hp",type = MySqlTypeConstant.DOUBLE,length = 16,decimalLength = 2)
    private double hp;

    @Column(name = "damage",type = MySqlTypeConstant.DOUBLE,length = 16,decimalLength = 2)
    private double damage;
}
