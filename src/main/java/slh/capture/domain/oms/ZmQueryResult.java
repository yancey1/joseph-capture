package slh.capture.domain.oms;

import com.google.gson.annotations.SerializedName;

public class ZmQueryResult {
  
  @SerializedName("fDay")
  private String fDay;
  
  @SerializedName("fAppName")
  private String fAppName;
  
  @SerializedName("fPartner")
  private String fPartner;
  
  @SerializedName("fAmt")
  private String fAmt;

  public String getfDay() {
    return fDay;
  }

  public void setfDay(String fDay) {
    this.fDay = fDay;
  }

  public String getfAppName() {
    return fAppName;
  }

  public void setfAppName(String fAppName) {
    this.fAppName = fAppName;
  }

  public String getfPartner() {
    return fPartner;
  }

  public void setfPartner(String fPartner) {
    this.fPartner = fPartner;
  }

  public String getfAmt() {
    return fAmt;
  }

  public void setfAmt(String fAmt) {
    this.fAmt = fAmt;
  }
  
}
