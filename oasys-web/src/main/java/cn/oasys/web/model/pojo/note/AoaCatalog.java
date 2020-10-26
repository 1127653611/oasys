package cn.oasys.web.model.pojo.note;

public class AoaCatalog {
    private Long catalogId;

    private String catalogName;

    private Integer parentId;

    private Long cataUserId;

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName == null ? null : catalogName.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Long getCataUserId() {
        return cataUserId;
    }

    public void setCataUserId(Long cataUserId) {
        this.cataUserId = cataUserId;
    }

    public AoaCatalog(String catalogName,  Long cataUserId) {
        this.catalogName = catalogName;
        this.cataUserId = cataUserId;
    }

    public AoaCatalog(String catalogName, Integer parentId, Long cataUserId) {
        this.catalogName = catalogName;
        this.parentId = parentId;
        this.cataUserId = cataUserId;
    }

    public AoaCatalog() {
    }
}