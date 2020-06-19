package cn.oasys.web.model.pojo.process;

public class AoaSubject {
    private Long subjectId;

    private String name;

    private Long parentId;

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "AoaSubject{" +
                "subjectId=" + subjectId +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}