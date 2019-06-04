package zd.zdanalysis.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class MaterialOnSite {
    private Date baselineEndDate;
    private Date planEndDate;
    private Date actualEndDate;
    private String owner;
    private String approveState;
    private String deliveryAttachmentRequired;
    private String deliveryAttachmentUploaded;
    private String accumulation;
    private String remarks;
    private String delayReason;

    @Override
    public String toString() {
        return "MaterialOnSite{" +
                "baselineEndDate=" + baselineEndDate +
                ", planEndDate=" + planEndDate +
                ", actualEndDate=" + actualEndDate +
                ", owner='" + owner + '\'' +
                ", approveState='" + approveState + '\'' +
                ", deliveryAttachmentRequired='" + deliveryAttachmentRequired + '\'' +
                ", deliveryAttachmentUploaded='" + deliveryAttachmentUploaded + '\'' +
                ", accumulation='" + accumulation + '\'' +
                ", remarks='" + remarks + '\'' +
                ", delayReason='" + delayReason + '\'' +
                '}';
    }
}
