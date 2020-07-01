package cn.oasys.web.model.pojo.discuss;

import cn.oasys.web.model.pojo.user.AoaUser;
import org.apache.ibatis.type.Alias;

@Alias("AoaLoveDiscussUser")
public class AoaLoveDiscussUser {
    private Long discussId;

    private Long userId;

    private AoaUser aoaUser;

    public AoaUser getAoaUser() {
        return aoaUser;
    }

    public void setAoaUser(AoaUser aoaUser) {
        this.aoaUser = aoaUser;
    }

    public Long getDiscussId() {
        return discussId;
    }

    public void setDiscussId(Long discussId) {
        this.discussId = discussId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}