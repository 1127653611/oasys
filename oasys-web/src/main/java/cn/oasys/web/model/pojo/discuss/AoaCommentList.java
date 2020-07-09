package cn.oasys.web.model.pojo.discuss;

import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.Date;

public class AoaCommentList {
    private Long commentId;

    private String comment;

    private Date time;

    private Long commentUserId;

    private Long replyId;

    private AoaUser aoaUser;

    public AoaCommentList(Long commentId, String comment, Date time, Long commentUserId, Long replyId) {
        this.commentId = commentId;
        this.comment = comment;
        this.time = time;
        this.commentUserId = commentUserId;
        this.replyId = replyId;
    }

    public AoaCommentList() {
    }

    public AoaUser getAoaUser() {
        return aoaUser;
    }

    public void setAoaUser(AoaUser aoaUser) {
        this.aoaUser = aoaUser;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Long commentUserId) {
        this.commentUserId = commentUserId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }
}