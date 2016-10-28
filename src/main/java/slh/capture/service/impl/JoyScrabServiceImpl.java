/**
 * 真趣页面处理service类
 * lingmincc
 */
package slh.capture.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import slh.capture.common.CapturePlatEnum;
import slh.capture.common.ConstantsCMP;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.domain.ScrabData;
import slh.capture.service.IJoyScrabService;
import edu.hziee.common.lang.DateUtil;

@Service("joyScrabService")
public class JoyScrabServiceImpl extends ScrabServiceImpl implements IJoyScrabService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    String[] loginKeys = genLoginKeys();
    String[] loginValues = genLoginValues(imgCode);
    boolean loginRet = getLoginHttpClient(client, CapturePlatEnum.JOY.getLoginUrl(), loginKeys, loginValues);
    if (!loginRet) {
      logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
      return null;
    }

    List<ScrabData> dataList = new ArrayList<ScrabData>();
    String queryPageHtml = getDataQueryHtml(client, CapturePlatEnum.JOY.getQueryPageUrl());
    List<String> productList = HtmlParserUtils.getSelectTagValues(queryPageHtml, "name", "appId");
    List<String> channelList = HtmlParserUtils.getSelectTagValues(queryPageHtml, "name", "channelId");
    if (productList != null) {
      int index = 0;
      for (String prd : productList) {
        if (StringUtils.isEmpty(prd) || StringUtils.equals(prd, "null")) {
          continue;
        }
        logger.info("No.[{}] product", index++);
        for (String chan : channelList) {
          if (StringUtils.isEmpty(chan) || StringUtils.equals(chan, "null")) {
            continue;
          }
          dataList.addAll(exstractData(client, genParamMap(), genCustomParamMap(startDate, endDate, prd, chan)));
        }
      }

    }

    // 获取美眉基地的数据

    String queryMMPageHtml = getDataQueryHtml(client, CapturePlatEnum.JOY_MM.getQueryPageUrl());
    List<String> productMMList = HtmlParserUtils.getSelectTagValues(queryMMPageHtml, "name", "query.appId");
    List<String> channelMMList = HtmlParserUtils.getSelectTagValues(queryMMPageHtml, "name", "query.channelId");
    List<ScrabData> mmList = new ArrayList<ScrabData>();
    if (productMMList != null) {
      int index = 0;
      for (String prd : productMMList) {
        if (StringUtils.isEmpty(prd) || StringUtils.equals(prd, "null")) {
          continue;
        }
        logger.info("No.[{}] product", index++);
        for (String chan : channelMMList) {
          if (StringUtils.isEmpty(chan) || StringUtils.equals(chan, "null")) {
            continue;
          }
          mmList = exstractData(client, genMMParamMap(), genMMCustomParamMap(startDate, endDate, prd, chan));

          for (ScrabData scrabData : mmList) {
            scrabData.setChannelId(chan);
            scrabData.setBizType(ConstantsCMP.TYPE_CPS);
          }

          dataList.addAll(mmList);
        }
      }

    }

    return dataList;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[3];
    loginKeys[0] = "userName";
    loginKeys[1] = "password";
    loginKeys[2] = "verificationCode";
    return loginKeys;
  }

  @Override
  public String[] genLoginValues(String imgCode) {

    String[] loginValues = new String[3];
    loginValues[0] = CapturePlatEnum.JOY.getUserName();
    loginValues[1] = CapturePlatEnum.JOY.getPassword();
    loginValues[2] = imgCode;
    return loginValues;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 真趣数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "startTime");
    paramMap.put(QueryParamEnum.END_TIME, "endTime");
    paramMap.put(QueryParamEnum.PRODUCT_ID, "appId");
    paramMap.put(QueryParamEnum.CHANNEL_ID, "channelId");
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

}
