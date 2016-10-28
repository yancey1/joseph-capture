package slh.capture.common.unified;

/**
 * 验证码生成类型枚举
 * 
 * @author xuwenqiang
 * */
public enum RandomCodeTypeEnum {

  SERVICE_GENERATE(1, "服务端代码生成"), JS_GENERATE(2, "js代码生成");

  private RandomCodeTypeEnum(int type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  private int    type;
  private String desc;

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
