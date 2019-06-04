package zd.zdanalysis.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class YiDong5G {
    private String customerSiteID;
    private String customerSiteName;
    private String dUID;
    private String dUName;
    private String area;//行政区域
    private String Subcontractor;
    private String supervisionUnit;//督导单位
    private SoftwareCommissioning softwareCommissioning;
    private AUU auu;
    private String residentialBroadband;//小区宽带
    private Date transmissionEquipped;//传输设备
    private String deliveryType;
    private String nMNEID;
    private String nROPO;
    private String rruToneNumber;//rru软调数量
    private String rruHardwareNumber;//rru硬件数量
    private String spectrum;//频段
    private String nroServiceContract;//NRO服务合同号
    private String scenario;//场景
    private String auuArrivalQuantity;//到货数量
    private String problemClassification;//5G问题分类
    private ReadyForInstallation readyForInstallation;
    private MaterialOnSite materialOnSite;
    private Date completionDate;//5G交优完成日期
    private Date receptionDate;//5G 交优接收日期
    private InstallationCompleted installationCompleted;
    private String engineeringServiceMode;//工程服务方式
    private String planningNumber;//5G规划编号
    private Date tianmianTransformation;//天面改造进展
    private Date dcFuse;//直流空开熔丝
    private Date acInduction;//交流引入
    private String design;//设计图纸
    private String bbuToneNumber;//BBU软调数量
    private String bbuESN;
    private String bbuSiteID;
    private String bbuSiteName;
    private String bbuScenario;//BBU交付场景
    private String bbuHardwareNumber;//BBU硬件数量
    private String deliveryRegion;
    private String nmNEName;
    private String rruSiteID;
    private String rruSiteName;
    private String rruScenario;//RRU交付场景
    private String rruBoxNo;//RRU框号
    private String standingType;//站型
    private String productModel;//产品型号
    private String contractConnection;//合同挂接方式
    private String remoteStationType;//拉远站类型
    private String standard;//制式
    private String transmissionBandwidth;//5G传输带宽
    private String nroSubcontractor;
    private String standingType2;//站型2

    @Override
    public String toString() {
        return "YiDong5G{" +
                "customerSiteID='" + customerSiteID + '\'' +
                ", customerSiteName='" + customerSiteName + '\'' +
                ", dUID='" + dUID + '\'' +
                ", dUName='" + dUName + '\'' +
                ", area='" + area + '\'' +
                ", Subcontractor='" + Subcontractor + '\'' +
                ", supervisionUnit='" + supervisionUnit + '\'' +
                ", softwareCommissioning=" + softwareCommissioning +
                ", auu=" + auu +
                ", residentialBroadband='" + residentialBroadband + '\'' +
                ", transmissionEquipped=" + transmissionEquipped +
                ", deliveryType='" + deliveryType + '\'' +
                ", nMNEID='" + nMNEID + '\'' +
                ", nROPO='" + nROPO + '\'' +
                ", rruToneNumber='" + rruToneNumber + '\'' +
                ", rruHardwareNumber='" + rruHardwareNumber + '\'' +
                ", spectrum='" + spectrum + '\'' +
                ", nroServiceContract='" + nroServiceContract + '\'' +
                ", scenario='" + scenario + '\'' +
                ", auuArrivalQuantity='" + auuArrivalQuantity + '\'' +
                ", problemClassification='" + problemClassification + '\'' +
                ", readyForInstallation=" + readyForInstallation +
                ", materialOnSite=" + materialOnSite +
                ", completionDate=" + completionDate +
                ", receptionDate=" + receptionDate +
                ", installationCompleted=" + installationCompleted +
                ", engineeringServiceMode='" + engineeringServiceMode + '\'' +
                ", planningNumber='" + planningNumber + '\'' +
                ", tianmianTransformation=" + tianmianTransformation +
                ", dcFuse=" + dcFuse +
                ", acInduction=" + acInduction +
                ", design='" + design + '\'' +
                ", bbuToneNumber='" + bbuToneNumber + '\'' +
                ", bbuESN='" + bbuESN + '\'' +
                ", bbuSiteID='" + bbuSiteID + '\'' +
                ", bbuSiteName='" + bbuSiteName + '\'' +
                ", bbuScenario='" + bbuScenario + '\'' +
                ", bbuHardwareNumber='" + bbuHardwareNumber + '\'' +
                ", deliveryRegion='" + deliveryRegion + '\'' +
                ", nmNEName='" + nmNEName + '\'' +
                ", rruSiteID='" + rruSiteID + '\'' +
                ", rruSiteName='" + rruSiteName + '\'' +
                ", rruScenario='" + rruScenario + '\'' +
                ", rruBoxNo='" + rruBoxNo + '\'' +
                ", standingType='" + standingType + '\'' +
                ", productModel='" + productModel + '\'' +
                ", contractConnection='" + contractConnection + '\'' +
                ", remoteStationType='" + remoteStationType + '\'' +
                ", standard='" + standard + '\'' +
                ", transmissionBandwidth='" + transmissionBandwidth + '\'' +
                ", nroSubcontractor='" + nroSubcontractor + '\'' +
                ", standingType2='" + standingType2 + '\'' +
                '}';
    }
}
