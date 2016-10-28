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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.BizTypeEnum;
import slh.capture.common.CompanyEnum;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.dao.IScrabDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.ScrabData;
import slh.capture.service.ISojTaoScrabService;
import edu.hziee.common.lang.DateUtil;

@Service("sojTaoService")
public class SojTaoScrabServiceImpl extends ScrabServiceImpl implements ISojTaoScrabService {

  private Logger          logger        = LoggerFactory.getLogger(getClass());
  private IScrabDAO       scrabDAO;
  private int             MAX_QUERY_DAY = 10;
  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    List<ScrabData> dataList = new ArrayList<ScrabData>();

    // 查询手淘的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.SOJTAO.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);
    String[] loginKeys = genLoginKeys();

    for (CapturePlat capturePlat : capturePlats) {

      String[] loginValues = getLoginValues(capturePlat.getUserName(), capturePlat.getPassword(), imgCode);

      boolean loginRet = getLoginHttpClient(client, capturePlat.getLoginUrl(), loginKeys, loginValues);
      String indexHtml = getDataQueryHtml(client, capturePlat.getQueryPageUrl());

      String login__VIEWSTATE = HtmlParserUtils.getInputTagValues(indexHtml, "id", "__VIEWSTATE");
      String login__PREVIOUSPAGE = HtmlParserUtils.getInputTagValues(indexHtml, "id", "__PREVIOUSPAGE");
      String login__EVENTVALIDATION = HtmlParserUtils.getInputTagValues(indexHtml, "id", "__EVENTVALIDATION");

      // 获取标签的值

      Map<String, Object> customParamMap = new HashMap<String, Object>();
      customParamMap.put("url", capturePlat.getQueryUrl());

      customParamMap.put("ctl00$ContentPlaceHolder1$dateFrom", DateUtil.parseDate(startDate, DateUtil.TRADITION_PATTERN));
      customParamMap.put("ctl00$ContentPlaceHolder1$dateTo", DateUtil.parseDate(endDate, DateUtil.TRADITION_PATTERN));
      customParamMap.put("ctl00$ContentPlaceHolder1$Statusdl", "未结算");
      customParamMap.put("ctl00$ContentPlaceHolder1$ADDdl", "0");
      customParamMap.put("ctl00$ContentPlaceHolder1$AspNetPager1_input", "1");
      customParamMap.put("ctl00$ContentPlaceHolder1$btCommit", "查询");
      customParamMap.put("__VIEWSTATE", login__VIEWSTATE);
      customParamMap.put("__PREVIOUSPAGE", login__PREVIOUSPAGE);
      customParamMap.put("__EVENTVALIDATION", login__EVENTVALIDATION);
      customParamMap.put("__EVENTARGUMENT", "");
      customParamMap.put("__EVENTTARGET", "");

      // 手淘后台查询结果table属性
      customParamMap.put("table_attr", "class");
      customParamMap.put("table_attr_value", "connecttable");

      // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
      Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
      dataIndex.put(0, DataNameEnum.BIZ_DATE);
      dataIndex.put(1, DataNameEnum.PRODUCT);
      dataIndex.put(4, DataNameEnum.BIZ_AMOUNT);
      customParamMap.put("column_index", dataIndex);

      List<ScrabData> scrabDataList = exstractData(client, genParamMap(), customParamMap);
      if (scrabDataList != null) {
        for (ScrabData data : scrabDataList) {
          data.setBizType(BizTypeEnum.CPA.getCode());
          data.setChannelId("花花");
          dataList.add(data);
        }
      }

    }
    return dataList;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 手淘数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "ctl00$ContentPlaceHolder1$dateFrom");
    paramMap.put(QueryParamEnum.END_TIME, "ctl00$ContentPlaceHolder1$dateTo");
    return paramMap;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[7];
    loginKeys[0] = "txtUserName";
    loginKeys[1] = "txtUserPass";
    loginKeys[2] = "txtYanzhengma";
    loginKeys[3] = "__EVENTVALIDATION";
    loginKeys[4] = "__VIEWSTATE";
    loginKeys[5] = "LoginSubmit.x";
    loginKeys[6] = "LoginSubmit.y";
    return loginKeys;
  }

  public String[] getLoginValues(String userName, String password, String imageCode) {
    String[] loginValues = new String[7];
    loginValues[0] = userName;
    loginValues[1] = password;
    loginValues[2] = imageCode;
    loginValues[3] = "/wEWBgKH8saGCQKl1bKzCQKrlJGNAgLnyrjnBwLFvMu4DAKPrri7D4TBlQt96JKYNlzKqU2j98JIKlpT";
    loginValues[4] = "/wEPDwULLTE3MTU2MTkyNTdkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBQtMb2dpblN1Ym1pdAUJcmVnQnV0dG9utwbj9h+FZ7P3rV/7FeX1gjquF+I=";
    loginValues[5] = "43";
    loginValues[6] = "16";
    return loginValues;
  }

  public String exstractSojTaoDataPageData(CloseableHttpClient httpClient, Map<QueryParamEnum, String> paramMap, Map<String, Object> customParamMap) {

    List<ScrabData> dataList = new ArrayList<ScrabData>();
    Set<ScrabData> dataSet = new HashSet<ScrabData>();
    String url = (String) customParamMap.get(paramMap.get(QueryParamEnum.URL));

    List<NameValuePair> formparams = new ArrayList<NameValuePair>();

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

    HttpResponse response = null;

    String html = null;
    try {
      HttpGet httpGet = new HttpGet(url);
      response = httpClient.execute(httpGet);
      HttpEntity httpEntity = response.getEntity();
      html = EntityUtils.toString(response.getEntity());

      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    dataList.addAll(dataSet);

    return html;

  }
}
