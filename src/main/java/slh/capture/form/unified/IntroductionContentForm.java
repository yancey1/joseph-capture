package slh.capture.form.unified;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import slh.capture.annotation.ExportExcelAnnotation;
import slh.capture.common.CustomDateTimeSerializer;

/**
 * 接入内容信息表
 * 
 * @author ck 2013-09-30
 * 
 */
public class IntroductionContentForm {

  private Integer partnerId;

  @ExportExcelAnnotation(index = 0)
  private String  advertiseName;     // 产品名称

  @ExportExcelAnnotation(index = 1)
  private String  providerContent;   // CP名称

  @ExportExcelAnnotation(index = 2)
  private String  incTypeName;       // 产品类型：1CPA;2.CPS;
  private Integer incType;

  @ExportExcelAnnotation(index = 3)
  private Double  commerceSign;      // 合同比例

  @ExportExcelAnnotation(index = 4)
  private String  dataTypeName;
  private Integer dataType;          // 数据类型:0信息费;1实收

  @ExportExcelAnnotation(index = 5)
  private Double  singlePrice;       // CPA单价

  @ExportExcelAnnotation(index = 6)
  private Double  singleCaptureRatio; // CP比率

  @ExportExcelAnnotation(index = 7)
  private Double  taxRate;           // CP税率

  @ExportExcelAnnotation(index = 8)
  private Double  badDebt;           // CP坏账

  @ExportExcelAnnotation(index = 9)
  private String  isPayName;
  private Integer isPay;             // 是否支付：0否;1是

  @ExportExcelAnnotation(index = 10)
  private String  adjustRatio;       // 调整比率

  @ExportExcelAnnotation(index = 11)
  private String  statisticalData;   // 后台地址

  @ExportExcelAnnotation(index = 12)
  private String  account;

  @ExportExcelAnnotation(index = 13)
  private String  password;

  @ExportExcelAnnotation(index = 14)
  private Date    createTime;        // 创建时间
  private Integer incId;             // 接入内容ID
  private Date    modifyTime;
  private String  mark;

  public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }

  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
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

  public String getAdjustRatio() {
    return adjustRatio;
  }

  public Integer getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(Integer partnerId) {
    this.partnerId = partnerId;
  }

  public void setAdjustRatio(String adjustRatio) {
    this.adjustRatio = adjustRatio;
  }

  public String getStatisticalData() {
    return statisticalData;
  }

  public void setStatisticalData(String statisticalData) {
    this.statisticalData = statisticalData;
  }

  public String getIsPayName() {
    return isPayName;
  }

  public void setIsPayName(String isPayName) {
    this.isPayName = isPayName;
  }

  public Integer getIsPay() {
    return isPay;
  }

  public void setIsPay(Integer isPay) {

    if (isPay != null) {
      if (isPay == 0) {
        setIsPayName("否");
      } else {
        setIsPayName("是");
      }
    }

    this.isPay = isPay;
  }

  public String getIncTypeName() {
    return incTypeName;
  }

  public void setIncTypeName(String incTypeName) {
    this.incTypeName = incTypeName;
  }

  public String getDataTypeName() {
    return dataTypeName;
  }

  public void setDataTypeName(String dataTypeName) {
    this.dataTypeName = dataTypeName;
  }

  public Integer getDataType() {
    return dataType;
  }

  public void setDataType(Integer dataType) {
    if (dataType != null) {
      if (dataType == 0) {
        setDataTypeName("信息费");
      } else if (dataType == 1) {
        setDataTypeName("实收");
      }
    }

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

  public Integer getIncType() {
    return incType;
  }

  public void setIncType(Integer incType) {

    if (incType == 1) {
      setIncTypeName("CPA");
    } else if (incType == 2) {
      setIncTypeName("CPS");
    }

    this.incType = incType;
  }

  public Double getCommerceSign() {
    return commerceSign;
  }

  public void setCommerceSign(Double commerceSign) {
    this.commerceSign = commerceSign;
  }

  public Double getSinglePrice() {
    return singlePrice;
  }

  public void setSinglePrice(Double singlePrice) {
    this.singlePrice = singlePrice;
  }

  public Double getSingleCaptureRatio() {
    return singleCaptureRatio;
  }

  public void setSingleCaptureRatio(Double singleCaptureRatio) {
    this.singleCaptureRatio = singleCaptureRatio;
  }

  public Double getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(Double taxRate) {
    this.taxRate = taxRate;
  }

  public Double getBadDebt() {
    return badDebt;
  }

  public void setBadDebt(Double badDebt) {
    this.badDebt = badDebt;
  }

}
