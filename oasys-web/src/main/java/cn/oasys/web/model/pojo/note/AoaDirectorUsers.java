package cn.oasys.web.model.pojo.note;

import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.Date;

public class AoaDirectorUsers {
    private Long directorUsersId;

    private String catelogName;

    private Integer isHandle;

    private Long directorId;

    private Long userId;

    private Long shareUserId;

    private Date sharetime;

    private AoaDirector aoaDirector;

    private AoaUser user;

    private AoaUser shareUser;

    public AoaUser getShareUser() {
        return shareUser;
    }

    public void setShareUser(AoaUser shareUser) {
        this.shareUser = shareUser;
    }

    public AoaUser getUser() {
        return user;
    }

    public void setUser(AoaUser user) {
        this.user = user;
    }

    public AoaDirector getAoaDirector() {
        return aoaDirector;
    }

    public AoaDirectorUsers(String catelogName, Long userId) {
        this.catelogName = catelogName;
        this.userId = userId;
    }

    public AoaDirectorUsers() {
    }

    public void setAoaDirector(AoaDirector aoaDirector) {
        this.aoaDirector = aoaDirector;
    }

    public Long getDirectorUsersId() {
        return directorUsersId;
    }

    public void setDirectorUsersId(Long directorUsersId) {
        this.directorUsersId = directorUsersId;
    }

    public String getCatelogName() {
        return catelogName;
    }

    public void setCatelogName(String catelogName) {
        this.catelogName = catelogName;
    }

    public Integer getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(Integer isHandle) {
        this.isHandle = isHandle;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShareUserId() {
        return shareUserId;
    }

    public void setShareUserId(Long shareUserId) {
        this.shareUserId = shareUserId;
    }

    public Date getSharetime() {
        return sharetime;
    }

    public void setSharetime(Date sharetime) {
        this.sharetime = sharetime;
    }

    @Override
    public String toString() {
        return "AoaDirectorUsers{" +
                "directorUsersId=" + directorUsersId +
                ", catelogName=" + catelogName  +
                ", isHandle=" + isHandle +
                ", directorId=" + directorId +
                ", userId=" + userId +
                ", shareUserId=" + shareUserId +
                ", sharetime=" + sharetime +
                ", aoaDirector=" + aoaDirector +
                '}';
    }
}