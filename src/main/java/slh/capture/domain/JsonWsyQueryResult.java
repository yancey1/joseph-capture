package slh.capture.domain;

import com.google.gson.annotations.SerializedName;

public class JsonWsyQueryResult {

  @SerializedName("buy_return_success")
  private String buyReturnCuccess;
  
  @SerializedName("data_date")
  private String dataDate;

  public String getBuyReturnCuccess() {
    return buyReturnCuccess;
  }

  public void setBuyReturnCuccess(String buyReturnCuccess) {
    this.buyReturnCuccess = buyReturnCuccess;
  }

  public String getDataDate() {
    return dataDate;
  }

  public void setDataDate(String dataDate) {
    this.dataDate = dataDate;
  }

  
  
}
