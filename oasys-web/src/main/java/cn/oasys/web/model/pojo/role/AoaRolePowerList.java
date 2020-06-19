package cn.oasys.web.model.pojo.role;

public class AoaRolePowerList {
    private Long pkId;

    private Integer isShow;

    private Long menuId;

    private Long roleId;

    public Long getPkId() {
        return pkId;
    }

    public void setPkId(Long pkId) {
        this.pkId = pkId;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public AoaRolePowerList(Integer isShow, Long menuId, Long roleId) {
        this.isShow = isShow;
        this.menuId = menuId;
        this.roleId = roleId;
    }

    public AoaRolePowerList() {
    }

    @Override
    public String toString() {
        return "AoaRolePoweiList{" +
                "pkId=" + pkId +
                ", isShow=" + isShow +
                ", menuId=" + menuId +
                ", roleId=" + roleId +
                '}';
    }
}