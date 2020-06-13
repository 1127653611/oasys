package cn.oasys.web.model.pojo.notice;

import java.util.List;

public class AoaNoticeUserRelation {
    private Long relatinId;

    private Integer isRead;

    private Long relatinNoticeId;

    private Long relatinUserId;

    private List<AoaNoticeList> aoaNoticeLists;

    @Override
    public String toString() {
        return "AoaNoticeUserRelation{" +
                "relatinId=" + relatinId +
                ", isRead=" + isRead +
                ", relatinNoticeId=" + relatinNoticeId +
                ", relatinUserId=" + relatinUserId +
                ", aoaNoticeLists=" + aoaNoticeLists +
                '}';
    }

    public List<AoaNoticeList> getAoaNoticeLists() {
        return aoaNoticeLists;
    }

    public void setAoaNoticeLists(List<AoaNoticeList> aoaNoticeLists) {
        this.aoaNoticeLists = aoaNoticeLists;
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
}