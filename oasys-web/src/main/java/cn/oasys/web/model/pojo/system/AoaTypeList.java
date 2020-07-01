package cn.oasys.web.model.pojo.system;

public class AoaTypeList {

    private Long typeId;

    private String typeColor;

    private String typeModel;

    private String typeName;

    private Integer sortValue;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }

    public String getTypeModel() {
        return typeModel;
    }

    public void setTypeModel(String typeModel) {
        this.typeModel = typeModel == null ? null : typeModel.trim();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Integer getSortValue() {
        return sortValue;
    }

    public void setSortValue(Integer sortValue) {
        this.sortValue = sortValue;
    }

    @Override
    public String toString() {
        return "AoaTypeList{" +
                "typeId=" + typeId +
                ", typeColor='" + typeColor + '\'' +
                ", typeModel='" + typeModel + '\'' +
                ", typeName='" + typeName + '\'' +
                ", sortValue=" + sortValue +
                '}';
    }
}