package cn.oasys.web.model.pojo.discuss;

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