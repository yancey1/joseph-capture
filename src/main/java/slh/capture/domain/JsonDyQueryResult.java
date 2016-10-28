package slh.capture.domain;

import com.google.gson.annotations.SerializedName;

public class JsonDyQueryResult {

  @SerializedName("logtime")
  private String logtime;
  
  @SerializedName("totalMoney")
  private String totalMoney;
  
  @SerializedName("game")
  private String game;
  
  @SerializedName("cid")
  private String cid;
  
  public String getLogtime() {
    return logtime;
  }

  public void setLogtime(String logtime) {
    this.logtime = logtime;
  }

  public String getTotalMoney() {
    return totalMoney;
  }

  public void setTotalMoney(String totalMoney) {
    this.totalMoney = totalMoney;
  }

  public String getGame() {
    return game;
  }

  public void setGame(String game) {
    this.game = game;
  }

  public String getCid() {
    return cid;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }
  
}
