package slh.capture.common;

public enum CapturePlatEnum {

  JOY("ocean", "000000", "http://data.androidservice.cn:9898/login.action", "http://data.androidservice.cn:9898/verificationCode_getImage.action", "http://data.androidservice.cn:9898/channelData_listDay.action", "http://data.androidservice.cn:9898/channelData_listDay.action"),
  JOY_MM("ocean", "000000", "http://data.androidservice.cn:9898/login.action", "http://data.androidservice.cn:9898/verificationCode_getImage.action", "http://data.androidservice.cn:9898/mmCps_mmAppCPSList.action", "http://data.androidservice.cn:9898/mmCps_mmAppCPSList.action"),
  CMGE("oxxx", "888888", "http://android.kkfun.com/login.action", null, "http://android.kkfun.com/androidReport/doAndroidCustomerReportQuery.action?ajax=true&search.dateType=0", "http://android.kkfun.com/androidReport/doAndroidCustomerReportList.action"),
  FUNU("30022", "517556", "http://data.funugame.com/login_index", null, "http://data.funugame.com/query_Channel", "http://data.funugame.com/queryResult_condition"),
  OMS("", "", "", "", "", "http://115.236.18.197:8185/androidOms/pages/yjpZjhCpa!getYjpZjhCPAList.action"),
  ACL("qdOX", "111111a", "http://122.224.212.152:8080/bill/acl/login!login.action", "http://122.224.212.152:8080/bill/validateCode?0.35296678645486235", "http://122.224.212.152:8080/bill/cpsDay.action", "http://122.224.212.152:8080/bill/bill/cpsDay!listData.action"),
  ZSJH("zqxmjtp", "zqxmjbtsqtp2013", "http://124.205.62.86/businessportal2/channel/channelaccount/channellogin.shtml", "", "", "http://124.205.62.86/businessportal2/channel/channelaccount/paydetail.shtml"),
  JZLD("tyd", "tyd", "http://coop.joloplay.com/auth/login/login.do", "http://coop.joloplay.com/codepicservlet", "http://coop.joloplay.com/query/coop/user/gamedata.do?timestamp=1389087435531", "http://coop.joloplay.com/query/coop/user/gamedata.do?timestamp=1389086871750"),
  SKY("", "", "", "https://bill.fivesky.net/validateCode", "", ""),
  TYD("ocean", "ocean@123", "http://cpchannel.zhuoyitech.net/user/login", "http://cpchannel.zhuoyitech.net/user/authcode?code=", "http://data.androidservice.cn:9898/channelData_listDay.action", "http://data.androidservice.cn:9898/channelData_listDay.action"),
  SOJTAO("花花", "123456", "http://ad.sojtao.com/LoginPage.aspx", "http://ad.sojtao.com/images.aspx", "http://ad.sojtao.com/DataSearch.aspx", "http://ad.sojtao.com/DataSearch.aspx"),
  VMTYD("vm-tyd","tyd#2014","","http://dev.10086.cn/cmdn/supesite/seccode/vCode.php?style=str","",""),
  XYH("ouxin","FodM9y5l","","http://ngsteam.xinyinhe.com/smspay/imgcode?0.4984880720730871","","");
  
  private String userName;
  private String password;
  private String loginUrl;
  private String loginImgUrl;
  private String queryPageUrl;
  private String queryUrl;

  CapturePlatEnum(String userName, String password, String loginUrl, String loginImgUrl, String queryPageUrl, String queryUrl) {
    this.userName = userName;
    this.password = password;
    this.loginUrl = loginUrl;
    this.loginImgUrl = loginImgUrl;
    this.queryPageUrl = queryPageUrl;
    this.queryUrl = queryUrl;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public String getLoginImgUrl() {
    return loginImgUrl;
  }

  public void setLoginImgUrl(String loginImgUrl) {
    this.loginImgUrl = loginImgUrl;
  }

  public String getQueryPageUrl() {
    return queryPageUrl;
  }

  public void setQueryPageUrl(String queryPageUrl) {
    this.queryPageUrl = queryPageUrl;
  }

  public String getQueryUrl() {
    return queryUrl;
  }

  public void setQueryUrl(String queryUrl) {
    this.queryUrl = queryUrl;
  }

}
