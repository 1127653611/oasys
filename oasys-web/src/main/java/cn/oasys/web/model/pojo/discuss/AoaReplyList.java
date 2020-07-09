package cn.oasys.web.model.pojo.discuss;

import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.Date;
import java.util.List;

public class AoaReplyList {
    private Long replyId;

    private Date replayTime;

    private Long discussId;

    private Long replyUserId;

    private String content;

    private List<AoaLoveUser> aoaLoveUsers;

    private List<AoaCommentList> aoaCommentLists;

    private AoaUser aoaUser;

    private AoaDiscussList aoaDiscussList;

    public AoaReplyList(Long replyId, Date replayTime, Long discussId, Long replyUserId, String content) {
        this.replyId = replyId;
        this.replayTime = replayTime;
        this.discussId = discussId;
        this.replyUserId = replyUserId;
        this.content = content;
    }

    public AoaReplyList() {
    }

    public AoaDiscussList getAoaDiscussList() {
        return aoaDiscussList;
    }

    public void setAoaDiscussList(AoaDiscussList aoaDiscussList) {
        this.aoaDiscussList = aoaDiscussList;
    }

    public AoaUser getAoaUser() {
        return aoaUser;
    }

    public void setAoaUser(AoaUser aoaUser) {
        this.aoaUser = aoaUser;
    }

    public List<AoaCommentList> getAoaCommentLists() {
        return aoaCommentLists;
    }

    public void setAoaCommentLists(List<AoaCommentList> aoaCommentLists) {
        this.aoaCommentLists = aoaCommentLists;
    }

    public List<AoaLoveUser> getAoaLoveUsers() {
        return aoaLoveUsers;
    }

    public void setAoaLoveUsers(List<AoaLoveUser> aoaLoveUsers) {
        this.aoaLoveUsers = aoaLoveUsers;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Date getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(Date replayTime) {
        this.replayTime = replayTime;
    }

    public Long getDiscussId() {
        return discussId;
    }

    public void setDiscussId(Long discussId) {
        this.discussId = discussId;
    }

    public Long getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(Long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}