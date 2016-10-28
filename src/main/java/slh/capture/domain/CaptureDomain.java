package slh.capture.domain;

import slh.capture.common.DataGridModel;

public class CaptureDomain {

  private Integer       id;

  private Integer       userId;

  private String        userName;

  private Integer       appId;

  private String        appName;

  private String        channelName;

  private Integer       newsUserAmount;

  private String        statisDate;

  private DataGridModel pageInfo;

  private String        randomCode;

  public String getRandomCode() {
    return randomCode;
  }

  public void setRandomCode(String randomCode) {
    this.randomCode = randomCode;
  }

  public DataGridModel getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(DataGridModel pageInfo) {
    this.pageInfo = pageInfo;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getAppId() {
    return appId;
  }

  public void setAppId(Integer appId) {
    this.appId = appId;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }

  public Integer getNewsUserAmount() {
    return newsUserAmount;
  }

  public void setNewsUserAmount(Integer newsUserAmount) {
    this.newsUserAmount = newsUserAmount;
  }

  public String getStatisDate() {
    return statisDate;
  }

  public void setStatisDate(String statisDate) {
    this.statisDate = statisDate;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}
