package zd.zdcommons.pojo;

import lombok.Data;

/**
 * @ClassName ReverseMes
 * @Author chenkun
 * @TIME 2019/6/26 11:04
 */
@Data
public class ReverseMes {
    private String id;
    private String area;
    private ErrorCount error;

    @Override
    public String toString() {
        return "ReverseMes{" +
                "id='" + id + '\'' +
                ", area='" + area + '\'' +
                ", error=" + error +
                '}';
    }

}
