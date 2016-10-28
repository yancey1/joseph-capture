package slh.capture.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import slh.capture.common.CustomDateTimeSerializer;
import slh.capture.common.DataGridModel;

/**
 * 应付查询信息表
 * 
 * @author ck 2013-09-30
 * 
 */
public class PaymentQuery implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 3531486912694141274L;
  private Integer           pqId;
  private Date              exDate;
  private String            setAccount;                             // 结算账号
  private String            partner;                                // 合作方
  private Integer           pqType;                                 // 类别:1.CPA;2.CPS
  private Integer           partnerId;                              // 合作方id
  private String            productName;                            // 产品名称
  private Integer           productId;                              // 产品ID
  private String            channelCode;                            // 渠道号
  private String            channelName;                            // 渠道名称
  private String            proxyPrice;                             // 代理价格
  private String            sharePercent;                           // 分成比例
  private Integer           minNum;                                 // 最小值
  private String            singlePercent;                          // 调整比例
  private String            installCosts;                           // 安装量/信息费
  private Integer           tpInstall;                              // TP安装量
  private BigDecimal        payable;                                // 应付金额
  private String            settleNum;                              // 结算单号
  private Integer           state;                                  // 状态：0:审批申请前、1:审批申请中、2:审批未通过、3:审批通过、4:已收票未付款、5:未收票已付款、6:已收票已付款、7:退回
  private Date              createTime;
  private Date              modifyTime;
  private DataGridModel     pageInfo;
  private String            startTime;
  private String            endTime;
  private Date              startDate;
  private Date              endDate;
  private List<Integer>     productIds;                             // 产品ID集合
  private List<Integer>     cmIdList;                               // 渠道ID 集合
  private String            taxRate;                                // 税率
  private String            badDebt;                                // 坏账
  private Integer           cmId;                                   // 渠道Id

  public Integer getTpInstall() {
    return tpInstall;
  }
  public void setTpInstall(Integer tpInstall) {
    this.tpInstall = tpInstall;
  }
  public List<Integer> getCmIdList() {
    return cmIdList;
  }
  public void setCmIdList(List<Integer> cmIdList) {
    this.cmIdList = cmIdList;
  }
  public Integer getCmId() {
    return cmId;
  }
  public void setCmId(Integer cmId) {
    this.cmId = cmId;
  }
  public List<Integer> getProductIds() {
    return productIds;
  }
  public void setProductIds(List<Integer> productIds) {
    this.productIds = productIds;
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

  public DataGridModel getPageInfo() {
    return pageInfo;
  }
  public void setPageInfo(DataGridModel pageInfo) {
    this.pageInfo = pageInfo;
  }

  public Integer getMinNum() {
    return minNum;
  }
  public void setMinNum(Integer minNum) {
    this.minNum = minNum;
  }
  public String getSinglePercent() {
    return singlePercent;
  }
  public void setSinglePercent(String singlePercent) {
    this.singlePercent = singlePercent;
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
  public String getSettleNum() {
    return settleNum;
  }
  public void setSettleNum(String settleNum) {
    this.settleNum = settleNum;
  }
  public Integer getPqId() {
    return pqId;
  }
  public void setPqId(Integer pqId) {
    this.pqId = pqId;
  }
  @JsonSerialize(using = CustomDateTimeSerializer.class)
  public Date getExDate() {
    return exDate;
  }
  public void setExDate(Date exDate) {
    this.exDate = exDate;
  }
  public String getSetAccount() {
    return setAccount;
  }
  public void setSetAccount(String setAccount) {
    this.setAccount = setAccount;
  }
  public String getPartner() {
    return partner;
  }
  public void setPartner(String partner) {
    this.partner = partner;
  }

  public Integer getPqType() {
    return pqType;
  }
  public void setPqType(Integer pqType) {
    this.pqType = pqType;
  }
  public Integer getPartnerId() {
    return partnerId;
  }
  public void setPartnerId(Integer partnerId) {
    this.partnerId = partnerId;
  }
  public String getProductName() {
    return productName;
  }
  public void setProductName(String productName) {
    this.productName = productName;
  }
  public Integer getProductId() {
    return productId;
  }
  public void setProductId(Integer productId) {
    this.productId = productId;
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
  public String getInstallCosts() {
    return installCosts;
  }
  public void setInstallCosts(String installCosts) {
    this.installCosts = installCosts;
  }
  public BigDecimal getPayable() {
    return payable;
  }
  public void setPayable(BigDecimal payable) {
    this.payable = payable;
  }
  public Integer getState() {
    return state;
  }
  public void setState(Integer state) {
    this.state = state;
  }

}
