package slh.capture.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.BizTypeEnum;
import slh.capture.common.CompanyEnum;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.HttpFactory;
import slh.capture.common.QueryParamEnum;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.ScrabData;
import slh.capture.service.IYGAppScrabService;
import edu.hziee.common.lang.DateUtil;

/**
 * 移告-17App应用联盟
 * 
 * @author jinlingmin
 * 
 */
@Service("ygScrabService")
public class YGScrabServiceImpl extends ScrabServiceImpl implements IYGAppScrabService {

  private Logger          logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    List<ScrabData> dataList = new ArrayList<ScrabData>();

    // 查询移告的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.YG.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);
    String[] loginKeys = genLoginKeys();
    for (CapturePlat capturePlat : capturePlats) {

      CloseableHttpClient httpClient = HttpFactory.createHttpClient();

      String[] loginValues = new String[2];
      loginValues[0] = capturePlat.getUserName();
      loginValues[1] = capturePlat.getPassword();

      boolean loginRet = getLoginHttpClient(httpClient, capturePlat.getLoginUrl(), loginKeys, loginValues);
      if (!loginRet) {
        logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
        return null;
      }

      String queryPageHtml = getDataQueryHtml(httpClient, capturePlat.getQueryPageUrl());
      List<String> productList = HtmlParserUtils.getSelectTagValues(queryPageHtml, "name", "gamelist");
      if (productList != null && !productList.isEmpty()) {
        for (String prd : productList) {
          if (prd.lastIndexOf("_") == -1) {
            continue;
          }
          String realPrd = prd.substring(0, prd.lastIndexOf("_"));

          Map<String, Object> customParamMap = new HashMap<String, Object>();
          customParamMap.put("url", capturePlat.getQueryUrl());
          customParamMap.put("tbStartDate", DateUtil.parseDate(startDate, DateUtil.TRADITION_PATTERN));
          customParamMap.put("tbEndDate", DateUtil.parseDate(endDate, DateUtil.TRADITION_PATTERN));
          customParamMap.put("txtGid", realPrd);
          customParamMap.put("balanceState", "-1");

          // 17App联盟后台查询结果table属性
          customParamMap.put("table_attr", "id");
          customParamMap.put("table_attr_value", "MyView");

          // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
          Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
          dataIndex.put(5, DataNameEnum.BIZ_DATE);
          dataIndex.put(1, DataNameEnum.PRODUCT);
          dataIndex.put(4, DataNameEnum.BIZ_AMOUNT);
          customParamMap.put("column_index", dataIndex);

          List<ScrabData> scrabDataList = exstractData(httpClient, genParamMap(), customParamMap);
          if (scrabDataList != null) {
            for (ScrabData data : scrabDataList) {
              data.setBizType(BizTypeEnum.CPA.getCode());
              data.setChannelId(capturePlat.getChannelCode());
              dataList.add(data);
            }
          }
        }
      }
    }
    return dataList;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 17App联盟数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "tbStartDate");
    paramMap.put(QueryParamEnum.END_TIME, "tbEndDate");
    return paramMap;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[2];
    loginKeys[0] = "tb1";
    loginKeys[1] = "tb2";
    return loginKeys;
  }
}
