package slh.capture.common;

public class ReturnJsonCode {

  public static final String RETURN_CODE = "return_code";
  public static final String RETURN_MSG  = "return_msg";

  public enum MsgCodeEnum {
    FAILURE(0, "操作失败！"),
    SUCCESS(1, "操作成功！"),
    INFO_EXISTS(2, "信息已存在！"),
    BEEN_APPLIED(3, "记录已被引用，不能删除！"),
    AUTHORITY(4, "没有权限！"),
    PARA_TYPE(5, "类型不匹配"),
    PROCESS_END(6, "流程已结束，不能退回！"),
    CAPTURED_BEEN(7, "当前时间段内已有数据抓取过，请重新选择条件！"),
    NO_CHANNEL(8, "渠道不存在"),
    HAD_RECIVE(9, "已生成过收入数据！");

    private int    code;
    private String msg;

    MsgCodeEnum(int _code, String _msg) {
      this.code = _code;
      this.msg = _msg;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public String getMsg() {
      return msg;
    }

    public void setMsg(String msg) {
      this.msg = msg;
    }

  }

}
