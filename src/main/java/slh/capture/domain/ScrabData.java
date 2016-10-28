package slh.capture.domain;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import slh.capture.common.DataGridModel;

public class ScrabData {

  private String        bizDate;    // 业务日期
  private String        channelId;  // 渠道号
  private String        productId;  // 产品编号
  private String        productName; // 产品名称
  private String        bizAmount;  // 安装量或收入
  private int           bizType;    // 业务类型 cpa,cps,cpc
  private Integer       state;      // 0:未入库;1:已入库
  private String        bookMark;   // 标签
  private String        mark;       // 备注
  private Date          createTime; // 创建时间
  private Date          modifyTime; // 最后一次更新时间
  private String        startTime;
  private String        endTime;
  private Date          startDate;
  private Date          endDate;
  private DataGridModel pageInfo;   // 分页
  private String userName;

  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public Integer getState() {
    return state;
  }
  public void setState(Integer state) {
    this.state = state;
  }
  public String getBookMark() {
    return bookMark;
  }
  public void setBookMark(String bookMark) {
    this.bookMark = bookMark;
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
  public String getMark() {
    return mark;
  }
  public void setMark(String mark) {
    this.mark = mark;
  }
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
  public String getBizDate() {
    return bizDate;
  }
  public void setBizDate(String bizDate) {
    this.bizDate = bizDate;
  }
  public String getChannelId() {
    return channelId;
  }
  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }
  public String getProductId() {
    return productId;
  }
  public void setProductId(String productId) {
    this.productId = productId;
  }
  public String getProductName() {
    return productName;
  }
  public void setProductName(String productName) {
    this.productName = productName;
  }
  public String getBizAmount() {
    return bizAmount;
  }
  public void setBizAmount(String bizAmount) {
    this.bizAmount = bizAmount;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bizAmount == null) ? 0 : bizAmount.hashCode());
    result = prime * result + ((bizDate == null) ? 0 : bizDate.hashCode());
    result = prime * result + bizType;
    result = prime * result + ((channelId == null) ? 0 : channelId.hashCode());
    result = prime * result + ((productId == null) ? 0 : productId.hashCode());
    result = prime * result + ((productName == null) ? 0 : productName.hashCode());
    return result;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ScrabData other = (ScrabData) obj;
    if (bizDate == null) {
      if (other.bizDate != null)
        return false;
    } else if (!bizDate.equals(other.bizDate))
      return false;
    if (bizType != other.bizType)
      return false;
    if (channelId == null) {
      if (other.channelId != null)
        return false;
    } else if (!channelId.equals(other.channelId))
      return false;
    if (productId == null) {
      if (other.productId != null)
        return false;
    } else if (!productId.equals(other.productId))
      return false;
    return true;
  }
  public int getBizType() {
    return bizType;
  }
  public void setBizType(int bizType) {
    this.bizType = bizType;
  }
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
