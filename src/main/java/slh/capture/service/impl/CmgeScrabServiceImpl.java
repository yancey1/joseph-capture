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
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.domain.ScrabData;
import slh.capture.service.ICmgeScrabService;
import edu.hziee.common.lang.DateUtil;

/**
 * 中手游数据拉取
 * 
 * @author ck
 * 
 */
@Service("cmgeScrabService")
public class CmgeScrabServiceImpl extends ScrabServiceImpl implements ICmgeScrabService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    String[] loginKeys = genLoginKeys();
    String[] loginValues = genLoginValues(imgCode);
    boolean loginRet = getLoginHttpClient(client, CapturePlatEnum.CMGE.getLoginUrl(), loginKeys, loginValues);
    if (!loginRet) {
      logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
      return null;
    }
    String queryPageHtml = getDataQueryHtml(client, CapturePlatEnum.CMGE.getQueryPageUrl());
    List<String> productList = HtmlParserUtils.getSelectTagValues(queryPageHtml, "name", "search.projectId");// 产品ID

    if (productList != null) {
      List<ScrabData> dataList = new ArrayList<ScrabData>();
      int index = 0;
      for (String prd : productList) {
        if (StringUtils.isEmpty(prd) || StringUtils.equals(prd, "null")) {
          continue;
        }
        logger.info("No.[{}] product", index++);

        List<ScrabData> ls = exstractData(client, genParamMap(), genCustomParamMap(startDate, endDate, prd, null));

        // 写入渠道号
        for (ScrabData scrabData : ls) {
          scrabData.setChannelId(prd);
        }

        dataList.addAll(ls);
      }
      return dataList;
    }
    return null;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[2];
    loginKeys[0] = "loginId";
    loginKeys[1] = "password";
    return loginKeys;
  }

  @Override
  public String[] genLoginValues(String imgCode) {

    String[] loginValues = new String[2];
    loginValues[0] = CapturePlatEnum.CMGE.getUserName();
    loginValues[1] = CapturePlatEnum.CMGE.getPassword();
    return loginValues;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 中手游数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "beginDate");
    paramMap.put(QueryParamEnum.END_TIME, "endDate");
    return paramMap;
  }

  @Override
  public Map<String, Object> genCustomParamMap(String startTime, String endTime, String productId, String channelId) {
    Map<String, Object> customParamMap = new HashMap<String, Object>();
    customParamMap.put("url", CapturePlatEnum.CMGE.getQueryUrl());
    customParamMap.put("beginDate", DateUtil.parseDate(startTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("endDate", DateUtil.parseDate(endTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("search.projectId", productId);
    customParamMap.put("search.dateType", "0");
    customParamMap.put("table_attr", "class");
    customParamMap.put("table_attr_value", "tb_datalist");
    customParamMap.put("ajax", "true");
    customParamMap.put("search.cooperateMode", "2");
    customParamMap.put("search.rowFields", "1,14,10");

    // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    dataIndex.put(0, DataNameEnum.BIZ_DATE);
    dataIndex.put(1, DataNameEnum.PRODUCT);
    dataIndex.put(2, DataNameEnum.BIZ_TYPE);
    dataIndex.put(4, DataNameEnum.BIZ_AMOUNT);
    customParamMap.put("column_index", dataIndex);
    return customParamMap;
  }

}
