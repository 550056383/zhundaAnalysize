package zd.zdanalysis.pojo;

import lombok.Data;

@Data
public class ProjectInfo {
    //po单号
    private String id;
    //总金额
    private String totalMoney;
    //派工进度
    private String works;
    //实际完工日期
    private String realityDate;
    //汇款金额
    private String remitMoney;
    //办事处
    private String office;
    //收入确认日期
    private String auditDate;
    //项目组归属
    private  String projectGroup;
}
