package com.shsnc.base.user.model;

public class UserInfoModel {

  private Long userId;
  private String username;
  private String passwd;
  private String alias;
  private String mobile;
  private String email;
  private Integer internal;
  private Integer status;
  private Long createTime;
  private Long attemptTime;
  private String attemptIp;
  private Integer isDelete;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getInternal() {
    return internal;
  }

  public void setInternal(Integer internal) {
    this.internal = internal;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public Long getAttemptTime() {
    return attemptTime;
  }

  public void setAttemptTime(Long attemptTime) {
    this.attemptTime = attemptTime;
  }

  public String getAttemptIp() {
    return attemptIp;
  }

  public void setAttemptIp(String attemptIp) {
    this.attemptIp = attemptIp;
  }

  public Integer getIsDelete() {
    return isDelete;
  }

  public void setIsDelete(Integer isDelete) {
    this.isDelete = isDelete;
  }
}
