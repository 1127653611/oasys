package cn.oasys.web.model.pojo.user;

import org.apache.catalina.User;

import java.util.Date;

public class AoaUserLoginRecord {
    private Long recordId;

    private String browser;

    private String ipAddr;

    private Date loginTime;

    private String sessionId;

    private Long userId;

    private AoaUser aoaUser;

    public AoaUser getAoaUser() {
        return aoaUser;
    }

    public void setAoaUser(AoaUser aoaUser) {
        this.aoaUser = aoaUser;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser == null ? null : browser.trim();
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AoaUserLoginRecord() {
    }

    public AoaUserLoginRecord( String browser, String ipAddr, Date loginTime, String sessionId, Long userId) {
        this.recordId = recordId;
        this.browser = browser;
        this.ipAddr = ipAddr;
        this.loginTime = loginTime;
        this.sessionId = sessionId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AoaUserLoginRecord{" +
                "recordId=" + recordId +
                ", browser='" + browser + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", loginTime=" + loginTime +
                ", sessionId='" + sessionId + '\'' +
                ", userId=" + userId +
                ", aoaUser=" + aoaUser +
                '}';
    }
}