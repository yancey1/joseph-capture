package slh.capture.domain;

import com.google.gson.annotations.SerializedName;

public class JsonLyQueryResult {

  @SerializedName("APP_ID")
  private String app_id;

  @SerializedName("APP_NAME")
  private String app_name;

  @SerializedName("PAYABLE")
  private String payable;

  @SerializedName("PAY_USER_CNT")
  private String pay_user_cnt;

  @SerializedName("PAY_CNT")
  private String pay_cnt;

  @SerializedName("ARPPU")
  private String arppu;

  @SerializedName("PER_PAY_CNT")
  private String per_pay_cnt;

  @SerializedName("ROW_NUM")
  private String row_num;

  public String getApp_id() {
    return app_id;
  }

  public void setApp_id(String app_id) {
    this.app_id = app_id;
  }

  public String getApp_name() {
    return app_name;
  }

  public void setApp_name(String app_name) {
    this.app_name = app_name;
  }

  public String getPayable() {
    return payable;
  }

  public void setPayable(String payable) {
    this.payable = payable;
  }

  public String getPay_user_cnt() {
    return pay_user_cnt;
  }

  public void setPay_user_cnt(String pay_user_cnt) {
    this.pay_user_cnt = pay_user_cnt;
  }

  public String getPay_cnt() {
    return pay_cnt;
  }

  public void setPay_cnt(String pay_cnt) {
    this.pay_cnt = pay_cnt;
  }

  public String getArppu() {
    return arppu;
  }

  public void setArppu(String arppu) {
    this.arppu = arppu;
  }

  public String getPer_pay_cnt() {
    return per_pay_cnt;
  }

  public void setPer_pay_cnt(String per_pay_cnt) {
    this.per_pay_cnt = per_pay_cnt;
  }

  public String getRow_num() {
    return row_num;
  }

  public void setRow_num(String row_num) {
    this.row_num = row_num;
  }

  /*
   * public static void main(String[] args) { JsonWmQuery wm=new JsonWmQuery();
   * wm.setAffectedRows(1); List<String> list=new ArrayList<String>();
   * list.add("1"); list.add("2"); wm.setColumnNames(list); wm.setResultCode(3);
   * wm.setResultDesc("5"); String str[]={"2","3","4"}; wm.setRows(str);
   * List<String> list2=new ArrayList<String>(); list2.add("111");
   * list2.add("2222"); Gson gson = new Gson();
   * System.out.println(gson.toJson(wm)); }
   */
}
