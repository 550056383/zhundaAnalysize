package zd.zdcommons.pojo;

import lombok.Data;

/**
 * @ClassName ErrorCount
 * @Author chenkun
 * @TIME 2019/6/26 11:01
 */
@Data
public class ErrorCount {
    private Long logic_err;
    private Long complete_err;
    private Long accurate_err;

    @Override
    public String toString() {
        return "ErrorCount{" +
                "logic_err=" + logic_err +
                ", complete_err=" + complete_err +
                ", accurate_err=" + accurate_err +
                '}';
    }
}
