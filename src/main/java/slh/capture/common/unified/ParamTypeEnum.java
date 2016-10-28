package slh.capture.common.unified;

/**
 * 参数类型枚举
 * 
 * 数据类型：1-普通类型，2-json类型
 * 
 * */
public enum ParamTypeEnum {

  COMMON_TYPE(1, "普通类型"), JSON_TYPE(2, "json类型");

  private int    type;
  private String desc;

  private ParamTypeEnum(int type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

}
