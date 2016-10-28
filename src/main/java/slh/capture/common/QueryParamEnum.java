package slh.capture.common;

public enum QueryParamEnum {

  URL("url", "URL地址"),
  PRODUCT_ID("product_id", "产品ID"),
  CHANNEL_ID("channel_id", "渠道号"),
  START_TIME("start_time", "开始日期"),
  END_TIME("end_time", "结束日期"),
  BEGINTIME("beginTime", "开始日期"),
  ENDTIME("endTime", "结束日期"),
  CHNAME("chName", "用户名"),
  START_DATE("startDate", "开始日期"),
  END_DATE("endDate", "结束日期"),
  CHPASSWORD("chPassword", "密码"),
  SELECTTEST("SelectTest", "全部应用"),
  SECTYPE("SecType", "按日查询"),
  DATEFORMATE("dateFormate", "查询日期类型"),
  PAGENO("page.pageNo", "页数"),
  PAGESIZE("pageSize", "每页记录数"),
  DATETYPE("DATE_TYPE", "日期類型"),
  MONTH("month", "月份"),
  RAND_CODE("randCode", "验证码"),
  APP_ID("app_id", "应用id"),
  GROUP_TYPE("grouptype", "分组类型"),

  ;

  private String key;
  private String name;

  QueryParamEnum(String key, String name) {
    this.key = key;
    this.name = name;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
