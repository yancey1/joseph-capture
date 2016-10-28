package slh.capture.domain;

import com.google.gson.annotations.SerializedName;

public class JsonHxQuery {

  @SerializedName("logDate")
  private String logDate;

  @SerializedName("rechargeAmount")
  private String rechargeAmount;
  
  @SerializedName("appName")
  private String appName;
  
  @SerializedName("channelId")
  private String channelId;
  
  
  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public String getLogDate() {
    return logDate;
  }

  public void setLogDate(String logDate) {
    this.logDate = logDate;
  }

  public String getRechargeAmount() {
    return rechargeAmount;
  }

  public void setRechargeAmount(String rechargeAmount) {
    this.rechargeAmount = rechargeAmount;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }
  
}
