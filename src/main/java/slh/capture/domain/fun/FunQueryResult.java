package slh.capture.domain.fun;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.gson.annotations.SerializedName;

public class FunQueryResult {

  @SerializedName("appName")
  private String appName;

  @SerializedName("channelId")
  private String channelId;

  @SerializedName("subChannelId")
  private String subChannelId;

  @SerializedName("rigisteredUsers")
  private String rigisteredUsers;

  @SerializedName("payingUsers")
  private String payingUsers;

  @SerializedName("rechargeCm")
  private String rechargeCm;

  @SerializedName("rechargeCu")
  private String rechargeCu;

  @SerializedName("rechargeAmount")
  private String rechargeAmount;

  @SerializedName("logDate")
  private String logDate;

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public String getSubChannelId() {
    return subChannelId;
  }

  public void setSubChannelId(String subChannelId) {
    this.subChannelId = subChannelId;
  }

  public String getRigisteredUsers() {
    return rigisteredUsers;
  }

  public void setRigisteredUsers(String rigisteredUsers) {
    this.rigisteredUsers = rigisteredUsers;
  }

  public String getPayingUsers() {
    return payingUsers;
  }

  public void setPayingUsers(String payingUsers) {
    this.payingUsers = payingUsers;
  }

  public String getRechargeCm() {
    return rechargeCm;
  }

  public void setRechargeCm(String rechargeCm) {
    this.rechargeCm = rechargeCm;
  }

  public String getRechargeCu() {
    return rechargeCu;
  }

  public void setRechargeCu(String rechargeCu) {
    this.rechargeCu = rechargeCu;
  }

  public String getRechargeAmount() {
    return rechargeAmount;
  }

  public void setRechargeAmount(String rechargeAmount) {
    this.rechargeAmount = rechargeAmount;
  }

  public String getLogDate() {
    return logDate;
  }

  public void setLogDate(String logDate) {
    this.logDate = logDate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
