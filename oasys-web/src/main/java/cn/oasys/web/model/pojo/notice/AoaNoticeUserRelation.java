package cn.oasys.web.model.pojo.notice;

import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.List;

public class AoaNoticeUserRelation {
    private Long relatinId;

    private Integer isRead;

    private Long relatinNoticeId;

    private Long relatinUserId;

    private AoaNoticeList aoaNoticeList;

    private AoaUser user;

    public AoaUser getUser() {
        return user;
    }

    public void setUser(AoaUser user) {
        this.user = user;
    }

    public AoaNoticeList getAoaNoticeList() {
        return aoaNoticeList;
    }

    public void setAoaNoticeList(AoaNoticeList aoaNoticeList) {
        this.aoaNoticeList = aoaNoticeList;
    }

    public Long getRelatinId() {
        return relatinId;
    }

    public void setRelatinId(Long relatinId) {
        this.relatinId = relatinId;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Long getRelatinNoticeId() {
        return relatinNoticeId;
    }

    public void setRelatinNoticeId(Long relatinNoticeId) {
        this.relatinNoticeId = relatinNoticeId;
    }

    public Long getRelatinUserId() {
        return relatinUserId;
    }

    public void setRelatinUserId(Long relatinUserId) {
        this.relatinUserId = relatinUserId;
    }

    public AoaNoticeUserRelation(Integer isRead, Long relatinNoticeId, Long relatinUserId) {
        this.isRead = isRead;
        this.relatinNoticeId = relatinNoticeId;
        this.relatinUserId = relatinUserId;
    }

    public AoaNoticeUserRelation() {
    }
}