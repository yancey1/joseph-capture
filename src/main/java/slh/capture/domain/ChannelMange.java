package slh.capture.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import slh.capture.common.CustomDateTimeSerializer;
import slh.capture.common.DataGridModel;

/**
 * 渠道代理应用信息表
 * 
 * @author ck 2013-09-30
 */
public class ChannelMange implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = -9140272230133759414L;

  private Integer           cmId;                                    // 渠道管理ID
  private Integer           incId;                                   // 引入内容ID
  private String            productName;                              // 产品名称
  private Integer           partnerId;                               // 合作方ID
  private String            channelCode;                             // 渠道号
  private String            channelName;                             // 渠道名称
  private String            singlePrice;                             // 单一价格
  private Integer           minPrice;                                // 最小值
  private String            singlePercent;                           // 调整比例
  private String            sharePercent;                            // 分成比例
  private String            taxRate;                                 // 税率
  private String            badDebt;                                 // 坏账
  private Date              createTime;                              // 创建时间
  private Date              modifyTime;                              // 最后一次更新时间
  private String            contentName;                             // 内容名称
  private String            providerContent;                         // 所属渠道商
  private String            mark;                                    // 备注
  private List<Integer>     cmIdList;

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }

  public List<Integer> getCmIdList() {
    return cmIdList;
  }

  public void setCmIdList(List<Integer> cmIdList) {
    this.cmIdList = cmIdList;
  }

  private DataGridModel pageInfo;

  public Integer getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(Integer partnerId) {
    this.partnerId = partnerId;
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

  public Integer getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(Integer minPrice) {
    this.minPrice = minPrice;
  }

  public Integer getCmId() {
    return cmId;
  }
  public void setCmId(Integer cmId) {
    this.cmId = cmId;
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

  public String getSinglePrice() {
    return singlePrice;
  }

  public void setSinglePrice(String singlePrice) {
    this.singlePrice = singlePrice;
  }

  public String getSharePercent() {
    return sharePercent;
  }

  public void setSharePercent(String sharePercent) {
    this.sharePercent = sharePercent;
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
  @JsonSerialize(using = CustomDateTimeSerializer.class)
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

  public Integer getIncId() {
    return incId;
  }
  public void setIncId(Integer incId) {
    this.incId = incId;
  }
  public String getContentName() {
    return contentName;
  }
  public void setContentName(String contentName) {
    this.contentName = contentName;
  }
  public String getProviderContent() {
    return providerContent;
  }
  public void setProviderContent(String providerContent) {
    this.providerContent = providerContent;
  }

}
