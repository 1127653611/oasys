package cn.oasys.web.model.pojo.process;

import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.Date;

public class AoaProcessList {
    private Long processId;

    private Date applyTime;

    private Long deeplyId;

    private Date endTime;

    private String processName;

    private Integer procseeDays;

    private Integer isChecked=0;

    private Date startTime;

    private Long statusId;

    private String typeName;

    private Long proFileId;

    private Long processUserId;

    private String shenuser;

    private String processDes;

    private AoaTypeList aoaTypeList;

    private AoaStatusList aoaStatusList;

    private AoaUser user;

    private AoaAttachmentList aoaAttachmentList;

    public AoaAttachmentList getAoaAttachmentList() {
        return aoaAttachmentList;
    }

    public void setAoaAttachmentList(AoaAttachmentList aoaAttachmentList) {
        this.aoaAttachmentList = aoaAttachmentList;
    }

    public AoaUser getUser() {
        return user;
    }

    public void setUser(AoaUser user) {
        this.user = user;
    }

    public AoaTypeList getAoaTypeList() {
        return aoaTypeList;
    }

    public void setAoaTypeList(AoaTypeList aoaTypeList) {
        this.aoaTypeList = aoaTypeList;
    }

    public AoaStatusList getAoaStatusList() {
        return aoaStatusList;
    }

    public void setAoaStatusList(AoaStatusList aoaStatusList) {
        this.aoaStatusList = aoaStatusList;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Long getDeeplyId() {
        return deeplyId;
    }

    public void setDeeplyId(Long deeplyId) {
        this.deeplyId = deeplyId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName == null ? null : processName.trim();
    }

    public Integer getProcseeDays() {
        return procseeDays;
    }

    public void setProcseeDays(Integer procseeDays) {
        this.procseeDays = procseeDays;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Long getProFileId() {
        return proFileId;
    }

    public void setProFileId(Long proFileId) {
        this.proFileId = proFileId;
    }

    public Long getProcessUserId() {
        return processUserId;
    }

    public void setProcessUserId(Long processUserId) {
        this.processUserId = processUserId;
    }

    public String getShenuser() {
        return shenuser;
    }

    public void setShenuser(String shenuser) {
        this.shenuser = shenuser == null ? null : shenuser.trim();
    }

    public String getProcessDes() {
        return processDes;
    }

    public void setProcessDes(String processDes) {
        this.processDes = processDes == null ? null : processDes.trim();
    }

    @Override
    public String toString() {
        return "AoaProcessList{" +
                "processId=" + processId +
                ", applyTime=" + applyTime +
                ", deeplyId=" + deeplyId +
                ", endTime=" + endTime +
                ", processName='" + processName + '\'' +
                ", procseeDays=" + procseeDays +
                ", isChecked=" + isChecked +
                ", startTime=" + startTime +
                ", statusId=" + statusId +
                ", typeName='" + typeName + '\'' +
                ", proFileId=" + proFileId +
                ", processUserId=" + processUserId +
                ", shenuser='" + shenuser + '\'' +
                ", processDes='" + processDes + '\'' +
                '}';
    }
}