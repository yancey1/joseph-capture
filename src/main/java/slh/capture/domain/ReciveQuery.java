package slh.capture.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import slh.capture.common.CustomDateTimeSerializer;
import slh.capture.common.DataGridModel;

/**
 * 应收查询信息表
 * 
 * @author ck 2013-09-30
 * 
 */
public class ReciveQuery implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  private Integer           rqId;                 // 应收id
  private Integer           partnerId;            // 合作方Id
  private Date              rqDate;               // 应收日期
  private String            partner;              // 合作方
  private String            productName;          // 产品名称
  private Integer           productId;            // 代理产品Id
  private String            channelCode;          // 渠道号
  private String            channelName;          // 渠道名称
  private Integer           rqType;               // 类型：1.CPA;2.CPS
  private String            introductPrice;       // 引入价格
  private String            rqRatio;              // 引入比率
  private String            taxRate;              // 税率
  private String            badDebt;              // 坏账
  private String            installCosts;         // 安装数、信息费
  private BigDecimal        shouldRecive;         // 应收
  private String            settleNum;            // 结算单号
  private Integer           reciveState;          // 状态0:审批申请前、1:审批申请中、2:审批未通过、3:审批通过、4:已开票未回款、5:未开票已回款、6:已开票已回款、7:退回
  private Date              createTime;           // 创建时间
  private Date              modifyTime;           // 更新时间
  private DataGridModel     pageInfo;
  private List<Integer>     productIds;           // 产品ID集合
  private String            startTime;
  private String            endTime;
  private Date              startDate;
  private Date              endDate;

  public List<Integer> getProductIds() {
    return productIds;
  }
  public void setProductIds(List<Integer> productIds) {
    this.productIds = productIds;
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
  public String getRqRatio() {
    return rqRatio;
  }
  public void setRqRatio(String rqRatio) {
    this.rqRatio = rqRatio;
  }
  public Integer getProductId() {
    return productId;
  }
  public void setProductId(Integer productId) {
    this.productId = productId;
  }
  public DataGridModel getPageInfo() {
    return pageInfo;
  }
  public void setPageInfo(DataGridModel pageInfo) {
    this.pageInfo = pageInfo;
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
  public Integer getRqId() {
    return rqId;
  }
  public void setRqId(Integer rqId) {
    this.rqId = rqId;
  }

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  public Date getRqDate() {
    return rqDate;
  }
  public void setRqDate(Date rqDate) {
    this.rqDate = rqDate;
  }
  public String getPartner() {
    return partner;
  }
  public void setPartner(String partner) {
    this.partner = partner;
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
  public Integer getRqType() {
    return rqType;
  }
  public void setRqType(Integer rqType) {
    this.rqType = rqType;
  }
  public String getIntroductPrice() {
    return introductPrice;
  }
  public void setIntroductPrice(String introductPrice) {
    this.introductPrice = introductPrice;
  }
  public String getInstallCosts() {
    return installCosts;
  }
  public void setInstallCosts(String installCosts) {
    this.installCosts = installCosts;
  }
  public BigDecimal getShouldRecive() {
    return shouldRecive;
  }
  public void setShouldRecive(BigDecimal shouldRecive) {
    this.shouldRecive = shouldRecive;
  }
  public String getSettleNum() {
    return settleNum;
  }
  public void setSettleNum(String settleNum) {
    this.settleNum = settleNum;
  }

  public Integer getReciveState() {
    return reciveState;
  }
  public void setReciveState(Integer reciveState) {
    this.reciveState = reciveState;
  }

}
