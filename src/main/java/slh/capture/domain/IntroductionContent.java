package slh.capture.domain;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import slh.capture.common.CustomDateTimeSerializer;
import slh.capture.common.DataGridModel;

/**
 * 接入内容信息表
 * 
 * @author ck 2013-09-30
 * 
 */
public class IntroductionContent implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = -6147041708233256384L;
  private Integer           incId;                                   // 接入内容ID
  private Integer           partnerId;                               // 供应商ID
  private String            advertiseName;                           // 接入内容名称
  private Integer           incType;                                 // 类别：1CPA;2.CPS;
  private String            singlePrice;                             // 单一价格
  private String            commerceSign;                            // 商务签订比例
  private Integer           dataType;                                // 后台数据类型
  private String            singleCaptureRatio;                      // 比率
  private String            statisticalData;                         // 数据统计地址
  private String            account;                                 // 账号
  private String            password;                                // 密码
  private String            providerContent;                         // 所属内容供应方
  private String            taxRate;                                 // 税率
  private String            badDebt;                                 // 坏账
  private String            mark;                                    // 备注
  private Date              createTime;                              // 创建时间
  private Date              modifyTime;                              // 最后一次更新时间
  private DataGridModel     pageInfo;

  public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }

  public Integer getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(Integer partnerId) {
    this.partnerId = partnerId;
  }

  public String getSinglePrice() {
    return singlePrice;
  }

  public void setSinglePrice(String singlePrice) {
    this.singlePrice = singlePrice;
  }

  public Integer getDataType() {
    return dataType;
  }

  public void setDataType(Integer dataType) {
    this.dataType = dataType;
  }

  public Integer getIncId() {
    return incId;
  }

  public void setIncId(Integer incId) {
    this.incId = incId;
  }

  public String getAdvertiseName() {
    return advertiseName;
  }
  public void setAdvertiseName(String advertiseName) {
    this.advertiseName = advertiseName;
  }

  public String getCommerceSign() {
    return commerceSign;
  }
  public void setCommerceSign(String commerceSign) {
    this.commerceSign = commerceSign;
  }

  public String getSingleCaptureRatio() {
    return singleCaptureRatio;
  }

  public void setSingleCaptureRatio(String singleCaptureRatio) {
    this.singleCaptureRatio = singleCaptureRatio;
  }

  public String getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(String taxRate) {
    this.taxRate = taxRate;
  }

  public String getBadDebt() {
    return badDebt;
  }

  public void setBadDebt(String badDebt) {
    this.badDebt = badDebt;
  }

  public String getStatisticalData() {
    return statisticalData;
  }

  public void setStatisticalData(String statisticalData) {
    this.statisticalData = statisticalData;
  }

  public String getAccount() {
    return account;
  }
  public void setAccount(String account) {
    this.account = account;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getProviderContent() {
    return providerContent;
  }
  public void setProviderContent(String providerContent) {
    this.providerContent = providerContent;
  }

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }

  public DataGridModel getPageInfo() {
    return pageInfo;
  }
  public void setPageInfo(DataGridModel pageInfo) {
    this.pageInfo = pageInfo;
  }

  public Integer getIncType() {
    return incType;
  }

  public void setIncType(Integer incType) {
    this.incType = incType;
  }

}
