package slh.capture.common;

public enum DataNameEnum {

  PRODUCT("productName", "产品名称"), CHANNEL_ID("channelId", "渠道ID"), BIZ_DATE("bizDate", "日期"), BIZ_AMOUNT("bizAmount", "收入或安装量"), BIZ_TYPE("bizType", "业务类型");
  private String name;
  private String desc;

  public static DataNameEnum getDataNameEnum(String name) {
    for (DataNameEnum dataNameEnum : DataNameEnum.values()) {
      if (dataNameEnum.getName().equals(name)) {
        return dataNameEnum;
      }
    }

    return null;
  }

  DataNameEnum(String name, String desc) {
    this.name = name;
    this.desc = desc;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

}
