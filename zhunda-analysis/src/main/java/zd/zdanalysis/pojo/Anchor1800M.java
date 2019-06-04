package zd.zdanalysis.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Anchor1800M {
    private Date deliveryDateFDD; //交优接收日期
    private String programNumberFDD;//规划编号
    private Date constructionPlanFDD;//施工计划
    private String questionClassificationFDD;//问题分类
    private String whetherPlanningFDD;//FDD1800是否规划
    private Date arrivalDateFDD;//到货日期
    private Date deliveryCompletionDateFFD;//交优完成日期
    private Date installationFDD;//FDD1800安装
    private Date openedFDD;//FDD1800开通
    private String nmNEIDFDD;//NM NE ID
    private String baseStationNameFDD;//FDD1800网管基站名称

    @Override
    public String toString() {
        return "Anchor1800M{" +
                "deliveryDateFDD=" + deliveryDateFDD +
                ", programNumberFDD='" + programNumberFDD + '\'' +
                ", constructionPlanFDD=" + constructionPlanFDD +
                ", questionClassificationFDD='" + questionClassificationFDD + '\'' +
                ", whetherPlanningFDD='" + whetherPlanningFDD + '\'' +
                ", arrivalDateFDD=" + arrivalDateFDD +
                ", deliveryCompletionDateFFD=" + deliveryCompletionDateFFD +
                ", installationFDD=" + installationFDD +
                ", openedFDD=" + openedFDD +
                ", nmNEIDFDD='" + nmNEIDFDD + '\'' +
                ", baseStationNameFDD='" + baseStationNameFDD + '\'' +
                '}';
    }
}
