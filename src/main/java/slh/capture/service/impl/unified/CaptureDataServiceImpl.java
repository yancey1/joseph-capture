package slh.capture.service.impl.unified;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import slh.capture.common.AppListEnum;
import slh.capture.common.AppOceanList;
import slh.capture.common.BizTypeEnum;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.JsonParserUtils;
import slh.capture.common.unified.TimeQueryTypeEnum;
import slh.capture.domain.JsonDyQuery;
import slh.capture.domain.JsonDyQueryResult;
import slh.capture.domain.JsonHxQuery;
import slh.capture.domain.JsonWmQuery;
import slh.capture.domain.JsonWsyQuery;
import slh.capture.domain.JsonWsyQueryResult;
import slh.capture.domain.ScrabData;
import slh.capture.domain.oms.AppQuery;
import slh.capture.domain.oms.AppQueryResult;
import slh.capture.domain.oms.ChannelQuery;
import slh.capture.domain.oms.KzwQuery;
import slh.capture.domain.oms.KzwQueryResult;
import slh.capture.domain.oms.KzwSdkQuery;
import slh.capture.domain.oms.KzwSdkQueryResult;
import slh.capture.domain.oms.WbQuery;
import slh.capture.domain.oms.WbQueryResult;
import slh.capture.domain.oms.XyhQuery;
import slh.capture.domain.oms.XyhQueryResult;
import slh.capture.domain.oms.ZmQuery;
import slh.capture.domain.oms.ZmQueryResult;
import slh.capture.domain.unified.CaptureConfigEntity;
import slh.capture.form.unified.CaptureQueryConditionForm;

import com.google.gson.reflect.TypeToken;

public class CaptureDataServiceImpl {
  private static final Logger     logger     = LoggerFactory.getLogger(CaptureDataServiceImpl.class);
  private static final DateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");

