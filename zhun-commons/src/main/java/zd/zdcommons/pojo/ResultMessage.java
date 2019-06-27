package zd.zdcommons.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ResultMessage {
    private String tError;
    private String dUID;
    private String dUName;
    private String darea;
    private List<Message>messge;
    private Long xcount;
    private Long count5g;
    private Long count3d;
    private Long count1800fdd;

    @Override
    public String toString() {
        return "ResultMessage{" +
                "tError='" + tError + '\'' +
                ", dUID='" + dUID + '\'' +
                ", dUName='" + dUName + '\'' +
                ", darea='" + darea + '\'' +
                ", messge=" + messge +
                ", xcount=" + xcount +
                ", count5g=" + count5g +
                ", count3d=" + count3d +
                ", count1800fdd=" + count1800fdd +
                '}';
    }
}
