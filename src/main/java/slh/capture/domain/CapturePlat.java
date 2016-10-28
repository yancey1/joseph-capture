package slh.capture.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import slh.capture.common.DataGridModel;

public class CapturePlat {

  private Integer       id;
  private String        channelCode;
  private String        userName;
  private String        productName;
  private String        password;
  private String        loginUrl;
  private String        loginImgUrl;
  private String        queryPageUrl;
  private String        queryUrl;
  private String        company;
  private DataGridModel pageInfo;

  public String getProductName() {
    return productName;
  }
  public void setProductName(String productName) {
    this.productName = productName;
  }
  public String getChannelCode() {
    return channelCode;
  }
  public void setChannelCode(String channelCode) {
    this.channelCode = channelCode;
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
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getLoginUrl() {
    return loginUrl;
  }
  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }
  public String getLoginImgUrl() {
    return loginImgUrl;
  }
  public void setLoginImgUrl(String loginImgUrl) {
    this.loginImgUrl = loginImgUrl;
  }
  public String getQueryPageUrl() {
    return queryPageUrl;
  }
  public void setQueryPageUrl(String queryPageUrl) {
    this.queryPageUrl = queryPageUrl;
  }
  public String getQueryUrl() {
    return queryUrl;
  }
  public void setQueryUrl(String queryUrl) {
    this.queryUrl = queryUrl;
  }
  public String getCompany() {
    return company;
  }
  public void setCompany(String company) {
    this.company = company;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
