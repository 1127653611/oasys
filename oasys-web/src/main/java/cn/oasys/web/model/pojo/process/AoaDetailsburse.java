package cn.oasys.web.model.pojo.process;

import java.util.Date;

public class AoaDetailsburse {
    private Long detailsburseId;

    private String descript;

    private Double detailmoney;

    private Integer invoices;

    private Date produceTime;

    private String subject;

    private Long bursmentId;

    public Long getDetailsburseId() {
        return detailsburseId;
    }

    public void setDetailsburseId(Long detailsburseId) {
        this.detailsburseId = detailsburseId;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript == null ? null : descript.trim();
    }

    public Double getDetailmoney() {
        return detailmoney;
    }

    public void setDetailmoney(Double detailmoney) {
        this.detailmoney = detailmoney;
    }

    public Integer getInvoices() {
        return invoices;
    }

    public void setInvoices(Integer invoices) {
        this.invoices = invoices;
    }

    public Date getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(Date produceTime) {
        this.produceTime = produceTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public Long getBursmentId() {
        return bursmentId;
    }

    public void setBursmentId(Long bursmentId) {
        this.bursmentId = bursmentId;
    }

    @Override
    public String toString() {
        return "AoaDetailsburse{" +
                "detailsburseId=" + detailsburseId +
                ", descript='" + descript + '\'' +
                ", detailmoney=" + detailmoney +
                ", invoices=" + invoices +
                ", produceTime=" + produceTime +
                ", subject='" + subject + '\'' +
                ", bursmentId=" + bursmentId +
                '}';
    }
}