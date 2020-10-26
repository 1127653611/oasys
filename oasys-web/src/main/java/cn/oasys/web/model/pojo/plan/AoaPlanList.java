package cn.oasys.web.model.pojo.plan;

import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.Date;

public class AoaPlanList {
    private Date createTime;

    private Date endTime;

    private String label;

    private String planComment;

    private String planContent;

    private String planSummary;

    private Long statusId;

    private String title;

    private Long typeId;

    private Long planUserId;

    private Long attachId;

    private AoaTypeList aoaTypeList;

    private AoaStatusList aoaStatusList;

    private AoaUser user;

    private Long planId;

    private Date startTime;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public AoaUser getUser() {
        return user;
    }

    public void setUser(AoaUser user) {
        this.user = user;
    }

    public AoaStatusList getAoaStatusList() {
        return aoaStatusList;
    }

    public void setAoaStatusList(AoaStatusList aoaStatusList) {
        this.aoaStatusList = aoaStatusList;
    }

    public AoaTypeList getAoaTypeList() {
        return aoaTypeList;
    }

    public void setAoaTypeList(AoaTypeList aoaTypeList) {
        this.aoaTypeList = aoaTypeList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public String getPlanComment() {
        return planComment;
    }

    public void setPlanComment(String planComment) {
        this.planComment = planComment == null ? null : planComment.trim();
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent == null ? null : planContent.trim();
    }

    public String getPlanSummary() {
        return planSummary;
    }

    public void setPlanSummary(String planSummary) {
        this.planSummary = planSummary == null ? null : planSummary.trim();
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getPlanUserId() {
        return planUserId;
    }

    public void setPlanUserId(Long planUserId) {
        this.planUserId = planUserId;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    @Override
    public String toString() {
        return "AoaPlanList{" +
                "createTime=" + createTime +
                ", endTime=" + endTime +
                ", label='" + label + '\'' +
                ", planComment='" + planComment + '\'' +
                ", planContent='" + planContent + '\'' +
                ", planSummary='" + planSummary + '\'' +
                ", statusId=" + statusId +
                ", title='" + title + '\'' +
                ", typeId=" + typeId +
                ", planUserId=" + planUserId +
                ", attachId=" + attachId +
                ", aoaTypeList=" + aoaTypeList +
                ", aoaStatusList=" + aoaStatusList +
                ", user=" + user +
                ", planId=" + planId +
                ", startTime=" + startTime +
                '}';
    }
}