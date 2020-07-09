package cn.oasys.web.model.pojo.discuss;

import cn.oasys.web.model.pojo.user.AoaUser;

public class AoaVoteTitleUser {
    private Long voteTitleUserId;

    private Long voteId;

    private Long userId;

    private Long titleId;

    private AoaUser aoaUser;

    public AoaVoteTitleUser(Long voteId, Long userId, Long titleId) {
        this.voteId = voteId;
        this.userId = userId;
        this.titleId = titleId;
    }

    public AoaVoteTitleUser() {
    }

    public AoaUser getAoaUser() {
        return aoaUser;
    }

    public void setAoaUser(AoaUser aoaUser) {
        this.aoaUser = aoaUser;
    }

    public Long getVoteTitleUserId() {
        return voteTitleUserId;
    }

    public void setVoteTitleUserId(Long voteTitleUserId) {
        this.voteTitleUserId = voteTitleUserId;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTitleId() {
        return titleId;
    }

    public void setTitleId(Long titleId) {
        this.titleId = titleId;
    }

    @Override
    public String toString() {
        return "AoaVoteTitleUser{" +
                "voteTitleUserId=" + voteTitleUserId +
                ", voteId=" + voteId +
                ", userId=" + userId +
                ", titleId=" + titleId +
                ", aoaUser=" + aoaUser +
                '}';
    }
}