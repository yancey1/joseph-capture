package slh.capture.domain.oms;

import com.google.gson.annotations.SerializedName;

public class WbQueryResult {

  @SerializedName("expdate")
  private String expdate;

  @SerializedName("app_name")
  private String app_name;

  @SerializedName("hz_name")
  private String hz_name;
 
  @SerializedName("totalfee")
  private String totalfee;

  public String getExpdate() {
    return expdate;
  }

  public void setExpdate(String expdate) {
    this.expdate = expdate;
  }

  public String getApp_name() {
    return app_name;
  }

  public void setApp_name(String app_name) {
    this.app_name = app_name;
  }

  public String getHz_name() {
    return hz_name;
  }

  public void setHz_name(String hz_name) {
    this.hz_name = hz_name;
  }

  public String getTotalfee() {
    return totalfee;
  }

  public void setTotalfee(String totalfee) {
    this.totalfee = totalfee;
  }
  
}
