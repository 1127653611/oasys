package cn.oasys.web.model.pojo.task;

import java.util.List;

public class AoaTaskUser {
    private Long pkId;

    private Long statusId;

    private Long taskId;

    private Long taskReciveUserId;

    List<AoaTaskList> aoaTaskLists;

    @Override
    public String toString() {
        return "AoaTaskUser{" +
                "pkId=" + pkId +
                ", statusId=" + statusId +
                ", taskId=" + taskId +
                ", taskReciveUserId=" + taskReciveUserId +
                ", aoaTaskLists=" + aoaTaskLists +
                '}';
    }

    public List<AoaTaskList> getAoaTaskLists() {
        return aoaTaskLists;
    }

    public void setAoaTaskLists(List<AoaTaskList> aoaTaskLists) {
        this.aoaTaskLists = aoaTaskLists;
    }

    public Long getPkId() {
        return pkId;
    }

    public void setPkId(Long pkId) {
        this.pkId = pkId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskReciveUserId() {
        return taskReciveUserId;
    }

    public void setTaskReciveUserId(Long taskReciveUserId) {
        this.taskReciveUserId = taskReciveUserId;
    }
}