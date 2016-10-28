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

import slh.capture.common.CompanyEnum;
import slh.capture.common.ConstantsCMP;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HttpFactory;
import slh.capture.common.QueryParamEnum;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.ScrabData;
import slh.capture.service.IRelianScrabService;
import edu.hziee.common.lang.DateUtil;

/**
 * 艾瑞斯
 * 
 * @author ck
 * 
 */
@Service("relianService")
public class RelianScrabServiceImpl extends ScrabServiceImpl implements IRelianScrabService {
  private final static Logger logger = LoggerFactory.getLogger(RelianScrabServiceImpl.class);

  @Autowired
  private ICapturePlatDAO     capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {
    List<ScrabData> list = new ArrayList<ScrabData>();

    // 查询艾瑞斯的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.RELIAN.getCompanyName());
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

      Map<String, Object> customParamMap = new HashMap<String, Object>();
      customParamMap.put("url", capturePlat.getQueryUrl());
      customParamMap.put("fromDate", DateUtil.parseDate(startDate, DateUtil.TRADITION_PATTERN));
      customParamMap.put("toDate", DateUtil.parseDate(endDate, DateUtil.TRADITION_PATTERN));

      customParamMap.put("table_attr", "class");
      customParamMap.put("table_attr_value", "report");

      // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
      Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
      dataIndex.put(0, DataNameEnum.BIZ_DATE);
      dataIndex.put(2, DataNameEnum.CHANNEL_ID);
      dataIndex.put(3, DataNameEnum.BIZ_AMOUNT);
      customParamMap.put("column_index", dataIndex);

      List<ScrabData> ls = exstractData(httpClient, genParamMap(), customParamMap);

      for (ScrabData scrabData : ls) {
        scrabData.setProductName("想恋爱");
        scrabData.setBizType(ConstantsCMP.TYPE_CPA);
      }

      if (ls != null) {
        list.addAll(ls);
      }

    }

    return list;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[2];
    loginKeys[0] = "username";
    loginKeys[1] = "password";
    return loginKeys;
  }

  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 中手游数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "fromDate");
    paramMap.put(QueryParamEnum.END_TIME, "toDate");
    return paramMap;
  }

}
