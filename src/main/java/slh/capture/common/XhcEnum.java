package slh.capture.common;

public enum XhcEnum {

  JYDDZ("精英斗地主", "http://d.xhc668.com/d/ouxin001", "欧昕"), YJPDDZ("赢奖品斗地主", "http://d.xhc668.com/d/ouxinsd2", "欧昕2");

  private String productName;
  private String backAddress;
  private String channelCode;

  XhcEnum(String productName, String backAddress, String channelCode) {
    this.productName = productName;
    this.backAddress = backAddress;
    this.channelCode = channelCode;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getBackAddress() {
    return backAddress;
  }

  public void setBackAddress(String backAddress) {
    this.backAddress = backAddress;
  }

  public String getChannelCode() {
    return channelCode;
  }

  public void setChannelCode(String channelCode) {
    this.channelCode = channelCode;
  }

}
