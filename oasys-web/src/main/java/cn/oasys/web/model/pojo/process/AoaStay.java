package cn.oasys.web.model.pojo.process;

import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.Date;

public class AoaStay {
    private Long stayId;

    private Integer day;

    private String hotelName;

    private Date leaveTime;

    private String stayCity;

    private Double stayMoney;

    private Date stayTime;

    private Long evemoneyId;

    private Long userName;

    private String nameuser;

    private AoaUser user;

    public AoaUser getUser() {
        return user;
    }

    public void setUser(AoaUser user) {
        this.user = user;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public Long getStayId() {
        return stayId;
    }

    public void setStayId(Long stayId) {
        this.stayId = stayId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName == null ? null : hotelName.trim();
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getStayCity() {
        return stayCity;
    }

    public void setStayCity(String stayCity) {
        this.stayCity = stayCity == null ? null : stayCity.trim();
    }

    public Double getStayMoney() {
        return stayMoney;
    }

    public void setStayMoney(Double stayMoney) {
        this.stayMoney = stayMoney;
    }

    public Date getStayTime() {
        return stayTime;
    }

    public void setStayTime(Date stayTime) {
        this.stayTime = stayTime;
    }

    public Long getEvemoneyId() {
        return evemoneyId;
    }

    public void setEvemoneyId(Long evemoneyId) {
        this.evemoneyId = evemoneyId;
    }

    public Long getUserName() {
        return userName;
    }

    public void setUserName(Long userName) {
        this.userName = userName;
    }
}