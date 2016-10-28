package slh.capture.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.CompanyEnum;
import slh.capture.common.ConstantsCMP;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.common.TableAttributeEnum;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.ScrabData;
import slh.capture.service.ISkyScrabService;

@Service("skyScrabService")
public class SkyScrabServiceImpl extends ScrabServiceImpl implements ISkyScrabService {
  private Logger          logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String month, String imgCode) {
    List<ScrabData> dataList = new ArrayList<ScrabData>();
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.SKY.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);
    String[] loginKeys = genLoginKeys();
    for (CapturePlat capturePlat : capturePlats) {
      String[] loginValues = new String[4];
      loginValues[0] = capturePlat.getUserName();
      loginValues[1] = "密码";
      loginValues[2] = capturePlat.getPassword();
      loginValues[3] = imgCode;
      boolean loginRet = getLoginHttpClient(client, capturePlat.getLoginUrl(), loginKeys, loginValues);
      if (!loginRet) {
        logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
        return null;
      }
      Map<String, Object> customParamMap = new HashMap<String, Object>();
      customParamMap.put("url", capturePlat.getQueryPageUrl());
      customParamMap.put("statDateByMonth", month);
      customParamMap.put("dateFormat", "2");
      customParamMap.put("button", "查询");
      List<String> urlList = exstractAllData(client, genParamMap(), customParamMap);
      if(urlList.size()==20){
        List<String> ll=getMoreData(2,client,capturePlat.getQueryPageUrl(),month);
        urlList.addAll(ll);
      }
      for (String string : urlList) {
        Map<String, Object> customParamDataMap = new HashMap<String, Object>();
        customParamDataMap.put("url", string);
        customParamDataMap.put("table_attr", "class");
        customParamDataMap.put("table_attr_value", "formtable");
        // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
        Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
        dataIndex.put(0, DataNameEnum.BIZ_DATE);
        dataIndex.put(4, DataNameEnum.CHANNEL_ID);
        dataIndex.put(5, DataNameEnum.PRODUCT);
        dataIndex.put(8, DataNameEnum.BIZ_AMOUNT);
        customParamDataMap.put("column_index", dataIndex);
        List<ScrabData> list = exstractZqSkyData(client, genParamDataMap(), customParamDataMap);
        if (list.size() == 20) {
          List<ScrabData> pageList = getPageListMap(2, capturePlat.getQueryUrl(), client, getDate(string), getType(string));
          if (pageList.size() > 0) {
            list.addAll(pageList);
          }
        }
        for (ScrabData scrabData : list) {
          scrabData.setBizType(ConstantsCMP.TYPE_CPS);
        }
        dataList.addAll(list);
      }
    }
    return dataList;
  }

  public List<ScrabData> getPageListMap(int page, String url, CloseableHttpClient client, String date, String type) {
    List<ScrabData> scrList = new ArrayList<ScrabData>();
    Map<String, Object> customParamDataMap = new HashMap<String, Object>();
    customParamDataMap.put("url", "https://bill.fivesky.net/bill/distributionChannelInfo!listDetail.action");
    customParamDataMap.put("statDate", date);
    customParamDataMap.put("filter_EQL_partnerId", "5831601");
    customParamDataMap.put("filter_EQS_channelCode", "16_ouxin_");
    customParamDataMap.put("filter_EQS_distributionType", type);
    customParamDataMap.put("page.pageNo", String.valueOf(page));
    customParamDataMap.put("table_attr", "class");
    customParamDataMap.put("table_attr_value", "formtable");
    // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    dataIndex.put(0, DataNameEnum.BIZ_DATE);
    dataIndex.put(4, DataNameEnum.CHANNEL_ID);
    dataIndex.put(5, DataNameEnum.PRODUCT);
    dataIndex.put(8, DataNameEnum.BIZ_AMOUNT);
    customParamDataMap.put("column_index", dataIndex);
    scrList = getPageList(client, getPageMap(), customParamDataMap);
    if (scrList.size() == 20) {
      page++;
      List<ScrabData> list = getPageListMap(page, url, client, date, type);
      scrList.addAll(list);
    }
    return scrList;
  }

  public Map<QueryParamEnum, String> getPageMap() {
    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "statDate");
    paramMap.put(QueryParamEnum.APP_ID, "filter_EQL_partnerId");
    paramMap.put(QueryParamEnum.CHANNEL_ID, "filter_EQS_channelCode");
    paramMap.put(QueryParamEnum.GROUP_TYPE, "filter_EQS_distributionType");
    paramMap.put(QueryParamEnum.PAGENO, "page.pageNo");
    return paramMap;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 17App联盟数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.MONTH, "statDateByMonth");
    paramMap.put(QueryParamEnum.DATEFORMATE, "dateFormat");
    paramMap.put(QueryParamEnum.BEGINTIME, "button");
    return paramMap;
  }
  
  public Map<QueryParamEnum, String> getMoreParm() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 17App联盟数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.DATETYPE, "statDateByDay");
    paramMap.put(QueryParamEnum.MONTH, "statDateByMonth");
    paramMap.put(QueryParamEnum.DATEFORMATE, "dateFormat");
    paramMap.put(QueryParamEnum.PAGENO, "listByDay.pageNo");
    return paramMap;
  }

  public Map<QueryParamEnum, String> genParamDataMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 17App联盟数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    return paramMap;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[4];
    loginKeys[0] = "loginname";
    loginKeys[1] = "notpassword";
    loginKeys[2] = "password";
    loginKeys[3] = "validateCode";
    return loginKeys;
  }

  /**
   * 
   * 
   * @param httpClient
   * @param paramMap
   * @param customParamMap
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> exstractAllData(CloseableHttpClient httpClient, Map<QueryParamEnum, String> paramMap, Map<String, Object> customParamMap) {

    List<String> dataList = new ArrayList<String>();
    Set<String> dataSet = new HashSet<String>();
    String url = (String) customParamMap.get(paramMap.get(QueryParamEnum.URL));
    String dateFormate = (String) customParamMap.get(paramMap.get(QueryParamEnum.DATEFORMATE));
    String statDateBymonth = (String) customParamMap.get(paramMap.get(QueryParamEnum.MONTH));
    String button = (String) customParamMap.get(paramMap.get(QueryParamEnum.BEGINTIME));

    List<NameValuePair> formparams = new ArrayList<NameValuePair>();

    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.DATEFORMATE), dateFormate.toString()));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.MONTH), statDateBymonth));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.BEGINTIME), button));

    UrlEncodedFormEntity entity;
    try {
      entity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(entity);
      HttpResponse response = httpClient.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<String> parseList = HtmlParserUtils.getLinkTagValues(html);

      dataSet.addAll(parseList);
    } catch (Exception e) {
      e.printStackTrace();
    }

    dataList.addAll(dataSet);

    return dataList;
  }

  @SuppressWarnings("unchecked")
  public List<ScrabData> exstractZqSkyData(CloseableHttpClient httpClient, Map<QueryParamEnum, String> paramMap, Map<String, Object> customParamMap) {

    List<ScrabData> dataList = new ArrayList<ScrabData>();
    Set<ScrabData> dataSet = new HashSet<ScrabData>();
    String url = (String) customParamMap.get(paramMap.get(QueryParamEnum.URL));

    String tableAttr = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR.getAttr());
    String tableAttrValue = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr());

    Map<Integer, DataNameEnum> dataIndex = (Map<Integer, DataNameEnum>) customParamMap.get(TableAttributeEnum.COLUMN_INDEX.getAttr());

    try {
      HttpGet httpget = new HttpGet(url);

      HttpResponse response = httpClient.execute(httpget);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> parseList = HtmlParserUtils.getTableDataValues(html, tableAttr, tableAttrValue, dataIndex);

      dataSet.addAll(parseList);
    } catch (Exception e) {
      e.printStackTrace();
    }

    dataList.addAll(dataSet);
    return dataList;
  }

  @SuppressWarnings("unchecked")
  public List<ScrabData> getPageList(CloseableHttpClient httpClient, Map<QueryParamEnum, String> paramMap, Map<String, Object> customParamMap) {

    List<ScrabData> dataList = new ArrayList<ScrabData>();
    Set<ScrabData> dataSet = new HashSet<ScrabData>();
    String url = (String) customParamMap.get(paramMap.get(QueryParamEnum.URL));
    String time = (String) customParamMap.get(paramMap.get(QueryParamEnum.START_TIME));
    String appId = (String) customParamMap.get(paramMap.get(QueryParamEnum.APP_ID));
    String channelId = (String) customParamMap.get(paramMap.get(QueryParamEnum.CHANNEL_ID));
    String type = (String) customParamMap.get(paramMap.get(QueryParamEnum.GROUP_TYPE));
    String page = (String) customParamMap.get(paramMap.get(QueryParamEnum.PAGENO));

    String tableAttr = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR.getAttr());
    String tableAttrValue = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr());
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();

    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.START_TIME), time.toString()));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.APP_ID), appId));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.CHANNEL_ID), channelId));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.GROUP_TYPE), type));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.PAGENO), page));
    Map<Integer, DataNameEnum> dataIndex = (Map<Integer, DataNameEnum>) customParamMap.get(TableAttributeEnum.COLUMN_INDEX.getAttr());
    UrlEncodedFormEntity entity;
    try {
      entity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(entity);
      HttpResponse response = httpClient.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> parseList = HtmlParserUtils.getTableDataValues(html, tableAttr, tableAttrValue, dataIndex);
      dataSet.addAll(parseList);
    } catch (Exception e) {
      e.printStackTrace();
    }
    dataList.addAll(dataSet);
    return dataList;
  }

  public String getType(String url) {
    String type = "";
    if (StringUtils.isNotBlank(url)) {
      String ary[] = url.substring(url.lastIndexOf("?")).split("&");
      type = ary[4].split("=")[1];
    }
    return type;
  };

  public String getDate(String url) {
    String date = "";
    if (StringUtils.isNotBlank(url)) {
      String ary[] = url.substring(url.lastIndexOf("?")).split("&");
      date = ary[1].split("=")[1];
    }
    return date;
  };
  
  public List<String> getMoreData(int page,CloseableHttpClient client,String url,String month){
    Map<String, Object> customParamMap = new HashMap<String, Object>();
    customParamMap.put("url", url);
    customParamMap.put("statDateByDay", "");
    customParamMap.put("statDateByMonth", month);
    customParamMap.put("dateFormat", "2");
    customParamMap.put("listByDay.pageNo",String.valueOf(page));
    List<String> urlList = moreAllDate(client, getMoreParm(), customParamMap);
    if(urlList.size()==20){
      page++;
      List<String> ll=getMoreData(page,client,url,month);
      urlList.addAll(ll);
    }
    return urlList;
  }
  
  public List<String> moreAllDate(CloseableHttpClient httpClient, Map<QueryParamEnum, String> paramMap, Map<String, Object> customParamMap) {

    List<String> dataList = new ArrayList<String>();
    Set<String> dataSet = new HashSet<String>();
    String url = (String) customParamMap.get(paramMap.get(QueryParamEnum.URL));
    String dateFormate = (String) customParamMap.get(paramMap.get(QueryParamEnum.DATEFORMATE));
    String statDateBymonth = (String) customParamMap.get(paramMap.get(QueryParamEnum.MONTH));
    String button = (String) customParamMap.get(paramMap.get(QueryParamEnum.DATETYPE));
    String page = (String) customParamMap.get(paramMap.get(QueryParamEnum.PAGENO));
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.DATEFORMATE), dateFormate.toString()));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.MONTH), statDateBymonth));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.DATETYPE), button));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.PAGENO), page));
    UrlEncodedFormEntity entity;
    try {
      entity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(entity);
      HttpResponse response = httpClient.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<String> parseList = HtmlParserUtils.getLinkTagValues(html);

      dataSet.addAll(parseList);
    } catch (Exception e) {
      e.printStackTrace();
    }

    dataList.addAll(dataSet);

    return dataList;
  }
 /* public static void main(String[] args) {
    String url = "https://bill.fivesky.net/bill/distributionChannelInfo!listDetail.action?statDate=2014-08-21&filter_EQL_partnerId=5831601&filter_EQS_channelCode=16_ouxin_&filter_EQS_distributionType=DISTRIBUTION_CHANNEL_TYPE_1";
    String ary[] = url.substring(url.lastIndexOf("?"), url.indexOf("&")).split("=");
    System.out.println(ary[1]);
  }*/
}
