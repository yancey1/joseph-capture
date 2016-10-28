package slh.capture.domain.unified;

import slh.capture.common.DataGridModel;

public class CaptureConfigEntity extends BaseEntity {

  /**
   * 
   */
  private static final long serialVersionUID = 2055689598778528286L;
  private Integer           id;
  private String            cpName;
  private String            channelCode;
  private String            appName;
  private String            userName;
  private String            password;
  private Integer           bizType;
  private String            loginUrl;
  private String            queryUrl;
  private String            loginImgUrl;
  private String            queryPageUrl;
  
  /**
   * 数据类型：1-普通类型，2-json类型
   * */
  private Integer           paramType;

  private String            dataIndex;

  /**
   * 1、固定格式:key:value,key:value 2、前几位必须是： （1）空值
   * （2）开始时间:value,结束时间:value,渠道号:value （3） 月:value,渠道号
   * */
  private String            params;

  private String            tableAttr;
  private String            tableAttrValue;

  /**
   * 验证码生成类型：1-后台生成，2-前台生成
   * */
  private Integer           randomCodeType;

  /**
   * randomCodeType为前台生成时，页面验证码随机数的来源
   * */
  private String            randomSrc;

  /**
   * 页面分页的关键字
   * */
  private String            pageKey;

  /**
   * 时间类型：1：YYYY-MM-DD，2-YYYYMMDD
   * */
  private Integer           dateType;

  /**
   * 时间的查询类型：1-时间区间，2-区间内的每一天
   * 
   * */
  private Integer           timeQueryType;

  /**
   * 0:未入库;1:已入库
   * */
  private Integer           state;

  private DataGridModel     pageInfo;

  private String            identifyType;
  
  private String            loginParam;
  public String getLoginParam() {
    return loginParam;
  }

  public void setLoginParam(String loginParam) {
    this.loginParam = loginParam;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  private String startTime;

  private String endTime;

  public String getIdentifyType() {
    return identifyType;
  }

  public void setIdentifyType(String identifyType) {
    this.identifyType = identifyType;
  }

  public DataGridModel getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(DataGridModel pageInfo) {
    this.pageInfo = pageInfo;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getChannelCode() {
    return channelCode;
  }

  public void setChannelCode(String channelCode) {
    this.channelCode = channelCode;
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

  public String getCpName() {
    return cpName;
  }

  public void setCpName(String cpName) {
    this.cpName = cpName;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public String getQueryUrl() {
    return queryUrl;
  }

  public void setQueryUrl(String queryUrl) {
    this.queryUrl = queryUrl;
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

  public Integer getParamType() {
    return paramType;
  }

  public void setParamType(Integer paramType) {
    this.paramType = paramType;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getDataIndex() {
    return dataIndex;
  }

  public void setDataIndex(String dataIndex) {
    this.dataIndex = dataIndex;
  }

  public String getTableAttr() {
    return tableAttr;
  }

  public void setTableAttr(String tableAttr) {
    this.tableAttr = tableAttr;
  }

  public String getTableAttrValue() {
    return tableAttrValue;
  }

  public void setTableAttrValue(String tableAttrValue) {
    this.tableAttrValue = tableAttrValue;
  }

  public String getRandomSrc() {
    return randomSrc;
  }

  public void setRandomSrc(String randomSrc) {
    this.randomSrc = randomSrc;
  }

  public String getPageKey() {
    return pageKey;
  }

  public void setPageKey(String pageKey) {
    this.pageKey = pageKey;
  }

  public Integer getRandomCodeType() {
    return randomCodeType;
  }

  public void setRandomCodeType(Integer randomCodeType) {
    this.randomCodeType = randomCodeType;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getDateType() {
    return dateType;
  }

  public void setDateType(Integer dateType) {
    this.dateType = dateType;
  }

  public Integer getTimeQueryType() {
    return timeQueryType;
  }

  public void setTimeQueryType(Integer timeQueryType) {
    this.timeQueryType = timeQueryType;
  }

  public Integer getBizType() {
    return bizType;
  }

  public void setBizType(Integer bizType) {
    this.bizType = bizType;
  }

}
