package slh.capture.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import slh.capture.common.CustomDateTimeSerializer;
import slh.capture.common.DataGridModel;

/**
 * 经营数据
 * 
 * @author oc_admin
 * 
 */
public class OperateData {
  private Integer       opId;         // 经营数据ID
  private Date          opDate;       // 日期
  private String        partnerName;  // 合作方方
  private Integer       partnerId;    // 合作方Id
  private Integer       productId;    // 产品名称
  private String        productName;  // 产品名称
  private String        channelCode;  // 渠道号
  private String        channelName;  // 渠道名称
  private Integer       opType;       // 类别：1CPA;2.CPA;
  private String        installCosts; // 安装数、信息费
  private String        opRatio;      // CP比率
  private String        badDebt;      // CP坏账
  private String        taxRate;      // CP税率
  private String        singlePrice;  // CP单价
  private BigDecimal    ocIncome;     // 欧昕实收
  private String        sharePercent; // TP分成比例
  private String        singlePercent; // TP调整比例
  private String        proxyPrice;   // TP代理价格
  private Integer       minNum;       // TP最小激活数
  private BigDecimal    tpIncome;     // TP收入
  private String        tpBadDebt;    // TP坏账
  private String        tpTaxRate;    // TP税率
  private Integer       tpInstall;    // TP安装量
  private Date          createTime;
  private Date          modifyTime;
  private DataGridModel pageInfo;
  private String        startTime;
  private String        endTime;
  private Date          startDate;
  private Date          endDate;

  public Integer getTpInstall() {
    return tpInstall;
  }
  public void setTpInstall(Integer tpInstall) {
    this.tpInstall = tpInstall;
  }
  public Integer getProductId() {
    return productId;
  }
  public void setProductId(Integer productId) {
    this.productId = productId;
  }
  public String getTpBadDebt() {
    return tpBadDebt;
  }
  public void setTpBadDebt(String tpBadDebt) {
    this.tpBadDebt = tpBadDebt;
  }
  public String getTpTaxRate() {
    return tpTaxRate;
  }
  public void setTpTaxRate(String tpTaxRate) {
    this.tpTaxRate = tpTaxRate;
  }
  public Integer getPartnerId() {
    return partnerId;
  }
  public void setPartnerId(Integer partnerId) {
    this.partnerId = partnerId;
  }
  public String getStartTime() {
    return startTime;
  }
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }
  public String getEndTime() {
    return endTime;
  }
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }
  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public String getSinglePercent() {
    return singlePercent;
  }
  public void setSinglePercent(String singlePercent) {
    this.singlePercent = singlePercent;
  }
  public Integer getOpId() {
    return opId;
  }
  public void setOpId(Integer opId) {
    this.opId = opId;
  }

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  public Date getOpDate() {
    return opDate;
  }
  public void setOpDate(Date opDate) {
    this.opDate = opDate;
  }
  public String getPartnerName() {
    return partnerName;
  }
  public void setPartnerName(String partnerName) {
    this.partnerName = partnerName;
  }
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
  public String getChannelName() {
    return channelName;
  }
  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }
  public Integer getOpType() {
    return opType;
  }
  public void setOpType(Integer opType) {
    this.opType = opType;
  }
  public String getInstallCosts() {
    return installCosts;
  }
  public void setInstallCosts(String installCosts) {
    this.installCosts = installCosts;
  }
  public String getSinglePrice() {
    return singlePrice;
  }
  public void setSinglePrice(String singlePrice) {
    this.singlePrice = singlePrice;
  }

  public String getOpRatio() {
    return opRatio;
  }
  public void setOpRatio(String opRatio) {
    this.opRatio = opRatio;
  }
  public String getBadDebt() {
    return badDebt;
  }
  public void setBadDebt(String badDebt) {
    this.badDebt = badDebt;
  }
  public String getTaxRate() {
    return taxRate;
  }
  public void setTaxRate(String taxRate) {
    this.taxRate = taxRate;
  }
  public BigDecimal getOcIncome() {
    return ocIncome;
  }
  public void setOcIncome(BigDecimal ocIncome) {
    this.ocIncome = ocIncome;
  }
  public String getProxyPrice() {
    return proxyPrice;
  }
  public void setProxyPrice(String proxyPrice) {
    this.proxyPrice = proxyPrice;
  }
  public String getSharePercent() {
    return sharePercent;
  }
  public void setSharePercent(String sharePercent) {
    this.sharePercent = sharePercent;
  }

  public Integer getMinNum() {
    return minNum;
  }
  public void setMinNum(Integer minNum) {
    this.minNum = minNum;
  }
  public BigDecimal getTpIncome() {
    return tpIncome;
  }
  public void setTpIncome(BigDecimal tpIncome) {
    this.tpIncome = tpIncome;
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

}
