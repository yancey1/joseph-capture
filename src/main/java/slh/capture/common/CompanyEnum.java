package slh.capture.common;

public enum CompanyEnum {

  JOY(0, "真趣"),
  CMGE(1, "中手游"),
  GC(2, "古川"),
  YG(3, "移告"),
  JZLD(4, "北京九州联动科技有限公司"),
  SOULGAME(5, "灵游"),
  RELIAN(6, "艾瑞斯"),
  ACL(7, "泰酷"),
  FUNU(8, "饭游"),
  OMS(9, "破晓"),
  XHC(10, "星华晨"),
  ZSJH(11, "掌上极浩"),
  ZHENSHUNXING(12, "振舜兴"),
  SKY(13, "斯凯"),
  ZQSKY(14, "真趣斯凯"),
  SOJTAO(15, "手淘"),
  TYD(16, "天奕达"),
  WM(17, "玩魔"),
  JT(18, "聚塔"),
  XYH(19, "新银河"),
  LY(20, "灵游"),
  ZYF(21, "指易付"),
  YM(22, "优蜜"),
  INIT_CONPANY(-100, "-100");

  private int    companyCode;
  private String companyName;

  CompanyEnum(int companyCode, String companyName) {
    this.companyCode = companyCode;
    this.companyName = companyName;
  }

  public int getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(int companyCode) {
    this.companyCode = companyCode;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

}
