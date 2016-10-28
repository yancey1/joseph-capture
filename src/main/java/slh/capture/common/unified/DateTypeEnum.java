package slh.capture.common.unified;

/**
 * 时间类型枚举
 * 
 * */
public enum DateTypeEnum {

  YYYY_MM_DD(1, "年-月-日"), YYYYMMDD(2, "年月日"), YYYYMM(3, "年月");

  private int    type;
  private String desc;

  private DateTypeEnum(int type, String desc) {
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
