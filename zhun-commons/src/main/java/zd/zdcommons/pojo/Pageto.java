package zd.zdcommons.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Pageto {
    private Long iCCount;
    private Long iACount;
    private Long iLCount;
    private List<ResultMessage> resultms;
    private String uId;//文件名
    private HashMap<String, HashMap<String, Object>> areacount;//区域错误计数

    @Override
    public String toString() {
        return "Pageto{" +
                "iCCount=" + iCCount +
                ", iACount=" + iACount +
                ", iLCount=" + iLCount +
                ", resultms=" + resultms +
                ", uId='" + uId + '\'' +
                ", areacount=" + areacount +
                '}';
    }
}