  public static String getDataQueryHtml(CloseableHttpClient httpClient, String queryUrl) {
    HttpResponse response = null;
    try {
      HttpGet httpGet = new HttpGet(queryUrl);
      logger.debug("get query page html by url=[{}]", queryUrl);
      response = httpClient.execute(httpGet);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      return html;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // 掌盟
  public static List<ScrabData> getZm(CaptureQueryConditionForm form, String startTime, String endTime, CaptureConfigEntity entity) {
    ClientConnectionManager cm = new MyBasicClientConnectionManager();
    DefaultHttpClient httpclient = new DefaultHttpClient(cm);
    HttpGet httpget = new HttpGet(entity.getQueryPageUrl());
    HttpResponse response;
    HttpEntity httpEntity;
    try {
      response = httpclient.execute(httpget);
      httpEntity = response.getEntity();
      String data = EntityUtils.toString(httpEntity);
      if (!data.trim().contains("登录成功")) {
        return null;
      }
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    List<ScrabData> list = new ArrayList<ScrabData>();
    try {
      String url = entity.getQueryUrl();
      url += "?beginday=" + form.getStartDate() + "&endday=" + form.getEndDate();
      if (StringUtils.isNotBlank(form.getChannelCode())) {
        url += "&partner=" + form.getChannelCode();
      }
      if (StringUtils.isNotBlank(form.getAppName())) {
        url += "&appid=" + form.getAppName();
      }
      HttpPost httppost = new HttpPost(url);
      response = httpclient.execute(httppost);
      httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      ZmQuery zmQuery = (ZmQuery) JsonParserUtils.parseFromJsonForObject(html, ZmQuery.class);
      List<ZmQueryResult> zmList = zmQuery.getRows();
      for (ZmQueryResult zmQueryResult : zmList) {
        if (zmQueryResult.getfDay().contains("合计")) {
          continue;
        }
        ScrabData data = new ScrabData();
        data.setBizDate(zmQueryResult.getfDay());
        data.setBizAmount(zmQueryResult.getfAmt());
        data.setBizType(entity.getBizType());
        data.setUserName(entity.getUserName());
        if (StringUtils.isNotBlank(entity.getChannelCode())) {
          data.setChannelId(entity.getChannelCode());
        } else {
          data.setChannelId(zmQueryResult.getfPartner());
        }
        if (StringUtils.isNotBlank(entity.getAppName())) {
          data.setProductName(entity.getAppName());
        } else {
          data.setProductName(zmQueryResult.getfAppName());
        }
        list.add(data);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("getZm is error");
      e.getMessage();
    }
    return list;
  }

  // 聚塔
  public static List<ScrabData> getJt(CloseableHttpClient client, String startTime, CaptureConfigEntity entity, CaptureQueryConditionForm conditionform,
      List<CaptureConfigEntity> entityList) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    String url = entity.getQueryUrl().trim();
    String param = entity.getParams().trim();
    UrlEncodedFormEntity tity;
    String month = startTime.split("-")[1];
    String year = startTime.split("-")[0];
    String time = "";
    time += String.valueOf(year) + month;
    String indexHtml = getDataQueryHtml(client, entity.getQueryPageUrl());
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(param)) {
        String parAry[] = param.split(",");
        int i = 1;
        for (String string : parAry) {
          String[] arm = string.split(":");
          if (StringUtils.isNotBlank(arm[0])) {
            if (i == 1) {
              formparams.add(new BasicNameValuePair(arm[0], time));
            } else if (i == 2) {
              formparams.add(new BasicNameValuePair(arm[0], HtmlParserUtils.getInputTagValues(indexHtml, "id", "__VIEWSTATE")));
            } else if (i == 3) {
              formparams.add(new BasicNameValuePair(arm[0], HtmlParserUtils.getInputTagValues(indexHtml, "id", "__EVENTVALIDATION")));
            } else {
              if (arm.length == 2) {
                formparams.add(new BasicNameValuePair(arm[0], arm[1]));
              }
            }
            i++;
          }
        }
      }
      List<String> option = HtmlParserUtils.getSelectTagValues(indexHtml, "id", "channel");
      for (int i = 1; i < option.size(); i++) {
        if (formparams.size() > 4) {
          formparams.remove(4);
        }
        formparams.add(new BasicNameValuePair("channel", option.get(i)));
        conditionform.setChannelCode(option.get(i));
        tity = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(tity);
        HttpResponse response = client.execute(httppost);
        HttpEntity httpEntity = response.getEntity();
        String html = EntityUtils.toString(response.getEntity());
        if (entity != null) {
          EntityUtils.consume(httpEntity);
        }
        List<ScrabData> parseList = HtmlParserUtils.getJtTableDataValues(html, entity, conditionform, entityList);
        list.addAll(parseList);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  // 唯变
  public static List<ScrabData> getWb(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<CaptureConfigEntity> entityList) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    String url = entity.getQueryUrl().trim();
    String param = entity.getParams().trim();
    UrlEncodedFormEntity tity;
    String ary[] = startTime.split("-");
    String month = ary[0] + ary[1];
    startTime = startTime.replaceAll("-", "");
    endTime = endTime.replaceAll("-", "");
    try {
      if (StringUtils.isNotBlank(param)) {
        String parAry[] = param.split(",");
        int i = 1;
        for (String string : parAry) {
          String[] arm = string.split(":");
          if (StringUtils.isNotBlank(arm[0])) {
            if (i == 1) {
              formparams.add(new BasicNameValuePair(arm[0], startTime));
            } else if (i == 2) {
              formparams.add(new BasicNameValuePair(arm[0], endTime));
            } else if (i == 3) {
              formparams.add(new BasicNameValuePair(arm[0], month));
            } else {
              if (arm.length == 2) {
                if (arm[0].equals("groupByStr")) {
                  String gor[] = arm[1].split("-");
                  formparams.add(new BasicNameValuePair(arm[0], gor[0] + "," + gor[1] + "," + gor[2]));
                } else {
                  formparams.add(new BasicNameValuePair(arm[0], arm[1]));
                }
              }
            }
            i++;
          }
        }
      }
      tity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(tity);
      HttpResponse response = client.execute(httppost);
      String html = EntityUtils.toString(response.getEntity());
      WbQuery wbQuery = (WbQuery) JsonParserUtils.parseFromJsonForObject(html, WbQuery.class);
      List<WbQueryResult> wbList = wbQuery.getRoot();
      for (WbQueryResult wbQueryResult : wbList) {
        ScrabData data = new ScrabData();
        String bizDate = wbQueryResult.getExpdate();
        bizDate = new String(bizDate.getBytes("iso-8859-1"), "UTF-8");
        if (bizDate.contains("总计") || StringUtils.isBlank(bizDate)) {
          continue;
        }
        bizDate = bizDate.substring(0, 4) + "-" + bizDate.substring(4, 6) + "-" + bizDate.substring(6, 8);
        data.setBizDate(bizDate);
        data.setBizAmount(wbQueryResult.getTotalfee());
        String appName = wbQueryResult.getApp_name();
        appName = new String(appName.getBytes("iso-8859-1"), "UTF-8");
        data.setProductName(appName);
        data.setChannelId(wbQueryResult.getHz_name());
        for (CaptureConfigEntity bean : entityList) {
          if (bean.getChannelCode().equals(wbQueryResult.getHz_name())) {
            data.setChannelId(bean.getChannelCode());
            data.setProductName(bean.getAppName());
            break;
          }
        }
        data.setUserName(conditionform.getUserName());
        data.setBizType(entity.getBizType());
        list.add(data);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  // 怡乐
  public static List<ScrabData> getYl(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    String url = entity.getQueryUrl().trim();
    String param = entity.getParams().trim();
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    UrlEncodedFormEntity tity;
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(param)) {
        String parAry[] = param.split(",");
        int i = 1;
        for (String string : parAry) {
          String[] arm = string.split(":");
          if (StringUtils.isNotBlank(arm[0])) {
            if (i == 1) {
              formparams.add(new BasicNameValuePair(arm[0], startTime));
            } else if (i == 2) {
              formparams.add(new BasicNameValuePair(arm[0], endTime));
            } else {
              if (arm.length == 2) {
                formparams.add(new BasicNameValuePair(arm[0], arm[1]));
              }
            }
            i++;
          }
        }
      }
      tity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(tity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> parseList = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      if (parseList.size() == 30) {
        List<ScrabData> ll = CapturePageServiceImpl.getYlPage(client, startTime, endTime, 2, dataIndex, entity);
        parseList.addAll(ll);
      }
      for (ScrabData scrabData : parseList) {
        scrabData.setChannelId(entity.getChannelCode());
        scrabData.setUserName(conditionform.getUserName());
        scrabData.setBizType(entity.getBizType());
      }
      list.addAll(parseList);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  // 新银河
  public static List<ScrabData> getXyh(int page, CloseableHttpClient client, String startDate, String endDate, CaptureConfigEntity config,
      CaptureQueryConditionForm conditionform) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    try {
      String html = "";
      startDate = "\"" + startDate + "\"";
      endDate = "\"" + endDate + "\"";
      String channelHtml = postByJson(client, config.getQueryUrl(), "{\"reqType\":\"F9902015\"}");
      ChannelQuery channelQuery = (ChannelQuery) JsonParserUtils.parseFromJsonForObject(channelHtml, ChannelQuery.class);
      List<AppQueryResult> channelList = channelQuery.getCpList();
      for (int i = 0; i < channelList.size(); i++) {
        String cpId = channelList.get(i).getId();
        cpId = "\"" + cpId + "\"";
        html = postByJson(client, config.getQueryUrl(), "{\"reqType\":\"F9902014\",\"cpid\":" + cpId + ",\"startDate\":" + startDate + ",\"endDate\":"
            + endDate + ",\"cppid\":\"-1\",\"pagesize\":20,\"currentpage\":" + page + ",\"totalRows\":0}");
        XyhQuery xyhQuery = (XyhQuery) JsonParserUtils.parseFromJsonForObject(html, XyhQuery.class);
        List<XyhQueryResult> rowsList = xyhQuery.getRows();
        if (rowsList.size() == 20) {
          page++;
          List<XyhQueryResult> dataList = CapturePageServiceImpl.getXyhPage(client, startDate, endDate, 2, config, Integer.parseInt(xyhQuery.getTotalrows()),
              cpId);
          rowsList.addAll(dataList);
        }
        String selectHtml = getSelectValue(client, config.getQueryUrl(), "{\"reqType\":\"F9902016\",\"cpid\":" + cpId + "}");
        AppQuery appQuery = (AppQuery) JsonParserUtils.parseFromJsonForObject(selectHtml, AppQuery.class);
        List<AppQueryResult> appList = appQuery.getProducts();
        for (XyhQueryResult query : rowsList) {
          ScrabData data = new ScrabData();
          data.setUserName(conditionform.getUserName());
          if (StringUtils.isNotBlank(config.getChannelCode())) {
            data.setChannelId(config.getChannelCode());
          } else {
            data.setChannelId(xyhQuery.getCpid());
          }
          String date = query.getDate().replaceAll("\\.", "-");
          data.setBizDate(date);
          data.setBizAmount(query.getMoney());
          data.setBizType(config.getBizType());
          for (AppQueryResult appQueryResult : appList) {
            if (query.getCppid().equals(appQueryResult.getId())) {
              data.setProductName(appQueryResult.getName());
              break;
            }
          }
          list.add(data);
        }
      }
    } catch (Exception e) {
      logger.error("UnifiedCaptureServiceImpl.excute() error with json.. conditionform=[{}]", "");
      logger.error(e.getMessage(), e);
    }
    return list;
  }

  // 彩云
  public static List<ScrabData> getCy(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    String url = entity.getQueryUrl().trim();
    String param = entity.getParams().trim();
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(param)) {
        String parAry[] = param.split(",");
        int i = 1;
        for (String string : parAry) {
          String[] arm = string.split(":");
          if (StringUtils.isNotBlank(arm[0])) {
            if (i == 1) {
              String time[] = startTime.split("-");
              url += "?" + arm[0] + "=" + time[0] + "%2F" + time[1] + "%2F" + time[2];
            } else if (i == 2) {
              String time[] = endTime.split("-");
              url += "&" + arm[0] + "=" + time[0] + "%2F" + time[1] + "%2F" + time[2];
            } else {
              if (arm.length == 2) {
                url += "&" + arm[0] + "=" + arm[1];
              }
            }
            i++;
          }
        }
      }
      String indexHtml = getDataQueryHtml(client, entity.getQueryPageUrl());
      List<String> option = HtmlParserUtils.getSelectTagValues(indexHtml, "id", "Scid");
      for (int i = 1; i < option.size(); i++) {
        String uu = url + "&Scid=" + option.get(i);
        HttpGet httppost = new HttpGet(uu);
        HttpResponse response = client.execute(httppost);
        HttpEntity httpEntity = response.getEntity();
        String html = EntityUtils.toString(response.getEntity());
        if (httpEntity != null) {
          EntityUtils.consume(httpEntity);
        }
        List<ScrabData> parseList = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
        if (parseList.size() == 20) {
          List<ScrabData> ll = CapturePageServiceImpl.getCyPage(client, uu, 2, entity, dataIndex);
          parseList.addAll(ll);
        }
        for (ScrabData scrabData : parseList) {
          scrabData.setChannelId(option.get(i));
          scrabData.setUserName(conditionform.getUserName());
          scrabData.setBizType(entity.getBizType());
          scrabData.setProductName(entity.getAppName());
        }
        list.addAll(parseList);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  // 深圳云朗
  public static List<ScrabData> getSzyl(CloseableHttpClient client, String startDate, String endDate, CaptureConfigEntity config,
      CaptureQueryConditionForm conditionform) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    HttpResponse response = null;
    String url = config.getQueryUrl();
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    url += "?username=zq";
    try {
      if (StringUtils.isNotBlank(config.getDataIndex())) {
        String dataIndexParamArgs[] = config.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      HttpGet httpGet = new HttpGet(url);
      logger.debug("get query page html by url=[{}]", url);
      response = client.execute(httpGet);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> parseList = HtmlParserUtils
          .getTableDataValues(html, config.getTableAttr(), "margin: 20px; width: 980px; background: #F1F1F1;", dataIndex);
      List<ScrabData> removeList = new ArrayList<ScrabData>();
      for (ScrabData scrabData : parseList) {
        if (!compareDay(scrabData.getBizDate(), startDate, endDate)) {
          removeList.add(scrabData);
          continue;
        }
        ;
        scrabData.setUserName(conditionform.getUserName());
        scrabData.setBizType(config.getBizType());
        scrabData.setProductName(config.getAppName());
        if (StringUtils.isNotBlank(config.getChannelCode())) {
          scrabData.setChannelId(config.getChannelCode());
        }
      }
      parseList.removeAll(removeList);
      list.addAll(parseList);
    } catch (Exception e) {
      logger.error("szyl is error------------------");
    }
    return list;
  }

  // 移动后台
  public static List<ScrabData> get10086(CloseableHttpClient client, String startDate, String endDate, CaptureConfigEntity config,
      CaptureQueryConditionForm conditionform) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    try {
      int timeQueryType = config.getTimeQueryType();
      if (TimeQueryTypeEnum.EVERY_DAY_IN_REGION.getType() == timeQueryType) {// 区间内的每一天
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(simpleDate.parse(startDate));
        endDay.setTime(simpleDate.parse(endDate));
        List<String> listDay = getListDay(startDay, endDay);
        if (listDay == null) {
          logger.info("enter execute(), but the listDay is null! conditionform=[{}]", conditionform.toString());
          return null;
        }
        String params = config.getParams();
        if (StringUtils.isBlank(params)) {
          logger.info("enter execute(), but the params is null! conditionform=[{}]", conditionform.toString());
          return null;
        }
        for (String day : listDay) {
          String html = "";
          String bizDate = day;
          if (StringUtils.isNotBlank(day)) {
            day = day.replaceAll("-", "");
            html = postByJson(client, config.getQueryUrl(),
                "{CommandName:'AP_V4.Chan_GetAllAppListPageData',Params:{\"BEGIN_ROW\":\"1\",\"END_ROW\":\"10\",\"ORDER_COLLUMN\":\"PAYABLE DESC\",\"INT_DAY\":"
                    + day + ",\"DATE_TYPE\":\"1\",\"AP_ID\":" + params.split(":")[1].trim() + ",\"APP_NAME\":\"\"}}");
          }
          html = html.replaceAll("\\[\\[", "[");
          html = html.replaceAll("\\]\\]", "]");
          html = html.replaceAll("\\],\\[", ",");
          JsonWmQuery wmObj = (JsonWmQuery) JsonParserUtils.parseFromJsonForObject(html, JsonWmQuery.class);
          List<String> rowsList = wmObj.getRows();
          if (rowsList.size() > 0) {
            for (int i = 0; i < rowsList.size() / 8; i++) {
              ScrabData data = new ScrabData();
              int j = i * 8;
              if (StringUtils.isBlank(conditionform.getAppName())) {
                data.setBizAmount(rowsList.get(j + 2));
                data.setProductName(rowsList.get(j + 1));
                data.setProductId(rowsList.get(j));
                data.setBizDate(bizDate);
                data.setBizType(config.getBizType());
                if (StringUtils.isNotBlank(config.getChannelCode())) {
                  data.setChannelId(config.getChannelCode());
                } else {
                  data.setChannelId(rowsList.get(j));
                }
                data.setUserName(conditionform.getUserName());
                list.add(data);
              } else if (conditionform.getAppName().contains(rowsList.get(j + 1))) {
                data.setBizAmount(rowsList.get(j + 2));
                data.setProductName(rowsList.get(j + 1));
                data.setProductId(rowsList.get(j));
                data.setBizDate(bizDate);
                data.setBizType(config.getBizType());
                if (StringUtils.isNotBlank(config.getChannelCode())) {
                  data.setChannelId(config.getChannelCode());
                } else {
                  data.setChannelId(rowsList.get(j));
                }
                data.setUserName(conditionform.getUserName());
                list.add(data);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("UnifiedCaptureServiceImpl.excute() error with json.. conditionform=[{}]", conditionform.toString());
      logger.error(e.getMessage(), e);
    }
    return list;
  }

  // OCEAN后台
  public static List<ScrabData> getOceanCp(CloseableHttpClient client, String startDate, String endDate, CaptureConfigEntity config,
      CaptureQueryConditionForm conditionform) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    // String str = getDataQueryHtml(client,
    // "http://dev.10086.cn/datau/modules/views/summary/basic_all.html");
    try {
      int timeQueryType = config.getTimeQueryType();
      if (TimeQueryTypeEnum.EVERY_DAY_IN_REGION.getType() == timeQueryType) {// 区间内的每一天
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(simpleDate.parse(startDate));
        endDay.setTime(simpleDate.parse(endDate));
        List<String> listDay = getListDay(startDay, endDay);
        if (listDay == null) {
          logger.info("enter execute(), but the listDay is null! conditionform=[{}]", conditionform.toString());
          return null;
        }
        List<AppListEnum> appList = AppListEnum.getAppList();
        for (AppListEnum app : appList) {
          for (String day : listDay) {
            String html = "";
            String bizDate = day;
            if (StringUtils.isNotBlank(day)) {
              day = day.replaceAll("-", "");
              String channelCode = "\"" + app.getChannelCode() + "\"";
              html = postByJson(client, config.getQueryUrl(),
                  "{CommandName:'AP_V4.GetAnaChanList',Params:{\"BEGIN_ROW\":1,\"END_ROW\":10,\"DATE_TYPE\":1,\"APP_ID\":" + channelCode + "" + ",\"INT_DAY\":"
                      + day + ",\"ORDER_BY\":\"PAY_USER DESC\",\"AP_ID\":\"3959301\"}}");
            }
            html = html.replaceAll("\\[\\[", "[");
            html = html.replaceAll("\\]\\]", "]");
            html = html.replaceAll("\\],\\[", ",");
            JsonWmQuery wmObj = (JsonWmQuery) JsonParserUtils.parseFromJsonForObject(html, JsonWmQuery.class);
            List<String> rowsList = wmObj.getRows();
            if (rowsList.size() > 0) {
              for (int i = 0; i < rowsList.size() / 17; i++) {
                ScrabData data = new ScrabData();
                int j = i * 17;
                if (StringUtils.isBlank(conditionform.getAppName())) {
                  if (StringUtils.equals(rowsList.get(j + 10), "-999")) {
                    data.setBizAmount("0");
                  } else {
                    data.setBizAmount(rowsList.get(j + 10));
                  }
                  data.setProductName(app.getDesc());
                  data.setProductId(rowsList.get(j + 1));
                  data.setBizDate(bizDate);
                  data.setBizType(config.getBizType());
                  if (StringUtils.isNotBlank(config.getChannelCode())) {
                    data.setChannelId(config.getChannelCode());
                  } else {
                    data.setChannelId(rowsList.get(j + 1));
                  }
                  data.setUserName(conditionform.getUserName());
                  list.add(data);
                } else if (conditionform.getAppName().contains(app.getDesc())) {
                  if (StringUtils.equals(rowsList.get(j + 10), "-999")) {
                    data.setBizAmount("0");
                  } else {
                    data.setBizAmount(rowsList.get(j + 10));
                  }
                  data.setProductName(app.getDesc());
                  data.setProductId(rowsList.get(j + 1));
                  data.setBizDate(bizDate);
                  data.setBizType(config.getBizType());
                  if (StringUtils.isNotBlank(config.getChannelCode())) {
                    data.setChannelId(config.getChannelCode());
                  } else {
                    data.setChannelId(rowsList.get(j + 1));
                  }
                  data.setUserName(conditionform.getUserName());
                  list.add(data);
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("UnifiedCaptureServiceImpl.excute() error with json.. conditionform=[{}]", conditionform.toString());
      logger.error(e.getMessage(), e);
    }
    return list;
  }

  
//乐米2
 public static List<ScrabData> getLm2(CloseableHttpClient client, String startDate, String endDate, CaptureConfigEntity config,
     CaptureQueryConditionForm conditionform) {
   List<ScrabData> list = new ArrayList<ScrabData>();
    String str = getDataQueryHtml(client,
    "http://dev.10086.cn/datau/modules/views/summary/basic_all.html");
   try {
     int timeQueryType = config.getTimeQueryType();
     if (TimeQueryTypeEnum.EVERY_DAY_IN_REGION.getType() == timeQueryType) {// 区间内的每一天
       Calendar startDay = Calendar.getInstance();
       Calendar endDay = Calendar.getInstance();
       startDay.setTime(simpleDate.parse(startDate));
       endDay.setTime(simpleDate.parse(endDate));
       List<String> listDay = getListDay(startDay, endDay);
       if (listDay == null) {
         logger.info("enter execute(), but the listDay is null! conditionform=[{}]", conditionform.toString());
         return null;
       }
       List<AppOceanList> appList = AppOceanList.getAppList();
       for (AppOceanList app : appList) {
         for (String day : listDay) {
           String html = "";
           String bizDate = day;
           if (StringUtils.isNotBlank(day)) {
             day = day.replaceAll("-", "");
             String channelCode = "\"" + app.getChannelCode() + "\"";
             html = postByJson(client, config.getQueryUrl(),
                 "{CommandName:'AP_V4.GetAnaChanList',Params:{\"BEGIN_ROW\":1,\"END_ROW\":100,\"DATE_TYPE\":1,\"APP_ID\":" + channelCode + "" + ",\"INT_DAY\":"
                     + day + ",\"ORDER_BY\":\"PAY_USER DESC\",\"AP_ID\":\"3924024\"}}");
           }
           html = html.replaceAll("\\[\\[", "[");
           html = html.replaceAll("\\]\\]", "]");
           html = html.replaceAll("\\],\\[", ",");
           JsonWmQuery wmObj = (JsonWmQuery) JsonParserUtils.parseFromJsonForObject(html, JsonWmQuery.class);
           List<String> rowsList = wmObj.getRows();
           if (rowsList.size() > 0) {
             for (int i = 0; i < rowsList.size() / 17; i++) {
               ScrabData data = new ScrabData();
               int j = i * 17;
               if (StringUtils.isBlank(conditionform.getAppName())) {
                 boolean state=false;
                 if(StringUtils.isNotBlank(config.getParams())){
                   String ary[]=config.getParams().split(",");
                   for (String string : ary) {
                    if(string.trim().equals(rowsList.get(j + 1))){
                      state=true;
                      break;
                    }
                  }
                 }
                 if(!state){
                   continue;
                 }
                 if (StringUtils.equals(rowsList.get(j + 10), "-999")) {
                   data.setBizAmount("0");
                 } else {
                   data.setBizAmount(rowsList.get(j + 10));
                 }
                 data.setProductName(app.getDesc());
                 data.setProductId(rowsList.get(j + 1));
                 data.setBizDate(bizDate);
                 data.setBizType(config.getBizType());
                 if (StringUtils.isNotBlank(config.getChannelCode())) {
                   data.setChannelId(config.getChannelCode());
                 } else {
                   data.setChannelId(rowsList.get(j + 1));
                 }
                 data.setUserName(conditionform.getUserName());
                 list.add(data);
               } else if (conditionform.getAppName().contains(app.getDesc())) {
                 if (StringUtils.equals(rowsList.get(j + 10), "-999")) {
                   data.setBizAmount("0");
                 } else {
                   data.setBizAmount(rowsList.get(j + 10));
                 }
                 data.setProductName(app.getDesc());
                 data.setProductId(rowsList.get(j + 1));
                 data.setBizDate(bizDate);
                 data.setBizType(config.getBizType());
                 if (StringUtils.isNotBlank(config.getChannelCode())) {
                   data.setChannelId(config.getChannelCode());
                 } else {
                   data.setChannelId(rowsList.get(j + 1));
                 }
                 data.setUserName(conditionform.getUserName());
                 list.add(data);
               }
             }
           }
         }
       }
     }
   } catch (Exception e) {
     logger.error("UnifiedCaptureServiceImpl.excute() error with json.. conditionform=[{}]", conditionform.toString());
     logger.error(e.getMessage(), e);
   }
   return list;
 }
  
  // 掌众
  public static List<ScrabData> getZz(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    String url = entity.getQueryUrl().trim();
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    UrlEncodedFormEntity tity;
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      formparams.add(new BasicNameValuePair("beginDate", startTime));
      formparams.add(new BasicNameValuePair("endDate", endTime));
      formparams.add(new BasicNameValuePair("numPerPage", "20"));
      tity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(tity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> parseList = HtmlParserUtils.getTableZzValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      if (parseList.size() == 20) {
        List<ScrabData> ll = CapturePageServiceImpl.getZzPage(client, startTime, endTime, 2, dataIndex, entity);
        parseList.addAll(ll);
      }
      for (ScrabData scrabData : parseList) {
        if (StringUtils.isNotBlank(entity.getChannelCode())) {
          String ary[] = entity.getChannelCode().split(",");
          for (String string : ary) {
            String code[] = string.split(":");
            if (scrabData.getProductName().contains(code[0])) {
              if (Integer.parseInt(code[2]) == BizTypeEnum.CPA.getCode()) {
                scrabData.setBizAmount(scrabData.getChannelId());
              }
              scrabData.setChannelId(code[1]);
              scrabData.setProductName(code[0]);
              scrabData.setBizType(Integer.parseInt(code[2]));
              break;
            }
          }
        }
        scrabData.setUserName(conditionform.getUserName());
      }
      list.addAll(parseList);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  // 动游
  public static List<ScrabData> getDy(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    try {
      formparams.add(new BasicNameValuePair("customer_id", "30"));
      formparams.add(new BasicNameValuePair("pid", "0"));
      formparams.add(new BasicNameValuePair("cid", "0"));
      formparams.add(new BasicNameValuePair("startdate", startTime));
      formparams.add(new BasicNameValuePair("enddate", endTime));
      formparams.add(new BasicNameValuePair("page", "1"));
      formparams.add(new BasicNameValuePair("rows", "30"));
      formparams.add(new BasicNameValuePair("insert", "CPS"));
      tity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(tity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      JsonDyQuery dyObj = (JsonDyQuery) JsonParserUtils.parseFromJsonForObject(html, JsonDyQuery.class);
      //加上总计，一共是31条
      if (dyObj.getTotal() == 31) {
        List<JsonDyQueryResult> ll = CapturePageServiceImpl.getYdPage(client, startTime, endTime, 2, entity);
        dyObj.getRows().addAll(ll);
      }
      for (JsonDyQueryResult obj : dyObj.getRows()) {
        if (obj.getLogtime().equals("总计")) {
          continue;
        }
        ScrabData scrabData = new ScrabData();
        scrabData.setUserName(conditionform.getUserName());
        scrabData.setBizType(entity.getBizType());
        scrabData.setProductName(obj.getGame());
        scrabData.setChannelId(obj.getCid());
        scrabData.setBizDate(obj.getLogtime());
        scrabData.setBizAmount(obj.getTotalMoney());
        list.add(scrabData);
      }
      dataList.addAll(list);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 搜影
  public static List<ScrabData> getSy(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split(":");
        formparams.add(new BasicNameValuePair(ary[0], ary[1]));
      }
      formparams.add(new BasicNameValuePair("pow", "[2]"));
      formparams.add(new BasicNameValuePair("begin", startTime));
      formparams.add(new BasicNameValuePair("end", endTime));
      tity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(tity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      for (ScrabData scrabData : ll) {
        scrabData.setUserName(entity.getUserName());
        if (StringUtils.isNotBlank(entity.getChannelCode())) {
          scrabData.setChannelId(entity.getChannelCode());
        }
        scrabData.setProductName(entity.getAppName());
        scrabData.setBizType(entity.getBizType());
      }
      dataList.addAll(ll);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 火星
  public static List<ScrabData> getHx(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    try {
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split(",");
        for (String string : ary) {
          String ss[] = string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          formparams.add(new BasicNameValuePair("channelId", "30022"));
          formparams.add(new BasicNameValuePair("startTime", startTime));
          formparams.add(new BasicNameValuePair("stopTime", endTime));
          formparams.add(new BasicNameValuePair("channel_mark", "child_channel"));
          formparams.add(new BasicNameValuePair("subChannelId", ss[1]));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          Type type = new TypeToken<List<JsonHxQuery>>() {
          }.getType();
          List<JsonHxQuery> ll = (List<JsonHxQuery>) JsonParserUtils.parseFromJsonForList(html, type);
          for (JsonHxQuery hx : ll) {
            ScrabData scr = new ScrabData();
            scr.setUserName(entity.getUserName());
            scr.setChannelId(ss[1]);
            scr.setProductName(ss[0]);
            scr.setBizAmount(hx.getRechargeAmount());
            scr.setBizType(entity.getBizType());
            if (StringUtils.isNotBlank(hx.getLogDate())) {
              String bizDate = hx.getLogDate().substring(0, 4) + "-" + hx.getLogDate().substring(5, 7) + "-" + hx.getLogDate().substring(8, 10);
              scr.setBizDate(bizDate);
            }
            list.add(scr);
          }
        }
      }
      dataList.addAll(list);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 幻网
  public static List<ScrabData> getHw(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      formparams.add(new BasicNameValuePair("pager.currentPage", "1"));
      formparams.add(new BasicNameValuePair("pager.limit", "10000"));
      formparams.add(new BasicNameValuePair("startDate", startTime));
      formparams.add(new BasicNameValuePair("endDate", endTime));
      tity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(tity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      for (ScrabData scrabData : ll) {
        scrabData.setUserName(entity.getUserName());
        if (StringUtils.isNotBlank(scrabData.getChannelId())) {
          String channelCode = scrabData.getChannelId().substring(scrabData.getChannelId().indexOf("海") + 1, scrabData.getChannelId().indexOf("&"));
          scrabData.setChannelId(channelCode);
        }
        if (StringUtils.isNotBlank(scrabData.getBizAmount())) {
          String amount = scrabData.getBizAmount().substring(0, scrabData.getBizAmount().indexOf("&"));
          scrabData.setBizAmount(amount);
        }
        scrabData.setProductName(entity.getAppName());
        scrabData.setBizType(entity.getBizType());
      }
      dataList.addAll(ll);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 匠游
  public static List<ScrabData> getJy(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String str = getDataQueryHtml(client, "http://112.124.60.92/admin/Sys_CPS_List.aspx");
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if(StringUtils.isNotBlank(entity.getParams())){
        String ary[]=entity.getParams().trim().split(",");
        for (String string : ary) {
          String ss[]=string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          formparams.add(new BasicNameValuePair("__VIEWSTATE", getValueByType(client, "http://112.124.60.92/admin/Sys_CPS_List.aspx", "__VIEWSTATE", "name")));
          formparams.add(new BasicNameValuePair("__EVENTVALIDATION", getValueByType(client, "http://112.124.60.92/admin/Sys_CPS_List.aspx", "__EVENTVALIDATION",
              "name")));
          formparams.add(new BasicNameValuePair("Uc_Client$DDL", "sy50"));
          formparams.add(new BasicNameValuePair("txtFromDate", startTime));
          formparams.add(new BasicNameValuePair("txtToDate", endTime));
          formparams.add(new BasicNameValuePair("btnSearch", "查询"));
          formparams.add(new BasicNameValuePair("Uc_Kind$DDL", ""));
          formparams.add(new BasicNameValuePair("Uc_Gate$DDL", ""));
          formparams.add(new BasicNameValuePair("Uc_Game$DDL", ss[0]));
          formparams.add(new BasicNameValuePair("ddlOperator", ""));
          formparams.add(new BasicNameValuePair("FileUpload1", ""));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          List<ScrabData> ll = HtmlParserUtils.getTableZzValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
          for (ScrabData scrabData : ll) {
            scrabData.setUserName(entity.getUserName());
            if (StringUtils.isNotBlank(entity.getChannelCode())) {
              scrabData.setChannelId(entity.getChannelCode());
            }
            scrabData.setProductName(ss[1]);
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(ll);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 有缘
  public static List<ScrabData> getYy(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String str = getDataQueryHtml(client, "http://ad.yoyuan.net/third/validuser4third.jsp");
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      String ary[] = startTime.split("-");
      formparams.add(new BasicNameValuePair("year", ary[0]));
      formparams.add(new BasicNameValuePair("month", ary[1]));
      tity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(tity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      for (ScrabData scrabData : ll) {
        scrabData.setUserName(entity.getUserName());
        scrabData.setChannelId(entity.getChannelCode());
        scrabData.setProductName(entity.getAppName());
        scrabData.setBizType(entity.getBizType());
      }
      dataList.addAll(ll);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 威搜游
  public static List<ScrabData> getWsy(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String str = getDataQueryHtml(client, "http://admin.zytcgame.com/loginController.do?login");
    List<ScrabData> list = new ArrayList<ScrabData>();
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    try {
      List<NameValuePair> formparams = new ArrayList<NameValuePair>();
      formparams.add(new BasicNameValuePair("gameName", "All"));
      formparams.add(new BasicNameValuePair("spreadChannel", "All,All"));
      formparams.add(new BasicNameValuePair("subChannel", "All"));
      formparams.add(new BasicNameValuePair("dataDate", startTime + "," + endTime));
      tity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(tity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      JsonWsyQuery wsy = (JsonWsyQuery) JsonParserUtils.parseFromJsonForObject(html, JsonWsyQuery.class);
      List<JsonWsyQueryResult> ll = wsy.getFooter();
      for (JsonWsyQueryResult hx : ll) {
        ScrabData scr = new ScrabData();
        scr.setUserName(entity.getUserName());
        scr.setChannelId(entity.getChannelCode());
        scr.setProductName(entity.getAppName());
        scr.setBizAmount(hx.getBuyReturnCuccess());
        scr.setBizType(entity.getBizType());
        scr.setBizDate(hx.getDataDate());
        list.add(scr);
      }
      dataList.addAll(list);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 威搜游
  public static List<ScrabData> getWsy2(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split(",");
        for (String string : ary) {
          String aa[] = string.split(":");
          formparams.add(new BasicNameValuePair(aa[0], aa[1]));
        }
      }
      formparams.add(new BasicNameValuePair("startDate", startTime));
      formparams.add(new BasicNameValuePair("endDate", endTime));
      tity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(tity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      if (ll.size() == 12) {
        ll.addAll(CapturePageServiceImpl.getWsyPage(client, dataIndex, startTime, endTime, 2, entity));
      }
      for (ScrabData scrabData : ll) {
        scrabData.setUserName(entity.getUserName());
        scrabData.setChannelId(entity.getChannelCode());
        scrabData.setProductName(entity.getAppName());
        scrabData.setBizType(entity.getBizType());
      }
      dataList.addAll(ll);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 点我
  public static List<ScrabData> getDw(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    // String str = getDataQueryHtml(client,
    // "http://www.gameapts.com/site/blank.php");
    String url = entity.getQueryUrl().trim();
    try {
      if (StringUtils.isNotBlank(entity.getParams())) {
        String parAry[] = entity.getParams().trim().split(",");
        for (String string : parAry) {
          String ary[] = string.split(":");
          String ll = url + "?sdate=" + startTime + "&ldate=" + endTime + "&site_id=" + entity.getAppName() + "&game_id=" + ary[0];
          HttpGet httpget = new HttpGet(ll);
          HttpResponse response = client.execute(httpget);
          String html = EntityUtils.toString(response.getEntity());
          System.out.println(html);
          List<ScrabData> list = fromJsonToObj(html);
          for (ScrabData scrabData : list) {
            scrabData.setUserName(entity.getUserName());
            scrabData.setChannelId(entity.getChannelCode());
            scrabData.setProductName(ary[1]);
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(list);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 趣源
  public static List<ScrabData> getQy(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String str = getDataQueryHtml(client, "http://game.joy-river.com:8081/JRGC-cms/main.action");
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split(",");
        for (String string : ary) {
          String aryName[] = string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          formparams.add(new BasicNameValuePair("searchStartDate", startTime));
          formparams.add(new BasicNameValuePair("searchEndDate", endTime));
          formparams.add(new BasicNameValuePair("pageInfo.pageNo", "1"));
          formparams.add(new BasicNameValuePair("pageInfo.orderBy", "date"));
          formparams.add(new BasicNameValuePair("pageInfo.order", "desc"));
          formparams.add(new BasicNameValuePair("filter#EQ#app_name", aryName[1]));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
          if (ll != null && ll.size() == 20) {
            ll.addAll(CapturePageServiceImpl.getQyPage(client, dataIndex, startTime, endTime, 2, entity, aryName[1]));
          }
          for (ScrabData scrabData : ll) {
            scrabData.setUserName(entity.getUserName());
            scrabData.setChannelId(aryName[0]);
            if (aryName[1].contains(entity.getAppName())) {
              scrabData.setProductName(entity.getAppName());
            } else {
              scrabData.setProductName(aryName[1]);
            }
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(ll);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 天旭汇丰
  public static List<ScrabData> getTxfh(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String str = getDataQueryHtml(client, "http://game.joy-river.com:8081/JRGC-cms/main.action");
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split(",");
        for (String string : ary) {
          String aryName[] = string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          formparams.add(new BasicNameValuePair("starttime", startTime));
          formparams.add(new BasicNameValuePair("endtime", endTime));
          formparams.add(new BasicNameValuePair("pid", aryName[1]));
          formparams.add(new BasicNameValuePair("numPerPage", "10000"));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
          for (ScrabData scrabData : ll) {
            scrabData.setUserName(entity.getUserName());
            scrabData.setChannelId(aryName[0]);
            scrabData.setProductName(aryName[2]);
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(ll);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 风酷
  public static List<ScrabData> getFk(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split(",");
        for (String string : ary) {
          String aryName[] = string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          formparams.add(new BasicNameValuePair("querySubmit", "提交"));
          formparams.add(new BasicNameValuePair("selectQudao", "0"));
          formparams.add(new BasicNameValuePair("selectGame", aryName[1]));
          formparams.add(new BasicNameValuePair("date_from", startTime));
          formparams.add(new BasicNameValuePair("date_to", endTime));
          formparams.add(new BasicNameValuePair("orderBy", "0"));
          formparams.add(new BasicNameValuePair("orderSequence", "0"));
          formparams.add(new BasicNameValuePair("queryType", "0"));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          List<ScrabData> ll = HtmlParserUtils.getTableZzValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
          for (ScrabData scrabData : ll) {
            scrabData.setUserName(entity.getUserName());
            scrabData.setChannelId(aryName[0]);
            scrabData.setProductName(entity.getAppName());
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(ll);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 酷宇
  public static List<ScrabData> getKy(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String str = getDataQueryHtml(client, "http://info.coeeland.com/bss/home/menu.aspx");
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split(",");
        for (String string : ary) {
          String aryName[] = string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          formparams.add(new BasicNameValuePair("__EVENTTARGET", ""));
          formparams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
          formparams.add(new BasicNameValuePair("__LASTFOCUS", ""));
          formparams.add(new BasicNameValuePair("txtSrhStartTime", startTime));
          formparams.add(new BasicNameValuePair("txtSrhEndTime", endTime));
          formparams.add(new BasicNameValuePair("__VIEWSTATE", getValueByType(client, url, "__VIEWSTATE", "name")));
          formparams.add(new BasicNameValuePair("__EVENTVALIDATION", getValueByType(client, url, "__EVENTVALIDATION", "name")));
          formparams.add(new BasicNameValuePair("ddlProductName", aryName[1]));
          formparams.add(new BasicNameValuePair("ddlFacName", "100179"));
          formparams.add(new BasicNameValuePair("ddlChannelName", "-100"));
          formparams.add(new BasicNameValuePair("ddlAreaName", "上海"));
          formparams.add(new BasicNameValuePair("CheckBox1", "on"));
          formparams.add(new BasicNameValuePair("CheckBox2", "on"));
          formparams.add(new BasicNameValuePair("btnSrh", "查询"));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
          for (ScrabData scrabData : ll) {
            scrabData.setUserName(entity.getUserName());
            scrabData.setChannelId(aryName[0]);
            scrabData.setProductName(entity.getAppName());
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(ll);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 奇葩
  public static List<ScrabData> getQp(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split(",");
        for (String string : ary) {
          String ss[]=string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          String month = startTime.substring(0, startTime.lastIndexOf("-"));
          formparams.add(new BasicNameValuePair("month", month));
          formparams.add(new BasicNameValuePair("chanId", ss[0]));
          formparams.add(new BasicNameValuePair("Submit4", "查询"));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
          for (ScrabData scrabData : ll) {
            scrabData.setUserName(entity.getUserName());
            scrabData.setChannelId(ss[0]);
            scrabData.setProductName(ss[1]);
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(ll);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 开迅
  public static List<ScrabData> getKx(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String url = entity.getQueryUrl().trim();
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      url += "?channelId=&begin_time=" + startTime + "&end_time=" + endTime;
      HttpGet httppost = new HttpGet(url);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      for (ScrabData scrabData : ll) {
        scrabData.setUserName(entity.getUserName());
        if (StringUtils.isNotBlank(entity.getChannelCode())) {
          scrabData.setChannelId(entity.getChannelCode());
        }
        scrabData.setProductName(entity.getAppName());
        scrabData.setBizType(entity.getBizType());
      }
      dataList.addAll(ll);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 开迅2
  public static List<ScrabData> getKx2(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String url = entity.getQueryUrl().trim();
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      url += "?date=&start=" + startTime + "&end=" + endTime;
      HttpGet httppost = new HttpGet(url);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      for (ScrabData scrabData : ll) {
        scrabData.setUserName(entity.getUserName());
        scrabData.setChannelId(entity.getChannelCode());
        scrabData.setProductName(entity.getAppName());
        scrabData.setBizType(entity.getBizType());
      }
      dataList.addAll(ll);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 乐畅
  public static List<ScrabData> getLc(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String url = entity.getQueryUrl().trim();
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      HttpGet httppost = new HttpGet(url);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      List<ScrabData> list = new ArrayList<ScrabData>();
      for (ScrabData scrabData : ll) {
        if (!compareDay(scrabData.getBizDate(), startTime, endTime)) {
          continue;
        }
        scrabData.setUserName(entity.getUserName());
        scrabData.setChannelId(entity.getChannelCode());
        scrabData.setProductName(entity.getAppName());
        scrabData.setBizType(entity.getBizType());
        list.add(scrabData);
      }
      dataList.addAll(list);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 炫亮
  public static List<ScrabData> getXl(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split(",");
        for (String string : ary) {
          String ss[] = string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          String dd[] = startTime.split("-");
          formparams.add(new BasicNameValuePair("years", dd[0]));
          formparams.add(new BasicNameValuePair("month", dd[1]));
          formparams.add(new BasicNameValuePair("customerName", ss[0]));
          formparams.add(new BasicNameValuePair("button_query", ""));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
          for (ScrabData scrabData : ll) {
            scrabData.setUserName(entity.getUserName());
            scrabData.setChannelId(entity.getChannelCode());
            scrabData.setProductName(ss[1]);
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(ll);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 乐米
  public static List<ScrabData> getLm(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String str = getDataQueryHtml(client, "http://114.215.129.247:8088/lemipay/indexAction.do");
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      List<NameValuePair> formparams = new ArrayList<NameValuePair>();
      formparams.add(new BasicNameValuePair("startDate", startTime));
      formparams.add(new BasicNameValuePair("endDate", endTime));
      formparams.add(new BasicNameValuePair("productName", "雄霸天下Q传单机版"));
      tity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(tity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      for (ScrabData scrabData : ll) {
        scrabData.setUserName(entity.getUserName());
        scrabData.setChannelId(entity.getChannelCode());
        scrabData.setProductName(entity.getAppName());
        scrabData.setBizType(entity.getBizType());
      }
      dataList.addAll(ll);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 同娱
  public static List<ScrabData> getTy(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String str = getDataQueryHtml(client, "http://115.28.55.233/acl/main.action");
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().split(",");
        for (String string : ary) {
          String ss[] = string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          String month = startTime.substring(0, startTime.lastIndexOf("-"));
          formparams.add(new BasicNameValuePair("page.pageNo", "1"));
          formparams.add(new BasicNameValuePair("page.orderBy", "statDate,income"));
          formparams.add(new BasicNameValuePair("page.order", "desc,desc"));
          formparams.add(new BasicNameValuePair("filter_LIKES_appName", ss[0].trim()));
          formparams.add(new BasicNameValuePair("statDateByMonth", month));
          formparams.add(new BasicNameValuePair("dateFormat", "2"));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          List<ScrabData> ll = HtmlParserUtils.getTableZzValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
          for (ScrabData scrabData : ll) {
            scrabData.setUserName(entity.getUserName());
            scrabData.setChannelId(ss[2]);
            scrabData.setProductName(ss[1]);
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(ll);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 快乐玩
  public static List<ScrabData> getKlw(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String url = entity.getQueryUrl().trim();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().split(",");
        for (String string : ary) {
          String ss[] = string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          formparams.add(new BasicNameValuePair("startDate", startTime));
          formparams.add(new BasicNameValuePair("endDate", endTime));
          formparams.add(new BasicNameValuePair("op", ss[0].trim()));
          formparams.add(new BasicNameValuePair("appId", ss[0].trim()));
          formparams.add(new BasicNameValuePair("channel", ss[2].trim()));
          formparams.add(new BasicNameValuePair("submit", "查询"));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          List<ScrabData> ll = HtmlParserUtils.getTableZzValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
          for (ScrabData scrabData : ll) {
            scrabData.setUserName(entity.getUserName());
            scrabData.setChannelId(ss[2].trim());
            scrabData.setProductName(ss[1].trim());
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(ll);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 空中网
  public static List<ScrabData> getKzw(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String url = "";
    UrlEncodedFormEntity tity;
    try {
      Calendar startDay = Calendar.getInstance();
      Calendar endDay = Calendar.getInstance();
      startDay.setTime(simpleDate.parse(startTime));
      endDay.setTime(simpleDate.parse(endTime));
      List<String> listDay = getListDay(startDay, endDay);
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split("&&");
        for (String string : ary) {
          String ss[] = string.split(",");
          if (ss[0].equals(String.valueOf(BizTypeEnum.CPA.getCode()))) {
            url = entity.getQueryUrl().trim();
            for (String date : listDay) {
              List<NameValuePair> formparams = new ArrayList<NameValuePair>();
              formparams.add(new BasicNameValuePair("beginDate", date));
              formparams.add(new BasicNameValuePair("endDate", date));
              formparams.add(new BasicNameValuePair("sumdate", ""));
              formparams.add(new BasicNameValuePair("gametype", ""));
              formparams.add(new BasicNameValuePair("cgid", ""));
              formparams.add(new BasicNameValuePair("service", ""));
              formparams.add(new BasicNameValuePair("GROUPTYPE", "6"));
              formparams.add(new BasicNameValuePair("sys", "%6"));
              formparams.add(new BasicNameValuePair("device", "%1"));
              formparams.add(new BasicNameValuePair("version", "%9"));
              formparams.add(new BasicNameValuePair("ptn", "%4"));
              formparams.add(new BasicNameValuePair("topAmountvalu", "0.10"));
              formparams.add(new BasicNameValuePair("empl_code", "xiazhiyi"));
              formparams.add(new BasicNameValuePair("flag", "UI"));
              formparams.add(new BasicNameValuePair("isRedis", "false"));
              tity = new UrlEncodedFormEntity(formparams, "UTF-8");
              HttpPost httppost = new HttpPost(url);
              httppost.setEntity(tity);
              HttpResponse response = client.execute(httppost);
              String html = EntityUtils.toString(response.getEntity());
              KzwQuery kzwQuery = (KzwQuery) JsonParserUtils.parseFromJsonForObject(html, KzwQuery.class);
              List<KzwQueryResult> list = kzwQuery.getRows();
              for (KzwQueryResult kzwQueryResult : list) {
                ScrabData scrabData = new ScrabData();
                scrabData.setBizDate(date);
                scrabData.setBizAmount(kzwQueryResult.getCredituser());
                scrabData.setBizType(Integer.parseInt(ss[0]));
                scrabData.setUserName(entity.getUserName());
                for (int i = 1; i < ss.length; i++) {
                  String code[] = ss[i].split(":");
                  if (code[0].equals(kzwQueryResult.getGroupType())) {
                    scrabData.setChannelId(code[1]);
                    scrabData.setProductName(code[2]);
                    dataList.add(scrabData);
                    break;
                  }
                }
              }
            }
          } else {
            url = entity.getQueryPageUrl().trim();
            for (int ii = 1; ii < ss.length; ii++) {
              String code[] = ss[ii].trim().split(":");
              for (String date : listDay) {
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("beginDate", date));
                formparams.add(new BasicNameValuePair("endDate", date));
                formparams.add(new BasicNameValuePair("ordprefix", ""));
                formparams.add(new BasicNameValuePair("grouptype", "5"));
                formparams.add(new BasicNameValuePair("cpids", code[0]));
                tity = new UrlEncodedFormEntity(formparams, "UTF-8");
                HttpPost httppost = new HttpPost(url);
                httppost.setEntity(tity);
                HttpResponse response = client.execute(httppost);
                String html = EntityUtils.toString(response.getEntity());
                KzwSdkQuery kzwQuery = (KzwSdkQuery) JsonParserUtils.parseFromJsonForObject(html, KzwSdkQuery.class);
                List<KzwSdkQueryResult> list = kzwQuery.getRows();
                for (KzwSdkQueryResult kzwQueryResult : list) {
                  ScrabData scrabData = new ScrabData();
                  scrabData.setBizDate(date);
                  scrabData.setBizAmount(kzwQueryResult.getCredituser());
                  scrabData.setBizType(Integer.parseInt(ss[0]));
                  scrabData.setUserName(entity.getUserName());
                  for (int j = 1; j < code.length; j++) {
                    String cc[] = code[j].trim().split("&");
                    if (cc[0].equals(kzwQueryResult.getGroupType())) {
                      scrabData.setChannelId(cc[1]);
                      scrabData.setProductName(cc[2]);
                      dataList.add(scrabData);
                      break;
                    }
                  }
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  // 创斯林
  public static List<ScrabData> getCsl(CloseableHttpClient client, String startTime, String endTime, CaptureConfigEntity entity,
      CaptureQueryConditionForm conditionform, List<ScrabData> dataList) {
    String str = getDataQueryHtml(client, "http://www.zsyj.com.cn/main.do");
    String url = entity.getQueryUrl();
    UrlEncodedFormEntity tity;
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      if (StringUtils.isNotBlank(entity.getParams())) {
        String ary[] = entity.getParams().trim().split(",");
        for (String string : ary) {
          String ss[] = string.split(":");
          List<NameValuePair> formparams = new ArrayList<NameValuePair>();
          formparams.add(new BasicNameValuePair("startDate", startTime));
          formparams.add(new BasicNameValuePair("endDate", endTime));
          formparams.add(new BasicNameValuePair("payType", ""));
          formparams.add(new BasicNameValuePair("provider", ""));
          formparams.add(new BasicNameValuePair("payTypeFather",""));
          formparams.add(new BasicNameValuePair("appId", ss[0].trim()));
          formparams.add(new BasicNameValuePair("channelId", ss[1].trim()));
          tity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(url);
          httppost.setEntity(tity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }
          List<ScrabData> ll = HtmlParserUtils.getTableZzValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
          for (ScrabData scrabData : ll) {
            scrabData.setUserName(entity.getUserName());
            scrabData.setChannelId(ss[3].trim());
            scrabData.setProductName(ss[2].trim());
            scrabData.setBizType(entity.getBizType());
          }
          dataList.addAll(ll);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  public static List<ScrabData> fromJsonToObj(String xml) {
    List<ScrabData> ll = new ArrayList<ScrabData>();
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    // 通过 解析器 工厂 创建 一个 解析 器
    try {
      DocumentBuilder db = dbf.newDocumentBuilder();
      InputStream iStream = new ByteArrayInputStream(xml.getBytes());
      Document dm = db.parse(iStream);
      // 得到 所有 row节点
      NodeList persons = dm.getElementsByTagName("row");
      for (int i = 0; i < persons.getLength(); i++) {
        ScrabData obj = new ScrabData();
        Element personElement = (Element) persons.item(i);
        String bizDate = personElement.getAttribute("id");
        NodeList nodeList = personElement.getElementsByTagName("cell");
        Element ele = (Element) nodeList.item(4);
        String amount = ele.getLastChild().toString();
        String bizAmount = "";
        if (StringUtils.isNotBlank(amount)) {
          bizAmount = amount.trim().split(":")[1];
          bizAmount = bizAmount.substring(0, bizAmount.indexOf("]"));
        }
        obj.setBizAmount(bizAmount.trim());
        obj.setBizDate(bizDate);
        ll.add(obj);
      }
    } catch (Exception e) {
      logger.error("xml is error");
    }
    return ll;
  }

  public static String getValueByType(CloseableHttpClient httpClient, String url, String value, String type) {
    HttpGet httpget = new HttpGet(url);
    HttpResponse response;
    String str = "";
    try {
      response = httpClient.execute(httpget);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      str = HtmlParserUtils.getInputTagValues(html, type, value);
    } catch (Exception e) {
      logger.error("getToken is error");
    }
    return str;
  }

  public static boolean compareDay(String day, String startDay, String endDay) {
    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    Calendar cal3 = Calendar.getInstance();
    try {
      cal1.setTime(sim.parse(startDay));
      cal2.setTime(sim.parse(endDay));
      cal3.setTime(sim.parse(day));
      if (cal3.compareTo(cal2) <= 0 && cal3.compareTo(cal1) >= 0) {
        return true;
      }
    } catch (ParseException e) {
      logger.error("compare day is error");
    }
    return false;
  }

  public static String getSelectValue(CloseableHttpClient client, String url, String jsonParam) {
    StringEntity entity;
    try {
      entity = new StringEntity(jsonParam);
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(entity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(httpEntity);
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      return html;
    } catch (Exception e) {
      logger.error("error in postByJson,e=[{}]", e);
    }
    return null;
  }

  public static String postByJson(CloseableHttpClient client, String url, String jsonParam) {
    StringEntity entity;
    try {
      entity = new StringEntity(jsonParam);
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(entity);
      HttpResponse response = client.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(httpEntity);
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      return html;
    } catch (Exception e) {
      logger.error("error in postByJson,e=[{}]", e);
    }
    return null;
  }

  private static List<String> getListDay(Calendar startDay, Calendar endDay) {
    List<String> list = new ArrayList<String>();
    // 开始日期-1，结束日期+1，这样才能取到开始日期和结束日期
    startDay.add(Calendar.DAY_OF_YEAR, -1);
    endDay.add(Calendar.DAY_OF_YEAR, 1);
    // 给出的日期开始日比结束日期大
    if (startDay.compareTo(endDay) >= 0) {
      return null;
    }
    Calendar currentPrintDay = startDay;
    while (true) {
      // 日期加一
      currentPrintDay.add(Calendar.DATE, 1);
      // 判断是否到达结束日期
      if (currentPrintDay.compareTo(endDay) == 0) {
        break;
      }
      list.add(simpleDate.format(currentPrintDay.getTime()));
    }
    return list;
  }
  
  public static void main(String[] args) {
	  //String str = getDataQueryHtml(client, "http://www.mdouvip.com/youku");
}
}
