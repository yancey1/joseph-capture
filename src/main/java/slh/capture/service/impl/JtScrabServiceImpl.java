package slh.capture.service.impl;

import java.util.ArrayList;
import java.util.Date;
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
import slh.capture.service.IJtScrabService;
import edu.hziee.common.lang.DateUtil;

@Service("jtService")
public class JtScrabServiceImpl extends ScrabServiceImpl implements IJtScrabService {
  private Logger          logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String month,String mm,String code) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    // 查询天奕达的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.JT.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);
    String[] loginKeys = genLoginKeys();
    for (CapturePlat capturePlat : capturePlats) {
      String[] loginValues = new String[4];
      loginValues[0] = "/wEPDwUKLTUwOTQ0NDQ3MWRkE9/dBKk34T907w5T9FDIPlobWmE=";
      loginValues[1] = "/wEWBALP5bGWBgL3k5CsCwLu6qzaCAKC3IfLCSkYDQCDDPhi1Zra2blsKGhDDr1a";
      loginValues[2] = capturePlat.getUserName();
      loginValues[3] = capturePlat.getPassword();
      boolean loginRet = getLoginHttpClient(client, capturePlat.getLoginUrl(), loginKeys, loginValues);
      if (!loginRet) {
        logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
        return null;
      }
      String indexHtml = getDataQueryHtml(client, capturePlat.getQueryPageUrl());
      String login__VIEWSTATE = HtmlParserUtils.getInputTagValues(indexHtml, "id", "__VIEWSTATE");
      String login__PREVIOUSPAGE = HtmlParserUtils.getInputTagValues(indexHtml, "id", "__PREVIOUSPAGE");
      Map<String, Object> customParamMap = new HashMap<String, Object>();
      customParamMap.put("url", capturePlat.getQueryUrl());
      customParamMap.put("channel", "youyou-dm-jt1");
      customParamMap.put("month", "201407");
      customParamMap.put("__VIEWSTATE", login__VIEWSTATE);
      customParamMap.put("__EVENTVALIDATION", login__PREVIOUSPAGE);
      // 天奕达后台查询结果table属性
      customParamMap.put("table_attr", "id");
      customParamMap.put("table_attr_value", "data_detail");
      // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
      Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
      dataIndex.put(1, DataNameEnum.BIZ_DATE);
      dataIndex.put(2, DataNameEnum.CHANNEL_ID);
      dataIndex.put(4, DataNameEnum.PRODUCT);
      dataIndex.put(5, DataNameEnum.BIZ_AMOUNT);
      customParamMap.put("column_index", dataIndex);
      List<ScrabData> ls = exstractData(client, genParamMap(), customParamMap);
      for (ScrabData scrabData : ls) {
        scrabData.setBizType(ConstantsCMP.TYPE_CPS);
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
    String[] loginKeys = new String[4];
    loginKeys[0] = "__VIEWSTATE";
    loginKeys[1] = "__EVENTVALIDATION";
    loginKeys[2] = "j_username";
    loginKeys[3] = "j_password";
    return loginKeys;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 真趣数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.MONTH, "month");
    return paramMap;
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

  @SuppressWarnings("unchecked")
  @Override
  public List<ScrabData> exstractData(CloseableHttpClient httpClient, Map<QueryParamEnum, String> paramMap, Map<String, Object> customParamMap) {
    List<ScrabData> dataList = new ArrayList<ScrabData>();
    Set<ScrabData> dataSet = new HashSet<ScrabData>();
    String url = (String) customParamMap.get(paramMap.get(QueryParamEnum.URL));
    String month = (String) customParamMap.get(paramMap.get(QueryParamEnum.MONTH));
    String tableAttr = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR.getAttr());
    String tableAttrValue = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr());
    Map<Integer, DataNameEnum> dataIndex = (Map<Integer, DataNameEnum>) customParamMap.get(TableAttributeEnum.COLUMN_INDEX.getAttr());
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.MONTH), month.toString()));
    UrlEncodedFormEntity entity;
    try {
      for (Map.Entry<String, Object> param : customParamMap.entrySet()) {
        Object obj = param.getValue();
        if (obj instanceof Date) {
        } else if (obj instanceof String) {
          String value = param.getValue().toString();
          String[] val = value.split(",");
          for (String v : val) {
            formparams.add(new BasicNameValuePair(param.getKey(), v));
          }
        }
      }
      entity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(entity);
      HttpResponse response = httpClient.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      /*FileWriter file=new FileWriter("d:\\a.text");
      file.write(html);
      file.close();*/
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
}
