package cn.oasys.web.model.pojo.discuss;

import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.Date;
import java.util.List;

public class AoaDiscussList {
    private Long discussId;

    private Integer attachmentId;

    private Date createTime;

    private Long statusId;

    private String title;

    private Long typeId;

    private Integer visitNum;

    private Long discussUserId;

    private Long voteId;

    private Date modifyTime;

    private String content;

    private AoaUser aoaUser;

    private AoaVoteList aoaVoteList;

    private List<AoaLoveDiscussUser> aoaLoveDiscussUsers;

    private List<AoaReplyList> aoaReplyLists;

    public AoaUser getAoaUser() {
        return aoaUser;
    }

    public void setAoaUser(AoaUser aoaUser) {
        this.aoaUser = aoaUser;
    }

    public AoaVoteList getAoaVoteList() {
        return aoaVoteList;
    }

    public void setAoaVoteList(AoaVoteList aoaVoteList) {
        this.aoaVoteList = aoaVoteList;
    }

    public List<AoaReplyList> getAoaReplyLists() {
        return aoaReplyLists;
    }

    public void setAoaReplyLists(List<AoaReplyList> aoaReplyLists) {
        this.aoaReplyLists = aoaReplyLists;
    }

    public List<AoaLoveDiscussUser> getAoaLoveDiscussUsers() {
        return aoaLoveDiscussUsers;
    }

    public void setAoaLoveDiscussUsers(List<AoaLoveDiscussUser> aoaLoveDiscussUsers) {
        this.aoaLoveDiscussUsers = aoaLoveDiscussUsers;
    }
    public Long getDiscussId() {
        return discussId;
    }

    public void setDiscussId(Long discussId) {
        this.discussId = discussId;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }

    public Long getDiscussUserId() {
        return discussUserId;
    }

    public void setDiscussUserId(Long discussUserId) {
        this.discussUserId = discussUserId;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }



    @Override
    public String toString() {
        return "AoaDiscussList{" +
                "discussId=" + discussId +
                ", attachmentId=" + attachmentId +
                ", createTime=" + createTime +
                ", statusId=" + statusId +
                ", title='" + title + '\'' +
                ", typeId=" + typeId +
                ", visitNum=" + visitNum +
                ", discussUserId=" + discussUserId +
                ", voteId=" + voteId +
                ", modifyTime=" + modifyTime +
                ", content='" + content + '\'' +
                ", aoaUser=" + aoaUser +
                ", aoaVoteList=" + aoaVoteList +
                ", aoaReplyLists=" + aoaReplyLists +
                '}';
    }
}