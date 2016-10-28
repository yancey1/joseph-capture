package slh.capture.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
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
import slh.capture.service.ISoulGameScrabService;
import edu.hziee.common.lang.DateUtil;

/**
 * 灵游数据拉取
 * 
 * @author ck
 * 
 */
@Service("soulGameScrabService")
public class SoulGameScrabServiceImpl extends ScrabServiceImpl implements ISoulGameScrabService {

  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    List<ScrabData> list = new ArrayList<ScrabData>();

    // 查询灵游的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.SOULGAME.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);

    for (CapturePlat capturePlat : capturePlats) {

      CloseableHttpClient httpClient = HttpFactory.createHttpClient();

      Map<String, Object> customParamMap = new HashMap<String, Object>();
      customParamMap.put("url", capturePlat.getQueryUrl());
      customParamMap.put("chName", capturePlat.getUserName());
      customParamMap.put("chPasswd", capturePlat.getPassword());
      customParamMap.put("beginTime", DateUtil.parseDate(startDate, DateUtil.TRADITION_PATTERN));
      customParamMap.put("endTime", DateUtil.parseDate(endDate, DateUtil.TRADITION_PATTERN));
      customParamMap.put("SelectTest", "why");
      customParamMap.put("secType", 2);

      customParamMap.put("table_attr", "cellpadding");
      customParamMap.put("table_attr_value", "3");

      // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
      Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
      dataIndex.put(0, DataNameEnum.BIZ_DATE);
      dataIndex.put(1, DataNameEnum.PRODUCT);
      dataIndex.put(3, DataNameEnum.BIZ_AMOUNT);
      customParamMap.put("column_index", dataIndex);

      List<ScrabData> ls = exstractData(httpClient, genParamMap(), customParamMap);

      for (ScrabData scrabData : ls) {
        scrabData.setChannelId(capturePlat.getChannelCode());
        scrabData.setBizType(ConstantsCMP.TYPE_CPS);
      }

      if (ls != null) {
        list.addAll(ls);
      }

    }

    return list;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 中手游数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "beginTime");
    paramMap.put(QueryParamEnum.END_TIME, "endTime");
    paramMap.put(QueryParamEnum.CHNAME, "chName");
    paramMap.put(QueryParamEnum.CHPASSWORD, "chPassword");
    paramMap.put(QueryParamEnum.SELECTTEST, "SelectTest");
    paramMap.put(QueryParamEnum.SECTYPE, "secType");
    return paramMap;
  }

}
