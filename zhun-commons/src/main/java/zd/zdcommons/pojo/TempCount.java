package zd.zdcommons.pojo;

import lombok.Data;

/**
 * @ClassName TempCount
 * @Author chenkun
 * @TIME 2019/6/26 0026-13:40
 */
@Data
public class TempCount {
    private Long count5g;
    private Long count3d;
    private Long count1800fdd;

    @Override
    public String toString() {
        return "TempCount{" +
                "count5g=" + count5g +
                ", count3d=" + count3d +
                ", count1800fdd=" + count1800fdd +
                '}';
    }

    public TempCount(Long count5g, Long count3d, Long count1800fdd) {
        this.count5g = count5g;
        this.count3d = count3d;
        this.count1800fdd = count1800fdd;
    }

    public TempCount() {
    }
}
