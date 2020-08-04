package cn.oasys.web.model.pojo.file;

import java.util.List;

public class AoaFilePath {
    private Long pathId;

    private Long parentId;

    private String pathName;

    private Long pathUserId;

    private Long pathIstrash;

    private List<AoaFilePath> childpath;

    public List<AoaFilePath> getChildpath() {
        return childpath;
    }

    public void setChildpath(List<AoaFilePath> childpath) {
        this.childpath = childpath;
    }

    private List<AoaFileList> fileLists;

    public List<AoaFileList> getFileLists() {
        return fileLists;
    }

    public void setFileLists(List<AoaFileList> fileLists) {
        this.fileLists = fileLists;
    }

    public Long getPathId() {
        return pathId;
    }

    public void setPathId(Long pathId) {
        this.pathId = pathId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName == null ? null : pathName.trim();
    }

    public Long getPathUserId() {
        return pathUserId;
    }

    public void setPathUserId(Long pathUserId) {
        this.pathUserId = pathUserId;
    }

    public Long getPathIstrash() {
        return pathIstrash;
    }

    public void setPathIstrash(Long pathIstrash) {
        this.pathIstrash = pathIstrash;
    }

    @Override
    public String toString() {
        return "AoaFilePath{" +
                "pathId=" + pathId +
                ", parentId=" + parentId +
                ", pathName='" + pathName + '\'' +
                ", pathUserId=" + pathUserId +
                ", pathIstrash=" + pathIstrash +
                ", childpath=" + childpath +
                ", fileLists=" + fileLists +
                '}';
    }
}