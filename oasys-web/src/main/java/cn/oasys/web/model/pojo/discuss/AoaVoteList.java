package cn.oasys.web.model.pojo.discuss;

import java.util.Date;
import java.util.List;


public class AoaVoteList {
    private Long voteId;

    private Date endTime;

    private Integer selectone;

    private Date startTime;

    private List<AoaVoteTitles> aoaVoteTitles;

    @Override
    public String toString() {
        return "AoaVoteList{" +
                "voteId=" + voteId +
                ", endTime=" + endTime +
                ", selectone=" + selectone +
                ", startTime=" + startTime +
                ", aoaVoteTitles=" + aoaVoteTitles +
                '}';
    }

    public List<AoaVoteTitles> getAoaVoteTitles() {
        return aoaVoteTitles;
    }

    public void setAoaVoteTitles(List<AoaVoteTitles> aoaVoteTitles) {
        this.aoaVoteTitles = aoaVoteTitles;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getSelectone() {
        return selectone;
    }

    public void setSelectone(Integer selectone) {
        this.selectone = selectone;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}