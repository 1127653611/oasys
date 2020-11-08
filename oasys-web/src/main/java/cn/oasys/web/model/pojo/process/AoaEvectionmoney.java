package cn.oasys.web.model.pojo.process;

import java.util.List;

public class AoaEvectionmoney {
    private Long evectionmoneyId;

    private String financialAdvice;

    private String managerAdvice;

    private Double money;

    private String name;

    private Long proId;

    private Integer pro;

    private String shenname;

    List<AoaTraffic> traffic;

    List<AoaStay> stay;

    public List<AoaStay> getStay() {
        return stay;
    }

    public void setStay(List<AoaStay> stay) {
        this.stay = stay;
    }

    private AoaProcessList aoaProcessList;

    public AoaProcessList getAoaProcessList() {
        return aoaProcessList;
    }

    public void setAoaProcessList(AoaProcessList aoaProcessList) {
        this.aoaProcessList = aoaProcessList;
    }

    public List<AoaTraffic> getTraffic() {
        return traffic;
    }

    public void setTraffic(List<AoaTraffic> traffic) {
        this.traffic = traffic;
    }

    public String getShenname() {
        return shenname;
    }

    public void setShenname(String shenname) {
        this.shenname = shenname;
    }

    public Long getEvectionmoneyId() {
        return evectionmoneyId;
    }

    public void setEvectionmoneyId(Long evectionmoneyId) {
        this.evectionmoneyId = evectionmoneyId;
    }

    public String getFinancialAdvice() {
        return financialAdvice;
    }

    public void setFinancialAdvice(String financialAdvice) {
        this.financialAdvice = financialAdvice == null ? null : financialAdvice.trim();
    }

    public String getManagerAdvice() {
        return managerAdvice;
    }

    public void setManagerAdvice(String managerAdvice) {
        this.managerAdvice = managerAdvice == null ? null : managerAdvice.trim();
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public Integer getPro() {
        return pro;
    }

    public void setPro(Integer pro) {
        this.pro = pro;
    }

}