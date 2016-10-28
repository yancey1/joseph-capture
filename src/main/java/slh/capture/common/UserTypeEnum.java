package slh.capture.common;

/**
 * 用户类型枚举类
 * 
 * @author xuwenqiang
 * */
public enum UserTypeEnum {

  IS_ADMIN(1, "管理员"), IS_NOT_ADMIN(0, "非管理员");

  private int    type;
  private String desc;

  private UserTypeEnum(int type, String desc) {
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
