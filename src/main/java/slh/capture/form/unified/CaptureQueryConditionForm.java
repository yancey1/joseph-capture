package slh.capture.form.unified;

/**
 * 数据抓取查询条件
 * 
 * @author xuwenqiang
 * */
public class CaptureQueryConditionForm extends BaseForm {

  private String cpName;
  private String channelCode;
  private String appName;
  private String userName;
  private String randomCode;
  private String startDate;
  private String endDate;

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getCpName() {
    return cpName;
  }

  public void setCpName(String cpName) {
    this.cpName = cpName;
  }

  public String getChannelCode() {
    return channelCode;
  }

  public void setChannelCode(String channelCode) {
    this.channelCode = channelCode;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getRandomCode() {
    return randomCode;
  }

  public void setRandomCode(String randomCode) {
    this.randomCode = randomCode;
  }

}
