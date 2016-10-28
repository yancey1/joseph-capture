package slh.capture.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.CapturePlatEnum;
import slh.capture.common.CompanyEnum;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.ScrabData;
import slh.capture.service.IZhenShunXingScrabService;
import edu.hziee.common.lang.DateUtil;

/**
 * 中手游数据拉取
 * 
 * @author ck
 * 
 */
@Service("zhenShunXingService")
public class ZhenShunXingScrabServiceImpl extends ScrabServiceImpl implements IZhenShunXingScrabService {

  private Logger          logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    List<ScrabData> list = new ArrayList<ScrabData>();

    // 查询古川的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.ZHENSHUNXING.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);

    for (CapturePlat capturePlat : capturePlats) {
      String[] loginKeys = genLoginKeys();

      String[] loginValues = new String[2];
      loginValues[0] = capturePlat.getUserName();
      loginValues[1] = capturePlat.getPassword();
      boolean loginRet = getLoginHttpClient(client, capturePlat.getLoginUrl(), loginKeys, loginValues);
      if (!loginRet) {
        logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
        return null;
      }
      String queryPageHtml = getDataQueryHtml(client, CapturePlatEnum.CMGE.getQueryPageUrl());
      List<String> productList = HtmlParserUtils.getSelectTagValues(queryPageHtml, "name", "search.projectId");// 产品ID

      List<ScrabData> dataList = new ArrayList<ScrabData>();
      if (productList != null) {
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

      list.addAll(dataList);
    }

    return list;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[2];
    loginKeys[0] = "loginId";
    loginKeys[1] = "password";
    return loginKeys;
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
