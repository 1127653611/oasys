package cn.oasys.web.model.pojo.system;

import cn.oasys.web.model.pojo.role.AoaRolePowerList;

public class AoaSysMenu {
    private Long menuId;

    private Integer isShow;

    private Integer menuGrade;

    private String menuIcon;

    private String menuName;

    private String menuUrl;

    private Long parentId;

    private Integer sortId;
    private AoaRolePowerList aoaRolePowerList;

    @Override
    public String toString() {
        return "AoaSysMenu{" +
                "menuId=" + menuId +
                ", isShow=" + isShow +
                ", menuGrade=" + menuGrade +
                ", menuIcon='" + menuIcon + '\'' +
                ", menuName='" + menuName + '\'' +
                ", menuUrl='" + menuUrl + '\'' +
                ", parentId=" + parentId +
                ", sortId=" + sortId +
                ", aoaRolePoweiList=" + aoaRolePowerList +
                '}';
    }

    public AoaRolePowerList getAoaRolePowerList() {
        return aoaRolePowerList;
    }

    public void setAoaRolePowerList(AoaRolePowerList aoaRolePowerList) {
        this.aoaRolePowerList = aoaRolePowerList;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getMenuGrade() {
        return menuGrade;
    }

    public void setMenuGrade(Integer menuGrade) {
        this.menuGrade = menuGrade;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon == null ? null : menuIcon.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }
}