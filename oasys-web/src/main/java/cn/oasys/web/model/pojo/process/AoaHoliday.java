package cn.oasys.web.model.pojo.process;

public class AoaHoliday {
    private Long holidayId;

    private Integer leaveDays;

    private Long typeId;

    private Long proId;

    private String managerAdvice;

    private String personnelAdvice;

    private String nameuser;

    private AoaProcessList aoaProcessList;

    public AoaProcessList getAoaProcessList() {
        return aoaProcessList;
    }

    public void setAoaProcessList(AoaProcessList aoaProcessList) {
        this.aoaProcessList = aoaProcessList;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public Long getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(Long holidayId) {
        this.holidayId = holidayId;
    }

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public String getManagerAdvice() {
        return managerAdvice;
    }

    public void setManagerAdvice(String managerAdvice) {
        this.managerAdvice = managerAdvice == null ? null : managerAdvice.trim();
    }

    public String getPersonnelAdvice() {
        return personnelAdvice;
    }

    public void setPersonnelAdvice(String personnelAdvice) {
        this.personnelAdvice = personnelAdvice == null ? null : personnelAdvice.trim();
    }
}