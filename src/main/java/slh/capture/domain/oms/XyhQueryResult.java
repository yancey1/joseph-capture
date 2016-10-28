package slh.capture.domain.oms;

import com.google.gson.annotations.SerializedName;

public class XyhQueryResult {

  @SerializedName("arpu")
  private String arpu;

  @SerializedName("cpaprice")
  private String cpaprice;

  @SerializedName("cpausercount")
  private String cpausercount;
 
  @SerializedName("cppid")
  private String cppid;
  
  @SerializedName("date")
  private String date;
  
  @SerializedName("money")
  private String money;
  
  @SerializedName("orgid")
  private String orgid;
  
  @SerializedName("orgrate")
  private String orgrate;
  
  @SerializedName("paycount")
  private String paycount;
  
  @SerializedName("usercount")
  private String usercount;
  
  public String getArpu() {
    return arpu;
  }

  public void setArpu(String arpu) {
    this.arpu = arpu;
  }

  public String getCpaprice() {
    return cpaprice;
  }

  public void setCpaprice(String cpaprice) {
    this.cpaprice = cpaprice;
  }

  public String getCpausercount() {
    return cpausercount;
  }

  public void setCpausercount(String cpausercount) {
    this.cpausercount = cpausercount;
  }

  public String getCppid() {
    return cppid;
  }

  public void setCppid(String cppid) {
    this.cppid = cppid;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getMoney() {
    return money;
  }

  public void setMoney(String money) {
    this.money = money;
  }

  public String getOrgid() {
    return orgid;
  }

  public void setOrgid(String orgid) {
    this.orgid = orgid;
  }

  public String getOrgrate() {
    return orgrate;
  }

  public void setOrgrate(String orgrate) {
    this.orgrate = orgrate;
  }

  public String getPaycount() {
    return paycount;
  }

  public void setPaycount(String paycount) {
    this.paycount = paycount;
  }

  public String getUsercount() {
    return usercount;
  }

  public void setUsercount(String usercount) {
    this.usercount = usercount;
  }
}
