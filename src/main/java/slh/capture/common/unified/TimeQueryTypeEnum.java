package slh.capture.common.unified;

/***
 * 时间的查询类型：1-时间日区间，2-区间内的每一天，3-时间月区间，4-月区间内每个月
 * 
 * @author xuwenqiang
 * */
public enum TimeQueryTypeEnum {

  DAY_REGION(1, "时间日区间"), EVERY_DAY_IN_REGION(2, "区间内的每一天"), month_region(3, "时间月区间"), EVERY_MONTH_IN_REGION(4, "月区间内每个月"), CURRENT_MONTH(5, "当前时间的月份");

  private int    type;
  private String desc;

  private TimeQueryTypeEnum(int type, String desc) {
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
