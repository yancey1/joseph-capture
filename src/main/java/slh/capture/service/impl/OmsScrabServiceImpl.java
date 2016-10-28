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
import slh.capture.service.IOmsScrabService;
import edu.hziee.common.lang.DateUtil;

/**
 * 破晓数据拉取
 * 
 * @author ck
 * 
 */
@Service("omsScrabService")
public class OmsScrabServiceImpl extends ScrabServiceImpl implements IOmsScrabService {

  private Logger          logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    List<ScrabData> list = new ArrayList<ScrabData>();

    // 查询古川的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.OMS.getCompanyName());
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
        scrabData.setBizType(ConstantsCMP.TYPE_CPA);
        scrabData.setProductName("赢奖品炸金花");
      }

      list.addAll(ls);

    }

    return list;
  }
  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[2];
    loginKeys[0] = "userName";
    loginKeys[1] = "userPassword";
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
    // 破晓数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "selectDate_start");
    paramMap.put(QueryParamEnum.END_TIME, "selectDate_end");
    return paramMap;
  }

  public Map<String, Object> genCustomParamMap(String startTime, String endTime, String url, String channelId) {
    Map<String, Object> customParamMap = new HashMap<String, Object>();
    customParamMap.put("url", url);
    customParamMap.put("selectDate_start", DateUtil.parseDate(startTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("selectDate_end", DateUtil.parseDate(endTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("page", 1);
    customParamMap.put("rows", 1000);
    customParamMap.put("table_attr", "style");
    customParamMap.put("table_attr_value", "tb_datalist");

    // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    dataIndex.put(0, DataNameEnum.BIZ_DATE);
    dataIndex.put(1, DataNameEnum.CHANNEL_ID);
    dataIndex.put(4, DataNameEnum.BIZ_AMOUNT);
    customParamMap.put("column_index", dataIndex);
    return customParamMap;
  }

}
