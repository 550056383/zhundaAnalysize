package zd.zdanalysis.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class MIMO3D {
    private Date miMO3DDate;//交优完成日期
    private String miMO3DID;//规划编号
    private String miMO3DGoodsQuantity;//到货数量
    private Date deliveryDate;//交优接收日期
    private String questionClassification;//问题分类
    private String planningNumber;//规划数量
    private Date installationDate;//安装时间
    private Date completionDate;//开通完成日期
    private String nmNEID;
    private String openTypeStand;//开通站型
    private String openTypeStandTarget;//目标站型（设计）
    private String baseStationName;//基站名称
    private String transmissionBandwidthe4G ;//4G传输带宽
    private String transmissionAvailable4G;//4G传输具备

    @Override
    public String toString() {
        return "MIMO3D{" +
                "miMO3DDate=" + miMO3DDate +
                ", miMO3DID='" + miMO3DID + '\'' +
                ", miMO3DGoodsQuantity='" + miMO3DGoodsQuantity + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", questionClassification='" + questionClassification + '\'' +
                ", planningNumber='" + planningNumber + '\'' +
                ", installationDate=" + installationDate +
                ", completionDate=" + completionDate +
                ", nmNEID='" + nmNEID + '\'' +
                ", openTypeStand='" + openTypeStand + '\'' +
                ", openTypeStandTarget='" + openTypeStandTarget + '\'' +
                ", baseStationName='" + baseStationName + '\'' +
                ", transmissionBandwidthe4G='" + transmissionBandwidthe4G + '\'' +
                ", transmissionAvailable4G='" + transmissionAvailable4G + '\'' +
                '}';
    }
}
