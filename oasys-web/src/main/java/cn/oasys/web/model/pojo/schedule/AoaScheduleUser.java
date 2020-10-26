package cn.oasys.web.model.pojo.schedule;

public class AoaScheduleUser {
    private Long rcid;

    private Long userId;

    private AoaScheduleList aoaScheduleList;

    public AoaScheduleList getAoaScheduleList() {
        return aoaScheduleList;
    }

    public void setAoaScheduleList(AoaScheduleList aoaScheduleList) {
        this.aoaScheduleList = aoaScheduleList;
    }

    public Long getRcid() {
        return rcid;
    }

    public void setRcid(Long rcid) {
        this.rcid = rcid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}