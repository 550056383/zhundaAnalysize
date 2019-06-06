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


    @Override
    public String toString() {
        return "ResultMessage{" +
                "tError='" + tError + '\'' +
                ", dUID='" + dUID + '\'' +
                ", dUName='" + dUName + '\'' +
                ", darea='" + darea + '\'' +
                ", messge=" + messge +
                ", xcount=" + xcount +
                '}';
    }
}
