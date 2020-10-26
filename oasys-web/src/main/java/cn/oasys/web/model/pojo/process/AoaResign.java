package cn.oasys.web.model.pojo.process;

import cn.oasys.web.model.pojo.user.AoaUser;

public class AoaResign {
    private Long resignId;

    private String financialAdvice;

    private Boolean isFinish;

    private String nofinish;

    private String personnelAdvice;

    private String suggest;

    private Long handUser;

    private Long proId;

    private String managerAdvice;

    private String nameuser;

    private AoaProcessList aoaProcessList;

    private AoaUser user;

    public AoaUser getUser() {
        return user;
    }

    public void setUser(AoaUser user) {
        this.user = user;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public AoaProcessList getAoaProcessList() {
        return aoaProcessList;
    }

    public void setAoaProcessList(AoaProcessList aoaProcessList) {
        this.aoaProcessList = aoaProcessList;
    }

    public Long getResignId() {
        return resignId;
    }

    public void setResignId(Long resignId) {
        this.resignId = resignId;
    }

    public String getFinancialAdvice() {
        return financialAdvice;
    }

    public void setFinancialAdvice(String financialAdvice) {
        this.financialAdvice = financialAdvice == null ? null : financialAdvice.trim();
    }

    public Boolean getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Boolean isFinish) {
        this.isFinish = isFinish;
    }

    public String getNofinish() {
        return nofinish;
    }

    public void setNofinish(String nofinish) {
        this.nofinish = nofinish == null ? null : nofinish.trim();
    }

    public String getPersonnelAdvice() {
        return personnelAdvice;
    }

    public void setPersonnelAdvice(String personnelAdvice) {
        this.personnelAdvice = personnelAdvice == null ? null : personnelAdvice.trim();
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest == null ? null : suggest.trim();
    }

    public Long getHandUser() {
        return handUser;
    }

    public void setHandUser(Long handUser) {
        this.handUser = handUser;
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


}