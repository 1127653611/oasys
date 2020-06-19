package cn.oasys.web.model.pojo.user;

public class AoaPosition {
    private Long positionId;

    private Integer level;

    private String name;

    private String describtion;

    private Long deptid;

    public AoaPosition() {
    }

    public AoaPosition(String name, Long deptid) {
        this.name = name;
        this.deptid = deptid;
    }

    public AoaPosition(Integer level, String name, String describtion, Long deptid) {
        this.level = level;
        this.name = name;
        this.describtion = describtion;
        this.deptid = deptid;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion == null ? null : describtion.trim();
    }

    public Long getDeptid() {
        return deptid;
    }

    public void setDeptid(Long deptid) {
        this.deptid = deptid;
    }

    @Override
    public String toString() {
        return "AoaPosition{" +
                "positionId=" + positionId +
                ", level=" + level +
                ", name='" + name + '\'' +
                ", describtion='" + describtion + '\'' +
                ", deptid=" + deptid +
                '}';
    }
}