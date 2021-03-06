package cn.oasys.web.model.pojo.process;

import java.util.Date;

public class AoaReviewed {
    private Long reviewedId;

    private String advice;

    private Date reviewedTime;

    private Long statusId;

    private Long proId;

    private Long userId;

    private Integer del=0;

    private AoaProcessList aoaProcessList;

    public AoaProcessList getAoaProcessList() {
        return aoaProcessList;
    }

    public void setAoaProcessList(AoaProcessList aoaProcessList) {
        this.aoaProcessList = aoaProcessList;
    }

    public Long getReviewedId() {
        return reviewedId;
    }

    public void setReviewedId(Long reviewedId) {
        this.reviewedId = reviewedId;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice == null ? null : advice.trim();
    }

    public Date getReviewedTime() {
        return reviewedTime;
    }

    public void setReviewedTime(Date reviewedTime) {
        this.reviewedTime = reviewedTime;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    @Override
    public String toString() {
        return "AoaReviewed{" +
                "reviewedId=" + reviewedId +
                ", advice='" + advice + '\'' +
                ", reviewedTime=" + reviewedTime +
                ", statusId=" + statusId +
                ", proId=" + proId +
                ", userId=" + userId +
                ", del=" + del +
                '}';
    }
}