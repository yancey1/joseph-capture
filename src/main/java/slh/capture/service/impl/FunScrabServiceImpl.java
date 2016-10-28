package slh.capture.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import slh.capture.common.BizTypeEnum;
import slh.capture.common.CapturePlatEnum;
import slh.capture.common.DataNameEnum;
import slh.capture.common.JsonParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.domain.ScrabData;
import slh.capture.domain.fun.FunChannel;
import slh.capture.service.IFunScrabService;

import com.google.gson.reflect.TypeToken;

import edu.hziee.common.lang.DateUtil;

@Service("funuService")
public class FunScrabServiceImpl extends ScrabServiceImpl implements IFunScrabService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @SuppressWarnings("unchecked")
  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    String[] loginKeys = genLoginKeys();
    String[] loginValues = genLoginValues(imgCode);
    boolean loginRet = getLoginHttpClient(client, CapturePlatEnum.FUNU.getLoginUrl(), loginKeys, loginValues);
    if (!loginRet) {
      logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
      return null;
    }

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("channelId", "30022");
    String jsonChannel = getChannelIdByJsonPost(client, CapturePlatEnum.FUNU.getQueryPageUrl(), paramMap);
    List<FunChannel> channelList = null;
    if (jsonChannel != null) {
      Type type = new TypeToken<List<FunChannel>>() {
      }.getType();
      channelList = (List<FunChannel>) JsonParserUtils.parseFromJsonForList(jsonChannel, type);
    }
    List<ScrabData> dataList = new ArrayList<ScrabData>();
    if (channelList != null) {
      for (FunChannel ch : channelList) {
        List<ScrabData> ls = exstractData(client, genParamMap(), genCustomParamMap(startDate, endDate, null, ch.getSubChannelId()));
        // 写入渠道号
        for (ScrabData scrabData : ls) {
          scrabData.setBizType(BizTypeEnum.CPS.getCode());
          scrabData.setBizDate(scrabData.getBizDate().substring(0, 10));
        }
        dataList.addAll(ls);
      }
    }
    return dataList;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[2];
    loginKeys[0] = "account";
    loginKeys[1] = "password";
    return loginKeys;
  }

  @Override
  public String[] genLoginValues(String imgCode) {

    String[] loginValues = new String[2];
    loginValues[0] = CapturePlatEnum.FUNU.getUserName();
    loginValues[1] = CapturePlatEnum.FUNU.getPassword();
    return loginValues;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 中手游数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "startTime");
    paramMap.put(QueryParamEnum.END_TIME, "stopTime");
    return paramMap;
  }

  @Override
  public Map<String, Object> genCustomParamMap(String startTime, String endTime, String productId, String subChannelId) {
    Map<String, Object> customParamMap = new HashMap<String, Object>();
    customParamMap.put("url", CapturePlatEnum.FUNU.getQueryUrl());
    customParamMap.put("startTime", DateUtil.parseDate(startTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("stopTime", DateUtil.parseDate(endTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("channelId", "30022");
    customParamMap.put("subChannelId", subChannelId);
    customParamMap.put("table_attr", "class");
    customParamMap.put("table_attr_value", "tb_datalist");
    customParamMap.put("channel_mark", "child_channel");

    // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    dataIndex.put(0, DataNameEnum.PRODUCT);
    dataIndex.put(1, DataNameEnum.BIZ_DATE);
    dataIndex.put(2, DataNameEnum.CHANNEL_ID);
    dataIndex.put(7, DataNameEnum.BIZ_AMOUNT);
    customParamMap.put("column_index", dataIndex);
    return customParamMap;
  }
}
