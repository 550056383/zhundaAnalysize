package zd.zdanalysis.pojo;

import lombok.Data;

/**
 * @ClassName Associa
 * @Author chenkun
 * @TIME 2019/7/19 0019-0:08
 */
@Data
public class Associa {
    private String table1;
    private String title1;
    private String table2;
    private String title2;
    @Override
    public String toString() {
        return "Associa{" +
                "table1='" + table1 + '\'' +
                ", title1='" + title1 + '\'' +
                ", table2='" + table2 + '\'' +
                ", title2='" + title2 + '\'' +
                '}';
    }
}
