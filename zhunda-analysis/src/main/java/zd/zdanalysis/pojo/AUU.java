package zd.zdanalysis.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class AUU {
    private Date baselineStartDate;
    private Date baselineEndDate;
    private Date planStartDate;
    private Date planEndDate;
    private Date actualStartDate;
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
        return "AUU{" +
                "baselineStartDate=" + baselineStartDate +
                ", baselineEndDate=" + baselineEndDate +
                ", planStartDate=" + planStartDate +
                ", planEndDate=" + planEndDate +
                ", actualStartDate=" + actualStartDate +
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
