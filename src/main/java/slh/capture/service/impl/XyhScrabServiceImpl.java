package slh.capture.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.CapturePlatEnum;
import slh.capture.common.CompanyEnum;
import slh.capture.common.ConstantsCMP;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.common.TableAttributeEnum;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.ScrabData;
import slh.capture.service.IXyhScrabService;
import edu.hziee.common.lang.DateUtil;

@Service("xyhService")
public class XyhScrabServiceImpl extends ScrabServiceImpl implements IXyhScrabService {
  private Logger          logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String randCode) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    // 查询新银河的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.XYH.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);
    String[] loginKeys = genLoginKeys();
    for (CapturePlat capturePlat : capturePlats) {
      String[] loginValues = new String[3];
      loginValues[0] = capturePlat.getUserName();
      loginValues[1] = capturePlat.getPassword();
      loginValues[2] = randCode;
      try {
        // 先打开登录页面
        this.loginPage(capturePlat.getLoginUrl(), client);
      } catch (Exception e) {
        logger.error("login page is failed! loginUrl=[{}], userName=[{}] , password=[{}], randCode=[{}]", new Object[] { capturePlat.getLoginUrl(),
            loginValues[0], loginValues[1], loginValues[2] });
        logger.error(e.getMessage(), e);
      }
      boolean loginRet = getLoginHttpClient(client, capturePlat.getLoginUrl(), loginKeys, loginValues);
      if (!loginRet) {
        logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
        return null;
      }
      Map<String, Object> customParamMap = new HashMap<String, Object>();
      customParamMap.put(QueryParamEnum.URL.getKey(), capturePlat.getQueryUrl());
      customParamMap.put(QueryParamEnum.START_DATE.getKey(), startDate);
      customParamMap.put(QueryParamEnum.END_DATE.getKey(), endDate);
      // 新银河后台查询结果table属性
      customParamMap.put(TableAttributeEnum.TABLE_ATTR.getAttr(), "id");
      customParamMap.put(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr(), "searchdatalist");
      // 页面数据的展示顺序,如日期(1), 渠道号(2), 产品(3), 客户实收(4),括号里为下标
      Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
      dataIndex.put(1, DataNameEnum.BIZ_DATE);
      dataIndex.put(2, DataNameEnum.CHANNEL_ID);
      dataIndex.put(3, DataNameEnum.PRODUCT);
      dataIndex.put(4, DataNameEnum.BIZ_AMOUNT);
      customParamMap.put(TableAttributeEnum.COLUMN_INDEX.getAttr(), dataIndex);
      List<ScrabData> ls = exstractGcData(client, customParamMap);
      for (ScrabData scrabData : ls) {
        scrabData.setBizType(ConstantsCMP.TYPE_CPA);
        String date = scrabData.getBizDate();
        scrabData.setBizDate(date.substring(0, 10));
      }
      if (ls != null) {
        list.addAll(ls);
      }
    }
    return list;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[3];
    loginKeys[0] = "username";
    loginKeys[1] = "password";
    loginKeys[2] = "authcode";
    return loginKeys;
  }

  @Override
  public String[] genLoginValues(String imgCode) {

    String[] loginValues = new String[3];
    loginValues[0] = CapturePlatEnum.TYD.getUserName();
    loginValues[1] = CapturePlatEnum.TYD.getPassword();
    loginValues[2] = imgCode;
    return loginValues;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 真趣数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_DATE, "startTime");
    paramMap.put(QueryParamEnum.END_DATE, "endTime");
    paramMap.put(QueryParamEnum.DATEFORMATE, "dateFormate");
    paramMap.put(QueryParamEnum.PAGENO, "page.pageNo");
    return paramMap;
  }

  @Override
  public Map<String, Object> genCustomParamMap(String startTime, String endTime, String productId, String channelId) {
    Map<String, Object> customParamMap = new HashMap<String, Object>();
    customParamMap.put("url", CapturePlatEnum.JOY.getQueryUrl());
    customParamMap.put("startTime", DateUtil.parseDate(startTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("endTime", DateUtil.parseDate(endTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("appId", productId);
    customParamMap.put("channelId", channelId);

    // 真趣后台查询结果table属性
    customParamMap.put("table_attr", "id");
    customParamMap.put("table_attr_value", "cl");

    // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    dataIndex.put(1, DataNameEnum.BIZ_DATE);
    dataIndex.put(2, DataNameEnum.PRODUCT);
    dataIndex.put(3, DataNameEnum.CHANNEL_ID);
    dataIndex.put(4, DataNameEnum.BIZ_TYPE);
    dataIndex.put(5, DataNameEnum.BIZ_AMOUNT);
    customParamMap.put("column_index", dataIndex);
    return customParamMap;
  }

  public Map<String, Object> genMMCustomParamMap(String startTime, String endTime, String productId, String channelId) {
    Map<String, Object> customParamMap = new HashMap<String, Object>();
    customParamMap.put("url", CapturePlatEnum.JOY_MM.getQueryUrl());
    customParamMap.put("query.beginTime", DateUtil.parseDate(startTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("query.endTime", DateUtil.parseDate(endTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("query.appId", productId);
    customParamMap.put("query.channelId", channelId);

    // 真趣后台查询结果table属性
    customParamMap.put("table_attr", "id");
    customParamMap.put("table_attr_value", "cl");

    // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    dataIndex.put(0, DataNameEnum.BIZ_DATE);
    dataIndex.put(1, DataNameEnum.PRODUCT);
    dataIndex.put(2, DataNameEnum.CHANNEL_ID);
    dataIndex.put(3, DataNameEnum.BIZ_AMOUNT);
    customParamMap.put("column_index", dataIndex);
    return customParamMap;
  }

  public Map<QueryParamEnum, String> genMMParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 真趣数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "query.beginTime");
    paramMap.put(QueryParamEnum.END_TIME, "query.endTime");
    paramMap.put(QueryParamEnum.PRODUCT_ID, "query.appId");
    paramMap.put(QueryParamEnum.CHANNEL_ID, "query.channelId");
    return paramMap;
  }

  /**
   * 泰酷数据解析
   * 
   * @param httpClient
   * @param paramMap
   * @param customParamMap
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ScrabData> exstractGcData(CloseableHttpClient httpClient, Map<String, Object> customParamMap) {
    List<ScrabData> dataList = new ArrayList<ScrabData>();
    Set<ScrabData> dataSet = new HashSet<ScrabData>();
    String url = (String) customParamMap.get(QueryParamEnum.URL.getKey());
    String statDate = (String) customParamMap.get(QueryParamEnum.START_DATE.getKey());
    String endDate = (String) customParamMap.get(QueryParamEnum.END_DATE.getKey());
    String channelId = "0";
    String appId = "4";
    String grouptype = "day";

    String tableAttr = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR.getAttr());
    String tableAttrValue = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr());
    Map<Integer, DataNameEnum> dataIndex = (Map<Integer, DataNameEnum>) customParamMap.get(TableAttributeEnum.COLUMN_INDEX.getAttr());

    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair(QueryParamEnum.PAGENO.getKey(), "1"));
    formparams.add(new BasicNameValuePair(QueryParamEnum.START_DATE.getKey(), statDate));
    formparams.add(new BasicNameValuePair(QueryParamEnum.END_DATE.getKey(), endDate));
    formparams.add(new BasicNameValuePair(QueryParamEnum.CHANNEL_ID.getKey(), channelId));
    formparams.add(new BasicNameValuePair(QueryParamEnum.APP_ID.getKey(), appId));
    formparams.add(new BasicNameValuePair(QueryParamEnum.GROUP_TYPE.getKey(), grouptype));

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
      logger.error("TydScrabServiceImpl.exstractGcData() is failed!");
      logger.error(e.getMessage(), e);
    }
    dataList.addAll(dataSet);
    return dataList;
  }

}
