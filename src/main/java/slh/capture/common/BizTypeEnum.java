package slh.capture.common;

public enum BizTypeEnum {

  CPA("cpa", 1, "CPA业务"), CPS("cps", 2, "CPS业务"), CPC("cpc", 3, "CPC业务");

  private String key;
  private int    code;
  private String desc;

  BizTypeEnum(String key, int code, String desc) {
    this.key = key;
    this.code = code;
    this.desc = desc;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

}
