package cn.oasys.web.model.pojo.schedule;

import cn.oasys.web.model.pojo.system.AoaStatusList;
import cn.oasys.web.model.pojo.system.AoaTypeList;
import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.Date;

public class AoaScheduleList {
    private Long rcId;

    private Date createTime;

    private Date endTime;

    private String filedescribe;

    private Integer isRemind;

    private Date startTime;

    private Long statusId;

    private String title;

    private Long typeId;

    private Long userId;

    private String miaoshu;

    private Integer isreminded;

    private AoaStatusList aoaStatusList;

    private AoaTypeList aoaTypeList;

    private AoaUser user;

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

    public Long getRcId() {
        return rcId;
    }

    public void setRcId(Long rcId) {
        this.rcId = rcId;
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

    public String getFiledescribe() {
        return filedescribe;
    }

    public void setFiledescribe(String filedescribe) {
        this.filedescribe = filedescribe == null ? null : filedescribe.trim();
    }

    public Integer getIsRemind() {
        return isRemind;
    }

    public void setIsRemind(Integer isRemind) {
        this.isRemind = isRemind;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMiaoshu() {
        return miaoshu;
    }

    public void setMiaoshu(String miaoshu) {
        this.miaoshu = miaoshu == null ? null : miaoshu.trim();
    }

    public Integer getIsreminded() {
        return isreminded;
    }

    public void setIsreminded(Integer isreminded) {
        this.isreminded = isreminded;
    }

    @Override
    public String toString() {
        return "AoaScheduleList{" +
                "rcId=" + rcId +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                ", filedescribe='" + filedescribe + '\'' +
                ", isRemind=" + isRemind +
                ", startTime=" + startTime +
                ", statusId=" + statusId +
                ", title='" + title + '\'' +
                ", typeId=" + typeId +
                ", userId=" + userId +
                ", miaoshu='" + miaoshu + '\'' +
                ", isreminded=" + isreminded +
                ", aoaStatusList=" + aoaStatusList +
                ", aoaTypeList=" + aoaTypeList +
                '}';
    }
}