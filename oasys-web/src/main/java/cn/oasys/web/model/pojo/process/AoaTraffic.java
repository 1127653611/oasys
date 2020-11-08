package cn.oasys.web.model.pojo.process;

import cn.oasys.web.model.pojo.user.AoaUser;

import java.util.Date;

public class AoaTraffic {
    private Long trafficId;

    private String departName;

    private Date departTime;

    private String reachName;

    private String seatType;

    private Double trafficMoney;

    private String trafficName;

    private Long evectionId;

    private Long userName;

    private String username;

    private AoaUser user;

    public AoaUser getUser() {
        return user;
    }

    public void setUser(AoaUser user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTrafficId() {
        return trafficId;
    }

    public void setTrafficId(Long trafficId) {
        this.trafficId = trafficId;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName == null ? null : departName.trim();
    }

    public Date getDepartTime() {
        return departTime;
    }

    public void setDepartTime(Date departTime) {
        this.departTime = departTime;
    }

    public String getReachName() {
        return reachName;
    }

    public void setReachName(String reachName) {
        this.reachName = reachName == null ? null : reachName.trim();
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType == null ? null : seatType.trim();
    }

    public Double getTrafficMoney() {
        return trafficMoney;
    }

    public void setTrafficMoney(Double trafficMoney) {
        this.trafficMoney = trafficMoney;
    }

    public String getTrafficName() {
        return trafficName;
    }

    public void setTrafficName(String trafficName) {
        this.trafficName = trafficName == null ? null : trafficName.trim();
    }

    public Long getEvectionId() {
        return evectionId;
    }

    public void setEvectionId(Long evectionId) {
        this.evectionId = evectionId;
    }

    public Long getUserName() {
        return userName;
    }

    public void setUserName(Long userName) {
        this.userName = userName;
    }
}