package slh.capture.common.unified;

/**
 * 枚举需要排重的字段
 * */
public enum DistinctColumnEnum {

  CP_NAME("cp_name", "cpName", "CP名称"),
  CHANNEL_CODE("channel_code", "channelCode", "渠道号"),
  APP_NAME("app_name", "appName", "应用名称"),
  USER_NAME("user_name", "userName", "用户名");

  private String column;
  private String property;
  private String desc;

  private DistinctColumnEnum(String column, String property, String desc) {
    this.column = column;
    this.property = property;
    this.desc = desc;
  }

  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

}
