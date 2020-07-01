package cn.oasys.web.model.pojo.discuss;

import java.util.List;

public class AoaVoteTitles {
    private Long titleId;

    private String color;

    private String title;

    private Long voteId;

    private List<AoaVoteTitleUser> aoaVoteTitleUsers;

    @Override
    public String toString() {
        return "AoaVoteTitles{" +
                "titleId=" + titleId +
                ", color='" + color + '\'' +
                ", title='" + title + '\'' +
                ", voteId=" + voteId +
                ", aoaVoteTitleUsers=" + aoaVoteTitleUsers +
                '}';
    }

    public List<AoaVoteTitleUser> getAoaVoteTitleUsers() {
        return aoaVoteTitleUsers;
    }

    public void setAoaVoteTitleUsers(List<AoaVoteTitleUser> aoaVoteTitleUsers) {
        this.aoaVoteTitleUsers = aoaVoteTitleUsers;
    }

    public Long getTitleId() {
        return titleId;
    }

    public void setTitleId(Long titleId) {
        this.titleId = titleId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }
}