package slh.capture.domain.zsjh;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

public class ZsjhQueryResult {

  @SerializedName("time")
  private String     time;

  @SerializedName("price")
  private BigDecimal price;

  @SerializedName("appName2")
  private String     appName2;

  @SerializedName("pv")
  private BigDecimal pv;

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getAppName2() {
    return appName2;
  }

  public void setAppName2(String appName2) {
    this.appName2 = appName2;
  }

  public BigDecimal getPv() {
    return pv;
  }

  public void setPv(BigDecimal pv) {
    this.pv = pv;
  }

}
