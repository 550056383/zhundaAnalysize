package zd.zdanalysis.pojo;

import lombok.Data;

@Data
public class Implementation {
    private YiDong5G yiDong5G;
    private MIMO3D mimo3D;
    private Anchor1800M anchor1800M;

    @Override
    public String toString() {
        return "Implementation{" +
                "yiDong5G=" + yiDong5G +
                ", mimo3D=" + mimo3D +
                ", anchor1800M=" + anchor1800M +
                '}';
    }
}
