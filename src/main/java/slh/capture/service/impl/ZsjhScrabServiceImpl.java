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
import slh.capture.common.QueryParamEnum;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.ScrabData;
import slh.capture.service.IZsjhScrabService;
import edu.hziee.common.lang.DateUtil;

/**
 * 掌上极浩数据拉取
 * 
 * @author ck
 * 
 */
@Service("zsjhScrabService")
public class ZsjhScrabServiceImpl extends ScrabServiceImpl implements IZsjhScrabService {

  private Logger          logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    List<ScrabData> list = new ArrayList<ScrabData>();

    // 查询古川的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.ZSJH.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);

    for (CapturePlat capturePlat : capturePlats) {
      String[] loginKeys = genLoginKeys();
      String[] loginValues = genLoginValues(capturePlat.getUserName(), capturePlat.getPassword());
      boolean loginRet = getLoginHttpClient(client, capturePlat.getLoginUrl(), loginKeys, loginValues);
      if (!loginRet) {
        logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
        return null;
      }

      List<ScrabData> ls = exstractData(client, genParamMap(), genCustomParamMap(startDate, endDate, capturePlat.getQueryUrl(), null));

      for (ScrabData scrabData : ls) {
        scrabData.setBizType(ConstantsCMP.TYPE_CPS);
        scrabData.setChannelId(capturePlat.getChannelCode());
      }

      list.addAll(ls);

    }

    return list;
  }
  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[2];
    loginKeys[0] = "account";
    loginKeys[1] = "password";
    return loginKeys;
  }

  public String[] genLoginValues(String userName, String userPassword) {

    String[] loginValues = new String[2];
    loginValues[0] = userName;
    loginValues[1] = userPassword;
    return loginValues;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 掌上极浩数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "startTime");
    paramMap.put(QueryParamEnum.END_TIME, "endTime");
    paramMap.put(QueryParamEnum.CHANNEL_ID, "cid");
    paramMap.put(QueryParamEnum.PAGENO, "currentPage");
    paramMap.put(QueryParamEnum.CHNAME, "account");
    paramMap.put(QueryParamEnum.PAGESIZE, "pageSize");
    return paramMap;
  }

  public Map<String, Object> genCustomParamMap(String startTime, String endTime, String url, String channelId) {
    Map<String, Object> customParamMap = new HashMap<String, Object>();
    customParamMap.put("url", url);
    customParamMap.put("startTime", DateUtil.parseDate(startTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("endTime", DateUtil.parseDate(endTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("pageSize", "1000");
    customParamMap.put("currentPage", "1");
    customParamMap.put("account", "1");
    customParamMap.put("cid", "744");

    customParamMap.put("table_attr", "class");
    customParamMap.put("table_attr_value", "table table-striped table-bordered table-condensed");

    // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    dataIndex.put(0, DataNameEnum.BIZ_DATE);
    dataIndex.put(1, DataNameEnum.PRODUCT);
    dataIndex.put(4, DataNameEnum.BIZ_AMOUNT);
    customParamMap.put("column_index", dataIndex);
    return customParamMap;
  }

}
